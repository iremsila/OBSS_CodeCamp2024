# Movie Application
Bu proje, The Movie DB API kullanarak geliştirilmiş bir film uygulamasıdır. Uygulama, popüler filmleri listeleme, film arama, film detayları görüntüleme ve favori filmleri işaretleme gibi özellikler sunmaktadır. Ayrıca, kullanıcı arayüzü modern ve estetik bir tasarıma sahip olup, çeşitli ekran boyutlarına uyumlu şekilde tasarlanmıştır.
## Özellikler
### Popüler Filmler Listesi: Ana ekranda popüler filmler listelenir. Kullanıcılar liste üzerinde aşağı doğru kaydırarak yeni filmleri yükleyebilir (sayfalama).
### Liste Görünümü: Kullanıcılar liste görünümünü tekil veya grid (2'li, 3'lü) olarak değiştirebilirler.
### Film Detayları: Bir film elemanına tıklandığında, film detay sayfasına yönlendirilir. Burada film detay bilgileri API üzerinden çekilir ve gösterilir.
### Film Arama: Ana ekrandaki arama özelliği ile kullanıcılar filmleri anahtar kelime ile arayabilir. Arama sonuçları liste şeklinde görüntülenir.
### Favoriler: Kullanıcılar filmleri favorilere ekleyebilir veya çıkarabilir. Favori filmler ana ekran ve arama sonuçlarında bir ikon ile gösterilir.
### Giriş ve Kayıt: Uygulama, kullanıcıların giriş yapabilmesi ve yeni hesap oluşturabilmesi için bir giriş ve kayıt sayfası içerir.
### Animasyonlar: Uygulama, kullanıcı deneyimini artırmak için arka plan animasyonları ve parallax efektler kullanır.
## Teknolojiler
### Kotlin: Uygulama dili olarak Kotlin kullanılmıştır.
### Android Jetpack: MVVM mimarisi, ViewBinding, DataBinding ve diğer Android Jetpack bileşenleri kullanılmaktadır.
### Retrofit: API istekleri ve veri dönüşümleri için Retrofit kullanılmaktadır.
### Glide ve Coil: Görüntü yükleme ve önbellekleme işlemleri için Glide ve Coil kütüphaneleri kullanılmaktadır.
### Hilt: Dependency Injection için Hilt kullanılmaktadır.
### Room: Yerel veritabanı işlemleri için Room kütüphanesi kullanılmıştır. Favori filmler ve diğer veriler Room veritabanında saklanmaktadır.
### Material Design: UI tasarımı için Material Design bileşenleri ve ikonları kullanılmıştır.
## Kurulum
### Token Alımı: The Movie DB API sitesine üye olun ve API token alın.
### Projeyi Klonlama: Projeyi GitHub üzerinden klonlayın.
bash
Kodu kopyala
git clone https://github.com/yourusername/your-repo.git
### Gradle Senkronizasyonu: Android Studio'da proje dosyasını açın ve Gradle senkronizasyonunu gerçekleştirin.
### API Token: strings.xml dosyasına API token'ınızı ekleyin.
## Kullanım
### Uygulama Başlatma: Android Studio'da projeyi çalıştırın.
### Ana Ekran: Popüler filmleri görüntüleyebilir ve liste görünümünü değiştirebilirsiniz.
### Film Detayı: Bir filme tıklayarak detay sayfasına geçebilirsiniz.
### Arama: Ana ekrandaki arama çubuğunu kullanarak film arayabilirsiniz.
### Favoriler: Filmleri favorilere ekleyebilir veya çıkarabilirsiniz.
### Giriş ve Kayıt: Kullanıcı hesabı oluşturabilir veya mevcut bir hesabınızla giriş yapabilirsiniz.
