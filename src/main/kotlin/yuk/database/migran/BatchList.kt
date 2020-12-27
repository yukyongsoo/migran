package yuk.database.migran

class BatchList {
    private val jobList: MutableList<QuartzJobDeclaration> = mutableListOf()

    init {
        addJob("testJob", "실행기1", "0 0/1 * 1/1 * ? *", "testBatch")
        addJob("testJob2", "실행기2", "0 0/1 * 1/1 * ? *", "testBatch2")

    }

    private fun addJob(name: String, desc: String, cron: String, batchName: String) {
        val declaration = QuartzJobDeclaration(name, desc, cron, batchName)
        jobList.add(declaration)
    }

    fun getAllJob(): List<QuartzJobDeclaration> {
        return jobList
    }

    class QuartzJobDeclaration(
        val name: String,
        val desc: String,
        val cron: String,
        val batchName: String
    )
}