package yuk.database.migran.batch.sms

import java.util.*

class MathflatSms {
    var id: Int = 0
    var classID: String = ""
    var studentID: String = ""
    var studentName: String = ""
    var teacherId: String = ""
    var status: String = ""
    var requestId: String? = null
    var sender: String = ""
    var receiver: String = ""
    var type: String = ""
    var message: String = ""
    var target: String = ""
    var requestStatusCode: Int? = null
    var requestMessage: String? = null
    var responseStatusCode: String? = null
    var responseMessage: String? = null
    var createDate: String = ""
    var sendDate: Date? = null
}