package com.example.appagendita_grupo1.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for SessionManager
 * These are instrumented tests because SessionManager requires Android Context
 */
@RunWith(AndroidJUnit4::class)
class SessionManagerTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        sessionManager = SessionManager.getInstance(context)
        // Clear any existing session before each test
        sessionManager.clearSession()
    }

    @After
    fun tearDown() {
        // Clean up after each test
        sessionManager.clearSession()
    }

    @Test
    fun testSaveSession() {
        // Given
        val userId = 123L
        val userEmail = "test@example.com"
        val userName = "Test User"

        // When
        sessionManager.saveSession(userId, userEmail, userName)

        // Then
        assertTrue(sessionManager.isLoggedIn())
        assertEquals(userId, sessionManager.getCurrentUserId())
        assertEquals(userEmail, sessionManager.getCurrentUserEmail())
        assertEquals(userName, sessionManager.getCurrentUserName())
    }

    @Test
    fun testClearSession() {
        // Given - save a session first
        sessionManager.saveSession(123L, "test@example.com", "Test User")
        assertTrue(sessionManager.isLoggedIn())

        // When
        sessionManager.clearSession()

        // Then
        assertFalse(sessionManager.isLoggedIn())
        assertNull(sessionManager.getCurrentUserId())
        assertNull(sessionManager.getCurrentUserEmail())
        assertNull(sessionManager.getCurrentUserName())
    }

    @Test
    fun testIsSessionValid() {
        // Initially no session
        assertFalse(sessionManager.isSessionValid())

        // Save a session
        sessionManager.saveSession(123L, "test@example.com", "Test User")

        // Now session should be valid
        assertTrue(sessionManager.isSessionValid())

        // Clear session
        sessionManager.clearSession()

        // Session should no longer be valid
        assertFalse(sessionManager.isSessionValid())
    }

    @Test
    fun testGetCurrentUserIdWhenNotLoggedIn() {
        // When no session exists
        assertFalse(sessionManager.isLoggedIn())
        
        // Then getCurrentUserId should return null
        assertNull(sessionManager.getCurrentUserId())
    }

    @Test
    fun testSessionPersistence() {
        // Given - save a session
        val userId = 456L
        val userEmail = "persist@example.com"
        val userName = "Persist User"
        sessionManager.saveSession(userId, userEmail, userName)

        // When - get a new SessionManager instance using singleton (simulating app restart)
        val newSessionManager = SessionManager.getInstance(context)

        // Then - session data should still be available
        assertTrue(newSessionManager.isLoggedIn())
        assertEquals(userId, newSessionManager.getCurrentUserId())
        assertEquals(userEmail, newSessionManager.getCurrentUserEmail())
        assertEquals(userName, newSessionManager.getCurrentUserName())
    }
}
