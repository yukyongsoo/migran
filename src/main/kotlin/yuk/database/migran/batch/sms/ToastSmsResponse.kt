package yuk.database.migran.batch.sms

import com.fasterxml.jackson.annotation.JsonProperty

class ToastSmsResponse {
    var header: ToastSMSHeader? = null
    var body: ToastSMSResultBody? = null
}

class ToastSMSHeader {
    @JsonProperty("isSuccessful")
    var isSuccessful: Boolean? = null
    var resultCode: Int? = null
    var resultMessage: String? = null
}

class ToastSMSResultBody {
    var totalCount: Int? = null
    var data: ToastSMSResultBodyData? = null
}

class ToastSMSResultBodyData {
    var requestId: String? = null
    var requestDate: String? = null
    var resultDate: String? = null
    var body: String? = null
    var sendNo: String? = null
    var recipientNo: String? = null
    var msgStatus: String? = null
    var msgStatusName: String? = null
    var resultCode: String? = null
    var resultCodeName: String? = null
    var mtPr: String? = null
}