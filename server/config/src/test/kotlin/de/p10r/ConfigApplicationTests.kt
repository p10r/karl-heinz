package de.p10r

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

//@SpringBootTest
class ConfigApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun `yes`() {

        val path: String = System.getProperty("user.dir").also { println(it) }
    }


}
