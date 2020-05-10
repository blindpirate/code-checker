package com.github.blindpirate.codechecker

import com.github.stkent.githubdiffparser.GitHubDiffParser
import com.github.stkent.githubdiffparser.models.Diff
import com.github.stkent.githubdiffparser.models.Line
import org.springframework.stereotype.Service
import java.io.File

@Service
class GitHubUnifiedDiffParser {
    /**
     * [key: file relative path
     *  value: [
     *            key: real code line number in right side
     *            value: position since first @@ hunk header, see https://developer.github.com/v3/pulls/comments/#create-a-comment
     *                   https://stackoverflow.com/questions/41662127/how-to-comment-on-a-specific-line-number-on-a-pr-on-github
     *                   <pre>
     *                       Note: The position value equals the number of lines down from the first "@@" hunk header in the file you want to add a comment.
     *                       The line just below the "@@" line is position 1, the next line is position 2, and so on.
     *                       The position in the diff continues to increase through lines of whitespace and additional hunks until the beginning of a new file.
     *                   </pre>
     *         ]
     *  ]
     *
     *  Deleted files and non-java files are discarded.
     */
    fun parseDiff(diff: File): Map<String, Map<Int, Int>> {
        return GitHubDiffParser().parse(diff)
            .filter { it.toFileName != "/dev/null" }
            .filter { it.toFileName.endsWith(".java") }
            .map {
                it.toFileName to toFileLineNumberToPositionMap(it)
            }.toMap()
    }

    private fun toFileLineNumberToPositionMap(diff: Diff): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()
        var position = -1
        diff.hunks.forEach { hunk ->
            // When starting a new hunk, position++
            // @@ -23,6 +28,24 @@ public String getName() {
            position++
            var toFileRealLineNumber = hunk.toFileRange.lineStart - 1
            hunk.lines.forEach { line ->
                position++
                if (line.lineType == Line.LineType.NEUTRAL || line.lineType == Line.LineType.TO) {
                    toFileRealLineNumber++
                    result.put(toFileRealLineNumber, position)
                }
            }
        }
        return result
    }
}