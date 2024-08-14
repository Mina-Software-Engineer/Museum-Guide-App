package com.example.musuemguide.localStorage.local

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.musuemguide.localStorage.dto.ArtifactDTO

interface DataSource {
    suspend fun addArtifacts(artifacts: List<ArtifactDTO>)

    suspend fun getAllArtifacts(): List<ArtifactDTO>

    suspend fun deleteAllArtifacts()
}