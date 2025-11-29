package com.example.appagendita_grupo1.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TaskApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoteApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EventApi