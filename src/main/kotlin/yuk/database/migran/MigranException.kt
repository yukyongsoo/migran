package yuk.database.migran

open class MigranException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, exception: Exception) : super(message, exception)
}

class DataSourceNotFoundException(
    message: String = "DataSource Not Found"
) : MigranException(message)