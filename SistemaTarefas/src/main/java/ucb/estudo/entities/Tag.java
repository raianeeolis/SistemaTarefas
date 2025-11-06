/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name="id_tag", length=20)
    private String idTag;
    @Column(nullable=false, unique=true)
    private String nome;

    public String getIdTag(){return idTag;}
    public void setIdTag(String id){this.idTag=id;}
    public String getNome(){return nome;}
    public void setNome(String n){this.nome=n;}
}
