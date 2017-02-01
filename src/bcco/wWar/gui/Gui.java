package bcco.wWar.gui;

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/** Controla a interface com o usuario
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Gui {
    private JFrame janela_;

    private int screen_width_;
    private int screen_heigth_;
    private String titulo_;

    private Game game_;
    private MapTable tabela_mapa_;

    private boolean atacando_ = false;
    private Territorio selecionado_ = null;
    private String tipo_exerc;

    /**
     * Cria as estruturas basicas para a janela_
     * @param titulo O titulo da janela_
     * @param screen_width A largura
     * @param screen_height A altura
     * @param game O jogo a qual esta janela_ esta ligado
     */
    public Gui(String titulo, int screen_width, int screen_height, Game game){
        game_ = game;
        tabela_mapa_ = new MapTable(game_.getMapa());

        screen_width_ = screen_width;
        screen_heigth_ = screen_height;
        janela_ = new JFrame(titulo);

        janela_.getContentPane().setLayout(new GridBagLayout());
        janela_.setSize(screen_width_, screen_heigth_);
        //Posiciona a tela no centro
        janela_.setLocationRelativeTo(null);
        janela_.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Mostra a janela_
     */
    public void show(){
        janela_.setVisible(true);

    }

    /**
     * Esconde a janela_
     */
    private void hide(){
        janela_.setVisible(false);
    }

    private void clear(){
        Component[] componentes = janela_.getContentPane().getComponents();

        for(Component c : componentes){
            janela_.getContentPane().remove(c);
        }
    }

    public void telaInicial(){
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane = new JPanel(new GridBagLayout());

        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel inicio = new JLabel("Bem vindo ao War Game");
        pane.add(inicio, c);

        JButton inicia = new JButton("Inciar jogo");

        //Cria uma classe anônima
        ActionListener sairListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game_.iniciarJogo();
                clear();
                telaJogo();
            }
        };
        inicia.addActionListener(sairListener);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        pane.add(inicia, c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        janela_.getContentPane().add(pane, c);

    }

    //Separa a tela em 2 panes para centralização
    private void telaJogo(){
        GridBagConstraints c = new GridBagConstraints();

        //Panes
        JPanel title_pane = new JPanel(new GridBagLayout());
        JPanel table_pane = new JPanel(new GridBagLayout());

        //Labels + tabela + botões
        JLabel titulo = new JLabel("War game");
        JLabel rodada = new JLabel("Vez de " + game_.getJogadorDaVez().getNome());
        JTable tabela = new JTable(tabela_mapa_);
        JLabel pais = new JLabel("País selecionado : ");
        JLabel dono = new JLabel("Dono: ");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel vizinhos = new JLabel("Vizinhos: ");
        JButton prox_rodada = new JButton("Terminar rodada");
        JButton atacar = new JButton("Iniciar ataque");

        //Listeners
        tabela.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int row = tabela.rowAtPoint(e.getPoint());
                        int col = tabela.columnAtPoint(e.getPoint());

                        if(game_.getMapa().getTablaMapa()[row][col][0] != -1) {
                            try {
                                selecionado_ = game_.getMapa().getTerritorio(game_.getMapa().getTablaMapa()[row][col][0],
                                        game_.getMapa().getTablaMapa()[row][col][1]);
                            } catch (MapaException e1) {
                                e1.printStackTrace();
                            }
                        }

                        updateInfos(pais, dono, num_terr, num_aereo, vizinhos, atacar, row, col);
                    }
                }
        );

        prox_rodada.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!atacando_) {
                            game_.mudaRodada();
                            rodada.setText("Vez de " + game_.getJogadorDaVez().getNome());

                            atacar.setEnabled(!atacar.isEnabled());
                        }
                        else{
                            JOptionPane.showMessageDialog(title_pane, "Ataque em andamento, cancele ou termine" +
                                    "antes de continuar");
                        }
                    }
                }
        );

        atacar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        atacar();
                    }
                }
        );

        //Titulo
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        title_pane.add(titulo, c);

        //Tebela + infos
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;

        table_pane.add(tabela, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.PAGE_START;

        table_pane.add(rodada, c);

        c.gridy = 1;

        table_pane.add(pais, c);

        c.gridy = 2;

        table_pane.add(dono, c);

        c.gridy = 3;

        table_pane.add(num_terr, c);

        c.gridy = 4;

        table_pane.add(num_aereo, c);

        c.gridy = 5;

        table_pane.add(vizinhos, c);

        c.gridy = 6;

        table_pane.add(prox_rodada, c);

        c.gridy = 7;

        atacar.setEnabled(false);
        table_pane.add(atacar, c);

        //Primeiro pane
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        janela_.getContentPane().add(title_pane, c);

        //Segundo pane
        c.gridx = 0;
        c.gridy = 1;

        janela_.getContentPane().add(table_pane, c);

        show();
    }

    private void updateInfos(JLabel pais, JLabel dono, JLabel num_terr, JLabel num_aereo, JLabel vizinhos, JButton atacar, int row, int col){
        if(game_.getMapa().getTablaMapa()[row][col][0] == -1){
            atacar.setEnabled(false);
            return;
        }

        Territorio t = null;
        try {
            t = game_.getMapa().getTerritorio(game_.getMapa().getTablaMapa()[row][col][0], game_.getMapa().getTablaMapa()[row][col][1]);
        } catch (MapaException e) {
            e.printStackTrace();
        }

        if(t != null) {
            if(t.getOcupante().getNome().equals(game_.getJogadorDaVez().getNome())){
                atacar.setEnabled(true);
            }
            else{
                atacar.setEnabled(false);
            }

            Territorio[] fronteira = t.getFronteira();

            pais.setText("Pais selecionao: " + t.getNome());
            dono.setText("Dono: " + t.getOcupante().getNome());
            num_terr.setText("Exércitos Terrestres: " + "3");
            num_aereo.setText("Exércitos Aéreos: " + "2");

            String f = "Vizinhos: ";
            for (Territorio aFronteira : fronteira) {
                f = f + aFronteira.getNome() + ", ";
            }

            vizinhos.setText(f);
        }
    }

    private void updateAtacado(JLabel dono, JLabel num_terr, JLabel num_aereo, Territorio t){

        dono.setText("Dono: " + t.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + "3");
        num_aereo.setText("Exércitos Aéreos: " + "2");

    }

    private void atacar(){
        atacando_ = true;

        List<Territorio> f = new ArrayList<>();
        for(Territorio t : selecionado_.getFronteira()){
          if(t.getOcupante() != game_.getJogadorDaVez()){
              f.add(t);
          }
        }

        if(f.size() == 0){
            JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com inimigos");
            atacando_ = false;
            return;
        }

        Territorio[] fronteiras = new Territorio[f.size()];
        for(int i = 0; i < fronteiras.length; i++){
            fronteiras[i] = f.get(i);
        }

        JFrame frame = new JFrame("Atacar");
        frame.getContentPane().setLayout(new GridBagLayout());

        JPanel pane = new JPanel(new GridBagLayout());

        JLabel titulo = new JLabel("Atacando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Atacar: ");
        JLabel dono = new JLabel("Dono: ");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel num = new JLabel("Quantidade: ");
        JComboBox<Territorio> combo = new JComboBox<>(fronteiras);

        JRadioButton terr = new JRadioButton("Terrestre");
        terr.setActionCommand("Terrestre");

        JRadioButton aereo = new JRadioButton("Aereo");
        aereo.setActionCommand("Aereo");

        ButtonGroup bg = new ButtonGroup();
        bg.add(terr);
        bg.add(aereo);
        terr.doClick();

        JTextField num_exe = new JTextField(5);
        JButton ok = new JButton("Cofirmar");

        //Listeners
        combo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateAtacado(dono, num_terr, num_aereo, (Territorio)((JComboBox)e.getSource()).getSelectedItem());
                    }
                }
        );

        terr.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tipo_exerc = e.getActionCommand();
                    }
                }
        );

        aereo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tipo_exerc = e.getActionCommand();
                    }
                }
        );

        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(num_exe.getText());
                    }
                }
        );

        updateAtacado(dono, num_terr, num_aereo, (Territorio)(combo.getSelectedItem()));

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        pane.add(titulo, c);

        c.gridy = 1;

        pane.add(atacar, c);

        c.gridx = 1;

        pane.add(combo, c);

        c.gridx = 0;
        c.gridy = 2;

        pane.add(num_terr, c);

        c.gridx = 1;

        pane.add(num_aereo, c);

        c.gridx = 0;
        c.gridy = 3;

        pane.add(dono, c);

        c.gridy = 4;

        pane.add(terr, c);

        c.gridy = 5;

        pane.add(aereo, c);

        c.gridx = 1;
        c.gridy = 4;

        pane.add(num, c);

        c.gridx = 2;

        pane.add(num_exe, c);

        c.gridx = 0;
        c.gridy = 6;

        pane.add(ok, c);

        c.gridx = 0;
        c.gridy = 0;

        frame.getContentPane().add(pane, c);

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                atacando_ = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.pack();
        frame.setVisible(true);

    }

}
