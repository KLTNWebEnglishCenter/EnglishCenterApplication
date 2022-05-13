package web.english.application.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;
import web.english.application.utils.RoleType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    private UserDAO userDAO;

    @Autowired
    public CustomAuthorizationFilter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private JwtHelper jwtHelper=new JwtHelper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            try{
                filterChain.doFilter(request, response);
            }catch (Exception exception){
                log.error("login error: {}", exception.getMessage());
                exception.printStackTrace();
                String error = "fail";
                Cookie cookie_error=new Cookie("error",error);
                cookie_error.setMaxAge(2);
                response.addCookie(cookie_error);

                response.sendRedirect("/login");
            }

        }else if(request.getServletPath().equals("/logout")){
            log.info("logout && delete cookie");
            Cookie cookie=new Cookie("access_token",null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect("/login");
        }
        else{
            String jwt=jwtHelper.getJwtFromCookie(request);
            String error="";
            if(!jwt.equals("")){
                try {
                    Users users=userDAO.getUserFromToken(jwtHelper.createToken(jwt));

                    if(users!=null){
                        UserDetails userDetails=new CustomUserDetails(users);
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    exception.printStackTrace();
                    log.error("login error: {}", exception.getMessage());
                    Cookie cookie=new Cookie("access_token",null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);

                    error = "expired";

                    Cookie cookie_error=new Cookie("error",error);
                    cookie_error.setMaxAge(2);
                    response.addCookie(cookie_error);

                    response.sendRedirect("/login");

                }
            }else{
                try {
                    log.info("jwt empty - 1");
                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    log.error("login error: {}", exception.getMessage());
                    exception.printStackTrace();
                    error = "fail";
                    Cookie cookie_error=new Cookie("error",error);
                    cookie_error.setMaxAge(2);
                    response.addCookie(cookie_error);

                    response.sendRedirect("/login");
                }
            }
        }


    }
}
