import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.awt.event.ActionEvent;
//import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class LoginGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel w_pane;
    private JTextField fld_garsonid;
    private JTextField fld_adminid;
    private JPasswordField fld_garsonpass;
    private JPasswordField fld_adminpass;
    private VeritabaniBaglantisi conn = new VeritabaniBaglantisi(); 

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginGUI frame = new LoginGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginGUI() {
    	setForeground(new Color(0, 0, 0));
        setResizable(false);
        setTitle("Kafe Otomasyonu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        w_pane = new JPanel();
        w_pane.setForeground(new Color(0, 0, 0));
        w_pane.setBackground(new Color(192, 192, 192));
        w_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(w_pane);
        w_pane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Seyir Kafe");
        lblNewLabel.setBounds(77, 65, 333, 26);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        w_pane.add(lblNewLabel);

        JTabbedPane w_tabpane = new JTabbedPane(JTabbedPane.TOP);
        w_tabpane.setForeground(new Color(192, 192, 192));
        w_tabpane.setBounds(11, 141, 466, 301);
        w_pane.add(w_tabpane);
        
        JPanel w_garsongiris = new JPanel();
        w_garsongiris.setBackground(new Color(192, 192, 192));
        w_tabpane.addTab("Garson Girişi", null, w_garsongiris, null);
        w_garsongiris.setLayout(null);
        
        JLabel lblGarsonID = new JLabel("Garson ID :");
        lblGarsonID.setBackground(new Color(192, 192, 192));
        lblGarsonID.setHorizontalAlignment(SwingConstants.LEFT);
        lblGarsonID.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblGarsonID.setBounds(23, 52, 246, 26);
        w_garsongiris.add(lblGarsonID);
        
        JLabel lblifreniz = new JLabel("Garson Şifresi :");
        lblifreniz.setHorizontalAlignment(SwingConstants.LEFT);
        lblifreniz.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblifreniz.setBounds(23, 106, 246, 26);
        w_garsongiris.add(lblifreniz);
        
        fld_garsonid = new JTextField();
        fld_garsonid.setBounds(208, 52, 243, 26);
        w_garsongiris.add(fld_garsonid);
        fld_garsonid.setColumns(10);
        //doğrulama yapmıyor buraya ve admin giriş sorgulamasına bak!!!
        JButton garsongirisbuton = new JButton("Giriş Yap");
        garsongirisbuton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        if (fld_garsonid.getText().length() == 0 || fld_garsonpass.getPassword().length == 0) {
        	Helper.showMsg("fill");
        } else {
        try {
        	Connection con = conn.baglantiAl();
        	Statement st = con.createStatement();
        	ResultSet rs = st.executeQuery("SELECT * FROM user WHERE type = 'garson'");
        	boolean isAuthenticated = false;
        	while (rs.next()) {
        		String dbId = rs.getString("id");
        		String dbPassword = rs.getString("password");
        		String inputId = fld_garsonid.getText();
        		String inputPassword = new String(fld_garsonpass.getPassword());
        	if (inputId.equals(dbId) && inputPassword.equals(dbPassword)) {
        		Garson garson = new Garson();
        		garson.setId(rs.getString("id"));
        		garson.setPassword(rs.getString("password"));
        		garson.setType(rs.getString("type"));

        		AnaEkran kGUI = new AnaEkran();
        		kGUI.setVisible(true);
        		dispose();
        		isAuthenticated = true;
        		break;
        	}
        	}
        	if (!isAuthenticated) {
        		Helper.showMsg("Geçersiz kullanıcı adı veya şifre.");
        	}
        } catch (SQLException e1) {
        	e1.printStackTrace();
        }
        }
        }
        });
        garsongirisbuton.setBackground(new Color(128, 128, 128));
        garsongirisbuton.setFont(new Font("Tahoma", Font.BOLD, 18));
        garsongirisbuton.setBounds(126, 164, 208, 48);
        w_garsongiris.add(garsongirisbuton);
        
            fld_garsonpass = new JPasswordField();
            fld_garsonpass.setBounds(208, 100, 243, 32);
            w_garsongiris.add(fld_garsonpass);

        JPanel w_admingiris = new JPanel();
        w_admingiris.setBackground(new Color(192, 192, 192));
        w_tabpane.addTab("Admin Girişi", null, w_admingiris, null);
        w_admingiris.setLayout(null);

        JLabel lblAdminID = new JLabel("Admin ID :");
        lblAdminID.setHorizontalAlignment(SwingConstants.LEFT);
        lblAdminID.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblAdminID.setBounds(23, 52, 246, 26);
        w_admingiris.add(lblAdminID);

        fld_adminid = new JTextField();
        fld_adminid.setColumns(10);
        fld_adminid.setBounds(208, 52, 243, 26);
        w_admingiris.add(fld_adminid);

        JLabel lblifreniz_1 = new JLabel("Admin Şifresi :");
        lblifreniz_1.setHorizontalAlignment(SwingConstants.LEFT);
        lblifreniz_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblifreniz_1.setBounds(23, 106, 246, 26);
        w_admingiris.add(lblifreniz_1);

        JButton ogretmengirisbuton = new JButton("Giriş Yap");
        ogretmengirisbuton.setForeground(new Color(0, 0, 0));
        ogretmengirisbuton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fld_adminid.getText().length() == 0 || fld_adminpass.getPassword().length == 0) {
                    Helper.showMsg("fill");
                } else {
                    try {
                        Connection con = conn.baglantiAl();
                        Statement st = con.createStatement(); 
                        ResultSet rs = st.executeQuery("SELECT * FROM user WHERE type = 'admin'");
                        boolean isAuthenticated = false;
                        while (rs.next()) {
                            String dbId = rs.getString("id");
                            String dbPassword = rs.getString("password");
                            String inputId = fld_adminid.getText();
                            String inputPassword = new String(fld_adminpass.getPassword());
                            if (inputId.equals(dbId) && inputPassword.equals(dbPassword)) {
                                Admin admin = new Admin();
                                admin.setId(rs.getString("id"));
                                admin.setPassword(rs.getString("password"));
                                admin.setType(rs.getString("type"));

                                adminGUI aGUI = new adminGUI(admin);
                                aGUI.setVisible(true);
                                dispose();
                                isAuthenticated = true;
                                break;
                            }
                        }
                        if (!isAuthenticated) {
                            Helper.showMsg("Geçersiz kullanıcı adı veya şifre.");
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        ogretmengirisbuton.setFont(new Font("Tahoma", Font.BOLD, 18));
        ogretmengirisbuton.setBackground(Color.GRAY);
        ogretmengirisbuton.setBounds(126, 165, 208, 48);
        w_admingiris.add(ogretmengirisbuton);

        fld_adminpass = new JPasswordField();
        fld_adminpass.setBounds(208, 100, 243, 32);
        w_admingiris.add(fld_adminpass);
       // w_pane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel, w_tabpane, w_garsongiris, lblGarsonID, lblifreniz, fld_garsonid, garsongirisbuton, fld_garsonpass, w_admingiris, lblAdminID, fld_adminid, lblifreniz_1, ogretmengirisbuton, fld_adminpass}));
    }
}
