package bcco.wWar.mapa;

import bcco.iomanager.IOManager;
import bcco.wWar.mapa.continentes.Continente;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.continentes.exceptions.TerritorioException;
import bcco.wWar.mapa.exceptions.ConstrutorException;
import bcco.wWar.mapa.exceptions.MapaException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Singleton responsável por contruir o mapa
 *  @author Cristofer Oswald
 *  @since 13/01/17
 */
public class ConstrutorMapa {

    //Flags utilizadas para ler o arquivo
    private static final String MAPA_INICIO_ = "MAPAINICIO";
    private static final String MAPA_FIM_ = "MAPAFIM";
    private static final String CONTINENTE_INICIO_ = "CONTINENTEINICIO";
    private static final String CONTINENTE_FIM_ = "CONTINENTEFIM";
    private static final String FRONTEIRAS_INICIO_ = "FRONTEIRASINICIO";
    private static final String FRONTEIRAS_FIM_ = "FRONTEIRASFIM";
    private static final String TABELA_INICIO_ = "MAPATINICIO";
    private static final String TABELA_FIM_ = "MAPATFIM";


    private static ConstrutorMapa instance_;

    /**
     * @return O singleton do construtorMapa
     */
    public static ConstrutorMapa getInstance(){
        if(instance_ == null){
            instance_ = new ConstrutorMapa();
        }
        return instance_;
    }

    /**
     * Constrói uma instância do Mapa a partir de um arquivo
     * @param map_file O caminho para o arquivo de mapa a ser lido
     * @param table_file O caminha para o arquivo da tabela a ser lido
     * @return A instância do mapa
     * @throws ConstrutorException caso houver algum erro na construção
     */
    public Mapa buildMap(String map_file, String table_file) throws ConstrutorException {
        List<String> map_text = null;
        List<String> table_text = null;
        Mapa new_mapa = new Mapa(map_file, table_file);

        //Le o arquivo de entrada
        try {
            map_text = IOManager.getInstance().readTextFile(map_file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if(map_text == null){
            throw new ConstrutorException();
        }
        else{
            buildM(map_file, map_text, new_mapa);

            //Le o arquivo de entrada
            try {
                table_text = IOManager.getInstance().readTextFile(table_file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            if(table_text == null){
                throw new ConstrutorException();
            }
            else{
                new_mapa.setTabela(buildTable(table_text));
            }

            return new_mapa;
        }
    }

    /**
     * Função privada que efetivamente constrói o mapa. Para informações de como construir o arquivo do mapa, ler map_help.txt
     * @param map_file O caminho para o mapa
     * @param map_text A lista de strings represetando o arquivo
     * @throws ConstrutorException caso haja algum erro na interpretação do mapa
     */
    private void buildM(String map_file, List<String> map_text, Mapa new_mapa) throws ConstrutorException {
        //Efetivamente constrói o mapa.
        //Cria duas listas, uma para os continentes e uma para os teritórios
        //as popula conforme o arquivo do mapa e então retorna ele pronto.
        List<Continente> continentes = new ArrayList<>();
        List<Territorio> territorios = new ArrayList<>();

        boolean is_started = false;
        boolean new_continente = false;
        boolean is_fronteiras = false;
        int num_continentes = 0;
        for (int i = 0; i < map_text.size(); i++) {
            String line = map_text.get(i);

            //Verifica se o mapa foi 'iniciado'
            if (!is_started) {
                if (line.equals(MAPA_INICIO_)) {
                    is_started = true;
                }
                else{
                    throw new ConstrutorException("Mapa " + map_file + " sem inicio/má formatação");
                }
            }
            else{
                //Se não estamos lendo um continente
                if(!new_continente){
                    switch (line) {
                        case CONTINENTE_INICIO_:
                            //Diz que estamos lendo um continente
                            //Lê a próxima linha, que deve conter seu nome, e pula para a próxima linha
                            //Caso chegar no fim do arquivo ou a conter algo invalido devolve erro
                            new_continente = true;
                            if (i + 1 == map_text.size()) {
                                throw new ConstrutorException("Mapa com erro na linha " + (i + 2) + ". Fim prematuro.");
                            } else {
                                String next_line = map_text.get(i + 1);
                                switch (next_line){
                                    case MAPA_INICIO_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    case MAPA_FIM_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    case CONTINENTE_INICIO_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    case CONTINENTE_FIM_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    case FRONTEIRAS_INICIO_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    case FRONTEIRAS_FIM_:
                                        throw new ConstrutorException("Mapa com erro na linha " + (i + 2));
                                    default:
                                        continentes.add(new Continente(next_line));
                                        num_continentes++;
                                        break;
                                }
                                i++;
                            }
                            break;
                        case FRONTEIRAS_INICIO_:
                            if(!is_fronteiras){
                                is_fronteiras = true;
                                if(continentes.size() == 0){
                                    throw new ConstrutorException("Mapa com erro na linha " + (i + 1) + ". Continentes devem ser definidos antes das fronteiras");
                                }
                            }
                            else{
                                throw new ConstrutorException("Mapa com erro na linha " + (i + 1) + ". Fronteiras iniciadas mais de uma vez");
                            }
                            break;
                        case FRONTEIRAS_FIM_:
                            is_fronteiras = false;
                            break;
                        case MAPA_FIM_:
                            //Se chagamos no fim do mapa configuramos a instâcia e limpamos a lista
                            if(!is_fronteiras){
                                is_started = false;
                                try {
                                    new_mapa.setContinentes(continentes);
                                    continentes.clear();
                                } catch (MapaException e) {
                                    throw new ConstrutorException(e.getMessage(), e);
                                }
                            }
                            else{
                                throw new ConstrutorException("Mapa com erro na linha " + (i + 1) + ". Fim do mapa com leitura em andamento");
                            }
                            break;
                        default:
                            //Se estamos lendo as fronteiras
                            if(is_fronteiras){
                                //Cria uma lista e lê os caracteres da linha para definir as fronteiras
                                List<Territorio> fronteiras = new ArrayList<>();
                                Territorio atual;

                                //Divide a linha por espaços
                                String[] div = line.split(" ");

                                //Se o tamanho da linha não for nulo, transformas as substrings em inteiros
                                if(div.length < 1){
                                    throw new ConstrutorException("Mapa com erro na linha " + (i + 1) + ", linha vazia");
                                }
                                else{
                                    int[] vals = new int[div.length];
                                    for (int j = 0; j < vals.length; j++) {
                                        vals[j] = Integer.parseInt(div[j]);
                                    }

                                    //O primeiro e o segundo valor da linha representam o continente e o terrotório
                                    //ao qual essa fronteira se refere
                                    int continente_index = vals[0] - 1;
                                    int territorio_index = vals[1] - 1;
                                    try {
                                        atual = continentes.get(continente_index).getTerritorio(territorio_index);
                                    }
                                    catch (IndexOutOfBoundsException ie){
                                        System.out.println(ie.getMessage());
                                        throw new ConstrutorException("Index errado de continente na linha " + (i + 1));
                                    }
                                    catch (ContinenteException e) {
                                        System.out.println(e.getMessage());
                                        throw new ConstrutorException("Erro ao ler fronteira na linha " + (i + 1));
                                    }

                                    //For que lê a linha inteira. Pula 4 caracteres pois temos que ler 2 carateres separados
                                    //por 1 espaço cada um. Adiciona o país selecionado na lista de fronteira
                                    for(int j = 2; j < vals.length; j = j + 2){
                                        continente_index = vals[j] - 1;
                                        territorio_index = vals[j+1] - 1;
                                        try {
                                            fronteiras.add(continentes.get(continente_index).getTerritorio(territorio_index));
                                        } catch (ContinenteException e) {
                                            System.out.println(e.getMessage());
                                            throw new ConstrutorException("Erro ao ler fronteira na linha " + (i + 1));
                                        }
                                    }

                                    //Após ler toda a linha, atribui a fronteira ao país de destino
                                    try {
                                        atual.setFronteira(fronteiras);
                                    } catch (TerritorioException e) {
                                        throw new ConstrutorException(e.getMessage());
                                    }
                                }
                            }
                            else{
                                throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                            }
                            break;
                    }
                }
                else{
                    switch (line){
                        //Se terminamos de ler um continente atribui a lista de países
                        //lidos ao continente e limpa a lista
                        case CONTINENTE_FIM_:
                            new_continente = false;
                            try {
                                continentes.get(num_continentes-1).setTerritorios(territorios);
                            } catch (ContinenteException e) {
                                throw new ConstrutorException(e.getMessage());
                            }
                            territorios.clear();
                            break;
                        case MAPA_INICIO_:
                            throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                        case MAPA_FIM_:
                            throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                        case CONTINENTE_INICIO_:
                            throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                        case FRONTEIRAS_INICIO_:
                            throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                        case FRONTEIRAS_FIM_:
                            throw new ConstrutorException("Mapa com erro na linha " + (i + 1));
                        default:
                            //Lê a linha e a atribui como nome do novo país
                            territorios.add(new Territorio(continentes.get(num_continentes-1), line));
                            break;
                    }
                }
            }
        }

    }


    /**
     * Cria uma tabela de duplas que representa o mapa na interface
     * @param text A lista de Strings que formam a tabela
     * @return A tabela construída
     * @throws ConstrutorException Ao haver erro na leitura
     */
    private int[][][] buildTable(List<String> text) throws ConstrutorException {
        //Cria uma matriz com o tamanho do arquivo -2 linhas.
        //-2 pois a primeira e última linha serão flags
        int[][][] tabela = new int[(text.size() - 2)][][];

        boolean is_table = false;
        int j = 0;
        for(int i = 0; i < text.size(); i++){
            String line = text.get(i);

            //Verifica se estamos lendo uma tabela
            if(is_table) {
                if(line.equals(TABELA_FIM_)){
                    is_table = false;
                }
                else{
                    //Lê a linha e a divide
                    String[] div = line.split(" ");

                    if(div.length < 1){
                        throw new ConstrutorException("Erro ao ler tabela na linha " + (i + 1) + ", linha vazia");
                    }
                    else{
                        //Cria a linha na tabela do tamanho equivalente a metade da
                        //linha lida pois a cada 2 valores formam uma cédula
                        tabela[j] = new int[(div.length/2)][];

                        //Lê o vetor de valores retirado do arquivo
                        //Cada célula é uma dupla
                        int l = 0;
                        for(int k = 0; k < div.length; k = k + 2){
                            tabela[j][l] = new int[2];
                            tabela[j][l][0] = Integer.parseInt(div[k]) -1;
                            tabela[j][l][1] = Integer.parseInt(div[k+1]) -1;
                            l++;
                        }
                        j++;
                    }
                }
            }
            else{
                if(line.equals(TABELA_INICIO_)){
                    is_table = true;
                }
                else{
                    throw new ConstrutorException("Tabela com erro na linha " + (i + 1) + ", tabela sem começo");
                }
            }
        }

        return tabela;
    }

    /**
     * Construtor privado sem argumentos
     */
    private ConstrutorMapa(){

    }
}
