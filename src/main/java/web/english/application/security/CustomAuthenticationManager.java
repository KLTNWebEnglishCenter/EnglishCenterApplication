package web.english.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import web.english.application.dao.UserDAO;
import web.english.application.entity.user.Users;
import web.english.application.security.entity.CustomUserDetails;
import web.english.application.utils.JwtHelper;

import java.util.ArrayList;
import java.util.List;

//@Component
@Slf4j
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserDAO userDAO;

    private JwtHelper jwtHelper=new JwtHelper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        String jwt = userDAO.login(username,password);
        String token =jwtHelper.createToken(jwt);

        Users users = userDAO.getUserFromToken(token);

        if (users == null) {
            throw new BadCredentialsException("1000");
        }
        if (users.isEnable()==false) {
            throw new DisabledException("1001");
        }

        UserDetails userDetails=new CustomUserDetails(users);

//        List<GrantedAuthority> grantedAuths = new ArrayList<>();
//        grantedAuths.add(new SimpleGrantedAuthority(users.getRole()));
//        log.info(userDetails.toString());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());

//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return authenticationToken;
    }
}
