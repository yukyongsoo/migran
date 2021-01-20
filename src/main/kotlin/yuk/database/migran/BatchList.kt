package yuk.database.migran

class BatchList {
    private val jobList: MutableList<QuartzJobDeclaration> = mutableListOf()

    init {
        //addJob("testJob", "실행기1", "0 0/1 * 1/1 * ? *", "testBatch")
        addJob("sms", "토스트 문자 내용 업데이터", "0 0/1 * 1/1 * ? *", "sms")
        //addJob("summary", "매쓰플랫 문제 사용 요약 데이터", "0 0/1 * 1/1 * ? *", "summary")
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