import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Kullanici extends Kisi {
    ArrayList<String> kitaplar = new ArrayList<>();
    ArrayList<String> bostaKitaplar = new ArrayList<>();
    Kitap kitap = new Kitap();
    KutuphaneDatabase kutuphaneDatabase = new KutuphaneDatabase();

    public Kullanici(String kullaniciAdi, String sifre) {
        super(kullaniciAdi, sifre);
    }


    @Override
    public void girisYap(Kisi kisi) {
        kutuphaneDatabase.getConnection();
        String query = "select * from kullanicilar";
        try {
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int sayac = 0;
            while (rs.next()) {
                //girilen bilgilerle veri tabanındaki herhangi bir veri ile eşleşme olup olmadığı sorgusu
                if ((Objects.equals(kisi.getKullaniciAdi(), rs.getString("kullaniciAdi"))) &&
                        (Objects.equals(kisi.getSifre(), rs.getString("sifre")))) {
                    sayac++;
                    System.out.println(">>Giris Basarili");
                    rs.close();
                    pst.close();

                    //Tum kayitli kitaplari tutmak icin
                    String query3 = "select kitaplar from kullanicilar where kullaniciAdi = ? and sifre = ?;";

                    try {
                        PreparedStatement ps = kutuphaneDatabase.getConnection().prepareStatement(query3);
                        ps.setString(1, kisi.getKullaniciAdi());
                        ps.setString(2, kisi.getSifre());
                        ResultSet resultSet = ps.executeQuery();

                        while (resultSet.next()){
                            kitaplar.add(resultSet.getString("kitaplar"));
                        }

                        pst.close();
                        rs.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    bostaKitaplar = kitap.mevcutKitaplar();
                    System.out.println(">>>Kitaplariniz: "+kitaplar);
                    System.out.println(">>>Alabileceginiz Kitaplar: "+bostaKitaplar);
                }
            }
            if (sayac == 0) {
                System.out.println(">>Hatali giris!");
                System.exit(0);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void kitapOduncAlma(Kullanici kullanici) throws IOException {
        System.out.println("Kitap Odunc alma Sayfasi:");
        System.out.println(">>Alabileceginiz Kitaplar: " + bostaKitaplar);
        System.out.print("Almak istediginiz kitabin adini giriniz: ");
        Scanner scanner = new Scanner(System.in);
        String secim = scanner.nextLine();

        if (bostaKitaplar.contains(secim)){
            kitaplar.add(secim);
            bostaKitaplar.remove(secim);
            kitap.dosyadanKitapSilme(secim);//dosyadan kitap silinir
            kitap.dosyadaKisiyeKitapEkleme(kullanici.getKullaniciAdi(),kullanici.getSifre(),secim);//kişinin kitap bilgisi girilir
        }
        else {
            System.out.println("Boyle bir kitap bulunmamaktadir!");
        }

        System.out.println(">>Mevcut Kitaplariniz: " + kitaplar);
        System.out.println(">>Alabileceginiz Kitaplar: " + bostaKitaplar);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void kitapIadeEtme(Kullanici kullanici){
        System.out.println("Kitap Iade etme Sayfasi:");
        System.out.println(">>Mevcut Kitaplariniz: " + kitaplar);
        System.out.print("Iade etmek istediginiz kitabin adini giriniz: ");
        Scanner scanner = new Scanner(System.in);
        String secim = scanner.nextLine();

        if (kitaplar.contains(secim)){
            kitaplar.remove(secim);
            bostaKitaplar.add(secim);
            kitap.dosyayaKitapEkleme(secim);//iade kitap, mevcut kitaplar veri tabanına eklenir
            kitap.dosyadaKisidenKitapSilme(kullanici.getKullaniciAdi(),kullanici.getSifre(),secim);//kullanicidan kitap silinir
        }
        else {
            System.out.println("Boyle bir kitabiniz bulunmamaktadir!");
        }

        System.out.println(">>Mevcut Kitaplariniz: " + kitaplar);
        System.out.println(">>Alabileceginiz Kitaplar: " + bostaKitaplar);
    }


}
