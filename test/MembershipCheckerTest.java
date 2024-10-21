import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MembershipCheckerTest {

@Test
    public void checkIfUserFound() {

    MembershipChecker checker = new MembershipChecker("Alhambra Aromes");

    assertTrue(checker.isFound());

    checker = new MembershipChecker("7608021234");

    assertTrue(checker.isFound());
}

@Test
    public void checkStatus() {
    MembershipChecker checker = new MembershipChecker("");

    checker.checkStatus("test", "test", "123", 35);
    assertEquals("member", checker.getCurrentStatus());

    checker.checkStatus("test", "test", "123", 465);
    assertEquals("former", checker.getCurrentStatus());

    checker = new MembershipChecker("");
    assertEquals("not member", checker.getCurrentStatus());

}

@Test
    public void checkPtPrint(){
    try (BufferedReader br = new BufferedReader(new FileReader("GymVisits.txt"))) {

        MembershipChecker checker = new MembershipChecker("");
        checker.checkStatus("test", "test", "123", 65);

        LocalDateTime time = LocalDateTime.now();
        String current;
        String last = null;

        while ((current = br.readLine()) != null) {
            last = current;
        }

        assertEquals("test: 123: " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), last);
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    }




}
