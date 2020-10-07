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

}
