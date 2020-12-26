package yuk.database.migran.batch

import org.springframework.stereotype.Service
import yuk.database.migran.base.BatchJobBuilder
import yuk.database.migran.base.BatchStepBuilder
import javax.annotation.PostConstruct

@Service
class TestBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<String, String>
) {
    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("testBatch")
        batchJobBuilder.setStep(batchStepBuilder) {
            val reader = it.getReaderBuilder("mathflat")
        }

        batchJobBuilder.build()
    }
}