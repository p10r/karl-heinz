package de.p10r.internal

import de.p10r.Room

internal interface RoomOperations {
    fun add(room: Room): Room
    fun update(room: Room): Room
    fun findBy(id: String): Room?
    fun findAll(): List<Room>
    fun delete(room: Room)
}

internal class Service(private val rooms: Rooms) : RoomOperations {

    override fun add(room: Room): Room = rooms.save(room.copy(id = randomUUID))

    override fun update(room: Room): Room = rooms.save(room)

    override fun findBy(id: String): Room? = rooms.findBy(id)

    override fun findAll(): List<Room> = rooms.findAll()

    override fun delete(room: Room) {
        rooms.delete(room)
    }
}