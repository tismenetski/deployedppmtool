package com.tismenetski.ppmtool.security;

import com.tismenetski.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tismenetski.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.tismenetski.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //Generate the token

    public String generateToken(Authentication  authentication)
    {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis()); // Current System Date
        Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME); // current date + predefined token expiration time
        String userId = Long.toString(user.getId()); //get user id from user -we need to cast it from long to string since token is a string
        Map<String,Object> claims = new HashMap<>(); //create a map to set the claims
        claims.put("id",Long.toString(user.getId()));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullName());
        //Comment : If you should consider using roles then it should be set here too

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .compact();
    }
    //Validate the token
    public boolean validateToken(String token)
    {
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT Toen");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT Token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT Claims String is empty");
        }
        return false; //Invalid token
    }
    //Get user Id from token
    public Long getUserIdFromJWT(String token)
    {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}
