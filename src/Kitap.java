import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Kitap {
    private String isim;
    ArrayList<String> mevcutKitaplar = new ArrayList<>();
    KutuphaneDatabase kutuphaneDatabase = new KutuphaneDatabase();

    public Kitap() {
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    //Musait olan kitap bilgisini db cekerek gosterir
    public ArrayList<String> mevcutKitaplar() throws IOException {
        kutuphaneDatabase.getConnection();
        String query = "select * from mevcutKitaplar";
        try {
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                mevcutKitaplar.add(rs.getString("kitapAdi"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mevcutKitaplar;
    }

    ///////////////////////////////////////////////ODUNC ALMA KISMI/////////////////////////////////////////////////////
    //Odunc kitap alinmasi durumunda dosyadaki musait kitapalardan secili kitap silinir
    public void dosyadanKitapSilme(String kitapAd) throws IOException {
        kutuphaneDatabase.getConnection();
        mevcutKitaplar.remove(kitapAd);
        String query = "select * from mevcutKitaplar";
        try {
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int sayac = 0;
            while (rs.next()) {
                if (Objects.equals(kitapAd, rs.getString("kitapAdi"))) {
                    sayac++;
                    pst.close();
                    rs.close();
                    System.out.println(">>Kitap sistemde tanimlidir");
                }
            }
            if (sayac == 0) {
                System.out.println(">>>Kitap sistemde kayitli degildir ya da bir kullanicidadir,Silinemez! Tekrar deneyin...");
                System.exit(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        String sql = "delete from mevcutKitaplar where kitapAdi = ?";
        try {
            PreparedStatement ps = kutuphaneDatabase.getConnection().prepareStatement(sql);
            ps.setString(1, kitapAd);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Odunc alinan kitabi kisinin kitaplar listesine ekleme islemi yapilir
    public void dosyadaKisiyeKitapEkleme(String kullaniciAdi, String sifre, String secim) throws IOException {
        try {
            String query = "insert into kullanicilar(kullaniciAdi, sifre, kitaplar) VALUES (?,?,?)";
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);

            pst.setString(1, kullaniciAdi);
            pst.setString(2, sifre);
            pst.setString(3, secim);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    ///////////////////////////////////////////////İADE ETME KISMI//////////////////////////////////////////////////////


    public void dosyayaKitapEkleme(String secim) {
        try {
            String query = "insert into mevcutKitaplar(kitapAdi) VALUES (?)";
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);

            pst.setString(1, secim);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dosyadaKisidenKitapSilme(String kullaniciAdi, String sifre, String secim) {
        kutuphaneDatabase.getConnection();
        mevcutKitaplar.add(secim);

        String sql = "delete from kullanicilar where kullaniciAdi = ? and sifre = ? and kitaplar = ?";
        try {
            PreparedStatement ps = kutuphaneDatabase.getConnection().prepareStatement(sql);
            ps.setString(1, kullaniciAdi);
            ps.setString(2, sifre);
            ps.setString(3, secim);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
