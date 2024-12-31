import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	static Admin admin = new Admin();
	 private JButton urunYonetimBtn;
	 private JButton garsonDüzenleBtn;
	 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					adminGUI frame = new adminGUI(admin);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		});
	}

	/**
	 * Create the frame.
	 */
	public adminGUI(Admin admin) {
	
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

        urunYonetimBtn = new JButton("Ürün Yönetimi");
        urunYonetimBtn.setBounds(127, 331, 289, 39);
        urunYonetimBtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 23));
        garsonDüzenleBtn = new JButton("Garson Yönetimi"); 
        garsonDüzenleBtn.setBounds(127, 93, 289, 39);
        garsonDüzenleBtn.setFont(new Font("Segoe UI Variable", Font.BOLD, 23));

        for (JButton btn : new JButton[]{urunYonetimBtn,garsonDüzenleBtn}) {
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
        }

        urunYonetimBtn.addActionListener(e -> new UrunYonetimEkrani().setVisible(true));
        garsonDüzenleBtn.addActionListener(e -> new GarsonYonetimEkrani().setVisible(true)); 
        anaPanel.setLayout(null);

      
        anaPanel.add(urunYonetimBtn);
        anaPanel.add(garsonDüzenleBtn);
        
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


        getContentPane().add(anaPanel);
    }
}
