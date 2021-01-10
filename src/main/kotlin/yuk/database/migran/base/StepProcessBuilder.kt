package yuk.database.migran.base

import org.springframework.batch.item.ItemProcessor
import javax.sql.DataSource

class StepProcessBuilder<I, O>(private val dataSource: DataSource) {
    fun <O> getItemProcessor(stepName: String, f: (DataSource, I) -> O?): ItemProcessor<I, O?> {
        return ItemProcessor<I, O?> { input -> f(dataSource, input) }
    }
}