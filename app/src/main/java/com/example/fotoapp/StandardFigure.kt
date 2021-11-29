package com.example.fotoapp

 abstract class  StandardFigure {
     companion object{
         var ACTION_MOVE=10
     var ACTION_RESIZE=100
     var ACTION_NOTHING=1
     }
     abstract   fun checkAction(x:Float,y:Float):Int
     abstract   fun move(x:Float,y:Float)
     abstract   fun resize(x:Float,y:Float)

}