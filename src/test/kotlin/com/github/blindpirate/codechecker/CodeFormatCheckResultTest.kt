package com.github.blindpirate.codechecker

import com.github.blindpirate.fixtures.ResourceLoaderFixture
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CodeFormatCheckResultTest : ResourceLoaderFixture {
    private val codeChecker = CodeFormatChecker()

    @Test
    fun `dont warn Chinese character`() {
        val result = codeChecker.runCheckstyle(getResource("code-containing-chinese-char.java"))
        result.asMap().forEach { (file, messages) ->
            println("$file\n ")
            println(messages.joinToString(" \n") { it.message })
        }
        Assertions.assertTrue(result.isEmpty)
    }

    @TestFactory
    fun test(): Iterable<DynamicTest> {
        val badFormatCodeDir = getResourceDir("/bad-format-code")
        val goodFormatCodeDir = getResourceDir("/good-format-code")

        return badFormatCodeDir.list { _, name -> name.endsWith(".java") }
            .map { toDynamicTest(it, badFormatCodeDir, goodFormatCodeDir) }
    }

    private fun toDynamicTest(javaFileName: String,
                              badFormatCodeDir: File,
                              goodFormatCodeDir: File): DynamicTest =
        DynamicTest.dynamicTest("Check $badFormatCodeDir/$javaFileName") {
//            val badCodeLines = File(badFormatCodeDir, javaFileName).readTrimmedLines()
//            val goodCodeLines = File(goodFormatCodeDir, javaFileName).readTrimmedLines()

//            val checkResults = codeChecker.check(badCodeLines)
        }

    // Remove ending spaces but not leading spaces
    private fun File.readTrimmedLines(): List<String> {
        Assertions.assertTrue(this.isFile) { "$this must be file!" }
        return readLines().map { it.trimEnd() }
    }
}