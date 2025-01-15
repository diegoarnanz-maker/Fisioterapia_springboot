package fisioterapia.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import fisioterapia.auth.filter.JwtAuthenticationFilter;
import fisioterapia.auth.filter.JwtValidationFilter;

import java.util.Arrays;

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
        return new BCryptPasswordEncoder(); // Usamos BCrypt para las contraseñas
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
        // Rutas públicas (sin autenticación)
        .requestMatchers("/api/auth/crearPrimerUsuario").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/servicios", "/api/servicios/{id}").permitAll()  // Servicios disponibles
        .requestMatchers(HttpMethod.GET, "/api/usuarios/{username}").permitAll()  // Ver usuario por username (acceso público)

        // Rutas de usuarios (solo ADMIN puede crear y gestionar usuarios)
        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMON")  // Crear usuario (solo ADMIN)
        .requestMatchers(HttpMethod.PUT, "/api/usuarios/{username}").hasRole("ADMON")  // Actualizar usuario (solo ADMIN)
        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{username}").hasRole("ADMON")  // Eliminar usuario (solo ADMIN)

        // Rutas de citas (CLIENTE puede ver y crear sus propias citas, FISIOTERAPEUTA y ADMIN pueden ver todas las citas)
        .requestMatchers(HttpMethod.GET, "/api/citas", "/api/citas/{id}").authenticated()  // Ver todas las citas
        .requestMatchers(HttpMethod.POST, "/api/citas").hasRole("CLIENTE")  // Crear cita (solo CLIENTE)
        .requestMatchers(HttpMethod.PUT, "/api/citas/{id}").hasRole("CLIENTE")  // Actualizar cita (solo CLIENTE)
        .requestMatchers(HttpMethod.DELETE, "/api/citas/{id}").hasRole("CLIENTE")  // Eliminar cita (solo CLIENTE)

        // Rutas de agenda (solo los fisioterapeutas pueden gestionar agendas)
        .requestMatchers(HttpMethod.GET, "/api/agenda", "/api/agenda/{id}").hasRole("FISIOTERAPEUTA")  // Ver agenda (solo FISIOTERAPEUTA)
        .requestMatchers(HttpMethod.POST, "/api/agenda").hasRole("FISIOTERAPEUTA")  // Crear agenda (solo FISIOTERAPEUTA)
        .requestMatchers(HttpMethod.PUT, "/api/agenda/{id}").hasRole("FISIOTERAPEUTA")  // Actualizar agenda (solo FISIOTERAPEUTA)
        .requestMatchers(HttpMethod.DELETE, "/api/agenda/{id}").hasRole("FISIOTERAPEUTA")  // Eliminar agenda (solo FISIOTERAPEUTA)

        // Rutas para gestionar servicios (solo ADMIN puede crear, actualizar o eliminar servicios)
        .requestMatchers(HttpMethod.POST, "/api/servicios").hasRole("ADMON")  // Crear servicio (solo ADMIN)
        .requestMatchers(HttpMethod.PUT, "/api/servicios/{id}").hasRole("ADMON")  // Actualizar servicio (solo ADMIN)
        .requestMatchers(HttpMethod.DELETE, "/api/servicios/{id}").hasRole("ADMON")  // Eliminar servicio (solo ADMIN)

        // Rutas para gestión de usuarios, solo ADMIN puede gestionarlos
        .anyRequest().authenticated())  // Cualquier otra ruta requiere autenticación

        .cors(cors -> cors.configurationSource(configurationSource()))
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
                new CorsFilter(this.configurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
