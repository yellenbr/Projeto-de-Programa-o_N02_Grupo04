// API Base URL
const API_URL = 'http://localhost:8080/api';

// Função de logout
function logout() {
//... (o resto do arquivo permanece o mesmo)
    if (confirm('Deseja realmente sair?')) {
        localStorage.removeItem('session');
        window.location.href = 'login.html';
    }
}

// Função de voltar ao login (trocar conta)
function voltarLogin() {
    if (confirm('Deseja voltar para a tela de login? Você será desconectado.')) {
        localStorage.removeItem('session');
        window.location.href = 'login.html';
    }
}

// Funções de Navegação
function showSection(sectionId) {
    // Esconder todas as seções
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remover classe active de todos os botões
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Mostrar seção selecionada
    document.getElementById(sectionId).classList.add('active');
    
    // Adicionar classe active ao botão clicado
    event.target.classList.add('active');
    
    // Carregar dados da seção
    switch(sectionId) {
        case 'dashboard':
            carregarDashboard();
            break;
        case 'alunos':
            carregarAlunos();
            break;
        case 'cursos':
            carregarCursos();
            break;
        case 'instrutores':
            carregarInstrutores();
            break;
        case 'inscricoes':
            carregarInscricoes();
            break;
    }
}

// Funções de Modal
function showModalAluno() {
    document.getElementById('modalAluno').classList.add('active');
}

function showModalCurso() {
    carregarInstrutoresSelect();
    document.getElementById('modalCurso').classList.add('active');
}

function showModalInstrutor() {
    document.getElementById('modalInstrutor').classList.add('active');
}

function showModalInscricao() {
    carregarAlunosSelect();
    carregarCursosSelect();
    document.getElementById('modalInscricao').classList.add('active');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

// Funções de Dashboard
async function carregarDashboard() {
    try {
        const totalAlunosEl = document.getElementById('totalAlunos');
        if (!totalAlunosEl) return;
        
        const response = await fetch(`${API_URL}/teste/status`);
        const data = await response.json();
        
        totalAlunosEl.textContent = data.alunos || 0;
        document.getElementById('totalCursos').textContent = data.cursos || 0;
        document.getElementById('totalInstrutores').textContent = data.instrutores || 0;
        document.getElementById('totalInscricoes').textContent = data.inscricoes || 0;
    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
    }
}

// ========== ALUNOS ==========
async function carregarAlunos() {
    try {
        const response = await fetch(`${API_URL}/alunos`);
        const alunos = await response.json();
        
        const listaAlunos = document.getElementById('listaAlunos');
        listaAlunos.innerHTML = '';
        
        if (alunos.length === 0) {
            listaAlunos.innerHTML = '<p class="alert alert-info">Nenhum aluno cadastrado.</p>';
            return;
        }
        
        alunos.forEach(aluno => {
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <h4>${aluno.nome}</h4>
                <p><strong>Email:</strong> ${aluno.email}</p>
                <p><strong>CPF:</strong> ${formatarCPF(aluno.cpf)}</p>
                <p><strong>ID:</strong> ${aluno.id}</p>
                <div class="item-actions">
                    <button onclick="verDetalhesAluno(${aluno.id})" class="btn-primary">Detalhes</button>
                    <button onclick="deletarAluno(${aluno.id})" class="btn-danger">Excluir</button>
                </div>
            `;
            listaAlunos.appendChild(div);
        });
    } catch (error) {
        console.error('Erro ao carregar alunos:', error);
        showAlert('Erro ao carregar alunos', 'error');
    }
}

async function salvarAluno(event) {
    event.preventDefault();
    
    const aluno = {
        nome: document.getElementById('alunoNome').value,
        email: document.getElementById('alunoEmail').value,
        cpf: document.getElementById('alunoCpf').value
    };
    
    try {
        const response = await fetch(`${API_URL}/alunos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(aluno)
        });
        
        if (response.ok) {
            showAlert('Aluno cadastrado com sucesso!', 'success');
            closeModal('modalAluno');
            carregarAlunos();
            event.target.reset();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao cadastrar aluno', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao cadastrar aluno', 'error');
    }
}

async function deletarAluno(id) {
    if (!confirm('Deseja realmente excluir este aluno?')) return;
    
    try {
        const response = await fetch(`${API_URL}/alunos/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('Aluno excluído com sucesso!', 'success');
            carregarAlunos();
        } else {
            showAlert('Erro ao excluir aluno', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao excluir aluno', 'error');
    }
}

async function verDetalhesAluno(id) {
    try {
        const response = await fetch(`${API_URL}/teste/aluno/${id}/detalhes`);
        const data = await response.json();
        
        let html = `
            <h3>Detalhes do Aluno</h3>
            <p><strong>Nome:</strong> ${data.aluno.nome}</p>
            <p><strong>Email:</strong> ${data.aluno.email}</p>
            <p><strong>CPF:</strong> ${formatarCPF(data.aluno.cpf)}</p>
            <p><strong>Cursos Ativos:</strong> ${data.numeroCursosAtivos}</p>
            <p><strong>Tem Pendentes:</strong> ${data.temInscricoesPendentes ? 'Sim' : 'Não'}</p>
            <h4>Inscrições:</h4>
        `;
        
        if (data.inscricoes && data.inscricoes.length > 0) {
            data.inscricoes.forEach(insc => {
                html += `<p>• ${insc.curso.nome} - ${insc.status}</p>`;
            });
        } else {
            html += '<p>Nenhuma inscrição</p>';
        }
        
        mostrarModal('Detalhes do Aluno', html);
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao carregar detalhes', 'error');
    }
}

// ========== CURSOS ==========
async function carregarCursos() {
    try {
        const response = await fetch(`${API_URL}/cursos`);
        const cursos = await response.json();
        
        const listaCursos = document.getElementById('listaCursos');
        listaCursos.innerHTML = '';
        
        if (cursos.length === 0) {
            listaCursos.innerHTML = '<p class="alert alert-info">Nenhum curso cadastrado.</p>';
            return;
        }
        
        cursos.forEach(curso => {
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <h4>${curso.nome}</h4>
                <p>${curso.descricao || 'Sem descrição'}</p>
                <p><strong>Preço:</strong> R$ ${curso.preco.toFixed(2)}</p>
                <p><strong>Carga Horária:</strong> ${curso.cargaHoraria}h</p>
                <p><strong>Vagas:</strong> ${curso.vagas}</p>
                <p><strong>Status:</strong> 
                    <span class="badge ${curso.ativo ? 'badge-success' : 'badge-danger'}">
                        ${curso.ativo ? 'Ativo' : 'Inativo'}
                    </span>
                </p>
                <div class="item-actions">
                    <button onclick="verDetalhesCurso(${curso.id})" class="btn-primary">Detalhes</button>
                    <button onclick="deletarCurso(${curso.id})" class="btn-danger">Excluir</button>
                </div>
            `;
            listaCursos.appendChild(div);
        });
    } catch (error) {
        console.error('Erro ao carregar cursos:', error);
        showAlert('Erro ao carregar cursos', 'error');
    }
}

async function salvarCurso(event) {
    event.preventDefault();
    
    const curso = {
        nome: document.getElementById('cursoNome').value,
        descricao: document.getElementById('cursoDescricao').value,
        preco: parseFloat(document.getElementById('cursoPreco').value),
        cargaHoraria: parseInt(document.getElementById('cursoCargaHoraria').value),
        instrutor: { id: parseInt(document.getElementById('cursoInstrutor').value) }
    };
    
    try {
        const response = await fetch(`${API_URL}/cursos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(curso)
        });
        
        if (response.ok) {
            showAlert('Curso cadastrado com sucesso!', 'success');
            closeModal('modalCurso');
            carregarCursos();
            event.target.reset();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao cadastrar curso', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao cadastrar curso', 'error');
    }
}

async function deletarCurso(id) {
    if (!confirm('Deseja realmente excluir este curso?')) return;
    
    try {
        const response = await fetch(`${API_URL}/cursos/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('Curso excluído com sucesso!', 'success');
            carregarCursos();
        } else {
            showAlert('Erro ao excluir curso', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao excluir curso', 'error');
    }
}

async function verDetalhesCurso(id) {
    try {
        const response = await fetch(`${API_URL}/teste/curso/${id}/detalhes`);
        const data = await response.json();
        
        let html = `
            <h3>Detalhes do Curso</h3>
            <p><strong>Nome:</strong> ${data.curso.nome}</p>
            <p><strong>Descrição:</strong> ${data.curso.descricao || 'N/A'}</p>
            <p><strong>Preço:</strong> R$ ${data.curso.preco.toFixed(2)}</p>
            <p><strong>Carga Horária:</strong> ${data.curso.cargaHoraria}h</p>
            <p><strong>Instrutor:</strong> ${data.instrutor ? data.instrutor.nome : 'Não vinculado'}</p>
            <p><strong>Inscritos:</strong> ${data.numeroInscritos}/${data.curso.vagas}</p>
            <p><strong>Vagas Disponíveis:</strong> ${data.vagasDisponiveis ? 'Sim' : 'Não'}</p>
        `;
        
        mostrarModal('Detalhes do Curso', html);
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao carregar detalhes', 'error');
    }
}

// ========== INSTRUTORES ==========
async function carregarInstrutores() {
    try {
        const response = await fetch(`${API_URL}/instrutores`);
        const instrutores = await response.json();
        
        const listaInstrutores = document.getElementById('listaInstrutores');
        listaInstrutores.innerHTML = '';
        
        if (instrutores.length === 0) {
            listaInstrutores.innerHTML = '<p class="alert alert-info">Nenhum instrutor cadastrado.</p>';
            return;
        }
        
        instrutores.forEach(instrutor => {
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <h4>${instrutor.nome}</h4>
                <p><strong>Email:</strong> ${instrutor.email}</p>
                <p><strong>Especialidade:</strong> ${instrutor.especialidade || 'Não informada'}</p>
                <p><strong>ID:</strong> ${instrutor.id}</p>
                <div class="item-actions">
                    <button onclick="deletarInstrutor(${instrutor.id})" class="btn-danger">Excluir</button>
                </div>
            `;
            listaInstrutores.appendChild(div);
        });
    } catch (error) {
        console.error('Erro ao carregar instrutores:', error);
        showAlert('Erro ao carregar instrutores', 'error');
    }
}

async function salvarInstrutor(event) {
    event.preventDefault();
    
    const instrutor = {
        nome: document.getElementById('instrutorNome').value,
        email: document.getElementById('instrutorEmail').value,
        especialidade: document.getElementById('instrutorEspecialidade').value
    };
    
    try {
        const response = await fetch(`${API_URL}/instrutores`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(instrutor)
        });
        
        if (response.ok) {
            showAlert('Instrutor cadastrado com sucesso!', 'success');
            closeModal('modalInstrutor');
            carregarInstrutores();
            event.target.reset();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao cadastrar instrutor', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao cadastrar instrutor', 'error');
    }
}

async function deletarInstrutor(id) {
    if (!confirm('Deseja realmente excluir este instrutor?')) return;
    
    try {
        const response = await fetch(`${API_URL}/instrutores/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('Instrutor excluído com sucesso!', 'success');
            carregarInstrutores();
        } else {
            showAlert('Erro ao excluir instrutor', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao excluir instrutor', 'error');
    }
}

// ========== INSCRIÇÕES ==========
async function carregarInscricoes() {
    try {
        const response = await fetch(`${API_URL}/inscricoes`);
        const inscricoes = await response.json();
        
        const listaInscricoes = document.getElementById('listaInscricoes');
        listaInscricoes.innerHTML = '';
        
        if (inscricoes.length === 0) {
            listaInscricoes.innerHTML = '<p class="alert alert-info">Nenhuma inscrição realizada.</p>';
            return;
        }
        
        inscricoes.forEach(inscricao => {
            const statusClass = 
                inscricao.status === 'PAGO' || inscricao.status === 'CONFIRMADA' ? 'badge-success' :
                inscricao.status === 'PENDENTE' ? 'badge-warning' :
                'badge-danger';
            
            const div = document.createElement('div');
            div.className = 'data-item';
            div.innerHTML = `
                <h4>Inscrição #${inscricao.id}</h4>
                <p><strong>Aluno:</strong> ${inscricao.aluno.nome}</p>
                <p><strong>Curso:</strong> ${inscricao.curso.nome}</p>
                <p><strong>Data:</strong> ${new Date(inscricao.dataInscricao).toLocaleString('pt-BR')}</p>
                <p><strong>Status:</strong> 
                    <span class="badge ${statusClass}">${inscricao.status}</span>
                </p>
                <div class="item-actions">
                    <button onclick="verDetalhesInscricao(${inscricao.id})" class="btn-primary">Detalhes</button>
                    ${inscricao.status === 'PENDENTE' ? 
                        `<button onclick="processarPagamento(${inscricao.id})" class="btn-secondary">Pagar</button>` : ''}
                    ${inscricao.status !== 'CANCELADA' ? 
                        `<button onclick="cancelarInscricao(${inscricao.id})" class="btn-danger">Cancelar</button>` : ''}
                </div>
            `;
            listaInscricoes.appendChild(div);
        });
    } catch (error) {
        console.error('Erro ao carregar inscrições:', error);
        showAlert('Erro ao carregar inscrições', 'error');
    }
}

async function salvarInscricao(event) {
    event.preventDefault();
    
    const alunoId = document.getElementById('inscricaoAluno').value;
    const cursoId = document.getElementById('inscricaoCurso').value;
    
    try {
        const response = await fetch(
            `${API_URL}/alunos/${alunoId}/inscrever?cursoId=${cursoId}`,
            { method: 'POST' }
        );
        
        if (response.ok) {
            showAlert('Inscrição realizada com sucesso!', 'success');
            closeModal('modalInscricao');
            carregarInscricoes();
            event.target.reset();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao realizar inscrição', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao realizar inscrição', 'error');
    }
}

async function processarPagamento(inscricaoId) {
    const metodo = prompt('Escolha o método de pagamento:\n1 - PIX\n2 - Cartão de Crédito\n3 - Boleto');
    const metodos = { '1': 'PIX', '2': 'CARTAO_CREDITO', '3': 'BOLETO' };
    
    if (!metodos[metodo]) {
        showAlert('Método de pagamento inválido', 'error');
        return;
    }
    
    try {
        // Buscar o alunoId da inscrição
        const inscResponse = await fetch(`${API_URL}/inscricoes/${inscricaoId}`);
        const inscricao = await inscResponse.json();
        
        const response = await fetch(
            `${API_URL}/alunos/${inscricao.aluno.id}/inscricoes/${inscricaoId}/pagamento?metodoPagamento=${metodos[metodo]}`,
            { method: 'POST' }
        );
        
        if (response.ok) {
            showAlert('Pagamento processado com sucesso!', 'success');
            carregarInscricoes();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao processar pagamento', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao processar pagamento', 'error');
    }
}

async function cancelarInscricao(inscricaoId) {
    if (!confirm('Deseja realmente cancelar esta inscrição?')) return;
    
    try {
        // Buscar o alunoId da inscrição
        const inscResponse = await fetch(`${API_URL}/inscricoes/${inscricaoId}`);
        const inscricao = await inscResponse.json();
        
        const response = await fetch(
            `${API_URL}/alunos/${inscricao.aluno.id}/inscricoes/${inscricaoId}/cancelar`,
            { method: 'POST' }
        );
        
        if (response.ok) {
            const reembolso = await response.json();
            if (reembolso.valorReembolsado > 0) {
                showAlert(`Inscrição cancelada! Reembolso de R$ ${reembolso.valorReembolsado.toFixed(2)}`, 'success');
            } else {
                showAlert('Inscrição cancelada com sucesso!', 'success');
            }
            carregarInscricoes();
        } else {
            const error = await response.json();
            showAlert(error.mensagem || 'Erro ao cancelar inscrição', 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao cancelar inscrição', 'error');
    }
}

async function verDetalhesInscricao(id) {
    try {
        const response = await fetch(`${API_URL}/teste/inscricao/${id}/detalhes`);
        const data = await response.json();
        
        let html = `
            <h3>Detalhes da Inscrição</h3>
            <p><strong>ID:</strong> ${data.inscricao.id}</p>
            <p><strong>Aluno:</strong> ${data.aluno.nome}</p>
            <p><strong>Curso:</strong> ${data.curso.nome}</p>
            <p><strong>Data:</strong> ${new Date(data.inscricao.dataInscricao).toLocaleString('pt-BR')}</p>
            <p><strong>Status:</strong> ${data.status}</p>
            <p><strong>Está Ativa:</strong> ${data.isAtiva ? 'Sim' : 'Não'}</p>
            <p><strong>Pode Cancelar:</strong> ${data.podeCancelar ? 'Sim' : 'Não'}</p>
            <p><strong>Tem Direito a Reembolso:</strong> ${data.temDireitoReembolso ? 'Sim' : 'Não'}</p>
        `;
        
        if (data.pagamento) {
            html += `
                <h4>Pagamento</h4>
                <p><strong>Valor:</strong> R$ ${data.pagamento.valor.toFixed(2)}</p>
                <p><strong>Método:</strong> ${data.pagamento.metodoPagamento}</p>
                <p><strong>Status:</strong> ${data.pagamento.status}</p>
            `;
        }
        
        mostrarModal('Detalhes da Inscrição', html);
    } catch (error) {
        console.error('Erro:', error);
        showAlert('Erro ao carregar detalhes', 'error');
    }
}

// ========== FUNÇÕES AUXILIARES ==========
async function carregarInstrutoresSelect() {
    try {
        const response = await fetch(`${API_URL}/instrutores`);
        const instrutores = await response.json();
        
        const select = document.getElementById('cursoInstrutor');
        select.innerHTML = '<option value="">Selecione o instrutor</option>';
        
        instrutores.forEach(instrutor => {
            const option = document.createElement('option');
            option.value = instrutor.id;
            option.textContent = instrutor.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar instrutores:', error);
    }
}

async function carregarAlunosSelect() {
    try {
        const response = await fetch(`${API_URL}/alunos`);
        const alunos = await response.json();
        
        const select = document.getElementById('inscricaoAluno');
        select.innerHTML = '<option value="">Selecione o aluno</option>';
        
        alunos.forEach(aluno => {
            const option = document.createElement('option');
            option.value = aluno.id;
            option.textContent = aluno.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar alunos:', error);
    }
}

async function carregarCursosSelect() {
    try {
        const response = await fetch(`${API_URL}/cursos`);
        const cursos = await response.json();
        
        const select = document.getElementById('inscricaoCurso');
        select.innerHTML = '<option value="">Selecione o curso</option>';
        
        cursos.filter(c => c.ativo).forEach(curso => {
            const option = document.createElement('option');
            option.value = curso.id;
            option.textContent = `${curso.nome} - R$ ${curso.preco.toFixed(2)}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar cursos:', error);
    }
}

function formatarCPF(cpf) {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
}

function showAlert(mensagem, tipo) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo === 'error' ? 'error' : tipo === 'success' ? 'success' : 'info'}`;
    alertDiv.textContent = mensagem;
    alertDiv.style.position = 'fixed';
    alertDiv.style.top = '20px';
    alertDiv.style.right = '20px';
    alertDiv.style.zIndex = '9999';
    alertDiv.style.minWidth = '300px';
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

function mostrarModal(titulo, conteudo) {
    const modal = document.createElement('div');
    modal.className = 'modal active';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close" onclick="this.closest('.modal').remove()">&times;</span>
            ${conteudo}
        </div>
    `;
    document.body.appendChild(modal);
}

function redirectToLoginWithMessage(message, type = 'info', extras = {}) {
    const params = new URLSearchParams();
    if (message) params.set('message', message);
    if (type) params.set('type', type);
    if (extras.email) params.set('email', extras.email);
    if (extras.cpf) params.set('cpf', extras.cpf);
    if (extras.tab) params.set('tab', extras.tab);
    const query = params.toString();
    window.location.href = query ? `login.html?${query}` : 'login.html';
}

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    carregarDashboard();
    
    // Fechar modal ao clicar fora
    window.onclick = function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.classList.remove('active');
        }
    };
});
