package editorgrafico;

import java.awt.*;

public class Ponto extends Figura {

    public Ponto(float x, float y, Color cor, int ordem) {
        this (x, y, cor, ordem, 1, 0);
    }
    
    public Ponto(float x, float y, Color cor, int ordem, int tamanho, float tracejado) {
        this.ordem = ordem;
        this.x = x;
        this.y = y;
        this.cor = cor;
        this.stroke = getBasicStroke(tamanho, tracejado);
    }
    
    public Ponto (String texto) throws Exception {
        String[] palavras = texto.split (":");
        this.x      = Float.parseFloat(palavras[1]);
        this.y      = Float.parseFloat(palavras[2]);
        this.cor    = new Color (Integer.parseInt(palavras[3]), Integer.parseInt(palavras[4]), Integer.parseInt(palavras[5]));
        this.stroke = new BasicStroke (Integer.parseInt(palavras[6]), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, Float.parseFloat(palavras[7]), dash, 0.0f);
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    @Override
    public void desenhar(Graphics g, ContextoGrafico c) {
        int xDesenho = c.converteX(x);
        int yDesenho = c.converteY(y);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(cor);
        g2d.setStroke(stroke);
        g2d.drawLine(xDesenho, yDesenho, xDesenho, yDesenho);
        g2d.dispose();
    }

    @Override
    public String toString() {
        return "p" + ":" + x + ":" + y + ":"
                + cor.getRed() + ":" + cor.getGreen()
                + ":" + cor.getBlue() + ":" + stroke.getLineWidth()
                + ":" + stroke.getDashPhase();
    }
}
