package GUI;

import Model.ModelUser;
import Swing.Button;
import Swing.MyPasswordField;
import Swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;


public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    private ModelUser user;

    public ModelUser getUser() {
        return user;
    }
    

    public PanelLoginAndRegister(ActionListener eventRegister, ActionListener eventLogin) {
        initComponents();
        initRegister(eventRegister);
        initLogin(eventLogin);
        login.setVisible(false);
        register.setVisible(true);
    }
    private void initRegister(ActionListener eventRegister){
        register.setLayout(new MigLayout("wrap","push[center]push","push[]25[]10[]10[]push"));
        JLabel label = new JLabel("Creat Account");
        label.setFont(new Font("sansserif",1,30));
        label.setForeground(new Color(65, 165, 235));
        register.add(label);
        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/Icon/user1.png")));
        txtUser.setHint("Name");
        register.add(txtUser,"w 60%");
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Icon/email.png")));
        txtEmail.setHint("Email");
        register.add(txtEmail,"w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/Icon/pass.png")));
        txtPass.setHint("Password");
        register.add(txtPass,"w 60%");
        Button cmd = new Button();
        cmd.setBackground(new Color(65, 165, 235));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventRegister);
        cmd.setText("Sign Up");
        register.add(cmd, "w 40%, h 40");     
        cmd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtUser.getText().trim();
                String email = txtEmail.getText().trim();
                String password = String.valueOf(txtPass.getPassword());
                user = new ModelUser(0, userName, email, password);
            }          
        });
    }
    private void initLogin(ActionListener eventLogin){
        login.setLayout(new MigLayout("wrap","push[center]push","push[]25[]10[]10[]push"));
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif",1,30));
        label.setForeground(new Color(65, 165, 235));
        login.add(label);
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/Icon/email.png")));
        txtEmail.setHint("Email");
        login.add(txtEmail,"w 60%");
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/Icon/pass.png")));
        txtPass.setHint("Password");
        login.add(txtPass,"w 60%");
        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1,12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setFocusPainted(false); // Disable focus border
        cmdForget.setBorderPainted(false); // Disable border when button is pressed
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);
        Button cmd = new Button();
        cmd.setBackground(new Color(65, 165, 235));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = txtEmail.getText().trim();
            String password = String.valueOf(txtPass.getPassword());
            user = new ModelUser(0, "", email, password);
            // Gọi eventLogin để xử lý đăng nhập
            eventLogin.actionPerformed(e);
        }
    });
        cmd.setText("Sign In");
        login.add(cmd, "w 40%, h 40");      
    }
    
    public void showRegister(boolean show){
        if (show){
            register.setVisible(true);
            login.setVisible(false);
        }else{
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JLayeredPane();
        register = new javax.swing.JLayeredPane();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.CardLayout());

        login.setOpaque(true);

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setOpaque(true);

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane login;
    private javax.swing.JLayeredPane register;
    // End of variables declaration//GEN-END:variables
}
