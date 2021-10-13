package com.aaron.vocabularyapi.security;

@Deprecated
// @Slf4j
// @AllArgsConstructor
// @Component
public class JWTAuthorizationFilter // extends OncePerRequestFilter
{
//    private static final String BEARER_HEADER = "Bearer ";

//    private JwtUtils jwtUtil;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
//    {
//        log.info("doFilterInternal. Start");
//
//        try
//        {
//            String token = getJwtToken(request);
//            if(isNotBlank(token))
//            {
//                validatedJwtToken(token);
//            }
//
//            filterChain.doFilter(request, response);
//        }
//        catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e)
//        {
//            log.error("doFilterInternal. Error={}", e.getMessage());
//            response.setStatus(SC_FORBIDDEN);
//            ((HttpServletResponse) response).sendError(SC_FORBIDDEN, e.getMessage());
//            return;
//        }
//
//    }
//
//    private String getJwtToken(HttpServletRequest request)
//    {
//        String authHeader = request.getHeader(AUTHORIZATION);
//
//        String token = "";
//        if(isNotBlank(authHeader) && authHeader.startsWith(BEARER_HEADER))
//        {
//            token = authHeader.substring(BEARER_HEADER.length());
//        }
//
//        return token;
//    }
//
//    private void validatedJwtToken(String authToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException
//    {
//        Claims claims = jwtUtil.parseJwtToken(authToken);
//        if(jwtUtil.isTokenNotExpired(claims))
//        {
//            String username = jwtUtil.getUsernameFromToken(claims);
//            List<String> authorities = jwtUtil.getAuthoritiesFromToken(claims);
//            log.info("validatedJwtToken. Token valid. username={} authorities={}", username, authorities);
//            if(isNotBlank(username) && isNotEmpty(authorities))
//            {
//                // can add validation on username in database, and also get authorities from there
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
//                        authorities.stream().map(SimpleGrantedAuthority::new).collect(toList()));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//        else
//        {
//            log.info("validatedJwtToken. Token is expired. token={}", authToken);
//            SecurityContextHolder.clearContext();
//        }
//    }
}
