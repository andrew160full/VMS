import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;


public class Campaign extends Observable {

    private Vector<User> observers;
    private String name;
    private String description;
    private int campaignId;
    private LocalDateTime start;
    private LocalDateTime finish;
    private int NoTotalVouchers;
    private int NoAvailableVouchers;
    private int lastVoucherId = 1;
    public CampaignVoucherMap map;
    private CampaignStatusType status;
    private strategyType strategy = null;

    enum strategyType {
        A, B, C
    }

    enum CampaignStatusType {
        NEW, STARTED, EXPIRED, CANCELLED
    }

    public Campaign() {
        observers = new Vector<>();
        map = new CampaignVoucherMap();
    }

    public Campaign(int id, String name, String description, LocalDateTime
            start, LocalDateTime finish, int NoAvailableVouchers,
                    strategyType s) {
        this();
        campaignId = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.finish = finish;
        this.NoAvailableVouchers = NoAvailableVouchers;
        this.strategy = s;
        if (NoAvailableVouchers > NoTotalVouchers)
            NoTotalVouchers = NoAvailableVouchers;
    }

    public String getName () {
        return this.name;
    }

    public int getAvailable() {
        return NoAvailableVouchers;
    }

    public int getTotal() {
        return NoTotalVouchers;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public CampaignStatusType getStatus() {
        return status;
    }

    public strategyType getStrategy() {
        return strategy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(int available) {
        NoAvailableVouchers = available;
    }

    public void setTotal(int total) {
        NoTotalVouchers = total;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    public void setStatus(CampaignStatusType status, LocalDateTime dateSent) {
        if (this.status != status) {
            this.status = status;
            Notification.NotificationType type;
            if (status == CampaignStatusType.CANCELLED)
                type = Notification.NotificationType.
                        CANCEL;
            else return;

            Notification n = new Notification(campaignId, dateSent, null, type);
            notifyAllObservers(n);
        }
    }

    public void setStrategy(strategyType strategy) {
        this.strategy = strategy;
    }

    public Vector<Voucher> getVouchers() {
        Vector<Voucher> allVouchers = new Vector<Voucher>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            ArrayMap.ArrayMapEntry pair = (ArrayMap.ArrayMapEntry) it.next();
            @SuppressWarnings("unchecked")
            Vector<Voucher> vouchers = (Vector<Voucher>) pair.getValue();
            allVouchers.addAll(vouchers);
        }
        return allVouchers;
    }

    public Voucher getVoucher(String code) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            ArrayMap.ArrayMapEntry entry = (ArrayMap.ArrayMapEntry) it.next();
            Vector<Voucher> vouchers = (Vector<Voucher>) entry.getValue();
            for (Voucher voucher : vouchers) {
                if (voucher.getVoucherCode() == code)
                    return voucher;
            }
        }
        return null;
    }

    public CampaignVoucherMap getCampaignVoucherMap() {
        return map;
    }

    public Voucher generateVoucher(String email, String voucherType,
                                   float value) {
        if (NoAvailableVouchers == 0)
            return null;

        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            ArrayMap.ArrayMapEntry entry = (ArrayMap.ArrayMapEntry) it.next();
            if (email.equals(entry.getKey())) { //atunci email valid
                Voucher voucher = null;
                String code = Integer.toString(lastVoucherId);
                if (voucherType.equals("GiftVoucher"))
                    voucher = new GiftVoucher(lastVoucherId, campaignId,
                            code, email, value);
                else if (voucherType.equals("LoyaltyVoucher"))
                    voucher = new LoyalityVoucher(lastVoucherId, campaignId,
                            code, email, value);
                if (voucher != null) {
                    for (User user : VMS.getInstance().getUsers()) {
                        if (email.equals(user.getEmail())) {
                            Vector<Voucher> vouchers = user.map.get
                                    (campaignId);
                            if (vouchers != null)
                                vouchers.add(voucher);
                            else
                                vouchers = new Vector ();
                            user.map.put(campaignId, vouchers);
                            user.map.addVoucher(voucher);
                            if (!observers.contains(user))
                                addObserver(user);
                            map.addVoucher(voucher);
                        }
                    }
                    NoAvailableVouchers--; //vouchere disponibile
                    lastVoucherId++;
                }
                return voucher;
            }
        }
        return null;
    }

    public void removeVoucherUser(Voucher v) {
        if (v == null)
            return;
        int campaignIdV = v.getCampaignId();

        if (campaignIdV != campaignId)
            return;
        String email = v.getUser_Email();
        for (User user : VMS.getInstance().getUsers()) {
            if (user.getEmail().equals(email)) {
                Vector<Voucher> vouchers = (Vector <Voucher>)user.map.
                        entrySet().iterator().next().getValue();
                if (user.map.size() == 1) {
                    if (vouchers.size() == 1) {
                        if (vouchers.get(0) == v)
                            removeObserver(user);
                    }
                } else if (user.map.size() == 0)
                    removeObserver(user);
                vouchers = map.get(email);
                map.remove(email, vouchers);
                vouchers.remove(v);
                map.put(email, vouchers);
                vouchers = user.map.get(campaignId);
                user.map.remove(campaignId, vouchers);
                vouchers.remove(v);
                user.map.put(campaignId, vouchers);

                break;
            }
        }
    }

    //marcheaza voucher ca utilizat
    public void redeemVoucher(String code, LocalDateTime date) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            ArrayMap.ArrayMapEntry entry = (ArrayMap.ArrayMapEntry) it.next();
            @SuppressWarnings("unchecked")
            Vector<Voucher> vouchers = (Vector<Voucher>) entry.getValue();
            for (Voucher voucher : vouchers) {
                if (voucher.getVoucherCode().equals(code)) {
                    if (voucher.voucher_status ==
                            Voucher.VoucherStatusType.UNUSED) {
                        String email = (String) entry.getKey();
                        vouchers.remove(voucher);
                        voucher.voucher_status = Voucher.
                                VoucherStatusType.USED;
                        voucher.setDateUsed(date);
                        vouchers.add(voucher);
                        map.put(email, vouchers);
                    }
                    return;
                }
            }
        }
    }

    public Vector<User> getObservers() {
        return observers;
    }

    public void addObserver(User user) {
        if (!observers.contains(user))
            observers.add(user);
    }

    public void removeObserver(User user) {
        if (observers.contains(user)) {
            observers.remove(user);
        }
    }

    public void notifyAllObservers(Notification notification) {
        for (User user : observers)
            user.update(this, notification);
    }

    public Voucher executeStrategy() {
        if (status == CampaignStatusType.CANCELLED)
            return null;
        if (strategy == strategyType.A)
            return (new StrategyA()).execute(this);
        else if (strategy == strategyType.B)
            return (new StrategyB()).execute(this);
        else if (strategy == strategyType.C)
            return (new StrategyC()).execute(this);

        return null;
    }

}
