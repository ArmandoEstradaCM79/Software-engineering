package mx.ipn.escom.loginsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/register", "/login").permitAll() // Permite acceso público a estas rutas
                .requestMatchers("/admin/**").hasRole("ADMIN") // Solo los ADMIN pueden acceder a /admin
                .requestMatchers("/homeUser").hasRole("USER") // Solo los USER pueden acceder a /homeUser
                .requestMatchers("/homeAdmin").hasRole("ADMIN") // Solo los ADMIN pueden acceder a /homeAdmin
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            )
            .formLogin(form -> form
                .loginPage("/login") // Página de inicio de sesión personalizada
                .defaultSuccessUrl("/home", true) // Redirige después del login exitoso
                .failureUrl("/login?error=true") // Redirige en caso de error
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login") // Redirige a /login después del cierre de sesión
                .permitAll()
            );
    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa BCrypt para encriptar contraseñas
    }
}