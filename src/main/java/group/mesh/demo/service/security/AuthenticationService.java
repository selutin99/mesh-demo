package group.mesh.demo.service.security;

import group.mesh.demo.exception.business.DataValidationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static java.util.Collections.emptyList;

public class AuthenticationService {

    private static final long EXPIRATION_TIME = 864_000_00;

    private static final String SIGNING_KEY = "signingKey";
    private static final String BEARER_PREFIX = "Bearer";

    private static final String AUTHORIZATION = "Authorization";
    private static final String EXPOSE = "Access-Control-Expose-Headers";

    public static void addJWTToken(HttpServletResponse response, String userId) throws IOException {
        String token = Jwts.builder().setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
        response.addHeader(AUTHORIZATION, BEARER_PREFIX + " " + token);
        response.addHeader(EXPOSE, AUTHORIZATION);

        response.getWriter().write(BEARER_PREFIX + " " + token);
        response.getWriter().flush();
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            try {
                String userId = Jwts.parser()
                        .setSigningKey(SIGNING_KEY)
                        .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                        .getBody()
                        .getSubject();
                if (userId != null) {
                    return new UsernamePasswordAuthenticationToken(userId, null, emptyList());
                }
            } catch (Exception e) {
                throw new DataValidationException("Invalid token");
            }
        }
        return null;
    }
}
