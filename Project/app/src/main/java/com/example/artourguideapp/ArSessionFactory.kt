package com.example.artourguideapp

import android.content.Context
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.ArSceneView

class ArSessionFactory {
    companion object {

        /**
         * Create an AR session with a given context and ArSceneView
         */
        fun createArSession(context: Context, arSceneView: ArSceneView) {
            try {
                var session = Session(context)
                var config = Config(session)

                setConfigParameters(config)
                session.configure(config)

                if (session == null) {
                    return
                } else {
                    arSceneView.session = session;
                    // Clip anything past visible node distance
                    arSceneView.scene.camera.farClipPlane = AppSettings.AR_VISIBILITY_DISTANCE
                }
            } catch (e: UnavailableException) {
                // TODO: Handle exception
            }
        }

        private fun setConfigParameters(config: Config) {
            config.updateMode = AppSettings.AR_UPDATE_MODE
            config.lightEstimationMode = AppSettings.AR_LIGHT_ESTIMATION_MODE
            config.geospatialMode = AppSettings.AR_GEOSPATIAL_MODE
            config.planeFindingMode = AppSettings.AR_PLANE_FINDING_MODE
        }
    }
}