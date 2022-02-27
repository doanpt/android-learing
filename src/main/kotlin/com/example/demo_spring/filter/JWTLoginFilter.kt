package com.example.demo_spring.filter

import com.example.demo_spring.service.TokenAuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(url: String?, authenticationManager: AuthenticationManager?) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {
    init {
        setAuthenticationManager(authenticationManager)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val username = request.getParameter("username")
        val password = request.getParameter("password")
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password, listOf()))
    }


    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        TokenAuthenticationService.addAuthentication(response, authResult.name)
    }
}