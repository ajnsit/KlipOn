package `in`.functionalprogramming.klipon

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            intent?.action == Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("image/") == true) {
                    handleSendImage(intent)
                    finish()
                }
            } else -> {
                enableEdgeToEdge()
                setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            }
        }
    }

    private inline fun <reified T : Parcelable> getParcelable(intent: Intent, key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") intent.getParcelableExtra(key) as? T
    }

    private fun handleSendImage(intent: Intent) {
        (getParcelable(intent, Intent.EXTRA_STREAM) as? Uri)?.let {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newUri(contentResolver, "", it)
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}