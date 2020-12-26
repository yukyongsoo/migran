package yuk.database.migran.base

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import yuk.database.migran.DataSourceNotFoundException
import javax.sql.DataSource

@Component
@Scope(scopeName = "prototype")
class BatchStepBuilder<I, O>(
    @Qualifier("batch") private val batchDataSource: DataSource,
    @Qualifier("pulley") private val pulleyDataSource: DataSource,
    @Qualifier("mathflat") private val mathflatDataSource: DataSource,
    private val stepBuilderFactory: StepBuilderFactory
) {
    private var stepName: String = ""
    private var chunkSize: Int = 0

    private val readerList: MutableList<ItemReader<I>> = mutableListOf()
    private val processList: MutableList<ItemProcessor<I, O>> = mutableListOf()
    private val writerList: MutableList<ItemWriter<O>> = mutableListOf()

    fun setBasicData(stepName: String, chunkSize: Int) {
        this.stepName = stepName
        this.chunkSize = chunkSize
    }

    fun getReaderBuilder(name: String): StepReaderBuilder<I> {
        val dataSource = getDataSource(name)
        return StepReaderBuilder<I>(dataSource)
    }

    fun getProcessBuilder(name: String): StepProcessBuilder<I, O> {
        val dataSource = getDataSource(name)
        return StepProcessBuilder<I, O>(dataSource)
    }

    fun getWriterBuilder(name: String): StepWriterBuilder<O> {
        val dataSource = getDataSource(name)
        return StepWriterBuilder<O>(dataSource)
    }

    private fun getDataSource(name: String): DataSource {
        return when (name) {
            "pulley" -> pulleyDataSource
            "mathflat" -> mathflatDataSource
            else -> throw DataSourceNotFoundException()
        }
    }

    fun addReader(itemReader: ItemReader<I>) {
        readerList.add(itemReader)
    }

    fun addProcess(itemProcessor: ItemProcessor<I, O>) {
        processList.add(itemProcessor)
    }

    fun addWriter(itemWriter: ItemWriter<O>) {
        writerList.add(itemWriter)
    }

    fun build(): Step {
        var builder = stepBuilderFactory.get(stepName)
            .chunk<I, O>(chunkSize)

        readerList.forEach {
            builder = builder.reader(it)
        }

        processList.forEach {
            builder = builder.processor(it)
        }

        writerList.forEach {
            builder = builder.writer(it)
        }

        readerList.clear()
        processList.clear()
        writerList.clear()

        return builder.build()
    }
}