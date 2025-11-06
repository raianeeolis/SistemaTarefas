/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.estudo.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    /**
     * Busca um usuário pelo seu email. Usado principalmente para autenticação (login).
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca um usuário pelo seu ID customizado (id_usuario).
     * Essencial para serviços como TarefaService para associar uma tarefa a um usuário logado.
     */
    Optional<Usuario> findByIdUsuario(Long idUsuario);
}
