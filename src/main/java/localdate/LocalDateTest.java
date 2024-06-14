package localdate;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class LocalDateTest {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        System.out.println("当前日期: " + currentDate);
        System.out.println("当前日期: " + currentDate.getYear() + "-" + currentDate.getMonthValue() + "-" + currentDate.getDayOfMonth());
        String format = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("当前日期格式化: " + format);
        String format2 = currentDate.format(DateTimeFormatter.ofPattern("yyyy~MM~dd"));
        System.out.println("当前日期格式化: " + format2);
        LocalDate tomorrow = currentDate.plusDays(1);
        LocalDate yesterday = currentDate.minusDays(1);
        System.out.println("明天: " + tomorrow + ", 昨天: " + yesterday);

        LocalTime currentTime = LocalTime.now();
        System.out.println("当前时间: " + currentTime);
        System.out.println("当前时间: " + currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("当前日期时间: " + localDateTime);
        System.out.println("当前日期时间转date: " + localDateTime.toLocalDate());
        System.out.println("当前日期时间转time: " + localDateTime.toLocalTime());
        String format3 = localDateTime.format(DateTimeFormatter.ofPattern("yyyy~MM~dd~hh~mm~ss"));
        System.out.println("当前日期时间格式化: " + format3);
        LocalDateTime nextHour = localDateTime.plusHours(1);
        LocalDateTime nextMinute = localDateTime.plusMinutes(1);
        LocalDateTime nextSecond = localDateTime.plusSeconds(1);
        System.out.println("下一个小时: " + nextHour + ", 下一分钟: " + nextMinute + ", 下一个秒: " + nextSecond);

        Instant instant = Instant.now();
        System.out.println("当前时间戳: " + instant);
        System.out.println("当前时间戳毫秒数: " + instant.toEpochMilli());

        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("当前时间戳秒数: " + currentTimeMillis);

        Period period = Period.between(yesterday, tomorrow);
        System.out.println("两个日期相差: " + period);

        Duration duration = Duration.between(currentTime, nextMinute);
        System.out.println("两个时间相差: " + duration);
    }
}
