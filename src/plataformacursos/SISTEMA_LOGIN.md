# 🔐 Sistema de Login - Veridia

## 📱 Acesso à Plataforma

A plataforma Veridia possui **3 tipos de usuários**, cada um com sua própria área e funcionalidades:

### 🚪 Página de Login
**URL:** `http://localhost:8080/login.html`

---

## 👥 Tipos de Usuário

### 1. **ALUNO** 👨‍🎓

**Alunos do Sistema:**

| Nome | Email | CPF | Status |
|------|-------|-----|--------|
| Carlos Mendes | `carlos.mendes@email.com` | `12345678900` | 2 cursos pagos |
| Ana Paula Costa | `ana.paula@email.com` | `98765432100` | 1 inscrição pendente |
| Roberto Silva | `roberto.silva@email.com` | `45678912300` | 1 pago, 1 pendente |
| Juliana Ferreira | `juliana.ferreira@email.com` | `78912345600` | 2 cursos pagos |
| Marcos Antonio | `marcos.antonio@email.com` | `32165498700` | 1 pago, 1 confirmado, 1 pendente |

**Aluno de Teste (criado automaticamente):**
- **Email:** `aluno@teste.com`
- **CPF:** `11111111111`

**Área do Aluno:** `http://localhost:8080/aluno.html`

**Funcionalidades:**
- ✅ Visualizar dados pessoais
- ✅ Ver estatísticas (total de inscrições, cursos ativos, pendentes, concluídos)
- ✅ Explorar cursos disponíveis
- ✅ Inscrever-se em cursos (limite de 5 cursos ativos)
- ✅ Visualizar minhas inscrições
- ✅ Processar pagamentos (PIX, Cartão, Boleto)
- ✅ Cancelar inscrições (com cálculo automático de reembolso)
- ✅ Ver histórico de pagamentos

**Limitações:**
- Máximo de 5 cursos ativos simultaneamente
- Não pode se inscrever em cursos que já começaram
- Não pode se inscrever em cursos sem vagas

---

### 2. **INSTRUTOR** 👨‍🏫

**Credenciais de Teste:**

| Nome | Email | Senha (CPF) | Especialidade |
|------|-------|-------------|---------------|
| João Silva | `joao.silva@veridia.com` | `11122233344` | Desenvolvimento Backend |
| Maria Santos | `maria.santos@veridia.com` | `22233344455` | Data Science e IA |
| Pedro Oliveira | `pedro.oliveira@veridia.com` | `33344455566` | DevOps e Cloud |
| Ana Rodrigues | `ana.rodrigues@veridia.com` | `44455566677` | Frontend e UX |

**Instrutor de Teste (criado automaticamente):**
- **Email:** `instrutor@teste.com`
- **Senha (CPF):** `22222222222`

**Área do Instrutor:** `http://localhost:8080/instrutor.html`

**Funcionalidades:**
- ✅ Visualizar dados pessoais e especialidade
- ✅ Ver estatísticas (total de cursos, cursos ativos, total de alunos, receita total)
- ✅ Criar novos cursos
- ✅ Gerenciar meus cursos
- ✅ Ativar/Desativar cursos
- ✅ Ver alunos inscritos em cada curso
- ✅ Marcar inscrições como concluídas
- ✅ Visualizar receita gerada pelos cursos

**Limitações:**
- Só pode ver e gerenciar seus próprios cursos
- Não pode excluir cursos com alunos inscritos

---

### 3. **ADMINISTRADOR** ⚙️

**Credenciais de Teste:**
- **Usuário:** `admin`
- **Senha:** `admin123`

**Painel Admin:** `http://localhost:8080/index.html` ou `http://localhost:8080/`

**Funcionalidades:**
- ✅ Acesso total ao dashboard do sistema
- ✅ Gerenciar TODOS os alunos
- ✅ Gerenciar TODOS os cursos
- ✅ Gerenciar TODOS os instrutores
- ✅ Gerenciar TODAS as inscrições
- ✅ Processar pagamentos de qualquer aluno
- ✅ Cancelar qualquer inscrição
- ✅ Ver estatísticas completas do sistema
- ✅ Criar/Editar/Excluir qualquer registro

**Sem Limitações:** Controle total sobre todo o sistema

---

## 🚀 Como Fazer Login

### Método 1: Login Manual

1. Acesse `http://localhost:8080/login.html`
2. Escolha a aba do tipo de usuário (Aluno, Instrutor ou Admin)
3. Preencha as credenciais conforme indicado
4. Clique em "Entrar"

### Método 2: Acesso Rápido (Recomendado para Testes)

Na página de login, clique em um dos botões de **Acesso Rápido**:
- **"Entrar como Aluno de Teste"** - Cria/loga automaticamente como aluno
- **"Entrar como Instrutor de Teste"** - Cria/loga automaticamente como instrutor  
- **"Entrar como Admin"** - Loga automaticamente como administrador

> 💡 **Dica:** O sistema cria automaticamente os usuários de teste se eles não existirem!

---

## 🔄 Fluxo de Autenticação

```
1. Usuário acessa login.html
2. Escolhe o tipo de login (Aluno/Instrutor/Admin)
3. Faz login com credenciais OU usa Acesso Rápido
4. Sistema valida credenciais contra o backend
5. Cria sessão no localStorage
6. Redireciona para área apropriada:
   - Aluno → aluno.html
   - Instrutor → instrutor.html
   - Admin → index.html
7. Área protege acesso verificando sessão
```

---

## 🔒 Proteção de Rotas

Todas as páginas verificam a autenticação:

- **login.html** - Redireciona para área logada se já tiver sessão ativa
- **aluno.html** - Só permite acesso de usuários tipo "aluno"
- **instrutor.html** - Só permite acesso de usuários tipo "instrutor"
- **index.html** - Só permite acesso de usuários tipo "admin"

Se tentar acessar sem login ou com tipo errado → **Redirecionamento para login.html**

---

## 💾 Armazenamento de Sessão

A sessão é salva no **localStorage** do navegador com estrutura:

```json
{
  "tipo": "aluno|instrutor|admin",
  "usuario": {
    "id": 1,
    "nome": "Nome do Usuário",
    "email": "usuario@email.com",
    ...
  },
  "loginTime": "2025-11-07T20:30:00.000Z"
}
```

---

## 🚪 Logout

Em todas as áreas há um botão **"🚪 Sair"** que:
1. Remove a sessão do localStorage
2. Redireciona para login.html

---

## 🧪 Testando o Sistema

### Cenário 1: Fluxo Completo como Aluno

1. Acesse `http://localhost:8080/login.html`
2. Clique em **"Entrar como Aluno de Teste"**
3. Veja seus dados na área do aluno
4. Vá em **"Cursos Disponíveis"**
5. Inscreva-se em um curso
6. Vá em **"Minhas Inscrições"**
7. Processe o pagamento
8. Veja o histórico em **"Pagamentos"**

### Cenário 2: Fluxo Completo como Instrutor

1. Acesse `http://localhost:8080/login.html`
2. Clique em **"Entrar como Instrutor de Teste"**
3. Vá em **"Criar Curso"**
4. Preencha e crie um novo curso
5. Vá em **"Meus Cursos"** para ver o curso criado
6. Quando alunos se inscreverem, veja em **"Alunos Inscritos"**

### Cenário 3: Gestão Completa como Admin

1. Acesse `http://localhost:8080/login.html`
2. Clique em **"Entrar como Admin"**
3. Acesse o **Dashboard** com estatísticas completas
4. Gerencie alunos, cursos, instrutores e inscrições
5. Tenha controle total sobre todo o sistema

---

## ⚠️ Observações Importantes

### Primeira vez usando o sistema?

Se você nunca criou usuários antes, use o **Acesso Rápido** para criar automaticamente:
- Aluno de Teste (ID será gerado)
- Instrutor de Teste (ID será gerado)

### Criando usuários manualmente?

**Para Alunos:**
1. Logue como admin
2. Vá em "Alunos" → "Novo Aluno"
3. Cadastre com email e CPF
4. Use essas credenciais para fazer login como aluno

**Para Instrutores:**
1. Logue como admin
2. Vá em "Instrutores" → "Novo Instrutor"
3. Cadastre com email e CPF
4. Use email + CPF (como senha) para fazer login como instrutor

---

## 🔐 Segurança

> ⚠️ **ATENÇÃO:** Este é um sistema de **TESTES/DESENVOLVIMENTO**

**Características da autenticação atual:**
- ✅ Validação de email e CPF contra banco de dados
- ✅ Proteção de rotas por tipo de usuário
- ✅ Sessão armazenada no localStorage
- ❌ **NÃO** usa criptografia de senha
- ❌ **NÃO** usa tokens JWT
- ❌ **NÃO** possui timeout de sessão
- ❌ **NÃO** deve ser usado em produção

**Para produção seria necessário:**
- Implementar Spring Security
- Usar JWT ou OAuth2
- Criptografar senhas com BCrypt
- Adicionar HTTPS
- Implementar rate limiting
- Adicionar autenticação de 2 fatores

---

## 🎯 Casos de Uso por Perfil

### Aluno pode:
- ✅ Ver apenas seus próprios dados
- ✅ Ver apenas suas próprias inscrições
- ✅ Ver apenas seus próprios pagamentos
- ❌ Não pode ver dados de outros alunos
- ❌ Não pode gerenciar cursos
- ❌ Não pode ver receitas

### Instrutor pode:
- ✅ Ver apenas seus próprios cursos
- ✅ Ver alunos inscritos nos seus cursos
- ✅ Ver receita dos seus cursos
- ❌ Não pode ver cursos de outros instrutores
- ❌ Não pode gerenciar alunos
- ❌ Não pode acessar área administrativa

### Admin pode:
- ✅ Ver TUDO
- ✅ Gerenciar TUDO
- ✅ Sem restrições

---

## 📞 Suporte

**Problemas comuns:**

1. **"Aluno não encontrado"**
   - Verifique se o CPF tem 11 dígitos
   - Use o Acesso Rápido para criar automaticamente

2. **"Acesso negado"**
   - Você está tentando acessar uma área de outro perfil
   - Faça logout e logue com o perfil correto

3. **"Erro ao conectar com servidor"**
   - Verifique se a aplicação Spring Boot está rodando
   - Confirme que está acessando `http://localhost:8080`

4. **Sessão não persiste**
   - Limpe o cache do navegador
   - Verifique se localStorage está habilitado

---

**Sistema desenvolvido com:**
- Frontend: HTML5 + CSS3 + JavaScript (Vanilla)
- Backend: Spring Boot 3.5.6 + Java 17
- Autenticação: Custom (localStorage + validação via API)
