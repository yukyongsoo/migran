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
            val jobDetail = buildJobDetail<QuartzJob>(it.name, it.desc, mapOf("batchName" to it.batchName))

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

    private inline fun <reified T : Job> buildJobDetail(
        name: String,
        desc: String,
        params: Map<String, String>
    ): JobDetail {
        val jobDataMap = JobDataMap()
        jobDataMap.putAll(params)
        return JobBuilder
            .newJob(T::class.java)
            .withIdentity(name)
            .withDescription(desc)
            .usingJobData(jobDataMap)
            .build()
    }

}