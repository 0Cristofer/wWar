package bcco.wWar.game.jogadores;

import java.util.List;

/** Controla os dados refrente a um jogador
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Jogador {
    private String nome_;
    private boolean is_pc_;
    private int terrestres_recebidos_;
    private int aereos_recebidos_;

    /**
     * Define o nome do jogador e se ele é ou não controlado pelo computador
     * @param is_pc Se é controlado pelo PC
     */
    public Jogador(boolean is_pc){
        is_pc_ = is_pc;
    }

    /**
     * @param nome O nome do jogador
     */
    public void setNome(String nome){ nome_  = nome;}

    /**
     * @return O nome do jogador
     */
    public String getNome(){
        return nome_;
    }

    /**
     *
     * @return
     */
    public int getTerrestres_recebidos_() {
        return terrestres_recebidos_;
    }

    /**
     *
     * @param terrestres_recebidos_
     */
    public void setTerrestres_recebidos_(int terrestres_recebidos_) {
        this.terrestres_recebidos_ = terrestres_recebidos_;
    }

    /**
     *
     * @return
     */
    public int getAereos_recebidos_() {
        return aereos_recebidos_;
    }

    /**
     *
     * @param aereos_recebidos_
     */
    public void setAereos_recebidos_(int aereos_recebidos_) {
        this.aereos_recebidos_ = aereos_recebidos_;
    }



}
