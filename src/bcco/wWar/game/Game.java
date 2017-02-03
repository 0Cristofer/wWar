package bcco.wWar.game;

import bcco.wWar.game.exercitos.Aereo;
import bcco.wWar.game.exercitos.Terrestre;
import bcco.wWar.game.jogadores.CPU;
import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.gui.Gui;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.Continente;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Representa um jogo pronto para ser utilizado
 * @author Cristofer Oswald
 * @since 29/01/17
 */

public class Game {

    private Mapa mapa_;
    private Gui gui_;

    private Jogador humano_;
    private CPU cpu_;

    private boolean running_ = false;
    private GameStates state_;
    private int rodada_;

    /**
     * Constrói a instância do jogo com o mapa construído e os nomes dos jogadores
     * @param mapa O mapa construído a partir do arquivo
     */
    public Game(Mapa mapa, Gui gui) {
        mapa_ = mapa;

        for(int i = 0; i < mapa_.getNumContinentes(); i++){
            try {
                mapa_.getContinente(i).setFronteira();
            } catch (MapaException e) {
                e.printStackTrace();
            }
        }
        humano_ = new Jogador("", this);
        state_ = GameStates.INICIO;
        gui_ = gui;
    }

    /**
     * Cria e configura o jogador CPU
     */
    public void createCPU(){
        cpu_ = new CPU(this);

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
        for(int i = 0; i < mapa_.getNumContinentes(); i++) {
            Continente continente = null;
            try {
                continente = mapa_.getContinente(i);
            } catch (MapaException e) {
                e.printStackTrace();
            }

            if(continente == null){
                return;
            }

            for (int j = 0; j < continente.getNumTerritorios(); j++) {
                Territorio territorio = null;
                try {
                    territorio = continente.getTerritorio(j);
                } catch (ContinenteException e) {
                    e.printStackTrace();
                }
                if(territorio != null) {
                    if (random.nextInt(2) == 0) {
                        territorio.setOcupante(humano_);
                    } else {
                        territorio.setOcupante(cpu_);
                    }
                }
                if(territorio != null){
                    territorio.insereExTerrestre(new Terrestre());
                    territorio.insereExAereo(new Aereo());
                }
            }
        }
    }

    /**
     * Muda a rodada atual para a próxima
     */
    public void mudaRodada() {
        int n_humano = getNumTerritorios(humano_);
        int n_cpu = getNumTerritorios(cpu_);

        humano_.setTerrestresRecebidos(n_humano/2);
        humano_.setAereosRecebidos(n_humano/3);

        cpu_.setTerrestresRecebidos(n_cpu/2);
        cpu_.setAereosRecebidos(n_cpu/3);

        rodada_++;
    }


    /**
     * Configura um novo jogo
     */
    public void iniciarJogo(String nome){
        humano_.setNome(nome);
        rodada_ = -1;
        distribuiTerritorios();
        mudaRodada();
    }

    /**
     * Recebe uma lista de quantidades de exércitos a serem adicionados aos territórios
     * @param valoresTerr A lista de valores (deve ter a mesmo tamanho da quantidade de territórios)
     */
    public void distribuirExercitos(List<Integer> valoresTerr, List<Integer> valoresAereo) {
        List<Territorio> territorios = getTerritorios(humano_);

        for (int i = 0; i < territorios.size(); i++) {

            for (int j = 0; j < valoresTerr.get(i); j++) {
                territorios.get(i).insereExTerrestre(new Terrestre());
            }

            for (int j = 0; j < valoresAereo.get(i); j++) {
                territorios.get(i).insereExAereo(new Aereo());
            }
        }
    }

    /**
     * Calcula a quantidade de territórios que um jogador possui
     * @param jogador O jogador a ser verificado
     * @return O número de territórios
     */
    public int getNumTerritorios(Jogador jogador){
        int num = 0;

        for(int i = 0; i < mapa_.getNumContinentes(); i++){
            try {
                for(int j = 0; j < mapa_.getContinente(i).getNumTerritorios(); j++){
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
            return getHumano();
        }
        return getCPU();
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

    /**
     * @return O jogador humano
     */
    public Jogador getHumano(){
        return humano_;
    }

    /**
     * Cria uma lista com os territórios de um jogador
     * @param jogador O jogador a ser verificado
     * @return A lista de territórios os quais o jogador ocupa
     */
    public List<Territorio> getTerritorios(Jogador jogador){
        List<Territorio> territorios = new ArrayList<>();

        for(int i = 0; i < mapa_.getNumContinentes(); i++){
            try {
                for(int j = 0; j < mapa_.getContinente(i).getNumTerritorios(); j++){
                    if(mapa_.getTerritorio(i, j).getOcupante() == jogador){
                        territorios.add(mapa_.getTerritorio(i,j));
                    }
                }
            } catch (MapaException e) {
                e.printStackTrace();
            }
        }

        return territorios;
    }

    /**
     * Calula a quantidade de exércitos terrestres de um jogador
     * @param jogador O jogador a ser verificado
     * @return A quantidade de exércitos terrestres
     */
    public int getNumTerrestres(Jogador jogador){
        int total = 0;
        List<Territorio> territorios = getTerritorios(jogador);

        for (Territorio t : territorios){
            if (t.getOcupante()==jogador){
                total += t.getNumExTerrestres();
            }
        }

        return total;
    }

    /**
     * Calula a quantidade de exércitos aéreos de um jogador
     * @param jogador O jogador a ser verificado
     * @return A quantidade de exércitos aéreos
     */
    public int getNumAereos(Jogador jogador){
        int total = 0;
        List<Territorio> territorios = getTerritorios(jogador);

        for (Territorio t : territorios){
            if (t.getOcupante()==jogador){
                total += t.getNumExAereos();
            }
        }

        return total;

    }

    public Territorio[] getTerrPossiveisAereo(Territorio t, Jogador jogador){
        List<Territorio> territorios = new ArrayList<>();
        Continente[] fronteira = t.getContinente().getFazFronteira();

        for(int i = 0; i < fronteira.length; i++){
            for(int j = 0; j < fronteira[i].getNumTerritorios(); j++){
                Territorio terr = null;
                try {
                    terr = fronteira[i].getTerritorio(i);
                } catch (ContinenteException e) {
                    e.printStackTrace();
                }

                if(terr != null){
                    if((terr.getOcupante() == jogador) && (terr.getNumExAereos() > 0)){
                        territorios.add(terr);
                    }
                }
            }
        }
        Territorio[] r = new Territorio[territorios.size()];
        r = territorios.toArray(r);

        return r;
    }

    public int atacarTerrestre(Jogador jogador, Territorio territorio, Territorio alvo, int qtd_ataque) {
        List<Integer> ataques = new ArrayList<>();
        List<Integer> defesas = new ArrayList<>();

        int n_defesa;
        int n_sucessos = 0;

        //Se humano atacando
        if (jogador == humano_) {
            //ataque
            for (int i = 0; i < qtd_ataque; i++) {
                ataques.add(territorio.getExercitos_terrestres_().get(i).combater());
            }

            //defesa
            n_defesa = cpu_.defenderTerr(alvo, qtd_ataque); //IA

            for (int i = 0; i < n_defesa; i++) {
                defesas.add(alvo.getExercitos_terrestres_().get(i).combater());
            }
        }
        //Se CPU atacando
        else {
            //ataque
            for (int i = 0; i < qtd_ataque; i++) {
                ataques.add(territorio.getExercitos_terrestres_().get(i).combater());
            }

            //defesa
            n_defesa = gui_.defender(territorio, alvo, qtd_ataque); //Chama janela de defesa.

            for (int i = 0; i < n_defesa; i++) {
                defesas.add(alvo.getExercitos_terrestres_().get(i).combater());
            }

        }

        Collections.sort(ataques);
        Collections.reverse(ataques);

        Collections.sort(defesas);
        Collections.reverse(defesas);

        //GUERRA!
        if (defesas.size() >= ataques.size()) {
            for (int i = 0; i < defesas.size(); i++) {
                if (ataques.get(i) > defesas.get(i)) {
                    //Ataque com sucesso!
                    n_sucessos++;
                }
            }
        } else {
            for (int i = 0; i < ataques.size(); i++) {
                if (ataques.get(i) > defesas.get(i)) {
                    //Ataque com sucesso!
                    n_sucessos++;
                }
            }
        }

        return n_sucessos;

    }

    /**
     * @return O jogador controlado pelo PC
     */
    public CPU getCPU() {
        return cpu_;
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
}