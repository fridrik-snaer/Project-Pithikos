package is.hi.hbv501g.hbv1.Security;

import is.hi.hbv501g.hbv1.Security.Filters.CustomAuthenticationFilter;
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

import static org.springframework.http.HttpMethod.GET;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
            .antMatchers( //TODO: VELJA NON-VERIFIED ENDPOINTS
                    "/api/words/**",
                    "/api/quotes/**",
                    "/api/login/**",
                    "/api/token/refresh/**")
            .permitAll();

        http.authorizeRequests()
            .antMatchers(GET, //TODO: VELJA USER ENDPOINT
                    "/api/user/**",
                    "/api/users/**")
            .hasAnyAuthority("ROLE_USER");
        http.authorizeRequests()
            .antMatchers() //TODO: VELJA ADMIN ONLY ENDPOINT
            .hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();

        //Middleware authorization / authentication

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean());

       //Velja hvar authentication á sér stað
       customAuthenticationFilter.setFilterProcessesUrl("/api/login");

       http.addFilter(customAuthenticationFilter);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
