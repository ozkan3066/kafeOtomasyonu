
import javax.swing.*;
import java.awt.*;

public class SicakİceceklerEkrani extends JFrame {
    private SiparisYoneticisi siparisYoneticisi;
    private JLabel toplamLabel;

    public SicakİceceklerEkrani() {
    	setResizable(false);
    	
        setTitle("Sıcak İçecekler Menüsü");
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        siparisYoneticisi = SiparisYoneticisi.getInstance();
        ekranOlustur();
    }

    private void ekranOlustur() {
        JPanel anaPanel = new JPanel(new BorderLayout());
        
        JPanel baslikPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        baslikPanel.setBackground(new Color(192, 192, 192));
        JLabel baslikLabel = new JLabel("Sıcak İçecekler Menüsü");
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 20));
        baslikPanel.add(baslikLabel);
        anaPanel.add(baslikPanel, BorderLayout.NORTH);

        JPanel urunlerPanel = new JPanel();
        urunlerPanel.setBackground(new Color(192, 192, 192));
        urunlerPanel.setLayout(new BoxLayout(urunlerPanel, BoxLayout.Y_AXIS));
        urunlerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Urun urun : siparisYoneticisi.getSicakIcecekler()) {
            JPanel urunPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel urunLabel = new JLabel(urun.toString());
            urunLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JSpinner adetSpinner = new JSpinner(new SpinnerNumberModel(urun.getAdet(), 0, 10, 1));
            adetSpinner.setPreferredSize(new Dimension(60, 25));

            adetSpinner.addChangeListener(e -> {
                urun.setAdet((int) adetSpinner.getValue());
                toplamGuncelle();
            });

            urunPanel.add(urunLabel);
            urunPanel.add(Box.createHorizontalStrut(10));
            urunPanel.add(adetSpinner);
            urunlerPanel.add(urunPanel);
            urunlerPanel.add(Box.createVerticalStrut(5));
        }
        
        

        JPanel altPanel = new JPanel(new BorderLayout());
        
        JPanel butonlarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton kahvaltiBtn = new JButton("Kahvaltı Menüsü");
        JButton sogukIceceklerBtn = new JButton("Soğuk İçecekler");
        JButton siparisButton = new JButton("Siparişi Tamamla");
        JButton geriButton = new JButton("Ana Menüye Dön");

        kahvaltiBtn.addActionListener(e -> {
            new KahvaltiEkrani().setVisible(true);
            dispose();
        });
        
        sogukIceceklerBtn.addActionListener(e -> {
            new SogukİceceklerEkrani().setVisible(true);
            dispose();
        });
        
        siparisButton.addActionListener(e -> siparisiTamamla());
        
        geriButton.addActionListener(e -> {
            new AnaEkran().setVisible(true);
            dispose();
        });

        
        
        butonlarPanel.add(kahvaltiBtn);
        butonlarPanel.add(sogukIceceklerBtn);
        butonlarPanel.add(siparisButton);
        butonlarPanel.add(geriButton);

        JPanel toplamPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toplamLabel = new JLabel(String.format("Toplam: %.2f TL", siparisYoneticisi.getToplamTutar()));
        toplamLabel.setFont(new Font("Arial", Font.BOLD, 16));
        toplamPanel.add(toplamLabel);

        altPanel.add(butonlarPanel, BorderLayout.CENTER);
        altPanel.add(toplamPanel, BorderLayout.SOUTH);

        anaPanel.add(new JScrollPane(urunlerPanel), BorderLayout.CENTER);
        anaPanel.add(altPanel, BorderLayout.SOUTH);

        getContentPane().add(anaPanel);
    }

    
    
    private void toplamGuncelle() {
        toplamLabel.setText(String.format("Toplam: %.2f TL", siparisYoneticisi.getToplamTutar()));
    }

    private void siparisiTamamla() {
        if (siparisYoneticisi.getToplamTutar() > 0) {
            int secim = JOptionPane.showConfirmDialog(
                this,
                siparisYoneticisi.getSiparisOzeti(),
                "Siparişi Onayla",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );

            if (secim == JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(
                    this,
                    "Siparişiniz başarıyla alınmıştır.",
                    "Sipariş Tamamlandı",
                    JOptionPane.INFORMATION_MESSAGE
                );
                siparisYoneticisi.siparisiSifirla();
                getContentPane().removeAll();
                ekranOlustur();
                revalidate();
                repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Lütfen en az bir ürün seçiniz!", 
                "Uyarı", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}