package com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ByteDance.Gotlin.im.R
import com.ByteDance.Gotlin.im.util.Constants
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnMediaEditInterceptListener
import com.luck.picture.lib.utils.DateUtils
import com.luck.picture.lib.utils.StyleUtils
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 19:36
 * @Email 762795632@qq.com
 * @Description
 */

class TMyEditMediaIListener(
    private val outputCropPath: String,
    private val context: Context,
    private val code: Int
) : OnMediaEditInterceptListener {
    override fun onStartMediaEdit(
        fragment: Fragment?,
        currentLocalMedia: LocalMedia?,
        requestCode: Int
    ) {
        val currentEditPath = currentLocalMedia!!.availablePath
        val inputUri =
            if (PictureMimeType.isContent(currentEditPath)) Uri.parse(currentEditPath) else Uri.fromFile(
                File(currentEditPath)
            )
        val destinationUri = Uri.fromFile(
            File(outputCropPath, DateUtils.getCreateFileName("CROP_") + ".jpeg")
        )
        val uCrop = UCrop.of<Any>(inputUri, destinationUri)
        var options = buildOptions(context, code)
        options.setHideBottomControls(false)
        uCrop.withOptions(options)
        uCrop.startEdit(fragment!!.requireActivity(), fragment, requestCode)
    }

    private fun buildOptions(context: Context, code: Int): UCrop.Options {
        val options = UCrop.Options()
        if (code == Constants.DEFAULT_TYPE) {
            //普通裁剪
            options.setCircleDimmedLayer(false)
        } else if (code == Constants.CIRCLE_TYPE) {
            //头像裁剪
            options.setCircleDimmedLayer(true)
        }
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(true)
        options.setShowCropFrame(true)
        options.setShowCropGrid(true)
        options.withAspectRatio(-1f, -1f)
        options.isCropDragSmoothToCenter(false)
        options.isUseCustomLoaderBitmap(true)
        options.isForbidCropGifWebp(true)
        options.isForbidSkipMultipleCrop(false)
        options.setMaxScaleMultiplier(100f)
        if (PictureSelectionConfig.selectorStyle != null && PictureSelectionConfig.selectorStyle.selectMainStyle.statusBarColor != 0) {
            val mainStyle = PictureSelectionConfig.selectorStyle.selectMainStyle
            val isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack
            val statusBarColor = mainStyle.statusBarColor
            options.isDarkStatusBarBlack(isDarkStatusBarBlack)
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor)
                options.setToolbarColor(statusBarColor)
            } else {
                options.setStatusBarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
                options.setToolbarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            }
            val titleBarStyle = PictureSelectionConfig.selectorStyle.titleBarStyle
            if (StyleUtils.checkStyleValidity(titleBarStyle.titleTextColor)) {
                options.setToolbarWidgetColor(titleBarStyle.titleTextColor)
            } else {
                options.setToolbarWidgetColor(
                    ContextCompat.getColor(
                        context,
                        R.color.ps_color_white
                    )
                )
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            options.setToolbarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.ps_color_white))
        }
        return options
    }
}