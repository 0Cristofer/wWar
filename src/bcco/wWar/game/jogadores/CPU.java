package bcco.wWar.game.jogadores;

//Imports próprios
import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;

import java.util.*;

/** Controla o jogador CPU
 * @author Bruno Cesar e Cristofer Oswald
 * @since 02/02/17
 */
public class CPU extends Jogador {
    private static List<String> nomesCPU_ = new ArrayList<>();

    private double agressividade_; //Atributo que controla a chance de ataque da CPU
    private double vontade_;
    private Comparator<Territorio> ex_terrestre_comparator_;

    /**
     * Cria um um jogador CPU
     * @param game Objeto do jogo
     */
    public CPU(Game game, double agressividade) {
        Random r = new Random();

        nome_ = nomesCPU_.get(r.nextInt(nomesCPU_.size()));
        game_ = game;
        agressividade_ = agressividade;
        ex_terrestre_comparator_ = new ExercitoTerrestreComparator();
        vontade_ = 0.5;
    }

    /**
     * Insere um nome na lista de nomes possíveis para a cpu
     * @param nome O nome a ser adicionado
     */
    static public void insertNomeCPU(String nome) {
        nomesCPU_.add(nome);
    }

    /**
     * Distribui os exércitos da CPU
     */
    public void distribuirExercitos() {
        Random rng = new Random();
        List<Territorio> territorios = game_.getTerritorios(this);
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
     * Faz uma jogada da CPU
     */
    public void jogar() {
        //TODO implementar um sistema melhor, incluir o movimentar.
        if (vontade_ > agressividade_) {
            if (!atacar(0, 0)) {
                game_.mudaRodada();
            }
        } else {
            game_.mudaRodada();
        }
    }

    /**
     * Algoritmo de defesa de território da CPU
     * @param territorio Território a ser defendido
     * @param qtd_ataque Quantidade de tropas atacando
     * @return A quantidade de tropas que serão utilizadas para defesa
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

    /**
     * Algoritmo que decide os territórios que serão atacados pela CPU
     * @param i Index de territórios
     * @param j Index da fronteira do território respectivo
     * @return True se o ataque foi sucedido
     */
    private boolean atacar(int i, int j) {
        int n_terrestres;
        int qtd_ataque;

        List<Territorio> territorios = game_.getTerritorios(this);
        List<Territorio> fronteiras;

        if (i > territorios.size()) {
            return false;
        }

        territorios.sort(ex_terrestre_comparator_);
        Collections.reverse(territorios);

        if (vontade_ > agressividade_) {
            fronteiras = territorios.get(i).getFronteirasInimigas(this);

            if (fronteiras.size() == 0) {
                return false;
            }

            fronteiras.sort(ex_terrestre_comparator_);

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
                game_.getGui().defender(territorios.get(i), fronteiras.get(j), qtd_ataque);

            } else {
                vontade_ -= 0.1;
                atacar(i + 1, j);
            }
        }
        return true;
    }

    /**
     * Altera a vontade da CPU com base no sucesso dela
     * @param resultado Resultado do ataque
     * @param n_sucessos Quantidade e sucessos
     * @param n_fracassos Quantidade de fracassos
     */
    public void alterarVontade(boolean resultado, int n_sucessos, int n_fracassos) {
        if (resultado) {
            vontade_ += 0.05 + n_sucessos * 0.05;
        } else {
            vontade_ -= (0.1 + n_fracassos * 0.05);
        }
    }

    /**
     * Reseta o atributo vontade para o padrão de inicio de rodada
     */
    public void resetarVontade() {
        vontade_ = 0.5;
    }

    /**
     * Classe que implementa o comparador para objetos do tipo Exercito Terrestre
     */
    class ExercitoTerrestreComparator implements Comparator<Territorio> {

        public int compare(Territorio t1, Territorio t2) {
            return t1.getNumExTerrestres() - t2.getNumExTerrestres(); //Ascendente
        }

    }
}
