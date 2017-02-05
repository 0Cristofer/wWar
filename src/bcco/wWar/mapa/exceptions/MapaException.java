package bcco.wWar.mapa.exceptions;

/** Classe controladora das excessões da classe Mapa
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 13/01/17
 */
public class MapaException extends Exception {
    public MapaException() {
        super("Erro no mapa");
    }
    public MapaException(String message) {
        super(message);
    }
    public MapaException(String message, Throwable cause) {
        super(message, cause);
    }
    public MapaException(Throwable cause) {
        super(cause);
    }
}
