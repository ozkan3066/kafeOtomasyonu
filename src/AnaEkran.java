
import javax.swing.*;
import java.awt.*;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnaEkran extends JFrame {
    private JButton kahvaltiBtn;
    private JButton sicakIceceklerBtn;
    private JButton sogukIceceklerBtn;
   

    public AnaEkran() {
    	setResizable(false); 
    	setBackground(new Color(0, 0, 0));
        setTitle("Kafe Otomasyonu");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        bilesenleriolustur();
    }

    private void bilesenleriolustur() {
        JPanel anaPanel = new JPanel();
        anaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        anaPanel.setBackground(new Color(192, 192, 192));

        kahvaltiBtn = new JButton("Kahvaltılıklar");
        kahvaltiBtn.setBounds(145, 71, 250, 65);
        kahvaltiBtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 23));
        sicakIceceklerBtn = new JButton("Sıcak İçecekler"); 
        sicakIceceklerBtn.setBounds(145, 190, 244, 65);
        sicakIceceklerBtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 23));
        sogukIceceklerBtn = new JButton("Soğuk İçecekler");
        sogukIceceklerBtn.setBounds(145, 309, 244, 65);
        sogukIceceklerBtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 23));
      
        for (JButton btn : new JButton[]{kahvaltiBtn, sicakIceceklerBtn, sogukIceceklerBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
        }

        kahvaltiBtn.addActionListener(e -> new KahvaltiEkrani().setVisible(true));
        sicakIceceklerBtn.addActionListener(e -> new SicakİceceklerEkrani().setVisible(true));
        sogukIceceklerBtn.addActionListener(e -> new SogukİceceklerEkrani().setVisible(true));
        
        anaPanel.setLayout(null);

        anaPanel.add(kahvaltiBtn);
        anaPanel.add(sicakIceceklerBtn);
        anaPanel.add(sogukIceceklerBtn);


        getContentPane().add(anaPanel); 
        
        JButton cıkısyapbtn = new JButton("Çıkış ");
        cıkısyapbtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 17)); 
        cıkısyapbtn.setBounds(404, 414, 122, 39);
        cıkısyapbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	LoginGUI loginGUI = new LoginGUI();
            	loginGUI.setVisible(true);
            	dispose();

        }
        });
       
        
        anaPanel.add(cıkısyapbtn);
    }
}