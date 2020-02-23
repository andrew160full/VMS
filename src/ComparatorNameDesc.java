import java.util.Comparator;

public class ComparatorNameDesc implements Comparator<Campaign> {

    @Override
    public int compare(Campaign o1, Campaign o2) {
        return o2.getName().compareTo(o1.getName());
    }
}