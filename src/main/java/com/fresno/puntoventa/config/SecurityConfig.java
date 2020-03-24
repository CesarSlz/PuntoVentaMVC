package com.fresno.puntoventa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fresno.puntoventa.service.PuntoVentaServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(
					"/", 
					"/login", 
					"/js/**",
					"/css/**", 
					"/icons/**", 
					"/img/**", 
					"/webjars/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/inicio")
				.failureUrl("/login?error")
				.usernameParameter("telefono")
				.passwordParameter("contrasena")
			.and()
			.logout()
				.permitAll()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutSuccessUrl("/login?logout");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);

		return bCryptPasswordEncoder;
	}
		
	@Autowired
	PuntoVentaServiceImpl service;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(service).passwordEncoder(passwordEncoder());
	}
}
