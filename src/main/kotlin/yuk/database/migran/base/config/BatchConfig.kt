package yuk.database.migran.base.config

import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class BatchConfig {
    @Bean
    fun jobRegistryBeanPostProcessor(@Autowired jobRegistry: JobRegistry): JobRegistryBeanPostProcessor {
        val jobRegistryBeanPostProcessor = JobRegistryBeanPostProcessor()
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry)
        return jobRegistryBeanPostProcessor
    }
}