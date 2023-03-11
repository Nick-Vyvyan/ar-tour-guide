package com.example.artourguideapp

import com.example.artourguideapp.entities.DummyEntities
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ControllerTest {

    private var controller = Controller()

    @Before
    fun setUp() {
        controller = Controller()
    }

    @Test
    fun getEntities() {
        controller.setEntities(DummyEntities.getEntityList())
        val result = controller.getEntities().size > 0

        assertThat(result).isTrue()
    }

    @Test
    fun setEntities() {
        controller.setEntities(DummyEntities.getEntityList())
        val result = controller.getEntities().size > 0

        assertThat(result).isTrue()
    }

    @Test
    fun setSearchIndex() {
    }

    @Test
    fun getSearchIndex() {
    }
}