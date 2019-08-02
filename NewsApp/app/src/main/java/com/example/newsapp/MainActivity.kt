package com.example.newsapp

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import com.example.newsapp.Networking.ApiClient
import com.example.newsapp.Networking.ApiInterface
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val apiKey: String = "485ea4701ad8431cb56fe3b4d587c745"
    var articles: ArrayList<Article> = ArrayList()

    private lateinit var adapter: Adapter
    private lateinit var viewManger: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)

        viewManger = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManger
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false

        onLoadRefresh("")


    }

    private fun loadJson(keyword: String) {
        swipeRefresh.isRefreshing = true

        val apiInterface: ApiInterface? = ApiClient.getApiClient?.create(ApiInterface::class.java)

        val utils = Utils()

        val country: String = utils.getCountry()
        val language: String = utils.getLanguage()

        val call: Call<News>?

        call = if (keyword.length < 0) {
            apiInterface?.getNewsSearch(keyword, language, "publishedAt", apiKey)
        } else {
            apiInterface?.getNews(country, apiKey)
        }


        call?.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                headlines.visibility = View.INVISIBLE
                swipeRefresh.isRefreshing = false
                Toast.makeText(this@MainActivity, "No Result", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                if (response!!.isSuccessful && response.body().article != null) {
                    if (articles.isNotEmpty()) {
                    }

                    articles = (response.body().article as ArrayList<Article>?)!!
                    adapter = Adapter(this@MainActivity, articles)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()




                    headlines.visibility = View.VISIBLE
                    swipeRefresh.isRefreshing = false

                }
            }
        })
    }

    /*fun initListener() {
        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                super.onItemClick(view, position)

                val intent = Intent(this@MainActivity,NewsDetail::class.java)

                val article: Article = articles[position]
                intent.putExtra("url",article.url)
                intent.putExtra("title",article.title)
                intent.putExtra("img",article.urlToImage)
                intent.putExtra("date",article.publishAt)
                intent.putExtra("source",article.source?.name)
                intent.putExtra("author",article.author)


                val pair: Pair<View,String> = Pair(img,ViewCompat.getTransitionName(img))
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,pair)

                startActivity(intent,optionsCompat.toBundle())

            }
        })

    }*/


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchManger: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu?.findItem(R.id.search_bar)?.actionView as SearchView
        val menuItem: MenuItem = menu.findItem(R.id.search_bar)

        searchView.setSearchableInfo(searchManger.getSearchableInfo(componentName))
        searchView.queryHint = "Search News..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.length!! > 2) {
                    loadJson(query)
                } else {
                    Toast.makeText(this@MainActivity, "Type more than two letters", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        menuItem.icon.setVisible(false, false)

        return true
    }

    override fun onRefresh() {
        loadJson("")
    }

    private fun onLoadRefresh(keyword: String) {
        swipeRefresh.post {
            Runnable {
                loadJson(keyword)
            }
        }
    }
}
