package yuk.database.migran.base

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import yuk.database.migran.DataSourceNotFoundException
import yuk.database.migran.StepReaderNotFoundException
import yuk.database.migran.StepWriterNotFoundException
import yuk.database.migran.batch.sms.MathflatSms
import javax.sql.DataSource

@Component
@Scope(scopeName = "prototype")
class BatchStepBuilder<I, O>(
    @Qualifier("batch") private val batchDataSource: DataSource,
    @Qualifier("pulley") private val pulleyDataSource: DataSource,
    @Qualifier("mathflat") private val mathflatDataSource: DataSource,
    private val stepBuilderFactory: StepBuilderFactory,
    private val transactionManager: PlatformTransactionManager
) {
    private var stepName: String = ""
    private var chunkSize: Int = 0

    private var reader: ItemReader<I>? = null
    private var process: ItemProcessor<I, O>? = null
    private var writer: ItemWriter<O>? = null

    fun setBasicData(stepName: String, chunkSize: Int) {
        this.stepName = stepName
        this.chunkSize = chunkSize
    }

    fun getReaderBuilder(datasourceName: String): StepReaderBuilder<I> {
        val dataSource = getDataSource(datasourceName)
        return StepReaderBuilder<I>(dataSource, chunkSize)
    }

    fun getProcessBuilder(datasourceName: String): StepProcessBuilder<I, O> {
        val dataSource = getDataSource(datasourceName)
        return StepProcessBuilder<I, O>(dataSource)
    }

    fun getWriterBuilder(datasourceName: String): StepWriterBuilder<O> {
        val dataSource = getDataSource(datasourceName)
        return StepWriterBuilder<O>(dataSource)
    }

    private fun getDataSource(datasourceName: String): DataSource {
        return when (datasourceName) {
            "pulley" -> pulleyDataSource
            "mathflat" -> mathflatDataSource
            else -> throw DataSourceNotFoundException()
        }
    }

    fun setReader(itemReader: ItemReader<I>) {
        reader = itemReader
    }

    fun setProcessor(itemProcessor: ItemProcessor<I, O>) {
        process = itemProcessor
    }

    fun setWriter(itemWriter: ItemWriter<O>) {
        writer = itemWriter
    }

    fun build(): Step {
        var builder = stepBuilderFactory.get(stepName)
            .transactionManager(transactionManager)
            .chunk<I, O>(chunkSize)


        if (reader == null)
            throw StepReaderNotFoundException()

        if (writer == null)
            throw  StepWriterNotFoundException()

        builder = builder.reader(reader!!)
        builder = process?.let { builder.processor(it) } ?: builder
        builder = builder.writer(writer!!)

        builder = builder.faultTolerant() //TODO:: for skip error

        val step = builder.build()

        reader = null
        writer = null
        process = null

        return step
    }
}