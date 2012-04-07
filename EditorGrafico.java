package editorgrafico;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditorGrafico extends JFrame {

    private LButton[] botoes;
    private JLabel status;
    private JPanel pnlBotoes;  // container dos botões acima
    private JDesktopPane deskframe;

    private ArrayList<Desenho> desenhos;
    private int desenhoAtual, operacaoAtual, estadoOperacao;
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
        botoes = new LButton[4];
        botoes[0] = new LButton("abrir", 0, "Escolha o arquivo que deseja abrir.", "Abrir um arquivo");
        botoes[1] = new LButton("salvar", 1, "Escolha o arquivo em que deseja salvar.", "Salvar em um arquivo");
        botoes[2] = new LButton("ponto", 6, "Clique no local do ponto que deseja desenhar.", "Desenhar um ponto");
        botoes[3] = new LButton("linha", 7, "Clique no primeiro ponto da linha.", "Desenhar uma linha");

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
        cntForm.add(status, BorderLayout.SOUTH);

        deskframe = new JDesktopPane();
        cntForm.add(deskframe, BorderLayout.CENTER);

        desenhos = new ArrayList<Desenho>();
        desenhos.add(new Desenho());
        desenhoAtual = 0;
        estadoOperacao = 0;
        operacaoAtual = 0;
        corAtual = Color.BLACK;
        corFundo = null;
        deskframe.add(desenhos.get(0));
    }

    public static void main(String[] args) {
        EditorGrafico aplicacao = new EditorGrafico();
    }

    private class Desenho extends JInternalFrame {

        AreaDesenho desenho;

        public Desenho() {
            super("Novo", true, true, true, true);
            desenho = new AreaDesenho(new ContextoGrafico(0, 0, LARGURA_INICIAL, ALTURA_INICIAL, LARGURA_INICIAL, ALTURA_INICIAL), new ListaSimples());
            this.add(desenho);

            this.setSize(LARGURA_INICIAL, ALTURA_INICIAL);
            this.show();
            this.setOpaque(true);
        }

        public Desenho(String nome, ListaSimples l, ContextoGrafico c) {
            super(nome, true, true, true, true);
            desenho = new AreaDesenho(c, l);
            this.add(desenho);

            this.setSize(this.getWidth() / 2, this.getHeight() / 2);
            this.setOpaque(true);
            this.setVisible(true);
        }

        private class AreaDesenho extends JPanel implements MouseListener {

            ContextoGrafico contextoGrafico;
            ListaSimples figuras;

            public AreaDesenho(ContextoGrafico contextoGrafico, ListaSimples figuras) {
                super();
                this.contextoGrafico = contextoGrafico;
                this.figuras = figuras;
                this.addMouseListener(this);
            }

            @Override
            public void paintComponent(Graphics g) {
                figuras.iniciarPercurso();
                while (figuras.podeAvancar()) {
                    try {
                        ((Ponto) figuras.getAtual()).desenhar(g, contextoGrafico);
                    } catch (Exception ex) {
                    }
                    figuras.avancar();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                AreaDesenho des = desenhos.get(desenhoAtual).desenho;
                switch (operacaoAtual) {
                    case 6: {
                        Ponto p = new Ponto (des.contextoGrafico.desconverteX(e.getX()), des.contextoGrafico.desconverteY(e.getY()), corAtual, 0);
                        des.figuras.incluirDadoAoFinal (p);
                        //des.repaint();
                        p.desenhar(des.getGraphics(), des.contextoGrafico);
                        break;
                    }
                    case 7 : {
                        if (estadoOperacao == 0) {
                            temp = new Figura [1];
                            temp[0] = new Ponto (des.contextoGrafico.desconverteX(e.getX()), des.contextoGrafico.desconverteY(e.getY()), corAtual, 0);
                            estadoOperacao = 1;
                            status.setText("Agora, clique no outro ponto da reta.");
                        } else {
                            Linha l = new Linha (temp[0].x, temp[0].y, des.contextoGrafico.desconverteX(e.getX()), des.contextoGrafico.desconverteY(e.getY()), corAtual, 0);
                            des.figuras.incluirDadoAoFinal (l);
                            l.desenhar(des.getGraphics(), des.contextoGrafico);
                            estadoOperacao = 0;
                            status.setText ("Clique no primeiro ponto da linha");
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

                Desenho d = new Desenho(arquivo.getName(), l, c);
                if (desenhoAtual == -1) {
                    desenhoAtual = desenhos.size();
                    desenhos.add(d);
                } else {
                    if (JOptionPane.showConfirmDialog(this, "Mensagem", "Aplicação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
                        return;
                    deskframe.remove(desenhos.get(desenhoAtual));
                    desenhos.set(desenhoAtual, d);
                }
                deskframe.add(desenhos.get(desenhoAtual));
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

    public void zerarBotoes() {
        for (int i = 0; i < botoes.length; i++)
            botoes[i].setUp(false);
    }

    private class LButton extends JButton implements ActionListener {

        protected int btnStatus;
        protected String mensagem, cursorImg;
        protected boolean up;

        public LButton(String img, int btnStatus, String mensagem, String tooltip, String caminho) {
            super(new ImageIcon("imgs//" + img + ".jpg"));

            this.setToolTipText(tooltip);
            this.btnStatus = btnStatus;
            this.mensagem = mensagem;
            this.up = false;

            this.cursorImg = "imgs//" + caminho + ".jpg";
            this.setBorder(null);
            addActionListener(this);
        }

        public LButton(String img, int btnStatus, String mensagem, String tooltip) {
            this(img, btnStatus, mensagem, tooltip, "imgs//cursor.png");
        }

        @Override
        public void paintComponent(Graphics g) {
            if (up)
                this.setBorder(BorderFactory.createBevelBorder(1));
            else
                this.setBorder(BorderFactory.createBevelBorder(0));
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

            for (int i = 0; i < botoes.length; i++)
                botoes[i].setUp(false);
            this.setUp(true);

            status.setText(mensagem);
            repaint();

            switch (btnStatus) {
                case 0:
                    abrirArquivo();
                    break;
                case 1:
                    salvarArquivo();
                    break;
                default:
                    operacaoAtual = btnStatus;
                    break;
            }
        }
    }
}
