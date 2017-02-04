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


    public void jogar() {
        double vontade = 0.5; //Atributo que controla a chance de atacar
        int n_terrestres;
        int qtd_ataque = 0;
        int i = 0;


        List<Territorio> territorios = game_.getTerritorios(this);
        List<Territorio> fronteiras = new ArrayList<>();


        Collections.sort(territorios, exTerrestreComparator);
        Collections.reverse(territorios);


        while (vontade > agressividade_) {
            fronteiras = new ArrayList<Territorio>(Arrays.asList(territorios.get(i).getFronteira()));
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

                //Ataca?

            } else {
                vontade -= 0.2;
            }


            //CHAMAR A DEFESA DO HUMANO// gui_.defender(territorio, alvo, qtd_ataque); //Chama janela de defesa.

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
