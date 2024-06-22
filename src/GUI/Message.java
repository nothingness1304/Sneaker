
package GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;


public class Message extends javax.swing.JPanel {
    private MessageType messageType= MessageType.SUCCESS;
    private boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    public Message() {
        initComponents();
        setOpaque(false);
        setVisible(false);
    }
    public static enum MessageType{
        SUCCESS,ERROR;
    }
    public void showMessage(MessageType messageType, String message){
        this.messageType=messageType;
        IbMessage.setText(message);
        if(messageType==MessageType.SUCCESS){
            IbMessage.setIcon(new ImageIcon(getClass().getResource("/Icon/success.gif")));
        } else{
            IbMessage.setIcon(new ImageIcon(getClass().getResource("/Icon/error.png")));
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IbMessage = new javax.swing.JLabel();

        IbMessage.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        IbMessage.setForeground(new java.awt.Color(248, 248, 248));
        IbMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        IbMessage.setText("Message");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IbMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(IbMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (messageType == MessageType.SUCCESS){
            g2.setColor(new Color(65, 165, 235));
        } else {
            g2.setColor(new Color(65, 165, 235));
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(245, 245, 245));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);    
        super.paintComponent(g); 
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IbMessage;
    // End of variables declaration//GEN-END:variables
}
