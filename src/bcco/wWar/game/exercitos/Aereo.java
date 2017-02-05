package bcco.wWar.game.exercitos;

/** Representa e controla um exercito do tipo aéreo
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */
public class Aereo extends Exercito {
    /**
     * Simula uma jogada no dado de ataque/defesa aéreo
     * @return Um valor de 0 a 3
     */
    public int combater(){
        return gerador.nextInt(3);
    }
}
