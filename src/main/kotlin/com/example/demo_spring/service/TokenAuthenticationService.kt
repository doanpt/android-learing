package com.example.demo_spring.service

import javax.servlet.http.HttpServletRequest
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.*
import javax.servlet.http.HttpServletResponse

object TokenAuthenticationService {

    private const val EXPIRE_TIME: Long = 864000000 // 10 days
    private const val SECRET = "AndroidT3h"
    private const val TOKEN_PREFIX = "Bearer"
    private const val HEADER_STRING = "Authorization"


    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            val user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .subject
            return if (user != null) UsernamePasswordAuthenticationToken(user, null, emptyList()) else null
        }
        return null
    }

    fun addAuthentication(response: HttpServletResponse, name: String?) {
        val token = Jwts.builder()
                .setSubject(name)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact()
        response.addHeader(HEADER_STRING, "$TOKEN_PREFIX $token")
    }
}