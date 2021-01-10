package yuk.database.migran.base

import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date?.toLocalDate(): LocalDateTime? {
    return this?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
}

fun LocalDateTime.toDate(): Date {
    val zoneDateTime = this.atZone(ZoneId.systemDefault())
    return Date.from(zoneDateTime.toInstant())
}

inline fun <reified T> JdbcTemplate.queryForList(sql: String, parameterMap: Map<String, Any>): MutableList<T> {
    val parameter = NamedParameterUtils.buildValueArray(sql, parameterMap)
    val convertedSql = NamedParameterUtils.substituteNamedParameters(sql, MapSqlParameterSource(parameterMap))

    return query(convertedSql, BeanPropertyRowMapper(T::class.java), parameter)
}
