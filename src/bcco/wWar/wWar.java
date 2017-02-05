package bcco.wWar;

//Imports próprios

import bcco.iomanager.IOManager;
import bcco.wWar.exceptions.wWarException;
import bcco.wWar.game.Game;
import bcco.wWar.game.jogadores.CPU;
import bcco.wWar.gui.Gui;
import bcco.wWar.mapa.ConstrutorMapa;
import bcco.wWar.mapa.exceptions.ConstrutorException;

import java.io.IOException;
import java.util.List;

//Imports do sistema


/** Singleton que controla o 'backbone' do jogo.
 * @author Cristofer Oswald
 * @author Bruno Cesar
 * @since 12/01/17
 */
public class wWar {
    //Variáveis de backend
    private static wWar instance_;
    private List<String> config_;

    //Variáveis de frontend
    private Game game_;
    private Gui gui_;

    /**
     * @return O singleton do jogo
     */
    public static synchronized wWar getInstance(){
        if(instance_ == null){
            instance_ = new wWar();
        }

        return instance_;
    }

    /**
     * Executa o jogo
     * @throws wWarException caso haja algum erro na execução
     */
    public void run() throws wWarException {
        //Tenta iniciar os sistemas
        try {
            initSystems();
        } catch (wWarException e) {
            System.out.println(e.getMessage());
        }

        gui_.telaInicial();
        gui_.show();
    }

    /**
     * Inicializa os sistemas inicias
     * @throws wWarException se houver algum erro na inicialização do sistema
     */
    private void initSystems() throws wWarException {
        //Tenta ler as configurações
        try {
            readConfigs();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new wWarException("Erro ao ler arquivo de configuração", e);
        }

        //Inicializa a GUI
        gui_ = new Gui("wWar", 1280, 720, game_);
        game_.setGui(gui_);
    }

    /**
     * Lê as configurações do arquivo e as executa
     * @throws IOException se houver erro na leitura do arquivo
     * @throws wWarException se houver erro na interpretação das configurações
     */
    private void readConfigs() throws IOException, wWarException {
        config_ = IOManager.getInstance().readTextFile("config");

        //Tenta construir o mapa a partir do arquivo lido nas configurações
        //e cria a instancia do jogo
        try {
            game_ = new Game(ConstrutorMapa.getInstance().buildMap(config_.get(0), config_.get(1)));

            if(game_.getMapa() == null){
                throw new wWarException("Mapa não foi criado, reinicie o sistema");
            }
        } catch (ConstrutorException e) {
            System.out.println(e.getMessage());
            throw new wWarException("Erro ao ler arquivo de mapa", e);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            throw new wWarException("Erro ao ler arquivo de configuração", e);
        }

        //Loop que lê os nomes dos oponentes, estes se encontram no arquivo config após MAPA e TABELA
        for (int i = 2; i < config_.size() ; i++) {
            CPU.insertNomeCPU(config_.get(i));
        }
        game_.createCPU();
    }

    /**
     * Contrutor privado sem argumentos
     */
    private wWar(){
    }
}
