import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class CampaignsManagement extends JFrame {

    private JTabbedPane campaignsPane;
    private JPanel panel1;
    private JButton showCampaignButton;
    private JTextField campaignIdTextField;
    private JTextField campaignIdTF;
    private JTextField campaignNameTF;
    private JTextField campaignDescriptionTF;
    private JTextField campaignStartTF;
    private JTextField campaignEndTF;
    private JTextField campaignBudgetTF;
    private JTextField campaignStrategyTF;
    private JLabel insertCampaignIdLabel;
    private JLabel campaignIdLabel;
    private JLabel campaignNameLabel;
    private JLabel campaignDescriptionLabel;
    private JLabel campaignStartLabel;
    private JLabel campaignEndLabel;
    private JLabel campaignBudgetLabel;
    private JLabel campaignStrategyLabel;
    private JTextField campaignIDAdd;
    private JLabel campaignExistsLabel;
    private JButton goBackButton;
    private JTable campaignsTable;
    private JPanel showCampaignsPanel;
    private JPanel addCampaignPanel;
    private JPanel editCampaignPanel;
    private JPanel closeCampaignPanel;
    private JPanel viewCampaignsPanel;
    private JPanel goBackPanel;
    private JButton sortNameAscButton;
    private JButton sortAscButton;
    private JButton sortDescButton;
    private JLabel sortByLabel;
    private JButton sortNameDescButton;
    private JTextField campaignNameAdd;
    private JTextField campaignDescriptionAdd;
    private JTextField campaignStartAdd;
    private JTextField campaignEndAdd;
    private JTextField campaignBudgetAdd;
    private JTextField campaignStrategyAdd;
    private JButton addCampaignButton;
    private JButton closeCampaignButton;
    private JTextField insertCampaignIDTF;
    private JTextField idEditTF;
    private JTextField nameEditTF;
    private JTextField descriptionEditTF;
    private JTextField startDateEditTF;
    private JTextField endDateEditTF;
    private JTextField budgetEditTF;
    private JButton editCampaignButton;
    private VMS vms;
    private User user;
    private LocalDateTime time;


    public CampaignsManagement(VMS vms, User user, LocalDateTime time) {
        this();
        this.vms = vms;
        this.user = user;
        this.time = time;
        addCampaignsToTable();
    }

    public CampaignsManagement() {
        super.setTitle("Campaigns");
        super.setContentPane(campaignsPane);
        setLocation(this);
        pack();
        setVisible(true);
        campaignExistsLabel.setVisible(false);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CampaignsManagement.super.dispose();
                MainMenu mainMenu = new MainMenu(vms, user, time);
                mainMenu.setVisible(true);
                mainMenu.show();
                mainMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
        sortAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortNameAscButton.setEnabled(true);
                sortDescButton.setEnabled(true);
                sortAscButton.setEnabled(false);
                sortNameDescButton.setEnabled(true);
                addCampaignsToTable();
            }
        });
        sortNameAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortNameAscButton.setEnabled(false);
                sortDescButton.setEnabled(true);
                sortAscButton.setEnabled(true);
                sortNameDescButton.setEnabled(true);
                addCampaignsToTable();
            }
        });
        sortDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortNameAscButton.setEnabled(true);
                sortDescButton.setEnabled(false);
                sortAscButton.setEnabled(true);
                sortNameDescButton.setEnabled(true);
                addCampaignsToTable();
            }
        });
        sortNameDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortNameAscButton.setEnabled(true);
                sortDescButton.setEnabled(true);
                sortAscButton.setEnabled(true);
                sortNameDescButton.setEnabled(false);
                addCampaignsToTable();
            }
        });
        campaignIdTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchCampaign(campaignIdTextField.getText());
                }
            }
        });
        showCampaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCampaign(campaignIdTextField.getText());
            }
        });
        addCampaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCampaign();
            }
        });
        campaignBudgetAdd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addCampaign();
                }
            }
        });
        campaignStrategyAdd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addCampaign();
                }
            }
        });
        closeCampaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeCampaign();
            }
        });
        insertCampaignIDTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    closeCampaign();
            }
        });
        editCampaignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCampaign();
            }
        });
        budgetEditTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    editCampaign();
            }
        });
    }
    private void editCampaign() {
        int ID = Integer.parseInt(idEditTF.getText());
        String name = nameEditTF.getText();
        String description = descriptionEditTF.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDateEditTF.getText(),
                formatter);
        LocalDateTime end = LocalDateTime.parse(endDateEditTF.
                getText(), formatter);
        int budget = Integer.parseInt(budgetEditTF.getText());
        Campaign c = new Campaign(ID, name,
                description, start, end,
                budget, null);
        Campaign.CampaignStatusType status = null;

        if (time.isAfter(end))
            status = Campaign.CampaignStatusType.EXPIRED;
        else if (time.isBefore(start))
            status = Campaign.CampaignStatusType.NEW;
        else if (time.isEqual(start))
            status = Campaign.CampaignStatusType.NEW;
        else if (time.isAfter(start) &&
                time.isBefore(end))
            status = Campaign.CampaignStatusType.STARTED;

        c.setStatus(status, time);
        vms.updateCampaign(ID, c);
        Campaign old = vms.getCampaign(ID);
        c.setStrategy(old.getStrategy());
        vms.getCampaigns().remove(old);
        vms.addCampaign(c);
        Notification.NotificationType type =
                Notification.NotificationType.EDIT;
        Notification notif = new Notification(ID, time,
                null, type);
        c.notifyAllObservers(notif);
        addCampaignsToTable();

    }

    private void addCampaignsToTable() {
        Vector<Campaign> campaigns = vms.getCampaigns();

        DefaultTableModel model = (DefaultTableModel) campaignsTable.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Campaign ID");
        model.addColumn("Campaign Name");
        model.addColumn("Description");
        model.addColumn("Start Date");
        model.addColumn("End Date");
        model.addColumn("Budget");
        model.addColumn("Strategy");

        model.addRow(new Object[]{"Campaign ID", "Name",
                "Description", "Start Date", "End Date", "Budget", "Strategy"});

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        Comparator comp = null;

        if (!sortNameAscButton.isEnabled())
            comp = new ComparatorNameAsc();
        else if (!sortNameDescButton.isEnabled())
            comp = new ComparatorNameDesc();
        else if (!sortAscButton.isEnabled())
            comp = new ComparatorTimeAsc();
        else
            comp = new ComparatorTimeDesc();

        Collections.sort(campaigns, comp);

        for (Campaign c : campaigns) {
            String idString = Integer.toString(c.getCampaignId());
            String cName = c.getName();
            String description = c.getDescription();
            String startDate = c.getStart().format(formatter);
            String endDate = c.getFinish().format(formatter);
            String budget = Integer.toString(c.getAvailable());
            String strategy;
            if (c.getStrategy() == null)
                strategy = "None";
            else
                strategy = c.getStrategy().toString();
            model.addRow(new Object[]{idString, cName, description, startDate,
                    endDate, budget, strategy});
        }
    }

    private void searchCampaign(String idString) {
        int ID = Integer.parseInt(idString);
        for (Campaign c : vms.getCampaigns()) {
            if (c.getCampaignId() == ID) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                        ("yyyy-MM-dd HH:mm");
                campaignIdTF.setText(idString);
                campaignNameTF.setText(c.getName());
                campaignDescriptionTF.setText(c.getDescription());
                campaignStartTF.setText(c.getStart().format(formatter));
                campaignEndTF.setText(c.getFinish().format(formatter));
                campaignBudgetTF.setText(Integer.toString(c.getAvailable()));
                if (c.getStrategy() != null)
                    campaignStrategyTF.setText(c.getStrategy().toString());
                else
                    campaignStrategyTF.setText("None");
                break;
            }
        }
    }

    private void addCampaign() {
        int ID = Integer.parseInt(campaignIDAdd.getText());
        String name = campaignNameAdd.getText();
        String description = campaignDescriptionAdd.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(campaignStartAdd.getText(),
                formatter);
        LocalDateTime end = LocalDateTime.parse(campaignEndAdd.
                getText(), formatter);
        int budget = Integer.parseInt(campaignBudgetAdd.getText());
        String strategyString = campaignStrategyAdd.getText();
        Campaign.strategyType strategy;
        switch (strategyString) {
            case "A":
                strategy = Campaign.strategyType.A;
                break;
            case "B":
                strategy = Campaign.strategyType.B;
                break;
            case "C":
                strategy = Campaign.strategyType.C;
                break;
            default:
                strategy = null;
        }
        Campaign newCampaign = new Campaign(ID, name, description, start, end,
                budget, strategy);
        boolean exists = checkIfExists(newCampaign);
        if (exists)
            campaignExistsLabel.setVisible(true);
        else {
            campaignExistsLabel.setVisible(false);
            vms.addCampaign(newCampaign);
            addCampaignsToTable();
        }
    }

    private boolean checkIfExists(Campaign campaign) {
        for (Campaign c : vms.getCampaigns()) {
            if (c.getCampaignId() == campaign.getCampaignId())
                return true;
        }
        return false;
    }

    private void closeCampaign() {
        int id = Integer.parseInt(insertCampaignIDTF.getText());
        for (Campaign campaign : vms.getCampaigns())
            if (campaign.getCampaignId() == id)
                campaign.setStatus
                        (Campaign.CampaignStatusType.CANCELLED, time);

    }

    public static void main(String[] args) {
        new CampaignsManagement();
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
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.setBackground(new Color(-14402239));
        panel1.setMinimumSize(new Dimension(960, 720));
        panel1.setPreferredSize(new Dimension(960, 720));
        campaignsPane = new JTabbedPane();
        campaignsPane.setBackground(new Color(-1));
        Font campaignsPaneFont = this.$$$getFont$$$(null, -1, 14, campaignsPane.getFont());
        if (campaignsPaneFont != null) campaignsPane.setFont(campaignsPaneFont);
        campaignsPane.setForeground(new Color(-4517105));
        campaignsPane.setMinimumSize(new Dimension(960, 720));
        campaignsPane.setOpaque(true);
        campaignsPane.setPreferredSize(new Dimension(960, 720));
        campaignsPane.setTabPlacement(1);
        panel1.add(campaignsPane);
        showCampaignsPanel = new JPanel();
        showCampaignsPanel.setLayout(new GridBagLayout());
        showCampaignsPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("Show campaign", showCampaignsPanel);
        insertCampaignIdLabel = new JLabel();
        Font insertCampaignIdLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, insertCampaignIdLabel.getFont());
        if (insertCampaignIdLabelFont != null) insertCampaignIdLabel.setFont(insertCampaignIdLabelFont);
        insertCampaignIdLabel.setForeground(new Color(-1));
        insertCampaignIdLabel.setText("Insert campaign id:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        showCampaignsPanel.add(insertCampaignIdLabel, gbc);
        showCampaignButton = new JButton();
        Font showCampaignButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, showCampaignButton.getFont());
        if (showCampaignButtonFont != null) showCampaignButton.setFont(showCampaignButtonFont);
        showCampaignButton.setText("Show campaign");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 50, 10);
        showCampaignsPanel.add(showCampaignButton, gbc);
        campaignIdTextField = new JTextField();
        Font campaignIdTextFieldFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIdTextField.getFont());
        if (campaignIdTextFieldFont != null) campaignIdTextField.setFont(campaignIdTextFieldFont);
        campaignIdTextField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 10);
        showCampaignsPanel.add(campaignIdTextField, gbc);
        campaignIdLabel = new JLabel();
        Font campaignIdLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIdLabel.getFont());
        if (campaignIdLabelFont != null) campaignIdLabel.setFont(campaignIdLabelFont);
        campaignIdLabel.setForeground(new Color(-1));
        campaignIdLabel.setText("Id:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignIdLabel, gbc);
        campaignIdTF = new JTextField();
        campaignIdTF.setEditable(false);
        Font campaignIdTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIdTF.getFont());
        if (campaignIdTFFont != null) campaignIdTF.setFont(campaignIdTFFont);
        campaignIdTF.setForeground(new Color(-4517105));
        campaignIdTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignIdTF, gbc);
        campaignNameTF = new JTextField();
        campaignNameTF.setEditable(false);
        Font campaignNameTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignNameTF.getFont());
        if (campaignNameTFFont != null) campaignNameTF.setFont(campaignNameTFFont);
        campaignNameTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignNameTF, gbc);
        campaignDescriptionTF = new JTextField();
        campaignDescriptionTF.setEditable(false);
        Font campaignDescriptionTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignDescriptionTF.getFont());
        if (campaignDescriptionTFFont != null) campaignDescriptionTF.setFont(campaignDescriptionTFFont);
        campaignDescriptionTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignDescriptionTF, gbc);
        campaignStartTF = new JTextField();
        campaignStartTF.setEditable(false);
        Font campaignStartTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStartTF.getFont());
        if (campaignStartTFFont != null) campaignStartTF.setFont(campaignStartTFFont);
        campaignStartTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignStartTF, gbc);
        campaignEndTF = new JTextField();
        campaignEndTF.setEditable(false);
        Font campaignEndTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignEndTF.getFont());
        if (campaignEndTFFont != null) campaignEndTF.setFont(campaignEndTFFont);
        campaignEndTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignEndTF, gbc);
        campaignBudgetTF = new JTextField();
        campaignBudgetTF.setEditable(false);
        Font campaignBudgetTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignBudgetTF.getFont());
        if (campaignBudgetTFFont != null) campaignBudgetTF.setFont(campaignBudgetTFFont);
        campaignBudgetTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignBudgetTF, gbc);
        campaignStrategyTF = new JTextField();
        campaignStrategyTF.setEditable(false);
        Font campaignStrategyTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStrategyTF.getFont());
        if (campaignStrategyTFFont != null) campaignStrategyTF.setFont(campaignStrategyTFFont);
        campaignStrategyTF.setForeground(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        showCampaignsPanel.add(campaignStrategyTF, gbc);
        campaignNameLabel = new JLabel();
        Font campaignNameLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignNameLabel.getFont());
        if (campaignNameLabelFont != null) campaignNameLabel.setFont(campaignNameLabelFont);
        campaignNameLabel.setForeground(new Color(-1));
        campaignNameLabel.setText("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignNameLabel, gbc);
        campaignDescriptionLabel = new JLabel();
        Font campaignDescriptionLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignDescriptionLabel.getFont());
        if (campaignDescriptionLabelFont != null) campaignDescriptionLabel.setFont(campaignDescriptionLabelFont);
        campaignDescriptionLabel.setForeground(new Color(-1));
        campaignDescriptionLabel.setText("Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignDescriptionLabel, gbc);
        campaignStartLabel = new JLabel();
        Font campaignStartLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStartLabel.getFont());
        if (campaignStartLabelFont != null) campaignStartLabel.setFont(campaignStartLabelFont);
        campaignStartLabel.setForeground(new Color(-1));
        campaignStartLabel.setText("Start date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignStartLabel, gbc);
        campaignEndLabel = new JLabel();
        Font campaignEndLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignEndLabel.getFont());
        if (campaignEndLabelFont != null) campaignEndLabel.setFont(campaignEndLabelFont);
        campaignEndLabel.setForeground(new Color(-1));
        campaignEndLabel.setText("End date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignEndLabel, gbc);
        campaignBudgetLabel = new JLabel();
        Font campaignBudgetLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignBudgetLabel.getFont());
        if (campaignBudgetLabelFont != null) campaignBudgetLabel.setFont(campaignBudgetLabelFont);
        campaignBudgetLabel.setForeground(new Color(-1));
        campaignBudgetLabel.setText("Budget:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignBudgetLabel, gbc);
        campaignStrategyLabel = new JLabel();
        Font campaignStrategyLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStrategyLabel.getFont());
        if (campaignStrategyLabelFont != null) campaignStrategyLabel.setFont(campaignStrategyLabelFont);
        campaignStrategyLabel.setForeground(new Color(-1));
        campaignStrategyLabel.setText("Strategy type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        showCampaignsPanel.add(campaignStrategyLabel, gbc);
        addCampaignPanel = new JPanel();
        addCampaignPanel.setLayout(new GridBagLayout());
        addCampaignPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("Add campaign", addCampaignPanel);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        label1.setText("Insert campaign details:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        addCampaignPanel.add(label1, gbc);
        addCampaignButton = new JButton();
        Font addCampaignButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, addCampaignButton.getFont());
        if (addCampaignButtonFont != null) addCampaignButton.setFont(addCampaignButtonFont);
        addCampaignButton.setText("Add campaign");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(addCampaignButton, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1));
        label2.setText("Id:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label2, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-1));
        label3.setText("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label3, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setForeground(new Color(-1));
        label4.setText("Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setForeground(new Color(-1));
        label5.setText("Start date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label5, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setForeground(new Color(-1));
        label6.setText("End date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label6, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setForeground(new Color(-1));
        label7.setText("Budget:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label7, gbc);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setForeground(new Color(-1));
        label8.setText("Strategy type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        addCampaignPanel.add(label8, gbc);
        campaignIDAdd = new JTextField();
        Font campaignIDAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignIDAdd.getFont());
        if (campaignIDAddFont != null) campaignIDAdd.setFont(campaignIDAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignIDAdd, gbc);
        campaignNameAdd = new JTextField();
        Font campaignNameAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignNameAdd.getFont());
        if (campaignNameAddFont != null) campaignNameAdd.setFont(campaignNameAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignNameAdd, gbc);
        campaignDescriptionAdd = new JTextField();
        Font campaignDescriptionAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignDescriptionAdd.getFont());
        if (campaignDescriptionAddFont != null) campaignDescriptionAdd.setFont(campaignDescriptionAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignDescriptionAdd, gbc);
        campaignStartAdd = new JTextField();
        Font campaignStartAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStartAdd.getFont());
        if (campaignStartAddFont != null) campaignStartAdd.setFont(campaignStartAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignStartAdd, gbc);
        campaignEndAdd = new JTextField();
        Font campaignEndAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignEndAdd.getFont());
        if (campaignEndAddFont != null) campaignEndAdd.setFont(campaignEndAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignEndAdd, gbc);
        campaignBudgetAdd = new JTextField();
        Font campaignBudgetAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignBudgetAdd.getFont());
        if (campaignBudgetAddFont != null) campaignBudgetAdd.setFont(campaignBudgetAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignBudgetAdd, gbc);
        campaignStrategyAdd = new JTextField();
        Font campaignStrategyAddFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, campaignStrategyAdd.getFont());
        if (campaignStrategyAddFont != null) campaignStrategyAdd.setFont(campaignStrategyAddFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        addCampaignPanel.add(campaignStrategyAdd, gbc);
        campaignExistsLabel = new JLabel();
        Font campaignExistsLabelFont = this.$$$getFont$$$(null, -1, 14, campaignExistsLabel.getFont());
        if (campaignExistsLabelFont != null) campaignExistsLabel.setFont(campaignExistsLabelFont);
        campaignExistsLabel.setForeground(new Color(-4517105));
        campaignExistsLabel.setText("Campaign already exists");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 70, 0);
        addCampaignPanel.add(campaignExistsLabel, gbc);
        editCampaignPanel = new JPanel();
        editCampaignPanel.setLayout(new GridBagLayout());
        editCampaignPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("Edit campaign", editCampaignPanel);
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setForeground(new Color(-1));
        label9.setText("Insert campaign details:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        editCampaignPanel.add(label9, gbc);
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setForeground(new Color(-1));
        label10.setText("Id:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label10, gbc);
        idEditTF = new JTextField();
        Font idEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, idEditTF.getFont());
        if (idEditTFFont != null) idEditTF.setFont(idEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(idEditTF, gbc);
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setForeground(new Color(-1));
        label11.setText("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label11, gbc);
        nameEditTF = new JTextField();
        Font nameEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, nameEditTF.getFont());
        if (nameEditTFFont != null) nameEditTF.setFont(nameEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(nameEditTF, gbc);
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setForeground(new Color(-1));
        label12.setText("Description:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label12, gbc);
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setForeground(new Color(-1));
        label13.setText("Start date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label13, gbc);
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setForeground(new Color(-1));
        label14.setText("End date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label14, gbc);
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setForeground(new Color(-1));
        label15.setText("Budget:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        editCampaignPanel.add(label15, gbc);
        descriptionEditTF = new JTextField();
        Font descriptionEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, descriptionEditTF.getFont());
        if (descriptionEditTFFont != null) descriptionEditTF.setFont(descriptionEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(descriptionEditTF, gbc);
        startDateEditTF = new JTextField();
        Font startDateEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, startDateEditTF.getFont());
        if (startDateEditTFFont != null) startDateEditTF.setFont(startDateEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(startDateEditTF, gbc);
        endDateEditTF = new JTextField();
        Font endDateEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, endDateEditTF.getFont());
        if (endDateEditTFFont != null) endDateEditTF.setFont(endDateEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(endDateEditTF, gbc);
        budgetEditTF = new JTextField();
        Font budgetEditTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, budgetEditTF.getFont());
        if (budgetEditTFFont != null) budgetEditTF.setFont(budgetEditTFFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 10);
        editCampaignPanel.add(budgetEditTF, gbc);
        editCampaignButton = new JButton();
        Font editCampaignButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, editCampaignButton.getFont());
        if (editCampaignButtonFont != null) editCampaignButton.setFont(editCampaignButtonFont);
        editCampaignButton.setText("Edit campaign");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 98, 10);
        editCampaignPanel.add(editCampaignButton, gbc);
        closeCampaignPanel = new JPanel();
        closeCampaignPanel.setLayout(new GridBagLayout());
        closeCampaignPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("Close campaign", closeCampaignPanel);
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setForeground(new Color(-1));
        label16.setText("Insert campaign id:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 30, 10);
        closeCampaignPanel.add(label16, gbc);
        insertCampaignIDTF = new JTextField();
        Font insertCampaignIDTFFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, insertCampaignIDTF.getFont());
        if (insertCampaignIDTFFont != null) insertCampaignIDTF.setFont(insertCampaignIDTFFont);
        insertCampaignIDTF.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 80;
        gbc.insets = new Insets(0, 0, 30, 10);
        closeCampaignPanel.add(insertCampaignIDTF, gbc);
        closeCampaignButton = new JButton();
        Font closeCampaignButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, closeCampaignButton.getFont());
        if (closeCampaignButtonFont != null) closeCampaignButton.setFont(closeCampaignButtonFont);
        closeCampaignButton.setText("Close campaign");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 50, 10);
        closeCampaignPanel.add(closeCampaignButton, gbc);
        viewCampaignsPanel = new JPanel();
        viewCampaignsPanel.setLayout(new GridBagLayout());
        viewCampaignsPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("View campaigns", viewCampaignsPanel);
        campaignsTable = new JTable();
        campaignsTable.setBackground(new Color(-14402239));
        Font campaignsTableFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 15, campaignsTable.getFont());
        if (campaignsTableFont != null) campaignsTable.setFont(campaignsTableFont);
        campaignsTable.setForeground(new Color(-1));
        campaignsTable.setGridColor(new Color(-4517105));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);
        viewCampaignsPanel.add(campaignsTable, gbc);
        sortByLabel = new JLabel();
        Font sortByLabelFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 14, sortByLabel.getFont());
        if (sortByLabelFont != null) sortByLabel.setFont(sortByLabelFont);
        sortByLabel.setForeground(new Color(-1));
        sortByLabel.setText("Sort by:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        viewCampaignsPanel.add(sortByLabel, gbc);
        sortNameAscButton = new JButton();
        sortNameAscButton.setEnabled(true);
        Font sortNameAscButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 12, sortNameAscButton.getFont());
        if (sortNameAscButtonFont != null) sortNameAscButton.setFont(sortNameAscButtonFont);
        sortNameAscButton.setText("Name (Asc)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        viewCampaignsPanel.add(sortNameAscButton, gbc);
        sortAscButton = new JButton();
        sortAscButton.setEnabled(true);
        Font sortAscButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 12, sortAscButton.getFont());
        if (sortAscButtonFont != null) sortAscButton.setFont(sortAscButtonFont);
        sortAscButton.setText("Time (Asc)");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        viewCampaignsPanel.add(sortAscButton, gbc);
        sortDescButton = new JButton();
        sortDescButton.setEnabled(true);
        Font sortDescButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 12, sortDescButton.getFont());
        if (sortDescButtonFont != null) sortDescButton.setFont(sortDescButtonFont);
        sortDescButton.setText("Time (Desc)");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        viewCampaignsPanel.add(sortDescButton, gbc);
        sortNameDescButton = new JButton();
        sortNameDescButton.setEnabled(true);
        Font sortNameDescButtonFont = this.$$$getFont$$$("Lucida Sans Unicode", -1, 12, sortNameDescButton.getFont());
        if (sortNameDescButtonFont != null) sortNameDescButton.setFont(sortNameDescButtonFont);
        sortNameDescButton.setText("Name (Desc)");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        viewCampaignsPanel.add(sortNameDescButton, gbc);
        goBackPanel = new JPanel();
        goBackPanel.setLayout(new GridBagLayout());
        goBackPanel.setBackground(new Color(-14402239));
        campaignsPane.addTab("Back", goBackPanel);
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
        goBackPanel.add(goBackButton, gbc);
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

    public static void setLocation(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int px = centerPoint.x / 2;
        int py = centerPoint.y / 2;
        frame.setLocation(px, py);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
