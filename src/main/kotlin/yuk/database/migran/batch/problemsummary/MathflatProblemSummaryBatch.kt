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
    private val batchStepBuilder: BatchStepBuilder<MathflatProblem, MathflatProblemSummary?>
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

    private fun getReader(readerBuilder: StepReaderBuilder<MathflatProblem>): ItemReader<MathflatProblem> {
        return readerBuilder.getJdbcReader<MathflatProblem>("problemReadStep", "select id from problem", mapOf())
    }

    private fun getProcessor(processBuilder: StepProcessBuilder<MathflatProblem, MathflatProblemSummary?>)
            : ItemProcessor<MathflatProblem, MathflatProblemSummary?> {
        return processBuilder.getItemProcessor("problemProcessStep") {
            // TODO :: we need develop
            MathflatProblemSummary(123, 1, 1, 1)
        }
    }

    private fun getWriter(writerBuilder: StepWriterBuilder<MathflatProblemSummary?>): ItemWriter<MathflatProblemSummary?> {
        return writerBuilder.getJdbcItemWriter(
            """INSERT INTO problemSummary (total_used,correct_times,wrong_times)
             VALUES (:total_used,:correct_times,:wrong_times) ON DUPLICATE KEY UPDATE
              problemId = :problemId
                """
        )
    }
}