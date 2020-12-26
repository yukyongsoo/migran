package yuk.database.migran.base

import org.quartz.JobExecutionContext
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.batch.core.JobParameters
import org.springframework.beans.factory.annotation.Autowired

class QuartzJob private constructor(): QuartzJobBean() {
    @Autowired
    private lateinit var jobLocator: JobLocator
    @Autowired
    private lateinit var jobLauncher: JobLauncher

    override fun executeInternal(context: JobExecutionContext) {
        val name = context.mergedJobDataMap["batchName"].toString()
        val job: Job = jobLocator.getJob(name)
        val params = JobParametersBuilder()
            .addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()

        jobLauncher.run(job, params)
    }
}