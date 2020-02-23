import java.time.LocalDateTime;
import java.util.Comparator;

public class ComparatorTimeDesc implements Comparator<Campaign> {

    @Override
    public int compare(Campaign o1, Campaign o2) {
        LocalDateTime time1 = o1.getStart();
        LocalDateTime time2 = o2.getStart();
        return time2.compareTo(time1);
    }
}