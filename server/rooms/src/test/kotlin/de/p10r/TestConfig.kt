package de.p10r

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(RoomConfigurationProperties::class)
class TestConfig: RoomConfig()