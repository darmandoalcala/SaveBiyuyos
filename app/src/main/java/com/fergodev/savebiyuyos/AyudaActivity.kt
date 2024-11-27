package com.fergodev.savebiyuyos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AyudaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayuda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val soporteButtonOnPress: Button = findViewById(R.id.btnSoporte)
        soporteButtonOnPress.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@Biyuyos.com"))
                putExtra(Intent.EXTRA_SUBJECT, "SOPORTE")
                putExtra(Intent.EXTRA_TEXT, "Necesito ayuda con mi app, presente el siguiente problema:")
            }
            startActivity(emailIntent)
        }

        val faqsButtonOnPress: Button = findViewById(R.id.btnPreguntasFrecuentes)
        faqsButtonOnPress.setOnClickListener {
            val url = "http://www.savebiyuyos.com/faqs/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}