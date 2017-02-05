package bcco.wWar.gui;

//Imports próprios

import bcco.wWar.game.Game;
import bcco.wWar.game.jogadores.Jogador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

//Imports do sistema

/**
 * Renderer da célula que especifica uma cor para a célula
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

        //Células são um JLabel
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        //Pega a tabela a qual ela está relacionada e chama a função de verificação
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

        //Retorna o label da célula
        return l;

    }
}

