
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeritabaniBaglantisi {
    static final String URL = "jdbc:mysql://localhost:3306/kafe_otomasyonu";
    static final String KULLANICI = "root";
    static final String SIFRE = "12345";

    public static Connection baglantiAl() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC sürücüsü bulunamadı.", e);
        }
        return DriverManager.getConnection(URL, KULLANICI, SIFRE);
    }

    public static void main(String[] args) {
        try {
            Connection conn = baglantiAl();
            System.out.println("Bağlantı başarılı!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}