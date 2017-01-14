package bcco;

import bcco.wWar.exceptions.wWarException;
import bcco.wWar.wWar;

/** Classe que executa o jogo
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */
public class Main {

    /**
     * Função main do sistema
     * @param args Argumentos passados na execução
     */
    public static void main(String[] args) {
        System.out.println("Iniciando jogo");
        try {
            wWar.getInstance().run();
        } catch (wWarException e) {
            System.out.println(e.getMessage());
        }
    }
}
