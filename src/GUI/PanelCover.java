package GUI;

import Swing.ButtonOutLine;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;


public class PanelCover extends javax.swing.JPanel {

    private final DecimalFormat df = new DecimalFormat("##0.###");
    private ActionListener event;
    private MigLayout layout;
    private JLabel title;
    private JLabel description;
    private JLabel description1;
    private ButtonOutLine Button;
    private boolean isLogin;

    public PanelCover() {
        initComponents();
        setOpaque(false);
        layout = new MigLayout("wrap, fill","[center]","push[]25[]10[]25[]push");
        setLayout(layout);
        init();
    }
    private void init(){
        title = new JLabel("Welcome Back !");
        title.setFont(new Font ("sansserif", 1,30));
        title.setForeground(new Color(245, 245, 245));
        add(title);
        description = new JLabel("Enter your personal details to access all our features");
        description.setForeground(new Color(245, 245, 245));
        description.setFont(new Font("sansserif", 0, 14));
        add(description);
        description1 = new JLabel("Login to continue enjoying our services");
        description1.setForeground(new Color(245, 245, 245));
        description1.setFont(new Font("sansserif", 0, 14));
        add(description1);
        Button = new ButtonOutLine();
        Button.setBackground(new Color(255, 255, 255));
        Button.setForeground(new Color(255, 255, 255));
        Button.setText("Sign In");
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event.actionPerformed(e);
            }
        });
        add(Button, "w 60%, h 40");
        }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gra = new GradientPaint(0, 0, new Color (65, 165, 235) , 0, getHeight(), new Color(65, 165, 235));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g); 
    }

    public void addEvent(ActionListener event){
        this.event = event;
    }
    
    public void registerLeft(double v){
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");    
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");  
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");  
    }
    
    public void registerRight(double v){
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");    
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");  
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");  
    }
    
    public void loginLeft(double v){
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%");   
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%"); 
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%"); 
    }
    
    public void loginRight(double v){
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%"); 
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%"); 
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%"); 
    }
    
    private void login(boolean login){
        if(this.isLogin != login){
        if(login){
            title.setText("Hello, Friend!");
            description.setText("Enter your personal deltils");
            description1.setText("and start journey with us");
            Button.setText("Sign Up");
        } else{
            title.setText("Welcome Back !");
            description.setText("Enter your personal details to access all our features");
            description1.setText("Login to continue enjoying our services");
            Button.setText("Sign In");
        }
        this.isLogin = login;
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
