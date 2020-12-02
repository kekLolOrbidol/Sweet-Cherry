package com.example.android.mines.data

import android.content.Context
import android.util.Log
import com.example.push.PushText
import com.facebook.applinks.AppLinkData

class LinksManager(val context: Context) {
    var url : String? = null
    var mainActivity : LinkApi? = null
    var exec = false
    private val prefUrl = LinksPreferences(context).apply { getSharedPref("fb") }

    init{
        url = prefUrl.getStroke("url")
        Log.e("Links", url.toString())
        if(url == null) oak()
    }

    fun attachWeb(api : LinkApi){
        mainActivity = api
    }

    private fun oak() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Log.e("DEEP", "SRABOTAL")
                    PushText().schedulePush(context)
                    exec = true
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        prefUrl.putStroke("url", url!!)
                        mainActivity?.response(url!!)
                    }
                }
            }
        }
    }}