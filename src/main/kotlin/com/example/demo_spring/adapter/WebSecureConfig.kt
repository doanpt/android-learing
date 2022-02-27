package com.example.demo_spring.adapter

import com.example.demo_spring.filter.JWTAuthenticationFilter
import com.example.demo_spring.filter.JWTLoginFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.sql.DataSource

//https://www.toptal.com/spring/spring-security-tutorial
@EnableWebSecurity
@Configuration
class WebSecureConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var dataSource: DataSource

    @Value("\${spring.queries.users-query}")
    lateinit var userQuery: String

    @Value("\${spring.queries.roles-query}")
    lateinit var roleQuery: String

//    Having configured the authentication manager, we now need to configure web security.
//    We are implementing a REST API and need stateless authentication with a JWT token; therefore, we need to set the following options:
//
//    1. Enable CORS and disable CSRF.
//    2. Set session management to stateless.(optional)
//    3. Set unauthorized requests exception handler(optional).
//    4. Set permissions on endpoints.
//    5. Add JWT token filter.

    //Configure the authentication manager with the correct provider
    override fun configure(auth: AuthenticationManagerBuilder) {
        //TODO: must remove supper.configure(auth)
        //For test
        //auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("admin");
        //auth.inMemoryAuthentication().withUser("member").password("{noop}member").roles("member");

        //for authentication
//        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("admin")
//        auth.inMemoryAuthentication().withUser("member").password("{noop}member").roles("member")
//        https://www.toptal.com/spring/spring-security-tutorial
        auth.jdbcAuthentication()
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(roleQuery)
                .dataSource(dataSource)
                .passwordEncoder(BCryptPasswordEncoder())
    }

    //Configure web security (public URLs, private URLs, authorization, etc.)
    override fun configure(http: HttpSecurity) {
        //TODO: must remove supper.configure(http)
        //Enable CORS and disable CSRF
        http.csrf().disable()
                // Set session management to stateless
                // Set permissions on endpoints
                .authorizeRequests()
                // Our public endpoints
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/student_jpa/").permitAll()
                // Our private endpoints
                .anyRequest().authenticated().and()
                //Add filter
                .addFilterBefore(JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}