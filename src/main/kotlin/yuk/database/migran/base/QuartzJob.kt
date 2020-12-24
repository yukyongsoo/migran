package yuk.database.migran.base

import org.quartz.JobExecutionContext
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean

class QuartzJob : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
       println("Asdfasdf")
    }
}