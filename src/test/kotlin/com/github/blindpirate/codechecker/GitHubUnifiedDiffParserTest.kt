package com.github.blindpirate.codechecker

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.blindpirate.fixtures.ResourceLoaderFixture
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GitHubUnifiedDiffParserTest : ResourceLoaderFixture {
    private val parser = GitHubUnifiedDiffParser()

    @TestFactory
    fun test(): Iterable<DynamicTest> {
        val githubDiffDir = getResourceDir("github-diffs")

        return githubDiffDir.listFiles { _, name -> name.endsWith(".diff") }
            .map(this::toDynamicTest)
    }

    private fun toDynamicTest(diffFile: File): DynamicTest {
        return DynamicTest.dynamicTest("Parse ${diffFile.name} succeeds") {
            val expectedResultJson = File(diffFile.parentFile, diffFile.name.replace(".diff", ".json"))
            val expectedResultMap: Map<String, Map<String, Int>> = ObjectMapper().readValue(expectedResultJson, object : TypeReference<Map<String, Map<String, Int>>>() {})
            val parserResult = parser.parseDiff(diffFile)

            expectedResultMap.forEach { fileRelativePath: String, realLineToPositionMap: Map<String, Int> ->
                realLineToPositionMap.forEach { realLineNum: String, position: Int ->
                    Assertions.assertEquals(position, parserResult.getValue(fileRelativePath)[realLineNum.toInt()])
                }
            }
        }
    }
}