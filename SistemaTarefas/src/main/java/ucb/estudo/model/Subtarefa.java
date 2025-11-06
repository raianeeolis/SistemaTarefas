/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subtarefas")
public class Subtarefa {
    @Id
    @Column(name = "id_subtarefa", length = 20)
    private String idSubtarefa;

    @ManyToOne
    @JoinColumn(name = "id_tarefa")
    private Tarefa tarefa;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public enum Status { Pendente, Concluída }

    @PrePersist
    public void prePersist(){ setDataCriacao(LocalDateTime.now()); if(getStatus()==null) setStatus(Status.Pendente); }

    public String getIdSubtarefa() {
        return idSubtarefa;
    }

    public void setIdSubtarefa(String idSubtarefa) {
        this.idSubtarefa = idSubtarefa;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
}
