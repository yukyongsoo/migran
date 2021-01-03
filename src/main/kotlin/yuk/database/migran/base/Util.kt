package yuk.database.migran.base

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date?.toLocalDate(): LocalDateTime? {
    return this?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
}
