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

    public void distribuirExercitos() {
        List<Territorio> territorios = game_.getTerritorios(this);
        Random rng = new Random();
        int i;

        while (terrestres_recebidos_ > 0) {
            i = rng.nextInt(territorios.size());
            territorios.get(i).insereExTerrestre();
            terrestres_recebidos_--;
        }

        while (aereos_recebidos_ > 0) {
            i = rng.nextInt(territorios.size());
            territorios.get(i).insereExAereo();
            aereos_recebidos_--;
        }

        /* TODO IMPLEMENTAR MÉTODO NÃO ALEATÓRIO
        boolean r = new Random().nextBoolean();

        if (r){
            //Distribuição por quantidade de fronteiras

        } else {
            //Distribuição buscando equilibrio

            for (Territorio t: territorios) {
                valoresTerr.add(t.getNumExTerrestres());
                valoresAereo.add(t.getNumExAereos());
            }

        }*/

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
