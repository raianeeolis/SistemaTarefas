/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.estudo.entities.Tarefa;
import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, String> {
    // Buscar todas as tarefas de um usuário específico
    List<Tarefa> findByUsuarioIdUsuario(Long idUsuario);
    
    // Buscar uma tarefa específica de um usuário específico
    Optional<Tarefa> findByIdTarefaAndUsuarioIdUsuario(String idTarefa, Long idUsuario);
}
