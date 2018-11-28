package com.anton.github.datasource.content.publication

import com.anton.github.data.entity.Notification

interface PublicationLocalRepository {
    fun getPublications(): List<Notification>

    fun savePublications(data: List<Notification>)
}