package bcco.wWar.mapa;

import bcco.iomanager.IOManager;
import bcco.wWar.mapa.continentes.*;
import bcco.wWar.mapa.continentes.exceptions.*;
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
     * @param map_file O caminho para o arquivo a ser lido
     * @return A instância do mapa
     * @throws ConstrutorException caso houver algum erro na construção
     */
    public Mapa buildMap(String map_file) throws ConstrutorException {
        List<String> map_text = null;

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
            return build(map_file, map_text);
        }
    }

    /**
     * Construtor privado sem argumentos
     */
    private ConstrutorMapa(){

    }

    /**
     * Função privada que efetivamente constrói o mapa. Para informações de como construir o arquivo do mapa, ler map_help.txt
     * @param map_file O caminho para o mapa
     * @param map_text A lista de strings represetando o arquivo
     * @return A instância do map criado
     * @throws ConstrutorException caso haja algum erro na interpretação do mapa
     */
    private Mapa build(String map_file, List<String> map_text) throws ConstrutorException {
        //Efetivamente constrói o mapa.
        //Cria duas listas, uma para os continentes e uma para os teritórios
        //as popula conforme o arquivo do mapa e então retorna ele pronto.
        Mapa new_mapa = new Mapa(map_file);
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
                                //Cria uam lista e lê os caracteres da linha para definir as fronteiras
                                List<Territorio> fronteiras = new ArrayList<>();
                                Territorio atual;
                                int continente_index = (Character.getNumericValue(line.charAt(0))-1);
                                int territorio_index = (Character.getNumericValue(line.charAt(2))-1);
                                try {
                                    //Le o primeiro caracter, transforma-o em int e subtrai 1 (pois os indeces no arquivo
                                    // começam em 1), pega o continente respectivo, pula um caracter e lê o país respectivo da mesma maneira
                                    atual = continentes.get(continente_index).getTerritorio(territorio_index);
                                } catch (ContinenteException e) {
                                    System.out.println(e.getMessage());
                                    throw new ConstrutorException("Erro ao ler fronteira na linha " + (i + 1));
                                }

                                //For que lê a linha inteira. Pula 4 caracteres pois temos que ler 2 carateres separados
                                //por 1 espaço cada um. Adiciona o país selecionado na lista de fronteira
                                for(int j = 4; j < line.length(); j = j+4){
                                    continente_index = (Character.getNumericValue(line.charAt(j))-1);
                                    territorio_index = (Character.getNumericValue(line.charAt(j+2))-1);
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

        return new_mapa;
    }
}
