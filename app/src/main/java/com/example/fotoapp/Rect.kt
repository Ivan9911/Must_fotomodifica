package com.example.fotoapp

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point


class Rect(width:Int,height:Int,paint:Paint) : StandardFigure() {
    var points:MutableList<Point>
    private var spessor:Int
    var axis1="x"
    var axis2="x"
    private lateinit var actual:Point
    var pointstomonitor= mutableListOf<Int>()
    init {
        points= mutableListOf<Point>()
        points.add(Point(300,300))
        points.add(Point(300,600))
        points.add(Point(600,300))
        points.add(Point(600,600))

        spessor=(paint.strokeWidth/2).toInt()
        println(spessor)
    }
    override fun checkAction(x:Float,y:Float): Int {
        var cont=0
        actual= Point(x.toInt(),y.toInt())
        if((x>points[0].x+30)&&(x<points[2].x-30)&&(y>points[0].y+30)&&(y<points[1].y-30)){
            return 10
        }
        for(i in points){
            if(controlPoint(i,x,y)){
                break
            }
            cont++
        }
        if(cont-1==points.size-1){
            pointstomonitor.clear()
            if(controlloRettaVert(points[0],points[1],x,y)){
                pointstomonitor.add(0,0)
                pointstomonitor.add(1,1)
                return 100
            }else{
                if (controlloRettaVert(points[2],points[3],x,y)){
                    pointstomonitor.add(0,2)
                    pointstomonitor.add(1,3)
                    return 100
                }else{
                    if(controlloRettaOriz(points[0],points[2],x,y)){
                        pointstomonitor.add(0,0)
                        pointstomonitor.add(1,2)
                        return 100
                    }else{
                        if(controlloRettaOriz(points[1],points[3],x,y)){
                            pointstomonitor.add(0,1)
                            pointstomonitor.add(1,3)
                            return 100
                        }

                    }
                }
            }
        }else{
            pointstomonitor.clear()
            when(cont){
               0->{
                   pointstomonitor.add(0)
                   pointstomonitor.add(1)
                   pointstomonitor.add(2)
                   axis1="x"
                   axis2="y"
               }
               1->{
                   pointstomonitor.add(1)
                   pointstomonitor.add(0)
                   pointstomonitor.add(3)
                   axis1="x"
                   axis2="y"
               }
               2->{
                   pointstomonitor.add(2)
                   pointstomonitor.add(3)
                   pointstomonitor.add(0)
                   axis1="x"
                   axis2="y"
               }
               3->{
                   pointstomonitor.add(3)
                   pointstomonitor.add(2)
                   pointstomonitor.add(1)
                   axis1="y"
                   axis2="x"
               }
           }
            return 100
        }
        return 1
    }
        override fun move(x: Float, y: Float) {
            var diffx=Math.abs(actual.x-x)
            var diffy=Math.abs((actual.y-y))
            println("differenze $diffx $diffy")
            if(actual.x>x){
                for(i in points){
                    i.x=i.x-diffx.toInt()
                }
            }else{
                for(i in points){
                    i.x=i.x+diffx.toInt()
                }
            }
            if(actual.y>y){
                for(i in points){
                    i.y=i.y-diffy.toInt()
                }
            }else{
                for(i in points){
                    i.y=i.y+diffy.toInt()
                }
            }
            actual.x=x.toInt()
            actual.y=y.toInt()


    }

    override fun resize(x: Float, y: Float) {

        var diffx=Math.abs(actual.x-x)
        var diffy=Math.abs((actual.y-y))
        if(pointstomonitor.size==3){
           if(actual.x>x){
               points[pointstomonitor[0]].x=points[pointstomonitor[0]].x-diffx.toInt()
               points[pointstomonitor[1]].x=points[pointstomonitor[1]].x-diffx.toInt()
           }else{
               points[pointstomonitor[0]].x=points[pointstomonitor[0]].x+diffx.toInt()
               points[pointstomonitor[1]].x=points[pointstomonitor[1]].x+diffx.toInt()
           }
            if (actual.y>y){
                points[pointstomonitor[0]].y=points[pointstomonitor[0]].y-diffy.toInt()
                points[pointstomonitor[2]].y=points[pointstomonitor[2]].y-diffy.toInt()
            }else{
                points[pointstomonitor[0]].y=points[pointstomonitor[0]].y+diffy.toInt()
                points[pointstomonitor[2]].y=points[pointstomonitor[2]].y+diffy.toInt()
            }

        }else{


            if((pointstomonitor[0]==0&&pointstomonitor[1]==1)||(pointstomonitor[0]==2&&pointstomonitor[1]==3)) {
            if (actual.x > x) {
                points[pointstomonitor[0]].x = points[pointstomonitor[0]].x - diffx.toInt()
                points[pointstomonitor[1]].x = points[pointstomonitor[1]].x - diffx.toInt()
            } else {
                points[pointstomonitor[0]].x = points[pointstomonitor[0]].x + diffx.toInt()
                points[pointstomonitor[1]].x = points[pointstomonitor[1]].x + diffx.toInt()
            }
        }else{
            if (actual.y > y) {
                points[pointstomonitor[0]].y = points[pointstomonitor[0]].y - diffy.toInt()
                points[pointstomonitor[1]].y = points[pointstomonitor[1]].y - diffy.toInt()
            } else {
                points[pointstomonitor[0]].y= points[pointstomonitor[0]].y + diffy.toInt()
                points[pointstomonitor[1]].y = points[pointstomonitor[1]].y + diffy.toInt()
            }
        }
        }
        actual.x=x.toInt()
        actual.y=y.toInt()
    }
    fun draw(canvas:Canvas,paint:Paint){
        canvas.drawCircle(points[0].x.toFloat(),points[0].y.toFloat(),paint.strokeWidth/2,paint)
        canvas.drawCircle(points[1].x.toFloat(),points[1].y.toFloat(),paint.strokeWidth/2,paint)
        canvas.drawCircle(points[2].x.toFloat(),points[2].y.toFloat(),paint.strokeWidth/2,paint)
        canvas.drawCircle(points[3].x.toFloat(),points[3].y.toFloat(),paint.strokeWidth/2,paint)
        canvas.drawLine(points[0].x.toFloat(),points[0].y.toFloat(),points[1].x.toFloat(),points[1].y.toFloat(),paint)
        canvas.drawLine(points[0].x.toFloat(),points[0].y.toFloat(),points[2].x.toFloat(),points[2].y.toFloat(),paint)
        canvas.drawLine(points[1].x.toFloat(),points[1].y.toFloat(),points[3].x.toFloat(),points[3].y.toFloat(),paint)
        canvas.drawLine(points[2].x.toFloat(),points[2].y.toFloat(),points[3].x.toFloat(),points[3].y.toFloat(),paint)
    }
    fun controlloRettaVert(pointA:Point,pointB:Point,touchx:Float,touchy:Float):Boolean{
        var rettax1=pointA.x-20
        var rettax2=pointA.x+20
        var rettay1=pointA.y+20
        var rettay2=pointB.y-20
        val pointx1=touchx-20
        val pointx2=touchx+20
        val pointy1=touchy-20
        val pointy2=touchy+20
        var contrv=false
        if((pointy2>rettay1&&pointy2<rettay2)||(pointy1<rettay2&&rettay1<pointy1)){
           contrv =true
            println("y ok")
        }
        var contrh=false
        if((pointx2>rettax1&&pointx2<rettax2)||(pointx1>rettax1&&pointx1<rettax2)){
            println("x ok")
            contrh=true
        }
        if(contrv==true&&contrh==true){
            println("ok")
            return true
        }
        return false

    }
    fun controlloRettaOriz(point1:Point,point2: Point,x:Float,y:Float):Boolean{
        var rettax1=point1.x+20
        var rettax2=point2.x-20
        var rettay1=point1.y-20
        var rettay2=point2.y+20
        val pointx1=x-20
        val pointx2=x+20
        val pointy1=y-20
        val pointy2=y+20
        var contrv=false
        if((pointy2>rettay1&&pointy2<rettay1)||(pointy1<rettay2&&rettay1<pointy1)){
            contrv =true

        }
        var contrh=false
        if((pointx2>rettax1&&pointx2<rettax2)||(pointx1>rettax1&&pointx1<rettax2)){
            contrh=true

        }
        if(contrv==true&&contrh==true){
            return true

        }
        return false


    }

    fun controlPoint(point:Point,x:Float,y:Float):Boolean{
        val pointx1=point.x-20
        val pointx2=point.x+20
        val pointy1=point.y-20
        val pointy2=point.y+20
        val touchpointx1=x-20
        val touchpointx2=x+20
        val touchpointy1=y-20
        val touchpointy2=y+20
        var contrx=false
        println(" $pointx1 $pointx2 $pointy1 $pointy2")
        println("$touchpointx1 $touchpointx2 $touchpointy1 $touchpointy2")
        println("$x $y")
        if((touchpointx2>pointx1&&touchpointx2<pointx2)||(touchpointx1<pointx2&&touchpointx1>pointx1)){

            contrx=true
        }
        var contry=false
        if((touchpointy2>pointy1&&touchpointy2<pointy2)||(touchpointy1<pointy2&&touchpointy1>pointy1)){

            contry=true
        }
        if(contrx&&contry){
            return true
        }
        return false
    }
}