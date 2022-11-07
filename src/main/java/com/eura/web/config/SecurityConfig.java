package com.eura.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()   //csrf 미적용
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    .antMatchers("/meet","/live").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/denied");

        // http.formLogin()
        //         .loginPage("/login")
        //         .defaultSuccessUrl("/")
        //         .permitAll();

        // http.logout()
        //         .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        //         .logoutSuccessUrl("/login")
        //         .invalidateHttpSession(true);

        // http
    }

    // @Override
    // public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    // }


   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/form").hasRole("ADMIN")  // Specific api method request based on role.
                .antMatchers("/home","/basic").permitAll()  // permited urls to guest users(without login).
                .anyRequest().authenticated()
                .and()
                .formLogin()       // not specified form page to use default login page of spring security
                .permitAll()
                .and()
                .logout().deleteCookies("JSESSIONID")  // delete memory of browser after logout

                .and()
                .rememberMe().key("uniqueAndSecret"); // remember me check box enabled.

        http.csrf().disable();  // ADD THIS CODE TO DISABLE CSRF IN PROJECT.**
    }*/
}
