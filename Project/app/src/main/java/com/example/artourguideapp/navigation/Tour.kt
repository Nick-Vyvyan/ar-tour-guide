package com.example.artourguideapp.navigation

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.artourguideapp.entities.EntityModel
import com.example.artourguideapp.R
import com.example.artourguideapp.entities.Entity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * This class allows a Tour option to be added to the main AR activity. The tour takes the
 * user through a series of destinations. The tour progresses at the user's pace with a "next" button
 * and can be stopped at any time. init MUST BE CALLED BEFORE ATTEMPTING TO TOUR.
 *
 * The tour has been hard-coded according to specifications by our client, Diana Feinson
 */
class Tour {

    /**
     * There only needs to be a single Tour object so all functionality inside companion
     */
    companion object {

        /** On tour (true or false) */
        var onTour = false

        /** AR Activity */
        private lateinit var activity: AppCompatActivity

        /** Destinations */
        private var destinations: MutableList<Entity> = mutableListOf()

        /** Destination index */
        private var destinationIndex = 0

        /** Hard-coded destination route */
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

        //region UI Elements

        /** Start tour button */
        private lateinit var startTourButton: FloatingActionButton

        /** Stop tour button */
        private lateinit var stopTourButton: FloatingActionButton

        /** Skip to next destination button */
        private lateinit var skipToNextDestinationButton: Button

        /** Navigation type text view */
        private lateinit var navigationTypeTextView: TextView

        //endregion

        /**
         * Initializes the tour functionality
         *
         * @param activity AR activity
         */
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
                advanceToNextDestination()
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

            skipToNextDestinationButton.text = "Next"
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

        /** Advances to the next destination */
        private fun advanceToNextDestination() {
            destinationIndex++
            if (destinationIndex < destinations.size) {
                Navigation.startNavigationTo(destinations[destinationIndex])
                formatTourText()
                if (destinationIndex == destinations.size - 1) {
                    skipToNextDestinationButton.text = "End"
                }
            }
            else {
                stopTour()
            }
        }

        /**
         * Helper function to retrieve entity from list with a specific name
         *
         * @param name Name of Entity to retrieve
         * @return Entity with given name
         */
        private fun getEntityWithName(name: String): Entity? {
            val entities = EntityModel.getEntities()
            for (entity in entities) {
                if (entity.getName() == name) {
                    return entity
                }
            }

            return null
        }

        /** Format tour text for user display */
        private fun formatTourText() {
            navigationTypeTextView.text = "Campus Tour: ${destinationIndex+1}/${destinations.size}"
        }
    }
}