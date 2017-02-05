package bcco;

//Imports próprios
import bcco.wWar.exceptions.wWarException;
import bcco.wWar.wWar;

/** Classe que executa o jogo
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 11/01/17
 */
public class Main {
    /**
     * Função main do sistema
     * @param args Argumentos passados na execução
     */
    public static void main(String[] args) {
        try {
            wWar.getInstance().run();
        } catch (wWarException e) {
            System.out.println(e.getMessage());
        }
    }
}
