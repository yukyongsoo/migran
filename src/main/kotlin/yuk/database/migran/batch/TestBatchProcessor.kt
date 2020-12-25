package yuk.database.migran.batch

import org.springframework.batch.item.ItemProcessor

class TestBatchProcessor: ItemProcessor<String, String> {
    override fun process(item: String): String {
        return "process"
    }
}