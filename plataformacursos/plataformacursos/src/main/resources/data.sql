-- ========================================
-- DADOS INICIAIS PARA TESTES
-- Plataforma de Cursos - Sistema de Gestão
-- ========================================

-- ========================================
-- 1. INSTRUTORES
-- ========================================
INSERT INTO instrutores (id, nome, email, especialidade) VALUES
(1, 'João Silva', 'joao.silva@veridia.com', 'Desenvolvimento Backend'),
(2, 'Maria Santos', 'maria.santos@veridia.com', 'Data Science e IA'),
(3, 'Pedro Oliveira', 'pedro.oliveira@veridia.com', 'DevOps e Cloud'),
(4, 'Ana Rodrigues', 'ana.rodrigues@veridia.com', 'Frontend e UX');

-- ========================================
-- 2. CURSOS (vinculados aos instrutores)
-- ========================================
INSERT INTO cursos (id, nome, descricao, preco, carga_horaria, vagas, ativo, instrutor_id) VALUES
(1, 'Java Spring Boot Completo', 'Curso completo de Spring Boot para desenvolvimento de APIs REST modernas com boas práticas', 299.90, 40, 60, true, 1),
(2, 'Python para Data Science', 'Análise de dados, visualização e machine learning com Python, Pandas e Scikit-learn', 399.90, 60, 60, true, 2),
(3, 'Docker e Kubernetes', 'Containerização e orquestração de aplicações em ambientes de produção', 349.90, 50, 60, true, 3),
(4, 'React.js Avançado', 'Desenvolvimento de aplicações web modernas e escaláveis com React e Redux', 279.90, 35, 60, true, 4),
(5, 'DevOps com AWS', 'Infraestrutura como código, CI/CD e automação na Amazon Web Services', 449.90, 55, 60, true, 3),
(6, 'Node.js e Express', 'Desenvolvimento backend com JavaScript e Node.js', 259.90, 30, 60, false, 1);

-- ========================================
-- 3. ALUNOS
-- ========================================
INSERT INTO alunos (id, nome, email, cpf) VALUES
(1, 'Carlos Mendes', 'carlos.mendes@email.com', '12345678900'),
(2, 'Ana Paula Costa', 'ana.paula@email.com', '98765432100'),
(3, 'Roberto Silva', 'roberto.silva@email.com', '45678912300'),
(4, 'Juliana Ferreira', 'juliana.ferreira@email.com', '78912345600'),
(5, 'Marcos Antonio', 'marcos.antonio@email.com', '32165498700');

-- ========================================
-- 4. INSCRIÇÕES
-- ========================================
INSERT INTO inscricoes (id, aluno_id, curso_id, data_inscricao, status) VALUES
-- Carlos está em 2 cursos confirmados
(1, 1, 1, '2025-10-01 10:00:00', 'PAGO'),
(2, 1, 3, '2025-10-05 14:30:00', 'PAGO'),

-- Ana está inscrita mas ainda não pagou
(3, 2, 2, '2025-10-08 09:15:00', 'PENDENTE'),

-- Roberto tem 1 curso confirmado e 1 pendente
(4, 3, 1, '2025-10-10 11:20:00', 'PAGO'),
(5, 3, 4, '2025-10-12 16:45:00', 'PENDENTE'),

-- Juliana tem cursos confirmados
(6, 4, 2, '2025-10-15 08:30:00', 'PAGO'),
(7, 4, 5, '2025-10-18 13:00:00', 'PAGO'),

-- Marcos está em vários cursos
(8, 5, 1, '2025-10-20 10:00:00', 'PAGO'),
(9, 5, 3, '2025-10-21 14:00:00', 'CONFIRMADA'),
(10, 5, 4, '2025-10-22 09:00:00', 'PENDENTE');

-- ========================================
-- 5. PAGAMENTOS (apenas para inscrições PAGAS)
-- ========================================
INSERT INTO pagamentos (id, inscricao_id, valor, data_pagamento, metodo_pagamento, status) VALUES
-- Pagamentos de Carlos
(1, 1, 299.90, '2025-10-01 10:15:00', 'PIX', 'APROVADO'),
(2, 2, 349.90, '2025-10-05 14:45:00', 'CARTAO_CREDITO', 'APROVADO'),

-- Pagamento de Roberto
(3, 4, 299.90, '2025-10-10 11:30:00', 'BOLETO', 'APROVADO'),

-- Pagamentos de Juliana
(4, 6, 399.90, '2025-10-15 08:45:00', 'PIX', 'APROVADO'),
(5, 7, 449.90, '2025-10-18 13:15:00', 'CARTAO_CREDITO', 'APROVADO'),

-- Pagamento de Marcos
(6, 8, 299.90, '2025-10-20 10:20:00', 'PIX', 'APROVADO');

-- ========================================
-- RESUMO DOS DADOS
-- ========================================
-- ✅ 4 Instrutores cadastrados
-- ✅ 6 Cursos (5 ativos, 1 inativo)
-- ✅ 5 Alunos cadastrados
-- ✅ 10 Inscrições (6 pagas, 1 confirmada, 3 pendentes)
-- ✅ 6 Pagamentos aprovados
-- 
-- 🎯 Cenários de teste cobertos:
-- - Aluno com múltiplos cursos pagos
-- - Aluno com inscrição pendente de pagamento
-- - Curso com múltiplos alunos inscritos
-- - Instrutor com múltiplos cursos
-- - Diferentes métodos de pagamento (PIX, Cartão, Boleto)
-- - Curso inativo (Node.js)
-- ========================================

