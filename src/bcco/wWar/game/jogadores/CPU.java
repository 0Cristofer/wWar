package bcco.wWar.game.jogadores;

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;

import java.util.*;

/**
 * Created by bruno on 02/02/17.
 */
public class CPU extends Jogador {
    private static List<String> nomesCPU_ = new ArrayList<>();
    private double agressividade_; //Atributo que controla a chance de ataque da CPU
    private double vontade_;
    Comparator<Territorio> exTerrestreComparator;


    /**
     * Cria um um jogador CPU
     *
     * @param game Objeto do jogo
     */
    public CPU(Game game, double agressividade) {
        Random r = new Random();
        String r_nome = nomesCPU_.get(r.nextInt(nomesCPU_.size()));
        nome_ = r_nome;
        game_ = game;
        agressividade_ = agressividade;
        exTerrestreComparator = new Exercito_Terrestre_Comparator();
        vontade_ = 0.5;
    }

    class Exercito_Terrestre_Comparator implements Comparator<Territorio> {

        public int compare(Territorio t1, Territorio t2) {
            return t1.getNumExTerrestres() - t2.getNumExTerrestres(); //Ascendente
        }

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

    /**
     * @param territorio
     * @param qtd_ataque
     * @return
     */
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

    public void resetarVontade() {
        vontade_ = 0.5;
    }

    /**
     *
     * @param resultado
     * @param n_sucessos
     * @param n_fracassos
     */
    public void alterarVontade(boolean resultado, int n_sucessos, int n_fracassos) {
        if (resultado) {
            vontade_ += 0.05 + n_sucessos * 0.05;
        } else {
            vontade_ -= (0.1 + n_fracassos * 0.05);
        }
    }



    /**
     *
     * @param i
     * @param j
     */
    public void atacar(int i, int j) {
        int n_terrestres;
        int qtd_ataque = 0;

        List<Territorio> territorios = game_.getTerritorios(this);
        List<Territorio> fronteiras;

        if (i >= territorios.size()) {
            return;
        }

        Collections.sort(territorios, exTerrestreComparator);
        Collections.reverse(territorios);

        if (vontade_ > agressividade_) {
            fronteiras = territorios.get(i).getFronteirasInimigas(this);
            if (fronteiras == null) {
                return;
            }

            Collections.sort(fronteiras, exTerrestreComparator);

            n_terrestres = territorios.get(i).getNumExTerrestres();

            if (n_terrestres > 1) {

                if (n_terrestres > 4) { //possivelmente baseado na agressividade
                    qtd_ataque = 3;
                } else if (n_terrestres > 3) {
                    qtd_ataque = 2;
                } else {
                    qtd_ataque = 1;
                }

                //Atacou
                game_.getGui_().defender(territorios.get(i), fronteiras.get(j), qtd_ataque);

            } else {
                vontade_ -= 0.1;
                atacar(i + 1, j);
            }
        }

    }

    /**
     *
     */
    public void jogar() {
        //TODO implementar um sistema melhor, incluir o movimentar.
        if (vontade_ > agressividade_) {
            atacar(0, 0);
        } else {
            game_.mudaRodada();
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
