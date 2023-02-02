package com.example.artourguideapp.navigation

import android.view.View
import android.widget.Button
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
        private lateinit var stopNavButton: Button
        private lateinit var startTourButton: FloatingActionButton
        private lateinit var stopTourButton: Button
        private lateinit var skipToNextDestinationButton: Button

        /** Initializes the tour functionality */
        fun init(activity: AppCompatActivity) {
            Companion.activity = activity

            initializeButtons()
            initializeDestinations()
        }

        /** Initializes all UI functionality */
        private fun initializeButtons() {
            stopNavButton = activity.findViewById(R.id.stopNavButton)
            startTourButton = activity.findViewById(R.id.start_tour_fab)
            startTourButton.setOnClickListener {
                startTour()
            }
            stopTourButton = activity.findViewById(R.id.stopTourButton)
            stopTourButton.setOnClickListener {
                stopTour()
            }

            skipToNextDestinationButton = activity.findViewById(R.id.skipToNextDestinationButton)
            skipToNextDestinationButton.setOnClickListener {
                skipToNextDestination()
            }
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
        private fun startTour() {
            destinationIndex = 0
            Navigation.startNavigationTo(destinations[destinationIndex])
            stopNavButton.visibility = View.INVISIBLE
            startTourButton.visibility = View.INVISIBLE
            stopTourButton.visibility = View.VISIBLE
            skipToNextDestinationButton.visibility = View.VISIBLE
        }

        /** Stops the tour */
        private fun stopTour() {
            Navigation.stopNavigation()
            stopNavButton.visibility = View.INVISIBLE
            startTourButton.visibility = View.VISIBLE
            stopTourButton.visibility = View.INVISIBLE
            skipToNextDestinationButton.visibility = View.INVISIBLE
        }

        /** Skips to the next destination */
        private fun skipToNextDestination() {
            destinationIndex++
            if (destinationIndex < destinations.size) {
                Navigation.startNavigationTo(destinations[destinationIndex])
                stopNavButton.visibility = View.INVISIBLE
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
    }
}