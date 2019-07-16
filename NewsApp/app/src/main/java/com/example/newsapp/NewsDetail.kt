package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.design.widget.AppBarLayout
import android.view.View
import android.webkit.WebViewClient
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlin.math.abs

abstract class NewsDetail : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener{

    private val mUrl : String? = null
    private var isHideToolbarView : Boolean = false
    abstract val titleAppbar : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
    }

    fun inWebView(url : String){
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId

        if(id==R.id.webView){
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(mUrl)
            startActivity(i)
            return true
        }

        return true
    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        val maxScroll : Int = p0!!.totalScrollRange
        val percentage : Float = abs(p1).toFloat() / maxScroll

        if(percentage == 1f && isHideToolbarView){
            date_behavior.visibility = View.GONE
            titleAppbar.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        }
        else if (percentage < 1f && !isHideToolbarView){
            date_behavior.visibility = View.VISIBLE
            titleAppbar.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }
}
