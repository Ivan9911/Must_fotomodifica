package com.example.fotoapp

import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File

class Photo :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo)
        val image=findViewById<ImageView>(R.id.imageview)
        Glide.with(this).load(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"File.jpg")).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).into(image)
    }
}