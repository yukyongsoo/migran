package yuk.database.migran.config

import org.quartz.*
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import yuk.database.migran.base.QuartzJob
import javax.annotation.PostConstruct
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor

import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.context.annotation.Bean


@Configuration
class QuartzConfig(
    private val schedulerFactoryBean: SchedulerFactoryBean,
) {
    @PostConstruct
    fun initialize() {
        val jobDetail = buildJobDetail(QuartzJob::class.java, "test", "test2", mapOf())

        schedulerFactoryBean.scheduler.scheduleJob(
            jobDetail,
            buildCronJobTrigger("0/5 * * * * ?")
        )
    }



    fun buildCronJobTrigger(scheduleExpr: String): Trigger? {
        return TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExpr))
            .build()
    }

    fun buildJobDetail(job: Class<out Job>, name: String, desc: String, params: Map<out String, Any>): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap.putAll(params)
        return JobBuilder
            .newJob(job)
            .withIdentity(name)
            .withDescription(desc)
            .usingJobData(jobDataMap)
            .build()
    }

}