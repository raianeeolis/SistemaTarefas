/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grupos_usuarios")
public class GrupoUsuarios implements Serializable {
    @Id
    @Column(name = "id_grupo", length = 20)
    private String idGrupo;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    public String getIdGrupo() { return idGrupo; }
    public void setIdGrupo(String idGrupo) { this.idGrupo = idGrupo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
