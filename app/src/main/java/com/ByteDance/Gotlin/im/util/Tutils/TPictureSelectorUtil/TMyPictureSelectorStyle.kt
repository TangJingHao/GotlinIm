package com.ByteDance.Gotlin.im.util.Tutils.TPictureSelectorUtil

import android.content.Context
import androidx.core.content.ContextCompat
import com.ByteDance.Gotlin.im.R
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.luck.picture.lib.utils.DensityUtil

/**
 * @Author 唐靖豪
 * @Date 2022/6/10 19:49
 * @Email 762795632@qq.com
 * @Description
 */

object TMyPictureSelectorStyle {
    /**
     * 获取选择器风格
     */
    fun getSelectorStyle(context: Context): PictureSelectorStyle {
        val pictureSelectorStyle = PictureSelectorStyle()
        // 主体风格
        val numberSelectMainStyle = SelectMainStyle()
        numberSelectMainStyle.isSelectNumberStyle = true
        numberSelectMainStyle.isPreviewSelectNumberStyle = false
        numberSelectMainStyle.isPreviewDisplaySelectGallery = true
        numberSelectMainStyle.selectBackground = R.drawable.t_ps_default_num_selector
        numberSelectMainStyle.previewSelectBackground = R.drawable.t_ps_preview_checkbox_selector
        numberSelectMainStyle.selectNormalBackgroundResources =
            R.drawable.t_ps_select_complete_normal_bg
        numberSelectMainStyle.selectNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_53575e)
        numberSelectMainStyle.selectNormalText = "完成"
        numberSelectMainStyle.adapterPreviewGalleryBackgroundResource =
            R.drawable.t_ps_preview_gallery_bg
        numberSelectMainStyle.adapterPreviewGalleryItemSize =
            DensityUtil.dip2px(context, 52f)
        numberSelectMainStyle.previewSelectText = "选择"
        numberSelectMainStyle.previewSelectTextSize = 14
        numberSelectMainStyle.previewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        numberSelectMainStyle.previewSelectMarginRight =
            DensityUtil.dip2px(context, 6f)
        numberSelectMainStyle.selectBackgroundResources = R.drawable.t_ps_select_complete_bg
        numberSelectMainStyle.selectText = "完成(%1\$d/%2\$d)"
        numberSelectMainStyle.selectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        numberSelectMainStyle.mainListBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_black)
        numberSelectMainStyle.isCompleteSelectRelativeTop = true
        numberSelectMainStyle.isPreviewSelectRelativeBottom = true
        numberSelectMainStyle.isAdapterItemIncludeEdge = false
        // 头部TitleBar 风格
        val numberTitleBarStyle = TitleBarStyle()
        numberTitleBarStyle.isHideCancelButton = true
        numberTitleBarStyle.isAlbumTitleRelativeLeft = true
        numberTitleBarStyle.titleBarHeight = 90
        numberTitleBarStyle.titleAlbumBackgroundResource = R.drawable.t_ps_album_bg
        numberTitleBarStyle.titleDrawableRightResource = R.drawable.t_ps_ic_grey_arrow
        numberTitleBarStyle.previewTitleLeftBackResource = R.drawable.t_ps_ic_normal_back
        // 底部NavBar 风格
        val numberBottomNavBarStyle = BottomNavBarStyle()
        numberBottomNavBarStyle.bottomPreviewNarBarBackgroundColor =
            ContextCompat.getColor(context, R.color.ps_color_half_grey)
        numberBottomNavBarStyle.bottomPreviewNormalText = "预览"
        numberBottomNavBarStyle.bottomPreviewNormalTextColor =
            ContextCompat.getColor(context, R.color.ps_color_9b)
        numberBottomNavBarStyle.bottomPreviewNormalTextSize = 16
        numberBottomNavBarStyle.isCompleteCountTips = false
        numberBottomNavBarStyle.bottomPreviewSelectText = "预览(%1\$d)"
        numberBottomNavBarStyle.bottomPreviewSelectTextColor =
            ContextCompat.getColor(context, R.color.ps_color_white)
        pictureSelectorStyle.titleBarStyle = numberTitleBarStyle
        pictureSelectorStyle.bottomBarStyle = numberBottomNavBarStyle
        pictureSelectorStyle.selectMainStyle = numberSelectMainStyle;
        return pictureSelectorStyle
    }
}