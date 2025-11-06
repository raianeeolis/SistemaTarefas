package ucb.estudo.services;

import org.springframework.stereotype.Service;
import ucb.estudo.model.Tarefa;
import ucb.estudo.repositories.TarefaRepository;
import ucb.estudo.model.Usuario;
import ucb.estudo.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    private final TarefaRepository tarefaRepo;
    private final UsuarioRepository usuarioRepo;
    private final IdGeneratorService idGenerator; // Necessário para gerar IDs customizados

    // Assumindo que você terá um IdGeneratorService para simular a função SQL
    public TarefaService(TarefaRepository tarefaRepo, UsuarioRepository usuarioRepo) {
        this.tarefaRepo = tarefaRepo;
        this.usuarioRepo = usuarioRepo;
        // Simulando a geração de ID, mas em um sistema real, 
        // o JPA faria isso via Dialect ou a função SQL seria chamada diretamente.
        this.idGenerator = new IdGeneratorService(); 
    }
    
    // Serviço Simples de Geração de ID (Substituindo a Function SQL por simulação Java)
    // Em um ambiente Spring/JPA real, seria melhor usar uma Sequence ou chamar a Function.
    private static class IdGeneratorService {
        private int counter = 0;
        public String generateId(String prefix) {
            counter++;
            return String.format("%s-%05d", prefix, counter);
        }
    }

    // LISTAR: Retorna todas as tarefas de um usuário
    // Assumindo que o ID do usuário é String ou Long. Usando String aqui para manter a compatibilidade com o Controller
    // Se o ID do usuário for Long no banco, mude para: public List<Tarefa> findAllByUserId(Long userId)
    public List<Tarefa> findAllByUserId(Long userId) {
        return tarefaRepo.findByUsuarioIdUsuario(userId);
    }

    // BUSCAR: Retorna uma tarefa específica de um usuário. CRÍTICO para segurança.
    public Optional<Tarefa> findByIdAndUserId(String tarefaId, Long userId) {
        // Observação: Assumindo que o método no Repositório aceita Long
        return tarefaRepo.findByIdTarefaAndUsuarioIdUsuario(tarefaId, userId); 
    }

    // CRIAR: Cria uma nova tarefa, associa ao usuário e gera ID customizado
    @Transactional
    public Tarefa createTarefa(Tarefa tarefa, Long userId) {
        Optional<Usuario> oUser = usuarioRepo.findByIdUsuario(userId);
        if (oUser.isEmpty()) {
             throw new IllegalArgumentException("Usuário não encontrado.");
        }
        
        tarefa.setIdTarefa(idGenerator.generateId("TAR")); // Gera ID (TAR-0000X)
        // CORRIGIDO: Chama o método setUsuario com o objeto Usuario encontrado
        tarefa.setUsuario(oUser.get()); 
        
        // Define status e datas padrão
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(Tarefa.Status.PENDENTE); // Usando PENDENTE do Enum
        }
        
        return tarefaRepo.save(tarefa);
    }

    // ATUALIZAR: Atualiza uma tarefa, verificando se pertence ao usuário
    @Transactional
    public Optional<Tarefa> updateTarefa(String tarefaId, Tarefa tarefaDetails, Long userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);

        if (oTarefa.isPresent()) {
            Tarefa existingTarefa = oTarefa.get();
            // Atualiza apenas campos permitidos
            existingTarefa.setTitulo(tarefaDetails.getTitulo());
            existingTarefa.setDescricao(tarefaDetails.getDescricao());
            existingTarefa.setPrioridade(tarefaDetails.getPrioridade());
            existingTarefa.setStatus(tarefaDetails.getStatus());
            existingTarefa.setDataVencimento(tarefaDetails.getDataVencimento());
            
            // A trigger MySQL trg_tarefa_concluida cuidará de data_conclusao, 
            // mas salvamos o objeto atualizado
            return Optional.of(tarefaRepo.save(existingTarefa));
        }
        return Optional.empty(); // Não encontrado ou não pertence ao usuário
    }

    // EXCLUIR: Exclui uma tarefa, verificando se pertence ao usuário
    @Transactional
    public boolean deleteTarefa(String tarefaId, Long userId) {
        Optional<Tarefa> oTarefa = findByIdAndUserId(tarefaId, userId);

        if (oTarefa.isPresent()) {
            tarefaRepo.delete(oTarefa.get());
            return true;
        }
        return false;
    }
}