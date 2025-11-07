// ===============================
// app.js — Sistema de Tarefas (Front-end)
// ===============================

// === URLs da API ===
const API_BASE_URL = "http://localhost:8082/api/tarefas"; // altere se necessário
const LOGIN_URL = "http://localhost:8082/api/login";       // exemplo — ajuste conforme seu endpoint real

// === Elementos do DOM ===
const loginSection = document.getElementById("login-section");
const dashboardSection = document.getElementById("dashboard-section");
const loginForm = document.getElementById("login-form");
const taskList = document.getElementById("task-list");
const btnAddTask = document.getElementById("btn-add-task");
const btnLogout = document.getElementById("btn-logout");
const welcomeMessage = document.getElementById("welcome-message");
const modal = document.getElementById("task-modal");
const closeBtn = document.querySelector(".close-button");
const taskForm = document.getElementById("task-form");
const modalTitle = document.getElementById("task-modal-title");

// === Estado global ===
let authToken = localStorage.getItem("authToken");
let editingTaskId = null;

// ===============================
// Função: Inicialização
// ===============================
document.addEventListener("DOMContentLoaded", () => {
    if (authToken) {
        carregarTarefas();
        mostrarDashboard();
    } else {
        mostrarLogin();
    }
});

// ===============================
// Login
// ===============================
loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const loginError = document.getElementById("login-error");

    try {
        const response = await fetch(LOGIN_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            loginError.textContent = "Credenciais inválidas.";
            return;
        }

        const data = await response.json();
        authToken = data.token;
        localStorage.setItem("authToken", authToken);
        loginError.textContent = "";

        carregarTarefas();
        mostrarDashboard();
    } catch (error) {
        console.error("Erro no login:", error);
        loginError.textContent = "Erro ao tentar fazer login.";
    }
});

// ===============================
// Logout
// ===============================
btnLogout.addEventListener("click", () => {
    localStorage.removeItem("authToken");
    authToken = null;
    mostrarLogin();
});

// ===============================
// Carregar Tarefas
// ===============================
async function carregarTarefas() {
    try {
        const response = await fetch(API_BASE_URL, {
            headers: { "X-Auth-Token": authToken }
        });

        if (response.status === 401) {
            alert("Sessão expirada. Faça login novamente.");
            localStorage.removeItem("authToken");
            mostrarLogin();
            return;
        }

        const tarefas = await response.json();
        exibirTarefas(tarefas);
    } catch (error) {
        console.error("Erro ao carregar tarefas:", error);
    }
}

// ===============================
// Exibir Tarefas no HTML
// ===============================
function exibirTarefas(tarefas) {
    taskList.innerHTML = "";

    if (tarefas.length === 0) {
        taskList.innerHTML = "<p>Nenhuma tarefa encontrada.</p>";
        return;
    }

    tarefas.forEach(t => {
        const div = document.createElement("div");
        div.classList.add("task-card");
        div.innerHTML = `
            <h4>${t.titulo}</h4>
            <p>${t.descricao || ""}</p>
            <small>Prioridade: ${t.prioridade}</small><br>
            <small>Status: ${t.status}</small><br>
            <small>Vencimento: ${t.dataVencimento || "—"}</small>
            <div class="task-actions">
                <button onclick="editarTarefa('${t.id}')">Editar</button>
                <button onclick="excluirTarefa('${t.id}')">Excluir</button>
            </div>
        `;
        taskList.appendChild(div);
    });
}

// ===============================
// Criar / Editar Tarefa
// ===============================
btnAddTask.addEventListener("click", () => abrirModal("Nova Tarefa"));
closeBtn.addEventListener("click", fecharModal);

taskForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const tarefa = {
        titulo: document.getElementById("task-title").value,
        descricao: document.getElementById("task-description").value,
        prioridade: document.getElementById("task-priority").value,
        status: document.getElementById("task-status").value,
        dataVencimento: document.getElementById("task-due-date").value
    };

    try {
        const url = editingTaskId ? `${API_BASE_URL}/${editingTaskId}` : API_BASE_URL;
        const method = editingTaskId ? "PUT" : "POST";

        const response = await fetch(url, {
            method,
            headers: {
                "Content-Type": "application/json",
                "X-Auth-Token": authToken
            },
            body: JSON.stringify(tarefa)
        });

        if (!response.ok) {
            alert("Erro ao salvar a tarefa.");
            return;
        }

        fecharModal();
        carregarTarefas();
    } catch (error) {
        console.error("Erro ao salvar tarefa:", error);
    }
});

// ===============================
// Editar Tarefa
// ===============================
async function editarTarefa(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            headers: { "X-Auth-Token": authToken }
        });

        if (!response.ok) return alert("Erro ao carregar tarefa.");

        const tarefa = await response.json();
        document.getElementById("task-title").value = tarefa.titulo;
        document.getElementById("task-description").value = tarefa.descricao;
        document.getElementById("task-priority").value = tarefa.prioridade;
        document.getElementById("task-status").value = tarefa.status;
        document.getElementById("task-due-date").value = tarefa.dataVencimento;

        editingTaskId = id;
        abrirModal("Editar Tarefa");
    } catch (error) {
        console.error("Erro ao editar tarefa:", error);
    }
}

// ===============================
// Excluir Tarefa
// ===============================
async function excluirTarefa(id) {
    if (!confirm("Tem certeza que deseja excluir esta tarefa?")) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: "DELETE",
            headers: { "X-Auth-Token": authToken }
        });

        if (response.ok) {
            carregarTarefas();
        } else {
            alert("Erro ao excluir tarefa.");
        }
    } catch (error) {
        console.error("Erro ao excluir tarefa:", error);
    }
}

// ===============================
// Controle de Interface
// ===============================
function mostrarLogin() {
    loginSection.style.display = "block";
    dashboardSection.style.display = "none";
    document.getElementById("main-nav").style.display = "none";
}

function mostrarDashboard() {
    loginSection.style.display = "none";
    dashboardSection.style.display = "block";
    document.getElementById("main-nav").style.display = "flex";
    welcomeMessage.textContent = "Bem-vindo!";
}

function abrirModal(titulo) {
    modal.style.display = "flex";
    modalTitle.textContent = titulo;
}

function fecharModal() {
    modal.style.display = "none";
    taskForm.reset();
    editingTaskId = null;
}