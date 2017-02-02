package bcco.wWar.gui;

import bcco.wWar.mapa.Mapa;
import bcco.wWar.mapa.continentes.exceptions.ContinenteException;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.swing.table.AbstractTableModel;

/**
 * @author Cristofer Oswald
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
}
