package com.example.fotoapp

import android.content.Intent
import android.graphics.*
import android.graphics.Rect
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(){

    lateinit var currentPhotoPath:String
    lateinit var editorView:DrawSurface

    @RequiresApi(Build.VERSION_CODES.Q)
    var resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode== RESULT_OK){
            setBitmap()
            Result()
        }else{
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container=findViewById<ConstraintLayout>(R.id.parent)
        container.alpha=0f

        createImageFile()
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID+".Provider",File(currentPhotoPath)))
        resultLauncher.launch(intent)
    }
    private fun createImageFile() {
        val file=File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"File.jpg")
        if(!file.exists()){
            file.createNewFile()
        }
        currentPhotoPath=file.path
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun Result() {
        val parent=findViewById<ConstraintLayout>(R.id.parent)

        editorView=findViewById<DrawSurface>(R.id.editorview)

        val imagev=findViewById<ImageView>(R.id.image_preview)
        val exif=ExifInterface(currentPhotoPath)
        val height=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,ExifInterface.ORIENTATION_NORMAL)
        val width=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,ExifInterface.ORIENTATION_NORMAL)
        if(height<width){
            val set=ConstraintSet()
            set.clone(parent)
            set.setDimensionRatio(imagev.id,"4:3")
            set.applyTo(parent)
        }
        Glide.with(this).load(File(currentPhotoPath)).fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).into(imagev)
        parent.alpha=1.0f

        val buttonrect=findViewById<Button>(R.id.addrect)
        buttonrect.setOnClickListener(View.OnClickListener {
            editorView.addRect()
        })
        val button=findViewById<Button>(R.id.cancel)
        button.setOnClickListener(View.OnClickListener {
            editorView.remove()
        })
        val buttonPencil=findViewById<Button>(R.id.pencil)
        buttonPencil.setOnClickListener(View.OnClickListener {
            editorView.write()
        })
        val exit=findViewById<Button>(R.id.exit)
        exit.setOnClickListener(View.OnClickListener {
                v->
            finish()
        })
        val save=findViewById<Button>(R.id.save)
        save.setOnClickListener(View.OnClickListener {
            val file=File(currentPhotoPath)
            val matrix=Matrix()
            val image=BitmapFactory.decodeFile(file.path)
            var bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
            val canvas=Canvas(bitmap)
            canvas.drawBitmap(image,null,Rect(0,0,width,height),null)
            editorView.isDrawingCacheEnabled=true
            editorView.buildDrawingCache()
            val written=editorView.getDrawingCache()
            canvas.drawBitmap(written,null, Rect(0,0,canvas.width,canvas.height),null)
            val out=FileOutputStream(File(currentPhotoPath))
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        })
    }
    fun getDegrees():Int {
        val exif = ExifInterface(currentPhotoPath)
        var rotate = 0
        if (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) == ExifInterface.ORIENTATION_ROTATE_270
        ) {
            rotate = -270
            Toast.makeText(this, "270", Toast.LENGTH_LONG).show()
        } else {
            if (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                ) == ExifInterface.ORIENTATION_ROTATE_180
            ) {
                Toast.makeText(this, "180", Toast.LENGTH_LONG).show()
                rotate = -180
            } else {
                if (exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    ) == ExifInterface.ORIENTATION_ROTATE_90
                ) {
                    Toast.makeText(this, "90", Toast.LENGTH_LONG).show()
                    rotate = -90
                }
            }

        }
        return rotate
    }


    override fun onDestroy() {
        super.onDestroy()

    }
    private fun setBitmap() {
        val file=File(currentPhotoPath)
        val matrix=Matrix()
        val image=BitmapFactory.decodeFile(file.path)
        val exif=ExifInterface(currentPhotoPath)
        val height=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,ExifInterface.ORIENTATION_NORMAL)
        val width=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,ExifInterface.ORIENTATION_NORMAL)
        val bitmap:Bitmap
        if (getDegrees()==-180||getDegrees()==0){
            bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        }else{
            bitmap= Bitmap.createBitmap(height,width,Bitmap.Config.ARGB_8888)
        }
        val canvas=Canvas(bitmap)
        matrix.postRotate(-getDegrees().toFloat(),(width/2).toFloat(),(height/2).toFloat())
        matrix.postTranslate( (canvas.getWidth() / 2 - width / 2).toFloat(),
            (canvas.getHeight() / 2 - height / 2).toFloat())
        canvas.drawBitmap(image,matrix,null)
        val out=FileOutputStream(File(currentPhotoPath))
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        exif.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL.toString())
        exif.saveAttributes()
    }
}