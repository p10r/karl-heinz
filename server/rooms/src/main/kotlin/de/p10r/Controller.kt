package de.p10r

import de.p10r.internal.RoomOperations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/rooms")
internal class Controller(private val roomOperations: RoomOperations) {
    @GetMapping
    fun getAll() = ResponseEntity.ok(roomOperations.findAll())

    @PostMapping
    fun create(@RequestBody room: Room) = ResponseEntity.ok(roomOperations.add(room))

    @PutMapping("/{id}")
    fun edit(@RequestBody room: Room, @PathVariable id: String): ResponseEntity<Room> {
        if(roomOperations.findBy(id) == null) return ResponseEntity.notFound().build()

        return ResponseEntity.ok(roomOperations.update(Room(id, room.name)))
    }
}