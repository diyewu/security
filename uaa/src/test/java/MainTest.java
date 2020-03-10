import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class MainTest {
    public static void main(String[] args) {
        test();
    }

    public static void test1(){

    }
    public static void test(){
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
    }
}
