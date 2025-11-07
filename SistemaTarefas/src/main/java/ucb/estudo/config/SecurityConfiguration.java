package ucb.estudo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Habilita o FORM LOGIN padrão e garante que a página de login seja acessível.
            .formLogin(form -> form.permitAll())
            
            // 2. Define as regras de autorização.
            .authorizeHttpRequests(auth -> auth
                // Permite acesso irrestrito APENAS a recursos estáticos (CSS, JS, Imagens, etc.).
                // O caminho raiz (/) NÃO está mais aqui e, portanto, será protegido.
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                
                // Protege TODAS as outras requisições, exigindo login.
                // Isso inclui a página inicial (/) e todos os endpoints da API.
                .anyRequest().authenticated()
            )
            
            // 3. Desabilita o CSRF (mantendo como estava).
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}