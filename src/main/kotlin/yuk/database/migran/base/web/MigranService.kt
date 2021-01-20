package yuk.database.migran.base.web

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import yuk.database.migran.BatchAlreadyStartedException
import yuk.database.migran.BatchCurrentlyNotRunningException
import yuk.database.migran.BatchNotFoundException
import yuk.database.migran.base.web.model.BatchModelConverter
import yuk.database.migran.base.web.model.MigranBatchHistory
import yuk.database.migran.base.web.model.MirgranBatchStatus

@Service
class MigranService(
    private val jobOperator: JobOperator,
    private val jobExplorer: JobExplorer,
    private val schedulerFactoryBean: SchedulerFactoryBean
) {
    fun getAllBatch(): List<MirgranBatchStatus> {
        val batchNameList = jobOperator.jobNames
        return batchNameList.mapNotNull {
            val jobInstance = jobExplorer.getLastJobInstance(it)
            val jobExecution = jobExplorer.getJobExecution(jobInstance?.id)

            BatchModelConverter.toBatchStatus(
                it,
                jobExecution?.startTime,
                jobExecution?.endTime,
                jobExecution?.status
            )
        }
    }

    fun getBatchStatus(jobName: String): List<MigranBatchHistory> {
        jobOperator.jobNames.firstOrNull { it == jobName } ?: throw BatchNotFoundException()

        val executionCount = jobExplorer.getJobInstanceCount(jobName)
        val executionList = jobExplorer.getJobInstances(jobName, 0, executionCount)
        return executionList.mapNotNull {
            val jobExecution = jobExplorer.getJobExecution(it.id) ?: return@mapNotNull null

            BatchModelConverter.toBatchHistory(
                jobExecution.startTime,
                jobExecution.endTime,
                jobExecution.exitStatus,
                jobExecution.status
            )
        }
    }

    fun stopBatch(jobName: String) {
        jobOperator.jobNames.firstOrNull { it == jobName } ?: throw BatchNotFoundException()

        val instance = jobExplorer.findRunningJobExecutions(jobName)
        instance.forEach {
            if (it.status == BatchStatus.STARTED || it.status == BatchStatus.STARTING) {
                jobOperator.stop(it.id)
                return
            }
        }
        throw BatchCurrentlyNotRunningException()
    }

    fun startBatch(jobName: String) {
        jobOperator.jobNames.firstOrNull { it == jobName } ?: throw BatchNotFoundException()

        val instance = jobExplorer.findRunningJobExecutions(jobName)

        instance.forEach {
            if (it.status == BatchStatus.STARTING || it.status == BatchStatus.STARTING)
                throw BatchAlreadyStartedException()
        }
        val params = JobParametersBuilder()
            .addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()

        jobOperator.start(jobName, params.toString())
    }
}