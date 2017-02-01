package bcco.wWar.game.exercitos;

/** Representa e controla um exercito do tipo terrestre
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */
public class Terrestre extends Exercito {
    public int combater(){
        return gerador.nextInt(6) + 1;
    }
}
