package mx.edu.ittepic.ladm_u4_llamadas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val siPermiso = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG,android.Manifest.permission.READ_CONTACTS),siPermiso)


        }else{
            leerLlamadas()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==siPermiso){
            leerLlamadas()
        }
    }

    private fun leerLlamadas() {
        var resultado = ""
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siPermiso)


        }
        var cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,null)
        if(cursor!!.moveToFirst()){
            var posColumnaNumero = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            var posColumnaTipo = cursor.getColumnIndex(CallLog.Calls.TYPE)
            var posColumnaFecha = cursor.getColumnIndex(CallLog.Calls.DATE)
            var tipo = cursor.getString(posColumnaTipo).toInt()
            var tipoLlamada =""

            do{
                when(tipo){
                    CallLog.Calls.OUTGOING_TYPE-> tipoLlamada = "Llamada Saliente"
                    CallLog.Calls.INCOMING_TYPE -> tipoLlamada ="Llamada Entrante"
                    CallLog.Calls.MISSED_TYPE -> tipoLlamada = "LLamada Perdida"
                }
                val fechaLlamada = cursor.getString(posColumnaFecha)
                resultado += "NUMERO: "+cursor.getString(posColumnaNumero)+
                        "\nTIPO DE LLAMADA: "+tipoLlamada+"\nFECHA DE LLAMADA: "+Date(fechaLlamada.toLong())+"\n-------------------------------------------------\n"
            }while (cursor.moveToNext())
        }else{
            resultado = "NO HAY LLAMADAS"
        }
        textView.setText(resultado)
    }
}
