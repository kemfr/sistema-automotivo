CREATE DATABASE IF NOT EXISTS sistema_automotivo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE sistema_automotivo;

CREATE TABLE IF NOT EXISTS marcas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_marcas_nome UNIQUE (nome)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS modelos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    marca_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_modelos_marca_id (marca_id),
    CONSTRAINT uk_modelos_nome_marca UNIQUE (nome, marca_id),
    CONSTRAINT fk_modelos_marca
        FOREIGN KEY (marca_id) REFERENCES marcas (id)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS veiculos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    modelo_id BIGINT NOT NULL,
    ano_fabricacao INT NOT NULL,
    cor VARCHAR(50) NOT NULL,
    preco DECIMAL(12, 2) NOT NULL,
    quilometragem INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_veiculos_modelo_id (modelo_id),
    INDEX idx_veiculos_ano_fabricacao (ano_fabricacao),
    INDEX idx_veiculos_preco (preco),
    INDEX idx_veiculos_status (status),
    CONSTRAINT chk_veiculos_ano_fabricacao
        CHECK (ano_fabricacao BETWEEN 1886 AND 2100),
    CONSTRAINT chk_veiculos_preco
        CHECK (preco >= 0),
    CONSTRAINT chk_veiculos_quilometragem
        CHECK (quilometragem >= 0),
    CONSTRAINT chk_veiculos_status
        CHECK (status IN ('DISPONIVEL', 'VENDIDO', 'DESCONTINUADO')),
    CONSTRAINT fk_veiculos_modelo
        FOREIGN KEY (modelo_id) REFERENCES modelos (id)
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
