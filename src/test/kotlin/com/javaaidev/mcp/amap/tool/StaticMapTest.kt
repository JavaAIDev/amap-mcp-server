package com.javaaidev.mcp.amap.tool

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StaticMapTest {
    val center = GeoLocation(39.990464, 116.481485)
    val p1 = GeoLocation(40.0, 116.5)
    val p2 = GeoLocation(40.05, 116.8)

    @Test
    fun simpleMap() {
        val url = StaticMap.generateUrl(
            StaticMapRequest(center)
        )
        assertNotNull(url)
    }

    @Test
    fun markers() {
        val url = StaticMap.generateUrl(
            StaticMapRequest(
                center,
                markers = Markers(
                    listOf(
                        MarkersGroup(
                            listOf(center),
                            MarkerStyle(MarkerSize.LARGE, "A")
                        )
                    )
                )
            )
        )
        assertNotNull(url)
        println(url)
        assertTrue(url.contains("markers="))
    }

    @Test
    fun labels() {
        val url = StaticMap.generateUrl(
            StaticMapRequest(
                center,
                labels = Labels(
                    listOf(
                        LabelsGroup(
                            listOf(center),
                            LabelStyle("Test", fontSize = 16)
                        )
                    )
                )
            )
        )
        assertNotNull(url)
        println(url)
        assertTrue(url.contains("labels="))
    }

    @Test
    fun paths() {
        val url = StaticMap.generateUrl(
            StaticMapRequest(
                center,
                size = MapSize.MAXIMUM,
                paths = Paths(
                    listOf(
                        PathsGroup(
                            listOf(center, p1, p2)
                        )
                    )
                )
            )
        )
        assertNotNull(url)
        println(url)
        assertTrue(url.contains("paths="))
    }
}