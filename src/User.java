import java.util.*;

public class User implements Observer {

    private int userId;
    private String name;
    private String email;
    private String password;
    public UserVoucherMap map;
    protected List<Notification> notifications;
    UserType type;

    enum UserType {
        ADMIN, GUEST;
    }

    public User() {
        map = new UserVoucherMap();
        notifications = new Vector<Notification>();
    }

    public User(int userId, String name, String password, String email,
                UserType type) {
        this();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        if (this.password.equals(password))
            return true;
        return false;
    }

    public UserType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public Vector<String> getVoucherCodes(int campaignId) {
        Vector<String> voucherCodes = new Vector<String>();
        Iterator<Map.Entry<Integer, Vector<Voucher>>> it =
                this.map.entrySet().iterator();
        while (it.hasNext()) {
            UserVoucherMap.ArrayMapEntry entry = (UserVoucherMap.ArrayMapEntry)
                    it.next();
            int id = (int) entry.getKey();
            if (id == campaignId) {
                @SuppressWarnings("unchecked")
                Vector<Voucher> vouchers = (Vector<Voucher>) entry.getValue();
                for (Voucher voucher : vouchers) {
                    String code = voucher.getVoucherCode();
                    voucherCodes.add(code);
                }
            }
        }
        return voucherCodes;
    }

    public void update(Observable c, Object arg) { //Campaign, Notification
        Notification n = (Notification) arg;
        Notification notification = new Notification
                (n.getCampaignId(), n.getDateSent(), getVoucherCodes
                        (n.getCampaignId()), n.type);
        notifications.add(notification);
    }

}
