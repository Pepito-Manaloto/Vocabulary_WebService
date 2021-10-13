package com.aaron.vocabularyapi.util;

import static java.util.stream.Collectors.toList;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aaron.vocabularyapi.config.VocabularyApiProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtUtils
{
    private Key key;
    private JwtParser jwtParser;
    private VocabularyApiProperties properties;

    public JwtUtils(VocabularyApiProperties properties)
    {
        this.properties = properties;
    }

    @PostConstruct
    public void init()
    {
        this.key = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        log.info("init. secret={}", properties.getJwtSecret());
    }

    public String createToken(@NotBlank UserDetails user)
    {
        Instant now = Instant.now();
        Date expiration = Date.from(now.plus(Duration.ofMinutes(properties.getJwtExpirationMinutes())));
        Date issuedAt = Date.from(now);
        // Can be replaced with user.getAuthorities() if it was retrieved from database
        List<String> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER")
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList());

        log.info("createJwt. username={} expiration={} issuedAt={}",
                user.getUsername(), expiration, issuedAt);
        return Jwts.builder()
                .claim("authorities", grantedAuthorities)
                .setSubject(user.getUsername())
                .setIssuer(properties.getAppName())
                .setExpiration(expiration)
                .setIssuedAt(issuedAt)
                .signWith(key)
                .compact();
    }

    public Claims parseJwtToken(@NotBlank String token)
    {
        final Claims claims = jwtParser
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public <T> T getPropertyFromClaims(@NotNull Claims claims, @NotNull Function<Claims, T> claimsResolver)
    {
        return claimsResolver.apply(claims);
    }

    public List<String> getAuthoritiesFromToken(@NotNull Claims claims)
    {
        log.info("getAuthoritiesFromToken.");

        @SuppressWarnings("unchecked")
        Function<Claims, List<String>> getAuthoritiesFromClaim = c -> (List<String>) c.get("authorities");

        return getPropertyFromClaims(claims, getAuthoritiesFromClaim);
    }

    public String getUsernameFromToken(@NotNull Claims claims)
    {
        log.info("getUsernameFromToken.");

        return getPropertyFromClaims(claims, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(@NotNull Claims claims)
    {
        log.info("getExpirationDateFromToken.");

        return getPropertyFromClaims(claims, Claims::getExpiration);
    }

    public boolean isTokenNotExpired(@NotNull Claims claims)
    {
        final Date expiration = getExpirationDateFromToken(claims);
        // Expiry check already done in .parseClaimsJws(), can also add subject/username checking in db here to be more useful
        boolean isNotExpired = expiration.after(new Date());

        log.info("isTokenValid. expiration={} isNotExpired={}", expiration, isNotExpired);

        return isNotExpired;
    }
}