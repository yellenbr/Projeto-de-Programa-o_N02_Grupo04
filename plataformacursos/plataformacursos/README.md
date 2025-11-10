# Plataforma Veridia Cursos

## Objetivo do Módulo Desenvolvido

Sistema completo de gerenciamento de cursos online que permite:

- **Gestão de Alunos**: Cadastro, autenticação e gerenciamento de perfil de estudantes
- **Gestão de Instrutores**: Cadastro e administração de professores e seus cursos
- **Gestão de Cursos**: Criação, edição e controle de cursos com limite de vagas
- **Sistema de Inscrições**: Processo completo de matrícula com controle de vagas e validações
- **Processamento de Pagamentos**: Sistema de pagamento com múltiplos métodos (PIX, Cartão, Boleto)
- **Transferências e Reembolsos**: Regras de negócio para mudança de curso e cancelamento
- **Interface Web Responsiva**: Frontend completo com HTML, CSS e JavaScript vanilla

## Bibliotecas Utilizadas

### Backend
- **Spring Boot 3.5.6** - Framework principal para desenvolvimento da aplicação
- **Spring Data JPA** - Abstração para persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping) para mapeamento objeto-relacional
- **H2 Database 2.3.232** - Banco de dados em memória para desenvolvimento
- **Jackson** - Serialização/deserialização JSON
- **Lombok** - Redução de código boilerplate através de anotações
- **Maven** - Gerenciamento de dependências e build

### Frontend
- **HTML5** - Estrutura das páginas web
- **CSS3** - Estilização e design responsivo
- **JavaScript (ES6+)** - Lógica do frontend e comunicação com API REST
- **Fetch API** - Requisições HTTP assíncronas

### Ferramentas de Desenvolvimento
- **Java 17 LTS** - Linguagem de programação
- **Maven Wrapper** - Gerenciador de build incluído no projeto
- **Git** - Controle de versão

## Instruções de Execução

### Pré-requisitos
- Java JDK 17 ou superior instalado
- Maven 3.8+ (ou use o Maven Wrapper incluído)
- Navegador web moderno (Chrome, Firefox, Edge)
- Git para clonar o repositório

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/yellenbr/Projeto-de-Programa-o_N02_Grupo04.git
cd Projeto-de-Programa-o_N02_Grupo04/plataformacursos/plataformacursos
```

2. **Compile o projeto**
```bash
# Windows
.\mvnw.cmd clean compile

# Linux/Mac
./mvnw clean compile
```

3. **Execute a aplicação**
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

4. **Acesse a aplicação**
- Abra o navegador e acesse: **http://localhost:8080**
- Para acessar o console do banco H2: **http://localhost:8080/h2-console**
  - JDBC URL: `jdbc:h2:file:./data/plataformacursos`
  - Username: `sa`
  - Password: (deixe em branco)

### Credenciais de Teste

**Aluno:**
- Email: `joao.santos@email.com`
- Senha: `senha123`

**Instrutor:**
- Email: `carlos.silva@email.com`
- Senha: `senha123`

## Responsabilidades de Cada Integrante

### Ianca Carolinne Carregosa Silva
- **Controllers**: Desenvolvimento de todos os controllers REST (AlunoController, CursoController, InstrutorController, PagamentoController, AlunoFluxoController, TesteController)
- **Documentação**: Contribuição na documentação técnica e guias de uso

### Ana Sofia Ribeiro de Meneses e Rocha Almeida
- **Services**: Implementação da camada de serviços com lógica de negócio (AlunoService, CursoService, InstrutorService, InscricaoService, PagamentoService)
- **Documentação**: Elaboração de documentação de arquitetura e fluxos do sistema

### Luan
- **Repository**: Criação das interfaces de repositório JPA (AlunoRepository, CursoRepository, InstrutorRepository, InscricaoRepository, PagamentoRepository)
- **Documentação**: Documentação do modelo de dados e queries

### Aluno 2
- **Resources**: Desenvolvimento dos recursos estáticos (HTML, CSS, JavaScript) e arquivos de configuração (application.properties, data.sql)
- **Documentação**: Documentação de frontend e guia de interface

### Aluno 3
- **DTOs**: Criação dos Data Transfer Objects (ReembolsoDTO, TransferenciaDTO)
- **Exception**: Implementação do sistema de tratamento de exceções (GlobalExceptionHandler, NegocioException, RecursoNaoEncontradoException, ValidacaoException)
- **Documentação**: Documentação de exceções e tratamento de erros

### Rayelen de Jesus Oliveira
- **Models**: Modelagem e implementação das entidades JPA (Aluno, Curso, Instrutor, Inscricao, Pagamento) com regras de negócio
- **Banco de Dados**: Configuração e estruturação do banco de dados H2, incluindo scripts SQL e relacionamentos
- **Testes**: Criação e execução de testes unitários e de integração
- **README**: Elaboração deste arquivo README.md com toda documentação do projeto

## Prints ou Exemplos de Saída

### Tela de Login
- Interface de autenticação com seleção de tipo de usuário (Aluno/Instrutor)
- Validação de credenciais e redirecionamento para dashboard apropriado

### Dashboard do Aluno
- **Meus Dados**: Estatísticas de cursos inscritos, concluídos e gasto total
- **Cursos Disponíveis**: Catálogo de cursos com filtros e botão de inscrição
- **Minhas Inscrições**: Lista de cursos matriculados com status e ações
- **Meus Pagamentos**: Histórico de transações financeiras

### Dashboard do Instrutor
- **Meus Dados**: Total de cursos, alunos e receita gerada
- **Meus Cursos**: Gerenciamento de cursos criados com botão "Ver Alunos"
- **Criar Curso**: Formulário para cadastro de novos cursos
- **Alunos Inscritos**: Visualização de todos os alunos matriculados

### Funcionalidades Principais
1. **Inscrição em Curso**: Aluno pode se inscrever em cursos disponíveis (máximo 5 ativos)
2. **Pagamento**: Processo de pagamento com seleção de método (PIX, Cartão, Boleto)
3. **Validação de Duplicatas**: Sistema impede inscrição duplicada no mesmo curso
4. **Ver Alunos (Instrutor)**: Modal mostrando lista completa de alunos inscritos por curso
5. **Transferência de Curso**: Aluno pode mudar de curso antes do início
6. **Cancelamento e Reembolso**: Sistema de cancelamento com reembolso automático

### Exemplo de Resposta da API

**GET /api/cursos**
```json
[
  {
    "id": 1,
    "nome": "Java Completo 2025",
    "descricao": "Curso completo de Java",
    "preco": 500.00,
    "cargaHoraria": 120,
    "limiteVagas": 60,
    "numeroInscritos": 3,
    "ativo": true,
    "instrutor": {
      "id": 1,
      "nome": "Carlos Silva"
    }
  }
]
```

**POST /api/alunos/{alunoId}/inscrever/{cursoId}**
```json
{
  "id": 4,
  "aluno": 1,
  "curso": 1,
  "status": "PENDENTE",
  "dataInscricao": "2025-11-10T19:30:00"
}
```

## Estrutura do Código

```
src/
├── main/
│   ├── java/com/veridia/gestao/plataformacursos/
│   │   ├── controller/          # REST Controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Tratamento de exceções
│   │   ├── model/               # Entidades JPA
│   │   ├── repository/          # Interfaces JPA Repository
│   │   └── service/             # Lógica de negócio
│   └── resources/
│       ├── static/              # Frontend (HTML, CSS, JS)
│       ├── application.properties
│       └── data.sql             # Dados iniciais
└── test/                        # Testes automatizados
```

## Regras de Negócio Implementadas

1. **Limite de Cursos por Aluno**: Máximo de 5 cursos ativos simultaneamente
2. **Controle de Vagas**: Validação automática de disponibilidade de vagas
3. **Inscrição Única**: Aluno não pode se inscrever duas vezes no mesmo curso
4. **Reembolso Condicional**: Permitido apenas se o curso não iniciou
5. **Status de Inscrição**: PENDENTE → PAGO → CONFIRMADA → CONCLUIDA
6. **Transferência de Curso**: Validação de vagas no curso destino

---

**Desenvolvido por Grupo 04 - Projeto de Programação N02**

**Tecnologias:** Java 17 | Spring Boot 3.5.6 | H2 Database | HTML5 | CSS3 | JavaScript ES6+


---

**Desenvolvido por Grupo 04 - Projeto de Programação N02**

**Tecnologias:** Java 17 | Spring Boot 3.5.6 | H2 Database | HTML5 | CSS3 | JavaScript ES6+

