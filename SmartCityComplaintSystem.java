import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SmartCityComplaintSystem extends JFrame {

    private static final String DB_FILE = "smartcity_database.dat";

    private ArrayList<String[]> data = new ArrayList<>();
    private int id = 1001;

    private DefaultTableModel tableModel;
    private JTable table;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel progressLabel;
    private JLabel resolvedLabel;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // =========================================================
    // COLORS
    // =========================================================

    private final Color CYAN = new Color(0, 235, 240);
    private final Color DARK = new Color(2, 15, 20);
    private final Color PANEL = new Color(3, 25, 30);
    private final Color FIELD = new Color(4, 35, 40);
    private final Color BORDER = new Color(0, 190, 200);
    private final Color TEXT = new Color(240, 250, 250);
    private final Color MUTED = new Color(175, 205, 210);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SmartCityComplaintSystem() {

        setTitle("Smart City Complaint & Response System");

        setSize(1280, 800);

        setMinimumSize(new Dimension(1100, 700));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadDatabase();

        createGUI();

        refreshTable();
    }

    // =========================================================
    // MAIN GUI
    // =========================================================

    private void createGUI() {

        BackgroundPanel background =
                new BackgroundPanel();

        setContentPane(background);

        background.setLayout(
                new BorderLayout()
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        22, 30, 12, 30
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "SMART CITY COMPLAINT"
                );

        title.setForeground(CYAN);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        JLabel title2 =
                new JLabel(
                        "CONTROL CENTER"
                );

        title2.setForeground(Color.WHITE);

        title2.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Citizen Complaint  •  Department Assignment  •  Response Tracking"
                );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        titlePanel.add(title);
        titlePanel.add(title2);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        JLabel database =
                new JLabel(
                        "● DATABASE ACTIVE"
                );

        database.setForeground(
                new Color(
                        70, 255, 150
                )
        );

        database.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                database,
                BorderLayout.EAST
        );

        background.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER
        // =====================================================

        JPanel center =
                new JPanel(
                        new BorderLayout(0, 14)
                );

        center.setOpaque(false);

        center.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 30, 10, 30
                )
        );

        // =====================================================
        // STATISTICS
        // =====================================================

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1, 4, 16, 0
                        )
                );

        stats.setOpaque(false);

        totalLabel = new JLabel("0");
        pendingLabel = new JLabel("0");
        progressLabel = new JLabel("0");
        resolvedLabel = new JLabel("0");

        stats.add(
                createStatCard(
                        "TOTAL COMPLAINTS",
                        totalLabel
                )
        );

        stats.add(
                createStatCard(
                        "PENDING",
                        pendingLabel
                )
        );

        stats.add(
                createStatCard(
                        "IN PROGRESS",
                        progressLabel
                )
        );

        stats.add(
                createStatCard(
                        "RESOLVED",
                        resolvedLabel
                )
        );

        center.add(
                stats,
                BorderLayout.NORTH
        );

        // =====================================================
        // MANAGEMENT PANEL
        // =====================================================

        JPanel management =
                new JPanel(
                        new BorderLayout(0, 10)
                );

        management.setOpaque(true);

        management.setBackground(PANEL);

        management.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14, 14, 14, 14
                        )
                )
        );

        JLabel managementTitle =
                new JLabel(
                        "COMPLAINT MANAGEMENT"
                );

        managementTitle.setForeground(CYAN);

        managementTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        management.add(
                managementTitle,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "COMPLAINT ID",
                                "CATEGORY",
                                "AREA",
                                "SEVERITY",
                                "PRIORITY",
                                "DEPARTMENT",
                                "STATUS"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        table =
                new JTable(tableModel);

        table.setRowHeight(32);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.setForeground(TEXT);

        table.setBackground(
                new Color(
                        1, 15, 20
                )
        );

        table.setGridColor(
                new Color(
                        0, 90, 100
                )
        );

        table.setSelectionBackground(
                new Color(
                        0, 100, 110
                )
        );

        table.setSelectionForeground(
                Color.WHITE
        );

        table.setAutoCreateRowSorter(true);

        table.setFillsViewportHeight(true);

        table.getTableHeader()
                .setOpaque(true);

        table.getTableHeader()
                .setBackground(
                        new Color(
                                0, 65, 75
                        )
                );

        table.getTableHeader()
                .setForeground(CYAN);

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0, 36
                        )
                );

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setOpaque(true);

        scroll.setBackground(DARK);

        scroll.getViewport()
                .setOpaque(true);

        scroll.getViewport()
                .setBackground(DARK);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        management.add(
                scroll,
                BorderLayout.CENTER
        );

        center.add(
                management,
                BorderLayout.CENTER
        );

        // =====================================================
        // BUTTONS
        // =====================================================

        JPanel controls =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                9,
                                3
                        )
                );

        controls.setOpaque(false);

        JButton register =
                createButton(
                        "+ REGISTER COMPLAINT"
                );

        JButton progress =
                createButton(
                        "▶ MARK IN PROGRESS"
                );

        JButton resolved =
                createButton(
                        "✓ MARK RESOLVED"
                );

        JButton details =
                createButton(
                        "▣ VIEW DETAILS"
                );

        JButton delete =
                createButton(
                        "✕ DELETE COMPLAINT"
                );

        JButton refresh =
                createButton(
                        "↻ REFRESH"
                );

        register.addActionListener(
                e -> showRegisterDialog()
        );

        progress.addActionListener(
                e -> changeStatus("In Progress")
        );

        resolved.addActionListener(
                e -> markResolved()
        );

        details.addActionListener(
                e -> showDetails()
        );

        delete.addActionListener(
                e -> deleteComplaint()
        );

        refresh.addActionListener(
                e -> refreshTable()
        );

        controls.add(register);
        controls.add(progress);
        controls.add(resolved);
        controls.add(details);
        controls.add(delete);
        controls.add(refresh);

        center.add(
                controls,
                BorderLayout.SOUTH
        );

        background.add(
                center,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            JLabel value
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setOpaque(true);

        card.setBackground(
                new Color(
                        2, 25, 30
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                CYAN
                        ),
                        BorderFactory.createEmptyBorder(
                                12, 18, 10, 18
                        )
                )
        );

        JLabel heading =
                new JLabel(title);

        heading.setForeground(
                new Color(
                        220, 235, 240
                )
        );

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        value.setForeground(CYAN);

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        card.add(
                heading,
                BorderLayout.NORTH
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        175, 42
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(
                        0, 55, 65
                )
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                CYAN
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 8, 5, 8
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        0, 105, 115
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                new Color(
                                        0, 55, 65
                                )
                        );
                    }
                }
        );

        return button;
    }

    // =========================================================
    // REGISTER COMPLAINT
    // =========================================================

    private void showRegisterDialog() {

        JDialog dialog =
                new JDialog(
                        this,
                        "Register New Complaint",
                        true
                );

        dialog.setSize(
                780, 720
        );

        dialog.setLocationRelativeTo(this);

        JPanel background =
                new JPanel(
                        new GridBagLayout()
                );

        background.setBackground(DARK);

        dialog.setContentPane(background);

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                new Color(
                        2, 20, 25
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 28, 18, 28
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6, 6, 6, 6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel heading =
                new JLabel(
                        "REGISTER NEW COMPLAINT"
                );

        heading.setForeground(CYAN);

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(
                heading,
                gbc
        );

        gbc.gridwidth = 1;

        JTextField name =
                createField();

        addFormField(
                panel,
                gbc,
                1,
                "Citizen Name *",
                name
        );

        JTextField contact =
                createField();

        addFormField(
                panel,
                gbc,
                2,
                "Contact Number *",
                contact
        );

        JComboBox<String> category =
                new JComboBox<>(
                        new String[]{
                                "Select Category",
                                "Road",
                                "Water",
                                "Electricity",
                                "Garbage",
                                "Street Light",
                                "Drainage",
                                "Transport",
                                "Other"
                        }
                );

        styleCombo(category);

        addFormField(
                panel,
                gbc,
                3,
                "Category *",
                category
        );

        JTextField area =
                createField();

        addFormField(
                panel,
                gbc,
                4,
                "Area *",
                area
        );

        JTextField address =
                createField();

        addFormField(
                panel,
                gbc,
                5,
                "Exact Address *",
                address
        );

        JComboBox<String> severity =
                new JComboBox<>(
                        new String[]{
                                "Select Severity",
                                "Low",
                                "Medium",
                                "High",
                                "Critical"
                        }
                );

        styleCombo(severity);

        addFormField(
                panel,
                gbc,
                6,
                "Severity *",
                severity
        );

        JTextArea description =
                new JTextArea(
                        4, 30
                );

        description.setLineWrap(true);

        description.setWrapStyleWord(true);

        description.setForeground(Color.WHITE);

        description.setCaretColor(Color.WHITE);

        description.setBackground(FIELD);

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        description.setBorder(
                BorderFactory.createEmptyBorder(
                        7, 8, 7, 8
                )
        );

        JScrollPane descriptionScroll =
                new JScrollPane(
                        description
                );

        descriptionScroll.setPreferredSize(
                new Dimension(
                        350, 100
                )
        );

        descriptionScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        addFormField(
                panel,
                gbc,
                7,
                "Description *",
                descriptionScroll
        );

        JButton submit =
                createButton(
                        "REGISTER COMPLAINT"
                );

        submit.setPreferredSize(
                new Dimension(
                        230, 44
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;

        panel.add(
                submit,
                gbc
        );

        submit.addActionListener(
                e -> {

                    String citizen =
                            name.getText().trim();

                    String phone =
                            contact.getText().trim();

                    String cat =
                            String.valueOf(
                                    category.getSelectedItem()
                            );

                    String areaText =
                            area.getText().trim();

                    String addressText =
                            address.getText().trim();

                    String sev =
                            String.valueOf(
                                    severity.getSelectedItem()
                            );

                    String desc =
                            description.getText().trim();

                    if (citizen.isEmpty()) {

                        error(
                                dialog,
                                "Citizen Name is mandatory."
                        );

                        return;
                    }

                    if (!phone.matches(
                            "[0-9]{10}"
                    )) {

                        error(
                                dialog,
                                "Enter a valid 10-digit contact number."
                        );

                        return;
                    }

                    if (category.getSelectedIndex() == 0) {

                        error(
                                dialog,
                                "Please select a category."
                        );

                        return;
                    }

                    if (areaText.isEmpty()) {

                        error(
                                dialog,
                                "Area is mandatory."
                        );

                        return;
                    }

                    if (addressText.isEmpty()) {

                        error(
                                dialog,
                                "Exact Address is mandatory."
                        );

                        return;
                    }

                    if (severity.getSelectedIndex() == 0) {

                        error(
                                dialog,
                                "Please select severity."
                        );

                        return;
                    }

                    if (desc.isEmpty()) {

                        error(
                                dialog,
                                "Description is mandatory."
                        );

                        return;
                    }

                    String department =
                            getDepartment(cat);

                    String priority =
                            getPriority(sev);

                    String complaintID =
                            "SC-" + id;

                    data.add(
                            new String[]{
                                    complaintID,
                                    citizen,
                                    phone,
                                    cat,
                                    areaText,
                                    addressText,
                                    sev,
                                    priority,
                                    department,
                                    "Pending",
                                    desc,
                                    "Not assigned yet",
                                    "Not started",
                                    "Not completed"
                            }
                    );

                    id++;

                    saveDatabase();

                    refreshTable();

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Complaint Registered Successfully!\n\n"
                                    + "Complaint ID : "
                                    + complaintID
                                    + "\nDepartment : "
                                    + department
                                    + "\nPriority : "
                                    + priority,
                            "Registration Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dialog.dispose();
                }
        );

        background.add(panel);

        dialog.setVisible(true);
    }

    // =========================================================
    // FORM FIELD
    // =========================================================

    private void addFormField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            Component component
    ) {

        gbc.gridy = row;

        gbc.gridx = 0;

        gbc.weightx = 0;

        JLabel label =
                new JLabel(labelText);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        panel.add(
                label,
                gbc
        );

        gbc.gridx = 1;

        gbc.weightx = 1;

        panel.add(
                component,
                gbc
        );
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createField() {

        JTextField field =
                new JTextField();

        field.setPreferredSize(
                new Dimension(
                        350, 38
                )
        );

        field.setForeground(Color.WHITE);

        field.setCaretColor(Color.WHITE);

        field.setBackground(FIELD);

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 8, 5, 8
                        )
                )
        );

        return field;
    }

    // =========================================================
    // COMBO
    // =========================================================

    private void styleCombo(
            JComboBox<String> box
    ) {

        box.setPreferredSize(
                new Dimension(
                        350, 38
                )
        );

        box.setForeground(Color.WHITE);

        box.setBackground(FIELD);

        box.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );
    }

    // =========================================================
    // DEPARTMENT
    // =========================================================

    private String getDepartment(
            String category
    ) {

        switch (category) {

            case "Road":
                return "Road Department";

            case "Water":
                return "Water Department";

            case "Electricity":
            case "Street Light":
                return "Electrical Department";

            case "Garbage":
                return "Waste Management";

            case "Drainage":
                return "Drainage Department";

            case "Transport":
                return "Transport Department";

            default:
                return "General Department";
        }
    }

    // =========================================================
    // PRIORITY
    // =========================================================

    private String getPriority(
            String severity
    ) {

        switch (severity) {

            case "Critical":
                return "URGENT";

            case "High":
                return "HIGH";

            case "Medium":
                return "MEDIUM";

            default:
                return "LOW";
        }
    }

    // =========================================================
    // MARK IN PROGRESS
    // =========================================================

    private void changeStatus(
            String status
    ) {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a complaint first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(
                        row
                );

        String complaintID =
                tableModel
                        .getValueAt(
                                modelRow,
                                0
                        )
                        .toString();

        for (String[] c : data) {

            if (c[0].equals(
                    complaintID
            )) {

                c[9] = status;

                c[11] = c[8];

                c[12] =
                        LocalDateTime
                                .now()
                                .format(formatter);

                saveDatabase();

                refreshTable();

                return;
            }
        }
    }

    // =========================================================
    // MARK RESOLVED
    // =========================================================

    private void markResolved() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a complaint first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(
                        row
                );

        String complaintID =
                tableModel
                        .getValueAt(
                                modelRow,
                                0
                        )
                        .toString();

        for (String[] c : data) {

            if (c[0].equals(
                    complaintID
            )) {

                c[9] = "Resolved";

                c[11] = c[8];

                if (c[12] == null ||
                        c[12].isEmpty() ||
                        c[12].equals(
                                "Not started"
                        )) {

                    c[12] =
                            LocalDateTime
                                    .now()
                                    .format(formatter);
                }

                c[13] =
                        LocalDateTime
                                .now()
                                .format(formatter);

                saveDatabase();

                refreshTable();

                return;
            }
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteComplaint() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a complaint first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(
                        row
                );

        String complaintID =
                tableModel
                        .getValueAt(
                                modelRow,
                                0
                        )
                        .toString();

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete\n"
                                + complaintID
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (answer !=
                JOptionPane.YES_OPTION) {

            return;
        }

        for (int i = 0;
             i < data.size();
             i++) {

            if (data.get(i)[0]
                    .equals(
                            complaintID
                    )) {

                data.remove(i);

                break;
            }
        }

        saveDatabase();

        refreshTable();
    }

    // =========================================================
    // COMPLAINT DETAILS
    // =========================================================

    private void showDetails() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a complaint first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                table.convertRowIndexToModel(
                        row
                );

        String complaintID =
                tableModel
                        .getValueAt(
                                modelRow,
                                0
                        )
                        .toString();

        String[] c = null;

        for (String[] complaint : data) {

            if (complaint[0]
                    .equals(
                            complaintID
                    )) {

                c = complaint;

                break;
            }
        }

        if (c == null) {
            return;
        }

        String workBy =
                c.length > 11
                        ? c[11]
                        : "Not assigned yet";

        String workStarted =
                c.length > 12
                        ? c[12]
                        : "Not started";

        String workCompleted =
                c.length > 13
                        ? c[13]
                        : "Not completed";

        // =====================================================
        // DETAILS WINDOW
        // =====================================================

        JDialog dialog =
                new JDialog(
                        this,
                        "Complaint Details",
                        true
                );

        dialog.setSize(
                900, 690
        );

        dialog.setMinimumSize(
                new Dimension(
                        850, 650
                )
        );

        dialog.setLocationRelativeTo(this);

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0, 15
                        )
                );

        main.setBackground(DARK);

        main.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 22, 18, 22
                )
        );

        dialog.setContentPane(main);

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "COMPLAINT DETAILS  •  "
                                + c[0]
                );

        title.setForeground(CYAN);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        main.add(
                title,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel content =
                new JPanel(
                        new GridLayout(
                                1, 2, 15, 0
                        )
                );

        content.setOpaque(false);

        // LEFT PANEL
        JPanel left =
                createDetailsPanel(
                        "COMPLAINT INFORMATION"
                );

        addDetailRow(
                left,
                "Complaint ID",
                c[0]
        );

        addDetailRow(
                left,
                "Citizen Name",
                c[1]
        );

        addDetailRow(
                left,
                "Contact Number",
                c[2]
        );

        addDetailRow(
                left,
                "Category",
                c[3]
        );

        addDetailRow(
                left,
                "Area",
                c[4]
        );

        addDetailRow(
                left,
                "Exact Address",
                c[5]
        );

        addDetailRow(
                left,
                "Severity",
                c[6]
        );

        addDetailRow(
                left,
                "Priority",
                c[7]
        );

        addDetailRow(
                left,
                "Department",
                c[8]
        );

        addDetailRow(
                left,
                "Status",
                c[9]
        );

        // RIGHT PANEL
        JPanel right =
                createDetailsPanel(
                        "WORK HISTORY"
                );

        addDetailRow(
                right,
                "Work Done By",
                workBy
        );

        addDetailRow(
                right,
                "Work Started",
                workStarted
        );

        addDetailRow(
                right,
                "Work Completed",
                workCompleted
        );

        JLabel historyStatus =
                new JLabel(
                        getHistoryStatus(
                                c[9]
                        )
                );

        historyStatus.setForeground(
                getStatusColor(
                        c[9]
                )
        );

        historyStatus.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        historyStatus.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 10, 12, 10
                )
        );

        right.add(historyStatus);

        // DESCRIPTION
        JPanel descriptionPanel =
                new JPanel(
                        new BorderLayout()
                );

        descriptionPanel.setBackground(
                new Color(
                        2, 30, 35
                )
        );

        descriptionPanel.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        JLabel descriptionTitle =
                new JLabel(
                        "DESCRIPTION"
                );

        descriptionTitle.setForeground(CYAN);

        descriptionTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        descriptionTitle.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 10, 5, 10
                )
        );

        descriptionPanel.add(
                descriptionTitle,
                BorderLayout.NORTH
        );

        JLabel description =
                new JLabel(
                        "<html>"
                                + "<div style='width:320px;'>"
                                + escapeHTML(c[10])
                                + "</div>"
                                + "</html>"
                );

        description.setForeground(
                Color.WHITE
        );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        description.setVerticalAlignment(
                SwingConstants.TOP
        );

        description.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 10, 10, 10
                )
        );

        descriptionPanel.add(
                description,
                BorderLayout.CENTER
        );

        right.add(
                descriptionPanel
        );

        content.add(left);

        content.add(right);

        main.add(
                content,
                BorderLayout.CENTER
        );

        // =====================================================
        // CLOSE BUTTON
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        bottom.setOpaque(false);

        JButton close =
                createButton(
                        "CLOSE"
                );

        close.setPreferredSize(
                new Dimension(
                        130, 42
                )
        );

        close.addActionListener(
                e -> dialog.dispose()
        );

        bottom.add(close);

        main.add(
                bottom,
                BorderLayout.SOUTH
        );

        dialog.setVisible(true);
    }

    // =========================================================
    // DETAILS PANEL
    // =========================================================

    private JPanel createDetailsPanel(
            String headingText
    ) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                new Color(
                        2, 25, 30
                )
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 12, 10, 12
                        )
                )
        );

        JLabel heading =
                new JLabel(
                        headingText
                );

        heading.setForeground(CYAN);

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 8, 0
                )
        );

        panel.add(heading);

        return panel;
    }

    // =========================================================
    // DETAIL ROW
    // =========================================================

    private void addDetailRow(
            JPanel panel,
            String name,
            String value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                10, 0
                        )
                );

        row.setOpaque(true);

        row.setBackground(
                new Color(
                        3, 35, 40
                )
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0,
                                new Color(
                                        0, 70, 80
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                6, 8, 6, 8
                        )
                )
        );

        JLabel nameLabel =
                new JLabel(
                        name
                );

        nameLabel.setForeground(
                new Color(
                        160, 205, 210
                )
        );

        nameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        nameLabel.setPreferredSize(
                new Dimension(
                        130, 30
                )
        );

        JLabel valueLabel =
                new JLabel(
                        "<html>"
                                + escapeHTML(
                                        value
                                )
                                + "</html>"
                );

        valueLabel.setForeground(
                Color.WHITE
        );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        row.add(
                nameLabel,
                BorderLayout.WEST
        );

        row.add(
                valueLabel,
                BorderLayout.CENTER
        );

        panel.add(row);
    }

    // =========================================================
    // HISTORY STATUS
    // =========================================================

    private String getHistoryStatus(
            String status
    ) {

        if (status.equals("Resolved")) {

            return "✓ WORK COMPLETED";

        } else if (
                status.equals(
                        "In Progress"
                )
        ) {

            return "▶ WORK IN PROGRESS";

        } else {

            return "● WORK NOT STARTED";
        }
    }

    // =========================================================
    // STATUS COLOR
    // =========================================================

    private Color getStatusColor(
            String status
    ) {

        if (status.equals("Resolved")) {

            return new Color(
                    70, 255, 150
            );

        } else if (
                status.equals(
                        "In Progress"
                )
        ) {

            return new Color(
                    255, 220, 80
            );

        } else {

            return new Color(
                    255, 150, 100
            );
        }
    }

    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHTML(
            String text
    ) {

        if (text == null) {
            return "";
        }

        return text
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\n",
                        "<br>"
                );
    }

    // =========================================================
    // REFRESH
    // =========================================================

    private void refreshTable() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        for (String[] c : data) {

            if (c.length >= 10) {

                tableModel.addRow(
                        new Object[]{
                                c[0],
                                c[3],
                                c[4],
                                c[6],
                                c[7],
                                c[8],
                                c[9]
                        }
                );
            }
        }

        updateStatistics();
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private void updateStatistics() {

        int total =
                data.size();

        int pending = 0;

        int progress = 0;

        int resolved = 0;

        for (String[] c : data) {

            if (c.length < 10) {
                continue;
            }

            if (c[9].equals(
                    "Pending"
            )) {

                pending++;

            } else if (
                    c[9].equals(
                            "In Progress"
                    )
            ) {

                progress++;

            } else if (
                    c[9].equals(
                            "Resolved"
                    )
            ) {

                resolved++;
            }
        }

        totalLabel.setText(
                String.valueOf(
                        total
                )
        );

        pendingLabel.setText(
                String.valueOf(
                        pending
                )
        );

        progressLabel.setText(
                String.valueOf(
                        progress
                )
        );

        resolvedLabel.setText(
                String.valueOf(
                        resolved
                )
        );
    }

    // =========================================================
    // SAVE DATABASE
    // =========================================================

    private void saveDatabase() {

        try {

            ObjectOutputStream output =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    DB_FILE
                            )
                    );

            output.writeObject(data);

            output.writeInt(id);

            output.close();

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database save error:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // LOAD DATABASE
    // =========================================================

    @SuppressWarnings("unchecked")
    private void loadDatabase() {

        File file =
                new File(
                        DB_FILE
                );

        if (!file.exists()) {
            return;
        }

        try {

            ObjectInputStream input =
                    new ObjectInputStream(
                            new FileInputStream(
                                    file
                            )
                    );

            data =
                    (ArrayList<String[]>)
                            input.readObject();

            id =
                    input.readInt();

            input.close();

            // Upgrade old records

            for (int i = 0;
                 i < data.size();
                 i++) {

                String[] old =
                        data.get(i);

                if (old.length < 14) {

                    String[] updated =
                            new String[14];

                    for (
                            int j = 0;
                            j < old.length &&
                                    j < 14;
                            j++
                    ) {

                        updated[j] =
                                old[j];
                    }

                    if (updated[11] == null) {

                        updated[11] =
                                "Not assigned yet";
                    }

                    if (updated[12] == null) {

                        updated[12] =
                                "Not started";
                    }

                    if (updated[13] == null) {

                        updated[13] =
                                "Not completed";
                    }

                    data.set(
                            i,
                            updated
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Database could not be loaded."
            );
        }
    }

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void error(
            Component parent,
            String message
    ) {

        JOptionPane.showMessageDialog(
                parent,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================================
    // BACKGROUND IMAGE
    // =========================================================

    class BackgroundPanel
            extends JPanel {

        private Image backgroundImage;

        public BackgroundPanel() {

            try {

                java.net.URL imageURL =
                        SmartCityComplaintSystem.class
                                .getResource(
                                        "/smart_city_bg.png"
                                );

                if (imageURL != null) {

                    backgroundImage =
                            new ImageIcon(
                                    imageURL
                            ).getImage();
                }

            } catch (Exception e) {

                backgroundImage = null;
            }

            setOpaque(true);
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            int width =
                    getWidth();

            int height =
                    getHeight();

            if (backgroundImage == null) {

                g.setColor(DARK);

                g.fillRect(
                        0,
                        0,
                        width,
                        height
                );

                return;
            }

            int imageWidth =
                    backgroundImage
                            .getWidth(this);

            int imageHeight =
                    backgroundImage
                            .getHeight(this);

            if (imageWidth <= 0 ||
                    imageHeight <= 0) {

                g.setColor(DARK);

                g.fillRect(
                        0,
                        0,
                        width,
                        height
                );

                return;
            }

            double scaleX =
                    (double) width
                            / imageWidth;

            double scaleY =
                    (double) height
                            / imageHeight;

            double scale =
                    Math.max(
                            scaleX,
                            scaleY
                    );

            int newWidth =
                    (int)
                            (imageWidth * scale);

            int newHeight =
                    (int)
                            (imageHeight * scale);

            int x =
                    (width - newWidth)
                            / 2;

            int y =
                    (height - newHeight)
                            / 2;

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            g2.drawImage(
                    backgroundImage,
                    x,
                    y,
                    newWidth,
                    newHeight,
                    this
            );

            // Dark overlay

            g2.setColor(
                    new Color(
                            0,
                            10,
                            15,
                            145
                    )
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    SmartCityComplaintSystem app =
                            new SmartCityComplaintSystem();

                    app.setVisible(true);
                }
        );
    }
}