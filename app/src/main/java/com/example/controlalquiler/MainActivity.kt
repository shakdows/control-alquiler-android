package com.example.controlalquiler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite edge-to-edge (status bar y navigation bar correctas)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Redirige inmediatamente al panel principal
        startActivity(Intent(this, PanelCasasActivity::class.java))
        finish()
    }
}
