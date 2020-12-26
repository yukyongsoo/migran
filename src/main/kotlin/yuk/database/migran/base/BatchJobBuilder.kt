package yuk.database.migran.base

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.job.builder.SimpleJobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
@Scope(scopeName = "prototype")
class BatchJobBuilder(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRepository: JobRepository
) {
    private var name = ""
    private val stepList: MutableList<Step> = mutableListOf()

    fun setBatchName(name: String) {
        this.name = name
    }

    fun <I, O> setStep(batchStepBuilder: BatchStepBuilder<I, O>, f: (BatchStepBuilder<I, O>) -> Unit) {
        f(batchStepBuilder)
        stepList.add(batchStepBuilder.build())
    }

    @Bean
    @Lazy
    fun build(): Job {
        var job = jobBuilderFactory.get(name)
            .preventRestart()
            .repository(jobRepository)
            .start(stepList.removeFirst())

        stepList.forEach {
            job = job.next(it)
        }

        return job.build()
    }
}