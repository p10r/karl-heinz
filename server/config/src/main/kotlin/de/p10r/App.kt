package de.p10r

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(scanBasePackageClasses = [RoomConfig::class])
@EnableConfigurationProperties(RoomConfigurationProperties::class)
class ConfigApplication

fun main(args: Array<String>) {
    runApplication<ConfigApplication>(*args)
}
