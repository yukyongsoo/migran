package yuk.database.migran.batch.problemsummary

data class MathflatProblemSummary(
    val problemId: Int,
    val total_used: Long,
    val correct_times: Long,
    val wrong_times: Long
)