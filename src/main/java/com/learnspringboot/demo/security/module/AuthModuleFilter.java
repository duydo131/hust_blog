package com.learnspringboot.demo.security.module;

import com.learnspringboot.demo.config.Contants;
import com.learnspringboot.demo.security.jwt.JwtUtil;
import com.learnspringboot.demo.security.UserPrincipal;
import com.learnspringboot.demo.security.UserPrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthModuleFilter extends OncePerRequestFilter{
    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserPrincipalDetailService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            UserPrincipal userPrincipal = (UserPrincipal)request.getAttribute(Contants.PRINCIPAL);

            if(userPrincipal != null) {
                ModuleAuthentication customAuthentication = new ModuleAuthentication(
                        userPrincipal,
                        request.getServletPath(),
                        request.getMethod()
                );

                Authentication authResult = authenticationManager.authenticate(customAuthentication);

                if (authResult.isAuthenticated())
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                else
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (AuthenticationException authenticationException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

}