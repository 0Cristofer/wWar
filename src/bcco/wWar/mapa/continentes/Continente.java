package bcco.wWar.mapa.continentes;

//Imports próprios

import bcco.wWar.mapa.continentes.exceptions.ContinenteException;

import java.util.ArrayList;
import java.util.List;

//Imports do sistema

/** Controla os dados referente a um continente
 *  @author Cristofer Oswald
 *  @since 11/01/17
 */
public class Continente{
    private String nome_;
    private Territorio[] territorios_;
    private Continente[] fronteiras;

    /**
     * Construtor padrão
     * @param nome_ Nome do novo continente
     */
    public Continente(String nome_){
        this.nome_ = nome_;
    }

    /**
     * Procura um território dado seu nome
     * @param nome O nome a ser procurad/o
     * @return O index do território se achado, se não, -1
     */
    public int findTerritorio(String nome){
        for (int i = 0; i < territorios_.length; i++) {
            if(territorios_[i].getNome().equals(nome)){
                return i;
            }
        }
        return -1;
    }

    /**
     * @return O nome do continente
     */
    public String getNome(){
        return nome_;
    }

    /**
     * @return O vetor com os continentes que fazem fronteira
     */
    public Continente[] getFazFronteira(){
        return fronteiras;
    }

    /**
     * Retorna um território a partir de seu index
     * @param index O index do terrotório
     * @return A instância do território
     * @throws ContinenteException Se o index for inválido
     */
    public Territorio getTerritorio(int index) throws ContinenteException {
        if((index < territorios_.length) && (index >= 0)){
            return territorios_[index];
        }
        else{
            throw new ContinenteException("Index de territorio inválido");
        }
    }

    /**
     * @return O vetor de territórios deste continente
     */
    public Territorio[] getTerritorios() {
        return territorios_;
    }

    /**
     * @return A quantidade de territorios nesse continente
     */
    public int getNumTerritorios(){
        return territorios_.length;
    }

    /**
     * Recebe uma lista de territórios e cria um vetor que definirá os territórios que compõem esse continente
     * @param territorios A lista de territórios
     * @throws ContinenteException Caso a lista seja vazia
     */
    public void setTerritorios(List<Territorio> territorios) throws ContinenteException {
        //Cria um vetor do tamanho da lista e o popula
        Territorio[] new_territorios = new Territorio[territorios.size()];

        for(int i = 0; i < territorios.size(); i++){
            new_territorios[i] = territorios.get(i);
        }

        if(new_territorios.length == 0){
            throw new ContinenteException("Continente com 0 territórios criado");
        }
        else{
            territorios_ = new_territorios;
        }
    }

    /**
     * Configura as fronteiras dos continentes
     */
    public void setFronteira(){
        List<Continente> fronteira = new ArrayList<>();

        for(Territorio t : territorios_){
            Territorio[] f = t.getFronteira();
            for(Territorio t1 : f){
                if(!fronteira.contains(t1.getContinente())){
                    fronteira.add(t1.getContinente());
                }
            }
        }

        fronteiras = new Continente[fronteira.size()];

        for(int i = 0; i < fronteiras.length; i++){
            fronteiras[i] = fronteira.get(i);
        }
    }
}
