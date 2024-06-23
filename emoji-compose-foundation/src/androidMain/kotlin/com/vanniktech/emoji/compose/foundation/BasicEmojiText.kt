package com.vanniktech.emoji.compose.foundation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.core.graphics.drawable.toBitmap
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.emojis
import com.vanniktech.emoji.getEmojiDrawable
import com.vanniktech.emoji.isPlatformEmojiProvider

@Composable
fun BasicEmojiText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = TextStyle.Default,
  onTextLayout: ((TextLayoutResult) -> Unit)? = null,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = 1,
  inlineContent: Map<String, InlineTextContent> = mapOf(),
  color: ColorProducer? = null,
) {
  BasicEmojiText(
    text = AnnotatedString(text),
    modifier = modifier,
    style = style,
    onTextLayout = onTextLayout,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    minLines = minLines,
    inlineContent = inlineContent,
    color = color,
  )
}

@Composable
fun BasicEmojiText(
  text: AnnotatedString,
  modifier: Modifier = Modifier,
  style: TextStyle = TextStyle.Default,
  onTextLayout: ((TextLayoutResult) -> Unit)? = null,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = 1,
  inlineContent: Map<String, InlineTextContent> = mapOf(),
  color: ColorProducer? = null,
) {
  val context = LocalContext.current
  val isPreview = LocalInspectionMode.current
  val emojis = remember(text) {
    if (!isPreview && !EmojiManager.isPlatformEmojiProvider()) {
      text.emojis()
    } else {
      null
    }
  }
  val combinedInlineContent = remember(inlineContent, emojis, context) {
    emojis?.let {
      buildMap {
        emojis.fastForEach { emojiRange ->
          put(
            emojiRange.emoji.unicode,
            InlineTextContent(
              Placeholder(
                width = 1.em,
                height = 1.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
              ),
            ) {
              Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = EmojiManager.getEmojiDrawable(emojiRange.emoji, context).toBitmap()
                  .asImageBitmap(),
                // Content description is inherited from inline content
                contentDescription = null,
              )
            },
          )
        }
      }
    }?.plus(inlineContent) ?: inlineContent
  }

  BasicText(
    text = if (emojis != null) {
      remember(emojis, text) {
        buildAnnotatedString {
          var textPosition = 0
          emojis.fastMap { emojiRange ->
            append(text.subSequence(textPosition, emojiRange.range.first))
            appendInlineContent(emojiRange.emoji.unicode, alternateText = emojiRange.emoji.unicode)
            textPosition = emojiRange.range.last
          }
        }
      }
    } else {
      text
    },
    modifier = modifier,
    style = style,
    onTextLayout = onTextLayout,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines,
    minLines = minLines,
    inlineContent = combinedInlineContent,
    color = color,
  )
}
