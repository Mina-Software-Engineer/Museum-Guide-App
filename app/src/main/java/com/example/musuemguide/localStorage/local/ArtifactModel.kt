package com.example.musuemguide.localStorage.local

import android.os.Parcelable
import com.example.musuemguide.localStorage.dto.ArtifactDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
class ArtifactModel(
    val id: Long = 0L,
    val title: String = "",
    val category: String = "",
    val details: String = "",
    val image: String = "",
    val type: String ): Parcelable


class HeaderModel(
    val id: Long = 0L,
    val title: String = "",
    val type: String
)

fun List<ArtifactDTO>.asArtifactModel(): List<ArtifactModel> {

    return map { artifactDTO ->
        ArtifactModel(
            id = artifactDTO.id,
            title = artifactDTO.title,
            category = artifactDTO.category,
            details = artifactDTO.details,
            image = artifactDTO.image,
            type = artifactDTO.type
        )
    }
}

fun HeaderModel.asArtifactModel(): ArtifactModel {
    return ArtifactModel(
        id = this.id,
        title = this.title,
        type = this.type
    )
}

fun List<ArtifactModel>.asArtifactDTO(): List<ArtifactDTO> {

    return map { artifactModel ->
        ArtifactDTO(
            id = artifactModel.id,
            title = artifactModel.title,
            category = artifactModel.category,
            details = artifactModel.details,
            image = artifactModel.image,
            type = artifactModel.type
        )
    }
}