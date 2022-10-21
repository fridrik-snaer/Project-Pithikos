package is.hi.hbv501g.hbv1.Security;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MVCConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        final Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("charset", "utf-8");
        configurer.defaultContentType(new MediaType(MediaType.APPLICATION_JSON, parameterMap));
    }
}

