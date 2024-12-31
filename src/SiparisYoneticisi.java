
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SiparisYoneticisi {
    private static SiparisYoneticisi instance;
    private ArrayList<Urun> kahvaltiUrunleri;
    private ArrayList<Urun> sicakIcecekler;
    private ArrayList<Urun> sogukIcecekler;
    private double toplamTutar;

    private SiparisYoneticisi() {
        kahvaltiUrunleri = new ArrayList<>();
        sicakIcecekler = new ArrayList<>();
        sogukIcecekler = new ArrayList<>();
        toplamTutar = 0.0;
        urunleriYukle();
    }

    public static SiparisYoneticisi getInstance() {
        if (instance == null) {
            instance = new SiparisYoneticisi();
        }
        return instance;
    }

    private void urunleriYukle() {
        try (Connection conn = VeritabaniBaglantisi.baglantiAl()) {
            yukleKategoriUrunleri(conn, "KAHVALTI", kahvaltiUrunleri);
            yukleKategoriUrunleri(conn, "SICAK_ICECEK", sicakIcecekler);
            yukleKategoriUrunleri(conn, "SOGUK_ICECEK", sogukIcecekler);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void yukleKategoriUrunleri(Connection conn, String kategori, ArrayList<Urun> liste) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM menu_urunleri WHERE kategori = ?"
        );
        stmt.setString(1, kategori);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            liste.add(new Urun(
                rs.getInt("id"),
                rs.getString("urun_adi"),
                rs.getDouble("fiyat"),
                rs.getString("kategori")
            ));
        }
    }

    public ArrayList<Urun> getKahvaltiUrunleri() {
        return kahvaltiUrunleri;
    }

    public ArrayList<Urun> getSicakIcecekler() {
        return sicakIcecekler;
    }

    public ArrayList<Urun> getSogukIcecekler() {
        return sogukIcecekler;
    }

    public double getToplamTutar() {
        toplamTutar = 0.0;
        for (ArrayList<Urun> urunler : Arrays.asList(kahvaltiUrunleri, sicakIcecekler, sogukIcecekler)) {
            toplamTutar += urunler.stream()
                .mapToDouble(urun -> urun.getFiyat() * urun.getAdet())
                .sum();
        }
        return toplamTutar;
    }

    public String getSiparisOzeti() {
        StringBuilder ozet = new StringBuilder("Sipariş Özeti:\n\n");
        
        ekleKategoriSiparis(ozet, "Kahvaltılıklar", kahvaltiUrunleri);
        ekleKategoriSiparis(ozet, "Sıcak İçecekler", sicakIcecekler);
        ekleKategoriSiparis(ozet, "Soğuk İçecekler", sogukIcecekler);
        
        ozet.append(String.format("\nToplam Tutar: %.2f TL", getToplamTutar()));
        
        return ozet.toString();
    }

    private void ekleKategoriSiparis(StringBuilder siparis, String kategoriAdi, ArrayList<Urun> urunler) {
        boolean kategoriEklendi = false;
        for (Urun urun : urunler) {
            if (urun.getAdet() > 0) {
                if (!kategoriEklendi) {
                    siparis.append(kategoriAdi).append(":\n");
                    kategoriEklendi = true;
                }
                siparis.append(String.format("  %s x%d: %.2f TL\n", 
                    urun.getAd(), urun.getAdet(), urun.getFiyat() * urun.getAdet()));
            }
        }
        if (kategoriEklendi) {
            siparis.append("\n");
        }
    }

    public void siparisiSifirla() {
        for (ArrayList<Urun> urunler : Arrays.asList(kahvaltiUrunleri, sicakIcecekler, sogukIcecekler)) {
            urunler.forEach(urun -> urun.setAdet(0));
        }
        toplamTutar = 0.0;
    }
}