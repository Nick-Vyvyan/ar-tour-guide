package com.example.artourguideapp.navigation

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.Model
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** This class allows a Tour option to be added to the main AR activity.
 *  The tour has been hard-coded according to specifications by our client, Diana Feinson
 */
class Tour {

    companion object {
        var onTour = false
        private lateinit var activity: AppCompatActivity

        private var destinations: MutableList<Entity> = mutableListOf()
        private var destinationIndex = 0

        private var destinationRoute = mutableListOf<String>(
            "Wade King Recreation Center",
            "Academic Instructional West",
            "Communications Facility",
            "Environmental Studies",
            "Arntzen Hall",
            "Parks Hall",
            "Interdisciplinary Science Building",
            "Biology",
            "Morse Hall",
            "Carver",
            "Fine Arts",
            "SMATE / Science Lecture",
            "Miller Hall",
            "Bond Hall",
            "Humanities Building",
            "Haggard Hall",
            "Wilson Library",
            "Old Main",
            "Edens Hall",
            "Edens Hall North",
            "Higginson Hall",
            "Nash Hall",
            "Mathes Hall",
            "Viking Commons",
            "Viking Union",
            "Performing Arts Center")

        /** UI Elements */
        private lateinit var startTourButton: FloatingActionButton
        private lateinit var stopTourButton: FloatingActionButton
        private lateinit var skipToNextDestinationButton: Button
        private lateinit var navigationTypeTextView: TextView

        /** Initializes the tour functionality */
        fun init(activity: AppCompatActivity) {
            Companion.activity = activity

            initializeUI()
            initializeDestinations()
        }

        /** Initializes all UI functionality */
        private fun initializeUI() {
            startTourButton = activity.findViewById(R.id.start_tour_fab)
            startTourButton.setOnClickListener {
                startTour()
            }
            stopTourButton = activity.findViewById(R.id.stopNavButton)
            stopTourButton.setOnClickListener {
                stopTour()
            }

            skipToNextDestinationButton = activity.findViewById(R.id.skipToNextDestinationButton)
            skipToNextDestinationButton.setOnClickListener {
                skipToNextDestination()
            }

            navigationTypeTextView = activity.findViewById(R.id.navigationTypeTextView)
            formatTourText()
        }

        /** Initializes all destinations based on destination route */
        private fun initializeDestinations() {
            for (name in destinationRoute) {
                val entity = getEntityWithName(name)
                if (entity != null) {
                    destinations.add(entity)
                }
            }
        }

        /** Starts the tour */
        fun startTour() {
            onTour = true
            destinationIndex = 0
            Navigation.startNavigationTo(destinations[destinationIndex])
            startTourButton.visibility = View.INVISIBLE
            stopTourButton.visibility = View.VISIBLE
            stopTourButton.setOnClickListener {
                stopTour()
            }
            skipToNextDestinationButton.visibility = View.VISIBLE
            navigationTypeTextView.visibility = View.VISIBLE
            formatTourText()
        }

        /** Stops the tour */
        fun stopTour() {
            onTour = false
            Navigation.stopNavigation()
            startTourButton.visibility = View.VISIBLE
            stopTourButton.visibility = View.INVISIBLE
            skipToNextDestinationButton.visibility = View.INVISIBLE
        }

        /** Skips to the next destination */
        private fun skipToNextDestination() {
            destinationIndex++
            if (destinationIndex < destinations.size) {
                Navigation.startNavigationTo(destinations[destinationIndex])
                formatTourText()
            }
            else {
                stopTour()
            }
        }

        /** Helper function to retrieve entity from list with a specific name */
        private fun getEntityWithName(name: String): Entity? {
            val entities = Model.getEntities()
            for (entity in entities) {
                if (entity.getName() == name) {
                    return entity
                }
            }

            return null
        }

        private fun formatTourText() {
            navigationTypeTextView.text = "Campus Tour: ${destinationIndex+1}/${destinations.size}"
        }
    }
}