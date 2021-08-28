package com.deofis.tiendaapirest.config;

import com.deofis.tiendaapirest.autenticacion.security.JwtFiltroAutenticacion;
import com.deofis.tiendaapirest.autenticacion.security.RestAuthenticationEntryPoint;
import com.deofis.tiendaapirest.autenticacion.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.deofis.tiendaapirest.autenticacion.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.deofis.tiendaapirest.autenticacion.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.deofis.tiendaapirest.autenticacion.services.oauth2.OAuth2UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final OAuth2UserDetailsService oAuth2UserDetailsService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtFiltroAutenticacion jwtFiltroAutenticacion;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint()).and()
                .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js").permitAll()
                .antMatchers("/api/auth/**", "/oauth2/**").permitAll()
                .antMatchers("/api/**").permitAll()
                /*
                .antMatchers("/api/catalogo/categorias/**", "/api/catalogo/subcategorias/**", "/api/catalogo/buscar", "/api/catalogo/destacados", "/api/catalogo/filtrar/**", "/api/catalogo/marcas", "/api/catalogo/precio-mayor", "/api/catalogo/precio-menor").permitAll()
                .antMatchers(HttpMethod.GET, "/api/catalogo/productos/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/catalogo/productos/consulta").permitAll()
                .antMatchers(HttpMethod.GET, "/api/paises/**", "/api/paises-por-nombre/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/checkout/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/banners").permitAll()
                .antMatchers(HttpMethod.GET, "/api/uploads/**").permitAll()
                 */
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration-security",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated().and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository()).and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*").and()
                    .userInfoEndpoint()
                        .userService(oAuth2UserDetailsService).and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler);

        // Agregar filtro de jwt
        http.addFilterBefore(this.jwtFiltroAutenticacion, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
