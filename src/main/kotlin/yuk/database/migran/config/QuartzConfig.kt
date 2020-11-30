package yuk.database.migran.config

import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import org.springframework.scheduling.quartz.JobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean


@Configuration
class QuartzConfig(
    private val jobLauncher: JobLauncher,
    private val jobLocator: JobLocator
) {
    @Bean
    fun jobRegistryBeanPostProcessor(jobRegistry: JobRegistry): JobRegistryBeanPostProcessor {
        val jobRegistryBeanPostProcessor = JobRegistryBeanPostProcessor()
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry)
        return jobRegistryBeanPostProcessor
    }

    @Bean
    fun jobDetailFactoryBean(): JobDetailFactoryBean {
        val jobDetailFactoryBean = JobDetailFactoryBean()
        jobDetailFactoryBean.setJobClass(QuartzJob::class.java)
        val map = mutableMapOf<String, Any>()
        map["jobName"] = "migranJob"
        map["jobLauncher"] = jobLauncher
        map["jobLocator"] = jobLocator
        jobDetailFactoryBean.setJobDataAsMap(map)
        return jobDetailFactoryBean
    }

    @Bean
    fun cronTriggerFactoryBean(): CronTriggerFactoryBean {
        val cronTriggerFactoryBean = CronTriggerFactoryBean()
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject()!!)
        cronTriggerFactoryBean.setCronExpression("0 0/5 * * * ?")
        return cronTriggerFactoryBean
    }

    @Bean
    fun schedulerFactoryBean(): SchedulerFactoryBean {
        val schedulerFactoryBean = SchedulerFactoryBean()
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean().getObject())
        return schedulerFactoryBean
    }
}