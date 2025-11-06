package ucb.estudo.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    // Construtor vazio obrigatório para JPA
    public Tarefa() {} 
    
    // Construtor parcial (mantido por compatibilidade, mas idealmente usar setters)
    public Tarefa(String idTarefa, String titulo, Prioridade prioridade, Status status, String descricao) {
        this.idTarefa = idTarefa;
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.status = status;
        this.descricao = descricao;
    }
    
    @Id
    @Column(name = "id_tarefa", length = 20)
    private String idTarefa;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; // Objeto Usuario, pois é uma relação ManyToOne

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate dataVencimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    @PrePersist
    public void prePersist(){
        dataCriacao = LocalDateTime.now();
        if (status==null) status = Status.PENDENTE;
        if (prioridade==null) prioridade = Prioridade.MEDIA;
    }

    public enum Prioridade { Baixa, Média, Alta, MEDIA /* fallback */ }
    public enum Status { Pendente, EmAndamento, Concluída, PENDENTE /* fallback */ }

    public String getIdTarefa(){return idTarefa;}
    public void setIdTarefa(String id){this.idTarefa=id;}
    public Usuario getUsuario(){return usuario;}
    
    // CORRIGIDO: Deve aceitar um objeto Usuario para a relação JPA
    public void setUsuario(Usuario usuario){this.usuario=usuario;} 
    
    public String getTitulo(){return titulo;}
    public void setTitulo(String t){this.titulo=t;}
    public String getDescricao(){return descricao;}
    public void setDescricao(String d){this.descricao=d;}
    public Prioridade getPrioridade(){return prioridade;}
    public void setPrioridade(Prioridade p){this.prioridade=p;}
    public Status getStatus(){return status;}
    public void setStatus(Status s){this.status=s;}
    public LocalDate getDataVencimento(){return dataVencimento;}
    public void setDataVencimento(LocalDate d){this.dataVencimento=d;}
    public LocalDateTime getDataCriacao(){return dataCriacao;}
    public LocalDateTime getDataConclusao(){return dataConclusao;}
    public void setDataConclusao(LocalDateTime d){this.dataConclusao=d;}
}