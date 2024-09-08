package com.iremsilayildirim.homework1

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

// Glide kütüphanesi ile ilgili yapılandırmaları yapmak için AppGlideModule sınıfını genişletiyoruz.
@GlideModule
class MyAppGlideModule : AppGlideModule() {
    // Gerektiğinde buraya ek yapılandırmalar yapabilirsiniz
}
