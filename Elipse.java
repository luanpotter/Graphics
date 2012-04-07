/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package editorgrafico;

import java.awt.*;

public class Elipse extends Figura {

    float raioA, raioB, angulo;
    Color corFundo;

    public Elipse(float x, float y, float raioA, float raioB, float angulo, Color cor, Color corFundo, int ordem) {
        this (x, y, raioA, raioB, angulo, cor, corFundo, ordem, 1, 0);
    }
    
    public Elipse(float x, float y, float raioA, float raioB, float angulo, Color cor, Color corFundo, int ordem, int tamanho, float tracejado) {
        this.x = x;
        this.y = y;
        this.raioA = raioA;
        this.raioB = raioB;
        this.angulo = angulo;
        this.cor = cor;
        this.corFundo = corFundo;
        this.stroke = getBasicStroke (tamanho, tracejado);
    }
    
    public Elipse (String texto) throws Exception {
        String[] palavras = texto.split (":");
        this.x        = Float.parseFloat(palavras[1]);
        this.y        = Float.parseFloat(palavras[2]);
        this.cor      = new Color (Integer.parseInt(palavras[3]), Integer.parseInt(palavras[4]), Integer.parseInt(palavras[5]));
        this.stroke   = new BasicStroke (Integer.parseInt(palavras[6]), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, Float.parseFloat(palavras[7]), dash, 0.0f);
        this.raioA    = Float.parseFloat(palavras[8]);
        this.raioB    = Float.parseFloat(palavras[9]);
        this.angulo   = Float.parseFloat(palavras[10]);
        this.corFundo = new Color (Integer.parseInt(palavras[11]), Integer.parseInt(palavras[12]), Integer.parseInt(palavras[13]));
    }

    public float getAngulo() {
        return angulo;
    }

    public void setAngulo(float angulo) {
        this.angulo = angulo;
    }

    public Color getCorFundo() {
        return corFundo;
    }

    public void setCorFundo(Color corFundo) {
        this.corFundo = corFundo;
    }

    public float getRaioA() {
        return raioA;
    }

    public void setRaioA(float raioA) {
        this.raioA = raioA;
    }

    public float getRaioB() {
        return raioB;
    }

    public void setRaioB(float raioB) {
        this.raioB = raioB;
    }

    @Override
    public void desenhar(Graphics g, ContextoGrafico c) {
        int raioAC = c.converter(raioA);
        int raioBC = c.converter(raioB);
        int xCentro = c.converteX(x);
        int yCentro = c.converteY(y);
        g.setColor(this.cor);
        g.drawOval(xCentro - raioAC, yCentro - raioBC, 2 * raioAC, 2 * raioBC);
    }
    
    @Override
    public String toString() {
        return "l" + ":" + x + ":" + y + ":"
                + cor.getRed() + ":" + cor.getGreen()
                + ":" + cor.getBlue() + ":" + stroke.getLineWidth()
                + ":" + stroke.getDashPhase() + ":" + raioA + ":" +
                raioB + ":" + angulo + ":" +
                corFundo.getRed() + ":" + corFundo.getGreen() + ":" +
                corFundo.getBlue();
    }
}
