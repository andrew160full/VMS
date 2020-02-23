import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.Vector;

public class GuestVouchers extends JFrame {
    private JButton backButton;
    private JTable vouchersTable;
    private JPanel vouchersPanel;
    private VMS vms;
    private User user;
    private LocalDateTime time;

    public GuestVouchers(VMS vms, User user, LocalDateTime time) {
        this();
        this.vms = vms;
        this.user = user;
        this.time = time;
        addVouchersToTable();
    }

    public GuestVouchers() {
        super.setTitle("Vouchers");
        super.setContentPane(vouchersPanel);
        setLocation(this);
        pack();
        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuestVouchers.super.dispose();
                MainMenu mainMenu = new MainMenu(vms, user, time);
                mainMenu.setVisible(true);
                mainMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }

    private void addVouchersToTable() {
        UserVoucherMap map = user.map;
        Vector<Voucher> userVouchers = new Vector<>();

        Iterator<Map.Entry<Integer, Vector
                <Voucher>>> it = user.map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Vector<Voucher>> entry
                    = it.next();
            Vector<Voucher> vouchersVector = entry.
                    getValue();
            if (vouchersVector != null)
                userVouchers.addAll(vouchersVector);
        }
        DefaultTableModel model = (DefaultTableModel) vouchersTable.getModel();
        model.addColumn("Campaign ID");
        model.addColumn("Email");
        model.addColumn("Voucher type");
        model.addColumn("Value");

        model.addRow(new Object[]{"Voucher ID", "Camapign ID", "Email",
                "Voucher type", "Value"});
        for (Voucher v : userVouchers) {
            String campaignID = Integer.toString(v.getCampaignId());
            String email = v.getUser_Email();
            String vType;
            String voucherID = Integer.toString(v.getVoucher_id());
            if (v instanceof GiftVoucher)
                vType = "Gift voucher";
            else
                vType = "Loyalty voucher";
            String value = Float.toString(v.getValue());
            model.addRow(new Object[]{voucherID, campaignID, email, vType,
                    value});
        }
        Timer t;

    }

    public static void main(String[] args) {
        new GuestVouchers();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        vouchersPanel = new JPanel();
        vouchersPanel.setLayout(new BorderLayout(0, 0));
        vouchersPanel.setBackground(new Color(-14401727));
        vouchersPanel.setMinimumSize(new Dimension(960, 720));
        vouchersPanel.setPreferredSize(new Dimension(960, 720));
        backButton = new JButton();
        Font backButtonFont = this.$$$getFont$$$("Lucida Sans Unicode",
                -1, 14, backButton.getFont());
        if (backButtonFont != null) backButton.setFont(backButtonFont);
        backButton.setText("Back");
        vouchersPanel.add(backButton, BorderLayout.NORTH);
        vouchersTable = new JTable();
        vouchersTable.setBackground(new Color(-14402239));
        Font vouchersTableFont = this.$$$getFont$$$("Lucida Sans Unicode",
                -1, 15, vouchersTable.getFont());
        if (vouchersTableFont != null) vouchersTable.setFont(vouchersTableFont);
        vouchersTable.setForeground(new Color(-1));
        vouchersTable.setGridColor(new Color(-4517105));
        vouchersTable.setIntercellSpacing(new Dimension(10, 1));
        vouchersPanel.add(vouchersTable, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font
            currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style :
            currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return vouchersPanel;
    }

    public static void setLocation(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int px = centerPoint.x / 2;
        int py = centerPoint.y / 2;
        frame.setLocation(px, py);
    }

    class ComparatorNameAsc implements Comparator<Campaign> {

        @Override
        public int compare(Campaign o1, Campaign o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
