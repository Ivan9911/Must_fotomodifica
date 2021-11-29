package com.example.fotoapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File

class DrawSurface(context:Context, attrs:AttributeSet):View(context,attrs) {
    var paint:Paint
    var figures= mutableListOf<Figure>()
    var composedfigures= mutableListOf<StandardFigure>()
    var isWriting=false
    var graphictems= mutableListOf<String>()
    var isinit=false
    var item=0
      var bitmap:Bitmap?
  lateinit  var canvas:Canvas
     var actionF=1
    init {
            paint=Paint().apply {
            setColor(Color.RED)
            strokeWidth=14f
           bitmap=null
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(bitmap==null){

                bitmap= Bitmap.createBitmap(canvas!!.width,canvas!!.height,Bitmap.Config.ARGB_8888)



        }

        canvas?.drawBitmap(bitmap!!,0.0f,0.0f,null)
        this.canvas=canvas!!
        if(figures.size>0){
            for(i in 0..figures.size-1){
                if(figures[i].points.size==1){
                    val point=figures[i].points.get(0)
                    canvas?.drawPoint(point.x.toFloat(),point.y.toFloat(),paint)
                }else{
                    val y=figures[i].points.size-2
                    for(x in 0..y){
                        val point1=figures[i].points[x]
                        val point2=figures[i].points[x+1]
                        canvas?.drawLine(point1.x.toFloat(),point1.y.toFloat(),point2.x.toFloat(),point2.y.toFloat(),paint)
                    }
                }
            }
        }
        if(composedfigures.size!=0){
            for(i in composedfigures){
                val figure=i as Rect
                figure.draw(canvas!!,paint)
            }
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(figures.size!=0||composedfigures.size!=0||isWriting) {
            if (isWriting) {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    figures.add(Figure())
                    graphictems.add("uncomposed")
                } else {
                    if (event?.action == MotionEvent.ACTION_MOVE) {
                        figures[figures.size - 1].points.add(
                            Point(
                                event.x.toInt(),
                                event.y.toInt()
                            )
                        )
                    }
                }
            } else {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    if (composedfigures.size != 0) {
                        var cont = composedfigures.size - 1
                        while (cont >= 0) {
                            if (composedfigures[cont].checkAction(
                                    event.x,
                                    event.y
                                ) != StandardFigure.ACTION_NOTHING
                            ) {
                                item = cont
                                actionF = composedfigures[cont].checkAction(event.x, event.y)
                                break
                            } else {
                                actionF = 1
                            }
                            cont--
                        }
                    }
                } else {
                    println("$item")
                    if (event?.action == MotionEvent.ACTION_MOVE) {
                        if (actionF == StandardFigure.ACTION_MOVE) {

                            composedfigures[item].move(event.x, event.y)
                        } else {
                            if (actionF == StandardFigure.ACTION_RESIZE) {
                                composedfigures[item].resize(event.x, event.y)
                            }
                        }
                    }
                }
            }
            postInvalidate()
        }

        return true
    }
    public fun remove():Boolean{
        if(graphictems.size>=1) {
            val grapich = graphictems[graphictems.size - 1]
            if (grapich.equals("composed")) {
                composedfigures.removeAt(composedfigures.size - 1)
            } else {
                figures.removeAt(figures.size - 1)
            }
            graphictems.removeAt(graphictems.size - 1)
            postInvalidate()
        }
        return true
    }
    fun addRect(){
         composedfigures.add(Rect(this.width,this.height,paint))
        graphictems.add("composed")
        if(isWriting){
            isWriting=false
        }
         postInvalidate()

    }
    fun addCircle(){

    }

    fun write() {
        if(!isWriting){
            isWriting=true
        }else{
            isWriting=false
        }

    }
fun init(){
    isinit=true
    bitmap=null

}
}