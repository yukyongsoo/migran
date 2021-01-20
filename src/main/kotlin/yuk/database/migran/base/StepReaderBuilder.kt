package yuk.database.migran.base

import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.support.ListPreparedStatementSetter
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterUtils


class StepReaderBuilder<I>(val dataSource: DataSource, val chunkSize: Int) {
    inline fun <reified I> getJdbcReader(stepName: String, sql: String, parameterMap: Map<String, Any>): ItemReader<I> {
        val parameter = ArgumentPreparedStatementSetter(NamedParameterUtils.buildValueArray(sql, parameterMap))
        val convertedSql = NamedParameterUtils.substituteNamedParameters(sql, MapSqlParameterSource(parameterMap))

        return JdbcCursorItemReaderBuilder<I>()
            .fetchSize(chunkSize)
            .dataSource(dataSource)
            .rowMapper(BeanPropertyRowMapper(I::class.java))
            .sql(convertedSql)
            .preparedStatementSetter(parameter)
            .name(stepName)
            .build()
    }

    fun getItemReader(stepName: String, f: (DataSource) -> I?): ItemReader<I> {
        return ItemReader<I> {
            f(dataSource)
        }
    }
}