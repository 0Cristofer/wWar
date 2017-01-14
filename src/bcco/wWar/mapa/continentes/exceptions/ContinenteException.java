package bcco.wWar.mapa.continentes.exceptions;

/** Classe que gerencia as excessões da classe Continente
 *  @author Cristofer Oswald
 *  @since 13/01/17
 */
public class ContinenteException extends Exception {
    public ContinenteException() {
        super("Erro na classe Continente");
    }
    public ContinenteException(String message) {
        super(message);
    }
    public ContinenteException(String message, Throwable cause) {
        super(message, cause);
    }
    public ContinenteException(Throwable cause) {
        super(cause);
    }

}
