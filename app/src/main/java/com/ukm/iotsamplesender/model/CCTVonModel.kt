package com.ukm.iotsamplesender.model

import com.google.firebase.firestore.PropertyName

data class CCTVonModel(
    @PropertyName("startTask")
    val startTask: Boolean = false
)
