import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMWithdrawal {
    static void withdraw(int accountNumber, double amount, double balance) {
        if (accountNumber <= 0) {
            throw new IllegalArgumentException("Account number must be positive");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient balance");
        }
        System.out.printf("Withdrawal successful. Remaining balance: %.2f%n", balance - amount);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter account number: ");
            int account = sc.nextInt();
            System.out.print("Enter withdrawal amount: ");
            double amount = sc.nextDouble();

            // Simulated account balance for this lab exercise.
            double balance = 5000.00;
            withdraw(account, amount, balance);
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input: enter numeric values.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid request: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Transaction declined: " + e.getMessage());
        } finally {
            System.out.println("ATM session finished.");
        }
    }
}
