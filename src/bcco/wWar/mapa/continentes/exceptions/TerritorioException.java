package bcco.wWar.mapa.continentes.exceptions;

/** Classe gerenciadora dos exceptions da classe Territorio
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 12/01/17
 */
public class TerritorioException extends Exception {
    public TerritorioException() {
        super("Erro na classe Territorio");
    }
    public TerritorioException(String message) {
        super(message);
    }
    public TerritorioException(String message, Throwable cause) {
        super(message, cause);
    }
    public TerritorioException(Throwable cause) {
        super(cause);
    }

}
