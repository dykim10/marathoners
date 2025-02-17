import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        // spring 4.0.0
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordEncoded = encoder.encode("1234");
        System.out.println(passwordEncoded);
    }
}
