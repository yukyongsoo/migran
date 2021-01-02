package yuk.database.migran

open class MigranException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, exception: Exception) : super(message, exception)
}

class DataSourceNotFoundException(
    message: String = "DataSource Not Found"
) : MigranException(message)

class BatchAlreadyStartedException(
    message: String = "Batch Already Started"
) : MigranException(message)

class BatchCurrentlyNotRunningException(
    message: String = "Batch Currently Not Started"
) : MigranException(message)

class BatchInstanceNotFoundException(
    message: String = "Batch Instance Not Founded"
) : MigranException(message)
