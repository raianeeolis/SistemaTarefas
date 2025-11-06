/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ucb.estudo.entities.SessionDoc;
import ucb.estudo.repositories.SessionRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Filtro customizado para validar o token de sessão armazenado no MongoDB.
 * Este filtro é executado uma vez por requisição.
 */
@Component
public class SessionTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SessionRepository sessionRepository; // Repositório MongoDB para SessionDoc

    // Prefixo esperado no cabeçalho Authorization
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extrair o token do cabeçalho
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String sessionToken = authorizationHeader.substring(BEARER_PREFIX.length());

            // 2. Tentar validar o token
            if (isValidTokenFormat(sessionToken)) {
                Optional<SessionDoc> sessionOpt = sessionRepository.findById(sessionToken);

                if (sessionOpt.isPresent()) {
                    // 3. Sessão válida: Autenticar no Spring Security
                    SessionDoc session = sessionOpt.get();
                    
                    // As autoridades (papéis) são baseadas no campo 'grupo' da sessão
                    List<GrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + session.getGrupo().toUpperCase())
                    );

                    // Cria o objeto principal (usuário)
                    User principal = new User(
                            session.getUserEmail(),
                            "", // A senha é vazia, pois já estamos autenticados pelo token
                            authorities
                    );
                    
                    // Cria o token de autenticação
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            null, // Credenciais nulas, pois o token é a credencial
                            authorities
                    );

                    // 4. Define a autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    logger.info("Sessão válida encontrada para o usuário: " + session.getUserEmail());

                } else {
                    logger.warn("Token de sessão inválido ou expirado: " + sessionToken);
                }
            }
        }
        
        // Continua a cadeia de filtros (ou para na próxima etapa de segurança)
        filterChain.doFilter(request, response);
    }

    /**
     * Verifica se a string do token tem o formato de um UUID (evita consultas desnecessárias ao BD).
     */
    private boolean isValidTokenFormat(String token) {
        try {
            // Tenta criar um UUID a partir da string
            UUID.fromString(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}