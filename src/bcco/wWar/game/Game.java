package bcco.wWar.game;

import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.Continente;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import java.util.Random;

/** Representa um jogo pronto para ser utilizado
 * @author Cristofer Oswald
 * @since 29/01/17
 */

public class Game {
    private Mapa mapa_;

    private Jogador humano_;
    private Jogador pc_;

    private boolean running_ = false;
    private GameStates state_;
    private int rodada_;

    /**
     * Contrói a instância do jogo com o mapa construído e os nomes dos jogadores
     * @param mapa O mapa construído a partir do arquivo
     * @param nome_humano O nome do jogador humano
     * @param nome_pc O nome do jogador PC
     */
    public Game(Mapa mapa, String nome_humano, String nome_pc){
        mapa_ = mapa;

        humano_ = new Jogador(nome_humano, false);
        pc_ = new Jogador(nome_pc, true);

        state_ = GameStates.INICIO;

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

            for (int j = 0; j < continente.numTerritorios(); j++) {
                Territorio territorio = null;
                try {
                    territorio = continente.getTerritorio(j);
                } catch (ContinenteException e) {
                    e.printStackTrace();
                }
                if (random.nextInt(2) == 0) {
                    territorio.setOcupante_(humano_);
                } else {
                    territorio.setOcupante_(pc_);
                }
            }

        }
    }

    public void mudaRodada(){
        rodada_++;
    }

    /**
     * Configura um novo jogo
     */
    public void iniciarJogo(){
        rodada_ = 0;
        distribuiTerritorios();
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