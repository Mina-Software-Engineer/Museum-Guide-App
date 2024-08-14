package com.example.musuemguide.localStorage.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musuemguide.localStorage.dto.ArtifactDTO

@Dao
interface ArtifactDao {

    @Query("SELECT * FROM artifactDao ORDER BY id")
    suspend fun getAllArtifacts(): List<ArtifactDTO>
            //List<ArtifactDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllArtifacts(artifacts: List<ArtifactDTO>)

    @Query("DELETE FROM artifactDao")
    suspend fun clearDatabase()
}