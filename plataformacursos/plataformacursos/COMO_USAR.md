# 🎓 Como Usar a Plataforma Veridia - Guia Rápido

## 🚀 Iniciando a Aplicação

### 1. Abrir o Terminal no diretório correto:
```powershell
cd C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos
```

### 2. Iniciar a aplicação:
```powershell
.\mvnw.cmd spring-boot:run
```

### 3. Acessar no navegador:
```
http://localhost:8080
```

---

## 📋 Passo a Passo para Usar o Sistema

### **Etapa 1: Cadastrar Instrutores** 👨‍🏫

1. Clique na aba **"Instrutores"**
2. Clique em **"➕ Novo Instrutor"**
3. Preencha os dados:
   - **Nome:** Ex: João Silva
   - **Email:** Ex: joao@email.com
   - **Especialidade:** Ex: Programação Java
4. Clique em **"Salvar"**

> ⚠️ **Importante:** Você precisa cadastrar pelo menos 1 instrutor antes de criar cursos!

---

### **Etapa 2: Criar Cursos** 📚

1. Clique na aba **"Cursos"**
2. Clique em **"➕ Novo Curso"**
3. Preencha os dados:
   - **Nome:** Ex: Java Completo 2025
   - **Descrição:** Ex: Curso completo de Java do zero ao avançado
   - **Preço:** Ex: 599.90
   - **Carga Horária:** Ex: 80
   - **Instrutor:** Selecione o instrutor cadastrado
4. Clique em **"Salvar"**

---

### **Etapa 3: Cadastrar Alunos** 👥

1. Clique na aba **"Alunos"**
2. Clique em **"➕ Novo Aluno"**
3. Preencha os dados:
   - **Nome:** Ex: Maria Santos
   - **Email:** Ex: maria@email.com
   - **CPF:** Ex: 12345678900
4. Clique em **"Salvar"**

---

### **Etapa 4: Fazer Inscrições** 📝

1. Clique na aba **"Inscrições"**
2. Clique em **"➕ Nova Inscrição"**
3. Selecione:
   - **Aluno:** Escolha o aluno cadastrado
   - **Curso:** Escolha o curso desejado
4. Clique em **"Salvar"**

> 📌 A inscrição será criada com status **PENDENTE**

---

### **Etapa 5: Processar Pagamento** 💰

1. Na lista de inscrições, localize a inscrição com status **PENDENTE**
2. Clique em **"💰 Pagar"**
3. Escolha o método de pagamento:
   - **1** - PIX
   - **2** - Cartão de Crédito
   - **3** - Boleto
4. Digite o número e confirme

> ✅ O status mudará para **PAGO** ou **CONFIRMADA**

---

## 🔍 Funcionalidades Adicionais

### Ver Detalhes
- Clique em **"📋 Detalhes"** em qualquer item para ver informações completas

### Cancelar Inscrição
- Clique em **"❌ Cancelar"** para cancelar uma inscrição
- Se houver pagamento, o sistema calculará o reembolso automaticamente

### Excluir Registros
- Use o botão **"🗑️ Excluir"** para remover alunos, cursos ou instrutores
- **Atenção:** Não é possível excluir itens que já possuem relacionamentos

### Atualizar Dashboard
- O **Dashboard** mostra estatísticas em tempo real
- Clique em **"🔄 Atualizar"** em cada seção para recarregar os dados

---

## 🗄️ Acessar o Banco de Dados

### H2 Console:
1. Acesse: `http://localhost:8080/h2-console`
2. Configurações:
   - **JDBC URL:** `jdbc:h2:file:./data/plataformacursos`
   - **User Name:** `sa`
   - **Password:** *(deixar em branco)*
3. Clique em **"Connect"**

### Tabelas Disponíveis:
- `ALUNO` - Alunos cadastrados
- `CURSOS` - Cursos disponíveis
- `INSTRUTOR` - Instrutores
- `INSCRICOES` - Inscrições realizadas
- `PAGAMENTOS` - Pagamentos processados

---

## 🧪 Testar a API Diretamente

### Endpoints de Teste:
```
http://localhost:8080/api/teste/status        # Ver estatísticas
http://localhost:8080/api/teste/dados         # Ver todos os dados
```

### Exemplos de Requests:

#### Listar todos os alunos:
```
GET http://localhost:8080/api/alunos
```

#### Criar um aluno via API:
```
POST http://localhost:8080/api/alunos
Content-Type: application/json

{
  "nome": "Carlos Oliveira",
  "email": "carlos@email.com",
  "cpf": "98765432100"
}
```

---

## ❌ Solução de Problemas

### Erro "Porta 8080 já está em uso":
```powershell
# Ver processos usando a porta 8080
netstat -ano | findstr :8080

# Matar o processo (substitua PID pelo número do processo)
taskkill /PID <número> /F
```

### Aplicação não inicia:
1. Verifique se está no diretório correto
2. Certifique-se que o Java 17 está instalado: `java -version`
3. Limpe e recompile: `.\mvnw.cmd clean package`

### Erro ao cadastrar:
- Verifique se preencheu todos os campos obrigatórios
- CPF deve ter 11 dígitos (apenas números)
- Email deve ser válido

---

## 📱 Design Responsivo

A interface se adapta automaticamente para:
- **Desktop** - Layout completo com grid
- **Tablet** - Layout adaptado
- **Mobile** - Menu e cards empilhados verticalmente

---

## 🎯 Regras de Negócio

### Limites:
- Cada aluno pode se inscrever em até **5 cursos ativos**
- Cursos têm **limite de vagas**
- Não é possível se inscrever em cursos que já começaram

### Reembolsos:
- **100%** - Cancelamento antes do curso começar
- **50%** - Cancelamento após início do curso (com limite de tempo)
- **0%** - Após período permitido

### Status de Inscrição:
- **PENDENTE** - Aguardando pagamento
- **PAGO** - Pagamento confirmado
- **CONFIRMADA** - Matrícula confirmada
- **CANCELADA** - Inscrição cancelada
- **REEMBOLSADA** - Valor reembolsado
- **CONCLUIDA** - Curso finalizado

---

## 🆘 Precisa de Ajuda?

- Verifique o **README.md** para documentação completa da API
- Consulte os logs do terminal para mensagens de erro detalhadas
- Teste os endpoints de teste para validar a conectividade

---

**Desenvolvido com Spring Boot 3.5.6 + Java 17 🚀**
