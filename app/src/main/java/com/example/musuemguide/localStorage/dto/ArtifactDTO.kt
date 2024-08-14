package com.example.musuemguide.localStorage.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "artifactDao")
data class ArtifactDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "artifact_title")
    val title: String = "",
    @ColumnInfo(name = "artifact_category")
    val category: String = "",
    @ColumnInfo(name = "artifact_details")
    val details: String = "",
    @ColumnInfo(name = "artifact_image")
    val image: String = "",
    @ColumnInfo(name = "type")
    val type: String

)