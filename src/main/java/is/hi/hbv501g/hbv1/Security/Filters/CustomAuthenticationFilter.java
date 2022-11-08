package is.hi.hbv501g.hbv1.Security.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Middleware for authentication of user
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    /**
     * Authentication Middleware that is called when front end attempts authentication
     * which is mainly logging in (aka. POST /api/login)
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username, password;
        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            System.out.println("request: " + request);
            username = requestMap.get("username");
            password = requestMap.get("password");

            log.info(username, password);
        } catch (IOException e){
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        log.info("Username is {}", username); log.info("Password is {}", password);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    /**
     * Authentication Middleware that is called when authentication is successful and
     * creates jwt tokens for access and refresh and puts them in response body
     */
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication) throws IOException, ServletException {

        //Ástæðan fyrir því að getUserByUsername var implementað í userservice
        User user = (User) authentication.getPrincipal(); // ATH ekki sami user og í persitance.entities
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //TODO: refactora secret
        //Hversu lengi endist acccess
        long accessExpirationTimeInMillis = (10*60*1000); //10 min
        //Hversu lengi endist refresh
        long refreshExpirationTimeInMillis = (12*60*60*1000); //12 hours

        //Create jwt accessToken
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpirationTimeInMillis))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        //Create jwt refreshToken
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationTimeInMillis))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        //Láta response-ið vera json hlut
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
