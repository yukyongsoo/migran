package yuk.database.migran.batch.sms

import org.springframework.stereotype.Service
import yuk.database.migran.base.*
import javax.annotation.PostConstruct

@Service
class MathflatSmsResultBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<MathflatSms, MathflatSms?>,
    private val toastSmsRequester: ToastSmsRequester
) {
    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("sms")
        batchStepBuilder.setBasicData("mathflatSms", 10)
        batchJobBuilder.setStep(batchStepBuilder) {
            val reader = getReader(it.getReaderBuilder("mathflat"))
            it.addReader(reader)

            val processor = getProcessor(it.getProcessBuilder("mathflat"))
            it.addProcess(processor)

            val writer = getWriter(it.getWriterBuilder("mathflat"))
            it.addWriter(writer)
        }

        batchJobBuilder.build()
    }

    private fun getReader(readerBuilder: StepReaderBuilder<MathflatSms>) =
        readerBuilder.getJdbcReader<MathflatSms>("smsReadStep", "select * from SMSSend where status = 'SUCCESS'")

    private fun getProcessor(processBuilder: StepProcessBuilder<MathflatSms, MathflatSms?>) =
        processBuilder.getItemProcessor("smsProcessStep") {
            if (it.requestId == null) return@getItemProcessor null

            val response = toastSmsRequester.getResult(
                it.type!!,
                it.requestId!!
            )!!

            return@getItemProcessor if (response.body!!.data != null) {
                it.responseStatusCode = response.body!!.data!!.resultCode
                it.responseMessage = response.body!!.data!!.resultCodeName
                it.status = if (it.requestStatusCode == 1000) "SUCCESS"
                else "FAIL"
                it
            } else null
        }


    private fun getWriter(writerBuilder: StepWriterBuilder<MathflatSms?>) =
        writerBuilder.getJdbcItemWriter("update SMSSend set responseStatusCode = :responseStatusCode, responseMessage = :responseMessage where id = :id")
}