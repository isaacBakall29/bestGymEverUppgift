import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class MembershipChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter social security number or name:");
        String input = scanner.nextLine();

        checkMembershipStatus(input);
        scanner.close();
    }

    public static void checkMembershipStatus(String input) {
        try (BufferedReader br = new BufferedReader(new FileReader("Membership.txt"))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] memberInfo = line.split(",", 2);
                String personalNumber = memberInfo[0].trim();
                String name = memberInfo[1].trim();

                String lastPaymentDateLine = br.readLine();
                if (lastPaymentDateLine == null) {
                    break;
                }

                LocalDate lastPaymentDate = LocalDate.parse(lastPaymentDateLine.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long daysSinceLastPayment = ChronoUnit.DAYS.between(lastPaymentDate, LocalDate.now());

                if (input.equals(personalNumber) || input.equalsIgnoreCase(name)) {
                    found = true;
                    if (daysSinceLastPayment <= 365) {
                        System.out.println(name + " is a current member");
                    } else {
                        System.out.println(name + " is a former member");
                    }
                    // Log the member's visit
                    logMemberVisit(name, personalNumber);
                    break;
                }
            }

            if (!found) {
                System.out.println("The person is not found in the system and has never been a member");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void logMemberVisit(String name, String personalNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("GymVisits.txt", true))) {
            writer.write(name + ": 1");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
