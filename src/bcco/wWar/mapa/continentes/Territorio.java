package bcco.wWar.mapa.continentes;

//Imports próprios

import bcco.wWar.game.exercitos.Aereo;
import bcco.wWar.game.exercitos.Terrestre;
import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.mapa.continentes.exceptions.TerritorioException;

import java.util.ArrayList;
import java.util.List;

//Imports do sistema

/** Representa um território no tabuleiro que pode ser conquistado.
 *  @author Cristofer Oswald e Bruno Cesar
 *  @since 11/01/17
 */
public class Territorio {
    //Detalhes do território
    private String nome_;
    private Continente continente_;
    private Territorio[] fronteiras_;
    private Jogador ocupante_;

    //Exércitos que ocupam este território
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
     * Adiciona um exército terrestre a lista de exércitos terrestres
     */
    public void insereExTerrestre(Terrestre tropa) {
        exercitos_terrestres_.add(tropa);
    }

    /**
     * Adiciona um exército aéreos a lista de exércitos aéreos
     */
    public void insereExAereo(Aereo tropa) {
        exercitos_aereos_.add(tropa);
    }

    /**
     * Remove um exército terrestre da lista
     * @return O exército removido
     */
    public Terrestre removeExTerrestre(){
        return exercitos_terrestres_.remove(exercitos_terrestres_.size()-1);
    }

    /**
     * Remove um exército aéreo da lista
     * @return O exército removido
     */
    public Aereo removeExAereo(){
        return exercitos_aereos_.remove(exercitos_aereos_.size()-1);
    }

    /**
     * @return O nome do território
     */
    public String getNome(){
        return nome_;
    }

    /**
     * @return O jogador ocupante deste território
     */
    public Jogador getOcupante(){
        return ocupante_;
    }

    /**
     * @return A lista de exércitos terrestres neste território
     */
    public List<Terrestre> getExercitosTerrestres() {
        return exercitos_terrestres_;
    }

    /**
     * @return A lista de exércitos aéreos neste território
     */
    public List<Aereo> getExercitosAereos() {
        return exercitos_aereos_;
    }

    /**
     * @return O vetor de territórios que fazem fronteira
     */
    public Territorio[] getFronteira(){
        return fronteiras_;
    }

    /**
     * Cria uma lista com os territórios que fazem fronteira com o especificado mas não pertencem ao jogador
     * @param jogador Jogador a ser verificado
     * @return A lista de territórios inimigos
     */
    public List<Territorio> getFronteirasInimigas(Jogador jogador) {
        List<Territorio> fronteirasInimigas = new ArrayList<>();

        for (Territorio t : fronteiras_) {
            if (t.getOcupante() != jogador) {
                fronteirasInimigas.add(t);
            }
        }

        return fronteirasInimigas;
    }

    /**
     * Cria uma lista com os territórios que fazem fronteira com o especificado e que pertencem
     * @param jogador Jogador a ser verificado
     * @return A lista de territórios aliados
     */
    public List<Territorio> getFronteirasAliadas(Jogador jogador) {
        List<Territorio> fronteirasInimigas = new ArrayList<>();

        for (Territorio t : fronteiras_) {
            if (t.getOcupante().equals(jogador)) {
                fronteirasInimigas.add(t);
            }
        }

        return fronteirasInimigas;
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
            fronteiras_ = new_territorios;
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
     * @return A string que representa este objeto
     */
    @Override
    public String toString(){
        return getNome();
    }

}
