package yuk.database.migran

class MigranException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, exception: Exception) : super(message, exception)
}