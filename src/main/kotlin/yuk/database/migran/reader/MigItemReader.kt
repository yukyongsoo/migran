package yuk.database.migran.reader

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class MigItemReader : ItemReader<String> {
    override fun read(): String? {
        println("Read")
        return null
    }
}