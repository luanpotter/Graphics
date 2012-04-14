package editorgrafico;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditorGrafico extends JFrame {

    private LButton[] botoes;
    private JLabel status, coords;
    private JPanel pnlBotoes;  // container dos botões acima
    private JDesktopPane deskframe;
    private ArrayList<Desenho> desenhos;
    private int desenhoAtual, operacaoAtual, estadoOperacao;
    private int tamanho, tracejado; //Stroke
    private Color corAtual, corFundo;
    private Figura[] temp;
    private static int LARGURA_INICIAL = 400, ALTURA_INICIAL = 200;

    public EditorGrafico() // construtor de Editor que criará o JFrame, colocará seu
    {   // título, estabelecerá um tamanho para o formulário e o exibirá
        super("Editor Gráfico"); // cria o JFrame e coloca um título

        this.addWindowListener(new WindowAdapter() { // cria instância da interface
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // método para procurar pastas System.getProperty("string com o nome da propriedade desejada")
        botoes = new LButton[8];
        botoes[0] = new LButton("system\\open", 0, "Escolha o arquivo que deseja abrir.", "Abrir um arquivo");
        botoes[1] = new LButton("system\\save", 1, "Escolha o arquivo em que deseja salvar.", "Salvar em um arquivo");
        botoes[2] = new LButton("system\\new", 2, "Um novo desenho foi criado.", "Novo desenho");
        botoes[3] = new LButton("system\\close", 3, "Fechando janela atual...", "Fechar janela atual");
        botoes[4] = new LButton("shapes\\ponto", 6, "Clique no local do ponto que deseja desenhar.", "Desenhar um ponto");
        botoes[5] = new LButton("shapes\\linha", 7, "Clique no primeiro ponto da linha.", "Desenhar uma linha");
        botoes[6] = new LButton("shapes\\circulo", 8, "Clique no centro do círculo.", "Desenhar um círculo");
        botoes[7] = new LButton("shapes\\elipse", 9, "Clique em um dos vértices do retângulo que circunscreve a elipse.", "Desenhar uma elipse");

        // cria o JPanel que armazenará os botões
        pnlBotoes = new JPanel();

        pnlBotoes.setLayout(new FlowLayout());
        for (int i = 0; i < botoes.length; i++)
            pnlBotoes.add(botoes[i]);

        setSize(700, 500); // dimensões do formulário em pixels
        setVisible(true); // exibe o formulário

        Container cntForm = this.getContentPane(); // acessa o painel de conteúdo do frame
        cntForm.setLayout(new BorderLayout());
        cntForm.add(pnlBotoes, BorderLayout.NORTH);

        status = new JLabel("Bem vindo!");
        coords = new JLabel("x: 0, y: 0");
        JPanel labels = new JPanel();
        labels.setLayout(new BorderLayout());
        labels.add(status, BorderLayout.CENTER);
        labels.add(coords, BorderLayout.EAST);
        cntForm.add(labels, BorderLayout.SOUTH);

        deskframe = new JDesktopPane();
        cntForm.add(deskframe, BorderLayout.CENTER);

        desenhos = new ArrayList<Desenho>();
        desenhos.add(new Desenho(0));
        desenhoAtual = 0;
        estadoOperacao = 0;
        operacaoAtual = 0;
        tamanho = 1;
        tracejado = 0;
        corAtual = Color.BLACK;
        corFundo = null;
        deskframe.add(desenhos.get(0));
        focusAtual ();
    }

    public static void main(String[] args) {
        EditorGrafico aplicacao = new EditorGrafico();
    }

    private class Desenho extends JInternalFrame implements WindowListener {
        AreaDesenho desenho;
        int ordem;

        public Desenho(int ordem) {
            this(ordem, "Novo", new ListaSimples(), new ContextoGrafico(LARGURA_INICIAL, ALTURA_INICIAL, 0, 0, LARGURA_INICIAL, ALTURA_INICIAL), LARGURA_INICIAL, ALTURA_INICIAL);
        }

        public Desenho(int ordem, String nome, ListaSimples l, ContextoGrafico c) {
            this(ordem, nome, l, c, (int) (EditorGrafico.super.getWidth() / 2), (int) (EditorGrafico.super.getHeight() / 2));
        }

        public Desenho(int ordem, String nome, ListaSimples l, ContextoGrafico c, int w, int h) {
            super(nome, true, true, true, true);
            desenho = new AreaDesenho(c, l);
            this.add(desenho);
            this.ordem = ordem;

            this.setSize(w, h);
            this.setOpaque(true);
            this.setVisible(true);
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            fecharJanela (ordem);
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowActivated(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        private class AreaDesenho extends JPanel implements MouseListener, MouseMotionListener {

            ContextoGrafico contextoGrafico;
            ListaSimples figuras;

            public AreaDesenho(ContextoGrafico contextoGrafico, ListaSimples figuras) {
                super();
                this.contextoGrafico = contextoGrafico;
                this.figuras = figuras;
                this.addMouseListener(this);
                this.addMouseMotionListener(this);
            }

            @Override
            public void paintComponent(Graphics g) {
                figuras.iniciarPercurso();
                while (figuras.podeAvancar()) {
                    try {
                        ((Figura) figuras.getAtual()).desenhar(g, contextoGrafico);
                    } catch (Exception ex) {
                    }
                    figuras.avancar();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (desenhoAtual != ordem) {
                    desenhoAtual = ordem;
                    estadoOperacao = 0;
                    status.setText(getStatusText(operacaoAtual));
                }
                AreaDesenho des = desenhos.get(desenhoAtual).desenho;
                ContextoGrafico c = des.contextoGrafico;
                switch (operacaoAtual) {
                    case 6: {
                        Ponto f = new Ponto(des.contextoGrafico.desconverteX(e.getX()), des.contextoGrafico.desconverteY(e.getY()), corAtual, 0);
                        des.figuras.incluirDadoAoFinal(f);
                        f.desenhar(des.getGraphics(), des.contextoGrafico);
                        break;
                    }
                    case 7: {
                        if (estadoOperacao == 0) {
                            temp = new Figura[1];
                            temp[0] = c.desconverterPonto(e.getX(), e.getY());
                            estadoOperacao = 1;
                            status.setText("Agora, clique no outro ponto da reta.");
                        } else {
                            Linha f = new Linha(temp[0].getX(), temp[0].getY(), c.desconverteX(e.getX()), c.desconverteY(e.getY()), corAtual, 0);
                            des.figuras.incluirDadoAoFinal(f);
                            f.desenhar(des.getGraphics(), des.contextoGrafico);
                            estadoOperacao = 0;
                            status.setText("Clique no primeiro ponto da linha");
                        }
                        break;
                    }
                    case 8: {
                        if (estadoOperacao == 0) {
                            temp = new Figura [1];
                            temp[0] = c.desconverterPonto(e.getX(), e.getY());
                            estadoOperacao = 1;
                            status.setText ("Agora, clique em calcular lugar que pertença ao círculo.");
                        } else {
                            Circulo f = new Circulo (temp[0].getX(), temp[0].getY(), c.distancaiEntre((Ponto) temp[0], e.getX(), e.getY()), corAtual, corFundo, 0, tamanho, tracejado);
                            des.figuras.incluirDadoAoFinal(f);
                            f.desenhar(des.getGraphics(), des.contextoGrafico);
                            estadoOperacao = 0;
                            status.setText("Clique no centro do círculo.");
                        }
                        break;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                ContextoGrafico c = desenhos.get(desenhoAtual).desenho.contextoGrafico;
                coords.setText("x: " + c.desconverteX(e.getX()) + ", y: " + c.desconverteY(e.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        }
    }

    public void abrirArquivo() {
        JFileChooser arqEscolhido = new JFileChooser();
        arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (arqEscolhido.showOpenDialog(EditorGrafico.this) == JFileChooser.APPROVE_OPTION) {
            File arquivo = arqEscolhido.getSelectedFile();
            try {
                BufferedReader figs = new BufferedReader(new FileReader(arquivo));
                ListaSimples l = new ListaSimples();
                String linha = figs.readLine();
                String[] partes = linha.split(":");
                ContextoGrafico c = new ContextoGrafico(Float.parseFloat(partes[0]), Float.parseFloat(partes[1]), Float.parseFloat(partes[2]), Float.parseFloat(partes[3]), LARGURA_INICIAL, ALTURA_INICIAL);
                while (figs.ready()) {
                    linha = figs.readLine();
                    switch (linha.charAt(0)) {
                        case 'p':
                            l.incluirDadoAoFinal(new Ponto(linha));
                            break;
                        case 'l':
                            l.incluirDadoAoFinal(new Linha(linha));
                            break;
                        case 'c':
                            l.incluirDadoAoFinal(new Circulo(linha));
                            break;
                        case 'e':
                            l.incluirDadoAoFinal(new Elipse(linha));
                            break;
                    }
                }

                if (desenhoAtual == -1) {
                    desenhoAtual = desenhos.size();
                    Desenho d = new Desenho(desenhoAtual, arquivo.getName(), l, c);
                    desenhos.add(d);
                } else {
                    if (JOptionPane.showConfirmDialog(this, "Mensagem", "Aplicação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
                        return;
                    deskframe.remove(desenhos.get(desenhoAtual));
                    Desenho d = new Desenho(desenhoAtual, arquivo.getName(), l, c);
                    desenhos.set(desenhoAtual, d);
                }
                deskframe.add(desenhos.get(desenhoAtual));
                focusAtual ();
                status.setText("Arquivo aberto com sucesso.");
            } catch (FileNotFoundException ex) {
                status.setText("Arquivo não encontrado.");
            } catch (IOException ex) {
                status.setText("Erro na leitura do arquivo.");
            } catch (Exception ex) {
                status.setText("Arquivo inválido.");
            }
        }

        zerarBotoes();
    }

    public void salvarArquivo() {
        if (desenhoAtual == -1) {
            status.setText("Você deve selecionar o desenho que deseja salvar.");
            return;
        }
        JFileChooser arqEscolhido = new JFileChooser();
        arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (arqEscolhido.showSaveDialog(EditorGrafico.this) == JFileChooser.APPROVE_OPTION) {
            ContextoGrafico c = desenhos.get(desenhoAtual).desenho.contextoGrafico;
            ListaSimples l = desenhos.get(desenhoAtual).desenho.figuras;
            File arquivo = arqEscolhido.getSelectedFile();
            try {
                BufferedWriter figs = new BufferedWriter(new FileWriter(arquivo));
                figs.write(c.toString());
                l.iniciarPercurso();
                while (l.podeAvancar()) {
                    figs.write(l.getAtual().toString());
                    l.avancar();
                }
                desenhos.get(desenhoAtual).setName(arquivo.getName());
                status.setText("Arquivo salvo com sucesso.");
            } catch (IOException ex) {
                status.setText("Erro na escrita do arquivo.");
            } catch (Exception ex) {
                status.setText("Arquivo inválido.");
            }
        }

        zerarBotoes();
    }
    
    public void novoArquivo () {
        desenhoAtual = desenhos.size ();
        desenhos.add( new Desenho (desenhoAtual) );
        deskframe.add (desenhos.get(desenhoAtual));
        focusAtual ();
        zerarBotoes ();
    }
    
    private void focusAtual () {
        try {
            desenhos.get(desenhoAtual).setSelected(true);
        } catch (Exception e) {
            System.err.println ("Erro inesperado.");
        }
    }
    
    public void fecharAtual () {
        if (desenhoAtual != -1) {
            Desenho d = desenhos.get (desenhoAtual);
            desenhos.remove (desenhoAtual);
            deskframe.remove (d);
            desenhoAtual = desenhos.size () - 1;
            if (desenhoAtual != -1)
                focusAtual ();
            status.setText ("Janela fechada com sucesso.");
        } else
            status.setText ("Nenhuma janela está selecionada.");
        zerarBotoes ();
    }
    
    public void fecharJanela (int janela) {
        Desenho d = desenhos.get (janela);
        desenhos.remove (janela);
        deskframe.remove (d);
        if (desenhoAtual == janela) {
            desenhoAtual = desenhos.size () - 1;
            if (desenhoAtual != -1)
                try {
                    ((Desenho) deskframe.getAllFrames()[desenhoAtual]).setSelected(true);
                } catch (Exception e) {
                    System.err.println ("Erro inesperado.");
                }
        }
    }

    public void zerarBotoes() {
        for (int i = 0; i < botoes.length; i++)
            botoes[i].setUp(false);
    }
    
    public String getStatusText (int operacao) {
        if (operacao < 1)
            return "";
        for (int i = 0; i < botoes.length; i++)
            if (botoes[i].btnStatus == operacao)
                return botoes[i].mensagem;
        return "";
    }

    private class LButton extends JButton implements ActionListener {

        protected int btnStatus;
        protected String mensagem, cursorImg;
        protected boolean up;

        public LButton(String img, int btnStatus, String mensagem, String tooltip, String caminho) {
            super(new ImageIcon("imgs//" + img + ".png"));

            this.setToolTipText(tooltip);
            this.btnStatus = btnStatus;
            this.mensagem = mensagem;
            this.up = false;

            this.cursorImg = "imgs//" + caminho + ".png";
            this.setBorder(null);
            addActionListener(this);
        }

        public LButton(String img, int btnStatus, String mensagem, String tooltip) {
            this(img, btnStatus, mensagem, tooltip, "imgs//cursor.png");
        }

        @Override
        public void paintComponent(Graphics g) {
            this.setBackground(Color.LIGHT_GRAY);
            if (up)
                this.setBorder(BorderFactory.createBevelBorder(1));
            else
                this.setBorder (null);
            super.paintComponent(g);
        }

        public void setUp(boolean b) {
            this.up = b;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            /*Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage(cursorImg);
            Point hotSpot = new Point(0, 0);
            Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Cursor");
            this.setCursor(cursor);*/

            zerarBotoes();
            this.setUp(true);
            estadoOperacao = 0;

            status.setText(mensagem);
            EditorGrafico.this.repaint();

            switch (btnStatus) {
                case 0:
                    abrirArquivo();
                    break;
                case 1:
                    salvarArquivo();
                    break;
                case 2:
                    novoArquivo ();
                    break;
                case 3 :
                    fecharAtual ();
                    break;
                default:
                    operacaoAtual = btnStatus;
                    break;
            }
        }
    }
}
