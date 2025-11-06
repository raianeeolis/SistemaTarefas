// js/app.js

// === Dados Mock (Simulação de Backend) ===
// Em um sistema real, estas operações seriam feitas via requisições HTTP (fetch/axios)
// para um servidor que se conecta ao seu banco de dados MySQL.

let currentUser = null; // Armazena o usuário atualmente logado

let mockUsers = [
    { id_usuario: 'USR-00001', id_grupo: 'GRP-00001', nome: 'Admin', email: 'admin@todo.com', senha_hash: '1234', ativo: true },
    { id_usuario: 'USR-00002', id_grupo: 'GRP-00002', nome: 'Maria', email: 'maria@todo.com', senha_hash: '1234', ativo: true }
];

let mockTasks = [
    { id_tarefa: 'TAR-00001', id_usuario: 'USR-00002', titulo: 'Estudar Banco de Dados', descricao: 'Ler sobre procedures e triggers', prioridade: 'Alta', status: 'Pendente', data_criacao: '2023-10-26', data_vencimento: '2023-11-05', data_conclusao: null },
    { id_tarefa: 'TAR-00002', id_usuario: 'USR-00002', titulo: 'Comprar Leite', descricao: 'Leite integral', prioridade: 'Baixa', status: 'Concluída', data_criacao: '2023-10-20', data_vencimento: null, data_conclusao: '2023-10-21' },
    { id_tarefa: 'TAR-00003', id_usuario: 'USR-00001', titulo: 'Revisar Relatório Mensal', descricao: 'Revisar dados de vendas do último mês', prioridade: 'Alta', status: 'Em Andamento', data_criacao: '2023-10-25', data_vencimento: '2023-10-31', data_conclusao: null }
];

// Função auxiliar para simular geração de ID único
function gerarIdUnico(prefixo, lista) {
    const ultimoNumero = lista.reduce((max, item) => {
        const idNum = parseInt(item.id_tarefa ? item.id_tarefa.split('-')[1] : item.id_usuario.split('-')[1]);
        return idNum > max ? idNum : max;
    }, 0);
    return `${prefixo}-${String(ultimoNumero + 1).padStart(5, '0')}`;
}

// === Elementos HTML ===
const loginSection = document.getElementById('login-section');
const dashboardSection = document.getElementById('dashboard-section');
const loginForm = document.getElementById('login-form');
const loginError = document.getElementById('login-error');
const mainNav = document.getElementById('main-nav');
const welcomeMessage = document.getElementById('welcome-message');
const btnLogout = document.getElementById('btn-logout');
const taskListContainer = document.getElementById('task-list');
const btnAddTask = document.getElementById('btn-add-task');
const taskModal = document.getElementById('task-modal');
const taskModalTitle = document.getElementById('task-modal-title');
const taskForm = document.getElementById('task-form');
const taskModalCloseButton = document.querySelector('#task-modal .close-button');


// === Funções de Exibição ===
function showSection(section) {
    loginSection.style.display = 'none';
    dashboardSection.style.display = 'none';

    if (section === 'login') {
        loginSection.style.display = 'block';
        mainNav.style.display = 'none';
    } else if (section === 'dashboard') {
        dashboardSection.style.display = 'block';
        mainNav.style.display = 'flex';
        welcomeMessage.textContent = `Olá, ${currentUser.nome}!`;
        loadTasks();
    }
}

// === Lógica de Autenticação ===
async function handleLogin(event) {
    event.preventDefault(); // Impede o recarregamento da página

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    loginError.textContent = ''; // Limpa mensagens de erro anteriores

    // Simula a chamada ao backend para autenticação
    const userFound = mockUsers.find(u => u.email === email && u.senha_hash === password);

    if (userFound) {
        currentUser = { ...userFound }; // Copia os dados do usuário
        localStorage.setItem('currentUser', JSON.stringify(currentUser)); // Armazena no localStorage
        showSection('dashboard');
    } else {
        loginError.textContent = 'E-mail ou senha inválidos.';
    }
}

function handleLogout() {
    currentUser = null;
    localStorage.removeItem('currentUser');
    showSection('login');
    loginForm.reset(); // Limpa o formulário de login
}

function checkAuth() {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
        currentUser = JSON.parse(storedUser);
        showSection('dashboard');
    } else {
        showSection('login');
    }
}

// === Lógica de Tarefas ===
function loadTasks() {
    if (!currentUser) return; // Garante que há um usuário logado

    taskListContainer.innerHTML = 'Carregando tarefas...';

    // Filtra as tarefas pelo id_usuario do usuário logado
    const userTasks = mockTasks.filter(task => task.id_usuario === currentUser.id_usuario);

    renderTasks(userTasks);
}

function renderTasks(tasksToRender) {
    taskListContainer.innerHTML = ''; // Limpa a lista existente

    if (tasksToRender.length === 0) {
        taskListContainer.innerHTML = '<p>Nenhuma tarefa encontrada.</p>';
        return;
    }

    tasksToRender.forEach(task => {
        const taskItem = document.createElement('div');
        taskItem.classList.add('task-item');
        taskItem.innerHTML = `
            <div>
                <h4>${task.titulo}</h4>
                <p>Descrição: ${task.descricao || 'N/A'}</p>
                <p>Prioridade: ${task.prioridade}</p>
                <p>Status: ${task.status}</p>
                <p>Vencimento: ${task.data_vencimento || 'N/A'}</p>
                ${task.data_conclusao ? `<p>Concluída em: ${task.data_conclusao}</p>` : ''}
            </div>
            <div class="task-actions">
                <button data-id="${task.id_tarefa}" class="edit-button">Editar</button>
                <button data-id="${task.id_tarefa}" class="delete-button">Excluir</button>
            </div>
        `;
        taskListContainer.appendChild(taskItem);
    });

    // Adiciona event listeners para os botões de editar e excluir
    taskListContainer.querySelectorAll('.edit-button').forEach(button => {
        button.addEventListener('click', (event) => openEditTaskModal(event.target.dataset.id));
    });
    taskListContainer.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', (event) => deleteTask(event.target.dataset.id));
    });
}

function openCreateTaskModal() {
    taskModalTitle.textContent = 'Adicionar Nova Tarefa';
    taskForm.reset(); // Limpa o formulário
    document.getElementById('task-id').value = ''; // Garante que não há ID para criação
    taskModal.style.display = 'block';
}

function openEditTaskModal(taskId) {
    taskModalTitle.textContent = 'Editar Tarefa';
    taskForm.reset();

    const task = mockTasks.find(t => t.id_tarefa === taskId);
    if (task) {
        document.getElementById('task-id').value = task.id_tarefa;
        document.getElementById('task-title').value = task.titulo;
        document.getElementById('task-description').value = task.descricao;
        document.getElementById('task-priority').value = task.prioridade;
        document.getElementById('task-status').value = task.status;
        document.getElementById('task-due-date').value = task.data_vencimento || '';
        taskModal.style.display = 'block';
    } else {
        alert('Tarefa não encontrada.');
    }
}

function closeTaskModal() {
    taskModal.style.display = 'none';
}

function handleTaskFormSubmit(event) {
    event.preventDefault();
    const taskId = document.getElementById('task-id').value;

    if (!currentUser) {
        alert('Usuário não autenticado.');
        return;
    }

    const taskData = {
        id_usuario: currentUser.id_usuario,
        titulo: document.getElementById('task-title').value,
        descricao: document.getElementById('task-description').value,
        prioridade: document.getElementById('task-priority').value,
        status: document.getElementById('task-status').value,
        data_vencimento: document.getElementById('task-due-date').value || null
    };

    if (taskId) {
        // Editar tarefa existente
        const index = mockTasks.findIndex(t => t.id_tarefa === taskId);
        if (index > -1) {
            mockTasks[index] = { ...mockTasks[index], ...taskData };
            // Simula o trigger de data_conclusao
            if (taskData.status === 'Concluída' && !mockTasks[index].data_conclusao) {
                mockTasks[index].data_conclusao = new Date().toISOString().split('T')[0];
            }
            alert('Tarefa atualizada com sucesso!');
        } else {
            alert('Erro: Tarefa não encontrada para atualização.');
        }
    } else {
        // Criar nova tarefa
        const newId = gerarIdUnico('TAR', mockTasks);
        const newTask = {
            id_tarefa: newId,
            data_criacao: new Date().toISOString().split('T')[0],
            ...taskData
        };
        mockTasks.push(newTask);
        alert('Tarefa criada com sucesso!');
    }
    closeTaskModal();
    loadTasks(); // Recarrega a lista de tarefas
}

function deleteTask(taskId) {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
        mockTasks = mockTasks.filter(task => task.id_tarefa !== taskId);
        alert('Tarefa excluída com sucesso!');
        loadTasks(); // Recarrega a lista de tarefas
    }
}

// === Event Listeners ===
document.addEventListener('DOMContentLoaded', checkAuth); // Verifica autenticação ao carregar a página
loginForm.addEventListener('submit', handleLogin);
btnLogout.addEventListener('click', handleLogout);
btnAddTask.addEventListener('click', openCreateTaskModal);
taskForm.addEventListener('submit', handleTaskFormSubmit);
taskModalCloseButton.addEventListener('click', closeTaskModal);

// Fecha o modal ao clicar fora dele
window.addEventListener('click', (event) => {
    if (event.target == taskModal) {
        closeTaskModal();
    }
});