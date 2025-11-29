package com.example.appagendita_grupo1.viewmodel

import com.example.appagendita_grupo1.data.repository.NoteRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class AddNoteViewModelTest : StringSpec({

    // Configuración de Corrutinas para pruebas (Main dispatcher)
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    // --- TEST 1: ESTADO INICIAL ---
    "El estado inicial debería tener campos vacíos" {
        val repository = mockk<NoteRepository>(relaxed = true)
        val viewModel = AddNoteViewModel(repository)

        viewModel.state.title shouldBe ""
        viewModel.state.description shouldBe ""
        viewModel.state.titleError shouldBe null
    }

    // --- TEST 2: VALIDACIÓN FALLIDA ---
    "onSaveNote NO debería llamar al repositorio si el título está vacío" {
        val repository = mockk<NoteRepository>(relaxed = true)
        val viewModel = AddNoteViewModel(repository)

        // Dejamos el título vacío
        viewModel.onTitleChange("")

        // Intentamos guardar
        viewModel.onSaveNote()

        // Verificamos que NO se llamó al repositorio
        coVerify(exactly = 0) { repository.addNote(any(), any(), any()) }

        // Verificamos que se marcó el error en el estado
        viewModel.state.titleError shouldNotBe null
    }

    // --- TEST 3: GUARDADO EXITOSO ---
    "onSaveNote DEBERÍA llamar al repositorio si los datos son válidos" {
        val repository = mockk<NoteRepository>(relaxed = true)
        val viewModel = AddNoteViewModel(repository)

        // Llenamos los datos
        viewModel.onTitleChange("Nota Válida")
        viewModel.onDescriptionChange("Descripción de prueba")

        // Intentamos guardar.
        // IMPORTANTE: Como viewModel.onSaveNote lanza una corrutina en viewModelScope,
        // necesitamos avanzar el tiempo del dispatcher de prueba para que se ejecute.
        viewModel.onSaveNote()
        testDispatcher.scheduler.advanceUntilIdle() // Ejecuta las corrutinas pendientes

        // Verificamos que se llamó al repositorio con los datos correctos
        coVerify(exactly = 1) {
            repository.addNote(
                title = "Nota Válida",
                description = "Descripción de prueba",
                imageUri = null
            )
        }
    }
})