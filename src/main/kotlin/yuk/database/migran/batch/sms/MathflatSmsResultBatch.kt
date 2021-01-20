package yuk.database.migran.batch.sms

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Service
import yuk.database.migran.base.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.PostConstruct

@Service
class MathflatSmsResultBatch(
    private val batchJobBuilder: BatchJobBuilder,
    private val batchStepBuilder: BatchStepBuilder<MathflatSms, MathflatSms?>,
    private val toastSmsRequester: ToastSmsRequester
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")

    @PostConstruct
    fun initialize() {
        batchJobBuilder.setBatchName("sms")
        batchStepBuilder.setBasicData("mathflatSms", 10)
        batchJobBuilder.addStep(batchStepBuilder) {
            val reader = getReader(it.getReaderBuilder("mathflat"))
            it.setReader(reader)

            val processor = getProcessor(it.getProcessBuilder("mathflat"))
            it.setProcessor(processor)

            val writer = getWriter(it.getWriterBuilder("mathflat"))
            it.setWriter(writer)
        }

        batchJobBuilder.build()
    }

    private fun getReader(readerBuilder: StepReaderBuilder<MathflatSms>): ItemReader<MathflatSms> {
        val parameterMap = mapOf<String, Any>("status" to "SUCCESS")

        return readerBuilder.getJdbcReader("smsReadStep", "select * from SMSSend where status = :status", parameterMap)
    }

    private fun getProcessor(processBuilder: StepProcessBuilder<MathflatSms, MathflatSms?>) =
        processBuilder.getItemProcessor("smsProcessStep") { _, sms ->
            if (sms.requestId == null) return@getItemProcessor null

            val response = toastSmsRequester.getResult(
                sms.type,
                sms.requestId!!
            )!!

            return@getItemProcessor if (response.body?.data != null) {
                sms.responseStatusCode = response.body!!.data!!.resultCode
                sms.responseMessage = response.body!!.data!!.resultCodeName
                sms.sendDate = LocalDateTime.parse(response.body!!.data!!.resultDate, dateTimeFormatter).toDate()
                sms.status = if (sms.requestStatusCode == 1000) "SUCCESS"
                else "FAIL"
                sms
            } else null
        }


    private fun getWriter(writerBuilder: StepWriterBuilder<MathflatSms?>) =
        writerBuilder.getJdbcItemWriter(
            """update SMSSend set 
               responseStatusCode = :responseStatusCode, responseMessage = :responseMessage, sendDate = :sendDate
               where id = :id
            """
        )
}