package fisioterapia.auth.filter;

import fisioterapia.auth.SimpleGrantedAuthorityJsonCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fisioterapia.auth.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtValidationFilter.class);

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);
        logger.info("Recibiendo la cabecera Authorization: {}", header);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            logger.info("No se encontró token en la cabecera para la ruta: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        // if (header == null || !header.startsWith(PREFIX_TOKEN)) {
        // logger.warn("No se encontró el token en la cabecera");
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // response.getWriter().write("{\"error\": \"Token is missing or invalid\"}");
        // return;
        // }

        String token = header.replace(PREFIX_TOKEN, "");
        logger.info("Token recibido para validación");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token).getPayload();

            String username = claims.getSubject();
            logger.info("Usuario autenticado: {}", username);

            Object authoritiesClaims = claims.get("authorities");
            logger.info("Claims de autoridades extraídos: {}", authoritiesClaims);

            // Log de claims extraídos
            logger.info("Claims de autoridades: {}", authoritiesClaims);

            Collection<? extends GrantedAuthority> roles = Arrays.asList(new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            logger.info("Roles extraídos: {}", roles);
            logger.info("Usuario autenticado: {}", username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null, roles);
            logger.info("Token de autenticación configurado con roles: {}", roles);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);

        } catch (JwtException e) {
            logger.error("Error al validar el token: {}", e.getMessage());

            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token es inválido!");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
        }
    }
}
