import java.util.Comparator;

public class ComparatorNameAsc implements Comparator<Campaign> {

    @Override
    public int compare(Campaign o1, Campaign o2) {
        return o1.getName().compareTo(o2.getName());
    }
}

