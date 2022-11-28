package jupiterpi.vocabulum.webappserver;

import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().and().authorizeRequests()
                    .anyRequest().permitAll();
        /*http.httpBasic().and().authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers("/ping2").authenticated()
                .antMatchers("/secret").authenticated()
                .antMatchers("/admin").hasRole("ADMIN");*/
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new DbAuthenticationProvider());
    }
}
