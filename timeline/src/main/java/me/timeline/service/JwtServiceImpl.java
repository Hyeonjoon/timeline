package me.timeline.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.timeline.dto.JwtInputDTO;
import me.timeline.exception.UnauthorizedException;

@Service
public class JwtServiceImpl implements JwtService{
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Override
	public String JwtCreate(JwtInputDTO jwtInputDTO){
		String jwt = Jwts.builder()
						 .setHeaderParam("typ", "JWT")
						 .setSubject(jwtInputDTO.getIdString())
						 .signWith(SignatureAlgorithm.HS512, secret)
						 .compact();
		return jwt;
	}
	
	@Override
	public boolean JwtIsUsable(String jwt) {
		try {
			Jws<Claims> claims = Jwts.parser()
					  .setSigningKey(secret)
					  .parseClaimsJws(jwt);
			return true;
		} catch (Exception e) {
			throw new UnauthorizedException();
		}
	}
	
	public int JwtGetUserId(String jwt) {
		return Integer.parseInt(Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject());
	}
}
