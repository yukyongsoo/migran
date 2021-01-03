package yuk.database.migran.base.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import yuk.database.migran.base.web.model.MigranBatchHistory
import yuk.database.migran.base.web.model.MirgranBatchStatus
import javax.print.attribute.standard.JobName

@RestController
class MigranController(
    private val migranService: MigranService
) {
    @GetMapping
    fun getHealth() {

    }

    @GetMapping("/batch")
    fun getBatchList(): List<MirgranBatchStatus> {
        return migranService.getAllBatch()
    }

    @GetMapping("batch/{jobName}")
    fun getBatchStatus(@PathVariable jobName: String): List<MigranBatchHistory> {
        return migranService.getBatchStatus(jobName)
    }

    @PostMapping("batch/start/{jobName}")
    fun startBatch(@PathVariable jobName: String) {
        migranService.startBatch(jobName)
    }

    @PostMapping("batch/stop/{jobName}")
    fun stopBatch(@PathVariable jobName: String) {
        migranService.stopBatch(jobName)
    }
}