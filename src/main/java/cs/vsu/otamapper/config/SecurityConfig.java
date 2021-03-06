package cs.vsu.otamapper.config;

import cs.vsu.otamapper.security.jwt.JwtConfigurer;
import cs.vsu.otamapper.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String ORGANIZATION_ENDPOINT = "/api/v1/organization";
    private static final String USER_ENDPOINT = "/api/v1/user";
    private static final String RULE_ENDPOINT = "/api/v1/rule";
    private static final String OTA_DICTIONARY_ENDPOINT = "/api/v1/dictionary";
    private static final String MAP_ENDPOINT = "/api/v1/map";
    private static final String OPEN_API_ENDPOINT = "/api/v1/openapi/**";

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(ORGANIZATION_ENDPOINT).hasAnyRole()
                .antMatchers(USER_ENDPOINT).hasAnyRole()
                .antMatchers(RULE_ENDPOINT).hasAnyRole()
                .antMatchers(OTA_DICTIONARY_ENDPOINT).hasAnyRole()
                .antMatchers(MAP_ENDPOINT).hasAnyRole()
                .antMatchers(OPEN_API_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
