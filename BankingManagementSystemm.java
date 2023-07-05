import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class BankingManagementSystemm extends JFrame {
    private final String FILE_NAME = "accounts.txt";

    private JLabel label1, label2, label3, label4;
    private JTextField txtAccountNumber, txtAccountHolderName, txtAmount;
    private JButton btnCreateAccount, btnDeposit, btnWithdraw, btnCheckBalance;

    public BankingManagementSystemm() {
        setTitle("Banking Management System");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        label1 = new JLabel("Account Number");
        txtAccountNumber = new JTextField();

        label2 = new JLabel("Account Holder Name");
        txtAccountHolderName = new JTextField();

        label3 = new JLabel("Amount");
        txtAmount = new JTextField();

        btnCreateAccount = new JButton("Create Account");
        btnDeposit = new JButton("Deposit");
        btnWithdraw = new JButton("Withdraw");
        btnCheckBalance = new JButton("Check Balance");

        btnCreateAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText());
                String accountHolderName = txtAccountHolderName.getText();
                createAccount(accountNumber, accountHolderName);
            }
        });

        btnDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText());
                double amount = Double.parseDouble(txtAmount.getText());
                depositMoney(accountNumber, amount);
            }
        });

        btnWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText());
                double amount = Double.parseDouble(txtAmount.getText());
                withdrawMoney(accountNumber, amount);
            }
        });

        btnCheckBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int accountNumber = Integer.parseInt(txtAccountNumber.getText());
                checkBalance(accountNumber);
            }
        });

        add(label1);
        add(txtAccountNumber);
        add(label2);
        add(txtAccountHolderName);
        add(label3);
        add(txtAmount);
        add(btnCreateAccount);
        add(btnDeposit);
        add(btnWithdraw);
        add(btnCheckBalance);
    }

    public void createAccount(int accountNumber, String accountHolderName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            if (isAccountExists(accountNumber)) {
                JOptionPane.showMessageDialog(this, "Account already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            writer.write(accountNumber + "," + accountHolderName + ",0.0");
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while creating account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void depositMoney(int accountNumber, double amount) {
        if (!isAccountExists(accountNumber)) {
            JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNumber = Integer.parseInt(parts[0]);
                if (accNumber == accountNumber) {
                    double currentBalance = Double.parseDouble(parts[2]);
                    double newBalance = currentBalance + amount;
                    line = accNumber + "," + parts[1] + "," + newBalance;
                }
                fileContent.append(line).append(System.lineSeparator());
            }
            reader.close();
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(fileContent.toString());
            writer.close();
            JOptionPane.showMessageDialog(this, "Amount deposited successfully.\nCurrent balance: " + getCurrentBalance(accountNumber), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while depositing amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void withdrawMoney(int accountNumber, double amount) {
        if (!isAccountExists(accountNumber)) {
            JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double currentBalance = getCurrentBalance(accountNumber);
        if (currentBalance < amount) {
            JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNumber = Integer.parseInt(parts[0]);
                if (accNumber == accountNumber) {
                    double newBalance = currentBalance - amount;
                    line = accNumber + "," + parts[1] + "," + newBalance;
                }
                fileContent.append(line).append(System.lineSeparator());
            }
            reader.close();
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(fileContent.toString());
            writer.close();
            JOptionPane.showMessageDialog(this, "Amount withdrawn successfully.\nCurrent balance: " + getCurrentBalance(accountNumber), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while withdrawing amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void checkBalance(int accountNumber) {
        if (!isAccountExists(accountNumber)) {
            JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Current balance: " + getCurrentBalance(accountNumber), "Balance", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isAccountExists(int accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNumber = Integer.parseInt(parts[0]);
                if (accNumber == accountNumber) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while checking account existence.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private double getCurrentBalance(int accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNumber = Integer.parseInt(parts[0]);
                if (accNumber == accountNumber) {
                    return Double.parseDouble(parts[2]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while getting current balance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BankingManagementSystemm().setVisible(true);
            }
        });
    }
}
