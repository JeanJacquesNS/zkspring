package com.example.zkspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Jean Jacques N. Shimwa
 * @created 23-02-2023 - 12:38 PM
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    public static final String ZUL_FILES="/zkau/web/**/*.zul";
    public static final String[] ZK_RESOURCES = {"/zkau/web/**/js/**","/zkau/web/**/zul/css/**","/zkau/web/**/img/**"};

    //permite o desktop fazer a limpeza depois de logout ou quando recarrega a pagina de login
    public static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(ZUL_FILES).denyAll()//bloqueia acesso directo aos ficheiros zul
                .antMatchers(HttpMethod.GET,ZK_RESOURCES).permitAll()//Permite todos recursos zk
                .regexMatchers(HttpMethod.GET,REMOVE_DESKTOP_REGEX).permitAll()//Permite o desktop fazer a limpeza
                .requestMatchers(httpServletRequest -> "rmDesktop".equals(httpServletRequest.getParameter("cmd_0"))).permitAll()//permite o desktop limpar a partir de ZATS
                .mvcMatchers("/","/login","logout").permitAll()
                .mvcMatchers("/secure/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/secure/main")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        UserDetails user=
                User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("password")
                    .roles("USER")
                    .build();

        return new InMemoryUserDetailsManager(user);

    }
}
