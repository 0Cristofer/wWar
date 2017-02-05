package bcco.wWar.game;

//Imports próprios

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

//Imports do sistema

/** Representa um jogo pronto para ser utilizado
 * @author Cristofer Oswald
 * @author Bruno Cesar
 * @since 29/01/17
 */
public class Game {
    private Mapa mapa_;
    private Gui gui_;

    //Jogadores
    private Jogador humano_;
    private CPU cpu_;

    private int rodada_;

    /**
     * Constrói a instância do jogo com o mapa construído e inicializa o jogador humano
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
    }

    /**
     * Cria e configura o jogador CPU
     */
    public void createCPU(){
        double agressividade = 0.2;

        cpu_ = new CPU(this, agressividade);
    }

    /**
     * Configura um novo jogo
     */
    public void iniciarJogo(){
        rodada_ = -1;
        mudaRodada();
    }

    /**
     * Distribui os territorios aleatoria e igualmente
     */
    public void distribuiTerritorios(){
        List<List<Territorio>> territorios_livres = new ArrayList<>();
        // Se 0 : Jogador tem vantagem Se 1 : CPU tem vantagem
        int mod = new Random().nextInt(2);
        int humans = 0;
        int robots = 0;

        for (Continente c : mapa_.getContinentes()) {
            territorios_livres.add(new ArrayList<>(Arrays.asList(c.getTerritorios())));
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

        if (rodada_ >= 0) {
            JOptionPane.showMessageDialog(null, "Fim da rodada! Distribua seus exércitos");
        }

        cpu_.resetarVontade();

        rodada_++;
    }


    /**
     * Recebe uma lista de quantidades de exércitos a serem adicionados aos territórios
     * @param jogador O jogador que receberá os exércitos
     * @param valoresTerr Quantidade de exércitos terrestres
     * @param valoresAereo Quantidade de exércitos aéreos
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

    /**
     * Movimenta os exércitos de um território para outro
     * @param origem O território de origem
     * @param destino O território de destino
     * @param tipo O tipo de tropa (Aérea ou Terrestre)
     * @param qtd A quantidade de tropas a serem movimentadas
     */
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
     * Realiza um ataque terrestre
     * @param jogador O atacante
     * @param origem O território de origem
     * @param destino O território de destino
     * @param qtd_ataque Quantidade de tropas no ataque
     * @param n_defesa Quantidae de tropas na defesa
     */
    public void atacarTerrestre(Jogador jogador, Territorio origem, Territorio destino, int qtd_ataque, int n_defesa) {
        List<Integer> ataques = new ArrayList<>();
        List<Integer> defesas = new ArrayList<>();
        List<Terrestre> exercitos_alvos = destino.getExercitosTerrestres();
        List<Terrestre> exercitos_atacantes = origem.getExercitosTerrestres();

        int n_sucessos = 0;
        int n_fracassos = 0;
        boolean resultado;

        //Decide defesa
        if (jogador == humano_) {
            n_defesa = cpu_.defenderTerr(destino, qtd_ataque); //IA
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
            origem.removeExTerrestre();
        }


        //Mata as defesas fracassadas
        for (int i = 0; i < n_sucessos; i++) {
            destino.removeExTerrestre();
        }

        System.out.format("\nlog - ataque terrestre:\n %s -> %s\n Exércitos atacantes mortos: %d\n" +
                        "Exércitos defensores mortos: %d\n", jogador.getNome(), destino.getNome(),
                n_fracassos, n_sucessos);

        //Dominou o território
        if (exercitos_alvos.isEmpty()) {
            resultado = true;
            destino.setOcupante(jogador);

            System.out.format(jogador.getNome() + " dominou o territorio " + destino.getNome() + "\n");

            for (int i = 0; i < qtd_ataque - n_fracassos; i++) {
                destino.insereExTerrestre(origem.removeExTerrestre());
            }

        } else {
            resultado = false;
        }

        gui_.relatorioAtaque(jogador, destino.getNome(),
                Integer.toString(n_fracassos), Integer.toString(n_sucessos), resultado);

        if (jogador == cpu_) {
            cpu_.alterarVontade(resultado, n_sucessos, n_fracassos);
            cpu_.jogar();
        }

        if(verificaVitória(jogador)){
            gui_.vitoria(jogador);
        }

        gui_.updateInfos();
    }

    /**
     * Realiza um ataque aéreo
     * @param jogador O jogador atacante
     * @param origem O território de origem
     * @param destino O território de destino
     * @param qtd_ataque Quantidade de tropas no ataque
     */
    public void atacarAereo(Jogador jogador, Territorio origem, Territorio destino, int qtd_ataque){
        int defesa = destino.getExercitosAereos().get(0).combater();
        int avioes_derrubados = 0;
        int terrestres_mortos = 0;

        for(int i = 0; i < qtd_ataque; i++){
            int comb = origem.getExercitosAereos().get(i).combater();

            if(comb > 0){
                if(destino.getNumExAereos() > 0){
                    destino.removeExAereo();
                    avioes_derrubados = avioes_derrubados + 1;
                }

                switch (comb) {
                    case 1:
                        if (destino.getNumExTerrestres() > 1) {
                            destino.removeExTerrestre();
                            terrestres_mortos = terrestres_mortos + 1;
                        }
                        break;
                    case 2:
                        if (destino.getNumExTerrestres() > 1) {
                            if (destino.getNumExTerrestres() > 2) {
                                destino.removeExTerrestre();
                                destino.removeExTerrestre();
                                terrestres_mortos = terrestres_mortos + 2;
                            }
                            else{
                                destino.removeExTerrestre();
                                terrestres_mortos = terrestres_mortos + 1;
                            }
                        }
                        break;
                    case 3:
                        if (destino.getNumExTerrestres() > 1){
                            if (destino.getNumExTerrestres() > 2) {
                                if(destino.getNumExTerrestres() > 3){
                                    destino.removeExTerrestre();
                                    destino.removeExTerrestre();
                                    destino.removeExTerrestre();
                                }
                                else {
                                    destino.removeExTerrestre();
                                    destino.removeExTerrestre();
                                    terrestres_mortos = terrestres_mortos + 2;
                                }
                            }
                            else{
                                destino.removeExTerrestre();
                                terrestres_mortos = terrestres_mortos + 1;
                            }
                        }
                        break;
                }
            }
        }

        for(int i = 0; i < Integer.min(defesa, qtd_ataque); i++){
            origem.removeExAereo();
        }

        gui_.relatorioAereo(jogador, origem, destino, defesa, avioes_derrubados, terrestres_mortos);
    }

    /**
     * Cria uma lista de territórios que podem ser atacados pelo ar
     * @param jogador Jogador atacante
     * @param origem Território de origem
     * @return A lista de territórios
     */
    public List<Territorio> checkAereo(Jogador jogador, Territorio origem){
        List<Territorio> pode_atacar = new ArrayList<>();

        for(Territorio t : origem.getFronteirasInimigas(jogador)){
            if((t.getNumExTerrestres() > 3) && (t.getNumExAereos() > 0)){
                pode_atacar.add(t);
            }
        }

        return pode_atacar;
    }

    /**
     * Verifica se um jogador ganhou o jogo (tem 2 ou mais continentes)
     * @param jogador O jogador a ser verificado
     * @return True se o jogador ganhou, se não False
     */
    private boolean verificaVitória(Jogador jogador){
        return (getNumContinentesJogador(jogador) > 1);
    }

    /**
     * Verifica quantos continentes o jogador possui
     * @param jogador O jogador a ser verificado
     * @return A quantidade de continentes
     */
    public int getNumContinentesJogador(Jogador jogador){
        int num_terrs;
        int num_cont = 0;

        for(Continente c : mapa_.getContinentes()) {
            num_terrs = 0;
            for(Territorio t : c.getTerritorios()){
                if(t.getOcupante().equals(jogador)){
                    num_terrs = num_terrs + 1;
                }
            }

            if(num_terrs == c.getNumTerritorios()){
                num_cont = num_cont + 1;
            }
        }

        return num_cont;
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

    /**
     * Cria um vetor com os territórios que podem enviar exércitos aéreos
     * @param origem O território de origem
     * @param jogador O jogador dono do território
     * @return O vetor com os territórios
     */
    public Territorio[] getTerrPossiveisAereo(Territorio origem, Jogador jogador){
        List<Territorio> territorios = new ArrayList<>();
        Continente[] fronteira = origem.getContinente().getFazFronteira();

        for (Continente f : fronteira) {
            for (int j = 0; j < f.getNumTerritorios(); j++) {
                Territorio terr = null;
                try {
                    terr = f.getTerritorio(j);
                } catch (ContinenteException e) {
                    e.printStackTrace();
                }

                if (terr != null) {
                    if ((terr.getOcupante() == jogador) && (terr.getNumExAereos() > 0)) {
                        territorios.add(terr);
                    }
                }
            }
        }
        territorios.remove(origem);//A orgiem é adicionada pois faz parte do continente

        Territorio[] r = new Territorio[territorios.size()];
        r = territorios.toArray(r);

        return r;
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
     * @return A instância do mapa desse jogo
     */
    public Mapa getMapa(){
        return mapa_;
    }

    /**
     * @return O jogador humano
     */
    public Jogador getHumano(){
        return humano_;
    }

    /**
     * @return O jogador controlado pelo PC
     */
    public CPU getCPU() {
        return cpu_;
    }

    /**
     * @return A GUI deste jogo
     */
    public Gui getGui() {
        return gui_;
    }

    /**
     * Define a gui que será usada nesse jogo
     * @param gui A instância da GUI
     */
    public void setGui(Gui gui) {
        gui_ = gui;
    }
}