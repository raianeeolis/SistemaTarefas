/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.estudo.model.Subtarefa;
import java.util.List;

public interface SubtarefaRepository extends JpaRepository<Subtarefa, String> {
    List<Subtarefa> findByTarefa_IdTarefa(String idTarefa);    
}
