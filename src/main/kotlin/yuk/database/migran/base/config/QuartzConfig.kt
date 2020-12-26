package yuk.database.migran.base.config

import org.quartz.*
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import yuk.database.migran.BatchList
import yuk.database.migran.base.QuartzJob
import javax.annotation.PostConstruct

@Configuration
class QuartzConfig(
    private val schedulerFactoryBean: SchedulerFactoryBean
) {
    private val jobList: List<BatchList.QuartzJobDeclaration> = BatchList().getAllJob()

    @PostConstruct
    fun initialize() {
        jobList.forEach {
            val jobDetail = buildJobDetail(QuartzJob::class.java, it.name, it.desc, mapOf("batchName" to it.batchName))

            schedulerFactoryBean.scheduler.scheduleJob(
                jobDetail,
                buildCronJobTrigger(it.cron)
            )
        }
    }

    private fun buildCronJobTrigger(scheduleExpr: String): Trigger {
        return TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExpr))
            .build()
    }

    private fun buildJobDetail(
        job: Class<out Job>,
        name: String,
        desc: String,
        params: Map<String, String>
    ): JobDetail {
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