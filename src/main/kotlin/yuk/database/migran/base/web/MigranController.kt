package yuk.database.migran.base.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MigranController(
    private val migranService: MigranService
) {
    @GetMapping
    fun getHealth() {

    }

    @GetMapping("/batch")
    fun getBatchList() {
        migranService.getAllBatch()
    }

    @GetMapping("batch/{id}")
    fun getBatchStatus(@PathVariable id: String){

    }
}