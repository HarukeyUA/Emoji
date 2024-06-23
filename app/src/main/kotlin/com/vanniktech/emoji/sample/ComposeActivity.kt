package com.vanniktech.emoji.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.vanniktech.emoji.compose.foundation.BasicEmojiText
import com.vanniktech.emoji.sample.databinding.ActivityComposeBinding
import androidx.compose.material3.MaterialTheme as Material3Theme
import com.vanniktech.emoji.compose.material.EmojiText as MaterialEmojiText
import com.vanniktech.emoji.compose.material3.EmojiText as Material3EmojiText

class ComposeActivity : AppCompatActivity() {
  private lateinit var binding: ActivityComposeBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityComposeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

    binding.composeView.setContent {
      Column(modifier = Modifier.fillMaxSize()) {
        BasicEmojiText(
          modifier = Modifier.fillMaxWidth(),
          text = "Emoji text component running on Jetpack Compose \uD83D\uDE18\uD83D\uDE02\uD83E\uDD8C",
        )
        MaterialTheme {
          MaterialEmojiText(
            modifier = Modifier.fillMaxWidth(),
            text = "Material emoji text component running on Jetpack Compose \uD83D\uDE18\uD83D\uDE02\uD83E\uDD8C",
          )
        }
        Material3Theme {
          Material3EmojiText(
            modifier = Modifier.fillMaxWidth(),
            text = "Material 3 emoji text component running on Jetpack Compose \uD83D\uDE18\uD83D\uDE02\uD83E\uDD8C",
          )
        }
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    android.R.id.home -> {
      finish()
      true
    }

    else -> super.onOptionsItemSelected(item)
  }
}
