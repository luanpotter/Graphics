package editorgrafico;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Luan
 */

public abstract class Figura implements Comparable {
    protected Color cor;
    protected BasicStroke stroke;
    protected static float dash[] = { 10.0f };    
    protected int ordem;
    protected float x, y;
    
    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
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
    public int compareTo(Object outroPonto) {
        return ordem - ((Ponto) outroPonto).ordem;
    }
    
    public abstract void desenhar(Graphics g, ContextoGrafico c);
    
    public static BasicStroke getBasicStroke (int tamanho, float tracejado) {
        if (tracejado < 1)
            tracejado = 1;
        return new BasicStroke(tamanho, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, tracejado, dash, 0.0f);
    }
}
