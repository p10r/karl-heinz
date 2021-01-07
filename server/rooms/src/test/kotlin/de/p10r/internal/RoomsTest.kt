package de.p10r.internal

import com.fasterxml.jackson.module.kotlin.readValue
import de.p10r.Room
import de.p10r.RoomConfig
import de.p10r.RoomConfigurationProperties
import de.p10r.helpers.TestDataFactory
import de.p10r.helpers.TestFile
import io.kotest.assertions.asClue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class RoomsTest {
    private val mapper = RoomConfig().objectMapper()
    private val testFile = TestFile()
    private val testProps = RoomConfigurationProperties(testFile.path)
    private val rooms: Rooms = RoomsFileRepository(testProps, mapper)

    @Test
    fun `creates a new file if the file under the given path does not exist`() {
        rooms
        File(testFile.path).exists() shouldBe true
    }

    @Test
    fun `should save a room`() {
        val expectedRoom = TestDataFactory.roomWithoutUuid

        rooms.save(expectedRoom)

        val actualSavedRooms: List<Room> = mapper.readValue(testFile.content)


        actualSavedRooms.asClue {
            it.size shouldBe 1
            it.first() shouldBe expectedRoom
        }
    }

    @Test
    fun `should find a room by id`() {
        val existingRoom = TestDataFactory.roomWithUuid.also { persist(listOf(it)) }

        val result = rooms.findBy(existingRoom.id!!)

        result?.name shouldBe existingRoom.name
    }

    @Test
    fun `should return null when id can't be found`() {
        val result = rooms.findBy("::unknownId::")

        result shouldBe null
    }

    @Test
    fun `should edit an already persisted room`() {
        val expectedRoom = TestDataFactory.roomWithUuid.also { persist(listOf(it, TestDataFactory.roomWithUuid)) }
        val changedRoom = expectedRoom.copy(name = "changed")

        rooms.save(changedRoom)

        val actualSavedRooms: List<Room> = mapper.readValue(testFile.content)

        actualSavedRooms shouldContain changedRoom
    }

    @Test
    fun `should find all saved rooms`() {
        val expectedRooms = listOf(
            TestDataFactory.roomWithUuid,
            TestDataFactory.roomWithUuid,
            TestDataFactory.roomWithUuid
        ).also { persist(it) }

        val actualRooms: List<Room> = rooms.findAll()

        actualRooms shouldContainAll expectedRooms
    }

    @Test
    fun `should return an empty list if there are no saved rooms`() {
        rooms.findAll() shouldBe emptyList()
    }

    @Test
    fun `deletes a room`() {
        val persistedRoom = TestDataFactory.roomWithUuid.also { persist(listOf(it)) }

        rooms.delete(persistedRoom)

        val actualRooms: List<Room> = mapper.readValue(testFile.content)

        actualRooms shouldBe emptyList()
    }

    @BeforeEach
    fun cleanDb() {
        testFile.write("")
    }

    private fun persist(Room: List<Room>) {
        testFile.write(mapper.writeValueAsString(Room))
    }

    @AfterAll
    fun removeTestFile() {
        testFile.delete()
    }
}
