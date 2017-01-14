package bcco.wWar.exceptions;

/** Controla as exceções da classe wWar
 *  @author Cristofer Oswald
 *  @since 13/01/17
 */
public class wWarException extends Exception {
    public wWarException() {
        super("Erro de execução do jogo");
    }
    public wWarException(String message) {
        super(message);
    }
    public wWarException(String message, Throwable cause) {
        super(message, cause);
    }
    public wWarException(Throwable cause) {
        super(cause);
    }
}
