package bcco.wWar.gui;

//Imports próprios

import bcco.wWar.game.Game;
import bcco.wWar.game.jogadores.Jogador;
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
import java.util.Objects;

//Imports do sistema

/** Controla a interface com o usuario
 *  @author Cristofer Oswald e Bruno Cesar
 *  @since 12/01/17
 */
public class Gui {
    //Frame principal
    private JFrame janela_;

    //Dados para o ataque e defesa
    private int n_defender = 0;
    private int n_ataque = 0;

    //Dados do jogo
    private Game game_;
    private MapTable tabela_mapa_;

    //Informações necessárias para a atualização da tela
    private boolean terminado_ataque_ = false;
    private Territorio selecionado_ = null;
    private Territorio selecionado_destino = null;
    private String tipo_exerc_;

    //Componentes da janela principal
    private JLabel jogador_num_territorios;
    private JLabel jogador_num_continentes;
    private JLabel jogador_num_terr;
    private JLabel jogador_num_aereo;
    private JLabel jogador_selecao_nome_pais;
    private JLabel jogador_selecao_vizinhos1;
    private JLabel jogador_selecao_vizinhos2;
    private JLabel jogador_selecao_num_terr;
    private JLabel jogador_selecao_num_aereo;
    private JButton jogador_atacar_terr;
    private JButton jogador_movimentar;
    private JButton jogador_atacar_aereo;
    private JLabel cpu_num_territorios;
    private JLabel cpu_num_continentes;
    private JLabel cpu_num_terr;
    private JLabel cpu_num_aereo;
    private JLabel cpu_selecao_nome_pais;
    private JLabel cpu_selecao_vizinhos1;
    private JLabel cpu_selecao_vizinhos2;
    private JLabel cpu_selecao_num_terr;
    private JLabel cpu_selecao_num_aereo;
    private JTable tabela;

    /**
     * Cria as estruturas basicas para a janela_
     * @param titulo O titulo da janela_
     * @param screen_width A largura
     * @param screen_heigth A altura
     * @param game O jogo a qual esta janela_ esta ligado
     */
    public Gui(String titulo, int screen_width, int screen_heigth, Game game){
        game_ = game;
        tabela_mapa_ = new MapTable(game_.getMapa());

        //Configura a janela
        janela_ = new JFrame(titulo);
        janela_.setSize(screen_width, screen_heigth);
        //Posiciona a tela no centro
        janela_.setLocationRelativeTo(null);
        janela_.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     * Mostra a tela inicial
     */
    public void telaInicial(){
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
                        game_.getHumano().setNome(nome_input.getText());
                        game_.distribuiTerritorios();
                        popIncial(game_.getHumano().getNome(), game_.getTerritorios(game_.getHumano()),
                                game_.getCPU().getNome());
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

        //Adiciona tudo ao pane principal
        janela_.getContentPane().add(pane, c);

    }

    /**
     * Tela de informações iniciais do jogo
     */
    private void popIncial(String nome, List<Territorio> territorios, String oponente) {
        janela_.setEnabled(false);

        //Configura o frame
        JFrame frame = new JFrame("Bem-vindo!");
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
                janela_.setEnabled(true);
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

        //Cria 2 strings com os nomes dos territórios ganhos
        String t_territorios1 = "";
        for (int i = 0; i < territorios.size()-8; i++) {
            t_territorios1 += territorios.get(i).getNome() + ", ";
        }

        String t_territorios2 = "";
        for (int i = territorios.size()-8; i < territorios.size()-1; i++) {
            t_territorios2 += territorios.get(i).getNome() + ", ";
        }

        //Componentes
        JLabel l_bemvindo = new JLabel(nome + ", bem-vindo ao WAR The Game");
        JLabel l_oponente = new JLabel("Neste jogo seu oponente será o maligno " + oponente + "!!!");
        JLabel l_territorios1 = new JLabel("Você recebeu os territórios: " + t_territorios1);
        JLabel l_territorios2 = new JLabel(t_territorios2 + territorios.get(territorios.size()-1).getNome() +
                ".");
        JLabel l_info = new JLabel("Um exército terrestre e um aéreo já se encontram nestes territórios.");
        JLabel l_fim = new JLabel("Pense estratégicamente, planeje e ataque! Vença a guerra!");
        JButton ok = new JButton("OK");

        //Listener
        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        janela_.setEnabled(true);
                        clear();
                        telaJogo();
                        game_.iniciarJogo();
                    }
                }
        );

        //Layout

        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(l_bemvindo, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridy = 1;
        pane.add(l_oponente, c);

        c.gridy = 2;
        pane.add(l_territorios1, c);

        c.gridy = 3;
        pane.add(l_territorios2, c);

        c.gridy = 4;
        pane.add(l_info, c);

        c.gridy = 5;
        pane.add(l_oponente, c);

        c.gridy = 6;
        pane.add(l_fim, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 7;
        pane.add(ok, c);

        frame.getContentPane().add(pane, c);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Tela principal de jogo
     */
    private void telaJogo(){
        //Define o contentPane como um pane com background
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

        //A tela é dividida em 3 panels
        JPanel tabela_pane = new JPanel(new GridBagLayout());
        JPanel jogador_pane = new JPanel(new GridBagLayout());
        JPanel cpu_pane = new JPanel(new GridBagLayout());

        //Componentes
        JLabel jogador_nome = new JLabel(game_.getHumano().getNome());
        jogador_num_territorios = new JLabel("Número de territórios: ");
        jogador_num_continentes = new JLabel("Número de continentes: ");
        jogador_num_terr = new JLabel("Exércitos Terrestres: ");
        jogador_num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel jogador_selecao_titulo = new JLabel("Informções do país");
        jogador_selecao_nome_pais = new JLabel("Nome: Nenhum");
        jogador_selecao_vizinhos1 = new JLabel("Vizinhos: Nenhum");
        jogador_selecao_vizinhos2 = new JLabel();
        jogador_selecao_num_terr = new JLabel("Exércitos Terrestres: 0");
        jogador_selecao_num_aereo = new JLabel("Exércitos Aéreos: 0");
        jogador_movimentar = new JButton("Movimentar tropas");
        jogador_atacar_terr = new JButton("Iniciar ataque terrestre");
        jogador_atacar_aereo = new JButton("Iniciar ataque aéreo");
        JLabel cpu_nome = new JLabel(game_.getCPU().getNome());
        cpu_num_territorios = new JLabel("Número de territórios: ");
        cpu_num_continentes = new JLabel("Número de continentes: ");
        cpu_num_terr = new JLabel("Exércitos Terrestres: ");
        cpu_num_aereo = new JLabel("Exércitos Aéreos: ");
        JLabel cpu_selecao_titulo = new JLabel("Informções do país");
        cpu_selecao_nome_pais = new JLabel("Nome: Nenhum");
        cpu_selecao_vizinhos1 = new JLabel("Vizinhos: Nenhum");
        cpu_selecao_vizinhos2 = new JLabel();
        cpu_selecao_num_terr = new JLabel("Exércitos Terrestres: 0");
        cpu_selecao_num_aereo = new JLabel("Exércitos Aéreos: 0");
        tabela = new JTable(tabela_mapa_);
        JButton prox_rodada = new JButton("Terminar rodada");

        //Colore as células
        for(int i = 0; i <tabela.getColumnCount(); i++){
            tabela.getColumnModel().getColumn(i).setCellRenderer(new MapColumnCellRenderer(game_));
            tabela.getColumnModel().getColumn(i).setMinWidth(90);
        }

        updateJogadoresInfos();

        //Botões desativados
        jogador_atacar_terr.setEnabled(false);
        jogador_atacar_aereo.setEnabled(false);
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

                            updateInfos();
                            if(selecionado_.getOcupante() == game_.getHumano()){
                                cpu_selecao_nome_pais.setText("Nome: Nenhum");
                                cpu_selecao_num_terr.setText("Exércitos Terrestres: 0");
                                cpu_selecao_num_aereo.setText("Exércitos Aéreo: 0");
                                cpu_selecao_vizinhos1.setText("Vizinhos: Nenhum");
                                cpu_selecao_vizinhos2.setText("");
                            }
                            else {
                                jogador_selecao_nome_pais.setText("Nome: Nenhum");
                                jogador_selecao_num_terr.setText("Exércitos Terrestres: 0");
                                jogador_selecao_num_aereo.setText("Exércitos Aéreo: 0");
                                jogador_selecao_vizinhos1.setText("Vizinhos: Nenhum");
                                jogador_selecao_vizinhos2.setText("");
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
                        if(JOptionPane.showConfirmDialog(tabela_pane,
                                "Tem certeza que deseja terminar a rodada?",
                                "Tem certeza?", JOptionPane.YES_NO_OPTION) ==
                                JOptionPane.YES_OPTION){

                            game_.getCPU().jogar();
                            terminado_ataque_ = false;

                            if(selecionado_ != null){
                                jogador_atacar_terr.setEnabled(!jogador_atacar_terr.isEnabled());
                                jogador_atacar_aereo.setEnabled(!jogador_atacar_terr.isEnabled());
                                jogador_movimentar.setEnabled(!jogador_movimentar.isEnabled());
                            }
                        }
                    }
                }
        );

        jogador_atacar_terr.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(terminado_ataque_){
                            JOptionPane.showMessageDialog(tabela_pane, "Você começou a movimentar tropas," +
                                    "não pode mais atacarTerrestre");
                        }
                        else{
                            atacarTerrestre();
                            updateJogadoresInfos();
                        }
                    }
                }
        );

        jogador_atacar_aereo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<Territorio> t = game_.checkAereo(game_.getHumano(), selecionado_);
                        if(terminado_ataque_){
                            JOptionPane.showMessageDialog(tabela_pane, "Você começou a movimentar tropas," +
                                    "não pode mais atacarTerrestre");
                        }
                        else{
                            if(t.size() == 0){
                                JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com" +
                                        "nenhum terriório que pode ser atacado por ar");
                            }
                            else {
                                atacarAereo(t);
                                updateJogadoresInfos();
                            }
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
                                    "Tem certeza que deseja movimentar? Você não poderá mais atacarTerrestre",
                                    "Tem certeza?", JOptionPane.YES_NO_OPTION) ==
                                    JOptionPane.YES_OPTION) {
                                movimentar();
                                updateJogadoresInfos();
                            }
                        }
                        else{
                            movimentar();
                            updateJogadoresInfos();
                        }
                    }
                }
        );

        //Layouts

        //Pane humano
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
        c.fill = GridBagConstraints.BOTH;
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
        jogador_pane.add(jogador_selecao_vizinhos1, c);

        c.gridy = 10;
        jogador_pane.add(jogador_selecao_vizinhos2, c);

        c.gridy = 11;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.VERTICAL;

        jogador_pane.add(jogador_atacar_terr, c);

        c.gridy = 12;
        jogador_pane.add(jogador_atacar_aereo, c);

        c.gridy = 13;
        jogador_pane.add(jogador_movimentar, c);

        //Pane tabela
        c.gridx = 0;
        c.gridy = 0;
        tabela_pane.add(prox_rodada, c);

        c.gridy = 1;
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
        cpu_pane.add(cpu_selecao_vizinhos1, c);

        //Adição de panels ao panel principal
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(280, -70, 100, 30);
        jogador_pane.setOpaque(false); //Panel transparente
        janela_.getContentPane().add(jogador_pane, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(530, 0, 25, 0);
        tabela_pane.setOpaque(false);
        janela_.getContentPane().add(tabela_pane, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(280, 30, 175, 0);
        cpu_pane.setOpaque(false);
        janela_.getContentPane().add(cpu_pane, c);

        show();
    }

    /**
     * Tela de distribuição de exército
     */
    public void distribuirExercito() {
        janela_.setEnabled(false);

        //Dados necessários para a configuração da tela
        int terr_recebidos = game_.getHumano().getTerrestresRecebidos();
        int aereo_recebidos = game_.getHumano().getAereosRecebidos();
        int i = 1;
        List<Territorio> territorios = game_.getTerritorios(game_.getHumano());

        //Configura o frame
        JFrame frame = new JFrame("Distribuir Exércitos");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Classe que configura o comportamento do botão de confirmação
        class Distribuir implements ActionListener{
            private List<Integer> valoresTerr;
            private List<JTextField> terrTextFields;
            private List<Integer> valoresAereo;
            private List<JTextField> aereoTextFields;

            private Distribuir(List<JTextField> terrTextFields, List<JTextField> aereoTextFields) {
                this.terrTextFields = terrTextFields;
                valoresTerr = new ArrayList<>();

                this.aereoTextFields = aereoTextFields;
                valoresAereo = new ArrayList<>();
            }


            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int somaTerr = 0;
                int somaAereo = 0;

                //Exercitos terrestres
                for (JTextField textField : terrTextFields) {
                    valoresTerr.add(Integer.parseInt(textField.getText()));
                    somaTerr += Integer.parseInt(textField.getText());
                }

                //Exercitos aereos
                for (JTextField textField : aereoTextFields) {
                    if(textField.getText().equals("")){
                        valoresAereo.add(0);
                    }
                    valoresAereo.add(Integer.parseInt(textField.getText()));
                    somaAereo += Integer.parseInt(textField.getText());
                }

                if (somaTerr > terr_recebidos || somaAereo > aereo_recebidos) {
                    JOptionPane.showMessageDialog(null, "Erro, valor total da distribuição maior que" +
                            " total de exércitos disponiveis!","ERRO",JOptionPane.OK_OPTION);
                } else if (somaTerr == terr_recebidos && somaAereo == aereo_recebidos) {
                    JOptionPane.showMessageDialog(null, "Tropas distribuídas!",
                            "Sucesso",JOptionPane.OK_CANCEL_OPTION);

                    game_.distribuirExercitos(game_.getHumano(), valoresTerr, valoresAereo);
                    frame.setVisible(false);
                    updateJogadoresInfos();
                    janela_.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Sobraram algumas tropas sem distribuição," +
                            " não abandone-as!","Atenção",JOptionPane.OK_OPTION);
                }
            }
        }

        //Componentes
        JLabel d_nome = new JLabel("Nome território");
        JLabel d_qtd = new JLabel("Quantidade de tropas");
        JLabel d_inTerr = new JLabel("Terrestres");
        JLabel d_inAereo = new JLabel("Aereos");
        JLabel terrestre_restante = new JLabel("Exercitos terrestres restantes: " +
                Integer.toString(game_.getHumano().getTerrestresRecebidos()));
        JLabel aereo_restante = new JLabel("Exercitos aereos restantes: " +
                Integer.toString(game_.getHumano().getAereosRecebidos()));
        JButton distribuir = new JButton("Distribuir exércitos");
        List<JTextField> terr_text_fields = new ArrayList<>();
        List<JTextField> aereo_text_fields = new ArrayList<>();

        //Configura os constrains para cada coluna
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.gridx = 0;
        c1.gridy = 0;

        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridx = 1;
        c2.gridy = 0;

        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(5, 5, 5, 5);
        c3.gridx = 2;
        c3.gridy = 0;

        GridBagConstraints c4 = new GridBagConstraints();
        c4.insets = new Insets(5, 5, 5, 5);
        c4.gridx = 3;
        c4.gridy = 0;

        pane.add(d_nome,c1);
        pane.add(d_inTerr, c2);
        pane.add(d_inAereo, c3);
        pane.add(d_qtd, c4);

        //Itera sobre a lista de territórios criando um textFiels e um label para cada um e os coloca na tela
        for (Territorio t : territorios) {
            //Componentes
            JLabel nome = new JLabel(t.getNome());
            JLabel qtd = new JLabel("(" + Integer.toString(t.getNumExTerrestres()) + "/" +
                    Integer.toString(t.getNumExAereos()) + ")");
            JTextField in_terr = new JTextField(3);
            JTextField in_aereo = new JTextField(3);

            //Adiciona os listeners
            in_terr.setText("0");
            in_terr.getDocument().addDocumentListener(new TropasListener(in_terr, terr_recebidos));

            in_aereo.setText("0");
            in_aereo.getDocument().addDocumentListener(new TropasListener(in_aereo, aereo_recebidos));

            //Layout
            c1.gridy = i;
            c2.gridy = i;
            c3.gridy = i;
            c4.gridy = i;

            pane.add(nome,c1);
            pane.add(in_terr, c2);
            pane.add(in_aereo, c3);
            pane.add(qtd, c4);
            i++;

            //Adiciona a lista de terrTextFields
            terr_text_fields.add(in_terr);
            aereo_text_fields.add(in_aereo);
        }

        //Layout pt.2
        c1.gridy = i + 1;
        pane.add(terrestre_restante, c1);

        c1.gridy++;
        c1.anchor = GridBagConstraints.LINE_START;
        pane.add(aereo_restante, c1);

        c4.gridy = i + 1;
        c4.gridheight = 2;
        c4.fill = GridBagConstraints.VERTICAL;

        distribuir.addActionListener(new Distribuir(terr_text_fields, aereo_text_fields));
        pane.add(distribuir, c4);

        frame.getContentPane().add(pane, c1);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Atualiza as informações do jogador
     */
    private void updateJogadoresInfos(){
        Jogador humano = game_.getHumano();
        Jogador cpu = game_.getCPU();

        jogador_num_territorios.setText("Número de territórios: " + game_.getNumTerritorios(humano));
        jogador_num_continentes.setText("Número de continentes: " + game_.getNumContinentesJogador(humano));
        jogador_num_terr.setText("Exércitos Terrestres: " + game_.getNumTerrestres(humano));
        jogador_num_aereo.setText("Exércitos Aéreos: " + game_.getNumAereos(humano));
        cpu_num_territorios.setText("Número de territórios: " + game_.getNumTerritorios(cpu));
        cpu_num_continentes.setText("Número de continentes: " + game_.getNumContinentesJogador(cpu));
        cpu_num_terr.setText("Exércitos Terrestres: " + game_.getNumTerrestres(cpu));
        cpu_num_aereo.setText("Exércitos Aéreos: " + game_.getNumAereos(cpu));
    }

    /**
     * Mostra a janela de defesa
     */
    public void defender(Territorio territorio, Territorio alvo, int qtd_ataque) {
        janela_.setEnabled(false);


        int n_tropas = alvo.getNumExTerrestres();

        //Configura o frame
        JFrame frame = new JFrame("Defender território");
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Componentes
        JLabel descricao = new JLabel("Os exércitos de " + game_.getCPU().getNome() + " estão atacando!");
        JLabel territorio_alvo = new JLabel(alvo.getNome() + " está em perigo!");
        JLabel alvo_info = new JLabel("Tropas neste território: " + n_tropas);
        JLabel defender_text = new JLabel("Defender território com: ");
        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");
        JLabel territorio_atacando = new JLabel(qtd_ataque + " exércitos marcham de " + territorio.getNome());
        JButton defender = new JButton("Defender");
        ButtonGroup bg = new ButtonGroup();

        //Confirgura os radio buttons
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);

        if (n_tropas < 3) {
            r3.setEnabled(false);
            if (n_tropas != 2) {
                r2.setEnabled(false);
            }
        }

        //Listeners
        r1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_defender = 1;
                    }
                }
        );

        r2.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_defender = 2;
                    }
                }
        );

        r3.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_defender = 3;
                    }
                }
        );

        defender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janela_.setEnabled(true);
                frame.setVisible(false);
                game_.atacarTerrestre(game_.getCPU(), territorio, alvo, qtd_ataque, n_defender);
            }
        });

        r1.doClick();

        //Layout
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(descricao, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridy = 1;
        c.gridwidth = 1;
        pane.add(territorio_alvo, c);

        c.gridy = 2;
        pane.add(alvo_info, c);

        c.gridy = 3;
        pane.add(defender_text, c);

        c.gridx = 1;
        pane.add(r1, c);

        c.gridx = 2;
        pane.add(r2, c);

        c.gridx = 3;
        pane.add(r3, c);

        c.gridx = 4;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.gridheight = 3;
        pane.add(territorio_atacando, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(defender, c);

        frame.getContentPane().add(pane, c);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Tela de pop-up de ataque
     */
    private void atacarTerrestre() {
        janela_.setEnabled(false);

        n_ataque = 1;
        int n_tropas = selecionado_.getNumExTerrestres();

        //Verifica quantos e quais territórios estão disponíveis para atacarTerrestre (são inimigos)
        List<Territorio> f = selecionado_.getFronteirasInimigas(game_.getHumano());

        if(f.size() == 0){
            JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com inimigos");
            return;
        }

        Territorio[] fronteiras = f.toArray(new Territorio[f.size()]);

        //Configura o frame
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
                janela_.setEnabled(true);
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
        JComboBox<Territorio> combo = new JComboBox<>(fronteiras);
        selecionado_destino = (Territorio) (combo.getSelectedItem());
        JLabel titulo = new JLabel("Atacando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Atacar: ");
        JLabel quantidade = new JLabel("Atacar com:");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aereos: " + selecionado_destino.getNumExAereos());
        JButton ok = new JButton("Atacar");
        ButtonGroup bg = new ButtonGroup();
        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");

        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        r1.doClick();

        if (n_tropas <= 3) {
            r3.setEnabled(false);
            if (n_tropas == 2) {
                r2.setEnabled(false);
            }
        }

        //Listeners
        combo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selecionado_destino = (Territorio) ((JComboBox) e.getSource()).getSelectedItem();
                        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
                        num_aereo.setText("Exércitos Aereos: " + selecionado_destino.getNumExAereos());
                    }
                }
        );

        r1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_ataque = 1;
                    }
                }
        );

        r2.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_ataque = 2;
                    }
                }
        );

        r3.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        n_ataque = 3;
                    }
                }
        );

        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game_.atacarTerrestre(game_.getHumano(), selecionado_, selecionado_destino,
                                n_ataque, 0);
                        janela_.setEnabled(true);
                        frame.setVisible(false);
                        updateInfos();
                    }
                }
        );

        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
        num_aereo.setText("Exércitos Aereos: " + selecionado_destino.getNumExAereos());


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
        pane.add(quantidade, c);

        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(r1, c);

        c.gridx = 1;
        pane.add(r2, c);

        c.gridx = 2;
        pane.add(r3, c);

        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 3;
        pane.add(ok, c);

        frame.getContentPane().add(pane, c);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Exibe a janela com o realtóriode ataque
     * @param jogador Jogador atacante
     * @param territorio Território atacado
     * @param n_fracassos Quantidade de tropas que perderam
     * @param n_sucessos Quantidade de tropas que ganhanram
     * @param resultado O sucesso do ataque (True para vitória, False para derrota)
     */
    public void relatorioAtaque(Jogador jogador, String territorio, String n_fracassos,
                                String n_sucessos, boolean resultado) {
        janela_.setEnabled(false);

        //Configura o frame
        JFrame frame = new JFrame("Resultados do combate");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(janela_);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                janela_.setEnabled(true);
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
        JLabel l_intro = new JLabel("Os exércitos de " + jogador.getNome() + " atacaram o(a) " + territorio);
        JLabel l_fracassos = new JLabel("Numero de exércitos perdidos em combate: " + n_fracassos);
        JLabel l_sucessos = new JLabel("Número de exércitos defensores mortos: " + n_sucessos);
        JLabel l_conquista = new JLabel("Parabéns " + jogador.getNome() +
                " você conquistou o território " + territorio + "!");
        JLabel l_derrota = new JLabel("Os exércitos de " + jogador.getNome() + "dominaram o(a) "
                + territorio + " e tomaram posse de suas tropas aereas.");
        JLabel l_fim = new JLabel("Fim do relatório de combate");
        JButton ok = new JButton("OK");

        //Listener
        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        janela_.setEnabled(true);
                    }
                }
        );

        //Layout
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(l_intro, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridy = 1;
        pane.add(l_fracassos, c);

        c.gridy = 2;
        pane.add(l_sucessos, c);

        c.gridy = 3;
        if (resultado) {
            if (jogador == game_.getHumano()) {
                pane.add(l_conquista, c);
            } else {
                pane.add(l_derrota, c);
            }
        }

        c.gridy = 4;
        pane.add(l_fim, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        pane.add(ok, c);

        frame.getContentPane().add(pane, c);
        frame.pack();

        frame.setVisible(true);
    }


    /**
     * Mostra a janela de movimentação de tropas
     */
    private void movimentar(){
        janela_.setEnabled(false);

        //Verifica quantos e quais territórios estão disponíveis para movimentar (aliados)
        List<Territorio> f = selecionado_.getFronteirasAliadas(game_.getHumano());

        if(f.size() == 0){
            JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com aliados");
            return;
        }

        Territorio[] fronteiras = f.toArray(new Territorio[f.size()]);

        //Configuar o frame
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
                janela_.setEnabled(true);
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
        JRadioButton terr = new JRadioButton("Terrestre");
        JRadioButton aereo = new JRadioButton("Aereo");
        ButtonGroup bg = new ButtonGroup();
        JTextField num_exe = new JTextField(3);
        JButton ok = new JButton("Cofirmar");

        //Configur os radio buttons
        terr.setActionCommand("Terrestre");
        aereo.setActionCommand("Aereo");
        bg.add(terr);
        bg.add(aereo);

        TropasListener terr_listener = new TropasListener(num_exe, (selecionado_.getNumExTerrestres() - 1));
        TropasListener aereo_listener = new TropasListener(num_exe, selecionado_.getNumExAereos());

        selecionado_destino = (Territorio) (combo.getSelectedItem());

        //Listeners
        combo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selecionado_destino = (Territorio) ((JComboBox) e.getSource()).getSelectedItem();
                        dono.setText("Dono: " + selecionado_destino.getOcupante().getNome());
                        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
                        num_aereo.setText("Exércitos Aéreos " + selecionado_destino.getNumExAereos());
                    }
                }
        );

        terr.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        num_exe.getDocument().addDocumentListener(terr_listener);
                        num_exe.getDocument().removeDocumentListener(aereo_listener);
                        tipo_exerc_ = e.getActionCommand();
                        System.out.println("clicou");
                    }
                }
        );

        aereo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        num_exe.getDocument().addDocumentListener(aereo_listener);
                        num_exe.getDocument().removeDocumentListener(terr_listener);
                        tipo_exerc_ = e.getActionCommand();
                    }
                }
        );


        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(tipo_exerc_);
                        game_.movimentar(selecionado_, selecionado_destino, tipo_exerc_,
                                Integer.parseInt(num_exe.getText()));
                        frame.setVisible(false);
                        janela_.setEnabled(true);
                        terminado_ataque_ = true;
                        updateInfos();
                    }
                }
        );

        terr.doClick();

        dono.setText("Dono: " + selecionado_destino.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
        num_aereo.setText("Exércitos Aéreos " + selecionado_destino.getNumExAereos());

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

        frame.getContentPane().add(pane, c);

        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Atualiza as informações de seleção de territórios
     */
    public void updateInfos() {
        JLabel nome_pais;
        JLabel num_terr;
        JLabel num_aereo;
        JLabel vizinhos1;
        JLabel vizinhos2;

        //Se a célula for vazia desativa o botão
        if(selecionado_ == null){
            jogador_atacar_terr.setEnabled(false);
            jogador_movimentar.setEnabled(false);
            return;
        }

        if(selecionado_ != null) {
            if(selecionado_.getOcupante().equals(game_.getHumano())){
                nome_pais = jogador_selecao_nome_pais;
                num_terr = jogador_selecao_num_terr;
                num_aereo = jogador_selecao_num_aereo;
                vizinhos1 = jogador_selecao_vizinhos1;
                vizinhos2 = jogador_selecao_vizinhos2;

                if(selecionado_.getNumExTerrestres() > 1) {
                    jogador_atacar_terr.setEnabled(true);
                }
                if(selecionado_.getNumExAereos() != 0) {
                    jogador_atacar_aereo.setEnabled(true);
                }
                jogador_movimentar.setEnabled(true);
            }
            else{
                nome_pais = cpu_selecao_nome_pais;
                num_terr = cpu_selecao_num_terr;
                num_aereo = cpu_selecao_num_aereo;
                vizinhos1 = cpu_selecao_vizinhos1;
                vizinhos2 = cpu_selecao_vizinhos2;

                jogador_atacar_terr.setEnabled(false);
                jogador_atacar_aereo.setEnabled(false);
                jogador_movimentar.setEnabled(false);
            }

            Territorio[] fronteira = selecionado_.getFronteira();

            nome_pais.setText("Pais selecionado: " + selecionado_.getNome());
            num_terr.setText("Exércitos Terrestres: " + selecionado_.getNumExTerrestres());
            num_aereo.setText("Exércitos Aéreos: " + selecionado_.getNumExAereos());

            String f1 = "Vizinhos: " + fronteira[0].getNome();
            if(fronteira.length > 1){
                f1 = f1 + ", " + fronteira[1].getNome();
            }

            String f2 = "";
            if(fronteira.length > 2){
                f2 = fronteira[2].getNome();
            }
            if(fronteira.length > 3){
                f2 = f2 + ", " +fronteira[3].getNome();
            }

            vizinhos1.setText(f1);
            vizinhos2.setText(f2);
        }
    }

    /**
     * Tela de ataques aéreos
     * @param territorios A lista de territórios que podem ser atacados
     */
    private void atacarAereo(List<Territorio> territorios){
        janela_.setEnabled(false);

        n_ataque = 0;
        int n_tropas = selecionado_.getNumExAereos();

        Territorio[] terrs = territorios.toArray(new Territorio[territorios.size()]);

        //Configura a janela
        JFrame frame = new JFrame("Ataque aéreo");
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
                janela_.setEnabled(true);
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
        JComboBox<Territorio> combo = new JComboBox<>(terrs);
        JLabel titulo = new JLabel("Atacando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Atacar: ");
        JLabel quantidade = new JLabel("Atacar com:");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aereos: " + selecionado_destino.getNumExAereos());
        JButton reforco = new JButton("Chamar reforço");
        JButton ok = new JButton("Atacar");
        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");
        ButtonGroup bg = new ButtonGroup();

        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        r1.doClick();

        selecionado_destino = (Territorio) (combo.getSelectedItem());

        if (n_tropas < 3) {
            r3.setEnabled(false);
        }
        if (n_tropas < 2) {
            r2.setEnabled(false);
        }
        if(n_tropas < 1){
            r1.setEnabled(false);
        }


        //Listeners
        combo.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selecionado_destino = (Territorio) ((JComboBox) e.getSource()).getSelectedItem();
                        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
                        num_aereo.setText("Exércitos Aereos: " + selecionado_destino.getNumExAereos());
                    }
                }
        );


        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        janela_.setEnabled(true);
                        if(r1.isSelected()){
                            game_.atacarAereo(game_.getHumano(), selecionado_, selecionado_destino, 1);
                        }
                        if(r2.isSelected()){
                            game_.atacarAereo(game_.getHumano(), selecionado_, selecionado_destino, 2);
                        }
                        if(r3.isSelected()){
                            game_.atacarAereo(game_.getHumano(), selecionado_, selecionado_destino, 3);
                        }
                        frame.setVisible(false);

                        updateInfos();
                    }
                }
        );

        reforco.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        Territorio[] t = game_.getTerrPossiveisAereo(selecionado_destino, game_.getHumano());
                        if(t.length == 0){
                            JOptionPane.showMessageDialog(null, "Não há aviões disponíveis para reforço",
                                    "Atenção",JOptionPane.OK_OPTION);
                        }
                        else{
                            frame.setEnabled(false);
                            reforcoAereo(t, r1, r2, r3, frame);
                        }
                    }
                }
        );

        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
        num_aereo.setText("Exércitos Aereos: " + selecionado_destino.getNumExAereos());


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
        pane.add(quantidade, c);

        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(r1, c);

        c.gridx = 1;
        pane.add(r2, c);

        c.gridx = 2;
        pane.add(r3, c);

        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 3;
        pane.add(reforco, c);

        c.gridy = 6;
        pane.add(ok, c);

        frame.getContentPane().add(pane, c);

        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Mostra janela de reforço aéreo
     * @param territorios O vetor territórios que podem apoiar
     * @param r1 Radio button 1
     * @param r2 Radio button 2
     * @param r3 Radio button 3
     * @param pai Frame o qual chamou essa janela
     */
    private void reforcoAereo(Territorio[] territorios, JRadioButton r1, JRadioButton r2, JRadioButton r3, JFrame pai){
        pai.setEnabled(false);

        //Configura o frame
        JFrame frame = new JFrame("Reforço aéreo");
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
                pai.setEnabled(true);
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
        List<JComboBox<Integer>> terrs_combos = new ArrayList<>();
        for(Territorio t : territorios){
            terrs_combos.add(new JComboBox<>());
        }
        JLabel[] terrs_labels = new JLabel[territorios.length];
        JLabel titulo = new JLabel("Reforçar: " + selecionado_.getNome());
        JLabel local = new JLabel("De");
        JLabel quantidade = new JLabel("Reforçar com");
        JButton ok = new JButton("Reforçar");

        List<Territorio> selecionados = new ArrayList<>();
        List<Integer> num_selecionados = new ArrayList<>();

        //Cria um listener especial para o os combo boxes
        class AereoListener implements ActionListener{
            private Territorio t_;
            private List<Territorio> sel_;
            private List<Integer> num_sel_;
            private AereoListener(Territorio t, List<Territorio> l, List<Integer> n){
                t_ = t;
                sel_ = l;
                num_sel_ = n;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                sel_.add(t_);
                num_sel_.add((Integer)((JComboBox)e.getSource()).getSelectedItem());
            }
        }

        //Cria um listener especial para o botão de confirmação
        class ConfirmarListener implements ActionListener{
            private List<Territorio> t_;
            private List<Integer> n_;
            private JFrame f;

            private ConfirmarListener(List<Territorio> t, List<Integer> n, JFrame frame){
                t_ = t;
                n_ = n;
                f = frame;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < t_.size(); i++){
                    for(int j = 0; j < n_.get(i); j++){
                        selecionado_.insereExAereo(t_.get(i).removeExAereo());
                        if(selecionado_.getNumExAereos() > 2){
                            r3.setEnabled(true);
                            if(selecionado_.getNumExAereos() > 1){
                                r2.setEnabled(true);
                                if(selecionado_.getNumExAereos() > 0){
                                    r1.setEnabled(true);
                                }
                            }
                            pai.setEnabled(true);
                        }
                        frame.setVisible(false);
                    }
                }
            }
        }

        ok.addActionListener(new ConfirmarListener(selecionados, num_selecionados, frame));

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        pane.add(titulo, c);

        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(local, c);

        c.gridx = 1;
        pane.add(quantidade, c);

        //Colaca os combo boxes na tela
        int i;
        for(i = 0; i < territorios.length; i++){
            if(territorios[i].getNumExAereos() != 0) {
                for (int j = 0; j < territorios[i].getNumExAereos(); j++) {
                    terrs_combos.get(i).addItem(j);
                }
                terrs_combos.get(i).addActionListener(new AereoListener(territorios[i], selecionados, num_selecionados));

                c.gridx = 0;
                c.gridy = i + 2;

                terrs_labels[i] = new JLabel(territorios[i].getNome());
                pane.add(terrs_labels[i], c);

                c.gridx = 1;

                pane.add(terrs_combos.get(i), c);
            }
        }

        c.gridx = 0;
        c.gridy = i + 3;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        pane.add(ok, c);

        frame.getContentPane().add(pane);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Mostra o relatório do ataque aéreo
     * @param jogador O jogador que atacou
     * @param origem Território que atacou
     * @param destino Território que foi atacado
     * @param defesa Quantidade de aviões perdidos
     * @param avioes_derrubados Quantidade de aviões derrubados
     * @param terrestres_mortos Quantidade de exércitos terrestres mortos
     */
    public void relatorioAereo(Jogador jogador, Territorio origem, Territorio destino,
                               int defesa, int avioes_derrubados, int terrestres_mortos){

        //Configura o frame
        JFrame frame = new JFrame("Resultados do combate");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();

        //componentes
        JLabel l_intro = new JLabel("Os aviões de " + jogador.getNome() + " atacaram o(a) " + destino.getNome());
        JLabel l_fracassos = new JLabel("Numero de aviões perdidos em combate: " + defesa);
        JLabel l_sucessos_avioes = new JLabel("Número de aviões defensores mortos: " + avioes_derrubados);
        JLabel l_sucessos_terrs = new JLabel("Número de tropas mortas: " + terrestres_mortos);
        JLabel l_fim = new JLabel("Fim do relatório de combate");
        JButton ok = new JButton("OK");

        //Listener
        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                    }
                }
        );

        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(l_intro, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridy = 1;
        pane.add(l_fracassos, c);

        c.gridy = 2;
        pane.add(l_sucessos_avioes, c);

        c.gridy = 3;
        pane.add(l_sucessos_terrs, c);

        c.gridy = 4;
        pane.add(l_fim, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5;
        pane.add(ok, c);

        frame.getContentPane().add(pane, c);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Tela de de vitória
     * @param jogador O jogador que ganhou
     */
    public void vitoria(Jogador jogador){
        JFrame frame = new JFrame("Acabou!");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        //Cria a string dependendo de quem ganhou
        String p = "Infelizmente " + jogador.getNome() + " ganhou";
        if(jogador.equals(game_.getHumano())){
            p = "Parabéns " + game_.getHumano().getNome() + ", você ganhou";
        }

        String con = jogador.getNome() + " conquistou 2 continentes, e você perdeu";
        if(jogador.equals(game_.getHumano())){
            con = "Você conquistou 2 continentes e ganhou!";
        }

        //Componentes
        JLabel parabens = new JLabel(p);
        JLabel continuar = new JLabel(con);
        JButton ok = new JButton("Terminar jogo");

        //Listener
        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5,5, 5);
        pane.add(parabens, c);

        c.gridy = 1;
        pane.add(continuar, c);

        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(ok, c);

        frame.getContentPane().add(pane);
        frame.setVisible(true);
        frame.pack();
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
     * Listener criado para verificar a entrada de texto
     */
    class TropasListener implements DocumentListener {
        private JTextField text;
        private int num;

        private TropasListener(JTextField text, int num) {
            this.text = text;
            this.num = num;
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
                    int v;
                    if (!Objects.equals(text.getText(), "")) {
                        try {
                            v = Integer.parseInt(text.getText());
                        } catch (NumberFormatException e) {
                            text.setText("0");
                            text.setColumns(3);
                            JOptionPane.showMessageDialog(null, "Erro, valor não numérico!",
                                    "ERRO", JOptionPane.OK_OPTION);
                            return;
                        }


                        if (v > num) {
                            text.setText("0");
                            text.setColumns(3);
                            JOptionPane.showMessageDialog(null, "Erro, valor maior que total de" +
                                    " exércitos disponiveis!", "ERRO", JOptionPane.OK_OPTION);
                        }
                    }
                }
            };
            SwingUtilities.invokeLater(doCheck);
        }
    }
}
