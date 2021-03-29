package codetao.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class TokenAuthenticationService {
    static final long EXPIRATIONTIME = 1000 * 60 * 60 * 24 * 1; //1 days
    static final String SECRET = "security-jwt";
    static final String HEADER_STRING = "Authorization";
    static final String TOKEN_PREFIX = "Bearer";

    public static void addAuthentication(HttpServletResponse res, String username){
        String jwtStr = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwtStr);
    }

    public static String getAuthentication(HttpServletRequest req){
        String token = req.getHeader(HEADER_STRING);
        if(token != null){
            // parse the token
            String username = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
            return username;
        }
        return null;
    }
}
