package yuk.database.migran.base.web

import org.springframework.web.bind.annotation.RestController

@RestController
class MigranController(
    private val migranService: MigranService
) {
}