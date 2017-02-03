package bcco.wWar.mapa.continentes;

import bcco.wWar.game.exercitos.Aereo;
import bcco.wWar.game.exercitos.Terrestre;
import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.mapa.continentes.exceptions.TerritorioException;

import java.util.ArrayList;
import java.util.List;

/** Representa um território no tabuleiro que pode ser conquistado.
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */
public class Territorio {

    private String nome_;
    private Continente continente_;
    private Territorio[] faz_fronteira_;
    private Jogador ocupante_;
    private List<Terrestre> exercitos_terrestres_;
    private List<Aereo> exercitos_aereos_;

    /**
     * Cosntrutor publico que define o continente a qual esse territorio pertence
     * @param continente Continente a qual este territorio esta ligado
     * @param nome Nome do território
     */
    public Territorio(Continente continente, String nome){
        this.continente_ = continente;
        this.nome_ = nome;

        exercitos_terrestres_ = new ArrayList<>();
        exercitos_aereos_ = new ArrayList<>();
    }

    /**
     * @return
     */
    public List<Terrestre> getExercitos_terrestres_() {
        return exercitos_terrestres_;
    }

    /**
     * @return
     */
    public List<Aereo> getExercitos_aereos_() {
        return exercitos_aereos_;
    }

    /**
     * Adiciona um exército terrestre a lista de exércitos terrestres
     */
    public void insereExTerrestre() {
        Terrestre tropa = new Terrestre();
        exercitos_terrestres_.add(tropa);
    }

    /**
     * Adiciona um exército aéreos a lista de exércitos aéreos
     */
    public void insereExAereo() {
        Aereo tropa = new Aereo();
        exercitos_aereos_.add(tropa);
    }

    /**
     * Escreve o nome e as fronteiras do território na tela
     */
    void printTerritorio(){
        System.out.println("--------------------------");
        System.out.println("Terriotorio " + nome_ + " faz fronteira com:");
        for (Territorio territorio : faz_fronteira_) {
            System.out.println(territorio.nome_);
        }
        System.out.println("Ocupante: " + ocupante_.getNome());
        System.out.println("--------------------------");

    }

    /**
     * @return O nome do território
     */
    public String getNome(){
        return nome_;
    }

    /**
     * @return O vetor de territórios que fazem fronteira
     */
    public Territorio[] getFronteira(){
        return faz_fronteira_;
    }

    /**
     * @return O jogador ocupante deste território
     */
    public Jogador getOcupante(){
        return ocupante_;
    }

    /**
     * @return O número de exércitos terrestres
     */
    public int getNumExTerrestres(){
        return exercitos_terrestres_.size();
    }

    /**
     * @return O número de exércitos aéreos
     */
    public int getNumExAereos(){
        return exercitos_aereos_.size();
    }

    /**
     * @return O continente a qual este território pertence
     */
    public Continente getContinente(){
        return continente_;
    }

    /**
     * Configura a fronteira desse território a partir de uma lista de territórios
     * @param territorios A lista de territórios que fazem fronteira
     * @throws TerritorioException caso a lista for vazia
     */
    public void setFronteira(List<Territorio> territorios) throws TerritorioException {
        //Cria um vetor do tamanho da lista e os popula
        Territorio[] new_territorios = new Territorio[territorios.size()];

        for(int i = 0; i < territorios.size(); i++){
            new_territorios[i] = territorios.get(i);
        }

        if(new_territorios.length == 0){
            throw new TerritorioException();
        }
        else{
            faz_fronteira_ = new_territorios;
        }
    }

    /**
     * Muda o jogador a qual pertence esse territorio
     * @param new_ocupante O jogador que ocupara
     */
    public void setOcupante(Jogador new_ocupante){
        ocupante_ = new_ocupante;
    }

    /**
     * @return
     */
    @Override
    public String toString(){
        return getNome();
    }



}
