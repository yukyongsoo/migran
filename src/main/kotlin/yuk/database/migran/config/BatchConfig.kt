package yuk.database.migran.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import yuk.database.migran.batch.TestBatchProcessor
import yuk.database.migran.batch.TestBatchReader
import yuk.database.migran.batch.TestBatchWriter


@Configuration
class BatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun jobRegistryBeanPostProcessor(jobRegistry: JobRegistry): JobRegistryBeanPostProcessor {
        val jobRegistryBeanPostProcessor = JobRegistryBeanPostProcessor()
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry)
        return jobRegistryBeanPostProcessor
    }

    @Bean
    fun getJob(): Job {
        return jobBuilderFactory.get("testJob")
            .preventRestart()
            .start(getStep())
            .build()
    }

    fun getStep(): TaskletStep {
        return stepBuilderFactory.get("testStep")
            .chunk<String, String>(10)
            .reader(TestBatchReader())
            .processor(TestBatchProcessor())
            .writer(TestBatchWriter())
            .build()
    }

}