package bcco.wWar.game.jogadores;

import java.util.List;

/** Controla os dados refrente a um jogador
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Jogador {
    private String nome_;
    private boolean is_pc_;

    /**
     * Define o nome do jogador e se ele é ou não controlado pelo computador
     * @param is_pc Se é controlado pelo PC
     */
    public Jogador(boolean is_pc){
        is_pc_ = is_pc;
    }

    /**
     * @return O nome do jogador
     */
    public String getNome(){
        return nome_;
    }

    public void setNome(String nome){ nome_  = nome;}

}
