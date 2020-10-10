package com.cyz2.kotlinapitest

import com.cyz2.kotlinapitest.PuffHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*

val json: String = ""

class SaveAndShareActivity {

//    suspend fun MTTemplateDetail.upload(): Boolean {
//        val imageTemplateDetailEn = Gson().fromJson(json, ImageTemplateDetailEn::class.java)
//        val paths = imageTemplateDetailEn.local2uploadPaths()
//        return uploadFileAndSaveFormula(paths, imageTemplateDetailEn)
//    }
//}
//
//private suspend fun uploadFileAndSaveFormula(
//    paths: Set<String>,
//    imageTemplateDetailEn: ImageTemplateDetailEn
//): Boolean = withContext(Dispatchers.IO) {
//    // ios的文件路径为uuid
//    val defferred = paths.map { path ->
//        GlobalScope.async {
//            // 传效果图和贴纸[Sticker.originalFullPath] 注意区分马赛克、涂鸦笔、自定义贴纸
////            Pair(path, PuffHelper.createPuffCall(path).execute().first)
//        }
//    }
//
//    val allResponse = defferred.map { it?.await() }
//
//    //是否成功，只要有失败的就会认为失败,TODO 重试中已经成功的部分可以优化
//    val isFail = allResponse.any { !it?.second.isSuccess }
//
//    if (!isFail) {
//        val imageData = JsonObject()
//        // 文件路径和key对应
//        allResponse.forEach {
//            imageData.addProperty(it.first, it.second.response.toString())
//        }
//        imageTemplateDetailEn.configure.steps.forEach {
//            if (it is Sticker) {
//                it.originalFullPath = null
//            }
//        }
//
//        val configure = Gson().toJson(imageTemplateDetailEn)
//        val response =
//            TemplateCommunityRetrofit.api.createEffect(configure, 2, imageData.toString()).execute()
//        return@withContext response?.isSuccessful == true
//    } else {
//        return@withContext false
//    }
//
//}
//
//
//class ImageTemplateDetailEn {
//    fun local2uploadPaths(): MutableSet<String> {
//        return "path"
//    }
//}
//
//class MTTemplateDetail {

}