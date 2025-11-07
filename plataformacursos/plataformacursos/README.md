# 🎓 Plataforma Veridia Cursos

Sistema de gerenciamento de cursos online desenvolvido com Spring Boot para a gestão completa de alunos, instrutores, cursos, inscrições e pagamentos.

## 📋 Sobre o Projeto

A Plataforma Veridia Cursos é uma aplicação web completa que permite:
- Cadastro e gerenciamento de alunos
- Cadastro e gerenciamento de instrutores
- Criação e administração de cursos
- Sistema de inscrições com controle de vagas
- Processamento de pagamentos
- Controle de transferências entre cursos
- Sistema de reembolso

## 🚀 Tecnologias Utilizadas

- **Java 17** - LTS (Long Term Support)
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **Hibernate** - ORM (Object-Relational Mapping)
- **H2 Database** - Banco de dados em memória para desenvolvimento
- **MySQL** - Banco de dados para produção
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de código boilerplate

## Pré-requisitos

- Java JDK 17 ou superior
- Maven 3.8+ (ou use o Maven Wrapper incluído no projeto)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## Instalação e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/yellenbr/Projeto-de-Programa-o_N02_Grupo04.git
cd Projeto-de-Programa-o_N02_Grupo04/plataformacursos/plataformacursos
```

### 2. Compile o projeto

```bash
# No Windows
.\mvnw.cmd clean install

# No Linux/Mac
./mvnw clean install
```

### 3. Execute a aplicação

```bash
# No Windows
.\mvnw.cmd spring-boot:run

# No Linux/Mac
./mvnw spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

## Estrutura do Projeto

```
plataformacursos/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/veridia/gestao/plataformacursos/
│   │   │       ├── controller/      # Controllers REST
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Tratamento de exceções
│   │   │       ├── model/           # Entidades JPA
│   │   │       ├── repository/      # Repositórios JPA
│   │   │       └── service/         # Lógica de negócio
│   │   └── resources/
│   │       ├── static/              # Arquivos estáticos (HTML, CSS, JS)
│   │       ├── application.properties
│   │       └── data.sql             # Dados iniciais
│   └── test/                        # Testes unitários e integração
├── pom.xml                          # Configuração Maven
└── README.md
```

## 🌐 Endpoints da API

### Alunos
- `GET /api/alunos` - Listar todos os alunos
- `GET /api/alunos/{id}` - Buscar aluno por ID
- `POST /api/alunos` - Criar novo aluno
- `PUT /api/alunos/{id}` - Atualizar aluno
- `DELETE /api/alunos/{id}` - Deletar aluno

### Cursos
- `GET /api/cursos` - Listar todos os cursos
- `GET /api/cursos/{id}` - Buscar curso por ID
- `GET /api/cursos/nome/{nome}` - Buscar cursos por nome
- `GET /api/cursos/instrutor/{instrutorId}` - Listar cursos de um instrutor
- `POST /api/cursos` - Criar novo curso
- `PUT /api/cursos/{id}` - Atualizar curso
- `DELETE /api/cursos/{id}` - Deletar curso

### Instrutores
- `GET /api/instrutores` - Listar todos os instrutores
- `GET /api/instrutores/{id}` - Buscar instrutor por ID
- `POST /api/instrutores` - Criar novo instrutor
- `POST /api/instrutores/{instrutorId}/cursos/{cursoId}` - Vincular curso ao instrutor
- `PUT /api/instrutores/{id}` - Atualizar instrutor
- `DELETE /api/instrutores/{id}` - Deletar instrutor

### Inscrições
- `GET /api/alunos/{alunoId}/inscricoes` - Listar inscrições de um aluno
- `POST /api/alunos/{alunoId}/inscricoes/{cursoId}` - Criar nova inscrição
- `DELETE /api/alunos/{alunoId}/inscricoes/{inscricaoId}` - Cancelar inscrição

### Pagamentos
- `GET /api/pagamentos` - Listar todos os pagamentos
- `GET /api/pagamentos/{id}` - Buscar pagamento por ID
- `POST /api/pagamentos` - Processar novo pagamento
- `PUT /api/pagamentos/{id}/aprovar` - Aprovar pagamento
- `PUT /api/pagamentos/{id}/recusar` - Recusar pagamento
- `DELETE /api/pagamentos/{id}` - Deletar pagamento

## Banco de Dados

### H2 Console (Desenvolvimento)

Acesse o console do H2 em: **http://localhost:8080/h2-console**

Configurações de conexão:
- **JDBC URL:** `jdbc:h2:file:./data/plataformacursos`
- **Username:** `SA`
- **Password:** (deixe em branco)

### MySQL (Produção)

Para usar MySQL em produção, atualize o `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plataformacursos
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

## Modelo de Dados

### Principais Entidades

- **Aluno**: Gerencia informações dos estudantes
- **Instrutor**: Gerencia informações dos professores
- **Curso**: Contém detalhes dos cursos oferecidos
- **Inscricao**: Relaciona alunos com cursos
- **Pagamento**: Controla transações financeiras

### Status de Inscrição

- `PENDENTE` - Inscrito mas não pagou
- `PAGO` - Pagamento confirmado
- `CONFIRMADA` - Inscrição confirmada pelo sistema
- `CANCELADA` - Cancelada pelo aluno
- `REEMBOLSADA` - Reembolso processado
- `CONCLUIDA` - Curso finalizado

## Segurança

O projeto utiliza Spring Security para:
- Autenticação de usuários
- Autorização baseada em roles
- Proteção contra CSRF
- Criptografia de senhas

## Testes

Execute os testes com:

```bash
# No Windows
.\mvnw.cmd test

# No Linux/Mac
./mvnw test
```

## Regras de Negócio

### Inscrições
- Aluno pode ter no máximo 5 cursos ativos simultaneamente
- Curso possui limite de vagas configurável
- Sistema controla automaticamente disponibilidade de vagas

### Pagamentos
- Reembolso permitido apenas se o curso não começou
- Pagamento deve ser aprovado para confirmar inscrição

### Transferências
- Aluno pode transferir para outro curso antes do início
- Sistema valida disponibilidade de vagas no curso destino

## Contribuindo

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto foi desenvolvido para fins educacionais.

## Equipe

**Grupo 04** - Projeto de Programação N02

Rayelen Oliveira
Ana Sofia
Ianca
Leo
Heitor

## Suporte

Para reportar bugs ou solicitar features, abra uma [issue](https://github.com/yellenbr/Projeto-de-Programa-o_N02_Grupo04/issues).

---

Desenvolvido com Spring Boot e Java 17
