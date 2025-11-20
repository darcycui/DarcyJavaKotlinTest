package time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestTimeZone {
    static String format(Date date, String formater, TimeZone tz)  {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }
    public static void main(String[] args) {
        Date currentDate = new Date();
        String format = format(currentDate, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(format);
        String format2 = format(currentDate, "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT+8:00"));
        System.out.println(format2);
    }
}
