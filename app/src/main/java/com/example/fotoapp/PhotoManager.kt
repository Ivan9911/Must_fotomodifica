package com.example.fotoapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PhotoManager :AppCompatActivity() {
    lateinit var editorView:DrawSurface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editimage)
        editorView=findViewById<DrawSurface>(R.id.editorview)
        val buttonrect=findViewById<Button>(R.id.addrect)
        buttonrect.setOnClickListener(View.OnClickListener {
            editorView.addRect()
        })
        val button=findViewById<ImageButton>(R.id.cancel)
        button.setOnClickListener(View.OnClickListener {
            editorView.remove()
        })
        val buttonPencil=findViewById<Button>(R.id.pencil)
        buttonPencil.setOnClickListener(View.OnClickListener {
            editorView.write()
        })
    }
}