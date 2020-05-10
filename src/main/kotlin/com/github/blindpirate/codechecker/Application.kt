package com.github.blindpirate.codechecker

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.blindpirate.codechecker.PullRequestAction.OPENED
import com.github.blindpirate.codechecker.PullRequestAction.REOPENED
import com.github.blindpirate.codechecker.PullRequestAction.SYNCHRONIZE
import com.github.blindpirate.codechecker.model.PullRequestGitHubEvent
import com.github.blindpirate.codechecker.model.PullRequestWithReviewThreads
import com.google.common.collect.Multimap
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import javax.servlet.http.HttpServletRequest

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

val objectMapper = ObjectMapper()
val client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_1_1)
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build()
val gitHubToken = "Bearer ${System.getenv("GITHUB_ACCESS_TOKEN") ?: throw IllegalArgumentException("Must set GITHUB_ACCESS_TOKEN!")}"
val tokenUser = "hcsp-bot"


enum class PullRequestAction {
    ASSIGNED, UNASSIGNED, LABELED, UNLABELED, OPENED, EDITED, CLOSED, REOPENED, SYNCHRONIZE, READY_FOR_REVIEW, LOCKED, UNLOCKED, REVIEW_REQUESTED;

    companion object {
        fun of(value: String) = valueOf(value.toUpperCase())
    }
}

@Controller
class HelloController @Autowired constructor(val gitHubUnifiedDiffParser: GitHubUnifiedDiffParser) {
    private val acceptedActions = listOf(OPENED, SYNCHRONIZE, REOPENED)

    @PostMapping("/github")
    @ResponseBody
    fun index(request: HttpServletRequest, @RequestBody body: String): String {
        val header = request.getHeader("X-GitHub-Event")
        if (header != null) {
            parseHeaderAndProcessEvent(header, body)
        }

        return "success"
    }

    private fun parseHeaderAndProcessEvent(header: String, body: String) {
        if ("pull_request" == header) {
            val event = objectMapper.readValue(body, PullRequestGitHubEvent::class.java)
            if (PullRequestAction.of(event.action) !in acceptedActions) {
                println("Skip pull request action: ${event.action}")
            } else {
                processPullRequest(event)
            }
        } else {
            println("Discard non-pull-request event")
        }
    }

    private fun processPullRequest(event: PullRequestGitHubEvent) {
        processPullRequest(event.repository.owner.login,
            event.repository.name,
            event.pullRequest.number.toInt(),
            event.pullRequest.head.sha)
    }

    fun processPullRequest(owner: String, name: String, number: Int, headCommit: String) {
        val pr: PullRequestWithReviewThreads = doQueryOrMutation(PullRequestWithReviewThreads::class.java, getPullRequestQuery(owner, name, number))
        val diffs = parseUnifiedDiff(owner, name, number)

        var commentCounter = 0
        // Only parse non-deleted file
        diffs.forEach { (fileRelativePath: String, realLineToPositionMap: Map<Int, Int>) ->
            val file = downloadChangedFile(pr, owner, name, headCommit, fileRelativePath)
            val issues = runCheckstyle(file)
            println("Run checkstyle on $file, found ${issues.values().size} issues")
            commentCounter += commentIfNotCommentedYet(pr, headCommit, fileRelativePath, realLineToPositionMap, issues.get(file.absolutePath))
        }

        if (commentCounter != 0) {
            submitReview(pr.data.repository.pullRequest.id)
        }
    }


    /**
     * Comment on the pull request, return comment number.
     */
    private fun commentIfNotCommentedYet(pr: PullRequestWithReviewThreads,
                                         headCommit: String,
                                         fileRelativePath: String,
                                         realLineToPositionMap: Map<Int, Int>,
                                         messages: Collection<LocalizedMessage>): Int {
        if (messages.isEmpty()) {
            return 0
        }

        var counter = 0
        LocalizedMessage.setLocale(Locale.CHINESE)
        val lineNumberToMessageMap = messages.groupBy { it.lineNo }.map { entry ->
            entry.key to entry.value.joinToString("\n") { "`${it.message}`" } // backquote to avoid markdown rendering
        }

        lineNumberToMessageMap.forEach { (lineNumber: Int, message: String) ->
            // Comment on the file lines, if it's not commented yet.
            if (!realLineToPositionMap.containsKey(lineNumber)) {
                println("$fileRelativePath:$lineNumber not found in diff, skip.")
            } else if (pr.data.repository.pullRequest.reviewThreads.nodes.any { reviewThread ->
                    reviewThread.comments.nodes.any { it.path == fileRelativePath && reviewThread.line == lineNumber }
                }) {
                println("$fileRelativePath:$lineNumber is already commented, skip.")
            } else {
                comment(pr.data.repository.pullRequest.id,
                    headCommit,
                    fileRelativePath,
                    realLineToPositionMap.getValue(lineNumber),
                    message)
                counter++
            }
        }
        return counter
    }

    private fun runCheckstyle(file: File): Multimap<String, LocalizedMessage> {
        LocalizedMessage.setLocale(Locale.CHINESE)
        val listener = MyListener()
        CodeFormatChecker().runCheckstyle("/checkstyle.xml", listOf(file), listener)
        return listener.fileToErrorsMap
    }

    private fun downloadChangedFile(pr: PullRequestWithReviewThreads, prRepoOwner: String, prRepoName: String, prHeadCommit: String, relativePath: String): File {
        val headRepoWithOwner = pr.data.repository.pullRequest.headRepository?.nameWithOwner ?: "$prRepoOwner/$prRepoName"

        val rawUrl = "https://raw.githubusercontent.com/$headRepoWithOwner/$prHeadCommit/${relativePath}"
        val targetFile = Files.createTempFile("", "${prRepoOwner}_${prRepoName}_${prHeadCommit}_${relativePath.replace('/', '_')}").toFile()
        saveTo(targetFile, rawUrl)
        return targetFile
    }

    /**
     * Parse a unified diff, return [relativePath -> first hunk line number]
     */
    private fun parseUnifiedDiff(owner: String, name: String, number: Int): Map<String, Map<Int, Int>> {
        val diffUrl = "https://patch-diff.githubusercontent.com/raw/${owner}/${name}/pull/$number.diff"
        val targetFile = Files.createTempFile("", "${owner}_${name}_${number}.diff").toFile()
        saveTo(targetFile, diffUrl)
        return gitHubUnifiedDiffParser.parseDiff(targetFile)
    }

}

private fun saveTo(targetFile: File, rawUrl: String) {
    val request: /*@@nwpymm@@*/HttpRequest = HttpRequest.newBuilder()
        .uri(URI.create(rawUrl))
        .header("Authorization", gitHubToken)
        .GET()
        .build()
    val response: HttpResponse<Path> = client.send(request, HttpResponse.BodyHandlers.ofFile(targetFile.toPath()))
    println("${response.statusCode()} on $rawUrl")
}

private fun comment(pullRequestId: String, commit: String, path: String, position: Int, commentBody: String): Map<*, *> =
    doQueryOrMutation(Map::class.java, addPullRequestReviewComment(pullRequestId, commit, path, position, commentBody))

private fun submitReview(pullRequestId: String): Map<*, *> =
    doQueryOrMutation(Map::class.java, submitPullRequestReview(pullRequestId))

fun addPullRequestReviewComment(pullRequestId: String, commit: String, path: String, position: Int, commentBody: String) = """
mutation { 
  addPullRequestReviewComment(input:{pullRequestId: "$pullRequestId",commitOID:"$commit",path:"$path",position:$position,body:${objectMapper.writeValueAsString(commentBody)}}) {
    clientMutationId
    comment {
      author { login }
      body
    }
  }
}  
        """.replace('\n', ' ')

fun submitPullRequestReview(pullRequestId: String) = """
mutation { 
  submitPullRequestReview(input:{pullRequestId: "$pullRequestId", event: COMMENT}) {
    clientMutationId
  }
}  
        """.replace('\n', ' ')


private
fun toQueryJson(query: String) = objectMapper.writeValueAsString(mapOf("query" to query))

private
fun <T> doQueryOrMutation(klass: Class<T>, queryString: String): T {
    val request: /*@@nwpymm@@*/HttpRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://api.github.com/graphql"))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .header("Authorization", gitHubToken)
        .POST(HttpRequest.BodyPublishers.ofString(toQueryJson(queryString)))
        .build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    println("${response.statusCode()}: ${response.body()}")
    require(response.statusCode() in 200..299)
    return objectMapper.readValue(response.body(), klass)
}

fun getPullRequestQuery(owner: String, name: String, number: Int) =
    """
query{
  repository(owner: "$owner", name: "$name") {
    pullRequest(number: $number) {
      id
      databaseId
      headRepository {
        nameWithOwner
      }
      reviewThreads(first: 50) {
        nodes {
          comments(first: 50) {
            nodes {
              body
              author {
                login
              }
              commit {
                oid
              }
              path
            }
          }
          line
          originalLine
          originalStartLine
          startLine
          startDiffSide
        }
      }
      files(first: 50) {
        edges {
          node {
            path
          }
        }
      }
    }
  }
}

    """.trimIndent()