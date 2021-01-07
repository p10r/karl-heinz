package de.p10r

import com.fasterxml.jackson.module.kotlin.readValue
import de.p10r.helpers.RoomPropertiesInitializer
import de.p10r.helpers.TestDataFactory
import de.p10r.helpers.TestFile
import de.p10r.helpers.randomString
import io.kotest.assertions.asClue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@Suppress("SameParameterValue")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [RoomPropertiesInitializer::class])
class RoomApiE2ETest(@Autowired private val mockMvc: MockMvc) {
    private val mapper = RoomConfig().objectMapper()
    private val testFile = TestFile("test.room.json")

    @Test
    fun `should respond with all rooms`() {
        val expectedRooms = listOf(TestDataFactory.roomWithUuid, TestDataFactory.roomWithUuid).also { persist(it) }

        val roomsApiResponse = getRequest("/rooms")

        val actualRooms: List<Room> = mapper.readValue(roomsApiResponse.response.contentAsString)

        actualRooms shouldContainAll expectedRooms
    }

    private fun getRequest(url: String) = mockMvc.get("/rooms")
        .andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
        .also { println(it.response.contentAsString) }

    @Test
    fun `should create an new room`() {
        val expectedRoom = TestDataFactory.roomWithoutUuid

        postRequest("/rooms", expectedRoom)

        val actualRooms: List<Room> = mapper.readValue(testFile.content)

        actualRooms.size shouldBe 1
        actualRooms.first().name shouldBe expectedRoom.name
    }

    private fun postRequest(url: String, room: Room) = mockMvc.post(url) {
        contentType = MediaType.APPLICATION_JSON
        content = mapper.writeValueAsString(room)
    }.andExpect {
        status { is2xxSuccessful() }
    }.andReturn()
        .also { println(it.response.contentAsString) }

    @Test
    fun `should update an existing room`() {
        val existingRoomEntity = TestDataFactory.roomWithUuid.also { persist(listOf(it)) }
        val changedRoom = Room(existingRoomEntity.id, "changed")

        putRequest("/rooms/${existingRoomEntity.id}", changedRoom)

        val updatedRooms: List<Room> = mapper.readValue(testFile.content)

        updatedRooms.size shouldBe 1
        updatedRooms.first().name shouldBe changedRoom.name
    }

    private fun putRequest(url: String, room: Room) = mockMvc.put(url) {
        contentType = MediaType.APPLICATION_JSON
        content = mapper.writeValueAsString(room)
    }.andExpect {
        status { is2xxSuccessful() }
    }.andReturn()
        .also { println(it.response.contentAsString) }

    @Test
    fun `returns 404 when executing PUT an unknown id`() {
        val invalidId = "ASDF"
        val room = Room(invalidId, "testRoom")

        mockMvc.put("/rooms/$randomString") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(room)
        }.andExpect {
            status { is4xxClientError() }
        }.andReturn()
            .also { println(it.response.contentAsString) }
    }

    @Test
    fun `returns 404 on an empty string`() {
        val invalidId = " "
        val room = Room(invalidId, "jajaja")

        mockMvc.put("/rooms/$randomString") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(room)
        }.andExpect {
            status { is4xxClientError() }
        }.andReturn()
            .also { println(it.response.contentAsString) }
    }

    @BeforeEach
    fun cleanFile() {
        testFile.write("")
    }

    private fun persist(roomEntity: List<Room>) {
        testFile.write(mapper.writeValueAsString(roomEntity))
    }

    @AfterAll
    fun removeTestFile() {
        testFile.delete()
    }
}
