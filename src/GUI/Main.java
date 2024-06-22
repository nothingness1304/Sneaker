package GUI;

import DAL.DatabaseConnection;
import Model.ModelUser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.JLayeredPane;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;


public class Main extends javax.swing.JFrame {

   private MigLayout layout;
   private PanelCover cover;
   private PanelLoading loading;
   private PanelVerifyCode verifyCode;
   private PanelLoginAndRegister loginandregister;
   private boolean isLogin;
   private final double addSize=30;
   private final double coverSize=40;
   private final double loginSize=60;
   private final DecimalFormat df = new DecimalFormat("##0.###");
    public Main() {
        initComponents();
        init();
    }
    private void init(){
        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        loading = new PanelLoading();
        verifyCode = new PanelVerifyCode();
        ActionListener eventRegister = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                register ();
            }        
        };
        loginandregister = new PanelLoginAndRegister(eventRegister);
        TimingTarget target = new TimingTargetAdapter(){
            @Override
            public void timingEvent(float fraction) {
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                if(fraction<=0.5f){
                    size += fraction * addSize;
                } else{
                    size += addSize - fraction * addSize;
                }
                if(isLogin){
                    fractionCover = 1f - fraction;
                    fractionLogin = fraction;
                    if(fraction>=0.5f){
                        cover.registerRight(fractionCover * 100);
                    } else{
                        cover.loginRight(fractionLogin * 100);
                    }
                } else{
                    fractionCover = fraction;
                    fractionLogin = 1f - fraction;
                    if(fraction<=0.5f){
                        cover.registerLeft(fraction * 100);
                    } else{
                        cover.loginLeft((1f - fraction) * 100);
                    }
                }
                if(fraction>=0.5f){
                    loginandregister.showRegister(isLogin);
                }
                fractionCover=Double.valueOf(df.format(fractionCover));
                fractionLogin=Double.valueOf(df.format(fractionLogin));
                layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover +"al 0 n 100%");
                layout.setComponentConstraints(loginandregister, "width " + loginSize + "%, pos " + fractionLogin +"al 0 n 100%");
                bg.revalidate();
            }

            @Override
            public void end() {
                isLogin=!isLogin;
            }
                    
        };
        Animator animator = new Animator(800, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);
        bg.setLayout(layout);
        bg.setLayer(loading, JLayeredPane.POPUP_LAYER);
        bg.setLayer(verifyCode, JLayeredPane.POPUP_LAYER);
        bg.add(loading, "pos 0 0 100% 100%");
        bg.add(verifyCode, "pos 0 0 100% 100%");
        bg.add(cover,"width " + coverSize +"%, pos 0al 0 n 100%");
        bg.add(loginandregister, "width " + loginSize +"%, pos 1al 0 n 100%");
        cover.addEvent(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               if(!animator.isRunning()){
                   animator.start();
               }
            }
            
        });
    }
    private void register(){
        ModelUser user = loginandregister.getUser();
        //loading.setVisible(true);
        showMessage(Message.MessageType.SUCCESS, "Test Message");
    }
    
    private void showMessage(Message.MessageType messageType, String message){
        Message ms = new Message();
        ms.showMessage(messageType, message);
        TimingTarget target = new TimingTargetAdapter(){
            @Override
            public void begin() {
                if( !ms.isShow()){
                    bg.add(ms, "pos 0.5al -30", 0); // index to bg fist index 0
                    ms.setVisible(true);
                    bg.repaint();
                }
            }

            @Override
            public void timingEvent(float fraction) {
               float f;
               if (ms.isShow()){
                   f = 40 * (1f - fraction);
               } else {
                   f = 40 * fraction;
               }
               layout.setComponentConstraints(ms, "pos 0.5al " + (int) (f - 30));
               bg.repaint();
               bg.revalidate();
            }
            
            @Override
            public void end() {
                if(ms.isShow()){
                    bg.remove(ms);
                    bg.repaint();
                    bg.revalidate();
                } else {
                    ms.setShow(true);
                }
            }        
        };
        Animator animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.start();
        new Thread(new Runnable(){
            @Override
            public void run() {
            try {
                    Thread.sleep(3000);
                    animator.start();              
            } catch (Exception e) {
                System.err.println(e);
            }
        }                    
        }).start();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(933, 537));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        try {
            DatabaseConnection.getInstance().connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}
