import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class StrategyB implements Strategy {
    public Voucher execute(Campaign c) {
        CampaignVoucherMap map = c.getCampaignVoucherMap();
        Iterator<Map.Entry<String, Vector<Voucher>>> it = map.entrySet().iterator();
        int maxUsedVouchers = 0;
        int usedVouchers = 0;
        String email = null;
        while (it.hasNext()) {
            CampaignVoucherMap.ArrayMapEntry entry =
                    (CampaignVoucherMap.ArrayMapEntry) it.next();
            @SuppressWarnings("unchecked")
            Vector<Voucher> vouchers = (Vector<Voucher>) entry.getValue();
            usedVouchers = 0;
            for (Voucher voucher : vouchers) {
                if (voucher.voucher_status == Voucher.VoucherStatusType.USED)
                    usedVouchers++;
            }
            if (usedVouchers > maxUsedVouchers) {
                maxUsedVouchers = usedVouchers;
                email = (String) entry.getKey();
            }
        }
        Voucher v = c.generateVoucher(email, "LoyaltyVoucher", 50);
        return v;

    }
}
