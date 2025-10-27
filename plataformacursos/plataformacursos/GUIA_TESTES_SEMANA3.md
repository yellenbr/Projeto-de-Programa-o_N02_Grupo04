# 🧪 Guia de Testes - Semana 3

## 📋 Objetivo
Validar a persistência com Spring Data JPA e os relacionamentos entre entidades.

## 🚀 Como Executar

### 1. Iniciar a Aplicação
```bash
cd plataformacursos/plataformacursos
./mvnw spring-boot:run
```

### 2. Acessar o Console H2
🌐 **URL:** http://localhost:8080/h2-console

**Credenciais:**
- JDBC URL: `jdbc:h2:file:./data/plataformacursos`
- Username: `sa`
- Password: *(deixar em branco)*

---

## 🔍 Endpoints de Teste

### ✅ Verificar Status do Banco
```
GET http://localhost:8080/api/teste/status
```
**Retorna:** Contagem de registros em cada tabela

### 📊 Listar Todos os Dados
```
GET http://localhost:8080/api/teste/dados
```
**Retorna:** Todos os instrutores, cursos, alunos, inscrições e pagamentos

---

## 📚 Endpoints por Entidade

### 👨‍🏫 Instrutores
```
GET http://localhost:8080/api/teste/instrutores
```

### 📖 Cursos
```
GET http://localhost:8080/api/teste/cursos
```

### 🧑‍🎓 Alunos
```
GET http://localhost:8080/api/teste/alunos
```

### 📝 Inscrições
```
GET http://localhost:8080/api/teste/inscricoes
```

### 💰 Pagamentos
```
GET http://localhost:8080/api/teste/pagamentos
```

---

## 🔗 Endpoints com Relacionamentos

### Detalhes de Curso (com inscrições e instrutor)
```
GET http://localhost:8080/api/teste/curso/1/detalhes
```
**Mostra:**
- Dados do curso
- Instrutor vinculado
- Número de inscritos
- Vagas disponíveis
- Lista de inscrições

### Detalhes de Aluno (com inscrições e cursos)
```
GET http://localhost:8080/api/teste/aluno/1/detalhes
```
**Mostra:**
- Dados do aluno
- Número de cursos ativos
- Inscrições pendentes
- Lista de todas as inscrições

### Detalhes de Inscrição (com aluno, curso e pagamento)
```
GET http://localhost:8080/api/teste/inscricao/1/detalhes
```
**Mostra:**
- Dados da inscrição
- Aluno vinculado
- Curso vinculado
- Pagamento relacionado
- Status e regras de negócio

---

## 🧪 Teste de Criação (POST)

### Criar Aluno de Teste
```
POST http://localhost:8080/api/teste/criar-aluno-teste
```
**Retorna:** Aluno criado com ID gerado automaticamente

---

## 📊 Dados Iniciais Populados

### 4 Instrutores:
1. João Silva - Desenvolvimento Backend
2. Maria Santos - Data Science e IA
3. Pedro Oliveira - DevOps e Cloud
4. Ana Rodrigues - Frontend e UX

### 6 Cursos:
1. Java Spring Boot Completo - R$ 299,90
2. Python para Data Science - R$ 399,90
3. Docker e Kubernetes - R$ 349,90
4. React.js Avançado - R$ 279,90
5. DevOps com AWS - R$ 449,90
6. Node.js e Express (INATIVO) - R$ 259,90

### 5 Alunos:
1. Carlos Mendes (2 cursos pagos)
2. Ana Paula Costa (1 pendente)
3. Roberto Silva (1 pago, 1 pendente)
4. Juliana Ferreira (2 cursos pagos)
5. Marcos Antonio (1 pago, 1 confirmado, 1 pendente)

### 10 Inscrições:
- 6 pagas
- 1 confirmada
- 3 pendentes

### 6 Pagamentos:
- Todos aprovados
- Métodos: PIX, Cartão de Crédito, Boleto

---

## ✅ Checklist de Validação

- [ ] Console H2 acessível e conectado
- [ ] Endpoint `/api/teste/status` retorna contagem correta
- [ ] Todos os instrutores listados (4)
- [ ] Todos os cursos listados (6)
- [ ] Todos os alunos listados (5)
- [ ] Relacionamento Curso → Instrutor funcionando
- [ ] Relacionamento Aluno → Inscrições funcionando
- [ ] Relacionamento Inscrição → Pagamento funcionando
- [ ] Métodos de negócio funcionando (ex: `getNumeroInscritos()`)
- [ ] Criação de novo aluno funciona (POST)

---

## 🐛 Resolução de Problemas

### Banco não populou automaticamente
**Solução:** Verifique se `spring.sql.init.mode=always` está no `application.properties`

### Erro de FK ou relacionamento
**Solução:** Verifique se o `ddl-auto=update` está configurado e reinicie a aplicação

### Console H2 não abre
**Solução:** Verifique se `spring.h2.console.enabled=true` está configurado

---

## 🎯 Próximos Passos

Após validar que tudo está funcionando:
1. Testar CRUD completo via Postman
2. Implementar testes automatizados (JUnit)
3. Adicionar validações de regras de negócio
4. Implementar segurança (Spring Security)

---

## 📞 Endpoints Completos da API

Além dos endpoints de teste, você tem os endpoints principais:

### Alunos
- `GET /api/alunos` - Listar todos
- `GET /api/alunos/{id}` - Buscar por ID
- `POST /api/alunos` - Criar
- `PUT /api/alunos/{id}` - Atualizar
- `DELETE /api/alunos/{id}` - Deletar

### Cursos
- `GET /api/cursos` - Listar todos
- `GET /api/cursos/{id}` - Buscar por ID
- `POST /api/cursos` - Criar
- `PUT /api/cursos/{id}` - Atualizar
- `DELETE /api/cursos/{id}` - Deletar

### Fluxo de Aluno (Inscrições)
- `POST /api/alunos/{id}/inscrever` - Inscrever em curso
- `POST /api/alunos/{alunoId}/inscricoes/{inscricaoId}/pagamento` - Processar pagamento
- `POST /api/alunos/{alunoId}/inscricoes/{inscricaoId}/cancelar` - Cancelar inscrição
- `POST /api/alunos/{alunoId}/inscricoes/{inscricaoId}/transferir` - Transferir curso

### Instrutores
- `POST /api/instrutores` - Criar instrutor
- `POST /api/instrutores/{instrutorId}/cursos/{cursoId}` - Vincular a curso
- `DELETE /api/instrutores/{instrutorId}/cursos/{cursoId}` - Cancelar curso

---

✅ **Semana 3 Completa!** Todos os requisitos implementados:
- ✅ Banco de dados configurado (H2)
- ✅ Relacionamentos JPA mapeados
- ✅ Repositórios implementados
- ✅ data.sql populando banco automaticamente
- ✅ Endpoints de teste funcionando
- ✅ CRUD completo validado
