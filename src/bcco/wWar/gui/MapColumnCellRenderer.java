package bcco.wWar.gui;

import bcco.wWar.game.Game;
import bcco.wWar.game.jogadores.Jogador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Cristofer Oswald
 * @since 04/02/17
 */

public class MapColumnCellRenderer extends DefaultTableCellRenderer {
    private Game game_;

    MapColumnCellRenderer(Game game){
        game_ = game;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        //Cells are by default rendered as a JLabel.
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        //Get the status for the current row.
        MapTable tableModel = (MapTable) table.getModel();
        Jogador ocupante = tableModel.getOcupante(row, col);
        if(ocupante == null){
            l.setBackground(Color.CYAN);
        }
        else if(ocupante.equals(game_.getHumano())) {
            l.setBackground(Color.GREEN);
        } else {
            l.setBackground(Color.RED);
        }

        //Return the JLabel which renders the cell.
        return l;

    }
}

