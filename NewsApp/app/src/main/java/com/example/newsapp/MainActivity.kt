package com.example.newsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.newsapp.Networking.ApiClient
import com.example.newsapp.Networking.ApiInterface
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() ,SwipeRefreshLayout.OnRefreshListener {

    private val apiKey : String  = "485ea4701ad8431cb56fe3b4d587c745"
    var article : List<Article> = ArrayList()

    private lateinit var adapter: Adapter
    private lateinit var viewManger : RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)

        viewManger = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManger
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false

        loadJson()

    }

    private fun loadJson(){

        swipeRefresh.isRefreshing = true

        val apiInterface : ApiInterface? = ApiClient.getApiClient?.create(ApiInterface::class.java)

        val utils = Utils()

        val country : String = utils.getCountry()
        val call : Call<News>
        call = apiInterface!!.getNews(country,apiKey)

        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                Toast.makeText(this@MainActivity,"No Result",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                if(response!!.isSuccessful && response.body().article !=null){
                    if(article.isNotEmpty()){
                    }

                    article = response.body().article!!
                    adapter = Adapter(this@MainActivity, article as ArrayList<Article>)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onRefresh() {

    }
}
