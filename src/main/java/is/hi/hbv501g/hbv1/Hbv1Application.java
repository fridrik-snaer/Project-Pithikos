package is.hi.hbv501g.hbv1;

import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.Role;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.StatisticsService;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Hbv1Application {
    //Valdi var hér
    //Ívan var hér!!!
    public static void main(String[] args) {
        System.out.println(Math.sqrt(9));

        SpringApplication.run(Hbv1Application.class, args);
    }

    /**
     * A bean that provides the application access to the BCryptPasswordEnoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    /**
     * A bean that allows us to inject data into the database
     */
/*    @Bean
    CommandLineRunner run(UserService userService){
        return (args) -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));
            userService.saveUser(new User("admin", "admin", "admin"));
            userService.addRoleToUser("admin", "ROLE_ADMIN");
            userService.addRoleToUser("admin", "ROLE_USER");
        };
    }*/

    /**
     * A bean that configures application cors policy configuration
     */
     @Bean
     CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://ritill.herokuapp.com"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
