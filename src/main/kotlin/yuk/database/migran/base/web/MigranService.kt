package yuk.database.migran.base.web

import org.springframework.batch.core.launch.JobOperator
import org.springframework.stereotype.Service

@Service
class MigranService(
    private val jobOperator: JobOperator
) {
    fun getAllBatchName(): Set<String> {
        return jobOperator.jobNames
    }
}