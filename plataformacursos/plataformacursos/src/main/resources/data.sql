-- Dados iniciais para testes

-- Inserir Instrutores
INSERT INTO instrutores (nome, email, especialidade) VALUES
('João Silva', 'joao.silva@example.com', 'Desenvolvimento Web'),
('Maria Santos', 'maria.santos@example.com', 'Data Science'),
('Pedro Oliveira', 'pedro.oliveira@example.com', 'DevOps');

-- Inserir Cursos
INSERT INTO cursos (nome, descricao, preco, carga_horaria, instrutor_id) VALUES
('Java Spring Boot', 'Curso completo de Spring Boot para desenvolvimento de APIs REST', 299.90, 40, 1),
('Python para Data Science', 'Análise de dados e machine learning com Python', 399.90, 60, 2),
('Docker e Kubernetes', 'Containerização e orquestração de aplicações', 349.90, 50, 3),
('React.js Avançado', 'Desenvolvimento de aplicações web modernas com React', 279.90, 35, 1);

-- Inserir Alunos
INSERT INTO alunos (nome, email, cpf) VALUES
('Carlos Mendes', 'carlos.mendes@example.com', '123.456.789-00'),
('Ana Paula', 'ana.paula@example.com', '987.654.321-00'),
('Roberto Costa', 'roberto.costa@example.com', '456.789.123-00');

-- Inserir Inscrições
INSERT INTO inscricoes (aluno_id, curso_id, data_inscricao, status) VALUES
(1, 1, '2025-10-01 10:00:00', 'CONFIRMADA'),
(1, 2, '2025-10-05 14:30:00', 'PENDENTE'),
(2, 3, '2025-10-10 09:15:00', 'CONFIRMADA'),
(3, 1, '2025-10-15 16:45:00', 'CONFIRMADA');

-- Inserir Pagamentos
INSERT INTO pagamentos (inscricao_id, valor, data_pagamento, metodo_pagamento, status) VALUES
(1, 299.90, '2025-10-01 10:15:00', 'PIX', 'APROVADO'),
(3, 349.90, '2025-10-10 09:30:00', 'CARTAO_CREDITO', 'APROVADO'),
(4, 299.90, '2025-10-15 17:00:00', 'BOLETO', 'PENDENTE');
