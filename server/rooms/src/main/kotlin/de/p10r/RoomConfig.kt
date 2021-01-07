package de.p10r

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.p10r.internal.Service
import de.p10r.internal.Rooms
import de.p10r.internal.RoomsFileRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
class RoomConfig {
    @Bean
    internal fun objectMapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

    @Bean
    internal fun repo(props: RoomConfigurationProperties, mapper: ObjectMapper) = RoomsFileRepository(props, mapper)

    @Bean
    internal fun service(rooms: Rooms) = Service(rooms)

    @Bean
    internal fun controller(service: Service) = Controller(service)
}

@ConstructorBinding
@ConfigurationProperties(prefix = "rooms")
class RoomConfigurationProperties(val filePath: String)

fun <T: Any> T.createLogger(): Logger = LoggerFactory.getLogger(this.javaClass)
