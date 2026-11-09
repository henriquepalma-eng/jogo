-- ========================================================
-- PROJETO: MESTRE DA COZINHA
-- AUTOR: Henrique Jara Palma
-- DISCIPLINA: Tópicos em Linguagem de Programação 2
-- INSTITUIÇÃO: IFMS
-- ========================================================

-- 1. Criação da Tabela de Jogadores
CREATE TABLE IF NOT EXISTS jogadores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    moedas INT NOT NULL DEFAULT 50,
    pontos_reputacao INT NOT NULL DEFAULT 0,
    nivel INT NOT NULL DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Criação da Tabela de Definição de Upgrades (Melhorias Permanentes)
CREATE TABLE IF NOT EXISTS upgrades (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    custo_base INT NOT NULL,
    nivel_maximo INT NOT NULL DEFAULT 5
);

-- 3. Tabela de Ligação: Upgrades adquiridos por cada Jogador
CREATE TABLE IF NOT EXISTS jogador_upgrades (
    id SERIAL PRIMARY KEY,
    jogador_id INT NOT NULL REFERENCES jogadores(id) ON DELETE CASCADE,
    upgrade_id INT NOT NULL REFERENCES upgrades(id) ON DELETE CASCADE,
    nivel_atual INT NOT NULL DEFAULT 0,
    CONSTRAINT uk_jogador_upgrade UNIQUE (jogador_id, upgrade_id)
);

-- 4. Tabela de Definição de Itens (Loja e Inventário)
CREATE TABLE IF NOT EXISTS itens (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    preco INT NOT NULL,
    tipo VARCHAR(20) NOT NULL -- 'CONSUMIVEL' ou 'EQUIPAMENTO'
);

-- 5. Tabela do Inventário do Jogador (Stage separado do jogo)
CREATE TABLE IF NOT EXISTS jogador_inventario (
    id SERIAL PRIMARY KEY,
    jogador_id INT NOT NULL REFERENCES jogadores(id) ON DELETE CASCADE,
    item_id INT NOT NULL REFERENCES itens(id) ON DELETE CASCADE,
    quantidade INT NOT NULL DEFAULT 1,
    CONSTRAINT uk_jogador_item UNIQUE (jogador_id, item_id)
);

-- 6. Tabela de Partidas (Histórico e Ranking)
CREATE TABLE IF NOT EXISTS partidas (
    id SERIAL PRIMARY KEY,
    jogador_id INT NOT NULL REFERENCES jogadores(id) ON DELETE CASCADE,
    pontuacao INT NOT NULL,
    clientes_atendidos INT NOT NULL DEFAULT 0,
    moedas_ganhas INT NOT NULL DEFAULT 0,
    derrotou_boss BOOLEAN NOT NULL DEFAULT FALSE,
    data_partida TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserção de Upgrades Iniciais do Jogo
INSERT INTO upgrades (chave, nome, descricao, custo_base, nivel_maximo) VALUES
('fogao_rapido', 'Fogão Industrial', 'Acelera o tempo de cozimento e preparo dos pratos em 15% por nível.', 100, 5),
('paciencia_cliente', 'Ambiente Confortável', 'Aumenta a paciência inicial dos clientes do restaurante em 20%.', 80, 5),
('gorjeta_extra', 'Charme do Garçom', 'Aumenta o valor das gorjetas recebidas em 25% por nível.', 120, 5),
('capacidade_mesas', 'Expansão do Salão', 'Permite atender mais clientes simultaneamente na tela de jogo.', 150, 3)
ON CONFLICT (chave) DO NOTHING;

-- Inserção de Itens Iniciais da Loja / Inventário
INSERT INTO itens (chave, nome, descricao, preco, tipo) VALUES
('cafe_espresso', 'Café Expresso Turbo', 'Consumível: Dá supervelocidade temporária ao chef por 15 segundos.', 30, 'CONSUMIVEL'),
('sobremesa_cortesia', 'Sobremesa de Cortesia', 'Consumível: Restaura instantaneamente 50% da paciência de um cliente.', 25, 'CONSUMIVEL'),
('tempero_dourado', 'Tempero Gourmet Dourado', 'Consumível: O próximo prato servido rende 2x mais moedas e reputação.', 40, 'CONSUMIVEL'),
('faca_chef', 'Faca de Prata do Chef', 'Equipamento: Melhora a precisão e reduz falhas de montagem.', 100, 'EQUIPAMENTO')
ON CONFLICT (chave) DO NOTHING;

-- Inserção de Jogador Padrão para testes imediatos
INSERT INTO jogadores (nome, moedas, pontos_reputacao, nivel)
VALUES ('Chef Henrique', 150, 80, 2)
ON CONFLICT (nome) DO NOTHING;

-- Inserção de dados iniciais de Ranking (Exemplo de demonstração)
INSERT INTO partidas (jogador_id, pontuacao, clientes_atendidos, moedas_ganhas, derrotou_boss)
SELECT id, 1250, 18, 240, false FROM jogadores WHERE nome = 'Chef Henrique' LIMIT 1;
