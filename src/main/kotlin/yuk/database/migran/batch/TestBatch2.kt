package yuk.database.migran.batch

import org.springframework.stereotype.Service
import yuk.database.migran.base.BatchJobBuilder
import yuk.database.migran.base.BatchStepBuilder
import javax.annotation.PostConstruct

@Service
class TestBatch2(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<String, String>
) {
    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("testBatch2")
        batchJobBuilder.setStep(batchStepBuilder) {

        }
    }
}