MERGE INTO instrutor (id, nome, email, cpf, senha, especialidade) KEY(id) VALUES
(1, 'Instrutor de Teste', 'instrutor@teste.com', '22222222222', 'senha123', 'Tecnologia');

MERGE INTO aluno (id, nome, email, cpf) KEY(id) VALUES
(1, 'Aluno de Teste', 'aluno@teste.com', '11111111111');
