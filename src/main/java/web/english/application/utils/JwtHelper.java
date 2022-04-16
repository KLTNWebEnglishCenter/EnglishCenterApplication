package web.english.application.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class JwtHelper {

    /**
     *
     * @author VQKHANH
     * @param httpServletRequest
     * @return
     */
    public String getJwtFromCookie(HttpServletRequest httpServletRequest){
        String jwt="";
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if(cookie.getName().equals("access_token")){
                jwt = cookie.getValue();
            }
        }
        return jwt;
    }

    /**
     *
     * @author VQKHANH
     * @param jwt
     * @return
     */
    public String createToken(String jwt){
        return "Bearer "+jwt;
    }
}
