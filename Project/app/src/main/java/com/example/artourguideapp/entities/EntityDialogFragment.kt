package com.example.artourguideapp.entities

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.example.artourguideapp.AppSettings
import com.example.artourguideapp.navigation.Navigation
import com.example.artourguideapp.R
import com.example.artourguideapp.navigation.Tour
import java.io.File

/**
 * This is a custom [DialogFragment] that is used as the parent class of [BuildingDialogFragment] and [LandmarkDialogFragment].
 * Both types of entities share some information so abstracting here is better. This class sets the URL, audio file, navigation
 * button, map button, and the dialog window size.
 *
 * @constructor Construct an entity dialog fragment from a given [Entity]
 *
 * @param entity Entity to create this dialog fragment from
 */
abstract class EntityDialogFragment(var entity: Entity): DialogFragment() {

    private var player: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entityData = entity.getEntityData()

        var audioButton: Button = view.findViewById(R.id.mediaButton)
        var navButton: Button = view.findViewById(R.id.arNavigationButton)
        var mapButton: Button = view.findViewById(R.id.mapButton)

        // Allow additional info to hold website link
        var additionalInfo: TextView = view.findViewById(R.id.url)
        additionalInfo.isClickable = true
        additionalInfo.movementMethod = LinkMovementMethod.getInstance()

        if (entityData.getAudioFileName().isNotEmpty()) {
            context?.let {
                var uri =
                    FileProvider.getUriForFile(it.applicationContext,
                        it.applicationContext.packageName + ".provider",
                        File(it.applicationContext.filesDir, entityData.getAudioFileName())
                    )

                player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    if (uri != null) {
                        setDataSource(it.applicationContext, uri)
                    }
                    prepare()
                }
            }

            audioButton.setOnClickListener {
                if (player!!.isPlaying) {
                    audioButton.text = "Play Audio"
                    player!!.pause()
                } else {
                    audioButton.text = "Pause Audio"
                    player!!.start()
                }
            }

            player?.setOnCompletionListener {
                audioButton.text = "Play Audio"
            }
        } else {
            audioButton.visibility = GONE
        }

        navButton.setOnClickListener {
            if (activity?.localClassName != "ArActivity") {
                activity?.finish()
            }
            if (Tour.onTour) {
                Tour.stopTour()
            }
            Navigation.startNavigationTo(entity)

            dialog?.dismiss()
        }

        mapButton.text = "Map"
        mapButton.setOnClickListener {
            var uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + entity.getLatLng().latitude + "%2C" + entity.getLatLng().longitude)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        // Build html website link in a string
        var hyperlinkText = "<a href='" + entityData.getURL() + "'> Link to website </a>"
        additionalInfo.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        // Set dialog width and height
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * AppSettings.DIALOG_SIZE_PERCENTAGE_OF_SCREEN_WIDTH
        val percentHeight = rect.height() * AppSettings.DIALOG_SIZE_PERCENTAGE_OF_SCREEN_HEIGHT
        dialog?.window?.setLayout(percentWidth.toInt(), percentHeight.toInt())
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        player?.stop()
        player?.release()

        view?.findViewById<ScrollView>(R.id.entity_data_scrollview)?.scrollY = 0
    }
}