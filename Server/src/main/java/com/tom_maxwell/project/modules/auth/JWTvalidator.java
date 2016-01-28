package com.tom_maxwell.project.modules.auth;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedIOException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 22/01/2016.
 */

/**
 * The JWT validator is responsible for validating JWT
 *
 * @see <a href="https://jwt.io/">jwt.io</a> for more infor on JWT tokens
 */
public class JWTvalidator {

	private static final Logger logger = LoggerFactory.getLogger(JWTvalidator.class);

	private PrivateKey privateKey;
	private PublicKey publicKey;

	@Value("${SLATE.auth.JWT.exp_time}")
	private long exp;
	@Value("${SLATE.auth.JWT.refresh_time}")
	private long refresh;

	@Value("${SLATE.auth.keys.private}")
	private String privateKeyLocation;
	@Value("${SLATE.auth.keys.public}")
	private String publicKeyLocation;

	/**
	 * Called by spring was the values have been injected
	 *
	 * @throws Exception Exception thrown when failure to read keys has occurred
	 */
	@PostConstruct
	public void JWTvalidator() throws Exception{

		try{

			//load private key
			File f = new File(privateKeyLocation);

			FileInputStream fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);
			byte[] keyBytes = new byte[(int)f.length()];
			dis.readFully(keyBytes);
			dis.close();

			PKCS8EncodedKeySpec spec =
					new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			privateKey = kf.generatePrivate(spec);

			//now load public key
			f = new File(publicKeyLocation);

			fis = new FileInputStream(f);
			dis = new DataInputStream(fis);
			keyBytes = new byte[(int)f.length()];
			dis.readFully(keyBytes);
			dis.close();

			X509EncodedKeySpec spec2 =
					new X509EncodedKeySpec(keyBytes);
			kf = KeyFactory.getInstance("RSA");

			publicKey = kf.generatePublic(spec2);

		}catch(Exception e){
			logger.error("Failed to load private and public keys", e);
			throw e;
		}

	}

	/**
	 * Generate a JWT for the given claims
	 *
	 * @param claims The claims for the JWT
	 *
	 * @return the JWT
	 */
	public String generate(Map<String, Object> claims){

		//time calcs
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		long expMillis = nowMillis + exp;
		Date exp = new Date(expMillis);

		//Add default claims
		//time added below for framework limitations
		claims.put("iss", "SLATE");

		String jwt = Jwts.builder()
				.setClaims(claims)
				.setExpiration(exp)
				.setIssuedAt(now)
				.signWith(SignatureAlgorithm.RS512, privateKey)
				.compact();

		return jwt;

	}

	/**
	 * Validate a JWT. If required generate a new one.
	 *
	 * @param jwt the JWT to validate
	 *
	 * @return the JWT
	 *
	 * @throws ExpiredJwtException The JWT has expired
	 * @throws JwtException Other hidden error. Hidden for security
	 */
	public String validate(String jwt, HttpServletRequest httpServletRequest) throws ExpiredJwtException, JwtException{

		//throws exceptions on fail
		Claims claims = Jwts.parser()
				.requireIssuer("SLATE")
				.setSigningKey(publicKey)
				.parseClaimsJws(jwt)
				.getBody();

		//add claims to the request
		httpServletRequest.setAttribute("username", claims.get("username"));
		httpServletRequest.setAttribute("role", claims.get("role"));



		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//if old, replace
		if(claims.getExpiration().getTime() < nowMillis + refresh) {

			//time calcs
			long expMillis = nowMillis + exp;
			Date exp = new Date(expMillis);

			//build new token, with higher expiry
			jwt = Jwts.builder()
					.setClaims(claims)
					.setExpiration(exp)
					.setIssuedAt(now)
					.signWith(SignatureAlgorithm.RS512, privateKey)
					.compact();
		}

		return jwt;
	}
}
