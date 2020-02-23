import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class VouchersManagement extends JFrame {
    private JTabbedPane vouchersPane;
    private JPanel vouchersPanel;
    private JButton goBackButton;
    private JTable vouchersTable;
    private JTextField cIDTF;
    private JLabel insertVoucherCodeLabel;
    private JTextField voucherCodeTF;
    private JButton findVoucherButton;
    private JLabel campaignIdLabel;
    private JTextField voucherIdTF;
    private JLabel voucherExistsLabel;
    private JPanel showVouchersPanel;
    private JPanel generateVoucherPanel;
    private JPanel searchVoucherPanel;
    private JPanel backPanel;
    private JLabel campaignIDLabel;
    private JLabel userEmailLabel;
    private JLabel voucherStatusLabel;
    private JLabel dateUserPanel;
    private JTextField campaignIDTF;
    private JTextField userEmailTF;
    private JTextField voucherStatusTF;
    private JTextField dateUsedTF;
    private JButton redeemVoucherButton;
    private JLabel insertVoucherLabel;
    private JLabel campaignIdL;
    private JLabel userEmailL;
    private JLabel voucherTypeL;
    private JLabel valueL;
    private JTextField emailTF;
    private JTextField voucherTypeTF;
    private JTextField voucherValueTF;
    private JButton generateVoucherButton;
    private JButton generateVouchersButton;
    private JTextField generateVouchersTF;
    private VMS vms;
    private User user;
    private LocalDateTime time;

    public VouchersManagement(VMS vms, User user, LocalDateTime time) {
        this();
        this.vms = vms;
        this.user = user;
        this.time = time;
        addVouchersToTable();
    }

    public VouchersManagement() {
        super.setTitle("Vouchers");
        super.setContentPane(vouchersPanel);
        voucherExistsLabel.setVisible(false);
        setLocation(this);
        pack();
        setVisible(true);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VouchersManagement.super.dispose();
                MainMenu mainMenu = new MainMenu(vms, user, time);
                mainMenu.setVisible(true);
                mainMenu.show();
                mainMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
        voucherCodeTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    findVoucher();
            }
        });
        findVoucherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findVoucher();
            }
        });
        redeemVoucherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redeemVoucher();
            }
        });
        generateVoucherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateVoucher();
            }
        });
        voucherValueTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    generateVoucher();
            }
        });
        generateVouchersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!generateVouchersTF.getText().isEmpty())
                        generateAllVouchers();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        generateVouchersTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        if (!generateVouchersTF.getText().isEmpty())
                            generateAllVouchers();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    private void generateAllVouchers() throws IOException {
        int campaignId = Integer.parseInt(generateVouchersTF.getText());
        File emails = new File("emails.txt");
        Scanner sc = new Scanner(emails);
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] toks = line.split(";");
            String email = toks[0];
            String voucherType = toks[1];
            float value = Float.parseFloat(toks[2]);
            Voucher v = null;
            for (Campaign camp : vms.getCampaigns()) {
                if (camp.getCampaignId() == campaignId) {
                    if (!camp.map.containsKey(email))
                        camp.map.put(email, new Vector<>());
                    if (voucherType.equals("GiftVoucher"))
                        v = camp.generateVoucher(email,
                                "GiftVoucher", value);
                    else
                        v = camp.generateVoucher(email,
                                "LoyaltyVoucher", value);
                    if (v == null)
                        break;
                }
                if (v != null) {
                    for (User u : vms.getUsers()) {
                        if (u.getEmail().equals(email)) {
                            u.map.addVoucher(v);
                            break;
                        }
                    }
                }
            }
        }
        addVouchersToTable();
    }

    private void generateVoucher() {
        int campaignId = Integer.parseInt(cIDTF.getText());
        String email = emailTF.getText();
        String voucherType = voucherTypeTF.getText();
        float value = Float.parseFloat(voucherValueTF.getText());
        Voucher v = null;

        for (Campaign camp : vms.getCampaigns()) {
            if (camp.getCampaignId() == campaignId) {
                if (!camp.map.containsKey(email))
                    camp.map.put(email, new Vector<>());
                if (voucherType.equals("GiftVoucher"))
                    v = camp.generateVoucher(email,
                            "GiftVoucher", value);
                else
                    v = camp.generateVoucher(email,
                            "LoyaltyVoucher", value);
                if (v == null)
                    break;
            }
            if (v != null) {
                for (User u : vms.getUsers()) {
                    if (u.getEmail().equals(email)) {
                        u.map.addVoucher(v);
                        break;
                    }
                }
            }
        }
        addVouchersToTable();
    }

    private void addVouchersToTable() {
        Vector<Voucher> allVouchers = new Vector<>();

        DefaultTableModel model = (DefaultTableModel) vouchersTable.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);

        model.addColumn("Voucher ID");
        model.addColumn("Campaign ID");
        model.addColumn("User Email");
        model.addColumn("Voucher Type");
        model.addColumn("Value");

        model.addRow(new Object[]{"Voucher ID", "Campaign ID",
                "User Email", "Voucher Type", "Value"});

        for (User u : vms.getUsers()) {
            Iterator<Map.Entry<Integer, Vector
                    <Voucher>>> it = u.map.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Integer, Vector<Voucher>> entry = it.next();
                Vector<Voucher> vouchersVector = entry.getValue();
                if (vouchersVector != null)
                    allVouchers.addAll(vouchersVector);
            }
        }
        for (Voucher v : allVouchers) {
            String userId = Integer.toString(v.getVoucher_id());
            String campaignID = Integer.toString(v.getCampaignId());
            String email = v.getUser_Email();
            String type;
            if (v instanceof GiftVoucher)
                type = "Gift Voucher";
            else
                type = "Loyalty Voucher";
            String value = Float.toString(v.getValue());

            model.addRow(new Object[]{userId, campaignID, email, type,
                    value});
        }

    }

    private void redeemVoucher() {
        int voucherId = Integer.parseInt(voucherIdTF.getText());
        int campaignId = Integer.parseInt(campaignIDTF.getText());
        String voucherCode = voucherCodeTF.getText();

        for (Campaign camp : vms.getCampaigns()) {
            if (camp.getCampaignId() == campaignId) {
                for (Voucher voucher : camp.getVouchers()) {
                    if (voucher.getVoucher_id() == voucherId) {
                        voucherCode = voucher.getVoucherCode();
                        break;
                    }
                }
                camp.redeemVoucher(voucherCode, time);
            }
        }
    }

    private void findVoucher() {
        String voucherCode = voucherCodeTF.getText();
        Voucher voucher = null;

        for (Campaign c : vms.getCampaigns()) {
            Vector<Voucher> vouchers = c.getVouchers();
            for (Voucher v : vouchers) {
                if (v.getVoucherCode().equals(voucherCode)) {
                    voucher = v;
                    break;
                }
            }
        }
        if (voucher != null) {
            voucherExistsLabel.setVisible(true);
            voucherIdTF.setText(Integer.toString(voucher.getVoucher_id()));
            campaignIDTF.setText(Integer.toString(voucher.getCampaignId()));
            userEmailTF.setText(voucher.getUser_Email());
            if (voucher.getVoucher_status() != null)
                voucherStatusTF.setText(voucher.getVoucher_status().
                        toString());
            else
                voucherStatusTF.setText("None");
            if (voucher.getDateUsed() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                        ("yyyy-MM-dd HH:mm");
                String dateString = voucher.getDateUsed().format(formatter);
                dateUsedTF.setText(dateString);
            } else
                dateUsedTF.setText("None");
        } else
            voucherExistsLabel.setVisible(false);
    }

    public static void main(String[] args) {
        new VouchersManagement();
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
        vouchersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        vouchersPanel.setBackground(new Color(-14402239));
        vouchersPanel.setMinimumSize(new Dimension(960, 720));
        vouchersPanel.setPreferredSize(new Dimension(960, 720));
        vouchersPane = new JTabbedPane();
        vouchersPane.setBackground(new Color(-1));
        Font vouchersPaneFont = this.$$$getFont$$$(null, -1, 14, vouchersPane.getFont());
        if (vouchersPaneFont != null) vouchersPane.setFont(vouchersPaneFont);
        vouchersPane.setForeground(new Color(-4517105));
        vouchersPane.setOpaque(true);
        vouchersPane.setPreferredSize(new Dimension(960, 720));
        vouchersPanel.add(vouchersPane);
        showVouchersPanel = new JPanel();
        showVouchersPanel.setLayout(new GridBagLayout());
        showVouchersPanel.setBackground(new Color(-14402239));
        vouchersPane.addTab("Show vouchers", showVouchersPanel);
        vouchersTable = new JTable();
        vouchersTable.setBackground(new Color(-14402239));
        Font vouchersTableFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 15, vouchersTable.getFont());
        if (vouchersTableFont != null) vouchersTable.setFont(vouchersTableFont);
        vouchersTable.setForeground(new Color(-1));
        vouchersTable.setGridColor(new Color(-4517105));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        showVouchersPanel.add(vouchersTable, gbc);
        generateVoucherPanel = new JPanel();
        generateVoucherPanel.setLayout(new GridBagLayout());
        generateVoucherPanel.setBackground(new Color(-14402239));
        vouchersPane.addTab("Generate voucher", generateVoucherPanel);
        insertVoucherLabel = new JLabel();
        Font insertVoucherLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, insertVoucherLabel.getFont());
        if (insertVoucherLabelFont != null) insertVoucherLabel.setFont(insertVoucherLabelFont);
        insertVoucherLabel.setForeground(new Color(-1));
        insertVoucherLabel.setText("Insert voucher details:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        generateVoucherPanel.add(insertVoucherLabel, gbc);
        campaignIdL = new JLabel();
        Font campaignIdLFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIdL.getFont());
        if (campaignIdLFont != null) campaignIdL.setFont(campaignIdLFont);
        campaignIdL.setForeground(new Color(-1));
        campaignIdL.setText("Campaign ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(campaignIdL, gbc);
        cIDTF = new JTextField();
        Font cIDTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, cIDTF.getFont());
        if (cIDTFFont != null) cIDTF.setFont(cIDTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        generateVoucherPanel.add(cIDTF, gbc);
        generateVoucherButton = new JButton();
        Font generateVoucherButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, generateVoucherButton.getFont());
        if (generateVoucherButtonFont != null) generateVoucherButton.setFont(generateVoucherButtonFont);
        generateVoucherButton.setText("Generate voucher");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 40, 10);
        generateVoucherPanel.add(generateVoucherButton, gbc);
        userEmailL = new JLabel();
        Font userEmailLFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, userEmailL.getFont());
        if (userEmailLFont != null) userEmailL.setFont(userEmailLFont);
        userEmailL.setForeground(new Color(-1));
        userEmailL.setText("Email:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(userEmailL, gbc);
        voucherTypeL = new JLabel();
        Font voucherTypeLFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherTypeL.getFont());
        if (voucherTypeLFont != null) voucherTypeL.setFont(voucherTypeLFont);
        voucherTypeL.setForeground(new Color(-1));
        voucherTypeL.setText("Voucher type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(voucherTypeL, gbc);
        valueL = new JLabel();
        Font valueLFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, valueL.getFont());
        if (valueLFont != null) valueL.setFont(valueLFont);
        valueL.setForeground(new Color(-1));
        valueL.setText("Value:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(valueL, gbc);
        emailTF = new JTextField();
        Font emailTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, emailTF.getFont());
        if (emailTFFont != null) emailTF.setFont(emailTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        generateVoucherPanel.add(emailTF, gbc);
        voucherTypeTF = new JTextField();
        Font voucherTypeTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherTypeTF.getFont());
        if (voucherTypeTFFont != null) voucherTypeTF.setFont(voucherTypeTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        generateVoucherPanel.add(voucherTypeTF, gbc);
        voucherValueTF = new JTextField();
        Font voucherValueTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherValueTF.getFont());
        if (voucherValueTFFont != null) voucherValueTF.setFont(voucherValueTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        generateVoucherPanel.add(voucherValueTF, gbc);
        generateVouchersButton = new JButton();
        Font generateVouchersButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, generateVouchersButton.getFont());
        if (generateVouchersButtonFont != null) generateVouchersButton.setFont(generateVouchersButtonFont);
        generateVouchersButton.setText("Generate vouchers");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(generateVouchersButton, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Campaign ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        generateVoucherPanel.add(label1, gbc);
        generateVouchersTF = new JTextField();
        Font generateVouchersTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, generateVouchersTF.getFont());
        if (generateVouchersTFFont != null) generateVouchersTF.setFont(generateVouchersTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        generateVoucherPanel.add(generateVouchersTF, gbc);
        searchVoucherPanel = new JPanel();
        searchVoucherPanel.setLayout(new GridBagLayout());
        searchVoucherPanel.setBackground(new Color(-14402239));
        vouchersPane.addTab("Search voucher", searchVoucherPanel);
        insertVoucherCodeLabel = new JLabel();
        Font insertVoucherCodeLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, insertVoucherCodeLabel.getFont());
        if (insertVoucherCodeLabelFont != null) insertVoucherCodeLabel.setFont(insertVoucherCodeLabelFont);
        insertVoucherCodeLabel.setForeground(new Color(-1));
        insertVoucherCodeLabel.setText("Insert voucher code:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        searchVoucherPanel.add(insertVoucherCodeLabel, gbc);
        voucherCodeTF = new JTextField();
        Font voucherCodeTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherCodeTF.getFont());
        if (voucherCodeTFFont != null) voucherCodeTF.setFont(voucherCodeTFFont);
        voucherCodeTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 10);
        searchVoucherPanel.add(voucherCodeTF, gbc);
        findVoucherButton = new JButton();
        Font findVoucherButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, findVoucherButton.getFont());
        if (findVoucherButtonFont != null) findVoucherButton.setFont(findVoucherButtonFont);
        findVoucherButton.setText("Find voucher");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 50, 10);
        searchVoucherPanel.add(findVoucherButton, gbc);
        campaignIdLabel = new JLabel();
        Font campaignIdLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIdLabel.getFont());
        if (campaignIdLabelFont != null) campaignIdLabel.setFont(campaignIdLabelFont);
        campaignIdLabel.setForeground(new Color(-1));
        campaignIdLabel.setText("Voucher ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        searchVoucherPanel.add(campaignIdLabel, gbc);
        voucherIdTF = new JTextField();
        voucherIdTF.setEditable(false);
        Font voucherIdTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherIdTF.getFont());
        if (voucherIdTFFont != null) voucherIdTF.setFont(voucherIdTFFont);
        voucherIdTF.setForeground(new Color(-4517105));
        voucherIdTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        searchVoucherPanel.add(voucherIdTF, gbc);
        campaignIDLabel = new JLabel();
        Font campaignIDLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIDLabel.getFont());
        if (campaignIDLabelFont != null) campaignIDLabel.setFont(campaignIDLabelFont);
        campaignIDLabel.setForeground(new Color(-1));
        campaignIDLabel.setText("Campaign ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        searchVoucherPanel.add(campaignIDLabel, gbc);
        userEmailLabel = new JLabel();
        Font userEmailLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, userEmailLabel.getFont());
        if (userEmailLabelFont != null) userEmailLabel.setFont(userEmailLabelFont);
        userEmailLabel.setForeground(new Color(-1));
        userEmailLabel.setText("User email:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        searchVoucherPanel.add(userEmailLabel, gbc);
        voucherStatusLabel = new JLabel();
        Font voucherStatusLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherStatusLabel.getFont());
        if (voucherStatusLabelFont != null) voucherStatusLabel.setFont(voucherStatusLabelFont);
        voucherStatusLabel.setForeground(new Color(-1));
        voucherStatusLabel.setText("Voucher status:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        searchVoucherPanel.add(voucherStatusLabel, gbc);
        dateUserPanel = new JLabel();
        Font dateUserPanelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, dateUserPanel.getFont());
        if (dateUserPanelFont != null) dateUserPanel.setFont(dateUserPanelFont);
        dateUserPanel.setForeground(new Color(-1));
        dateUserPanel.setText("Date used:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        searchVoucherPanel.add(dateUserPanel, gbc);
        campaignIDTF = new JTextField();
        campaignIDTF.setEditable(false);
        Font campaignIDTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIDTF.getFont());
        if (campaignIDTFFont != null) campaignIDTF.setFont(campaignIDTFFont);
        campaignIDTF.setForeground(new Color(-4517105));
        campaignIDTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        searchVoucherPanel.add(campaignIDTF, gbc);
        userEmailTF = new JTextField();
        userEmailTF.setEditable(false);
        Font userEmailTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, userEmailTF.getFont());
        if (userEmailTFFont != null) userEmailTF.setFont(userEmailTFFont);
        userEmailTF.setForeground(new Color(-4517105));
        userEmailTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        searchVoucherPanel.add(userEmailTF, gbc);
        voucherStatusTF = new JTextField();
        voucherStatusTF.setEditable(false);
        Font voucherStatusTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, voucherStatusTF.getFont());
        if (voucherStatusTFFont != null) voucherStatusTF.setFont(voucherStatusTFFont);
        voucherStatusTF.setForeground(new Color(-4517105));
        voucherStatusTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        searchVoucherPanel.add(voucherStatusTF, gbc);
        dateUsedTF = new JTextField();
        dateUsedTF.setEditable(false);
        Font dateUsedTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, dateUsedTF.getFont());
        if (dateUsedTFFont != null) dateUsedTF.setFont(dateUsedTFFont);
        dateUsedTF.setForeground(new Color(-4517105));
        dateUsedTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        searchVoucherPanel.add(dateUsedTF, gbc);
        redeemVoucherButton = new JButton();
        Font redeemVoucherButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, redeemVoucherButton.getFont());
        if (redeemVoucherButtonFont != null) redeemVoucherButton.setFont(redeemVoucherButtonFont);
        redeemVoucherButton.setText("Redeem voucher");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(40, 0, 50, 10);
        searchVoucherPanel.add(redeemVoucherButton, gbc);
        voucherExistsLabel = new JLabel();
        Font voucherExistsLabelFont = this.$$$getFont$$$(null, -1, 14, voucherExistsLabel.getFont());
        if (voucherExistsLabelFont != null) voucherExistsLabel.setFont(voucherExistsLabelFont);
        voucherExistsLabel.setForeground(new Color(-4517105));
        voucherExistsLabel.setText("Voucher not found");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        searchVoucherPanel.add(voucherExistsLabel, gbc);
        backPanel = new JPanel();
        backPanel.setLayout(new GridBagLayout());
        backPanel.setBackground(new Color(-14402239));
        vouchersPane.addTab("Back", backPanel);
        goBackButton = new JButton();
        Font goBackButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 24, goBackButton.getFont());
        if (goBackButtonFont != null) goBackButton.setFont(goBackButtonFont);
        goBackButton.setText("Go back");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 120;
        gbc.ipady = 80;
        backPanel.add(goBackButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
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
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return vouchersPanel;
    }

    private static void setLocation(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int px = centerPoint.x / 2;
        int py = centerPoint.y / 2;
        frame.setLocation(px, py);
    }

}
