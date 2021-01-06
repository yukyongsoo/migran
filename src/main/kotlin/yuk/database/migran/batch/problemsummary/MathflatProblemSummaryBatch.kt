package yuk.database.migran.batch.problemsummary

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Service
import yuk.database.migran.base.*
import yuk.database.migran.batch.sms.MathflatSms
import javax.annotation.PostConstruct

@Service
class MathflatProblemSummaryBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<MathflatSms, MathflatProblemSummary?>
) {
    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("summary")
        batchStepBuilder.setBasicData("mathflatSummary", 100)
        batchJobBuilder.setStep(batchStepBuilder) {
            val reader = getReader(it.getReaderBuilder("mathflat"))
            it.addReader(reader)

            val processor = getProcessor(it.getProcessBuilder("mathflat"))
            it.addProcess(processor)

            val writer = getWriter(it.getWriterBuilder("mathflat"))
            it.addWriter(writer)
        }

        batchJobBuilder.build()
    }

    private fun getReader(readerBuilder: StepReaderBuilder<MathflatSms>): ItemReader<MathflatSms> {
        TODO("Not yet implemented")
    }

    private fun getProcessor(processBuilder: StepProcessBuilder<MathflatSms, MathflatProblemSummary?>): ItemProcessor<MathflatSms, MathflatProblemSummary?> {
        TODO("Not yet implemented")
    }

    private fun getWriter(writerBuilder: StepWriterBuilder<MathflatProblemSummary?>): ItemWriter<MathflatProblemSummary?> {
        TODO("Not yet implemented")
    }
}