package GUI;

import Event.EventMenuSelected;
import Model.ModelMenu;
import Swing.MenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;


public class Menu extends javax.swing.JPanel {

    private MigLayout layout;
    private JPanel panelMenu;
    private JButton cmdMenu;
    private JButton cmdLogout;
    private Header header;
    private EventMenuSelected event;

    public EventMenuSelected getEvent() {
        return event;
    }

    public void setEvent(EventMenuSelected event) {
        this.event = event;
    }
    public Menu() {
        initComponents();
        setOpaque(false);
        init();
    }
    
    private void init(){
        setLayout(new MigLayout("wrap, fillx, insets 0", "[fill]", "5[]0"));
        panelMenu = new JPanel();
        header = new Header();
        createButtonMenu();
        createButtonLogout();
        panelMenu.setOpaque(false);
        layout = new MigLayout("fillx, wrap", "0[fill]0", "0[]3[]00");
        panelMenu.setLayout(layout);
        add(cmdMenu, "pos 1al 0al 100% 50");
        add(cmdLogout, "pos 1al 1al 100% 100, height 60!");
        add(header);
        add(panelMenu);
        
        cmdLogout.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Tạo mới frame đăng nhập
        Main loginFrame = new Main();
        loginFrame.setVisible(true); // Hiển thị frame đăng nhập mới

        // Đóng frame hiện tại (dashboard)
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
        currentFrame.dispose();
    }
});
}
    
    public void addMenu(ModelMenu menu) {
        MenuItem item = new MenuItem(menu.getIcon(), menu.getMenuName(), panelMenu.getComponentCount());
        item.addEvent(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                clearMenu(index);
            }
        });
        item.addEvent(event);
        panelMenu.add(item);
    }

    
    private void createButtonMenu() {
        cmdMenu = new JButton();
        cmdMenu.setContentAreaFilled(false);
        cmdMenu.setFocusPainted(false); 
        cmdMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMenu.setIcon(new ImageIcon(getClass().getResource("/Icon/menu23.png")));
        cmdMenu.setBorder(new EmptyBorder(5, 12, 5, 12));
    }
    
    private void createButtonLogout() {
        cmdLogout = new JButton();
        cmdLogout.setContentAreaFilled(false);
        cmdLogout.setFocusPainted(false);
        cmdLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogout.setIcon(new ImageIcon(getClass().getResource("/Icon/logout25.png")));
        cmdLogout.setBorder(new EmptyBorder(5, 12, 5, 12));
    }
    
    
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        GradientPaint gra = new GradientPaint(0, 0, Color.decode("#56CCF2"), 0, getHeight(), Color.decode("#2F80ED"));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }
    
    private void clearMenu(int exceptIndex) {
        for (Component com : panelMenu.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.getIndex() != exceptIndex) {
                item.setSelected(false);
            }
        }
    }

    
    public void addEventMenu(ActionListener event) {
        cmdMenu.addActionListener(event);
    }
    
    public void setAlpha(float alpha) {
        header.setAlpha(alpha);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
