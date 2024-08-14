package com.example.musuemguide.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musuemguide.R
import com.example.musuemguide.databinding.RecyclerviewHeaderBinding
import com.example.musuemguide.databinding.RecyclerviewListItemsBinding
import com.example.musuemguide.localStorage.local.ArtifactModel
import com.example.musuemguide.localStorage.local.HeaderModel


class HomeAdapter(
    private val clickListener: HomeItemClickListener,
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(HomeDiffCallback()) {

    //private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addList(list: List<ArtifactModel>) {
        submitList(list.map { artifactModel ->
            if (artifactModel.type == "HEADER") {
                //Log.d("checkkkkkk, ", "10")
                DataItem.Header(
                    HeaderModel(
                        artifactModel.id,
                        artifactModel.title,
                        artifactModel.type
                    )
                )
            } else {
                //Log.d("checkkkkkk, ", "1")
                DataItem.ArtifactItem(
                    ArtifactModel(
                        id = artifactModel.id,
                        title = artifactModel.title,
                        details = artifactModel.details,
                        category = artifactModel.category,
                        image = artifactModel.image,
                        type = artifactModel.type
                    )
                )
            }
        })
    }

    // ViewHolder for Header
    class HeaderViewHolder(private val binding: RecyclerviewHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Bind header data if needed
        fun bind(artifactItem: DataItem.Header) {
            //Log.d("checkkkkkk, ", "2")
            binding.artifact = artifactItem.headerArtifact
            binding.executePendingBindings()
        }
    }

    // ViewHolder for ArtifactItem
    class ArtifactViewHolder(private val binding: RecyclerviewListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Bind artifact data if needed
        fun bind(clickListener: HomeItemClickListener, artifactItem: DataItem.ArtifactItem) {
            //Log.d("checkkkkkk, ", "3")
            binding.artifact = artifactItem.artifact
            binding.homeTitle.text = artifactItem.artifact.title
            binding.homeImg.setImageBitmap(BitmapConverter.stringToBitmap(artifactItem.artifact.image))
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DataItem.ItemType.HEADER.ordinal -> {
                val headerBinding = RecyclerviewHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(headerBinding)
            }

            DataItem.ItemType.BODY.ordinal -> {
                val artifactBinding = RecyclerviewListItemsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ArtifactViewHolder(artifactBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val animator = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.cardview_anim)
        when (holder.itemViewType) {
            DataItem.ItemType.HEADER.ordinal -> {
                //Log.d("checkkkkkk, ", "6")
                val headerHolder = holder as HeaderViewHolder

                holder.itemView.startAnimation(animator)
                headerHolder.bind(item as DataItem.Header)
            }

            DataItem.ItemType.BODY.ordinal -> {
                //Log.d("checkkkkkk, ", "7")
                // Bind artifact data
                val artifactHolder = holder as ArtifactViewHolder
                val artifactItem = item as DataItem.ArtifactItem
                holder.itemView.startAnimation(animator)
                artifactHolder.bind(clickListener, artifactItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        //Log.d("checkkkkkk, ", "8")
        return getItem(position)!!.type.ordinal
    }

}

class HomeItemClickListener(val clickListener: (artifactModel: ArtifactModel) -> Unit) {
    fun onClick(artifact: ArtifactModel) = clickListener(artifact)
}

class HomeDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class ArtifactItem(val artifact: ArtifactModel) : DataItem() {
        override val type: ItemType = ItemType.BODY
    }

    data class Header(val headerArtifact: HeaderModel) : DataItem() {
        override val type: ItemType = ItemType.HEADER
    }

    abstract val type: ItemType

    enum class ItemType {
        HEADER,
        BODY
    }
}