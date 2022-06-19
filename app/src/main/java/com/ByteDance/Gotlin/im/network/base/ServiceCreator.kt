package com.ByteDance.Gotlin.im.network.base

import android.util.Log
import com.ByteDance.Gotlin.im.application.BaseApp
import com.ByteDance.Gotlin.im.util.Constants
import com.ByteDance.Gotlin.im.util.DUtils.DLogUtils
import com.ByteDance.Gotlin.im.util.DUtils.TimeUtils
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @Author 唐靖豪
 * @Date 2022/6/9 21:04
 * @Email 762795632@qq.com
 * @Description
 */

object ServiceCreator {

    // 缓存头
    private const val CACHE_CONTROL = "Cache-Control"

    // 缓存时间
    private const val MAX_AGE = 60
    private const val STR_MAX_AGE = "max-age=$MAX_AGE"

    // 缓存大小100MB
    private const val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong()

    //缓存地址
    private val cacheFile: String = BaseApp.getContext().getCacheDir().toString() + "/http_cache"
    private val cache = Cache(File(cacheFile), SIZE_OF_CACHE)

    private val customLogInterceptor = CustomLogInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(customLogInterceptor)  // 添加了日志拦截器，不用可以注释掉
        .addNetworkInterceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder() // http1.0 的 pragma 头
                .removeHeader("pragma") // 缓存头 Cache-Control 以及缓存时间 max-age= 60
                .addHeader(CACHE_CONTROL, STR_MAX_AGE)
                .build()
        }
        .cache(cache)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 用于webSocket
    var WebSocketClient = OkHttpClient.Builder()
        .pingInterval(30,TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //指定对象类型关键字reified让泛型实例化可以获得相应的class文件
    //泛型实例化关键字（inline和reified）
    inline fun <reified T> create(): T = create(T::class.java)
}

class CustomLogInterceptor : Interceptor {

    val TAG = "OkHttp日志拦截器"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestLog = generateRequestLog(request)
        val response = chain.proceed(request)
        val responseLog = generateResponseLog(response)
        DLogUtils.w(TAG, requestLog.plus(responseLog))
        return response
    }

    private fun generateResponseLog(response: Response?): String {
        if (response == null) {
            return ""
        }
        return "Response Time-->：${
            TimeUtils.getDateToString(System.currentTimeMillis())
        } \r\n Response Result ${
            if (response.code != 200)
                response.code
            else
                ""
        } -->：${
            getResponseText(response)
        }"
    }

    private fun generateRequestLog(request: Request?): String {
        if (request == null) {
            return ""
        }
        val requestParams = getRequestParams(request)
        val needPrintRequestParams = requestParams.contains("IsFile").not()
        return "自定义日志打印 \r\n Request Time-->：${
            TimeUtils.getDateToString(System.currentTimeMillis())
        } \r\n Request Url-->：${request.method} ${request.url} \r\n Request Header-->：${
            getRequestHeaders(
                request
            )
        } \r\n Request Parameters-->：${
            if (needPrintRequestParams)
                requestParams
            else
                "文件上传，不打印请求参数"
        } \r\n "
    }

    @Deprecated("unused")
    private fun printInfo(request: Request?, response: Response?) {
        if (request != null && response != null) {
            val requestParams = getRequestParams(request)
            val needPrintRequestParams = requestParams.contains("IsFile").not()
            val logInfo =
                "自定义日志打印 \r\n Request Url-->：${request.method} ${request.url} \r\n Request Header-->：${
                    getRequestHeaders(
                        request
                    )
                } \r\n Request Parameters-->：${
                    if (needPrintRequestParams)
                        requestParams
                    else
                        "文件上传，不打印请求参数"
                } \r\n Response Result ${
                    if (response.code != 200)
                        response.code
                    else
                        ""
                } -->：${
                    getResponseText(response)
                }"
            DLogUtils.w(TAG, logInfo)
        }
    }

    /**
     * 获取请求参数
     */
    private fun getRequestParams(request: Request): String {
        var str: String? = null
        try {
            request.body?.let {
                val buffer = Buffer()
                it.writeTo(buffer)
                val charset = it.contentType()?.charset(Charset.forName("UTF-8"))
                    ?: Charset.forName("UTF-8")
                str = buffer.readString(charset)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (str.isNullOrEmpty()) "Empty!" else str!!
    }

    private fun getRequestHeaders(request: Request): String {
        val headers = request.headers
        return if (headers.size > 0) {
            headers.toString()
        } else {
            "Empty!"
        }
    }

    /**
     * 获取返回数据字符串
     */
    private fun getResponseText(response: Response): String {
        try {
            response.body?.let {
                val source = it.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val charset = it.contentType()?.charset(Charset.forName("UTF-8"))
                    ?: Charset.forName("UTF-8")
                if (it.contentLength().toInt() != 0) {
                    buffer.clone().readString(charset).let { result ->
                        return result
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "Empty!"
    }
}
