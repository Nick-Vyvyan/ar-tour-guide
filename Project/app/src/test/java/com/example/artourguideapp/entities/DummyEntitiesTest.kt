package com.example.artourguideapp.entities

import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DummyEntitiesTest {

    @Test
    fun dummyEntitiesCoverageFunction() {
        val result = DummyEntities.getEntityList().size > 0
        assertThat(result).isTrue()
    }
}