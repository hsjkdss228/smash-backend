package kr.megaptera.smash;

import kr.megaptera.smash.interceptors.AuthenticationInterceptor;
import kr.megaptera.smash.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SmashApplication {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public static void main(String[] args) {
        SpringApplication.run(SmashApplication.class, args);
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().antMatchers("/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(authenticationInterceptor());
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.DELETE.name()
                    );
            }
        };
    }

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(jwtUtil());
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecret);
    }
}
