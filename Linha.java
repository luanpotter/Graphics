package editorgrafico;

import java.awt.*;

public class Linha extends Figura {
    // herda (x, y) da classe Ponto, que são as coordenadas
    // do ponto inicial da reta; também herda a cor e, em
    // seguida define o ponto final:

    private float x2, y2;

    public Linha(float x1, float y1, float x2, float y2, Color cor, int ordem) {
        this (x1, y1, x2, y2, cor, ordem, 1, 0);
    }
    
    public Linha(float x1, float y1, float x2, float y2, Color cor, int ordem, int tamanho, float tracejado) {
        this.x = x1;
        this.y = y1;
        this.x2 = x2;
        this.y2 = y2;
        
        this.ordem = ordem;
        this.cor = cor;
        this.stroke = getBasicStroke (tamanho, tracejado);
    }
    
    public Linha (String texto) throws Exception {
        String[] palavras = texto.split (":");
        this.x      = Float.parseFloat(palavras[1]);
        this.y      = Float.parseFloat(palavras[2]);
        this.cor    = new Color (Integer.parseInt(palavras[3]), Integer.parseInt(palavras[4]), Integer.parseInt(palavras[5]));
        this.stroke = new BasicStroke (Integer.parseInt(palavras[6]), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, Float.parseFloat(palavras[7]), dash, 0.0f);
        this.x2      = Float.parseFloat(palavras[8]);
        this.y2      = Float.parseFloat(palavras[9]);
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }    

    @Override
    public void desenhar(Graphics g, ContextoGrafico c) {
        int x1Desenho = c.converteX(x);
        int y1Desenho = c.converteY(y);
        int x2Desenho = c.converteX(x2);
        int y2Desenho = c.converteY(y2);
        Graphics2D g2d = (Graphics2D) g.create ();
        g2d.setColor(cor);
        g2d.setStroke (stroke);
        g2d.drawLine(x1Desenho, y1Desenho, x2Desenho, y2Desenho);
        g2d.dispose ();
    }
    
    @Override
    public String toString() {
        return "l" + ":" + x + ":" + y + ":"
                + cor.getRed() + ":" + cor.getGreen()
                + ":" + cor.getBlue() + ":" + stroke.getLineWidth()
                + ":" + stroke.getDashPhase() + ":" + x2 + ":" + y2;
    }
}