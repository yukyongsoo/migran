package yuk.database.migran.base

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.support.ReferenceJobFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(scopeName = "prototype")
class BatchJobBuilder(
    private val jobBuilderFactory: JobBuilderFactory,
    private val jobRegistry: JobRegistry
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

    fun build(): Job {
        var jobBuilder = jobBuilderFactory.get(name)
            .preventRestart()
            .start(stepList.removeFirst())

        stepList.forEach {
            jobBuilder = jobBuilder.next(it)
        }

        val job = jobBuilder.build()

        val factory = ReferenceJobFactory(job)
        jobRegistry.register(factory)

        return job
    }
}