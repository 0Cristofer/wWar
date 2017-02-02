package bcco.wWar.game.jogadores;

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
     * Cria um jogador com um nome e define se ele é controlado pela CPU
     * @param is_pc Se é controlado pelo PC
     * @param nome O nome do jogador
     */
    public Jogador(boolean is_pc, String nome){
        nome_ = nome;
        is_pc_ = is_pc;
    }

    /**
     * Cria um jogador definindo se é controlado pela CPU
     * @param is_pc Se é controlado pela CPU
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

    /**
     * @return A quantidade de exércitos terrestres recebidos na última rodada
     */
    public int getTerrestresRecebidos() {
        return terrestres_recebidos_;
    }

    /**
     * @return A quantidade de exércitos terrestres recebidos na última rodada
     */
    public int getAereos_recebidos_() {
        return aereos_recebidos_;
    }

    /**
     * @param nome O nome do jogador
     */
    public void setNome(String nome){ nome_  = nome;}

    /**
     * @param terrestres_recebidos_ A quantidade de exércitos terrestres recebidos
     */
    public void setTerrestresRecebidos(int terrestres_recebidos_) {
        this.terrestres_recebidos_ = terrestres_recebidos_;
    }

    /**
     * @param aereos_recebidos_ A quantidade de exércitos aéreos recebidos
     */
    public void setAereosRecebidos(int aereos_recebidos_) {
        this.aereos_recebidos_ = aereos_recebidos_;
    }
}
