package yuk.database.migran.base.web.model

import java.time.LocalDateTime

data class MigranBatchHistory(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val exitStatus: String,
    val currentStatus: String
)
