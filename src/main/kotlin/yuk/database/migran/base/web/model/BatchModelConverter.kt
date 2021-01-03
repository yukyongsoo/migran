package yuk.database.migran.base.web.model

import yuk.database.migran.base.toLocalDate
import java.util.*
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus

object BatchModelConverter {
    fun toBatchStatus(jobName: String, startTime: Date?, endTime: Date?, status: BatchStatus?): MirgranBatchStatus {
        return MirgranBatchStatus(jobName, startTime.toLocalDate(), endTime.toLocalDate(), status?.toString())
    }

    fun toBatchHistory(
        startTime: Date,
        endTime: Date,
        exitStatus: ExitStatus,
        status: BatchStatus
    ): MigranBatchHistory {
        return MigranBatchHistory(
            startTime.toLocalDate()!!,
            endTime.toLocalDate()!!,
            exitStatus.toString(),
            status.toString()
        )
    }
}