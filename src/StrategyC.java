import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class StrategyC implements Strategy {
    public Voucher execute(Campaign c) {
        Vector<User> users = c.getObservers();

        int leastVouchers = Integer.MAX_VALUE;
        String email = null;

        for (User user : users) {
            Iterator<Map.Entry<Integer, Vector<Voucher>>> it =
                    user.map.entrySet().iterator();
            while (it.hasNext()) {
                UserVoucherMap.ArrayMapEntry entry =
                        (UserVoucherMap.ArrayMapEntry) it.next();
                int userCampaignId = (int) entry.getKey();
                if (userCampaignId == c.getCampaignId()) {
                    @SuppressWarnings("unchecked")
                    Vector<Voucher> vouchers = (Vector<Voucher>) entry.getValue();
                    if (vouchers.size() < leastVouchers) {
                        leastVouchers = vouchers.size();
                        email = user.getEmail();
                        break;
                    }
                }
            }
        }
        if (leastVouchers != Integer.MAX_VALUE) {
            Voucher v = c.generateVoucher(email, "GiftVoucher", 100);
            return v;
        }

        return null;
    }
}
