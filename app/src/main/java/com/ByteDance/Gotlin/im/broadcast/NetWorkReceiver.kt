package com.ByteDance.Gotlin.im.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast

/**
 * @Author 唐靖豪
 * @Date 2022/6/25 18:50
 * @Email 762795632@qq.com
 * @Description
 */

class NetWorkReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            var networkInfo = manager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isAvailable) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {//WIFI
                    Toast.makeText(context, "连接上WIFI", Toast.LENGTH_SHORT).show()
                    //TODO:需要网络波动监听的同事在这里写
                } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {//移动数据
                    Toast.makeText(context, "连接上移动数据", Toast.LENGTH_SHORT).show()
                    //TODO:需要网络波动监听的同事在这里写
                }
            } else {
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show()
                //TODO:需要网络波动监听的同事在这里写
            }
        } else {
            var network = manager.activeNetwork
            if (network != null) {
                var nc = manager.getNetworkCapabilities(network)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {//WIFI
                        Toast.makeText(context, "连接上WIFI", Toast.LENGTH_SHORT).show()
                        //TODO:需要网络波动监听的同事在这里写
                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {//移动数据
                        Toast.makeText(context, "连接上移动数据", Toast.LENGTH_SHORT).show()
                        //TODO:需要网络波动监听的同事在这里写
                    }
                } else {
                    //不用特殊处理
                }
            } else {
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show()
                //TODO:需要网络波动监听的同事在这里写
            }
        }
    }
}