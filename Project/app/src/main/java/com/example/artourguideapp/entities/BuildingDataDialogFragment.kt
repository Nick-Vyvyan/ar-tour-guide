package com.example.artourguideapp.entities

import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Rect
import android.content.Intent
import android.location.Location
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.example.artourguideapp.R
import java.io.File

/**
 * This is a custom Dialog that can be used to display building info.
 *
 * To use:
 *      Construct a BuildingEntity
 *      Get the BuildingEntity's Dialog Fragment (.getDialogFragment())
 *      call buildingInfoDialogFragment.show(supportFragmentManager, "custom tag")
 */
class BuildingDataDialogFragment(var buildingData: BuildingData, var center: Location): DialogFragment() {

    private val sizePercentageOfScreen = .95f
    var player: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.building_data_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* GET ALL UI ELEMENTS */
        var nameAndCode: TextView = view.findViewById(R.id.landmark_data_name)
        var buildingScrollView : ScrollView = view.findViewById(R.id.landmark_data_scrollview)
        var types: TextView = view.findViewById(R.id.landmark_data_description)
        var departments: TextView = view.findViewById(R.id.buildingDepartments)
        var accessibilityLayout: LinearLayout = view.findViewById(R.id.accessibilityLayout)
        var genderNeutralRestrooms: TextView = view.findViewById(R.id.genderNeutralRestrooms)
        var computerLabs: TextView = view.findViewById(R.id.computerLabs)
        var audioButton: Button = view.findViewById(R.id.buildingMediaButton)
        var navButton: Button = view.findViewById(R.id.buildingNavigationButton)

        // Allow links in parking info
        var parkingInfo: TextView = view.findViewById(R.id.parkingInfo)
        parkingInfo.isClickable = true
        parkingInfo.movementMethod = LinkMovementMethod.getInstance()

        var dining: TextView = view.findViewById(R.id.dining)

        // Allow additional info to hold website link
        var additionalInfo: TextView = view.findViewById(R.id.landmark_data_url)
        additionalInfo.isClickable = true
        additionalInfo.movementMethod = LinkMovementMethod.getInstance()

        /* SET ALL UI ELEMENTS */
        nameAndCode.text = buildingData.getTitle() + " (" + buildingData.getCode() + ")"
        types.text = buildingData.getTypes()
        departments.text = buildingData.getDepartments()

        //accessibilityLayout.removeAllViews()
        var accessibilityInfo: String = buildingData.getAccessibilityInfo()

        // CURRENT METHOD OF PARSING ACCESSIBILITY INFO. MAY CHANGE
        if (accessibilityInfo != "") {
            var accessibilitySections = accessibilityInfo.split("\n")
            for (section in accessibilitySections) {
                var newView = TextView(activity)
                newView.textSize = 18f
                newView.text = "-    $section"
                accessibilityLayout.addView(newView)
            }
        }

        if (buildingData.getAudioFileName().isNotEmpty()) {
            context?.let {
                var uri =
                    FileProvider.getUriForFile(it.applicationContext,
                        it.applicationContext.packageName + ".provider",
                        File(it.applicationContext.filesDir, buildingData.getAudioFileName())
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
            // Log.d("DEBUG","Made to has audio")
        } else {
            audioButton.visibility = GONE
        }

        navButton.setOnClickListener {
            var uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + center.latitude + "%2C" + center.longitude)
            Log.d("DEBUG", "" + center.latitude + " " + center.longitude)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        genderNeutralRestrooms.text = buildingData.getGenderNeutralRestrooms()
        computerLabs.text = buildingData.getComputerLabs()
        dining.text = buildingData.getDining()

        // Build html website link in a string
        var hyperlinkText = "<a href='" + buildingData.getURL() + "'> Link to website </a>"
        additionalInfo.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        buildingScrollView.scrollY = 0

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