package bcco.wWar.game;

import bcco.wWar.game.exercitos.Terrestre;
import bcco.wWar.game.jogadores.CPU;
import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.gui.Gui;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.Continente;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.swing.*;
import java.util.*;

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
    public Game(Mapa mapa) {
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
    }

    public void setGui_(Gui gui) {
        gui_ = gui;
    }

    /**
     * Cria e configura o jogador CPU
     */
    public void createCPU(){
        double agressividade = 0.3; //Nível fácil?

        cpu_ = new CPU(this, agressividade);
    }

    /**
     * Distribui os territorios aleatoriamente e igualmente
     */
    public void distribuiTerritorios(){
        List<List<Territorio>> territorios_livres = new ArrayList<>();
        int mod = new Random().nextInt(2); // Se 0 : Jogador tem vantagem Se 1 : CPU tem vantagem
        int humans = 0;
        int robots = 0;

        for (Continente c : mapa_.getContinentes_()) {
            territorios_livres.add(new ArrayList<Territorio>(Arrays.asList(c.getTerritorios_())));
        }

        for (List<Territorio> continente : territorios_livres) {

            Collections.shuffle(continente);

            for (Territorio t : continente) {
                if (mod % 2 == 0) {
                    t.setOcupante(humano_);
                    humans++;

                } else {
                    t.setOcupante(cpu_);
                    robots++;
                }

                t.insereExTerrestre();
                t.insereExAereo();
                mod++;

            }
        }

        System.out.format("\nlog - Distribuir Territórios:\n Territorios jogador: %d\n Territorios computador: %d\n", humans, robots);
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

        cpu_.distribuirExercitos();
        gui_.distribuirExercito();


        rodada_++;

        if (rodada_ > 0) {
            JOptionPane.showMessageDialog(null, "Fim da rodada! Distribua seus exércitos");
        }
    }


    /**
     * Configura um novo jogo
     */
    public void iniciarJogo(){
        rodada_ = -1;
        mudaRodada();
    }

    /**
     * Recebe uma lista de quantidades de exércitos a serem adicionados aos territórios
     * @param valoresTerr A lista de valores (deve ter a mesmo tamanho da quantidade de territórios)
     */
    public void distribuirExercitos(Jogador jogador, List<Integer> valoresTerr, List<Integer> valoresAereo) {
        List<Territorio> territorios = getTerritorios(jogador);

        for (int i = 0; i < territorios.size(); i++) {
            for (int j = 0; j < valoresTerr.get(i); j++) {
                territorios.get(i).insereExTerrestre();
            }

            for (int j = 0; j < valoresAereo.get(i); j++) {
                territorios.get(i).insereExAereo();
            }
        }
    }

    public void movimentar(Territorio origem, Territorio destino, String tipo, int qtd){
        if(tipo.equals("Terrestre")){
            for(int i = 0; i < qtd; i++){
                destino.insereExTerrestre(origem.removeExTerrestre());
            }
        }
        else{
            destino.insereExAereo(origem.removeExAereo());
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
        for(Continente c : fronteira){
            System.out.println(c.getNome());
        }

        for(int i = 0; i < fronteira.length; i++){
            for(int j = 0; j < fronteira[i].getNumTerritorios(); j++){
                Territorio terr = null;
                try {
                    terr = fronteira[i].getTerritorio(j);
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

    public void atacarTerrestre(Jogador jogador, Territorio territorio, Territorio alvo, int qtd_ataque, int n_defesa) {
        List<Integer> ataques = new ArrayList<>();
        List<Integer> defesas = new ArrayList<>();
        List<Terrestre> exercitos_alvos = alvo.getExercitos_terrestres_();
        List<Terrestre> exercitos_atacantes = territorio.getExercitos_terrestres_();

        int n_sucessos = 0;
        int n_fracassos = 0;
        boolean resultado;

        //Decide defesa
        if (jogador == humano_) {
            n_defesa = cpu_.defenderTerr(alvo, qtd_ataque); //IA
        }

        //ataque
        for (int i = 0; i < qtd_ataque; i++) {
            ataques.add(exercitos_atacantes.get(i).combater());
        }

        //defesa
        for (int i = 0; i < n_defesa; i++) {
            defesas.add(exercitos_alvos.get(i).combater());
        }

        Collections.sort(ataques);
        Collections.reverse(ataques);

        Collections.sort(defesas);
        Collections.reverse(defesas);

        System.out.println("\nLIST ATAQUES:");
        System.out.println(ataques.toString());

        System.out.println("\nLIST DEFESAS:");
        System.out.println(defesas.toString());

        //GUERRA!
        if (defesas.size() <= ataques.size()) {
            for (int i = 0; i < defesas.size(); i++) {
                if (ataques.get(i) > defesas.get(i)) {
                    n_sucessos++;
                } else {
                    n_fracassos++;
                }
            }
        } else {
            for (int i = 0; i < ataques.size(); i++) {
                if (ataques.get(i) > defesas.get(i)) {
                    n_sucessos++;
                } else {
                    n_fracassos++;
                }
            }
        }


        //Mata os ataques fracassados
        for (int i = 0; i < n_fracassos; i++) {
            territorio.removeExTerrestre();
        }


        //Mata as defesas fracassadas
        for (int i = 0; i < n_sucessos; i++) {
            alvo.removeExTerrestre();
        }

        System.out.format("\nlog - ataque terrestre:\n %s -> %s\n Exércitos atacantes mortos: %d\n" +
                        "Exércitos defensores mortos: %d\n", jogador.getNome(), alvo.getNome(),
                n_fracassos, n_sucessos);

        //Dominou o território
        if (exercitos_alvos.isEmpty()) {
            resultado = true;
            alvo.setOcupante(jogador);

            System.out.format(jogador.getNome() + " dominou o territorio " + alvo.getNome() + "\n");


            for (int i = 0; i < qtd_ataque - n_fracassos; i++) {
                alvo.insereExTerrestre(territorio.removeExTerrestre());
            }

        } else {
            resultado = false;
        }

        if (jogador == humano_) {
            gui_.resultadoAtaque(jogador.getNome(), territorio.getNome(),
                    Integer.toString(n_fracassos), Integer.toString(n_sucessos), resultado);
        }
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