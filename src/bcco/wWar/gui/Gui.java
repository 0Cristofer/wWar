package bcco.wWar.gui;

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
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
    private String tipo_exerc_;

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

        //Configura a janela
        screen_width_ = screen_width;
        screen_heigth_ = screen_height;
        janela_ = new JFrame(titulo);

        //Define o contentPane como um JPanel com background
        try {
            final Image background_image = ImageIO.read(new File("SketchWarGame.png"));
            janela_.setContentPane(new JPanel(new GridBagLayout()) {
                @Override
                public void paintComponent(Graphics g) {
                    g.drawImage(background_image, 0, 0, null);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        janela_.setSize(screen_width_, screen_heigth_);
        //Posiciona a tela no centro
        janela_.setLocationRelativeTo(null);
        janela_.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Mostra a janela
     */
    public void show(){
        janela_.setVisible(true);

    }

    /**
     * Esconde a janela
     */
    private void hide(){
        janela_.setVisible(false);
    }

    /**
     * Remove todos os componentes da tela
     */
    private void clear(){
        Component[] componentes = janela_.getContentPane().getComponents();

        for(Component c : componentes){
            janela_.getContentPane().remove(c);
        }
    }

    /**
     * Mostra a tela inicial
     */
    public void telaInicial(){
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane = new JPanel(new GridBagLayout());

        //Componentes
        JLabel inicio = new JLabel("Bem vindo ao War Game");
        JButton inicia = new JButton("Inciar jogo");

        //Listeners
        inicia.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game_.iniciarJogo();
                        clear();
                        telaJogo();
                    }
                }
        );

        //Layout
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(inicio, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        pane.add(inicia, c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;

        //Adiciona tudo ao pane principal
        janela_.getContentPane().add(pane, c);

    }

    /**
     * Tela principal de jogo
     */
    private void telaJogo(){
        GridBagConstraints c = new GridBagConstraints();

        JPanel table_pane = new JPanel(new GridBagLayout());

        //Componentes
        JLabel vez_de = new JLabel("Vez de " + game_.getJogadorDaVez().getNome());
        JTable tabela = new JTable(tabela_mapa_);
        JLabel pais = new JLabel("País selecionado : ");
        JLabel dono = new JLabel("Dono: ");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel vizinhos = new JLabel("Vizinhos: ");
        JButton prox_rodada = new JButton("Terminar rodada");
        JButton atacar = new JButton("Iniciar ataque");

        atacar.setEnabled(false); //Desativado por default

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
                        else{
                            selecionado_ = null;
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
                            vez_de.setText("Vez de " + game_.getJogadorDaVez().getNome());

                            if(selecionado_ != null){
                                atacar.setEnabled(!atacar.isEnabled());
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(table_pane, "Ataque em andamento, cancele ou termine" +
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

        //Layout
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;

        table_pane.add(tabela, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;

        table_pane.add(vez_de, c);

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
        c.anchor = GridBagConstraints.CENTER;

        table_pane.add(prox_rodada, c);

        c.gridy = 7;

        table_pane.add(atacar, c);

        janela_.getContentPane().add(table_pane);

        show();
    }

    /**
     * Atualiza as informações mostradas na tela de jogo
     * @param pais O label do país
     * @param dono O label do dono
     * @param num_terr O label referente ao número de exércitos terrestres
     * @param num_aereo O label referente ao número de exércitos aéreos
     * @param vizinhos Label que mostra os países vizinhos
     * @param atacar Botão que é ativado ou não dependendo das informações
     * @param row Linha da tabela selecionada
     * @param col Coluna da tabela selecionada
     */
    private void updateInfos(JLabel pais, JLabel dono, JLabel num_terr, JLabel num_aereo, JLabel vizinhos, JButton atacar, int row, int col){
        //Se a célula for vazia desativa o botão
        if(game_.getMapa().getTablaMapa()[row][col][0] == -1){
            atacar.setEnabled(false);
            return;
        }

        //Lê o território na respectiva célula
        Territorio t = null;
        try {
            t = game_.getMapa().getTerritorio(game_.getMapa().getTablaMapa()[row][col][0], game_.getMapa().getTablaMapa()[row][col][1]);
        } catch (MapaException e) {
            e.printStackTrace();
        }

        //Atualiza os campos com os respectivos dados
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

    /**
     * Atualiza os dados da tela de ataque
     * @param dono Label do dono do território
     * @param num_terr O label do número de exércitos terrestres
     * @param num_aereo Label referente ao número de exércitos aéros
     * @param t Território utilizado para atualizar os dados
     */
    private void updateAtacado(JLabel dono, JLabel num_terr, JLabel num_aereo, Territorio t){
        dono.setText("Dono: " + t.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + "3");
        num_aereo.setText("Exércitos Aéreos: " + "2");
    }

    /**
     * Mostra a tela poup-up de ataque
     */
    private void atacar(){
        atacando_ = true;

        //Verifica quantos e quais territórios estão disponíveis para atacar (são inimigos)
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

        //Cria a nova janela
        JFrame frame = new JFrame("Atacar");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
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

        GridBagConstraints c = new GridBagConstraints();

        //Componentes
        JLabel titulo = new JLabel("Atacando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Atacar: ");
        JLabel dono = new JLabel("Dono: ");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel num = new JLabel("Quantidade: ");
        JComboBox<Territorio> combo = new JComboBox<>(fronteiras);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton terr = new JRadioButton("Terrestre");
        JRadioButton aereo = new JRadioButton("Aereo");

        JTextField num_exe = new JTextField(5);
        JButton ok = new JButton("Cofirmar");

        //Configur os radio buttons
        terr.setActionCommand("Terrestre");
        aereo.setActionCommand("Aereo");
        terr.doClick(); //Simula um clique
        bg.add(terr);
        bg.add(aereo);

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
                        tipo_exerc_ = e.getActionCommand();
                    }
                }
        );

        aereo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tipo_exerc_ = e.getActionCommand();
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

        //Layout
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

        frame.pack();
        frame.setVisible(true);

    }

}
