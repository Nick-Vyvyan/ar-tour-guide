package com.example.artourguideapp.entities

import android.content.DialogInterface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.example.artourguideapp.R

/**
 * This is a custom Dialog that can be used to display landmark info.
 *
 * To use:
 *      Construct a LandmarkEntity
 *      Get the LandmarkEntity's Dialog Fragment (.getDialogFragment())
 *      call landmarkInfoDialogFragment.show(supportFragmentManager, "custom tag")
 */
class LandmarkDataDialogFragment(var landmarkData: LandmarkData): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.landmark_data_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* GET ALL UI ELEMENTS */
        var name: TextView = view.findViewById(R.id.landmark_data_name)
        var landmarkScrollView : ScrollView = view.findViewById(R.id.landmark_data_scrollview)
        var description: TextView = view.findViewById(R.id.landmark_data_description)

        // TODO: Initialize audio embedding variable here

        // Allow additional info to hold website link
        var url: TextView = view.findViewById(R.id.landmark_data_url)
        url.isClickable = true
        url.movementMethod = LinkMovementMethod.getInstance()


        /* SET ALL UI ELEMENTS */
        name.text = landmarkData.getTitle()
        description.text = landmarkData.getDescription()

        // TODO: Embed audio here

        // Build html website link in a string
        var hyperlinkText = "<a href='" + landmarkData.getURL() + "'> Link to website </a>"
        url.text = HtmlCompat.fromHtml(hyperlinkText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        landmarkScrollView.scrollY = 0 // Might not need
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // Set ScrollView back to top so opening it again will appear as a fresh view
        view?.findViewById<ScrollView>(R.id.landmark_data_scrollview)?.scrollY = 0
    }
}