package yuk.database.migran.base

import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import javax.sql.DataSource

class StepWriterBuilder<O>(private val dataSource: DataSource) {
    fun <O> getItemWriter(stepName: String, f: () -> O): ItemWriter<O> {
        return ItemWriter<O> { f() }
    }

    fun <O> getJdbcItemWriter(sql: String): ItemWriter<O> {
        return JdbcBatchItemWriterBuilder<O>()
            .dataSource(dataSource)
            .sql(sql)
            .build()
    }
}