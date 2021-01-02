package yuk.database.migran.base.web

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobInstance
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import yuk.database.migran.BatchAlreadyStartedException
import yuk.database.migran.BatchCurrentlyNotRunningException
import yuk.database.migran.BatchInstanceNotFoundException

@Service
class MigranService(
    private val jobOperator: JobOperator,
    private val jobExplorer: JobExplorer,
    private val schedulerFactoryBean: SchedulerFactoryBean
) {
    fun getAllBatch(){
        val batchNameList =  jobOperator.jobNames
        batchNameList.forEach {
            val jobInstance = jobExplorer.getLastJobInstance(it)
                ?: return@forEach
            jobExplorer.getJobExecution(jobInstance.id)
        }
    }

    fun getBatchStatus(jobName: String) {
        val executionCount = jobExplorer.getJobInstanceCount(jobName)
        val executionList = jobExplorer.getJobInstances(jobName,0,executionCount)
        executionList.forEach {
            jobExplorer.getJobExecution(it.id)
        }
    }

    fun stopBatch(jobName: String) {
        val instance = jobExplorer.findRunningJobExecutions(jobName)
        instance.forEach {
            if(it.status == BatchStatus.STARTED || it.status == BatchStatus.STARTING){
                jobOperator.stop(it.id)
                return
            }
        }
        throw BatchCurrentlyNotRunningException()
    }

    fun startBatch(jobName: String) {
        val instance = jobExplorer.findRunningJobExecutions("testBatch")
        instance.forEach {
            if(it.status == BatchStatus.STARTING || it.status == BatchStatus.STARTING)
                throw BatchAlreadyStartedException()
        }
        jobOperator.start(jobName,"")
    }
}