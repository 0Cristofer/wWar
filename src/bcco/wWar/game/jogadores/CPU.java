package bcco.wWar.game.jogadores;

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bruno on 02/02/17.
 */
public class CPU extends Jogador {
    private static List<String> nomesCPU_ = new ArrayList<>();


    /**
     * Cria um um jogador CPU
     *
     * @param game Objeto do jogo
     */
    public CPU(Game game) {
        Random r = new Random();
        String r_nome = nomesCPU_.get(r.nextInt(nomesCPU_.size()));
        nome_ = r_nome;
        game_ = game;
    }

    public int defenderTerr(Territorio territorio, int qtd_ataque) {
        int n_defesa = territorio.getNumExTerrestres();

        if (n_defesa == 1) {
            return 1;
        } else {
            //Algoritmo heuristico fodão de decisão
            if (n_defesa > qtd_ataque) {
                if (n_defesa == 2) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                if (n_defesa == 2) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
    }

    /**
     * Insere um nome na lista de nomes possíveis para a cpu
     *
     * @param nome O nome a ser adicionado
     */
    static public void insertNomeCPU(String nome) {
        nomesCPU_.add(nome);
    }
}
