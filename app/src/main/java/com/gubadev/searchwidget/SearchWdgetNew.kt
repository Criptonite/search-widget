package com.gubadev.searchwidget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.search_widget_new.view.*

class SearchWdgetNew: FrameLayout {

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
        View.inflate(context, R.layout.search_widget_new, this)
        transitionBackground = search_form.background as TransitionDrawable
        search_edit.setOnClickListener { searchListener?.onSearchClicked() }
        close_search.setOnClickListener { searchCloseListener?.onSearchClicked() }
        levels.setOnClickListener { levelsListener?.onLevelClicked() }
        shadow.scaleY = 0.5f

    }


    fun onSearchActive() {
        val scaleLevels = ObjectAnimator.ofFloat(levels, "scaleX", 0f);
        val moveLineAnimation = ObjectAnimator.ofFloat(line_separator, "translationX", (levels.width / 2).toFloat())
        val scaleLine = ObjectAnimator.ofFloat(line_separator, "scaleY", 0f)
        val showCloseX = ObjectAnimator.ofFloat(close_search, "scaleX", 1f)
        val showCloseY = ObjectAnimator.ofFloat(close_search, "scaleY", 1f)
        val scaleShadow = ObjectAnimator.ofFloat(shadow, "scaleY", 2f)
        shadow.pivotY = 0f
        val animations = AnimatorSet()
        animations.play(scaleLevels).with(moveLineAnimation).with(scaleShadow)
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
        animations.duration = 200
        animations.start()

        search_form.background = ContextCompat.getDrawable(context, R.drawable.background_in_search_mode)
    }

    fun onSearchInactive() {
        line_separator.visibility = View.VISIBLE
        levels.visibility = View.VISIBLE
        close_search.visibility = View.GONE


        val scaleLevels = ObjectAnimator.ofFloat(levels, "scaleX", 1f);
        val moveLineAnimation = ObjectAnimator.ofFloat(line_separator, "translationX", 0f)
        val scaleLine = ObjectAnimator.ofFloat(line_separator, "scaleY", 1f)
        val scaleShadow = ObjectAnimator.ofFloat(shadow, "scaleY", 0.5f)
        shadow.pivotY = 0f

        val animations = AnimatorSet()

        animations.play(scaleLine).before(moveLineAnimation).with(scaleShadow)
        animations.play(moveLineAnimation).with(scaleLevels)
        animations.duration = 200
        animations.start()
        search_form.background = ContextCompat.getDrawable(context, R.drawable.background_animation)
        transitionBackground = search_form.background as TransitionDrawable
    }

    fun onLevelsActive() {
        val scaleShadow = ObjectAnimator.ofFloat(shadow, "scaleY", 2f)
        transitionBackground?.startTransition(TRANSITION_DURATION)
        scaleShadow.start()
    }

    fun onLevelsInactive() {
        val scaleShadow = ObjectAnimator.ofFloat(shadow, "scaleY", 0.5f)
        transitionBackground?.reverseTransition(TRANSITION_DURATION)
        scaleShadow.start()
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