package is.hi.hbv501g.hbv1.Security;

import is.hi.hbv501g.hbv1.Security.Filters.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Configures security of routing by intercepting all http requests and inserting Middleware that
 * Restricts and cleans up http requests
 */
@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { //This is deprecated but so are you opinions
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Provide the userDetailsService with the passwordEncoder used for password encryption
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Configure http request security such as csrf and providing middleware responsible for restricting access to api
     * according to role of user
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //TODO: Breyta friends í user Endpoint
        //TODO: VELJA NON-VERIFIED ENDPOINTS
        http.authorizeRequests()
            .antMatchers("/", "/api/words/**", "/api/quotes/**", "/api/login/**", "/api/refreshToken/**","/friends/**")
            .permitAll();

        //TODO: VELJA USER ENDPOINT
        http.authorizeRequests()
            .antMatchers(GET, "/api/user/**")
            .hasAnyAuthority("ROLE_USER");

        //TODO: VELJA ADMIN ONLY ENDPOINT
        http.authorizeRequests()
            .antMatchers(POST, "/api/role/save")
            .hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();

        //Middleware authorization / authentication

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean());

       //Velja hvar authentication á sér stað
       customAuthenticationFilter.setFilterProcessesUrl("/api/login");

       http.addFilter(customAuthenticationFilter);
       http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * Magic Bean
     * @return A beanstalk leading to the Giants Castle, the home of the golden goose!
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
