package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.design.widget.AppBarLayout
import android.view.View
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlin.math.abs

class NewsDetail : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private var mUrl: String? = null
    private var mImg: String? = null
    private var mTitle: String? = null
    private var mDate: String? = null
    private var mSource: String? = null
    private var mAuthor: String? = null
    private var isHideToolbarView: Boolean = false

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)


        collapsing_toolbar.title = ""


        val i: Intent = intent
        mUrl = i.getStringExtra("url")
        mImg = i.getStringExtra("img")
        mTitle = i.getStringExtra("title")
        mDate = i.getStringExtra("date")
        mSource = i.getStringExtra("source")
        mAuthor = i.getStringExtra("author")

        val utils = Utils()
        val requestOptions = RequestOptions()
        requestOptions.error(utils.getRandomColor())

        Glide.with(this)
            .load(mImg)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(backdrop)

        title_on_appbar.text = mSource
        subtitle_on_appbar.text = mUrl
        date.text = mDate

        val author: String?

        author = if (mAuthor != null) {
            "\u2022" + mAuthor
        } else {
            ""
        }

        time.text = mSource + author + "\u2022" + utils.dateFormat(mDate.toString())

        inWebView(mUrl.toString())
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun inWebView(url: String) {
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
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId

        if (id == R.id.webView) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(mUrl)
            startActivity(i)
            return true
        }

        return true
    }

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        val maxScroll: Int = p0!!.totalScrollRange
        val percentage: Float = abs(p1).toFloat() / maxScroll

        if (percentage == 1f && isHideToolbarView) {
            date_behavior.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        } else if (percentage < 1f && !isHideToolbarView) {
            date_behavior.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        }
    }
}
