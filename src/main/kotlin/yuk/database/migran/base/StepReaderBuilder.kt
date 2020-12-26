package yuk.database.migran.base

import javax.sql.DataSource

class StepReaderBuilder<I>(private val dataSource: DataSource) {
//    fun syncProblemReader(chunkSize: Int) =
//        JdbcCursorItemReaderBuilder<String>()
//            .fetchSize(chunkSize)
//            .dataSource(pulleyDataSource)
//            .rowMapper(BeanPropertyRowMapper(String::class.java))
//            .sql("select * from Problems where updateDate > '2020-11-26 15:33:06'")
//            .name("syncProblemReader")
//            .build()

}