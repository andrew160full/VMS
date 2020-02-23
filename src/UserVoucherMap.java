import java.util.Vector;

/**
 * campaignId, vouchere din campanii
 */
public class UserVoucherMap extends ArrayMap<Integer, Vector<Voucher>> {

    public boolean addVoucher(Voucher v) {

        int Id = v.getCampaignId();
        Vector<Voucher> vector = super.get(Id);

        if (vector == null || vector.contains(v))
            return false;

        vector.add(v);
        put(Id, vector);

        return true;
    }

}
