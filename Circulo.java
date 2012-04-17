package editorgrafico;

import java.awt.*;

public class Circulo extends Figura 
{
    
   private float raio;
   private Color corFundo;
   
   public Circulo (float xCentro, float yCentro, float novoRaio, Color novaCor, Color corFundo, int ordem) {
       this (xCentro, yCentro, novoRaio, novaCor, corFundo, ordem, 1, 0);
   }
           
   public Circulo (float xCentro, float yCentro, float novoRaio, Color novaCor, Color corFundo, int ordem, int tamanho, float tracejado) 
   {
        this.x = xCentro;
        this.y = yCentro;
        this.raio = novoRaio;
        
        this.cor = novaCor;
        this.corFundo = corFundo;
        this.ordem = ordem;
        this.stroke = getBasicStroke (tamanho, tracejado);
   }
   
    public Circulo (String texto) throws Exception {
        String[] palavras = texto.split (":");
        this.x        = Float.parseFloat(palavras[1]);
        this.y        = Float.parseFloat(palavras[2]);
        this.cor      = new Color (Integer.parseInt(palavras[3]), Integer.parseInt(palavras[4]), Integer.parseInt(palavras[5]));
        this.stroke   = new BasicStroke (Integer.parseInt(palavras[6]), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, Float.parseFloat(palavras[7]), dash, 0.0f);
        this.raio     = Float.parseFloat(palavras[8]);
        this.corFundo = new Color (Integer.parseInt(palavras[9]), Integer.parseInt(palavras[10]), Integer.parseInt(palavras[11]));
    }
   
    @Override
    public void desenhar (Graphics g, ContextoGrafico c)
    {
        int xCentro = c.converteX(x);
        int yCentro = c.converteY(y);
        int raioEmX = c.converter(raio);
        Graphics2D g2d = (Graphics2D) g.create ();
        if (this.corFundo != null) {
            g2d.setColor  (this.corFundo);
            g2d.fillOval(xCentro - raioEmX, yCentro - raioEmX, 2*raioEmX,2*raioEmX);
        }
        g2d.setColor  (this.cor);
        g2d.setStroke (this.stroke);
        g2d.drawOval(xCentro - raioEmX, yCentro - raioEmX, 2*raioEmX,2*raioEmX);
        g2d.dispose ();
    }
    
    @Override
    public String toString() {
        return "l" + ":" + x + ":" + y + ":"
                + cor.getRed() + ":" + cor.getGreen()
                + ":" + cor.getBlue() + ":" + stroke.getLineWidth()
                + ":" + stroke.getDashPhase() + ":" + raio + ":" +
                corFundo.getRed() + ":" + corFundo.getGreen() + ":" +
                corFundo.getBlue();
    }
}
