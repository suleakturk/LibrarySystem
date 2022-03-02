import java.io.IOException;
import java.util.Scanner;

public abstract class Kisi {
    /*hem personel hem de kullanici sınıfının ortak degiskenlerini icerdiginden
    bu sınıf personel ve kullanıcı sınıfında extend edilmiştir
     */
    private String kullaniciAdi;
    private String sifre;

    public Kisi(String kullaniciAdi, String sifre) {
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
    }
    /*giris yapma işlevi hem personel hem de kullanıcı sınıfında
    yapılacagından , fakat aynı rotayı takip etmeyeceklerinden bu yapı "abstract"
    olarak tanimlanmıştır.
     */
    public abstract void girisYap(Kisi kisi);

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

/////////////////////////////////////////////TEST SINIFI////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        int sayac = 1;
        while (sayac == 1) {
            Scanner input = new Scanner(System.in);
            System.out.println("Kutuphane Sistemine Hos Geldiniz!");
            System.out.println("1)Kullanici Girisi" + "\n" + "2)Personel Girisi");
            int secim = input.nextInt();
            //giris yolu secilir; personel ya da kullanıcı
            switch (secim) {
                case 1://KULLANICI GIRISI
                    System.out.println("Kullanici Girisine Hos Geldiniz");
                    sayac = 0;

                    System.out.print("Kullanici adinizi giriniz: ");
                    String kullaniciAdi = input.next();
                    System.out.print("Sifrenizi giriniz: ");
                    String sifre = input.next();
                    //constructor üzerinden nesne tanimlama ve metotlara erişme işlemi
                    Kullanici kullanici = new Kullanici(kullaniciAdi,sifre);
                    //Giris yapma islemi gerceklenir
                    kullanici.girisYap(kullanici);

                    //Secim islemi gerceklenir
                    kullaniciSecimIslemi(kullanici);
                    break;

                case 2://PERSONEL GIRISI
                    System.out.println("Personel Girisine Hos Geldiniz");
                    sayac = 0;

                    System.out.print("Kullanici adinizi giriniz: ");
                    String kullaniciAdii = input.next();
                    System.out.print("Sifrenizi giriniz: ");
                    String sifree = input.next();
                    //constructor üzerinden nesne tanimlama ve metotlara erişme işlemi
                    Personel personel = new Personel(kullaniciAdii,sifree);
                    //Giris yapma islemi gerceklenir
                    personel.girisYap(personel);

                    //Secim islemi gerceklenir
                    personelSecimIslemi(personel);
                    break;

                default://HATALI GIRIS
                    //personel ya da kullanıcı girisinin secilmeme durumu
                    System.out.println("Hatali Giris ! Tekrar Deneyiniz...");
                    break;
            }
        }
    }
    /* statik olarak tanimlanan bu metot bu sınıf üzerinde nesneye ihtiyac duymadan
    erişilebilir, kullanıcı girişi secildiği takdirde hangi işlemi gercekleştirmek
    şstediği bilgisi ve yönlendirmeleri burada yapilir
     */
    public static void kullaniciSecimIslemi(Kullanici kullanici) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("!!!Yapmak istediginiz islemin numarasini giriniz," +
                "cikmak icin herhangi bir seye basiniz"+"\n"+"1)Kitap odunc almak"+"\n"+"2)Kitap iade etmek");
        int secim2 = input.nextInt();
        switch (secim2) {
            case 1 -> {
                //kitap odunc alma ekranı
                kullanici.kitapOduncAlma(kullanici);
                kullaniciSecimIslemi(kullanici);
            }
            case 2 -> {
                //kitap iade etme ekrani
                kullanici.kitapIadeEtme(kullanici);
                kullaniciSecimIslemi(kullanici);
            }
            default -> System.out.println("Cikis saglaniyor...");
        }
    }
/*
yine personel girişi doğrulandığı takdirde personelin ne yapmak istediği buradaki
switch-case sorguları yardımıyla bulunur ve yönlendirilir.
 */
    public static void personelSecimIslemi(Personel personel) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("!!!Yapmak istediginiz islemin numarasini giriniz," +
                "cikmak icin herhangi bir seye basiniz"+"\n"+"1)Kitap eklemek"+"\n"+"2)Kitap silmek"+"\n"+"3)Yeni kullanici eklemek");
        int secim = input.nextInt();
        switch (secim) {
            case 1 -> {
                //yeni kitap ekleme ekrani
                personel.kitapEkle();
                personelSecimIslemi(personel);
            }
            case 2 -> {
                //litap silme ekrani
                personel.kitapSil();
                personelSecimIslemi(personel);
            }
            case 3 -> {
                //yeni kullanici ekleme ekrani
                personel.yeniKullaniciEkle();
                personelSecimIslemi(personel);
            }
            default -> System.out.println("Cikis saglaniyor...");
        }
    }
}


