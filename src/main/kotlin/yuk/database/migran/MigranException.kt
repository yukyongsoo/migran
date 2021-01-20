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

class BatchNotFoundException(
    message: String = "Batch Not Founded"
) : MigranException(message)

class BatchNotHaveStepException(
    message: String = "Batch Not Have Any Step"
) : MigranException(message)

class StepReaderNotFoundException(
    message: String = "Batch Reader can't be null"
) : MigranException(message)

class StepWriterNotFoundException(
    message: String = "Batch Writer can't be null"
) : MigranException(message)
