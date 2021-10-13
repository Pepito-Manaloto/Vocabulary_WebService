package com.aaron.vocabularyapi.security;

@Deprecated
//@AllArgsConstructor
//@Configuration
//@EnableWebSecurity
public class WebSecurityConfigOld // extends WebSecurityConfigurerAdapter
{
//    private JWTAuthorizationFilter jwtAuthorizationFilter;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception
//    {
//        http.csrf().disable()
//            .formLogin().disable()
//            .httpBasic().disable()
//            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
//            .authorizeRequests()
//            .antMatchers(HttpMethod.POST, "/token").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
}
