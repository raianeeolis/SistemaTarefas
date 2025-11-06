/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucb.estudo.model.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {
    Tag findByNome(String nome);
}
