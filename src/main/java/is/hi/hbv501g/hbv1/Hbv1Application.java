package is.hi.hbv501g.hbv1;

import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Hbv1Application {
    //Valdi var hér
    //Ívan var hér!!!
    public static void main(String[] args) {
        SpringApplication.run(Hbv1Application.class, args);
    }
//    @Bean
//    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
//
//    @Bean
//    CommandLineRunner run(UserService userService){
//        return (args) -> {
//        };
//    }
}
