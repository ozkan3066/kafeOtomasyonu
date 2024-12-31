import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class GarsonYonetimEkrani extends JFrame {
    private JTextField idField;
    private JTextField sifreField;
    private JTable garsonlarTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public GarsonYonetimEkrani() {
    	setResizable(false);
        setTitle("Garson Yönetimi");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel anaPanel = new JPanel(new BorderLayout(10, 10));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Garson Ekle/Düzenle"));
        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Garson ID:"), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        idField = new JTextField(20);
        formPanel.add(idField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Şifre:"), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        sifreField = new JTextField(20);
        formPanel.add(sifreField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        JButton ekleBtn = new JButton("Garson Ekle");
        ekleBtn.addActionListener(e -> garsonEkle());
        formPanel.add(ekleBtn, gbc);

        anaPanel.add(formPanel, BorderLayout.NORTH);

        String[] kolonlar = {"ID", "Şifre"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        garsonlarTable = new JTable(tableModel);
        garsonlarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(garsonlarTable);
        anaPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton silBtn = new JButton("Seçili Garsonu Sil");
        silBtn.addActionListener(e -> garsonSil());
        altPanel.add(silBtn);
        anaPanel.add(altPanel, BorderLayout.SOUTH);

        getContentPane().add(anaPanel);
        tabloGuncelle();
    }

    private void tabloGuncelle() {
        tableModel.setRowCount(0);

        try (Connection conn = VeritabaniBaglantisi.baglantiAl();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM user ORDER BY id,password")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("password")
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

    private void garsonEkle() {
        try {
            int id;
            try {
                id = Integer.parseInt(idField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Geçerli bir ID giriniz!");
                return;
            }

            String sifre = sifreField.getText().trim();
            if (sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Şifre boş olamaz!");
                return;
            }

            String sql = "INSERT INTO user (id, password) VALUES (?, ?)"; 
            try (Connection conn = VeritabaniBaglantisi.baglantiAl();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.setString(2, sifre);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Garson başarıyla eklendi!");
                    idField.setText("");
                    sifreField.setText("");
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

    private void garsonSil() {
        int selectedRow = garsonlarTable.getSelectedRow();
        if (selectedRow == -1) { 
            JOptionPane.showMessageDialog(this, "Lütfen silinecek garsonu seçin!");
            return;
        }

        int id = (int) garsonlarTable.getValueAt(selectedRow, 0);
        String sifre = (String) garsonlarTable.getValueAt(selectedRow, 1);

        int secim = JOptionPane.showConfirmDialog(this,
            "ID: " + id + " ve Şifre: " + sifre + " garsonunu silmek istediğinizden emin misiniz?",
            "Garson Silme",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (secim == JOptionPane.YES_OPTION) {
            try (Connection conn = VeritabaniBaglantisi.baglantiAl();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user WHERE id = ?")) {

                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Garson başarıyla silindi!");
                    tabloGuncelle();
                } else {
                    JOptionPane.showMessageDialog(this, "Garson silinirken bir hata oluştu!");
                } 

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
            }
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GarsonYonetimEkrani().setVisible(true));
    }
}
