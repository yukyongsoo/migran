package yuk.database.migran.batch

import org.springframework.batch.item.ItemWriter

class TestBatchWriter : ItemWriter<String> {
    override fun write(items: MutableList<out String>) {
        items.forEach {
            println(it)
        }
    }
}