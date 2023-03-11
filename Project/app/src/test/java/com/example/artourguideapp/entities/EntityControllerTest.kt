package com.example.artourguideapp.entities

import com.example.artourguideapp.entities.DummyEntities
import com.example.artourguideapp.entities.EntityController
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityControllerTest {

    private var entityController = EntityController()

    @Before
    fun setUp() {
        entityController = EntityController()
    }

    @Test
    fun getEntities() {
        entityController.setEntities(DummyEntities.getEntityList())
        val result = entityController.getEntities().size > 0

        assertThat(result).isTrue()
    }

    @Test
    fun setEntities() {
        entityController.setEntities(DummyEntities.getEntityList())
        val result = entityController.getEntities().size > 0

        assertThat(result).isTrue()
    }

    @Test
    fun setSearchIndex() {
    }

    @Test
    fun getSearchIndex() {
    }
}