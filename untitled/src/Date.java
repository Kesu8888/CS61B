import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Date {
    public static String getDateNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formatDate = dtf.format(now) + " US EAST";
        return formatDate;
    }
    public static void main (String[] Args) {
        System.out.println(getDateNow());
    }
}
