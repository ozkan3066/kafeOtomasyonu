
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UrunYonetimEkrani extends JFrame {
    private JComboBox<String> kategoriCombo;
    private JTextField urunAdiField;
    private JTextField fiyatField;
    private JTable urunlerTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public UrunYonetimEkrani() {
    	setResizable(false);
        setTitle("Ürün Yönetimi"); 
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        JPanel anaPanel = new JPanel(new BorderLayout(10, 10));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Yeni Ürün Ekle"));
        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Kategori:"), gbc);
        

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        String[] kategoriler = {"KAHVALTI", "SICAK_ICECEK", "SOGUK_ICECEK"};
        kategoriCombo = new JComboBox<>(kategoriler);
        formPanel.add(kategoriCombo, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Ürün Adı:"), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        urunAdiField = new JTextField(20);
        formPanel.add(urunAdiField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Fiyat:"), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        fiyatField = new JTextField(20);
        formPanel.add(fiyatField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        JButton ekleBtn = new JButton("Ürün Ekle");
        ekleBtn.addActionListener(e -> urunEkle());
        formPanel.add(ekleBtn, gbc);

        anaPanel.add(formPanel, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Ürün Adı", "Fiyat", "Kategori"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        urunlerTable = new JTable(tableModel);
        urunlerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(urunlerTable);
        anaPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton silBtn = new JButton("Seçili Ürünü Sil");
        silBtn.addActionListener(e -> urunSil());
        altPanel.add(silBtn);
        anaPanel.add(altPanel, BorderLayout.SOUTH);

        getContentPane().add(anaPanel);
        tabloGuncelle();
    }

    private void tabloGuncelle() {
        tableModel.setRowCount(0); 

        try (Connection conn = VeritabaniBaglantisi.baglantiAl();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu_urunleri ORDER BY kategori, urun_adi")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("urun_adi"),
                    rs.getDouble("fiyat"),
                    rs.getString("kategori")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Veritabanı hatası: " + e.getMessage(), 
                "Hata", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void urunEkle() {
        try {
            String urunAdi = urunAdiField.getText().trim();
            if (urunAdi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ürün adı boş olamaz!");
                return;
            }

            double fiyat;
            try {
                fiyat = Double.parseDouble(fiyatField.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Geçerli bir fiyat giriniz!");
                return;
            }

            String kategori = (String) kategoriCombo.getSelectedItem();

            String sql = "INSERT INTO menu_urunleri (urun_adi, fiyat, kategori) VALUES (?, ?, ?)";
            try (Connection conn = VeritabaniBaglantisi.baglantiAl();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, urunAdi);
                pstmt.setDouble(2, fiyat);
                pstmt.setString(3, kategori);
                
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi!");
                    urunAdiField.setText("");
                    fiyatField.setText("");
                    tabloGuncelle();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + e.getMessage());
        }
    }

    private void urunSil() {
        int selectedRow = urunlerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek ürünü seçin!");
            return;
        }

        int urunId = (int) urunlerTable.getValueAt(selectedRow, 0);
        String urunAdi = (String) urunlerTable.getValueAt(selectedRow, 1);
 
        int secim = JOptionPane.showConfirmDialog(this,
            urunAdi + " ürününü silmek istediğinizden emin misiniz?",
            "Ürün Silme",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (secim == JOptionPane.YES_OPTION) {
            try (Connection conn = VeritabaniBaglantisi.baglantiAl();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM menu_urunleri WHERE id = ?")) {
                
                pstmt.setInt(1, urunId);
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi!");
                    tabloGuncelle();
                } else {
                    JOptionPane.showMessageDialog(this, "Ürün silinirken bir hata oluştu!");
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
            }
        }
    }
}