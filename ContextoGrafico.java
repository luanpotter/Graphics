package editorgrafico;

import java.awt.Color;

/**
 * @author Luan
 */
public class ContextoGrafico {

    private float xSupDir, ySupDir, xInfEsq, yInfEsq, zoom;
    private int largura, altura;

    public ContextoGrafico(float xSupDir, float ySupDir, float xInfEsq, float yInfEsq, int largura, int altura) {
        this.xSupDir = xSupDir;
        this.ySupDir = ySupDir;
        this.xInfEsq = xInfEsq;
        this.yInfEsq = yInfEsq;
        this.zoom = 1;
        this.altura = altura;
        this.largura = largura;
    }

    public int converteX(float x) {
        return (int) ((x - xInfEsq) / Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura));
    }

    public int converteY(float y) {
        return (int) ((y - yInfEsq) / Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura));
    }

    public int converter(float medida) {
        return (int) (medida / Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura));
    }
    
    public float desconverteX(int x) {
        return x * Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura) + xInfEsq;
    }

    public float desconverteY(int y) {
        return y * Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura) + yInfEsq;
    }
    
    public float desconverter(int medida) {
        return medida * Math.max((xSupDir - xInfEsq) / largura, (ySupDir - yInfEsq) / altura);
    }
    
    public Ponto desconverterPonto (int x, int y) {
        return new Ponto (desconverteX(x), desconverteY(y), Color.BLACK, 0);
    }
    
    public float distanciaEntre (float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    
    public float distanciaEntre (Ponto p1, Ponto p2) {
        return distanciaEntre (p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    
    public float distancaiEntre (Ponto p1, int x2, int y2) {
        return distanciaEntre (p1.getX(), p1.getY(), desconverteX(x2), desconverteY(y2));
    }
    
    @Override
    public String toString () {
        return xSupDir + ":" + ySupDir + ":" + xInfEsq + ":" + yInfEsq;
    }
}
