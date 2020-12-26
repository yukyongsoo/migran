package yuk.database.migran.batch.test

import org.springframework.batch.item.ItemReader
import kotlin.random.Random

class TestBatchReader : ItemReader<String> {
    override fun read(): String? {
        val random = Random.nextInt(10)
        if(random == 1)
            return null
        return "read"
    }
}