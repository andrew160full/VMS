import java.util.Random;
import java.util.Vector;

public class StrategyA implements Strategy {
    public Voucher execute(Campaign c) {
        Random r = new Random();
        Vector<User> users = c.getObservers();
        if (users == null)
            return null;
        int randomIndex = r.nextInt(users.size());
        User randomUser = users.get(randomIndex);
        String email = randomUser.getEmail();
        Voucher v = c.generateVoucher(email, "GiftVoucher", 100);
        return v;
    }
}
