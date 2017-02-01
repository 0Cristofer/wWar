package bcco.wWar.game;

import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.Continente;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Representa um jogo pronto para ser utilizado
 * @author Cristofer Oswald
 * @since 29/01/17
 */

public class Game {
    private Mapa mapa_;

    private Jogador humano_;
    private Jogador pc_;
    private List<String> nomesCPU_;

    private boolean running_ = false;
    private GameStates state_;
    private int rodada_;

    /**
     * Constrói a instância do jogo com o mapa construído e os nomes dos jogadores
     * @param mapa O mapa construído a partir do arquivo
     */
    public Game(Mapa mapa){
        mapa_ = mapa;

        nomesCPU_ = new ArrayList<>();

        humano_ = new Jogador(false);

        state_ = GameStates.INICIO;
    }

    /**
     * Cria e configura o jogador CPU
     */
    public void createCPU(){
        pc_ = new Jogador(true);
        pc_.setNome(getrandomNomeCPU());

        //TODO Implementar uma IA maneira :)
    }

    /**
     * Distribui os territorios aleatoria e igualmente
     */
    private void distribuiTerritorios(){
        //iterar sobre continentes x territorios
        //randomizando 0 ou 1 (jogador 0 ou 1)
        //NAO TERMINADO
        Random random = new Random();
        for(int i = 0; i < mapa_.numContinentes(); i++) {
            Continente continente = null;
            try {
                continente = mapa_.getContinente(i);
            } catch (MapaException e) {
                e.printStackTrace();
            }

            if(continente == null){
                return;
            }

            for (int j = 0; j < continente.numTerritorios(); j++) {
                Territorio territorio = null;
                try {
                    territorio = continente.getTerritorio(j);
                } catch (ContinenteException e) {
                    e.printStackTrace();
                }
                if(territorio != null) {
                    if (random.nextInt(2) == 0) {
                        territorio.setOcupante_(humano_);
                    } else {
                        territorio.setOcupante_(pc_);
                    }
                }
            }
        }
    }

    /**
     * Muda a rodada atual para a próxima
     */
    public void mudaRodada(){
        rodada_++;
    }

    /**
     * Configura um novo jogo
     */
    public void iniciarJogo(String nome){
        humano_.setNome(nome);
        rodada_ = 0;
        distribuiTerritorios();
    }

    /**
     * Insere um nome na lista de nomes possíveis para a cpu
     * @param nome O nome a ser adicionado
     */
    public void insertNomeCPU(String nome){
        nomesCPU_.add(nome);
    }

    /**
     * @return O nome escolhido aleatóriamente
     */
    private String getrandomNomeCPU(){
        Random r = new Random();
        return nomesCPU_.get(r.nextInt(nomesCPU_.size()));
    }

    /**
     * @param running O estado atual do jogo
     */
    public void setRunning(boolean running){
        running_ = running;
    }

    /**
     * @param state O estado atual do jogo
     */
    public void setState(GameStates state){
        state_ = state;
    }

    /**
     * Calcula a quantidade de territórios que um jogador possúi
     * @param jogador O jogador a ser verificado
     * @return O número de territórios
     */
    public int getNumTerritorios(Jogador jogador){
        int num = 0;

        for(int i = 0; i < mapa_.numContinentes(); i++){
            try {
                for(int j = 0; j < mapa_.getContinente(i).numTerritorios(); j++){
                    if(mapa_.getTerritorio(i, j).getOcupante() == jogador){
                        num++;
                    }
                }
            } catch (MapaException e) {
                e.printStackTrace();
            }
        }

        return num;
    }

    /**
     * @return O jogador que joga nesta rodada
     */
    public Jogador getJogadorDaVez(){
        if((rodada_ % 2) == 0){
            return humano_;
        }
        return pc_;
    }

    /**
     * @return O estado do jogo
     */
    public boolean getRunning(){
        return running_;
    }

    /**
     * @return A instância do mapa desse jogo
     */
    public Mapa getMapa(){
        return mapa_;
    }

    /**
     * @return O estado atual do jogo
     */
    public GameStates getState(){
        return state_;
    }
}