/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.codelabs.hellogeospatial

import android.graphics.Color
import android.location.Location
import android.opengl.Matrix
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.artourguideapp.entities.*
import com.example.artourguideapp.GeospatialActivity
import com.example.artourguideapp.arcore.AnchorHelper
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.helpers.TrackingStateHelper
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.samplerender.*
import com.example.artourguideapp.arcore.java.com.google.ar.core.examples.java.common.samplerender.arcore.BackgroundRenderer
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Anchor
import com.google.ar.core.TrackingState
//import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper
//import com.google.ar.core.examples.java.common.helpers.TrackingStateHelper
//import com.google.ar.core.examples.java.common.samplerender.Framebuffer
//import com.google.ar.core.examples.java.common.samplerender.Mesh
//import com.google.ar.core.examples.java.common.samplerender.SampleRender
//import com.google.ar.core.examples.java.common.samplerender.Shader
//import com.google.ar.core.examples.java.common.samplerender.Texture
//import com.google.ar.core.examples.java.common.samplerender.arcore.BackgroundRenderer
import com.google.ar.core.exceptions.CameraNotAvailableException
import java.io.IOException
import kotlin.math.max


class HelloGeoRenderer(val activity: GeospatialActivity) :
  SampleRender.Renderer, DefaultLifecycleObserver {
  //<editor-fold desc="ARCore initialization" defaultstate="collapsed">
  companion object {
    val TAG = "HelloGeoRenderer"

    private val Z_NEAR = 0.1f
    private val Z_FAR = 1000f
  }

  lateinit var backgroundRenderer: BackgroundRenderer
  lateinit var virtualSceneFramebuffer: Framebuffer
  var hasSetTextureNames = false

  // Virtual object (ARCore pawn)
  lateinit var virtualObjectMesh: Mesh
  lateinit var virtualObjectShader: Shader
  lateinit var virtualObjectTexture: Texture

  // Temporary matrix allocated here to reduce number of allocations for each frame.
  val modelMatrix = FloatArray(16)
  val viewMatrix = FloatArray(16)
  val projectionMatrix = FloatArray(16)
  val modelViewMatrix = FloatArray(16) // view x model

  val modelViewProjectionMatrix = FloatArray(16) // projection x view x model

  val session
    get() = activity.arCoreSessionHelper.session

  val displayRotationHelper = DisplayRotationHelper(activity)
  val trackingStateHelper = TrackingStateHelper(activity)

  override fun onResume(owner: LifecycleOwner) {
    displayRotationHelper.onResume()
    hasSetTextureNames = false
  }

  override fun onPause(owner: LifecycleOwner) {
    displayRotationHelper.onPause()
  }

  override fun onSurfaceCreated(render: SampleRender) {
    // Prepare the rendering objects.
    // This involves reading shaders and 3D model files, so may throw an IOException.
    try {
      backgroundRenderer = BackgroundRenderer(render)
      virtualSceneFramebuffer = Framebuffer(render, /*width=*/ 1, /*height=*/ 1)

      // Virtual object to render (Geospatial Marker)
      virtualObjectTexture =
        Texture.createFromAsset(
          render,
          "models/pink.png",
          Texture.WrapMode.CLAMP_TO_EDGE,
          Texture.ColorFormat.SRGB
        )

      virtualObjectMesh = Mesh.createFromAsset(render, "models/smallCube.obj")
      virtualObjectShader =
        Shader.createFromAssets(
          render,
          "shaders/ar_unlit_object.vert",
          "shaders/ar_unlit_object.frag",
          /*defines=*/ null)
          .setTexture("u_Texture", virtualObjectTexture)

      backgroundRenderer.setUseDepthVisualization(render, false)
      backgroundRenderer.setUseOcclusion(render, false)
    } catch (e: IOException) {
      Log.e(TAG, "Failed to read a required asset file", e)
      showError("Failed to read a required asset file: $e")
    }
  }

  override fun onSurfaceChanged(render: SampleRender, width: Int, height: Int) {
    displayRotationHelper.onSurfaceChanged(width, height)
    virtualSceneFramebuffer.resize(width, height)
  }
  //</editor-fold>

  private var currentAnchorList : MutableList<Anchor> = mutableListOf()

  override fun onDrawFrame(render: SampleRender) {
    val session = session ?: return

    //<editor-fold desc="ARCore frame boilerplate" defaultstate="collapsed">
    // Texture names should only be set once on a GL thread unless they change. This is done during
    // onDrawFrame rather than onSurfaceCreated since the session is not guaranteed to have been
    // initialized during the execution of onSurfaceCreated.
    if (!hasSetTextureNames) {
      session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))
      hasSetTextureNames = true
    }

    // -- Update per-frame state

    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session)

    // Obtain the current frame from ARSession. When the configuration is set to
    // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
    // camera framerate.
    val frame =
      try {
        session.update()
      } catch (e: CameraNotAvailableException) {
        Log.e(TAG, "Camera not available during onDrawFrame", e)
        showError("Camera not available. Try restarting the app.")
        return
      }

    val camera = frame.camera

    // BackgroundRenderer.updateDisplayGeometry must be called every frame to update the coordinates
    // used to draw the background camera image.
    backgroundRenderer.updateDisplayGeometry(frame)

    // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
    trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

    // -- Draw background
    if (frame.timestamp != 0L) {
      // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
      // drawing possible leftover data from previous sessions if the texture is reused.
      backgroundRenderer.drawBackground(render)
    }

    // If not tracking, don't draw 3D objects.
    if (camera.trackingState == TrackingState.PAUSED) {
      return
    }

    // Get projection matrix.
    camera.getProjectionMatrix(projectionMatrix, 0, Z_NEAR, Z_FAR)

    // Get camera matrix and draw.
    camera.getViewMatrix(viewMatrix, 0)

    render.clear(virtualSceneFramebuffer, 0f, 0f, 0f, 0f)
    //</editor-fold>

    // TODO: Obtain Geospatial information and display it on the map.
    val earth = session.earth
    if (earth?.trackingState == TrackingState.TRACKING) {
      // TODO: the earth object may be used here
      val cameraGeospatialPose = earth.cameraGeospatialPose

      activity.view.mapView?.updateMapPosition(
        latitude = cameraGeospatialPose.latitude,
        longitude = cameraGeospatialPose.longitude,
        heading = cameraGeospatialPose.heading
      )

      activity.view.updateStatusText(earth, cameraGeospatialPose)


    }

    // check status of terrain anchors, render a cube if ready
    if (currentAnchorList.isNotEmpty()) {
      for (anchor in currentAnchorList) {
//        when (anchor.terrainAnchorState) {
//          Anchor.TerrainAnchorState.SUCCESS -> {
//            if (anchor.trackingState == TrackingState.TRACKING) {
//              render.renderCompassAtAnchor(anchor)
//              println("DEBUG - RENDERED ANCHOR AT ${anchor.pose}")
//            }
//          }
//          Anchor.TerrainAnchorState.TASK_IN_PROGRESS -> {
//            println("Waiting on ARCore API to resolve terrain anchor's pose")
//          }
//          Anchor.TerrainAnchorState.ERROR_UNSUPPORTED_LOCATION -> {
//            println("Terrain Anchor in unsupported location")
//          }
//          Anchor.TerrainAnchorState.ERROR_NOT_AUTHORIZED -> {
//            println("Error authorizing app with ARCore API")
//          }
//          Anchor.TerrainAnchorState.ERROR_INTERNAL -> {
//            println("Terrain anchor could not be resolved due to an internal error")
//          }
//          Anchor.TerrainAnchorState.NONE -> {
//            println("This anchor is not a Terrain Anchor or became invalid")
//          }
//          else -> {}
//        }
      }
    }

    // Draw the placed anchor, if it exists.
    earthAnchor?.let {
      render.renderCompassAtAnchor(it)
    }

//    testAnchorMyHouse?.let {
//      render.renderCompassAtAnchor(it)
//    }
//
//    testAnchorCF?.let {
//      render.renderCompassAtAnchor(it)
//    }
//
//    testAnchorWK?.let {
//      render.renderCompassAtAnchor(it)
//    }

    // Compose the virtual scene with the background.
    backgroundRenderer.drawVirtualScene(render, virtualSceneFramebuffer, Z_NEAR, Z_FAR)
  }

  var earthAnchor: Anchor? = null

  fun onMapClick(latLng: LatLng) {
    val earth = session?.earth ?: return
    if (earth.trackingState != TrackingState.TRACKING) {
      return
    }
    earthAnchor?.detach()

    // Place the earth anchor at the same altitude as that of the camera to make it easier to view.
    val altitude = earth.cameraGeospatialPose.altitude - 1
    // The rotation quaternion of the anchor in the East-Up-South (EUS) coordinate system.
    val qx = 0f
    val qy = 0f
    val qz = 0f
    val qw = 1f
    earthAnchor =
      earth.createAnchor(latLng.latitude, latLng.longitude, altitude, qx, qy, qz, qw)

    activity.view.mapView?.earthMarker?.apply {
      position = latLng
      isVisible = true
    }

  }


  /** Detaches all current anchors and then replaces
   * them with the new anchors created from the list
   * of entities */
  fun updateAnchors(entityList : MutableList<Entity>) {
    val earth = session?.earth ?: return
    if (earth.trackingState != TrackingState.TRACKING) {
      return
    }

    // TODO: add check to see if we can keep an existing anchor
//    for (anchor in currentAnchorList) {
//      anchor.detach()
//    }
//    currentAnchorList.clear()
//
//    for (entity in entityList) {
//      val newAnchor = earth.resolveAnchorOnTerrain(
//        entity.getCentralLocation().latitude,
//        entity.getCentralLocation().longitude,
//        10.0,
//        0f,0f,0f,1f
//      )
//      currentAnchorList.add(newAnchor)
//    }

    AnchorHelper.run {
      val currentLocation = Location("User")
      currentLocation.latitude = earth.cameraGeospatialPose.latitude
      currentLocation.longitude = earth.cameraGeospatialPose.longitude

      removeUnusedAnchors(entityList, currentLocation)

      currentAnchorList = updateCurrentAnchors(earth, entityList, currentLocation)
    }

    // for debug purposes
    println("DEBUG - FINISHED UPDATEANCHORS() CALL INSIDE RENDERER")
  }


  private fun SampleRender.renderCompassAtAnchor(anchor: Anchor) {
    // Get the current pose of the Anchor in world space. The Anchor pose is updated
    // during calls to session.update() as ARCore refines its estimate of the world.
    anchor.pose.toMatrix(modelMatrix, 0)

    // Calculate model/view/projection matrices
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)

    // Update shader properties and draw
    virtualObjectShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
    draw(virtualObjectMesh, virtualObjectShader, virtualSceneFramebuffer)
  }

  private fun showError(errorMessage: String) =
    activity.view.snackbarHelper.showError(activity, errorMessage)
}
