package yuk.database.migran

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class MigranApplication

fun main(args: Array<String>) {
    runApplication<MigranApplication>(*args)
}
