package yuk.database.migran.base

import org.quartz.JobExecutionContext
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.batch.core.JobParameters


class QuartzJob(
    private val jobLocator: JobLocator,
    private val jobLauncher: JobLauncher
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val job: Job = jobLocator.getJob("testJob")
        val params = JobParametersBuilder()
            .addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()

        jobLauncher.run(job, params)
    }
}