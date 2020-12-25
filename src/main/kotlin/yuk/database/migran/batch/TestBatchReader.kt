package yuk.database.migran.batch

import org.springframework.batch.item.ItemReader

class TestBatchReader : ItemReader<String> {
    override fun read(): String {
        return "read"
    }
}