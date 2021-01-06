package yuk.database.migran.batch.problemsummary

import org.springframework.stereotype.Service
import yuk.database.migran.base.BatchJobBuilder
import yuk.database.migran.base.BatchStepBuilder
import yuk.database.migran.batch.sms.MathflatSms

@Service
class MathflatProblemSummaryBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<MathflatSms, MathflatSms?>
) {
}