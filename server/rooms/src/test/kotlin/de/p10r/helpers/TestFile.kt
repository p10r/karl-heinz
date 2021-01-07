package de.p10r.helpers

import java.io.File

class TestFile(givenPath: String? = null) {
    val path = givenPath ?: "src/test/resources/$randomString.json"
    private val testFile = File(path)

    val content
        get() = testFile.readText()

    fun write(string: String) {
        testFile.writeText(string)
    }

    fun delete() {
        testFile.delete()
    }
}
