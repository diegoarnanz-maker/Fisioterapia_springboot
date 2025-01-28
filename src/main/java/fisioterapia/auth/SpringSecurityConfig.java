package fisioterapia.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fisioterapia.auth.filter.JwtAuthenticationFilter;
import fisioterapia.auth.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz

                // Rutas públicas GENERALES (sin autenticación)
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/crearPrimerUsuario").permitAll()

                // Rutas de USUARIOS (solo ADMIN puede crear y gestionar usuarios)
                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMON")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/{username}").hasRole("ADMON")
                .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMON")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/{username}").hasRole("ADMON")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{username}").hasRole("ADMON")

                // Rutas de ROLES (solo ADMIN puede crear y gestionar roles)
                .requestMatchers(HttpMethod.GET, "/api/roles").hasRole("ADMON")
                .requestMatchers(HttpMethod.GET, "/api/roles/{id}").hasRole("ADMON")
                .requestMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMON")
                .requestMatchers(HttpMethod.PUT, "/api/roles/{id}").hasRole("ADMON")
                .requestMatchers(HttpMethod.DELETE, "/api/roles/{id}").hasRole("ADMON")

                // Rutas de SERVICIOS (solo ADMIN puede crear, actualizar o eliminar servicios)
                .requestMatchers(HttpMethod.GET, "/api/servicios", "/api/servicios/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/servicios").hasRole("ADMON")
                .requestMatchers(HttpMethod.PUT, "/api/servicios/{id}").hasRole("ADMON")
                .requestMatchers(HttpMethod.DELETE, "/api/servicios/{id}").hasRole("ADMON")

                // Rutas de AGENDAS
                // Rutas publicas (Clientes pueden ver las agendas de los fisios para ver
                // disponibilidad)
                .requestMatchers(HttpMethod.GET, "/api/agendas/**").permitAll()
                // Rutas protegidas por role (fisioterapeutas pueden gestionar sus propias
                // agendas, administradores pueden gestionar todas)
                .requestMatchers(HttpMethod.POST, "/api/agendas/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/agendas/{id}").hasAnyRole("ADMON", "FISIOTERAPEUTA")
                .requestMatchers(HttpMethod.DELETE, "/api/agendas/{id}").hasAnyRole("ADMON", "FISIOTERAPEUTA")

                .anyRequest().authenticated())

                // .cors(cors -> cors.configurationSource(configurationSource()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())

                // Security maneja el la sesion a diferencia que con thyemeleaf
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // @Bean
    // CorsConfigurationSource configurationSource() {
    // CorsConfiguration config = new CorsConfiguration();
    // config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    // config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
    // config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    // config.setAllowCredentials(true);

    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", config);
    // return source;
    // }

    // @Bean
    // FilterRegistrationBean<CorsFilter> corsFilter() {
    // FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
    // new CorsFilter(this.configurationSource()));
    // corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    // return corsBean;
    // }

}
