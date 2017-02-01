package bcco.wWar.game.jogadores;

/** Controla os dados refrente a um jogador
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Jogador {
    private String nome_;
    private boolean is_pc_;

    /**
     * Define o nome do jogador e se ele é ou não controlado pelo computador
     * @param nome Nome do jogandor
     * @param is_pc Se é controlado pelo PC
     */
    public Jogador(String nome, boolean is_pc){
        nome_ = nome;
        is_pc_ = is_pc;
    }

    /**
     * @return O nome do jogador
     */
    public String getNome(){
        return nome_;
    }
}
