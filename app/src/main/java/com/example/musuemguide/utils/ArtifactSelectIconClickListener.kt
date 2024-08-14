package com.example.musuemguide.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.musuemguide.R

class ArtifactSelectIconClickListener @JvmOverloads internal constructor(
    private val context: Context,
    private val sheet: View,
    private val interpolator: Interpolator? = null,
    private val openIcon: Drawable? = null,
    private val closeIcon: Drawable? = null
) : View.OnClickListener {

    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(view: View) {
        updateIcon(view)
        //backdropShown = true

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()



        val translateY = height - context.resources.getDimensionPixelSize(R.dimen.list_reveal_height)

        val animator = ObjectAnimator.ofFloat(
            sheet,
            "translationY",
            (if (backdropShown) translateY else 0).toFloat()
        )
        animator.duration = 500
        if (interpolator != null) {
            animator.interpolator = interpolator
        }
        animatorSet.play(animator)
        animator.start()
    }

    private fun updateIcon(view: View) {
        if (openIcon != null && closeIcon != null) {
            if (view !is ImageView) {
                throw IllegalArgumentException("updateIcon() must be called on an ImageView")
            }
            if (view.drawable == openIcon) {
                view.setImageDrawable(closeIcon)
                backdropShown = true
            } else if (view.drawable == closeIcon) {
                view.setImageDrawable(openIcon)
                backdropShown = false
            }else{
                view.setImageDrawable(closeIcon)
                backdropShown = true
            }
        }
    }
}
