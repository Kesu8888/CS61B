package gitlet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    public static String getDateNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now) + " +0800";
    }
}
