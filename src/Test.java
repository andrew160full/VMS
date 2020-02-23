import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
    public static void main(String[] args) throws IOException {
        int i;
        Scanner scan = new Scanner(System.in);
        System.out.print ("Introduceti indexul testului: ");
        i = scan.nextInt();

        if (i > 9 || i < 0) {
            System.err.println("Index invalid ([0-9])");
            System.exit(0);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter
                ("outputPOO.txt"));
        File campaigns = new File("VMStests\\test0" + i + "\\input\\" +
                "campaigns.txt");
        File events = new File("VMStests\\test0" + i + "\\input\\" +
                "events.txt");
        File users = new File("VMStests\\test0" + i + "\\input\\" +
                "users.txt");
        VMS vms = VMS.getInstance();

        Scanner scanner = new Scanner(campaigns);
        LocalDateTime time;
        int N;
        N = Integer.parseInt(scanner.nextLine());
        String date = scanner.nextLine();

        DateTimeFormatter format = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        time = LocalDateTime.parse(date, format);

        for (int j = 0; j < N; j++) { // N campanii
            String line = scanner.nextLine();
            String[] toks = line.split(";");
            int id = Integer.parseInt(toks[0]);
            String name = toks[1];
            String description = toks[2];
            LocalDateTime start = LocalDateTime.parse(toks[3], format);
            LocalDateTime finish = LocalDateTime.parse(toks[4], format);
            int budget = Integer.parseInt(toks[5]);
            String strategy = toks[6];
            Campaign.strategyType strategy1 = null;

            if (strategy.equals("A"))
                strategy1 = Campaign.strategyType.A;
            else if (strategy.equals("B"))
                strategy1 = Campaign.strategyType.B;
            else if (strategy.equals("C"))
                strategy1 = Campaign.strategyType.C;

            Campaign campaign = new Campaign(id, name, description, start,
                    finish, budget, strategy1);

            vms.addCampaign(campaign);
        }

        scanner = new Scanner(users);
        N = Integer.parseInt(scanner.nextLine());
        Vector<User> allUsers = new Vector<User>();

        for (int j = 0; j < N; j++) { // N users
            String line = scanner.nextLine();
            String[] toks = line.split(";");

            int id = Integer.parseInt(toks[0]);
            String name = toks[1];
            String password = toks[2];
            String email = toks[3];
            User.UserType type = null;
            if (toks[4].equals("GUEST"))
                type = User.UserType.GUEST;
            else if (toks[4].equals("ADMIN"))
                type = User.UserType.ADMIN;

            User user = new User(id, name, password, email, type);
            allUsers.add(user);
            vms.addUser(user);
        }
        scanner = new Scanner(events);
        date = scanner.nextLine();

        format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        time = LocalDateTime.parse(date, format);
        N = Integer.parseInt(scanner.nextLine());

        for (int j = 0; j < N; j++) {
            String line = scanner.nextLine();
            String[] toks = line.split(";");
            int userId = Integer.parseInt(toks[0]);
            User.UserType userType;
            User user = allUsers.elementAt(userId - 1);
            if (user.getType() == User.UserType.ADMIN)
                userType = User.UserType.ADMIN;
            else
                userType = User.UserType.GUEST;

            int campaignId = Integer.MAX_VALUE;
            String campaignName;
            String campaignDescription;
            String startDateString;
            String endDateString;
            int budget;
            Campaign.strategyType strategy;
            String out;

            switch (toks[1]) {

                case "addCampaign":
                    if (userType == User.UserType.GUEST)
                        break;
                    campaignId = Integer.parseInt(toks[2]);
                    campaignName = toks[3];
                    campaignDescription = toks[4];
                    startDateString = toks[5];
                    endDateString = toks[6];
                    budget = Integer.parseInt(toks[7]);

                    if (toks[8].equals("A"))
                        strategy = Campaign.strategyType.A;
                    else if (toks[8].equals("B"))
                        strategy = Campaign.strategyType.B;
                    else
                        strategy = Campaign.strategyType.C;

                    LocalDateTime startDate = LocalDateTime.parse
                            (startDateString, format);
                    LocalDateTime endDate = LocalDateTime.parse
                            (endDateString, format);
                    Campaign c = new Campaign(campaignId, campaignName,
                            campaignDescription, startDate, endDate,
                            budget, strategy);

                    Campaign.CampaignStatusType status = null;

                    if (time.isAfter(endDate))
                        status = Campaign.CampaignStatusType.EXPIRED;
                    else if (time.isBefore(startDate))
                        status = Campaign.CampaignStatusType.NEW;
                    else if (time.isEqual(startDate))
                        status = Campaign.CampaignStatusType.NEW;
                    else if (time.isAfter(startDate) &&
                            time.isBefore(endDate))
                        status = Campaign.CampaignStatusType.STARTED;

                    c.setStatus(status, time);
                    vms.addCampaign(c);

                    break;

                case "editCampaign":
                    if (userType == User.UserType.GUEST)
                        break;
                    campaignId = Integer.parseInt(toks[2]);

                    if (campaignId > vms.getCampaigns().size())
                        break; //daca campaignId incorect
                    campaignName = toks[3];
                    campaignDescription = toks[4];
                    startDateString = toks[5];
                    endDateString = toks[6];
                    budget = Integer.parseInt(toks[7]);
                    startDate = LocalDateTime.parse
                            (startDateString, format);
                    endDate = LocalDateTime.parse
                            (endDateString, format);

                    c = new Campaign(campaignId, campaignName,
                            campaignDescription, startDate, endDate,
                            budget, null);
                    status = null;

                    if (time.isAfter(endDate))
                        status = Campaign.CampaignStatusType.EXPIRED;
                    else if (time.isBefore(startDate))
                        status = Campaign.CampaignStatusType.NEW;
                    else if (time.isEqual(startDate))
                        status = Campaign.CampaignStatusType.NEW;
                    else if (time.isAfter(startDate) &&
                            time.isBefore(endDate))
                        status = Campaign.CampaignStatusType.STARTED;

                    c.setStatus(status, time);
                    vms.updateCampaign(campaignId, c);
                    Campaign old = vms.getCampaign(campaignId);
                    c = old;
                    Notification.NotificationType type =
                            Notification.NotificationType.EDIT;
                    Notification notif = new Notification(campaignId, time,
                            null, type);
                    c.notifyAllObservers(notif);

                    break;

                case "cancelCampaign":
                    if (userType == User.UserType.GUEST)
                        break;

                    campaignId = Integer.parseInt(toks[2]);
                    for (Campaign campaign : vms.getCampaigns())
                        if (campaign.getCampaignId() == campaignId)
                            campaign.setStatus
                                (Campaign.CampaignStatusType.CANCELLED, time);

                    break;

                case "generateVoucher":
                    campaignId = Integer.parseInt(toks[2]);
                    String email = toks[3];
                    String voucherType = toks[4]; //Gift / Loyalty voucher
                    float value = Float.parseFloat(toks[5]);
                    Voucher v = null;

                    if (!voucherType.equals("GiftVoucher") &&
                            !voucherType.equals("LoyaltyVoucher"))
                        break;

                    for (Campaign camp : vms.getCampaigns()) {
                        if (camp.getCampaignId() == campaignId) {
                            if (!camp.map.containsKey(email))
                                camp.map.put(email, new Vector());
                            if (voucherType.equals("GiftVoucher"))
                                v = camp.generateVoucher(email,
                                        "GiftVoucher", value);
                            else
                                v = camp.generateVoucher(email,
                                        "LoyaltyVoucher", value);
                            if (v == null)
                                break;

                            if (userType == User.UserType.GUEST) {
                                camp.removeVoucherUser(v);//nu-l distribuim
                                break;
                            }
                            for (User u : vms.getUsers()) {
                                if (u.getEmail().equals(email)) {
                                    u.map.addVoucher(v);
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    break;

                case "redeemVoucher":
                    if (userType == User.UserType.GUEST)
                        break;

                    campaignId = Integer.parseInt(toks[2]);

                    int voucherId = Integer.parseInt(toks[3]);
                    String localDateString = toks[4];
                    LocalDateTime localDate = LocalDateTime.parse
                            (localDateString, format);
                    String voucherCode = null;

                    for (Campaign camp : vms.getCampaigns()) {
                        if (camp.getCampaignId() == campaignId) {
                            for (Voucher voucher : camp.getVouchers()) {
                                if (voucher.getVoucher_id() == voucherId) {
                                    voucherCode = voucher.getVoucherCode();
                                    break;
                                }
                            }
                            camp.redeemVoucher(voucherCode, localDate);
                        }
                    }

                    break;

                case "getVouchers":
                    if (userType == User.UserType.ADMIN)
                        break;
                    @SuppressWarnings("Unchecked")
                    Vector<Voucher> userVouchers = new Vector<>();

                    for (User u : vms.getUsers()) {
                        if (u.getUserId() == userId) {
                            Iterator<Map.Entry<Integer, Vector
                            <Voucher>>> it = u.map.entrySet().iterator();

                            while (it.hasNext()) {
                                Map.Entry<Integer, Vector<Voucher>> entry
                                        = it.next();
                                Vector<Voucher> vouchersVector = entry.
                                        getValue();
                                if (vouchersVector != null)
                                    userVouchers.addAll(vouchersVector);
                            }
                            break;
                        }
                    }
                    out = printVouchers (userVouchers);
                    writer.write(out);
                    writer.newLine();

                    break;

                case "getObservers":
                    if (userType == User.UserType.GUEST)
                        break;

                    Vector <User> observers = null;
                    campaignId = Integer.parseInt(toks[2]);
                    for (Campaign camp : vms.getCampaigns()) {
                        if (camp.getCampaignId() == campaignId) {
                            observers = camp.getObservers();
                            break;
                        }
                    }
                    out = printObservers(observers);
                    writer.write(out);
                    writer.newLine();

                    break;

                case "getNotifications":
                    if (userType == User.UserType.ADMIN)
                        break;

                    List<Notification> notifications = null;
                    for (User u : vms.getUsers()) {
                        if (u.getUserId() == userId) {
                            notifications = u.getNotifications();
                        }
                    }
                    out = printNotifications(notifications);
                    if (notifications.size() > 0)
                        writer.write(out);
                    else
                        writer.write("[]");
                    writer.newLine();

                    break;

                case "getVoucher":
                    if (userType == User.UserType.GUEST)
                        break;

                    Voucher voucher;
                    campaignId = Integer.parseInt(toks[2]);
                    for (Campaign camp : vms.getCampaigns()) {
                        if (camp.getCampaignId() == campaignId) {
                            voucher = camp.executeStrategy();
                            if (voucher == null)
                                break;
                            out = printVoucher(voucher);
                            writer.write(out);
                            writer.newLine();
                        }
                    }
                    break;
            }
        }
        writer.close();
        scanner.close();
        System.out.println("FINALIZAT");
    }
    private static String printObservers(Vector<User> v) {
        StringBuilder s = new StringBuilder("[");

        for (User user : v) {
            s.append("[");
            s.append(Integer.toString(user.getUserId())).append(';');
            s.append(user.getName()).append(';');
            s.append(user.getEmail()).append(';');
            s.append(user.getType().toString());
            s.append("]");
        }
        s.append(']');
        return s.toString();
    }
    private static String printVouchers(Vector<Voucher> vouchers) {
        StringBuilder s = new StringBuilder("[");
        DateTimeFormatter format = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        for (Voucher v : vouchers) {
            s.append('[');
            s.append(Integer.toString(v.getVoucher_id())).append(';');
            s.append(v.getVoucher_status().toString()).append(';');
            s.append(v.getUser_Email()).append(';');
            s.append(String.format("%.1f", v.getValue())).append(';');
            s.append(Integer.toString(v.getCampaignId())).append(';');
            if (v.getDateUsed() == null)
                s.append("null");
            else
                s.append(v.getDateUsed().format(format).toString());
            s.append(']');
            if (v != vouchers.lastElement())
                s.append(", ");
        }
        s.append(']');

        return s.toString();
    }
    private static String printNotifications(List<Notification> list) {
        StringBuilder s = new StringBuilder();
        if (list.size() > 1)
            s.append("[");
        DateTimeFormatter format = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        for (Notification n : list) {
            s.append('[');
            s.append(Integer.toString(n.getCampaignId())).append(';');
            List<String> codes = n.getVoucher_codes();
            s.append('[');
            for (String code : codes) {
                s.append(code);
                if (codes.lastIndexOf(code) != codes.size() - 1)
                    s.append(", ");
            }
            s.append("];");
            s.append(n.getDateSent().format(format)).append(';');
            s.append(n.getType().toString());
            s.append(']');
        }
        if (list.size() > 1)
            s.append("]");
        return s.toString();
    }
    private static String printVoucher(Voucher v) {
        StringBuilder s = new StringBuilder("[");
        DateTimeFormatter format = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        s.append(v.getVoucher_id()).append(';');
        s.append(v.getVoucher_status().toString()).append(';');
        s.append(v.getUser_Email()).append(';');
        s.append(String.format("%.1f", v.getValue())).append(';');
        s.append(v.getCampaignId()).append(';');
        if (v.getDateUsed() == null)
            s.append("null");
        else
            s.append(v.getDateUsed().format(format).toString());
        s.append(']');

        return s.toString();
    }
}