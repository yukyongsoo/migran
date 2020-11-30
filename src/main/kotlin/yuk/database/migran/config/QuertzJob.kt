package yuk.database.migran.config

import org.quartz.JobExecutionContext
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean

class QuartzJob : QuartzJobBean() {
    var jobName = "migranJob"
    var jobLauncher: JobLauncher? = null
    var jobLocator: JobLocator? = null

    override fun executeInternal(context: JobExecutionContext) {
        val jobParameters = JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()

        val job = jobLocator!!.getJob(jobName)
        jobLauncher!!.run(job, jobParameters)
    }
}