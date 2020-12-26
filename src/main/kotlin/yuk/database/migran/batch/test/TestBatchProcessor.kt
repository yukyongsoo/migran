package yuk.database.migran.batch.test

import org.springframework.batch.item.ItemProcessor

class TestBatchProcessor: ItemProcessor<String, String> {
    override fun process(item: String): String {
       return "process"
    }
}

