import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Personel extends Kisi {
    public Kitap kitap;//nesnesi üzerinden gerekli mettlara erişilir
    KutuphaneDatabase kutuphaneDatabase = new KutuphaneDatabase();//veri tabani baglantısı kurulur
    Scanner scanner = new Scanner(System.in);

    //kisi sınıfından extend edildigi icin sıper komutu yardımıyla degiskenlere constructor ile erişilir
    public Personel(String kullaniciAdi, String sifre) {
        super(kullaniciAdi, sifre);
    }

    @Override
    public void girisYap(Kisi kisi) {
        try {
            /*burada "personel.txt" dosyası içinde bulunan personel kullanıcı adı ve
            şifre bilgisi ile personelin girdileri karsilaştirilir ve girişi yapilip
            yapilamayacagı ogrenilir.
             */
            File file = new File("personeller.txt");
            if (!file.exists()) {
                //dosya yoksa oluşturma
                file.createNewFile();
            } else {
                //okuma işlemi için gerekli classlar üzerinden okuma yapma
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                int sayac = 0;
                //dosyanın sonuna gelene kadar okuma işlemi yapılması
                while ((line = bufferedReader.readLine()) != null) {
                    //burada yapılan okuma işlemi ile girdilerin karşılaştirimi yapilir, dogruysa giriş yapilir
                    if (Objects.equals(line, kisi.getKullaniciAdi() + " " + kisi.getSifre())) {
                        sayac++;
                        System.out.println(">>Giris Basarili");
                    }
                }
                if (sayac == 0) {
                    //yanlişsa giriş yapilamaz, hata verir ve sistem kapanir.
                    System.out.println(">>Hatali giris!");
                    System.exit(0);
                }
                //son olarak dosya kapatilir
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
//personelin yeni kitap ekleme yapabildigi yer
    public void kitapEkle() throws IOException {
        kutuphaneDatabase.getConnection();
        kitap = new Kitap();
        System.out.println("Kitap Ekleme Islemi:");
        System.out.println("Eklemek istediginiz kitap adi: ");
        kitap.setIsim(scanner.nextLine());

        ArrayList<String> mevcutKitapBilgisi;
        /*anlik musait kitap bilgisi aşagıdaki metotta veri tabanından sorgu ile ogrenilip
        arrayliste aktarilir boylelikle ayni kitabın eklenmesi durumu engellenir ve
        var olan kitap bilgisi kullanıcı ile gorsel olarak paylaşılabilir
         */
        mevcutKitapBilgisi = kitap.mevcutKitaplar();
        if (mevcutKitapBilgisi.contains(kitap.getIsim())){
            System.out.println("Bu kitap zaten sistemde kayitli, eklenemez!");
        }
        else {
            //aynı adda kitabın olmaması durumunda kitap ekleme işlemi sqlite komutları ile gerceklenir
            String query = "insert into mevcutKitaplar(kitapAdi) VALUES(?)";
            try {
                PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
                //'?' ile boş birakılan alanın doldurulması işlemi
                pst.setString(1, kitap.getIsim());

                pst.executeUpdate();//veri tabanının güncellenmesi
                pst.close();//ve veri tabanının kapatılması
                System.out.println(">>>Kitap ekleme islemi basarili!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//personelin kitap silme işlemini gerceklesigi yer
    public void kitapSil() throws IOException {
        System.out.print("Silmek istediginiz kitap ismini girin: ");
        String isim = scanner.nextLine();

        //KITABI BULMA ISLEMI
        String query = "select * from mevcutKitaplar";//tum mevcut kitaplar sorgulanir,secilir
        try {
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            int sayac = 0;
            while (rs.next()){
                //parametre olarak gelen degerle eşleşmesi durumunda kitabın bulundugu ve boş oldugu anlaşılır
                if (Objects.equals(isim, rs.getString("kitapAdi"))){
                    sayac++;
                    pst.close();
                    rs.close();
                    System.out.println(">>Kitap sistemde kayitli ve bostur");
                }
            }
            if (sayac == 0){
                //parametre olarak gelen degerle eşleşmemesi durumunda kitabın bulunamadığı ya da birinde oldugu anlaşılır
                System.out.println(">>Kitap sistemde kayitli degildir ya da bir kullanicidadir,Silinemez! Tekrar deneyin...");
                kitapSil();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //KITABI SILME ISLEMI
        String query2 = "delete from mevcutKitaplar where kitapAdi = ?";
        try {
            PreparedStatement ps = kutuphaneDatabase.getConnection().prepareStatement(query2);
            ps.setString(1,isim);

            ps.executeUpdate();
            ps.close();

            System.out.println(">>>Kitap basariyla silindi");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void yeniKullaniciEkle(){
        System.out.print("Kullanici isminizi girin: ");
        String isim = scanner.nextLine();
        System.out.print("Sifrenizi girin: ");
        String sifre = scanner.nextLine();
        Kullanici kullanici = new Kullanici(isim,sifre);

        //yeni kullanıcı ekleme işlemi
        String query = "insert into kullanicilar(kullaniciAdi, sifre, kitaplar) VALUES(?,?,?)";
        try {
            //soru işaretli alanlar yeni kullanıcı bilgileri ile doldurulur ve kaydedilir
            PreparedStatement pst = kutuphaneDatabase.getConnection().prepareStatement(query);
            pst.setString(1,kullanici.getKullaniciAdi());
            pst.setString(2,kullanici.getSifre());
            pst.setString(3,"");

            pst.executeUpdate();
            pst.close();
            System.out.println(">>>Kullanici ekleme islemi basarili!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
