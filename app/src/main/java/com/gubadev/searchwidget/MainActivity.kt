package com.gubadev.searchwidget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search.searchListener = object : SearchWidget.SearchListener{
            override fun onSearchClicked() {
                MainActivity@search.onSearchActive()
            }
        }

        search.searchCloseListener = object : SearchWidget.CloseSearchListener{
            override fun onSearchClicked() {
                MainActivity@search.onSearchInactive()
            }
        }

        search.levelsListener = object : SearchWidget.LevelsListener{
            override fun onLevelClicked() {
                MainActivity@search.onLevelsActive()
            }
        }

        new_search.searchListener = object : SearchWdgetNew.SearchListener{
            override fun onSearchClicked() {
                MainActivity@new_search.onSearchActive()
            }
        }

        new_search.searchCloseListener = object : SearchWdgetNew.CloseSearchListener{
            override fun onSearchClicked() {
                MainActivity@new_search.onSearchInactive()
            }
        }

        new_search.levelsListener = object : SearchWdgetNew.LevelsListener{
            override fun onLevelClicked() {
                MainActivity@new_search.onLevelsActive()
            }
        }


    }
}
