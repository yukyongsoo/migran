package yuk.database.migran.base

import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import yuk.database.migran.batch.test.TestBatchProcessor
import yuk.database.migran.batch.test.TestBatchReader
import yuk.database.migran.batch.test.TestBatchWriter

@Configuration
class BatchJob(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
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