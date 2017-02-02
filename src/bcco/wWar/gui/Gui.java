package bcco.wWar.gui;

import bcco.wWar.game.Game;
import bcco.wWar.mapa.continentes.Territorio;
import bcco.wWar.mapa.exceptions.MapaException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private boolean terminado_ataque_ = false;
    private boolean movimentando_ = false;
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
            final Image background_image = ImageIO.read(new File("tela_inicial.png"));
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
     * Mostra a tela inicial
     */
    public void telaInicial(){
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane = new JPanel(new GridBagLayout());

        //Componentes
        JLabel nome = new JLabel("Insira seu nome:");
        JTextField nome_input = new JTextField(10);
        JLabel inicio = new JLabel("Bem vindo ao War Game");
        JButton inicia = new JButton("Inciar jogo");

        //Listeners
        inicia.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game_.iniciarJogo(nome_input.getText());
                        clear();
                        distribuirExercito(); //Arrumar
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

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;

        pane.add(nome, c);

        c.gridx = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        pane.add(nome_input,c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;

        pane.add(inicia, c);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        c.fill = GridBagConstraints.BOTH;

        //Adiciona tudo ao pane principal
        janela_.getContentPane().add(pane, c);

    }

    /**
     * Tela principal de jogo
     */
    private void telaJogo(){
        try {
            final Image background_image = ImageIO.read(new File("tela_jogo.png"));
            janela_.setContentPane(new JPanel(new GridBagLayout()) {
                @Override
                public void paintComponent(Graphics g) {
                    g.drawImage(background_image, 0, 0, null);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GridBagConstraints c = new GridBagConstraints();

        JPanel tabela_pane = new JPanel(new GridBagLayout());
        JPanel jogador_pane = new JPanel(new GridBagLayout());
        JPanel cpu_pane = new JPanel(new GridBagLayout());

        //Componentes
        JLabel jogador_nome = new JLabel(game_.getHumano().getNome());
        JLabel jogador_num_territorios = new JLabel("Número de territórios: ");
        JLabel jogador_num_continentes = new JLabel("Número de continentes: ");
        JLabel jogador_num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel jogador_num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel jogador_selecao_titulo = new JLabel("Informções do país");
        JLabel jogador_selecao_nome_pais = new JLabel("Nome: Nenhum");
        JLabel jogador_selecao_vizinhos = new JLabel("Vizinhos: Nenhum");
        JLabel jogador_selecao_num_terr = new JLabel("Exércitos Terrestres: 0");
        JLabel jogador_selecao_num_aereo = new JLabel("Exércitos Aéreos: 0");
        JButton jogador_atacar = new JButton("Iniciar ataque");
        JButton jogador_movimentar = new JButton("Movimentar tropas");
        JLabel cpu_nome = new JLabel(game_.getPc().getNome());
        JLabel cpu_num_territorios = new JLabel("Número de territórios: ");
        JLabel cpu_num_continentes = new JLabel("Número de continentes: ");
        JLabel cpu_num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel cpu_num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel cpu_selecao_titulo = new JLabel("Informções do país");
        JLabel cpu_selecao_nome_pais = new JLabel("Nome: Nenhum");
        JLabel cpu_selecao_vizinhos = new JLabel("Vizinhos: Nenhum");
        JLabel cpu_selecao_num_terr = new JLabel("Exércitos Terrestres: 0");
        JLabel cpu_selecao_num_aereo = new JLabel("Exércitos Aéreos: 0");
        JTable tabela = new JTable(tabela_mapa_);
        JButton prox_rodada = new JButton("Terminar rodada");

        //Botões desativados
        jogador_atacar.setEnabled(false);
        jogador_movimentar.setEnabled(false);

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

                            if(selecionado_.getOcupante() == game_.getHumano()){
                                updateInfos(jogador_selecao_nome_pais, jogador_num_terr,
                                        jogador_num_aereo, jogador_selecao_vizinhos, jogador_atacar, jogador_movimentar, row, col);
                                cpu_selecao_nome_pais.setText("Nome: Nenhum");
                                cpu_num_terr.setText("Exércitos Terrestres: 0");
                                cpu_num_aereo.setText("Exércitos Aéreo: 0");
                                cpu_selecao_vizinhos.setText("Vizinhos: Nenhum");
                            }
                            else {
                                updateInfos(cpu_selecao_nome_pais, cpu_num_terr,
                                        cpu_num_aereo, cpu_selecao_vizinhos, jogador_atacar, jogador_movimentar, row, col);
                                jogador_selecao_nome_pais.setText("Nome: Nenhum");
                                jogador_num_terr.setText("Exércitos Terrestres: 0");
                                jogador_num_aereo.setText("Exércitos Aéreo: 0");
                                jogador_selecao_vizinhos.setText("Vizinhos: Nenhum");
                            }
                        }
                        else{
                            selecionado_ = null;
                        }

                    }
                }
        );

        prox_rodada.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if((!atacando_) && (!movimentando_)) {
                            if(JOptionPane.showConfirmDialog(tabela_pane,
                                    "Tem certeza que deseja terminar a rodada?",
                                    "Tem certeza?", JOptionPane.YES_NO_OPTION) ==
                                    JOptionPane.YES_OPTION){

                                game_.mudaRodada();
                                jogador_nome.setText("Vez de " + game_.getJogadorDaVez().getNome());

                                if(selecionado_ != null){
                                    jogador_atacar.setEnabled(!jogador_atacar.isEnabled());
                                    jogador_movimentar.setEnabled(!jogador_movimentar.isEnabled());
                                }
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(tabela_pane, "Ataque ou movimentação em andamento, " +
                                    "cancele ou termine antes de continuar");
                        }

                    }
                }
        );

        jogador_atacar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(terminado_ataque_){
                            JOptionPane.showMessageDialog(tabela_pane, "Você começou a movimentar tropas," +
                                    "não pode mais atacar");
                        }
                        else{
                            atacar();
                        }
                    }
                }
        );

        jogador_movimentar.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!terminado_ataque_) {
                            if (JOptionPane.showConfirmDialog(tabela_pane,
                                    "Tem certeza que deseja movimentar? Você não poderá mais atacar",
                                    "Tem certeza?", JOptionPane.YES_NO_OPTION) ==
                                    JOptionPane.YES_OPTION) {
                                terminado_ataque_ = true;
                                movimentar();
                            }
                        }
                        else{
                            movimentar();
                        }
                    }
                }
        );

        //Layout
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;

        jogador_nome.setFont(new Font(jogador_nome.getFont().getName(), Font.BOLD, 24));
        jogador_pane.add(jogador_nome, c);

        c.gridy = 1;

        jogador_pane.add(jogador_num_territorios, c);

        c.gridy = 2;

        jogador_pane.add(jogador_num_continentes, c);

        c.gridy = 3;

        jogador_pane.add(jogador_num_terr, c);

        c.gridy = 4;
        c.insets = new Insets(5, 5, 15, 5);

        jogador_pane.add(jogador_num_aereo, c);

        c.gridy = 5;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(5, 5, 5, 5);

        jogador_pane.add(jogador_selecao_titulo, c);

        c.gridy = 6;
        c.anchor = GridBagConstraints.LINE_START;

        jogador_pane.add(jogador_selecao_nome_pais, c);

        c.gridy = 7;

        jogador_pane.add(jogador_selecao_num_terr, c);

        c.gridy = 8;

        jogador_pane.add(jogador_selecao_num_aereo, c);

        c.gridy = 9;

        jogador_pane.add(jogador_selecao_vizinhos, c);

        c.gridy = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.VERTICAL;

        jogador_pane.add(jogador_atacar, c);

        c.gridy = 11;

        jogador_pane.add(jogador_movimentar, c);

        //Pane tabela
        c.gridx = 0;
        c.gridy = 0;

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela_pane.add(tabela, c);

        //Pane cpu
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;

        cpu_nome.setFont(new Font(cpu_nome.getFont().getName(), Font.BOLD, 24));
        cpu_pane.add(cpu_nome, c);

        c.gridy = 1;

        cpu_pane.add(cpu_num_territorios, c);

        c.gridy = 2;

        cpu_pane.add(cpu_num_continentes, c);

        c.gridy = 3;

        cpu_pane.add(cpu_num_terr, c);

        c.gridy = 4;
        c.insets = new Insets(5, 5, 15, 5);

        cpu_pane.add(cpu_num_aereo, c);

        c.gridy = 5;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(5, 5, 5, 5);

        cpu_pane.add(cpu_selecao_titulo, c);

        c.gridy = 6;
        c.anchor = GridBagConstraints.LINE_START;

        cpu_pane.add(cpu_selecao_nome_pais, c);

        c.gridy = 7;

        cpu_pane.add(cpu_selecao_num_terr, c);

        c.gridy = 8;

        cpu_pane.add(cpu_selecao_num_aereo, c);

        c.gridy = 9;

        cpu_pane.add(cpu_selecao_vizinhos, c);

        c.insets = new Insets(280, -100, 5, 50);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;

        jogador_pane.setOpaque(false);
        janela_.getContentPane().add(jogador_pane, c);

        c.insets = new Insets(580, 0, 5, 0);
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;

        tabela_pane.setOpaque(false);
        janela_.getContentPane().add(tabela_pane, c);

        c.insets = new Insets(280, 50, 5, 0);
        c.gridx = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        cpu_pane.setOpaque(false);
        janela_.getContentPane().add(cpu_pane, c);

        show();
    }


    /**
     * Tela de distribuição de exército
     */
    public void distribuirExercito(){
        int qtd_recebida = game_.getHumano().getTerrestres_recebidos_();
        int soma = 0;
        int i = 1;
        List<Territorio> territorios = game_.getTerritorios(game_.getHumano());

        JFrame frame = new JFrame("Distribuir Exércitos");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        class InListener implements DocumentListener{
            private JTextField text;

            InListener(JTextField text) {
                this.text = text;
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                check();
            }

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                check();

            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                check();

            }

            private void check(){
                Runnable doCheck = new Runnable() {
                    @Override
                    public void run() {
                        int v = 0;
                        try {
                            v = Integer.parseInt(text.getText());
                        } catch (NumberFormatException e) {
                            text.setText("0");
                            JOptionPane.showMessageDialog(null, "Erro, valor não numérico!",
                                    "ERRO",JOptionPane.OK_OPTION);
                            return;
                        }


                        if (v > qtd_recebida){
                            text.setText("0");
                            JOptionPane.showMessageDialog(null, "Erro, valor maior que total de" +
                                    " exércitos disponiveis!","ERRO",JOptionPane.OK_OPTION);
                        }
                    }
                };
                SwingUtilities.invokeLater(doCheck);
            }
        }


        class distribuir implements ActionListener{
            int soma;
            List<Integer> valores;
            List<JTextField> textFields;

            distribuir(List<JTextField> textFields){
                this.textFields = textFields;
                valores = new ArrayList<>();
            }


            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                soma = 0;

                for (JTextField textField: textFields) {
                    valores.add(Integer.parseInt(textField.getText()));
                    soma += Integer.parseInt(textField.getText());
                }

                if (soma > qtd_recebida){
                    JOptionPane.showMessageDialog(null, "Erro, valor total da distribuição maior que" +
                            " total de exércitos disponiveis!","ERRO",JOptionPane.OK_OPTION);
                } else if (soma == qtd_recebida){
                    JOptionPane.showMessageDialog(null, "Tropas distribuídas!",
                            "Sucesso",JOptionPane.OK_CANCEL_OPTION);

                    game_.distribuirExercitos(valores);
                    frame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Sobraram algumas tropas sem distribuição," +
                            " não abandone-as!","Atenção",JOptionPane.OK_OPTION);
                }
            }
        }


        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.gridx = 0;

        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridx = 1;

        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(5, 5, 5, 5);
        c3.gridx = 2;

        JLabel d_nome = new JLabel("Nome território");
        JLabel d_qtd = new JLabel("Quantidade de tropas");
        JLabel d_in = new JLabel("Adicionar");

        c1.gridy = 0;
        c2.gridy = 0;
        c3.gridy = 0;

        pane.add(d_nome,c1);
        pane.add(d_qtd,c3);
        pane.add(d_in,c2);
        frame.getContentPane().add(pane, c1);

        List<JTextField> textFields = new ArrayList<>();

        //Componentes
        for (Territorio t : territorios) {
            JLabel nome = new JLabel(t.getNome());
            JLabel qtd = new JLabel(Integer.toString(t.getNumExTerrestres()));
            JTextField in = new JTextField(3);
            in.setText("0");
            in.getDocument().addDocumentListener(new InListener(in));

            c1.gridy = i;
            c2.gridy = i;
            c3.gridy = i;

            pane.add(nome,c1);
            pane.add(qtd,c3);
            pane.add(in,c2);
            i++;

            textFields.add(in);
        }

        JLabel ex_restante = new JLabel("Exercitos terrestres restantes: " + Integer.toString(game_.getHumano().getTerrestres_recebidos_()) );
        JButton distribuir = new JButton("Distribuir exércitos");
        distribuir.addActionListener(new distribuir(textFields));

        c3.gridy = i + 1;
        c1.gridy = i + 1;

        pane.add(ex_restante, c1);
        pane.add(distribuir, c3);

        frame.getContentPane().add(pane, c1);
        frame.pack();

        frame.setVisible(true);

    }

    /**
     * Tela de pop-up de ataque
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
                        updatePopUpInfo(dono, num_terr, num_aereo, (Territorio)((JComboBox)e.getSource()).getSelectedItem());
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

        updatePopUpInfo(dono, num_terr, num_aereo, (Territorio)(combo.getSelectedItem()));

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

    /**
     * Mostra a janela de movimentação de tropas
     */
    private void movimentar(){
        movimentando_ = true;

        //Verifica quantos e quais territórios estão disponíveis para movimentar (aliados)
        List<Territorio> f = new ArrayList<>();
        for(Territorio t : selecionado_.getFronteira()){
            if(t.getOcupante() == game_.getJogadorDaVez()){
                f.add(t);
            }
        }

        if(f.size() == 0){
            JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com aliados");
            movimentando_ = false;
            return;
        }

        Territorio[] fronteiras = new Territorio[f.size()];
        for(int i = 0; i < fronteiras.length; i++){
            fronteiras[i] = f.get(i);
        }

        //Cria a nova janela
        JFrame frame = new JFrame("Movimentar");
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
                movimentando_ = false;
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
        JLabel titulo = new JLabel("Movimentando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Para: ");
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
                        updatePopUpInfo(dono, num_terr, num_aereo, (Territorio)((JComboBox)e.getSource()).getSelectedItem());
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

        updatePopUpInfo(dono, num_terr, num_aereo, (Territorio)(combo.getSelectedItem()));

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

    /**
     * Atualiza as informações mostradas na tela de jogo
     * @param pais O label do país
     * @param num_terr O label referente ao número de exércitos terrestres
     * @param num_aereo O label referente ao número de exércitos aéreos
     * @param vizinhos Label que mostra os países vizinhos
     * @param atacar Botão referente ao ataque que é ativado ou não dependendo das informações
     * @param movimentar Botão referente a movimentação
     * @param row Linha da tabela selecionada
     * @param col Coluna da tabela selecionada
     */
    private void updateInfos(JLabel pais, JLabel num_terr, JLabel num_aereo, JLabel vizinhos,
                             JButton atacar, JButton movimentar, int row, int col){
        //Se a célula for vazia desativa o botão
        if(game_.getMapa().getTablaMapa()[row][col][0] == -1){
            atacar.setEnabled(false);
            movimentar.setEnabled(false);
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
                movimentar.setEnabled(true);
            }
            else{
                atacar.setEnabled(false);
                movimentar.setEnabled(false);
            }

            Territorio[] fronteira = t.getFronteira();

            pais.setText("Pais selecionao: " + t.getNome());
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
    private void updatePopUpInfo(JLabel dono, JLabel num_terr, JLabel num_aereo, Territorio t){
        dono.setText("Dono: " + t.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + "3");
        num_aereo.setText("Exércitos Aéreos: " + "2");
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
}
