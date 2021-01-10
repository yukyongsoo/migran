package yuk.database.migran.batch

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Service
import yuk.database.migran.base.*
import javax.annotation.PostConstruct
import kotlin.random.Random

@Service
class TestBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<String, String?>
) {
    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("testBatch")
        batchStepBuilder.setBasicData("testStep", 10)
        batchJobBuilder.setStep(batchStepBuilder) {
            val reader = getTestReader(it.getReaderBuilder("mathflat"))
            it.addReader(reader)

            val processor = getTestProcessor(it.getProcessBuilder("mathflat"))
            it.addProcess(processor)

            val writer = getTestWriter(it.getWriterBuilder("mathflat"))
            it.addWriter(writer)
        }

        batchJobBuilder.build()
    }

    private fun getTestReader(readerBuilder: StepReaderBuilder<String>): ItemReader<String> {
        return readerBuilder.getItemReader("testReadStep") {
            val random = Random.nextInt(10)
            if (random == 1)
                return@getItemReader null
            else
                return@getItemReader "test"
        }
    }

    private fun getTestProcessor(processBuilder: StepProcessBuilder<String, String?>): ItemProcessor<String, String?> {
        return processBuilder.getItemProcessor("testProcessStep") { _, it ->
            return@getItemProcessor it
        }
    }

    private fun getTestWriter(writerBuilder: StepWriterBuilder<String?>): ItemWriter<String?> {
        return writerBuilder.getItemWriter("testWriteStep") {
            println("asdfasdf")
            return@getItemWriter "asdfasdf"
        }
    }
}