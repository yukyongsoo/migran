package yuk.database.migran.base.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DatasourceConfig {
    @Primary
    @Bean(name = ["batch"])
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    fun batchDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["pulley"])
    @ConfigurationProperties(prefix = "spring.datasource.pulley")
    fun getPulleyDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["mathflat"])
    @ConfigurationProperties(prefix = "spring.datasource.mathflat")
    fun getMathflatDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}