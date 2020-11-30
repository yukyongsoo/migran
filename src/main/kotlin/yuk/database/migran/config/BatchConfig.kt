package yuk.database.migran.config

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import yuk.database.migran.listener.MigListener
import yuk.database.migran.processor.MigProcessor
import yuk.database.migran.reader.MigItemReader


@Configuration
@EnableBatchProcessing
class BatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val migItemReader: MigItemReader,
    private val migProcessor: MigProcessor,
    private val migListener: MigListener
) {
    @Bean
    fun inputJob(step: Step) =
        jobBuilderFactory.get("migranJob")
            .incrementer(RunIdIncrementer())
            .listener(migListener)
            .flow(step)
            .end()
            .build()

    @Bean
    fun getStep(writer: ItemWriter<String>): Step =
        stepBuilderFactory["migranJob"]
            .chunk<String, String>(10)
            .reader(migItemReader)
            .processor(migProcessor)
            .writer(writer)
            .build()
}