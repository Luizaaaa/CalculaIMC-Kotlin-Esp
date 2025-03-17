package com.pos.moveis.calculaimc_kotlin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var etPeso : EditText
    private lateinit var etAltura : EditText
    private lateinit var tvResult : TextView
    private lateinit var btCalcular : Button
    private lateinit var btLimpar : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPeso = findViewById(R.id.etPeso)
        etAltura = findViewById(R.id.etAltura)
        tvResult = findViewById(R.id.tvResult)
        btCalcular = findViewById(R.id.btCalcular)
        btLimpar = findViewById(R.id.btLimpar)

        btCalcular.setOnClickListener {
            btCalcularOnClick()
            classifyResult(tvResult)
        }

        btLimpar.setOnClickListener {
            btLimparOnClick()
        }

        //btLimpar.set

    }

    private fun btLimparOnClick() {
        etPeso.setText("")
        etAltura.setText("")
        tvResult.text = getString(R.string.zeros)
        etPeso.requestFocus()
    }



    private fun btCalcularOnClick() {

        if(etAltura.text.toString().isEmpty()){
            etAltura.error = R.string.error_alt.toString()
            return
        }
        if(etPeso.text.toString().isEmpty()){
            etPeso.error = R.string.error_peso.toString()
            return
        }

        val peso:Double = etPeso.text.toString().toDouble()
        val altura:Double = etAltura.text.toString().toDouble()
        var imc: Double
        if (Locale.getDefault().language.equals("en")){
            imc = calculaIMC(peso,altura,Locale.getDefault().language)
            val nf:NumberFormat = NumberFormat.getNumberInstance(Locale.US)
            val df:DecimalFormat = nf as DecimalFormat
            tvResult.text = df.format(imc)
        }
        else{
            imc = calculaIMC(peso,altura,Locale.getDefault().language)
            val df = DecimalFormat("0.0")
            tvResult.text = df.format(imc)
        }
    }

    private fun classifyResult(result: TextView){
        val builder = AlertDialog.Builder(this)

        var msg = ""
        val texto = result.text.toString().replace(",",".")

        val valor: Double? = texto.toDoubleOrNull()

        if (valor != null) {
            if (valor < 18.5) msg = "Seu IMC está baixo, você está abaixo do peso ideal"
            else if (valor in 18.5..24.9) msg = "Seu IMC está normal! :)"
            else if (valor in 25.0..29.9) msg = "Você está com o IMC um pouco elevado, está com excesso de peso. Procure um médico!"
            else if (valor > 30 && valor < 35) msg = "Você está com o IMC elevado, está obeso! Procure um médico!"
            else msg = "Você está com o IMC extremamente elevado, está com obesidade extrema!! Procure um médico!"
        }

        builder.setMessage(msg)
            .setPositiveButton("Saiba mais!") { dialog, id ->
                val intent = Intent(this, Details::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Fechar") { dialog, id ->
                fun onClick(dialog: DialogInterface, which: Int) {
                    Toast.makeText(this, "Ação cancelada!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

        builder.create()
        builder.show()
    }

    companion object {

        fun calculaIMC(peso:Double, altura:Double, locale:String):Double{
            if (locale.equals("en")){
                return 703 * (peso/altura.pow(2))
            }
            else{
                return peso/altura.pow(2)
            }
        }
    }
}//end MainAct
