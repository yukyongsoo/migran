package yuk.database.migran.batch.problemsummary

import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import yuk.database.migran.base.*
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
        return processBuilder.getItemProcessor("problemProcessStep") { dataSource, item ->
            val parameterMap = mapOf("id" to item.id)

            val workbookScoringList = NamedParameterJdbcTemplate(dataSource).queryForList<MathflatWorkbookScoring>(
                """select problem_id, result, count(*) as count
                     from custom_workbook_problem
                     join student_workbook_scoring on problem_id = :id and
                     custom_workbook_problem.workbook_problem_id = student_workbook_scoring.workbook_problem_id
                     group by problem_id, result
                """,
                parameterMap
            )

            val worksheetScoringList = NamedParameterJdbcTemplate(dataSource).queryForList<MathflatWorksheetScoring>(
                """
                    select problem_id, result, count(*) as count
                     from worksheet_problem
                     join student_worksheet_scoring on problem_id = :id and 
                     worksheet_problem.id = student_worksheet_scoring.worksheet_problem_id
                     group by problem_id, result;
                """, parameterMap
            )

            countProblemSummary(item.id, workbookScoringList, worksheetScoringList)
        }
    }

    private fun countProblemSummary(
        problemId: Int,
        workbookScoringList: MutableList<MathflatWorkbookScoring>,
        worksheetScoringList: MutableList<MathflatWorksheetScoring>
    ): MathflatProblemSummary {
        val summary = MathflatProblemSummary()
        summary.problemId = problemId

        workbookScoringList.forEach {
            summary.totalUsed += it.count
            if (it.result == "CORRECT")
                summary.correctTimes += it.count
            else if (it.result == "WRONG")
                summary.wrongTimes += it.count
        }

        worksheetScoringList.forEach {
            summary.totalUsed += it.count
            if (it.result == "CORRECT")
                summary.correctTimes += it.count
            else if (it.result == "WRONG")
                summary.wrongTimes += it.count
        }

        return summary
    }

    private fun getWriter(writerBuilder: StepWriterBuilder<MathflatProblemSummary?>): ItemWriter<MathflatProblemSummary?> {
        return writerBuilder.getJdbcItemWriter(
            """INSERT INTO problem_summary (problem_id,total_used,correct_times,wrong_times)
             VALUES (:problemId,:totalUsed,:correctTimes,:wrongTimes) ON DUPLICATE KEY UPDATE
              total_used = :totalUsed, correct_times = :correctTimes, wrong_times = :wrongTimes
                """
        )
    }
}