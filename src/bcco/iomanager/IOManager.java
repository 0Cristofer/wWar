package bcco.iomanager;

//Imports do sistema

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/** Classe responsável por controlar a leitura e escrita de arquivos
 *  @author Cristofer Oswald e Bruno Cesar
 *  @since 12/01/17
 */
public class IOManager {
    private static IOManager instance_;

    /**
     * @return O singleton do IOManager
     */
    public static synchronized IOManager getInstance(){
        if(instance_ == null){
            instance_ = new IOManager();
        }
        return instance_;
    }

    /**
     * Lê um arquivo de texto a partir do seu caminho e retorna a lista de strings dele
     * @param file_path String contendo o caminho para o arquivo
     * @return Uma lista de Strings representando o arquivo
     * @throws IOException se o caminho for inválido
     */
    public List<String> readTextFile(String file_path) throws IOException{
        Path path = Paths.get(file_path);
        return Files.readAllLines(path);
    }

    /**
     * Escreve uma lista de Strings em um arquivo de texto
     * @param lines A lista de linhas (strings) a serem escritas
     * @param file_path O caminho para o arquivo de saida
     * @param option Maneira como o arquivo deve ser aberto e escrito
     * @throws IOException se houver erro na abertura do arquivo
     */
    public void writeTextFile(List<String> lines, String file_path, StandardOpenOption option) throws IOException {
        Path path = Paths.get(file_path);
        Files.write(path, lines, option);
    }

    /**
     * Construtor privado sem parametros
     */
    private IOManager(){

    }
}
