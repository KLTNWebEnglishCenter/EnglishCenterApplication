package web.english.application.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.RoleType;
import web.english.application.utils.UsersType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private JwtHelper jwtHelper=new JwtHelper();

    @Autowired
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            log.info("Username is: {}", username);
            log.info("Password is: {}", password);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
//        }catch (Exception exception){
//            exception.printStackTrace();
//            throw new InternalAuthenticationServiceException(exception.getMessage());
//        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String access_token =authentication.getCredentials().toString();
        Cookie cookie=new Cookie("access_token",access_token);
        response.addCookie(cookie);

//        log.info("SecurityContextHolder:"+SecurityContextHolder.getContext().getAuthentication().toString());

        log.info("Auth:"+user.getUsers().getRole());
        if(user.getUsers().getRole().equals(RoleType.STUDENT)){
            response.sendRedirect("/home");
        }else if(user.getUsers().getRole().equals(RoleType.TEACHER)){
            response.sendRedirect("/admin/schedule");
        }else if(user.getUsers().getRole().equals(RoleType.EMPLOYEE)){
            response.sendRedirect("/admin/teacher");
        }else if(user.getUsers().getRole().equals(RoleType.ADMIN)){
            response.sendRedirect("/admin/employee");
        }else{
            response.sendRedirect("/home");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        failed.printStackTrace();
        log.error("login error: {}", failed.getMessage());

        response.sendRedirect("/login/disable");
    }
}
