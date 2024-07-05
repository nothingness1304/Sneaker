package GUI;

import DAL.DatabaseConnection;
import Model.ModelUser;
import Service.ServiceUser;
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
   private ServiceUser service;
   
    public Main() {
        initComponents();
        init();
    }
    private void init(){
        service = new ServiceUser();
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
        ActionListener eventLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        };
        loginandregister = new PanelLoginAndRegister(eventRegister, eventLogin);
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
        verifyCode.addEventButtonOK(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent ae) {
        String inputCode = verifyCode.getInputCode();
        ModelUser user = loginandregister.getUser();
        boolean isVerified;
        try {
            isVerified = service.verifyUser(user, inputCode);
            if (isVerified) {
                showMessage(Message.MessageType.SUCCESS, "Verification successful! You can now log in.");
                verifyCode.setVisible(false);  // Ẩn panel verify code
            } else {
                showMessage(Message.MessageType.ERROR, "Incorrect verification code. Please try again.");
            }
        } catch (SQLException e) {
            showMessage(Message.MessageType.ERROR, "Error verifying user: " + e.getMessage());
        }
    }
});

    }
    private void register(){
        ModelUser user = loginandregister.getUser();
        loading.setVisible(true);  // Hiển thị panel loading
    new Thread(() -> {
        try {
            if (service.checkDuplicateUser(user.getUserName()) || service.checkDuplicateEmail(user.getEmail())) {
                showMessage(Message.MessageType.ERROR, "Username or Email already exists.");
            } else {
                service.registerUser(user);  // Đăng ký người dùng và gửi mã xác minh
                showMessage(Message.MessageType.SUCCESS, "Please check your email for verification code.");
                // Sau khi đăng ký xong, chuyển đến panel verify code
                loading.setVisible(false);  // Ẩn panel loading
                verifyCode.setVisible(true);
            }
        } catch (SQLException e) {
            showMessage(Message.MessageType.ERROR, "Error registering user: " + e.getMessage());
        } finally {
            loading.setVisible(false);  // Đảm bảo rằng panel loading được ẩn đi
        }
    }).start();
    }
    
    private void login() {
            ModelUser user = loginandregister.getUser();
        if (user == null) {
        showMessage(Message.MessageType.ERROR, "User object is null.");
        return;
    }
    String email = user.getEmail();
    String password = user.getPassword();
    
    if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
        showMessage(Message.MessageType.ERROR, "Email and Password incorrect.");
        return;
    }
    
        loading.setVisible(true);
        new Thread(() -> {
            try {
                if (service.loginUser(email, password)) {
                    showMessage(Message.MessageType.SUCCESS, "Login successful!");
                    loading.setVisible(false);
                    new DashBoard().setVisible(true);
                    this.dispose();
                } else {
                    showMessage(Message.MessageType.ERROR, "Incorrect email or password. Please try again.");
                    loading.setVisible(true);
                }
            } catch (SQLException e) {
                showMessage(Message.MessageType.ERROR, "Error logging in: " + e.getMessage());
            } finally {
                loading.setVisible(false);
            }
        }).start();
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
        bg.setPreferredSize(new java.awt.Dimension(933, 537));

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
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
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
