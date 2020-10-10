package org.unitec.apptextovoz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule


//proyecto texto a voz y vice-versa
//Objetivo indicar a nuestra app que nos de un mensaje de bienvenida personalizado y que las acciones se den a traves de nuestro comando de voz

//Parte 1:
//implementar la interface TextToSpeech que tiene de manera automatica Android para este tipo de funcionalidad
//Una interfaz es un elemento fundamental porque nos ayuda a comunicar dos bloques separados de un proyecto: Nuestra activity con las clases de Texto a Voz.
//En una interfaz el medio que UNE es un metodo(s) o funcion(es) que es lo mismo.
//oninitlistener ya esta listo para usarlos cuando se importa
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //Este objeto es el intermediario entre nuestra app y TextToSpeech
    private var tts:TextToSpeech?=null
    //El siguiente codigo de peticion es un entero, que nos va a ayudar a garantizar el objeto texttospeech
    //se inicio completamente
    private val CODIGO_PETICION=100

    // ejemplo de la linea de arriba pero diferente sintaxis: private val x:int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamos ahora si la variable tts para que ya no este en null
        tts = TextToSpeech(this,this)

        //Invocamos la clase log
        Log.i("XYZ","Se acaba de iniciar el metodo OnCreate")
        Log.i("XYZ","Tu edad en dias es "+tuEdadEnDias(21))
        //El signo de pesos en kotlin se conoce como interpolacion de string
        Log.i("XYZ", "Tu edad en dias es ${tuEdadEnDias(21)} ya sale bien")
        //En kotlin las funciones tambien son variables y su ambito se puede definir solo con llaves
        Log.i("XYZ","La siguiente es otro ejemplo ${4+5} te dara una suma de 9")
        //En kotlin, ademas de ser orientado a objetos: Tambien es funcional
        //Es decir las funciones son tratados como una varible
        var x=2
        //En kotlin una funcion puede ser declarada dentro de otra porque son tratadas como variables
        fun funcioncita()={
            print("Una funcioncita ya con notacion funcional!!");
        }


        //Otro ejemplo con argumentos
        fun otraFuncion(x:Int, y:Int)={
          print(  "Esta funcion hace la suma de los argumentos que le pases ${x+y}")
        }

        Log.i("XYZ", "Mi primer funcion con notacion duncional ${funcioncita()}Listooo!!!!")

        //Se invoca directamente abajo:
        otraFuncion(5,4)

        //Funciones de orden superior y operado lambda


        //Para este ejercicio necesitamos crear una nueva clase
        class Ejemplito:(Int)->Int{
            override  fun invoke(p1: Int): Int{
                TODO("Not yet implemented")
            }
        }


        hablar.setOnClickListener {
            val intent =Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            try{
                startActivityForResult(intent,CODIGO_PETICION)
            }catch (e:Exception){

            }
        }

        //programamos el clicqueo del boton para que interprete lo escrito

        interpretar.setOnClickListener {
            if (FraseEscrita.text.isEmpty()){
                Toast.makeText(this,"Debes escribir algo para que lo hable",Toast.LENGTH_SHORT).show()
            }else{
                //Este metodo ahorita lo vamos a implementar
                hablartexto(FraseEscrita.text.toString())
            }
        }

        //Kemosion!!! VAMOS A ESCUCHAR ESA VOCESITA DE ANDROID, DE BIENVENIDA
        //este es un metodo
        Timer("Bienvenida",false).schedule(1000){
            tts!!.speak(
                    "Hola, Me llamo Jarvis, bienvenido",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
            )
        }
    }

    override fun onInit(estado: Int) {
        //Este metodo o funcion sirve para que se inicialize la configuracion al arrancar la app.(IDIOMA)
        if (estado == TextToSpeech.SUCCESS){
            //si el if se cumplio la ejecucion seguira aqui adentro
            var local = Locale("spa","MEX")
            //La siguiente variable es para que internamente nosotros sepamos que la app va bien
            val resultado = tts!!.setLanguage(local)
            if (resultado == TextToSpeech.LANG_MISSING_DATA){
                Log.i("MALO","NOOOOOOO, NO FUNCIONO EL LENGUAJE ALGO ANDA MAL")
            }
        }
    }

    //Esta funcion es la que nos ayuda interpretar lo que se escriba en el texto de la frase

    fun hablartexto(textoHablar:String){            //queue es para limpar la memoria
        tts!!.speak(textoHablar, TextToSpeech.QUEUE_FLUSH,null,"")
    }

    //Este metodo es opcional pero se los recomiendo para limpiar la memoria de esta app cuando la cierren

    override fun onDestroy() {
        super.onDestroy()
        if (tts!=null){
            //en el caso de las app de espionaje estos dos renglones nunca se apagan
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_PETICION->{
                if (resultCode== RESULT_OK && null !=data){
                    val result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    //Finalmente le vamos a decir a nuestro texto estatico que aqui nos muestre lo
                    //lo que dijimos pero en texto
                    textointerpretado.setText(result!![0])
                }
            }
        }
    }

    //Implementamos un metodo o funcion, que es lo mismo
    fun  saludar( mensaje:String){
        Log.i("Hola","Un mensaje dentro de kotlin")
    }

    fun saludar2(mensaje: String):String{
        return "Mi mensaje de bienvenida"
    }

    fun tuEdadEnDias(edad:Int):Int{
        val diasAnio=365
        return diasAnio*edad
    }
}
