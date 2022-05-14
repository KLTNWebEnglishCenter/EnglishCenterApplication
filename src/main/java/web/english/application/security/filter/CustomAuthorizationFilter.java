package web.english.application.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        //Người dùng truy cập vào trang login
        if (request.getServletPath().equals("/login")) {
            try{
                filterChain.doFilter(request, response);
            }catch (Exception exception){
                exception.printStackTrace();
                log.error("login error: {}", exception.getMessage());
                response.sendRedirect("/login/fail");
            }
        // Người dùng truy cập vào trang logout
        }else if(request.getServletPath().equals("/logout")){
            response.sendRedirect("/login");
        }else{
            String jwt=jwtHelper.getJwtFromCookie(request);
            //Nếu đã tồn tại jwt
            if(!jwt.equals("")){
                try {
                    //Lấy user từ server
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

                    response.sendRedirect("/login/expired");
                }
            }else{
                try {
                    log.info("jwt empty");
                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    exception.printStackTrace();
                    log.error("login error: {}", exception.getMessage());
                    response.sendRedirect("/login/fail");
                }
            }
        }


    }
}
