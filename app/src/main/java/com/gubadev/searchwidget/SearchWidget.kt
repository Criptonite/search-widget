package com.gubadev.searchwidget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.search_widget.view.*


class SearchWidget : LinearLayout {

    val TRANSITION_DURATION = 500
    var searchListener: SearchListener? = null
    var levelsListener: LevelsListener? = null
    var searchCloseListener: CloseSearchListener? = null
    var transitionBackground: TransitionDrawable? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = HORIZONTAL
        View.inflate(context, R.layout.search_widget, this)
        transitionBackground = background as TransitionDrawable
        search_edit.setOnClickListener { searchListener?.onSearchClicked() }
        close_search.setOnClickListener { searchCloseListener?.onSearchClicked() }
        levels.setOnClickListener { levelsListener?.onLevelClicked() }

    }


    fun onSearchActive() {
        val scaleLevels = ObjectAnimator.ofFloat(levels, "scaleX", 0f);
        val moveLineAnimation = ObjectAnimator.ofFloat(line_separator, "translationX", (levels.width / 2).toFloat())
        val scaleLine = ObjectAnimator.ofFloat(line_separator, "scaleY", 0f)
        val showCloseX = ObjectAnimator.ofFloat(close_search, "scaleX", 1f)
        val showCloseY = ObjectAnimator.ofFloat(close_search, "scaleY", 1f)
        val animations = AnimatorSet()
        animations.play(scaleLevels).with(moveLineAnimation)
        animations.play(scaleLine).after(moveLineAnimation)
        animations.play(showCloseY).after(scaleLevels)
        animations.play(showCloseX).with(showCloseY)
        animations.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                levels.visibility = View.GONE
                line_separator.visibility = View.GONE
                close_search.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animations.start()

        background = ContextCompat.getDrawable(context, R.drawable.background_in_search_mode)
    }

    fun onSearchInactive() {
        line_separator.visibility = View.VISIBLE
        levels.visibility = View.VISIBLE
        close_search.visibility = View.GONE


        val scaleLevels = ObjectAnimator.ofFloat(levels, "scaleX", 1f);
        val moveLineAnimation = ObjectAnimator.ofFloat(line_separator, "translationX", 0f)
        val scaleLine = ObjectAnimator.ofFloat(line_separator, "scaleY", 1f)
        val animations = AnimatorSet()

        animations.play(scaleLine).before(moveLineAnimation)
        animations.play(moveLineAnimation).with(scaleLevels)

        animations.start()
        background = ContextCompat.getDrawable(context, R.drawable.background_animation)
        transitionBackground = background as TransitionDrawable
    }

    fun onLevelsActive() {

        transitionBackground?.startTransition(TRANSITION_DURATION)
    }

    fun onLevelsInactive() {
        transitionBackground?.reverseTransition(TRANSITION_DURATION)
    }


    interface SearchListener {
        fun onSearchClicked()
    }

    interface LevelsListener {
        fun onLevelClicked()
    }

    interface CloseSearchListener {
        fun onSearchClicked()
    }

}