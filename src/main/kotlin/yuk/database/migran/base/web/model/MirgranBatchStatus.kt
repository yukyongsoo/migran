package yuk.database.migran.base.web.model

import java.time.LocalDateTime

data class MirgranBatchStatus(
    val name: String,
    val lastStartDatetime: LocalDateTime?,
    val lastEndDatetime: LocalDateTime?,
    val currentStatus: String?
)
