package com.mycompany.mavenproject1.util;

import com.mycompany.mavenproject1.dao.JogadorDAO;
import com.mycompany.mavenproject1.dao.UpgradeDAO;
import com.mycompany.mavenproject1.model.Jogador;
import com.mycompany.mavenproject1.model.Upgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contexto global da aplicação para compartilhamento do estado da sessão.
 */
public class GameContext {

    private static GameContext instance;
    private Jogador jogadorLogado;
    private final Map<String, Integer> upgradesNiveis = new HashMap<>();

    private final JogadorDAO jogadorDAO = new JogadorDAO();
    private final UpgradeDAO upgradeDAO = new UpgradeDAO();

    private GameContext() {
        // Inicializa com jogador padrão ou busca no banco
        inicializarJogador("Chef Henrique");
    }

    public static synchronized GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }

    public void inicializarJogador(String nome) {
        this.jogadorLogado = jogadorDAO.buscarOuCriar(nome);
        recarregarUpgrades();
    }

    public void recarregarUpgrades() {
        upgradesNiveis.clear();
        if (jogadorLogado != null) {
            List<Upgrade> lista = upgradeDAO.listarPorJogador(jogadorLogado.getId());
            for (Upgrade u : lista) {
                upgradesNiveis.put(u.getChave(), u.getNivelAtual());
            }
        }
    }

    public int getNivelUpgrade(String chave) {
        return upgradesNiveis.getOrDefault(chave, 0);
    }

    public boolean isBossDesbloqueado() {
        if (jogadorLogado == null) return false;
        // Boss desbloqueia a partir do nível 2 ou 200 pontos de reputação
        return jogadorLogado.getNivel() >= 2 || jogadorLogado.getPontosReputacao() >= 200;
    }

    public void salvarProgressoJogador() {
        if (jogadorLogado != null) {
            jogadorDAO.atualizar(jogadorLogado);
        }
    }

    public Jogador getJogadorLogado() {
        return jogadorLogado;
    }

    public void setJogadorLogado(Jogador jogadorLogado) {
        this.jogadorLogado = jogadorLogado;
        recarregarUpgrades();
    }
}
