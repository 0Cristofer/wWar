package bcco.wWar.gui;

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

/** Controla a interface com o usuario
 *  @author Cristofer Oswald
 *  @since 12/01/17
 */
public class Gui {

    private JFrame janela_;

    private int screen_width_;
    private int screen_heigth_;
    private int n_defender = 0;
    int n_ataque = 0;
    private String titulo_;

    private Game game_;
    private MapTable tabela_mapa_;

    private boolean atacando_ = false;
    private boolean terminado_ataque_ = false;
    private boolean movimentando_ = false;
    private Territorio selecionado_ = null;
    private Territorio selecionado_destino = null;
    private String tipo_exerc_;

    private JLabel jogador_num_territorios;
    private JLabel jogador_num_continentes;
    private JLabel jogador_num_terr;
    private JLabel jogador_num_aereo;
    private JLabel jogador_selecao_nome_pais;
    private JLabel jogador_selecao_vizinhos;
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
    private JLabel cpu_selecao_vizinhos;
    private JLabel cpu_selecao_num_terr;
    private JLabel cpu_selecao_num_aereo;
    private JTable tabela;

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

        janela_.setSize(screen_width_, screen_heigth_);
        //Posiciona a tela no centro
        janela_.setLocationRelativeTo(null);
        janela_.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     * Tela de informações iniciais do jogo
     */
    public void popUpInfo(String nome, List<Territorio> territorios, String oponente) {
        String t_territorios = "";

        //Configura o frame
        JFrame frame = new JFrame("Bem-vindo!");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();


        for (Territorio t : territorios) {
            t_territorios += t.getNome() + ", ";
        }

        //componentes
        JLabel l_bemvindo = new JLabel(nome + "Bem-vindo ao WAR The Game ");
        JLabel l_oponente = new JLabel("Neste jogo seu oponente será o maligno " + oponente + "!!!");
        JLabel l_territorios = new JLabel("Você recebeu os territórios: " + t_territorios + ".");
        JLabel l_info = new JLabel("Um exército terrestre e um aereo já se encontram nestes territórios.");
        JLabel l_fim = new JLabel("Pense estratégicamente, planeje e ataque! Vença a guerra!");
        JButton ok = new JButton("OK");


        ok.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                    }
                }
        );

        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(l_bemvindo, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridy = 1;
        pane.add(l_oponente, c);

        c.gridy = 2;
        pane.add(l_territorios, c);

        c.gridy = 3;
        pane.add(l_oponente, c);

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
                        clear();
                        telaJogo();
                        game_.iniciarJogo(nome_input.getText());
                        popUpInfo(game_.getHumano().getNome(), game_.getTerritorios(game_.getHumano()),
                                game_.getCPU().getNome());
                    }
                }
        );

        //Layout
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

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
        jogador_selecao_vizinhos = new JLabel("Vizinhos: Nenhum");
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
        cpu_selecao_vizinhos = new JLabel("Vizinhos: Nenhum");
        cpu_selecao_num_terr = new JLabel("Exércitos Terrestres: 0");
        cpu_selecao_num_aereo = new JLabel("Exércitos Aéreos: 0");
        tabela = new JTable(tabela_mapa_);
        JButton prox_rodada = new JButton("Terminar rodada");

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

                            if(selecionado_.getOcupante() == game_.getHumano()){
                                updateInfos();
                                cpu_selecao_nome_pais.setText("Nome: Nenhum");
                                cpu_selecao_num_terr.setText("Exércitos Terrestres: 0");
                                cpu_selecao_num_aereo.setText("Exércitos Aéreo: 0");
                                cpu_selecao_vizinhos.setText("Vizinhos: Nenhum");
                            }
                            else {
                                updateInfos();
                                jogador_selecao_nome_pais.setText("Nome: Nenhum");
                                jogador_selecao_num_terr.setText("Exércitos Terrestres: 0");
                                jogador_selecao_num_aereo.setText("Exércitos Aéreo: 0");
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
                                terminado_ataque_ = false;

                                if(selecionado_ != null){
                                    jogador_atacar_terr.setEnabled(!jogador_atacar_terr.isEnabled());
                                    jogador_atacar_aereo.setEnabled(!jogador_atacar_terr.isEnabled());
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
                        if(terminado_ataque_){
                            JOptionPane.showMessageDialog(tabela_pane, "Você começou a movimentar tropas," +
                                    "não pode mais atacarTerrestre");
                        }
                        else{
                            List<Territorio> t = checkAereo();
                            if(t.size() == 0){
                                JOptionPane.showMessageDialog(janela_, "Este território não faz fronteira com" +
                                        "nenhum terriório que pode ser atacado por ar");
                                atacando_ = false;
                            }
                            else {
                                atacarAereo(checkAereo());
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
                                terminado_ataque_ = true;
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
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 5, 5);

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

        jogador_pane.add(jogador_atacar_terr, c);

        c.gridy = 11;

        jogador_pane.add(jogador_atacar_aereo, c);

        c.gridy = 12;

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

        cpu_pane.add(cpu_selecao_vizinhos, c);

        //Adição de panels ao panl principal
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(280, -100, 5, 50);

        jogador_pane.setOpaque(false); //Panel transparente
        janela_.getContentPane().add(jogador_pane, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(580, 0, 5, 0);

        tabela_pane.setOpaque(false);
        janela_.getContentPane().add(tabela_pane, c);

        c.gridx = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(280, 50, 5, 0);

        cpu_pane.setOpaque(false);
        janela_.getContentPane().add(cpu_pane, c);

        show();
    }


    /**
     * Tela de distribuição de exército
     */
    public void distribuirExercito() {
        int terr_recebidos = game_.getHumano().getTerrestresRecebidos();
        int aereo_recebidos = game_.getHumano().getAereos_recebidos_();
        int i = 1;
        List<Territorio> territorios = game_.getTerritorios(game_.getHumano());

        //Configura o frame
        JFrame frame = new JFrame("Distribuir Exércitos");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        //Classe que define o comportamento dos terrTextFields dos terrestres
        class TropasListener implements DocumentListener {
            private JTextField text;

            private TropasListener(JTextField text) {
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
                        int v;
                        if (!Objects.equals(text.getText(), "")) {
                            try {
                                v = Integer.parseInt(text.getText());
                            } catch (NumberFormatException e) {
                                text.setText("0");
                                JOptionPane.showMessageDialog(null, "Erro, valor não numérico!",
                                        "ERRO", JOptionPane.OK_OPTION);
                                return;
                            }


                            if (v > terr_recebidos) {
                                text.setText("0");
                                JOptionPane.showMessageDialog(null, "Erro, valor maior que total de" +
                                        " exércitos disponiveis!", "ERRO", JOptionPane.OK_OPTION);
                            }
                        }
                    }
                };
                SwingUtilities.invokeLater(doCheck);
            }
        }


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
                Integer.toString(game_.getHumano().getAereos_recebidos_()));
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
            in_terr.getDocument().addDocumentListener(new TropasListener(in_terr));

            in_aereo.setText("0");
            in_aereo.getDocument().addDocumentListener(new TropasListener(in_aereo));

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
        jogador_num_continentes.setText("Número de continentes: ");
        jogador_num_terr.setText("Exércitos Terrestres: " + game_.getNumTerrestres(humano));
        jogador_num_aereo.setText("Exércitos Aéreos: " + game_.getNumAereos(humano));
        cpu_num_territorios.setText("Número de territórios: " + game_.getNumTerritorios(cpu));
        cpu_num_continentes.setText("Número de continentes: ");
        cpu_num_terr.setText("Exércitos Terrestres: " + game_.getNumTerrestres(cpu));
        cpu_num_aereo.setText("Exércitos Aéreos: " + game_.getNumAereos(cpu));
    }

    /**
     *
     */
    public int defender(Territorio territorio, Territorio alvo, int qtd_ataque) {
        //Configura o frame
        JFrame frame = new JFrame("Defender território");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        int n_tropas = alvo.getNumExTerrestres();


        //Componentes

        JLabel descricao = new JLabel("Os exércitos de " + game_.getCPU().getNome() + " estão atacando!");

        JLabel territorio_alvo = new JLabel(alvo.getNome() + " está em perigo!");
        JLabel alvo_info = new JLabel("Tropas neste território: " + n_tropas);
        JLabel defender_text = new JLabel("Defender território com: ");

        ButtonGroup bg = new ButtonGroup();
        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);

        if (n_tropas < 3) {
            r3.setEnabled(false);
            if (n_tropas != 2) {
                r2.setEnabled(false);
            }
        }

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

        r1.doClick();

        JLabel territorio_atacando = new JLabel(territorio.getNumExTerrestres() + " exércitos marcham de " + territorio.getNome());

        JButton defender = new JButton("Defender");

        //Configura os constrains para cada coluna
        GridBagConstraints c = new GridBagConstraints();
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
        return n_defender;
    }

    /**
     * Tela de pop-up de ataque
     */
    private void atacarTerrestre() {
        atacando_ = true;
        n_ataque = 1;
        int n_tropas = selecionado_.getNumExTerrestres();

        //Verifica quantos e quais territórios estão disponíveis para atacarTerrestre (são inimigos)
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

        if (n_tropas < 3) {
            r3.setEnabled(false);
            if (n_tropas != 2) {
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
                        System.out.println(n_ataque); //Atacar
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
        selecionado_destino = (Territorio) (combo.getSelectedItem());

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
                        selecionado_destino = (Territorio) ((JComboBox) e.getSource()).getSelectedItem();
                        dono.setText("Dono: " + selecionado_destino.getOcupante().getNome());
                        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());
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

        dono.setText("Dono: " + selecionado_destino.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + selecionado_destino.getNumExTerrestres());

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
     * Atualiza as informações de seleção de territórios
     */
    private void updateInfos(){
        JLabel nome_pais;
        JLabel num_terr;
        JLabel num_aereo;
        JLabel vizinhos;

        if(selecionado_.getOcupante() == game_.getHumano()){
            nome_pais = jogador_selecao_nome_pais;
            num_terr = jogador_selecao_num_terr;
            num_aereo = jogador_selecao_num_aereo;
            vizinhos = jogador_selecao_vizinhos;
        }
        else{
            nome_pais = cpu_selecao_nome_pais;
            num_terr = cpu_selecao_num_terr;
            num_aereo = cpu_selecao_num_aereo;
            vizinhos = cpu_selecao_vizinhos;
        }
        //Se a célula for vazia desativa o botão
        if(selecionado_ == null){
            jogador_atacar_terr.setEnabled(false);
            jogador_movimentar.setEnabled(false);
            return;
        }

        //Atualiza os campos com os respectivos dados
        if(selecionado_ != null) {
            if(selecionado_.getOcupante().equals(game_.getHumano())){
                if(selecionado_.getNumExTerrestres() > 1) {
                    jogador_atacar_terr.setEnabled(true);
                }
                if(selecionado_.getNumExAereos() != 0) {
                    jogador_atacar_aereo.setEnabled(true);
                }
                jogador_movimentar.setEnabled(true);
            }
            else{
                jogador_atacar_terr.setEnabled(false);
                jogador_atacar_aereo.setEnabled(false);
                jogador_movimentar.setEnabled(false);
            }

            Territorio[] fronteira = selecionado_.getFronteira();

            nome_pais.setText("Pais selecionao: " + selecionado_.getNome());
            num_terr.setText("Exércitos Terrestres: " + selecionado_.getNumExTerrestres());
            num_aereo.setText("Exércitos Aéreos: " + selecionado_.getNumExAereos());

            String f = "Vizinhos: ";
            for (Territorio aFronteira : fronteira) {
                f = f + aFronteira.getNome() + ", ";
            }

            vizinhos.setText(f);
        }
    }

    private void atacarAereo(List<Territorio> territorios){
        atacando_ = true;
        n_ataque = 0;
        int n_tropas = selecionado_.getNumExAereos();

        Territorio[] terrs = new Territorio[territorios.size()];

        for(int i = 0; i < terrs.length; i++){
            terrs[i] = territorios.get(i);
        }

        //Cria a nova janela
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
        JComboBox<Territorio> combo = new JComboBox<>(terrs);
        selecionado_destino = (Territorio) (combo.getSelectedItem());

        JLabel titulo = new JLabel("Atacando de: " + selecionado_.getNome());
        JLabel atacar = new JLabel("Atacar: ");
        JLabel quantidade = new JLabel("Atacar com:");
        JLabel num_terr = new JLabel("Exércitos Terrestres: ");
        JLabel num_aereo = new JLabel("Exércitos Aereos: " + selecionado_destino.getNumExAereos());
        JButton reforco = new JButton("Chamar reforço");
        JButton ok = new JButton("Atacar");

        ButtonGroup bg = new ButtonGroup();
        JRadioButton r1 = new JRadioButton("1");
        JRadioButton r2 = new JRadioButton("2");
        JRadioButton r3 = new JRadioButton("3");
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        r1.doClick();

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
                        if(r1.isSelected()){
                            System.out.println("1");
                        }
                        if(r2.isSelected()){
                            System.out.println("2");
                        }
                        if(r3.isSelected()){
                            System.out.println("3");
                        }
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
                            reforcoAereo(t, r1, r2, r3);
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

    private void reforcoAereo(Territorio[] territorios, JRadioButton r1, JRadioButton r2, JRadioButton r3){

        //Cria a nova janela
        JFrame frame = new JFrame("Ataque aéreo");
        JPanel pane = new JPanel(new GridBagLayout());
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

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
     * Atualiza os dados da tela de ataque
     * @param dono Label do dono do território
     * @param num_terr O label do número de exércitos terrestres
     * @param num_aereo Label referente ao número de exércitos aéros
     * @param t Território utilizado para atualizar os dados
     */
    private void updatePopUpInfo(JLabel dono, JLabel num_terr, JLabel num_aereo, Territorio t){
        dono.setText("Dono: " + t.getOcupante().getNome());
        num_terr.setText("Exércitos Terrestres: " + t.getNumExTerrestres());
        num_aereo.setText("Exércitos Aéreos: " + t.getNumExAereos());
    }

    private List<Territorio> checkAereo(){
        List<Territorio> pode_atacar = new ArrayList<>();

        for(Territorio t : selecionado_.getFronteira()){
            if((t.getNumExTerrestres() > 3) && (t.getNumExAereos() > 0)){
                pode_atacar.add(t);
            }
        }

        return pode_atacar;
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
