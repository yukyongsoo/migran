package yuk.database.migran.processor

import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class MigProcessor : ItemProcessor<String, String> {
    override fun process(item: String): String? {
        println("processing")
        return ""
    }
}