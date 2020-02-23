import java.util.Vector;

/**
 * userEmail, vouchere din campanie
 */
public class CampaignVoucherMap extends ArrayMap<String, Vector<Voucher>> {

    public boolean addVoucher(Voucher v) {
        String email = v.getUser_Email();
        Vector<Voucher> vector = get(email);

        if (vector == null) {
            this.put(v.getUser_Email(), new Vector());
            return false;
        }
        if (vector.contains(v))
            return false;

        vector.add(v);
        put(email, vector);

        return true;
    }
}