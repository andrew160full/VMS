import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;


public class LoginMenu extends JFrame {
    private JPanel panel1;
    private JButton loadButton;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel invalidLoginLabel;
    private JLabel filesLoadedLabel;
    private boolean validLogin = false;
    private boolean invalidatedLogin = false;
    private VMS vms;
    private LocalDateTime time = null;
    private User mainUser = null;


    public LoginMenu(VMS vms, User mainUser, LocalDateTime time) throws
            IOException {
        this();
        this.vms = vms;
        this.mainUser = mainUser;
        this.time = time;
        filesLoadedLabel.setVisible(true);
    }
    public LoginMenu() throws IOException {
        time = LocalDateTime.now();
        super.setTitle("Login");
        super.setContentPane(panel1);
        setLocation(this);
        super.pack();
        super.setVisible(true);
        invalidLoginLabel.setVisible(false);
        filesLoadedLabel.hide();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkLogin();
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File campaignsFile = new File("campaigns.txt");
                File usersFile = new File("users.txt");
                if (campaignsFile.exists() && usersFile.exists()) {
                    filesLoadedLabel.setText("Files loaded successfully");
                    filesLoadedLabel.setForeground(new Color(-196780));
                    filesLoadedLabel.setVisible(true);

                    vms = VMS.getInstance();
                    try {
                        Scanner scanner = new Scanner(campaignsFile);
                        int N;
                        N = Integer.parseInt(scanner.nextLine());
                        String date = scanner.nextLine();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                                ("yyyy-MM-dd HH:mm");

                        for (int j = 0; j < N; j++) { // N campanii
                            String line = scanner.nextLine();
                            String[] toks = line.split(";");
                            int id = Integer.parseInt(toks[0]);
                            String name = toks[1];
                            String description = toks[2];
                            LocalDateTime start = LocalDateTime.parse(toks[3], formatter);
                            LocalDateTime finish = LocalDateTime.parse(toks[4], formatter);
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
                            Campaign.CampaignStatusType status = null;

                            if (time.isAfter(finish))
                                status = Campaign.CampaignStatusType.EXPIRED;
                            else if (time.isBefore(start))
                                status = Campaign.CampaignStatusType.NEW;
                            else if (time.isEqual(start))
                                status = Campaign.CampaignStatusType.NEW;
                            else if (time.isAfter(start) &&
                                    time.isBefore(finish))
                                status = Campaign.CampaignStatusType.STARTED;

                            campaign.setStatus(status, time);

                            vms.addCampaign(campaign);
                        }

                            scanner = new Scanner(usersFile);
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
                                vms.addUser(user);
                            }

                    } catch (IOException e1) {
                        System.out.println(e1.getStackTrace());
                    }


                } else {
                    filesLoadedLabel.setText("Failed loading files");
                    filesLoadedLabel.setForeground(new Color(-4517105));
                    filesLoadedLabel.setVisible(true);
                }

            }
        });
    }

    public static void main(String[] args) {
        try {
            new LoginMenu();
        } catch (IOException e2) {
            System.out.println(e2.getStackTrace());
        }
    }

    private void checkLogin() {
        String userName = userField.getText();
        String pass = passwordField.getText();

        if (vms != null) {
            Vector<User> users = vms.getUsers();
            for (User u : users) {
                if (u.getName().equals(userName)) {
                    if (u.checkPassword(pass)) {
                        mainUser = u;
                        validLogin = true;
                    } else
                        break;
                }
            }
        }

        if (validLogin) {
            LoginMenu.super.dispose();
            MainMenu mainMenu = new MainMenu(vms, mainUser, time);
            setLocation(mainMenu);
            mainMenu.setVisible(true);
            mainMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);

        } else if (!invalidatedLogin) {
            Border border = invalidLoginLabel.getBorder();
            Border margin = new EmptyBorder(0, 0, 10, 0);
            invalidLoginLabel.setBorder(new CompoundBorder(border, margin));
            invalidLoginLabel.setVisible(true);
            super.show();
            invalidatedLogin = true;
        }
    }

    public static void setLocation(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int px = centerPoint.x / 2;
        int py = centerPoint.y / 2;
        frame.setLocation(px, py);
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
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setAlignmentX(0.0f);
        panel1.setBackground(new Color(-14402239));
        panel1.setEnabled(true);
        panel1.setMaximumSize(new Dimension(1920, 1080));
        panel1.setMinimumSize(new Dimension(640, 480));
        panel1.setOpaque(true);
        panel1.setPreferredSize(new Dimension(640, 480));
        filesLoadedLabel = new JLabel();
        filesLoadedLabel.setEnabled(true);
        filesLoadedLabel.setForeground(new Color(-196780));
        filesLoadedLabel.setText("Files loaded successfully");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 10;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel1.add(filesLoadedLabel, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Username:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 10;
        gbc.insets = new Insets(0, 0, 2, 0);
        panel1.add(label1, gbc);
        loadButton = new JButton();
        loadButton.setAlignmentX(0.0f);
        loadButton.setDoubleBuffered(false);
        loadButton.setEnabled(true);
        Font loadButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, loadButton.getFont());
        if (loadButtonFont != null) loadButton.setFont(loadButtonFont);
        loadButton.setHideActionText(false);
        loadButton.setHorizontalAlignment(0);
        loadButton.setIconTextGap(4);
        loadButton.setMargin(new Insets(0, 0, 0, 0));
        loadButton.setRequestFocusEnabled(true);
        loadButton.setSelected(false);
        loadButton.setText("Load files");
        loadButton.setToolTipText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 40;
        gbc.ipady = 10;
        panel1.add(loadButton, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1));
        label2.setText("Password:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 10;
        gbc.insets = new Insets(0, 0, 2, 0);
        panel1.add(label2, gbc);
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel1.add(passwordField, gbc);
        userField = new JTextField();
        userField.setFocusAccelerator('0');
        Font userFieldFont = this.$$$getFont$$$("Georgia", -1, 14, userField.getFont());
        if (userFieldFont != null) userField.setFont(userFieldFont);
        userField.setName("");
        userField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel1.add(userField, gbc);
        final JSeparator separator1 = new JSeparator();
        separator1.setBackground(new Color(-16777216));
        separator1.setForeground(new Color(-16777216));
        separator1.setMinimumSize(new Dimension(1, 1));
        separator1.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel1.add(separator1, gbc);
        final JSeparator separator2 = new JSeparator();
        separator2.setBackground(new Color(-16777216));
        separator2.setForeground(new Color(-16777216));
        separator2.setMinimumSize(new Dimension(1, 1));
        separator2.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel1.add(separator2, gbc);
        loginButton = new JButton();
        loginButton.setAlignmentX(0.0f);
        loginButton.setDoubleBuffered(false);
        loginButton.setEnabled(true);
        Font loginButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, loginButton.getFont());
        if (loginButtonFont != null) loginButton.setFont(loginButtonFont);
        loginButton.setHideActionText(false);
        loginButton.setHorizontalAlignment(0);
        loginButton.setIconTextGap(4);
        loginButton.setMargin(new Insets(0, 0, 0, 0));
        loginButton.setRequestFocusEnabled(true);
        loginButton.setSelected(false);
        loginButton.setText("Login");
        loginButton.setToolTipText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 40;
        panel1.add(loginButton, gbc);
        invalidLoginLabel = new JLabel();
        Font invalidLoginLabelFont = this.$$$getFont$$$(null, -1, 12, invalidLoginLabel.getFont());
        if (invalidLoginLabelFont != null) invalidLoginLabel.setFont(invalidLoginLabelFont);
        invalidLoginLabel.setForeground(new Color(-4517105));
        invalidLoginLabel.setText("Invalid username/password");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(invalidLoginLabel, gbc);
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
        return panel1;
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
