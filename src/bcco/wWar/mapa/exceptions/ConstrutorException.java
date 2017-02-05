package bcco.wWar.mapa.exceptions;

/** Classe controladora das excessões da classe ContrutorMapa
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 13/01/17
 */
public class ConstrutorException extends Exception {
    public ConstrutorException() {
        super("Construção do mapa com erro");
    }
    public ConstrutorException(String message) {
        super(message);
    }
    public ConstrutorException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConstrutorException(Throwable cause) {
        super(cause);
    }

}
