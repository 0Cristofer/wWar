package bcco.wWar.gui;

//Impots próprios

import bcco.wWar.game.jogadores.Jogador;
import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.swing.table.AbstractTableModel;

//Imports do sistema

/**
 * Modelo de tabela usado para criar o JTable utilizado na GUI
 * @author Cristofer Oswald e Bruno Cesar
 * @since 30/01/17
 */
public class MapTable extends AbstractTableModel{
    private Mapa mapa_;

    /**
     * Cria uma tabela a partir de um mapa
     * @param mapa O mapa do jogo
     */
    MapTable(Mapa mapa){
        mapa_ = mapa;
    }

    @Override
    public int getRowCount() {
        return mapa_.getTablaMapa().length;
    }

    @Override
    public int getColumnCount() {
        return mapa_.getTablaMapa()[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String nome = null;
        if(mapa_.getTablaMapa()[rowIndex][columnIndex][0] == -1){
            return "";
        }
        try {
            nome = mapa_.getContinente(mapa_.getTablaMapa()[rowIndex][columnIndex][0]).getTerritorio(mapa_.getTablaMapa()[rowIndex][columnIndex][1]).getNome();
        } catch (ContinenteException | MapaException e) {
            System.out.println(e.getMessage());
        }
        return nome;
    }

    /**
     * Verifica qual é o ocupante de uma célula
     * @param row A linha da tabela
     * @param col A coluna da tabela
     * @return O jogador que ocupa aquela posição. Null se não houver dono.
     */
    Jogador getOcupante(int row, int col){
        Jogador jogador = null;
        if(mapa_.getTablaMapa()[row][col][0] != -1) {

            try {
                jogador = mapa_.getContinente(mapa_.getTablaMapa()[row][col][0]).getTerritorio(mapa_.getTablaMapa()[row][col][1]).getOcupante();
            } catch (ContinenteException | MapaException e) {
                e.printStackTrace();
            }
        }
        return jogador;
    }
}