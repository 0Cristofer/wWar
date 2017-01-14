package bcco.wWar.mapa;

import bcco.wWar.mapa.continentes.*;
import bcco.wWar.mapa.exceptions.MapaException;

import java.util.List;

/** Representa o mapa (ou tabuleiro)
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Mapa {
    private String map_file_;
    private Continente[] continentes_;

    /**
     * Retorna a instância de um continente a partir do seu index
     * @param index O index do continente
     * @return A instância do continente
     * @throws MapaException caso o index seja invalido
     */
    public Continente getContinente(int index) throws MapaException {
        if((index < continentes_.length) && (index >= 0)){
            return continentes_[index];
        }
        else{
            throw new MapaException("Index de continente inválido");
        }
    }

    /**
     * Procura um continente dado seu nome
     * @param nome O nome a ser procurado
     * @return O index do continente se achado, se não, -1
     */
    public int findContinente(String nome){
        for (int i = 0; i < continentes_.length; i++) {
            if(continentes_[i].getNome().equals(nome)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Escreve os detalhes do mapa
     */
    public void printMapa(){
        for (Continente continente: continentes_) {
            continente.printContinente();
        }
    }

    /**
     * Cria uma instância do mapa com o caminho para a sua leitura
     * @param map_file O caminho para o arquivo
     */
    Mapa(String map_file){
        map_file_ = map_file;

    }

    /**
     * Define os continentes desse mapa a partir de uma lista
     * @param continentes A lista com os continentes
     * @throws MapaException caso a lista esteja vazia
     */
    void setContinentes(List<Continente> continentes) throws MapaException {
        Continente[] new_continentes = new Continente[continentes.size()];
        for(int i = 0; i < continentes.size(); i++){
            new_continentes[i] = continentes.get(i);
        }

        if(continentes.size() == 0){
            throw new MapaException("Tamanho dos continentes não pode ser 0");
        }
        else{
            continentes_ = new_continentes;
        }
    }
}
