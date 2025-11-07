# 🔐 CREDENCIAIS DE ACESSO - VERIDIA

## 🌐 URLs de Acesso
- **Login:** http://localhost:8080/login.html
- **Área do Aluno:** http://localhost:8080/aluno.html
- **Área do Instrutor:** http://localhost:8080/instrutor.html
- **Painel Admin:** http://localhost:8080/index.html

---

## 👤 ADMINISTRADOR

| Usuário | Senha | Descrição |
|---------|-------|-----------|
| `admin` | `admin123` | Acesso total ao sistema |

**Acesso direto:** http://localhost:8080/index.html

---

## 👨‍🏫 INSTRUTORES

| Nome | Email | Senha (CPF) | Especialidade |
|------|-------|-------------|---------------|
| João Silva | `joao.silva@veridia.com` | `11122233344` | Desenvolvimento Backend |
| Maria Santos | `maria.santos@veridia.com` | `22233344455` | Data Science e IA |
| Pedro Oliveira | `pedro.oliveira@veridia.com` | `33344455566` | DevOps e Cloud |
| Ana Rodrigues | `ana.rodrigues@veridia.com` | `44455566677` | Frontend e UX |

### Instrutor de Teste
- **Email:** `instrutor@teste.com`
- **Senha:** `22222222222`
- **Observação:** Criado automaticamente pelo sistema

---

## 👨‍🎓 ALUNOS

| Nome | Email | CPF (Senha) | Situação |
|------|-------|-------------|----------|
| Carlos Mendes | `carlos.mendes@email.com` | `12345678900` | 2 cursos pagos (Java Spring Boot, Docker) |
| Ana Paula Costa | `ana.paula@email.com` | `98765432100` | 1 inscrição pendente (Python Data Science) |
| Roberto Silva | `roberto.silva@email.com` | `45678912300` | 1 pago (Java), 1 pendente (React) |
| Juliana Ferreira | `juliana.ferreira@email.com` | `78912345600` | 2 cursos pagos (Python, DevOps AWS) |
| Marcos Antonio | `marcos.antonio@email.com` | `32165498700` | 3 cursos (1 pago, 1 confirmado, 1 pendente) |

### Aluno de Teste
- **Email:** `aluno@teste.com`
- **CPF:** `11111111111`
- **Observação:** Criado automaticamente pelo sistema

---

## 🚀 COMO USAR

### Login Manual
1. Acesse http://localhost:8080/login.html
2. Escolha a aba: **Aluno**, **Instrutor** ou **Admin**
3. Digite as credenciais da tabela acima
4. Clique em "Entrar"

### Formato de Login

**Para ALUNO:**
- Campo 1: Email
- Campo 2: CPF (11 dígitos, pode usar com ou sem formatação)

**Para INSTRUTOR:**
- Campo 1: Email
- Campo 2: Senha (que é o CPF de 11 dígitos)

**Para ADMIN:**
- Campo 1: Usuário
- Campo 2: Senha

---

## 🎯 CENÁRIOS DE TESTE RECOMENDADOS

### 1. Testar como Aluno
**Use:** `carlos.mendes@email.com` / `12345678900`
- ✅ Já tem cursos pagos
- ✅ Pode se inscrever em mais cursos
- ✅ Ver histórico de pagamentos

### 2. Testar como Instrutor
**Use:** `joao.silva@veridia.com` / `11122233344`
- ✅ Já tem 2 cursos criados (Java Spring Boot, Node.js)
- ✅ Tem alunos inscritos
- ✅ Ver receita gerada

### 3. Testar como Admin
**Use:** `admin` / `admin123`
- ✅ Acesso total ao dashboard
- ✅ Gerenciar tudo no sistema

---

## 📊 DADOS PRÉ-CADASTRADOS

### Cursos Disponíveis
1. **Java Spring Boot Completo** - R$ 299,90 (João Silva)
2. **Python para Data Science** - R$ 399,90 (Maria Santos)
3. **Docker e Kubernetes** - R$ 349,90 (Pedro Oliveira)
4. **React.js Avançado** - R$ 279,90 (Ana Rodrigues)
5. **DevOps com AWS** - R$ 449,90 (Pedro Oliveira)
6. **Node.js e Express** - R$ 259,90 (João Silva) - **INATIVO**

### Estatísticas Iniciais
- 👥 **5 Alunos** cadastrados
- 👨‍🏫 **4 Instrutores** cadastrados
- 📚 **6 Cursos** (5 ativos, 1 inativo)
- 📝 **10 Inscrições** (6 pagas, 1 confirmada, 3 pendentes)
- 💰 **6 Pagamentos** aprovados (R$ 2.099,40)

---

## ⚠️ IMPORTANTE

### Para usar os instrutores cadastrados:
**Você precisa reiniciar o servidor para carregar os CPFs!**

```powershell
# Parar o servidor (Ctrl+C no terminal)
# Depois executar novamente:
cd C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos
.\mvnw.cmd spring-boot:run
```

### Depois de reiniciar:
✅ Todos os 4 instrutores poderão fazer login
✅ Banco de dados será recriado com os CPFs
✅ Todas as inscrições e pagamentos estarão disponíveis

---

## 🔒 SEGURANÇA

⚠️ **Este é um sistema de DESENVOLVIMENTO/TESTES**

- Senhas são CPFs sem criptografia
- Sessão armazenada no localStorage
- **NÃO USAR EM PRODUÇÃO**

---

**Última atualização:** 07/11/2025
