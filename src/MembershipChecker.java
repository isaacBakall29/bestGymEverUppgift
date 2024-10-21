import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MembershipChecker {

    private boolean found = false;
    private String currentStatus = null;

    public MembershipChecker(){

        try{
            Scanner scanner = new Scanner(System.in);

        while(true){
        System.out.println("Enter social security number or name:");
        String input = scanner.nextLine();

        checkMembershipStatus(input);

        }
        }catch(NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    public MembershipChecker(String input){
        checkMembershipStatus(input);
    }


    public static void main(String[] args) {

        MembershipChecker checker = new MembershipChecker();
    }

    public boolean isFound() {
        return found;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void checkMembershipStatus(String input) {
        try (BufferedReader br = new BufferedReader(new FileReader("Membership.txt"))) {
            String line;
            found = false;

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

                checkStatus(input, name, personalNumber, daysSinceLastPayment);
            }

            if (!found) {
                System.out.println("The person is not found in the system and has never been a member");
                currentStatus = "not member";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkStatus(String input, String name, String personalNumber, long daysSinceLastPayment) {
        if (input.equals(personalNumber) || input.equalsIgnoreCase(name)) {
            found = true;
            if (daysSinceLastPayment <= 365) {
                System.out.println(name + " is a current member");
                currentStatus = "member";
                logMemberVisit(name, personalNumber);

            } else {
                System.out.println(name + " is a former member");
                currentStatus = "former";
            }

        }
    }


    public void logMemberVisit(String name, String personalNumber) {
        LocalDateTime time = LocalDateTime.now();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("GymVisits.txt", true))) {
            writer.write(name + ": " + personalNumber +": " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
