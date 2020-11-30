/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firefighter.desktop;

import firefighter.core.utils.FileNameExt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author romanow
 */
public class ImagePanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage originalImage = null;
    private Image image = null;
    private int xPanel,yPanel;
    private int newHight;
    public int getNewHight(){ return newHight; }
    public ImagePanel(int xPanel0, int yPanel0, FileNameExt fname) {
        initComponents();
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
                }
            });
        xPanel = xPanel0;
        yPanel = yPanel0;
        try {
            originalImage = ImageIO.read(new File(fname.fullName()));
            formComponentResized(null);
            repaint();
            } catch (IOException ex) { System.err.println("Ошибка загрузки файла "+fname.getName()); }
        }
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        int w = this.getWidth();
        int h = this.getHeight();
        double him = originalImage.getHeight();
        double wim = originalImage.getWidth();
        if (originalImage != null) {
            newHight = (int)(xPanel/wim*him);
            setBounds(10,10,xPanel,newHight);
            image = originalImage.getScaledInstance(xPanel, newHight, Image.SCALE_DEFAULT);
            this.repaint();
            }
        }
    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, null);
            }
        super.paintChildren(g);
        super.paintBorder(g);
        }
    public BufferedImage getImage() {
        return originalImage;
        }
    public void setImageFile(File iF) {
        if(iF==null)originalImage=null;
        else{
            try {
                originalImage = ImageIO.read(iF);
                } catch (IOException ex) { System.err.println("Ошибка загрузки файла "+iF.getName()); }
            formComponentResized(null);
            repaint();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String ss[]){
        JFrame ff = new JFrame();
        FileNameExt gg = new FileNameExt("src/main/resources/drawable/firefighter240.png");
        int x0 = 100;
        ImagePanel pp = new ImagePanel(x0,50, gg);
        ff.setBounds(200,200,x0+40,pp.getNewHight()+60);
        ff.add(pp);
        ff.setVisible(true);
        }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
