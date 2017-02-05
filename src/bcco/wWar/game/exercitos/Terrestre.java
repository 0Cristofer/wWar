package bcco.wWar.game.exercitos;

/** Representa e controla um exercito do tipo terrestre
 *  @author Cristofer Oswald
 *  @author Bruno Cesar
 *  @since 11/01/17
 */
public class Terrestre extends Exercito {
    /**
     * Realiza a simulação de jogada nos dados de ataque/defesa terrestre
     * @return Um valor de 0 a 5
     */
    public int combater(){
        return gerador.nextInt(6);
    }
}
