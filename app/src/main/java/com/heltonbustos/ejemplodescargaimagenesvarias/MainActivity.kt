package com.heltonbustos.ejemplodescargaimagenesvarias

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.net.URL

class MainActivity : AppCompatActivity() {

     lateinit var progressBar: ProgressBar
     lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        content = findViewById(R.id.content)
        var btnDescargar: Button = findViewById(R.id.btnDescargar)
        var txtUrl: EditText = findViewById(R.id.txtUrl)

        btnDescargar.setOnClickListener {
            var url1: String = txtUrl.text.toString()

            Descargar(content).execute(
                "https://astelus.com/wp-content/viajes/Lago-Moraine-Parque-Nacional-Banff-Alberta-Canada.jpg",
                "https://www.blogdelfotografo.com/wp-content/uploads/2020/04/fotografo-paisajes.jpg",
                "https://elviajerofeliz.com/wp-content/uploads/2015/09/paisajes-de-Canada.jpg",
                "https://fondosmil.com/fondo/11114.jpg",
                url1
            )
        }
    }

    inner class Descargar(var content: LinearLayout): AsyncTask<String, Int, ArrayList<Bitmap>>(){

        override fun onPreExecute() {
            progressBar.progress = 0
            progressBar.max = 100
            Toast.makeText(applicationContext, "Se inició la descarga", Toast.LENGTH_SHORT).show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): ArrayList<Bitmap> {
            val listaImagenes: ArrayList<Bitmap> = ArrayList()

            for(i in 0.. p0.size-1){
                val url = URL(p0[i])
                val input = url.openStream()
                val bitmap: Bitmap = BitmapFactory.decodeStream(input)
                listaImagenes.add(bitmap)
                //regla de 3
                //4->100%
                //2->50%
                publishProgress(((i+1)*100)/p0.size)
            }
            return listaImagenes
        }

        override fun onProgressUpdate(vararg values: Int?) {
            progressBar.progress = values[0]!!
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: ArrayList<Bitmap>?) {
            content.removeAllViews()

            for(imagen in result!!){
                content.addView(agregarImagen(imagen))
            }

            Toast.makeText(applicationContext, "Finalizó la descarga", Toast.LENGTH_SHORT).show()
            super.onPostExecute(result)
        }
    }

    fun agregarImagen(bitmap: Bitmap): ImageView{
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400)
        params.bottomMargin = 20
        val imageView = ImageView(applicationContext)
        imageView.layoutParams = params

        imageView.run {
            setImageBitmap(bitmap)
            setBackgroundResource(R.color.purple_200)
        }
        return imageView
    }

}