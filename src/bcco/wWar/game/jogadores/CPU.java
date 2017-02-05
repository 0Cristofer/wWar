package bcco.wWar.game.jogadores;

//Imports próprios

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;

import java.util.*;

//Imports do sistema

/** Controla o jogador CPU
 * @author Bruno Cesar
 * @author Cristofer Oswald
 * @since 02/02/17
 */
public class CPU extends Jogador {
    private static List<String> nomesCPU_ = new ArrayList<>();

    private double agressividade_; //Atributo que controla a chance de ataque da CPU
    private double vontade_;
    private Comparator<Territorio> ex_terrestre_comparator_;
    private Comparator<Territorio> ex_aereo_comparator_;
    private Comparator<Territorio> qtd_fronteiras_comparator_;

    /**
     * Cria um um jogador CPU
     * @param game Objeto do jogo
     * @param agressividade A agressividade da CPU
     */
    public CPU(Game game, double agressividade) {
        Random r = new Random();

        vontade_ = 0.6; //Pode ser um parametro
        nome_ = nomesCPU_.get(r.nextInt(nomesCPU_.size()));
        game_ = game;
        agressividade_ = agressividade;
        ex_terrestre_comparator_ = new ExercitoTerrestreComparator();
        ex_aereo_comparator_ = new ExercitoAereoComparator();
        qtd_fronteiras_comparator_ = new QtdFronteirasComparator();
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
        System.out.println("log - Distribuição exército CPU\n" + terrestres_recebidos_ +
                " terrestres recebidos\n" + aereos_recebidos_ + " aereos recebidos");

        Random rng = new Random();
        int qtd_add_t = rng.nextInt(terrestres_recebidos_ / 2) + 1;
        int qtd_add_a = rng.nextInt(aereos_recebidos_ / 2) + 1;

        terrestres_recebidos_ -= qtd_add_t;
        aereos_recebidos_ -= qtd_add_a;

        List<Territorio> territorios = game_.getTerritorios(this);

        boolean r = rng.nextBoolean();

        if (r){
            //Distribuição por quantidade de fronteiras
            System.out.println("log - Distribuição território CPU por quantidade de fronteiras");
            territorios.sort(qtd_fronteiras_comparator_);
            Collections.reverse(territorios);

            for (Territorio t : territorios) {
                for (int i = 0; i < qtd_add_t; i++) {
                    t.insereExTerrestre();
                    System.out.println("Inseriu terrestre em " + t.getNome());
                }

                for (int i = 0; i < qtd_add_a; i++) {
                    t.insereExAereo();
                    System.out.println("Inseriu aereo em " + t.getNome());
                }

                if (terrestres_recebidos_ == 0 && aereos_recebidos_ == 0) {
                    break;
                }

                qtd_add_t = rng.nextInt(terrestres_recebidos_);
                qtd_add_a = rng.nextInt(aereos_recebidos_);

                terrestres_recebidos_ -= qtd_add_t;
                aereos_recebidos_ -= qtd_add_a;
            }

        } else {
            //Distribuição buscando equilibrio
            System.out.println("log - Distribuição território CPU buscando equilibrio");

            territorios.sort(ex_terrestre_comparator_);

            for (Territorio t : territorios) {
                for (int i = 0; i < qtd_add_t; i++) {
                    t.insereExTerrestre();
                    System.out.println("Inseriu terrestre em " + t.getNome());

                }

                if (terrestres_recebidos_ == 0) {
                    break;
                }

                qtd_add_t = rng.nextInt(terrestres_recebidos_);
                terrestres_recebidos_ -= qtd_add_t;

            }

            territorios.sort(ex_aereo_comparator_);

            for (Territorio t : territorios) {

                for (int i = 0; i < qtd_add_a; i++) {
                    t.insereExAereo();
                    System.out.println("Inseriu aereo em " + t.getNome());
                }

                if (aereos_recebidos_ == 0) {
                    break;
                }

                qtd_add_a = rng.nextInt(aereos_recebidos_);

                aereos_recebidos_ -= qtd_add_a;

            }
        }
    }

    /**
     * Faz uma jogada da CPU
     */
    public void jogar() {
        //TODO implementar um sistema melhor, incluir o movimentar.
        if (vontade_ > agressividade_) {
            atacarAereo();
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
                vontade_ -= 0.01;
                atacar(i + 1, j);
            }
        }
        return true;
    }

    private void atacarAereo(){
        for(Territorio terrs :game_.getTerritorios(this)){
            for(Territorio t : game_.checkAereo(this, terrs)){
                game_.atacarAereo(this, terrs, t, terrs.getNumExAereos());
            }
        }
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
            vontade_ -= (0.05 + n_fracassos * 0.025);
        }
    }

    /**
     * Reseta o atributo vontade para o padrão de inicio de rodada
     */
    public void resetarVontade() {
        vontade_ = 0.6;
    }

    /**
     * Classe que implementa o comparador para objetos do tipo Exercito Terrestre
     */
    class ExercitoTerrestreComparator implements Comparator<Territorio> {

        public int compare(Territorio t1, Territorio t2) {
            return t1.getNumExTerrestres() - t2.getNumExTerrestres(); //Ascendente
        }

    }

    /**
     * Classe que implementa o comparador para objetos do tipo Exercito Aereo
     */
    class ExercitoAereoComparator implements Comparator<Territorio> {

        public int compare(Territorio t1, Territorio t2) {
            return t1.getNumExAereos() - t2.getNumExAereos(); //Ascendente
        }

    }

    /**
     * Classe que implementa o comparador por quantidade de fronteiras
     */
    class QtdFronteirasComparator implements Comparator<Territorio> {

        public int compare(Territorio t1, Territorio t2) {
            return t1.getFronteirasInimigas(game_.getCPU()).size() -
                    t2.getFronteirasInimigas(game_.getCPU()).size(); //Ascendente
        }
    }

}
