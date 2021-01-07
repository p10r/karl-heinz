package de.p10r.internal

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.p10r.Room
import de.p10r.RoomConfigurationProperties
import de.p10r.createLogger
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

interface Rooms {
    fun save(room: Room): Room
    fun findBy(id: String): Room?
    fun findAll(): List<Room>
    fun delete(room: Room)
}

class RoomsFileRepository(properties: RoomConfigurationProperties, private val mapper: ObjectMapper) : Rooms {
    val log = createLogger()

    private val file = File(properties.filePath)

    init {
        log.info("Room json will be located at: ${properties.filePath}")
        if (file.exists().not()) Files.createFile(Path.of(properties.filePath))
    }

    private val content
        get() = if (file.readText().trim().isEmpty()) "[]" else file.readText()

    private val persistedRooms: List<Room>
        get() = mapper.readValue(content)

    override fun save(room: Room): Room = (persistedRooms.dropWhile { it.id == room.id } + room)
        .persist()
        .let { room }

    override fun findBy(id: String): Room? = persistedRooms.find { it.id == id }

    override fun findAll(): List<Room> = persistedRooms

    override fun delete(room: Room) {
        persistedRooms.dropWhile { it.id == room.id }
            .persist()
    }

    private fun List<Room>.persist() {
        file.writeText(mapper.writeValueAsString(this))
    }
}