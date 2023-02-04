package com.example.artourguideapp.entities

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.location.Location
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.example.artourguideapp.navigation.Navigation
import com.example.artourguideapp.R
import com.example.artourguideapp.navigation.Tour
import java.io.File

/**
 * This is a custom Dialog that can be used to display landmark info.
 *
 * To use:
 *      Construct a LandmarkEntity
 *      Get the LandmarkEntity's Dialog Fragment (.getDialogFragment())
 *      call landmarkInfoDialogFragment.show(supportFragmentManager, "custom tag")
 */
class LandmarkDialogFragment(var landmarkData: LandmarkData, var center: Location, var entity: Entity): DialogFragment() {

    private val sizePercentageOfScreen = .95f
    var player: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.landmark_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* GET ALL UI ELEMENTS */
        var name: TextView = view.findViewById(R.id.landmark_data_name)
        var landmarkScrollView : ScrollView = view.findViewById(R.id.landmark_data_scrollview)
        var description: TextView = view.findViewById(R.id.landmark_data_description)
        var audioButton: Button = view.findViewById(R.id.landmarkMediaButton)
        var navButton: Button = view.findViewById(R.id.landmarkArNavigationButton)
        var mapButton: Button = view.findViewById(R.id.landmarkMapButton)

        // TODO: Initialize audio embedding variable here

        // Allow additional info to hold website link
        var url: TextView = view.findViewById(R.id.landmark_data_url)
        url.isClickable = true
        url.movementMethod = LinkMovementMethod.getInstance()


        /* SET ALL UI ELEMENTS */
        name.text = landmarkData.getTitle()
        description.text = landmarkData.getDescription()

        navButton.setOnClickListener {
            if (activity?.localClassName == "SearchActivity") {
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
            var uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + center.latitude + "%2C" + center.longitude)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        if (landmarkData.getAudioFileName().isNotEmpty()) {
            context?.let {
                var uri =
                    FileProvider.getUriForFile(it.applicationContext,
                        it.applicationContext.packageName + ".provider",
                        File(it.applicationContext.filesDir, landmarkData.getAudioFileName())
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
            audioButton.visibility = View.GONE
        }

        // Build html website link in a string
        var hyperlinkText = "<a href='" + landmarkData.getURL() + "'> Link to website </a>"
        url.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        landmarkScrollView.scrollY = 0 // Might not need

        // Set dialog width and height
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * sizePercentageOfScreen
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        player?.stop()
        player?.release()

        // Set ScrollView back to top so opening it again will appear as a fresh view
        view?.findViewById<ScrollView>(R.id.landmark_data_scrollview)?.scrollY = 0
    }
}