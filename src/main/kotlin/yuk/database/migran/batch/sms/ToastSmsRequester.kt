package yuk.database.migran.batch.sms

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class ToastSmsRequester {
    private val appKey = "pvmySw32VtYzaZsB"
    private val webClient = WebClient.builder().baseUrl("https://api-sms.cloud.toast.com").build()

    fun getResult(type: String, requestId: String): ToastSmsResponse? {
        return webClient.get()
            .uri("/sms/v2.1/appKeys/$appKey/sender/${type.toLowerCase()}/$requestId") { builder ->
                builder.queryParam("mtPr", "1")
                builder.build()
            }
            .retrieve()
            .bodyToMono<ToastSmsResponse>()
            .block()
    }
}

