package bcco.wWar.game.exercitos;

//Imports do sistema
import java.util.Random;

/** Classe abstrata representante de um exércio
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 11/01/17
 */
abstract class Exercito {
    //Todos exércitos tem um gerador de random
    Random gerador = new Random();

    public abstract int combater();
}
