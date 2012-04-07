package editorgrafico;

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
    
    @Override
    public String toString () {
        return xSupDir + ":" + ySupDir + ":" + xInfEsq + ":" + yInfEsq;
    }
}
