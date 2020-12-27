package yuk.database.migran.base

import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

class StepReaderBuilder<I>(private val dataSource: DataSource, private val chunkSize: Int) {
    fun <I> getJdbcReader(stepName: String, sql: String): ItemReader<I> {
        return JdbcCursorItemReaderBuilder<I>()
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(BeanPropertyRowMapper<I>())
            .sql(sql)
            .name(stepName)
            .build()
    }

    fun <I> getItemReader(stepName: String, f: () -> I?): ItemReader<I> {
        return ItemReader<I> {
            f()
        }
    }
}