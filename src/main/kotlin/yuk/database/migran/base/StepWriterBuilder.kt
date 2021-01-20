package yuk.database.migran.base

import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import java.sql.PreparedStatement
import javax.sql.DataSource

class StepWriterBuilder<O>(private val dataSource: DataSource) {
    fun getItemWriter(stepName: String, f: (DataSource, List<O>) -> O): ItemWriter<O> {
        return ItemWriter<O> { it ->
            f(dataSource, it)
        }
    }

    fun getJdbcItemWriter(sql: String): ItemWriter<O> {
        val writer = JdbcBatchItemWriterBuilder<O>()
            .dataSource(dataSource)
            .sql(sql)
            .beanMapped()
            .build()

        writer.afterPropertiesSet()
        return writer
    }
}