package de.p10r.helpers

import de.p10r.Room
import de.p10r.internal.randomUUID

object TestDataFactory {
    val roomWithUuid
        get() = Room(randomUUID, randomString)

    val roomWithoutUuid
        get() = Room(null, randomString)
}

val randomString: String
    get() {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

