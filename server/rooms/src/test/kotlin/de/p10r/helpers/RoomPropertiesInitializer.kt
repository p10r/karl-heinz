package de.p10r.helpers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource

import org.springframework.web.server.adapter.WebHttpHandlerBuilder.applicationContext

import org.springframework.core.env.ConfigurableEnvironment




class RoomPropertiesInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val props = mapOf("rooms.file-path" to "test.room.json")
        val testcontainers = MapPropertySource("roomproperties", props)

        context.environment.propertySources.addFirst(testcontainers)
    }
}