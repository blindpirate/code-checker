package com.github.blindpirate.fixtures

import org.junit.jupiter.api.Assertions
import java.io.File

interface ResourceLoaderFixture {
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getResourceDir(path: String): File =
        File(javaClass.classLoader.getResource(path).toURI()).also {
            Assertions.assertTrue(it.isDirectory) { "$it must be directory!" }
        }

    fun getResource(path: String): File =
        File(javaClass.classLoader.getResource(path).toURI())
}