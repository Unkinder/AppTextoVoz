package org.unitec.apptextovoz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
        //Kemosion!!! VAMOS A ESCUCHAR ESA VOCESITA DE ANDROID, DE BIENVENIDA
        //este es un metodo
        Timer("Bienvenida",false).schedule(1000){
            tts!!.speak(
                    "Hola, Me llamo Jarvis, porque no te vas y chingas a tu madre puto de mierda",
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
}
