package GUI;

import BLL.Brand;
import BLL.Customers;
import BLL.Order;
import BLL.Product;
import BLL.Report;
import Event.EventMenuSelected;
import Model.ModelMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;


public class DashBoard extends javax.swing.JFrame {

    private Menu menu = new Menu();
    private JPanel main = new JPanel();
    private MigLayout layout;
    private Animator animator;
    private boolean menuShow;
    
    public DashBoard() {
        initComponents();
        init();
    }

    private void init(){
        layout = new MigLayout("fill", "0[]10[]0", "0[fill]0");
        DashBoard.setLayout(layout);
        main.setOpaque(false);
        main.setLayout(new BorderLayout());
        
        menu.addEventMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
            }
        });
        menu.setEvent (new EventMenuSelected() {
            @Override
            public void selected(int index) {
        switch (index) {
            case 0:
                showForm(new Order());
                break;
            case 1:
                showForm(new Product());
                break;
            case 2:
                showForm(new Brand());
                break;
            case 3:
                showForm(new Customers());
                break;
            case 4:
                showForm(new Report());
                break;
            default:
                System.out.println("Unhandled menu index");
                break;
        }
    }
        });
        menu.addMenu(new ModelMenu("Order", new ImageIcon(getClass().getResource("/Icon/order23.png"))));
        menu.addMenu(new ModelMenu("Product", new ImageIcon(getClass().getResource("/Icon/product23.png"))));
        menu.addMenu(new ModelMenu("Brand", new ImageIcon(getClass().getResource("/Icon/brand23.png"))));
        menu.addMenu(new ModelMenu("Customers", new ImageIcon(getClass().getResource("/Icon/customers23.png"))));
        menu.addMenu(new ModelMenu("Report", new ImageIcon(getClass().getResource("/Icon/report23.png"))));
        DashBoard.add(menu, "w 50!");
        DashBoard.add(main, "w 100%");   
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menuShow) {
                    width = 50 + (150 * (1f - fraction));
                    menu.setAlpha(1f - fraction);
                } else {
                    width = 50 + (150 * fraction);
                    menu.setAlpha(fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!");
                DashBoard.revalidate();
            }
            
            @Override
            public void end() {
                menuShow = !menuShow;
            }
        };
        animator = new Animator(400, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }
    
    private void showForm(Component component) {
        main.removeAll(); // Xóa tất cả các thành phần hiện có trong body 
        main.add(component); // Thêm component vào body (hoặc DashBoard)
        main.repaint(); // Vẽ lại giao diện body (hoặc DashBoard)
        main.revalidate(); // Cập nhật giao diện body (hoặc DashBoard)
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DashBoard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1088, 570));

        DashBoard.setBackground(new java.awt.Color(245, 245, 245));

        javax.swing.GroupLayout DashBoardLayout = new javax.swing.GroupLayout(DashBoard);
        DashBoard.setLayout(DashBoardLayout);
        DashBoardLayout.setHorizontalGroup(
            DashBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        DashBoardLayout.setVerticalGroup(
            DashBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DashBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DashBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DashBoard;
    // End of variables declaration//GEN-END:variables
}
