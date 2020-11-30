package yuk.database.migran.writer

import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class MigItemWriter : ItemWriter<String>{
    override fun write(items: MutableList<out String>) {
        println("write")
    }
}