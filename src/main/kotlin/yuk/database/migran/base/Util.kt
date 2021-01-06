package yuk.database.migran.base

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import java.time.ZonedDateTime




fun Date?.toLocalDate(): LocalDateTime? {
    return this?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
}

fun LocalDateTime.toDate(): Date {
    val zoneDateTime = this.atZone(ZoneId.systemDefault())
    return Date.from(zoneDateTime.toInstant())
}
