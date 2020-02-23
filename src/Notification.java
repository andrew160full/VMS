import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

public class Notification {

    private int campaignId;
    private LocalDateTime dateSent;
    private List<String> voucher_codes = null;
    NotificationType type;

    enum NotificationType {
        EDIT, CANCEL
    }

    public Notification() {
        voucher_codes = new Vector<String>();
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public NotificationType getType() {
        return type;
    }

    public List<String> getVoucher_codes() {
        return voucher_codes;
    }

    public Notification(int campaignId, LocalDateTime dateSent, List<String>
            voucher_codes, NotificationType type) {
        this();
        this.campaignId = campaignId;
        this.dateSent = dateSent;
        this.voucher_codes = voucher_codes;
        this.type = type;
    }
}
