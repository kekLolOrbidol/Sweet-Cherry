package com.example.android.mines

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.android.mines.data.LinksPreferences
import com.example.android.mines.data.LinksManager
import com.example.android.mines.data.LinkApi
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoadingActivity : AppCompatActivity(), LinkApi, CoroutineScope {

    var prefResp : LinksPreferences? = null
    var job : Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        window.statusBarColor = Color.BLACK
        actionBar?.hide()
        val links = LinksManager(this)
        links.attachWeb(this)
        if (links.url != null) response(url = links.url!!)
        job = launch(Dispatchers.Main) {
            delay(5000)
            progress_bar.visibility = View.GONE
            next()
        }
    }

    fun next(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun response(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        job.cancel()
        customTabsIntent.launchUrl(this, Uri.parse(url))
        finish()
    }

}