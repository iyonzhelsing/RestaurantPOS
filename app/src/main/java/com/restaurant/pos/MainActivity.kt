package com.restaurant.pos

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var tvTotal: TextView
    private lateinit var tvListPesanan: TextView
    private var totalHarga = 0
    private val listPesanan = StringBuilder()
    
    data class MenuItem(val emoji: String, val nama: String, val harga: Int)
    
    private val daftarMenu = listOf(
        MenuItem("🍛", "Nasi Goreng", 15000),
        MenuItem("🍜", "Mie Goreng", 12000),
        MenuItem("🍗", "Ayam Bakar", 20000),
        MenuItem("🍢", "Sate Ayam", 18000),
        MenuItem("🥩", "Sop Buntut", 25000),
        MenuItem("🧊", "Es Teh", 5000),
        MenuItem("🥑", "Jus Alpukat", 10000),
        MenuItem("☕", "Kopi Hitam", 8000)
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tvTotal = findViewById(R.id.tvTotal)
        tvListPesanan = findViewById(R.id.tvListPesanan)
        
        setupMenuButtons()
        setupActionButtons()
    }
    
    private fun setupMenuButtons() {
        // Layout menu container
        val menuContainer = findViewById<LinearLayout>(R.id.menuContainer)
        
        daftarMenu.forEach { menu ->
            val cardView = createMenuCard(menu)
            menuContainer.addView(cardView)
        }
    }
    
    private fun createMenuCard(menu: MenuItem): View {
        val cardView = layoutInflater.inflate(R.layout.item_menu, null) as LinearLayout
        
        cardView.findViewById<TextView>(R.id.tvEmoji).text = menu.emoji
        cardView.findViewById<TextView>(R.id.tvNamaMenu).text = menu.nama
        cardView.findViewById<TextView>(R.id.tvHargaMenu).text = "Rp ${menu.harga}"
        
        cardView.setOnClickListener {
            tambahPesanan("${menu.emoji} ${menu.nama}", menu.harga)
        }
        
        // Animasi saat ditekan
        cardView.setOnTouchListener { view, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                android.view.MotionEvent.ACTION_UP -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            false
        }
        
        return cardView
    }
    
    private fun setupActionButtons() {
        findViewById<Button>(R.id.btnReset).setOnClickListener { resetPesanan() }
        findViewById<Button>(R.id.btnBayar).setOnClickListener { prosesPembayaran() }
    }
    
    private fun tambahPesanan(nama: String, harga: Int) {
        if (listPesanan.isEmpty() || listPesanan.toString() == "Belum ada pesanan...") {
            listPesanan.clear()
        }
        
        listPesanan.append("• $nama - Rp $harga\n")
        tvListPesanan.text = listPesanan.toString()
        
        totalHarga += harga
        tvTotal.text = "Total: Rp $totalHarga"
        
        Toast.makeText(this, "✓ $nama ditambahkan", Toast.LENGTH_SHORT).show()
    }
    
    private fun resetPesanan() {
        listPesanan.clear()
        listPesanan.append("Belum ada pesanan...")
        tvListPesanan.text = listPesanan.toString()
        totalHarga = 0
        tvTotal.text = "Total: Rp 0"
        Toast.makeText(this, "🔄 Pesanan direset!", Toast.LENGTH_SHORT).show()
    }
    
    private fun prosesPembayaran() {
        if (totalHarga == 0) {
            Toast.makeText(this, "❌ Belum ada pesanan!", Toast.LENGTH_LONG).show()
            return
        }
        
        val struk = """
            ╔══════════════════════════╗
            ║    🍽️ RESTAURANT POS     ║
            ╚══════════════════════════╝
            
            ${listPesanan.toString()}
            ────────────────────────────
            TOTAL: Rp $totalHarga
            ────────────────────────────
               TERIMA KASIH! 😊
            ════════════════════════════
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("💳 PEMBAYARAN BERHASIL!")
            .setMessage(struk)
            .setPositiveButton("OKE") { _, _ -> resetPesanan() }
            .setCancelable(false)
            .show()
    }
}
