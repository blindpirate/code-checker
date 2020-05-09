package com.github.blindpirate.fixtures

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.blindpirate.PullRequestReviewThreadResponse
import com.github.blindpirate.PullRequestReviewThreadResponse.DataBean.RepositoryBean.PullRequestsBean
import com.github.blindpirate.PullRequestReviewThreadResponse.DataBean.RepositoryBean.PullRequestsBean.EdgesBean.NodeBean.ReviewThreadsBean
import com.github.blindpirate.RepositoryQueryResponse
import com.github.blindpirate.codechecker.CodeFormatIssueType
import java.io.File
import java.net.InetSocketAddress
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Path


// Scan github.com/hcsp repositories
// Find out code with comments:
//   - if/else后面不加花括号是不好的实践
//   - 请养成写完代码按一下格式化快捷键将代码格式化的习惯
// Then save it to src/test/resources/bad-format-code
//
fun main() {
//    BadCodeFetcher().saveBadCodeOfRepo(
//        File("src/test/resources/bad-format-code"),
//        "hcsp",
//        "symmetric-string"
//    )
    BadCodeFetcher().saveBadCode(File("src/test/resources/bad-format-code"))
}


class BadCodeFetcher {
    val objectMapper: ObjectMapper = ObjectMapper()
    val client = HttpClient.newBuilder()
        .proxy(ProxySelector.of(InetSocketAddress("localhost", 8888)))
        .version(HttpClient.Version.HTTP_1_1)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    fun saveBadCode(dir: File) {
        val repos = getAllRepo("hcsp")
        repos.forEach {
            saveBadCodeOfRepo(dir, "hcsp", it)
            println("Done: $it")
        }
    }

    fun saveBadCodeOfRepo(targetDir: File, repoOwner: String, repoName: String) {
        getPullRequestsOfRepo(repoOwner, repoName).forEach { pr ->
            pr.node.reviewThreads.nodes.forEach { reviewThread ->
                saveBadCodeIfNecessary(targetDir,
                    repoOwner, repoName,
                    pr,
                    reviewThread,
                    CodeFormatIssueType.MISSING_CURLY_BRACKETS,
                    "后面不加花括号是不好的实践")
                saveBadCodeIfNecessary(targetDir,
                    repoOwner, repoName,
                    pr,
                    reviewThread,
                    CodeFormatIssueType.MISSING_SPACES,
                    "请养成写完代码按一下格式化快捷键将代码格式化的习惯")
                saveBadCodeIfNecessary(targetDir,
                    repoOwner, repoName,
                    pr,
                    reviewThread,
                    CodeFormatIssueType.UNUSED_IMPORT,
                    "这是什么")
            }
        }
    }

    fun getPullRequestsOfRepo(repoOwner: String, repoName: String): List<PullRequestsBean.EdgesBean> {
        val cacheFile = File("$repoOwner-$repoName-prs.json")
        if (cacheFile.isFile) {
            return objectMapper.readValue(cacheFile, object : TypeReference<List<PullRequestsBean.EdgesBean>>() {
            })
        }
        val result = mutableListOf<PullRequestsBean.EdgesBean>()
        var currentPage = doQuery(PullRequestReviewThreadResponse::class.java,
            getPullRequestQuery(repoOwner, repoName))

        while (currentPage.data.repository.pullRequests.edges.isNotEmpty()) {
            result.addAll(currentPage.data.repository.pullRequests.edges)


            val lastCursor = currentPage.data.repository.pullRequests.edges.last().cursor
            currentPage = doQuery(PullRequestReviewThreadResponse::class.java,
                getPullRequestQuery(repoOwner, repoName, cursor = lastCursor))
        }
        cacheFile.writeText(objectMapper.writeValueAsString(result))
        return result
    }

    private fun saveBadCodeIfNecessary(targetDir: File,
                                       prRepoOwner: String,
                                       prRepoName: String,
                                       pr: PullRequestsBean.EdgesBean,
                                       reviewThread: ReviewThreadsBean.NodesBeanXX,
                                       type: CodeFormatIssueType,
                                       content: String) {
        if (reviewThread.comments.nodes.any { it.body!!.contains(content) }) {
            val lineNumber = reviewThread.line
            val filePath = reviewThread.comments.nodes[0].pullRequest.files.nodes[0].path
            val commit = reviewThread.comments.nodes[0].commit.oid
            val headRepoWithOwner = pr.node.headRepository?.nameWithOwner ?: "$prRepoOwner/$prRepoName"
            val rawUrl = "https://raw.githubusercontent.com/$headRepoWithOwner/$commit/$filePath"
            val targetFileName = "${type}_${prRepoOwner}_${prRepoName}_${pr.node.number}_${filePath.replace('/', '_')}"
            val targetFile = File(targetDir, targetFileName)
            val targetFileMetadata = File(targetDir, "$targetFileName.properties")
            saveTo(targetFile, rawUrl)
            targetFileMetadata.writeText("line=$lineNumber\n")
        }
    }


    private fun saveTo(targetFile: File, rawUrl: String) {
        if (targetFile.isFile) {
            println("Ignore existing $targetFile")
            return
        }
        val request: /*@@nwpymm@@*/HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(rawUrl))
            .header("Authorization", "Bearer ${System.getenv("HCSP_MANAGER_GITHUB_TOKEN")}")
            .GET()
            .build()
        val response: HttpResponse<Path> = client.send(request, BodyHandlers.ofFile(targetFile.toPath()))
        println("${response.statusCode()} on $rawUrl")
    }

    private
    fun <T> doQuery(klass: Class<T>, queryString: String): T {
        val request: /*@@nwpymm@@*/HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/graphql"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer ${System.getenv("HCSP_MANAGER_GITHUB_TOKEN")}")
            .POST(BodyPublishers.ofString(toQueryJson(queryString)))
            .build()
        val response: HttpResponse<String> = client.send(request, BodyHandlers.ofString())
        println("${response.statusCode()}: ${response.body()}")
        require(response.statusCode() in 200..299)
        return objectMapper.readValue(response.body(), klass)
    }

    private
    fun toQueryJson(query: String) = objectMapper.writeValueAsString(mapOf("query" to query))

    private fun getAllRepo(org: String): List<String> {
        val cacheFile = File("$org-repos.json")
        if (cacheFile.isFile) {
            return objectMapper.readValue(cacheFile, List::class.java) as List<String>
        }
        val result = mutableListOf<String>()
        var currentPage = doQuery(RepositoryQueryResponse::class.java, getRepoQuery(org))

        while (!currentPage.data.organization.repositories.edges.isEmpty()) {
            result.addAll(currentPage.data.organization.repositories.edges.map { it.node.name })
            val lastCursor = currentPage.data.organization.repositories.edges.last().cursor
            currentPage = doQuery(RepositoryQueryResponse::class.java, getRepoQuery(org, cursor = lastCursor))
        }
        cacheFile.writeText(objectMapper.writeValueAsString(result))
        return result
    }
}

/*
query {
  organization(login: "hcsp") {
    repositories(first: 100) {
      edges {
        cursor
        node {
          id
          name
        }
      }
    }
  }
}

query {
  organization(login: "hcsp") {
    repositories(after: "Y3Vyc29yOnYyOpHOCn6qIQ==", first: 100) {
      edges {
        cursor
        node {
          id
          name
        }
      }
    }
  }
}


 */

fun getPullRequestQuery(owner: String, name: String, cursor: String = "") =
    """
query{
  repository(owner: "$owner", name: "$name") {
    pullRequests(first: 100 ${afterCondition(cursor)}) {
      edges {
        cursor
        node {
          number
          reviewThreads(first: 10) {
            nodes {
              comments(first: 10) {
                nodes {
                  body
                  author {
                    login
                  }
                  commit {
                    oid
                  }
                  pullRequest {
                    files(first: 10) {
                      nodes {
                        path
                      }
                    }
                  }
                }
              }
              line
              originalLine
              originalStartLine
              startLine
              startDiffSide
            }
          }
        }
      }
    }
  }
} 
    """.trimIndent()

fun afterCondition(cursor: String) = if (cursor == "") "" else ", after: \"$cursor\""

fun getRepoQuery(org: String, cursor: String = "") =
    """
        query {
          organization(login: "$org") {
            repositories(first: 100 ${afterCondition(cursor)}) {
              edges {
                cursor
                node {
                  id
                  name
                }
              }
            }
          }
        }
    """.replace('\n', ' ').trimIndent()

