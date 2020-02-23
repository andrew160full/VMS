import java.time.LocalDateTime;

public abstract class Voucher {
    private int voucher_id; //de la 1
    private String voucher_code;
    VoucherStatusType voucher_status;
    private String user_email;
    private int campaignId;
    private LocalDateTime dateUsed;

    enum VoucherStatusType {
        USED, UNUSED
    }

    public Voucher(int id, int campaignId, String code, String email) {

        this.voucher_id = id;
        this.campaignId = campaignId;
        this.voucher_code = code;
        this.user_email = email;
        this.voucher_status = VoucherStatusType.UNUSED;
        this.dateUsed = null;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public String getVoucherCode() {
        return voucher_code;
    }

    public LocalDateTime getDateUsed() {
        return dateUsed;
    }

    public String getUser_Email() {
        return user_email;
    }

    public int getVoucher_id() {
        return voucher_id;
    }

    public abstract float getValue ();

    public VoucherStatusType getVoucher_status() {
        return voucher_status;
    }

    public void setDateUsed(LocalDateTime dateUsed) {
        this.dateUsed = dateUsed;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}

class GiftVoucher extends Voucher {
    private float sum; //poate fi folosit in campanie doar o data - suma

    public GiftVoucher(int id, int campaignId, String code, String email,
                       float sum) {
        super(id, campaignId, code, email);
        this.sum = sum;
    }

    public float getValue () {
        return sum;
    }
}

class LoyalityVoucher extends Voucher {
    private float value; //poate fi folosit in campanie doar o data - reducere

    public LoyalityVoucher(int id, int campaignId, String code, String email,
                           float value) {
        super(id, campaignId, code, email);
        this.value = value;
    }

    public float getValue () {
        return value;
    }
}
