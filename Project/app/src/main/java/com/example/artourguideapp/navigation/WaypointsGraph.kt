package com.example.artourguideapp.navigation

import android.location.Location
import android.util.Log
import kotlin.collections.ArrayList

class WaypointsGraph{

    private val adjacencyMap = mutableMapOf<WaypointVertex, ArrayList<WaypointEdge>>()

    fun size(): Int {
        return adjacencyMap.size
    }

    fun createVertex(data: Location): WaypointVertex {
        val vertex = WaypointVertex(adjacencyMap.count(), data)
        adjacencyMap[vertex] = arrayListOf()
        return vertex
    }

    private fun getClosestVertexToLocation(location: Location) : WaypointVertex? {
        var smallestDistance = Float.MAX_VALUE
        var closestVertex: WaypointVertex? = null

        adjacencyMap.forEach { (vertex, edges) ->
            if (vertex.location.distanceTo(location) < smallestDistance) {
                smallestDistance = vertex.location.distanceTo(location)
                closestVertex = vertex
            }
        }
        return closestVertex
    }

    fun getVertex(location: Location): WaypointVertex? {
        adjacencyMap.forEach { (vertex, edges) ->
            if (vertex.location == location) {
                return vertex
            }
        }
        return null
    }

    fun addEdge(vertexA: WaypointVertex, vertexB: WaypointVertex) {
        val edgeAB = WaypointEdge(vertexA, vertexB, vertexA.location.distanceTo(vertexB.location))
        val edgeBA = WaypointEdge(vertexB, vertexA, vertexB.location.distanceTo(vertexA.location))
        adjacencyMap[vertexA]?.add(edgeAB)
        adjacencyMap[vertexB]?.add(edgeBA)
    }

    override fun toString(): String {
        return buildString {
            adjacencyMap.forEach { (vertex, edges) ->
                edges.forEach {
                    val distanceInFeet = 3.28084 * it.weight
                    append("Distance from ${it.vertexA.location.provider} to ${it.vertexB.location.provider} = $distanceInFeet feet\n")
                }
            }
        }
    }

    // Dijkstra's Algorithm to find shortest path. Algorithm terminates once destination is processed
    fun shortestPath(sourceLocation: Location, destinationLocation: Location): NavigationPath {

        val source = getClosestVertexToLocation(sourceLocation)
        val destination = getClosestVertexToLocation(destinationLocation)

        // Path to return
        var path = mutableListOf<Location>()
        var distance = 0f

        // If null arguments, return empty path
        if (source == null || destination == null) {
            return NavigationPath(path, distance)
        }

        distance += destination.location.distanceTo(destinationLocation)

        // Get the set of vertices without the source
        var setOfVerticesWithoutSource = adjacencyMap.keys.toMutableSet()
        setOfVerticesWithoutSource.remove(source)

        // Create a distance map and initialize distance from the source to itself as 0
        var distanceMap = mutableMapOf<WaypointVertex, Float>()
        distanceMap[source] = 0f

        // Construct a path map that gives a path to every vertex and initialize path for source
        var pathMap = mutableMapOf<WaypointVertex, MutableList<WaypointVertex>>()
        pathMap[source] = mutableListOf()

        // Set distances to everything but source as max value and initialize path maps
        setOfVerticesWithoutSource.forEach {
            distanceMap[it] = Float.MAX_VALUE
            pathMap[it] = mutableListOf()
        }

        // Created visited vertices set and unvisited vertices set
        var visitedVertices = mutableSetOf<WaypointVertex>()
        var unvisitedVertices = adjacencyMap.keys.toMutableSet()
        unvisitedVertices.remove(source)

        // While there are still vertices to visit...
        while (unvisitedVertices.isNotEmpty()) {

            // Get the vertex with the smallest distance from the source
            var vertexWithMinDistance = getVertexWithMinimumDistance(unvisitedVertices, distanceMap, source)

            // Add to visited vertices and remove from unvisited vertices
            visitedVertices.add(vertexWithMinDistance)
            unvisitedVertices.remove(vertexWithMinDistance)

            // Iterate through each edge of that vertex and update distanceMap and pathMap
            adjacencyMap[vertexWithMinDistance]?.forEach {

                // If the saved distance to the current vertex's neighbor is bigger than the distance
                // from the source to the current vertex plus its edge to its neighbor
                if (distanceMap[it.vertexB]!! > (distanceMap[it.vertexA]!! + it.weight)) {

                    // Reassign saved distance (Path now goes through the current vertex)
                    distanceMap[it.vertexB] = distanceMap[it.vertexA]!! + it.weight

                    // Clear old pathMap for neighbor, add everything from current vertex pathMap into
                    // neighbor pathMap, add current vertex to neighbor pathMap
                    pathMap[it.vertexB]!!.clear()
                    pathMap[it.vertexB]!!.addAll(pathMap[it.vertexA] as Collection<WaypointVertex>)
                    pathMap[it.vertexB]!!.add(it.vertexA)
//                    Log.d("NAVIGATION", "Updating path to vertex (${it.vertexB.location}). Path is ${pathMap[it.vertexB]}")
                }
            }

            // If current vertex is our destination, construct path with this vertex's pathMap plus this
            // vertex at the end and return the path
            if (vertexWithMinDistance == destination) {
                pathMap[destination]!!.forEach {
                    path.add(it.location)
                }
                path.add(vertexWithMinDistance.location)
                distance += distanceMap[vertexWithMinDistance]!!
                return NavigationPath(path, distance)
            }
        }

        return NavigationPath(path, distance)
    }

    // Helper function for Dijkstra. Gets the next closest vertex in the distanceMap from the unvisited set
    private fun getVertexWithMinimumDistance(vertexSet: Set<WaypointVertex>,
                                             distanceMap: Map<WaypointVertex, Float>,
                                             defaultVertex: WaypointVertex
    )
                                            : WaypointVertex {
        var smallestDistance = Float.MAX_VALUE
        var smallestVertex: WaypointVertex = defaultVertex

        vertexSet.forEach {
            if (distanceMap[it]!! < smallestDistance) {
                smallestDistance = distanceMap[it]!!
                smallestVertex = it
            }
        }

        return smallestVertex
    }
}