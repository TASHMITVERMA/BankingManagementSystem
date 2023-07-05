import java.io.*;
import java.util.Scanner;

public class BankingManagementSystem {
    private final String FILE_NAME = "accounts.txt";

    public void createAccount(int accountNumber, String accountHolderName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            if (isAccountExists(accountNumber)) {
                System.out.println("Account already exists.");
                return;
            }
            writer.write(accountNumber + "," + accountHolderName + ",0.0");
            writer.newLine();
            System.out.println("Account created successfully.");
        } catch (IOException e) {
            System.out.println("Error while creating account.");
        }
    }

    public void depositMoney(int accountNumber, double amount) {
        if (!isAccountExists(accountNumber)) {
            System.out.println("Account not found.");
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
            System.out.println("Amount deposited successfully.");
            System.out.println("Current balance: " + getCurrentBalance(accountNumber));
        } catch (IOException e) {
            System.out.println("Error while depositing amount.");
        }
    }

    public void withdrawMoney(int accountNumber, double amount) {
        if (!isAccountExists(accountNumber)) {
            System.out.println("Account not found.");
            return;
        }
        double currentBalance = getCurrentBalance(accountNumber);
        if (currentBalance < amount) {
            System.out.println("Insufficient balance.");
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
                fileContent.append(line).append(System.lineSeparator());//this line written by referring google
            }
            reader.close();
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(fileContent.toString());
            writer.close();
            System.out.println("Amount withdrawn successfully.");
            System.out.println("Current balance: " + getCurrentBalance(accountNumber));
        } catch (IOException e) {
            System.out.println("Error` while withdrawing amount.");
        }
    }

    public void checkBalance(int accountNumber) {
        if (!isAccountExists(accountNumber)) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Current balance: " + getCurrentBalance(accountNumber));
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
            System.out.println("Error while checking account existence.");
        }
        return false;
    }

    private double getCurrentBalance(int accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int accNumber = Integer.parseInt(parts[0]);
                if (accNumber == accountNumber) {System.out.println("Name: "+parts[1]);
                    return Double.parseDouble(parts[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while getting current balance.");
        }
        return 0.0;
    }

    public static void main(String[] args) {
        BankingManagementSystem bank = new BankingManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nBanking Management System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    int accountNumber = scanner.nextInt();
                    System.out.print("Enter account holder name: ");
                    String accountHolderName = scanner.next();
                    bank.createAccount(accountNumber, accountHolderName);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    bank.depositMoney(accountNumber, depositAmount);
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    bank.withdrawMoney(accountNumber, withdrawAmount);
                    break;
                case 4:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextInt();
                    bank.checkBalance(accountNumber);
                    break;
                case 5:
                    System.out.println("Thank you for using the Banking Management System.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
