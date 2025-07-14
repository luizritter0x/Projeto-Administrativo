CREATE DATABASE dbluiz;
USE dbluiz;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE keys_registro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chave VARCHAR(255) NOT NULL UNIQUE,
    usada BOOLEAN DEFAULT FALSE
);

DROP TABLE IF EXISTS keys_registro;
DROP TABLE IF EXISTS usuarios;

INSERT INTO usuarios (nome, usuario, senha, is_admin) VALUES
('admin', 'admin', '123', TRUE);

select * from usuarios;
select * from keys_registro;

CREATE TABLE chamados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    usuario VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE mensagens_chamado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chamado_id INT NOT NULL,
    autor VARCHAR(255) NOT NULL,
    texto TEXT NOT NULL,
    data_hora DATETIME NOT NULL,
    FOREIGN KEY (chamado_id) REFERENCES chamados(id)
);

CREATE TABLE mensagens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chamado_id INT NOT NULL,
    autor VARCHAR(255) NOT NULL,
    texto TEXT NOT NULL,
    dataHora TIMESTAMP NOT NULL,
    FOREIGN KEY (chamado_id) REFERENCES chamados(id)
);

CREATE TABLE avisos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data_publicacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    criado_por VARCHAR(100) NOT NULL
);
