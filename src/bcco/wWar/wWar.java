package bcco.wWar;

import bcco.iomanager.IOManager;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.ConstrutorMapa;
import bcco.wWar.mapa.exceptions.ConstrutorException;
import bcco.wWar.exceptions.wWarException;

import java.io.IOException;
import java.util.List;

/** Singleton representante de um jogo. Controla e exeuta o jogo.
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class wWar {

    private static wWar instance_;
    private List<String> config_;
    private Mapa mapa_;

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
        System.out.println("Estou rodando!");

        //Tenta iniciar os sistemas
        try {
            initSystems();
        } catch (wWarException e) {
            System.out.println(e.getMessage());
        }

        if(mapa_ == null){
            throw new wWarException("Mapa não foi criado, reinicie o sistema");
        }
        else{
            System.out.println("Estou pronto para funcionar");
            //mapa_.printMapa();
        }
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
    }

    /**
     * Lê as configurações de um arquivo de texto e as guarda
     * @throws IOException se houver erro na leitura do arquivo
     * @throws wWarException se houver erro na interpretação das configurações
     */
    private void readConfigs() throws IOException, wWarException {
        config_ = IOManager.getInstance().readTextFile("config");

        //Tenta contruir o mapa_ a partir do arquivo lido nas configurações
        try {
            mapa_ = ConstrutorMapa.getInstance().buildMap(config_.get(0));
        } catch (ConstrutorException e) {
            System.out.println(e.getMessage());
            throw new wWarException("Erro ao ler arquivo de mapa", e);
        }
    }

    /**
     * Contrutor privado sem argumentos
     */
    private wWar(){
    }
}
