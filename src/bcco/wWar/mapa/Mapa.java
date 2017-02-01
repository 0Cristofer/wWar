package bcco.wWar.mapa;

import bcco.wWar.mapa.continentes.*;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import java.util.List;

/** Representa o mapa (ou tabuleiro)
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Mapa {
    private String map_file_;
    private String table_file_;
    private Continente[] continentes_;
    private int[][][] tabela_mapa_;

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
     * Retorna um território a partir do index do seu continente e do próprio território
     * @param cont_index Index do continente ao qual o território pertence
     * @param terr_index Index do território a ser retornado
     * @return A instância do território
     * @throws MapaException Caso houver erro
     */
    public Territorio getTerritorio(int cont_index, int terr_index) throws MapaException {
        if((cont_index < continentes_.length) && (cont_index >= 0)){
            try {
                return continentes_[cont_index].getTerritorio(terr_index);
            } catch (ContinenteException e) {
                throw new MapaException(e);
            }
        }
        else{
            throw new MapaException("Index de continente inválido");
        }
    }

    public int[][][] getTablaMapa(){
        return tabela_mapa_;
    }

    /**
     * @return A quantidade de continentes nesse mapa
     */
    public int numContinentes(){
        return continentes_.length;
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
     * Escreve a tabela de países
     */
    public void printTabela(){
        for(int i = 0; i < tabela_mapa_.length; i++){
            for(int j = 0; j < tabela_mapa_[i].length; j++){
                if(tabela_mapa_[i][j][0] == -1){
                    System.out.print("--------| ");
                }
                else {
                    try {
                        System.out.print(getTerritorio(tabela_mapa_[i][j][0], tabela_mapa_[i][j][1]).getNome() + "| ");
                    } catch (MapaException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * Cria uma instância do mapa com o caminho para a sua leitura
     * @param map_file O caminho para o arquivo
     */
    Mapa(String map_file, String table_file){
        map_file_ = map_file;
        table_file_ = table_file;

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

    /**
     * Define a tabela que organiza os territórios e cria
     * @param tabela A tabela cosntruída
     */
    void setTabela(int[][][] tabela){
        tabela_mapa_ = tabela;
    }
}
