
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class PayrollSystem extends JFrame implements ActionListener {

    JPanel loginPanel, adminPanel, employeePanel;
    CardLayout cardLayout;
    Connection con;

    JTextField txtAdminUser, txtEmpUser, txtId, txtName, txtBasic,
            txtDeductions, txtOt, txtAttendance, txtEmpId;
    JPasswordField txtAdminPass, txtEmpPass;

    JButton btnAdminLogin, btnEmpLogin, btnAdd, btnUpdate,
            btnLogoutAdmin, btnLogoutEmp, btnView, btnShowEmployees;

    JTextArea adminArea, empArea;

    public PayrollSystem() {
        setTitle("Employee Payroll Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        connectDatabase();
        createLoginPanel();
        createAdminPanel();
        createEmployeePanel();

        add(loginPanel, "Login");
        add(adminPanel, "Admin");
        add(employeePanel, "Employee");

        cardLayout.show(getContentPane(), "Login");
    }

    // ------------------ DATABASE ------------------
    public void connectDatabase() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            System.out.println("✅ Database Connected");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Database Connection Failed\n" + e.getMessage());
        }
    }

    // ------------------ LOGIN PANEL ------------------
    void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Admin Login
        JPanel adminLoginPanel = new JPanel(new GridBagLayout());
        adminLoginPanel.setBorder(BorderFactory.createTitledBorder("👑 Admin Login"));
        GridBagConstraints a = new GridBagConstraints();
        a.insets = new Insets(10, 10, 10, 10);

        txtAdminUser = new JTextField(15);
        txtAdminPass = new JPasswordField(15);
        btnAdminLogin = new JButton("Login as Admin");

        a.gridx = 0; a.gridy = 0;
        adminLoginPanel.add(new JLabel("Username:"), a);
        a.gridx = 1;
        adminLoginPanel.add(txtAdminUser, a);

        a.gridx = 0; a.gridy = 1;
        adminLoginPanel.add(new JLabel("Password:"), a);
        a.gridx = 1;
        adminLoginPanel.add(txtAdminPass, a);

        a.gridx = 0; a.gridy = 2; a.gridwidth = 2;
        adminLoginPanel.add(btnAdminLogin, a);

        // Employee Login
        JPanel empLoginPanel = new JPanel(new GridBagLayout());
        empLoginPanel.setBorder(BorderFactory.createTitledBorder("💼 Employee Login"));
        GridBagConstraints e = new GridBagConstraints();
        e.insets = new Insets(10, 10, 10, 10);

        txtEmpUser = new JTextField(15);
        txtEmpPass = new JPasswordField(15);
        btnEmpLogin = new JButton("Login as Employee");

        e.gridx = 0; e.gridy = 0;
        empLoginPanel.add(new JLabel("Username:"), e);
        e.gridx = 1;
        empLoginPanel.add(txtEmpUser, e);

        e.gridx = 0; e.gridy = 1;
        empLoginPanel.add(new JLabel("Password:"), e);
        e.gridx = 1;
        empLoginPanel.add(txtEmpPass, e);

        e.gridx = 0; e.gridy = 2; e.gridwidth = 2;
        empLoginPanel.add(btnEmpLogin, e);

        loginPanel.add(adminLoginPanel);
        loginPanel.add(empLoginPanel);

        btnAdminLogin.addActionListener(this);
        btnEmpLogin.addActionListener(this);
    }

    // ------------------ ADMIN PANEL ------------------
    void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout(10, 10));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        txtId = new JTextField();
        txtName = new JTextField();
        txtBasic = new JTextField();
        txtDeductions = new JTextField();
        txtOt = new JTextField();
        txtAttendance = new JTextField();

        topPanel.add(new JLabel("Employee ID:")); topPanel.add(txtId);
        topPanel.add(new JLabel("Name:")); topPanel.add(txtName);
        topPanel.add(new JLabel("Basic Salary:")); topPanel.add(txtBasic);
        topPanel.add(new JLabel("Deductions:")); topPanel.add(txtDeductions);
        topPanel.add(new JLabel("Overtime Hours:")); topPanel.add(txtOt);
        topPanel.add(new JLabel("Attendance (days):")); topPanel.add(txtAttendance);

        JPanel buttonPanel = new JPanel();

        btnAdd = new JButton("Add Employee ➕");
        btnUpdate = new JButton("Update Employee 🔄");
        btnShowEmployees = new JButton("Show Employees 📋");
        btnLogoutAdmin = new JButton("Logout 🚪");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnShowEmployees);
        buttonPanel.add(btnLogoutAdmin);

        adminArea = new JTextArea(12, 40);
        adminArea.setEditable(false);

        adminPanel.add(topPanel, BorderLayout.NORTH);
        adminPanel.add(new JScrollPane(adminArea), BorderLayout.CENTER);
        adminPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnShowEmployees.addActionListener(this);
        btnLogoutAdmin.addActionListener(this);
    }

    // ------------------ EMPLOYEE PANEL ------------------
    void createEmployeePanel() {
        employeePanel = new JPanel(new BorderLayout(10, 10));
        employeePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel();
        txtEmpId = new JTextField(10);
        btnView = new JButton("View Details 👁️");
        btnLogoutEmp = new JButton("Logout 🚪");

        top.add(new JLabel("Enter Employee ID:"));
        top.add(txtEmpId);
        top.add(btnView);
        top.add(btnLogoutEmp);

        empArea = new JTextArea(12, 40);
        empArea.setEditable(false);

        employeePanel.add(top, BorderLayout.NORTH);
        employeePanel.add(new JScrollPane(empArea), BorderLayout.CENTER);

        btnView.addActionListener(this);
        btnLogoutEmp.addActionListener(this);
    }

    // ------------------ ACTION HANDLER ------------------
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnAdminLogin) adminLogin();
            else if (e.getSource() == btnEmpLogin) employeeLogin();
            else if (e.getSource() == btnAdd) addEmployee();
            else if (e.getSource() == btnUpdate) updateEmployee();
            else if (e.getSource() == btnShowEmployees) showAllEmployees();
            else if (e.getSource() == btnView) viewEmployee();
            else if (e.getSource() == btnLogoutAdmin || e.getSource() == btnLogoutEmp)
                cardLayout.show(getContentPane(), "Login");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ------------------ LOGIC METHODS ------------------
    void adminLogin() throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?");
        pst.setString(1, txtAdminUser.getText());
        pst.setString(2, new String(txtAdminPass.getPassword()));

        ResultSet rs = pst.executeQuery();
        if (rs.next() && rs.getString("role").equals("admin"))
            cardLayout.show(getContentPane(), "Admin");
        else
            JOptionPane.showMessageDialog(this, "❌ Invalid Admin Login!");
    }

    void employeeLogin() throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?");
        pst.setString(1, txtEmpUser.getText());
        pst.setString(2, new String(txtEmpPass.getPassword()));

        ResultSet rs = pst.executeQuery();
        if (rs.next() && rs.getString("role").equals("employee"))
            cardLayout.show(getContentPane(), "Employee");
        else
            JOptionPane.showMessageDialog(this, "❌ Invalid Employee Login!");
    }

    void addEmployee() throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "INSERT INTO employees VALUES(?,?,?,?,?,?)");
        pst.setInt(1, Integer.parseInt(txtId.getText()));
        pst.setString(2, txtName.getText());
        pst.setDouble(3, Double.parseDouble(txtBasic.getText()));
        pst.setDouble(4, Double.parseDouble(txtDeductions.getText()));
        pst.setInt(5, Integer.parseInt(txtOt.getText()));
        pst.setInt(6, Integer.parseInt(txtAttendance.getText()));
        pst.executeUpdate();

        adminArea.setText("✅ Employee Added Successfully!");
    }

    void updateEmployee() throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "UPDATE employees SET name=?, basic=?, deductions=?, ot=?, attendance=? WHERE id=?");
        pst.setString(1, txtName.getText());
        pst.setDouble(2, Double.parseDouble(txtBasic.getText()));
        pst.setDouble(3, Double.parseDouble(txtDeductions.getText()));
        pst.setInt(4, Integer.parseInt(txtOt.getText()));
        pst.setInt(5, Integer.parseInt(txtAttendance.getText()));
        pst.setInt(6, Integer.parseInt(txtId.getText()));
        pst.executeUpdate();

        adminArea.setText("✅ Employee Updated Successfully!");
    }

    void showAllEmployees() throws SQLException {
        PreparedStatement pst = con.prepareStatement("SELECT * FROM employees");
        ResultSet rs = pst.executeQuery();

        StringBuilder sb = new StringBuilder("===== Employee List =====\n\n");

        while (rs.next()) {
            double basic = rs.getDouble("basic");
            double deductions = rs.getDouble("deductions");
            int ot = rs.getInt("ot");
            int attendance = rs.getInt("attendance");

            double total = basic + (ot * 100) - deductions;
            total *= (attendance / 30.0);

            sb.append("ID: ").append(rs.getInt("id"))
              .append("\nName: ").append(rs.getString("name"))
              .append("\nNet Pay: ₹").append(String.format("%.2f", total))
              .append("\n--------------------------\n");
        }

        adminArea.setText(sb.toString());
    }

    void viewEmployee() throws SQLException {
        PreparedStatement pst = con.prepareStatement(
                "SELECT * FROM employees WHERE id=?");
        pst.setInt(1, Integer.parseInt(txtEmpId.getText()));

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            double total = rs.getDouble("basic") +
                    (rs.getInt("ot") * 100) -
                    rs.getDouble("deductions");

            total *= (rs.getInt("attendance") / 30.0);

            empArea.setText(
                    "Name: " + rs.getString("name") +
                    "\nNet Pay: ₹" + String.format("%.2f", total)
            );
        } else {
            empArea.setText("No employee found!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new PayrollSystem().setVisible(true));
    }
}
