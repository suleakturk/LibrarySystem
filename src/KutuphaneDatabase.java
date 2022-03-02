import java.sql.*;

public class KutuphaneDatabase {
    /*Veri tabaniyla baglanti saglanir, bu method bu sinifin
      bir ornek nesnesi uretilerek cagrılıp,kullanilabilir.
     */
    public Connection getConnection() {
        Connection baglanti;
        try {
            baglanti = DriverManager.getConnection("jdbc:sqlite:kutuphane.db");
            return baglanti;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

