# CREDENCIAIS DE ACESSO - VERIDIA

## URLs de Acesso
- **Login:** http://localhost:8080/login.html
- **Área do Aluno:** http://localhost:8080/aluno.html
- **Área do Instrutor:** http://localhost:8080/instrutor.html
- **Painel Admin:** http://localhost:8080/index.html
- **Console H2 (Banco de Dados):** http://localhost:8080/h2-console

---

## 🗄️ ACESSO AO BANCO DE DADOS H2

### Console Web H2
- **URL de Acesso**: http://localhost:8080/h2-console
- **Driver Class**: `org.h2.Driver`
- **JDBC URL**: `jdbc:h2:file:./data/plataformacursos`
- **User Name**: `sa`
- **Password**: *(deixe em branco)*

### Localização do Arquivo do Banco
```
C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos\data\plataformacursos.mv.db
```

### Como Acessar:
1. Certifique-se de que o servidor está rodando
2. Abra o navegador em: http://localhost:8080/h2-console
3. Preencha os dados acima
4. Clique em **"Connect"**

### Tabelas Disponíveis:
- `ALUNO` - Dados dos alunos
- `INSTRUTOR` - Dados dos instrutores
- `CURSOS` - Catálogo de cursos
- `INSCRICOES` - Matrículas dos alunos
- `PAGAMENTOS` - Histórico de pagamentos

### Exemplos de Consultas SQL:
```sql
-- Ver todos os alunos
SELECT * FROM ALUNO;

-- Ver todos os cursos ativos
SELECT * FROM CURSOS WHERE ATIVO = TRUE;

-- Ver inscrições com pagamentos
SELECT i.*, p.* FROM INSCRICOES i 
LEFT JOIN PAGAMENTOS p ON i.ID = p.INSCRICAO_ID;

-- Receita total por instrutor
SELECT c.INSTRUTOR_ID, i.NOME, SUM(p.VALOR) AS RECEITA_TOTAL
FROM PAGAMENTOS p
JOIN INSCRICOES ins ON p.INSCRICAO_ID = ins.ID
JOIN CURSOS c ON ins.CURSO_ID = c.ID
JOIN INSTRUTOR i ON c.INSTRUTOR_ID = i.ID
WHERE p.STATUS = 'CONFIRMADO'
GROUP BY c.INSTRUTOR_ID, i.NOME;
```

---

## ADMINISTRADOR

| Email | Senha | Descrição |
|-------|-------|-----------|
| `admin@veridia.com` | `admin123` | Acesso total ao sistema |

**Acesso direto:** http://localhost:8080/index.html

---

## INSTRUTOR DE TESTE

| Nome | Email | Senha | Especialidade |
|------|-------|-------|---------------|
| Instrutor de Teste | `instrutor@teste.com` | `senha123` | Tecnologia |

**IMPORTANTE:** A senha pode conter letras e números (até 8 caracteres).

---

## ALUNO DE TESTE

| Nome | Email | CPF (Senha) |
|------|-------|-------------|
| Aluno de Teste | `aluno@teste.com` | `11111111111` |

**IMPORTANTE:** O CPF pode ser digitado **COM** ou **SEM** formatação. O sistema aceita ambos os formatos:
- Com formatação: `111.111.111-11`
- Sem formatação: `11111111111`

---

## COMO USAR

### Login Manual
1. Acesse http://localhost:8080/login.html
2. Escolha a aba: **Aluno**, **Instrutor** ou **Admin**
3. Digite as credenciais abaixo
4. Clique em "Entrar"

### Formato de Login

**Para ALUNO:**
- Email: `aluno@teste.com`
- CPF: `11111111111` (pode digitar `111.111.111-11` ou `11111111111`)

**Para INSTRUTOR:**
- Email: `instrutor@teste.com`
- Senha: `senha123` (até 8 caracteres, letras e números)

**Para ADMIN:**
- Usuário: `admin@veridia.com`
- Senha: `admin123` (até 8 caracteres)

---

## 💳 CREDENCIAIS DE PAGAMENTO (TESTE)

### 1. PIX 💰
- **Chave PIX**: `pagamento@teste.com`
- **Nome do Beneficiário**: Plataforma Veridia
- **Tipo de Chave**: E-mail

### 2. Cartão de Crédito 💳
- **Número do Cartão**: `4111 1111 1111 1111`
- **Titular**: TESTE USUARIO
- **Validade**: `12/2030`
- **CVV**: `123`
- **Bandeira**: Visa

**Cartões alternativos:**
- Mastercard: `5555 5555 5555 4444`
- Amex: `3782 822463 10005`

### 3. Boleto Bancário 🧾
- **Código de Barras**: Gerado automaticamente
- **Vencimento**: 3 dias úteis
- **Banco**: Banco de Testes - 001

### Como usar:
Ao clicar em **"Inscrever-se"** em um curso, escolha:
- Digite `1` para PIX
- Digite `2` para Cartão de Crédito
- Digite `3` para Boleto

⚠️ **IMPORTANTE**: Credenciais apenas para testes. Nunca use dados reais!

---

## COMO INICIAR O SERVIDOR

### Método 1: Maven Wrapper (RECOMENDADO para desenvolvimento)
```powershell
cd C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos
.\mvnw.cmd spring-boot:run
```
**Vantagens:**
- Não precisa compilar o JAR antes
- Recarrega automaticamente mudanças em código Java
- Mais prático durante desenvolvimento

### Método 2: Executar JAR compilado (mais rápido se já compilou)
```powershell
cd C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos
java -jar target\plataformacursos-0.0.1-SNAPSHOT.jar
```
**Observação:** Se mudou arquivos HTML/CSS/JS, precisa recompilar primeiro:
```powershell
.\mvnw.cmd clean package -DskipTests
```

### Para parar o servidor:
Pressione **Ctrl+C** no terminal onde o servidor está rodando

---

## IMPORTANTE

### Para apagar e recriar o banco de dados:
```powershell
# 1. Parar o servidor (Ctrl+C)

# 2. Apagar o banco de dados
Remove-Item C:\Users\rayel\Documents\GitHub\Projeto-de-Programa-o_N02_Grupo04\plataformacursos\plataformacursos\data\plataformacursos.mv.db -Force

# 3. Iniciar o servidor novamente (método 1 ou 2 acima)
```

### Depois de recriar o banco:
- Apenas 1 instrutor de teste
- Apenas 1 aluno de teste
- Banco de dados limpo

---

## SEGURANÇA

**Este é um sistema de DESENVOLVIMENTO/TESTES**

- Senhas são CPFs sem criptografia
- Sessão armazenada no localStorage
- **NÃO USAR EM PRODUÇÃO**

---

**Última atualização:** 10/11/2025
