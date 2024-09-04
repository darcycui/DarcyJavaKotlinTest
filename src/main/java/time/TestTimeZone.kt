package time

import java.text.SimpleDateFormat
import java.util.*


fun main() {
    val timeString = TestTimeZone().format(
        Date(),
        "yyyy-MM-dd-HH:mm:ss",
//        TimeZone.getTimeZone("UTC")
        TimeZone.getTimeZone("GMT+8:00")
    )
    println("timeString=$timeString")
}

class TestTimeZone {
    fun format(self: Date?, format: String?, tz: TimeZone?): String {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = tz
        return sdf.format(self)
    }
}