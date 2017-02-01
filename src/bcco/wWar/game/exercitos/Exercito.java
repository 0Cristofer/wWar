package bcco.wWar.game.exercitos;

import java.util.Random;

/** Classe abstrata representante de um exércio
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */

abstract class Exercito {
    Random gerador = new Random();

    public abstract int combater();
}
