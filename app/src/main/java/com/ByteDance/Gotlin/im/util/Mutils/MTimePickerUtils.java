package com.ByteDance.Gotlin.im.util.Mutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ByteDance.Gotlin.im.R;
import com.ByteDance.Gotlin.im.info.Data;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description：
 * @Author：Suzy.Mo
 * @Date：2022/6/24 21:55
 */
public class MTimePickerUtils {
    private TimePickerView timePickerView = null;
    private String mDate = null;
    private final String mTimeslot = null;
    private AlertDialog dialog = null;
    private List<Integer> hour;
    private List<Integer> min;
    private WheelView hour1;
    private WheelView hour2;
    private WheelView min1;
    private WheelView min2;
    public int h1;
    public int h2;
    public int m1;
    public int m2;

    /*
    获取日期 yyyy-MM-dd 年月日
    */
    public String getDateYMD(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /*
    生成格式化的日期 某一天的00:00:00
     */
    public Date getDateHMS(Date day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = getDateYMD(day) + " 00:00:00";
        Date data = null;
        try {
             data = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }


    /*
    初始化日期选择器
    */
    @SuppressLint("ResourceType")
    public TimePickerView initDatePicker(Context context, Activity activity, OnTimeSelectListener onTimeSelectListener){
        /*
        设置时间跨度
         */
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2022,5,16);
        endDate.set(2022,5,26);
        timePickerView = new TimePickerBuilder(context,onTimeSelectListener)
                //自定义布局
                .setLayoutRes(R.layout.m_picker_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView next = v.findViewById(R.id.timepicker_date_next);
                        ImageView cancel = v.findViewById(R.id.timepicker_date_cancel);
                        //点击下一步
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                                timePickerView.dismiss();
                            }
                        });
                        //点击取消
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });

                    }
                })
                .isDialog(true)//显示模式
                //.setTitleText("时间选择 • 日期")
                .setOutSideCancelable(false)//外部是否可以点击
                .setRangDate(startDate,endDate)//设置时间跨度
                //.setOutSideColor(activity.getResources().getColor(R.color.bg_default))//外部背景颜色
                //.setBgColor(activity.getResources().getColor(R.color.accent_bg_default))//背景颜色
                .setTextColorCenter(activity.getResources().getColor(R.color.dark_text_link))//选中字体颜色
                .setTextColorOut(activity.getResources().getColor(R.color.text_ultrastrong))//未选中字体颜色
                .setContentTextSize(16)
                .isCenterLabel(true)//只显示中央标签
                .setItemVisibleCount(5)//可见标签数
                .setDividerColor(Color.argb(0,0,0,0))
                .setType(new boolean[]{true,true,true,false,false,false})//是否显示年月日，时分秒
                .isAlphaGradient(true)//滚轮透明
                .build();

        /*
         *  设置dialog的宽度
         */
        Dialog timePickerDialog ;
        timePickerDialog = timePickerView.getDialog();
        Window window = timePickerDialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth() * 0.95);
        window.setAttributes(params);

        return timePickerView;
    }

    /*
     *  初始化时间段数据
     */
    private void initTimeslotData(){
        hour = new ArrayList<>();
        min = new ArrayList<>();

        for (int i = 0; i < 24; i++){
            hour.add(i);
        }
        for (int i = 0; i < 60; i++){
            min.add(i);
        }
    }
//    /*
//     *  初始化时间段选择器
//     */
//    public AlertDialog initTimeSlotDialog(Context context,Activity activity, View.OnClickListener onClickListener){
//        TextView tv_confirm;
//        ImageView iv_cancel;
//        initTimeslotData();
//        View view = LayoutInflater.from(context).inflate(R.layout.timepicker_timeslot,null,false);
//        dialog = new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setView(view)
//                .create();
//
//        tv_confirm = view.findViewById(R.id.timepicker_timeslot_confirm);
//        iv_cancel = view.findViewById(R.id.timepicker_timeslot_cancel);
//
//        tv_confirm.setOnClickListener(onClickListener);
//
//        iv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        hour1 = view.findViewById(R.id.options1);
//        min1 = view.findViewById(R.id.options2);
//        hour2 = view.findViewById(R.id.options3);
//        min2 = view.findViewById(R.id.options4);
//
//        hour1.setCyclic(false);
//        min1.setCyclic(false);
//        hour2.setCyclic(false);
//        min2.setCyclic(false);
//        hour1.setDividerColor(Color.argb(0,0,0,0));
//        min1.setDividerColor(Color.argb(0,0,0,0));
//        hour2.setDividerColor(Color.argb(0,0,0,0));
//        min2.setDividerColor(Color.argb(0,0,0,0));
//
//        hour1.setTextColorCenter(activity.getResources().getColor(R.color.timepicker_timeslot_center));
//        min1.setTextColorCenter(activity.getResources().getColor(R.color.timepicker_timeslot_center));
//        hour2.setTextColorCenter(activity.getResources().getColor(R.color.timepicker_timeslot_center));
//        min2.setTextColorCenter(activity.getResources().getColor(R.color.timepicker_timeslot_center));
//
//        hour1.setTextColorOut(activity.getResources().getColor(R.color.timepicker_timeslot_out));
//        min1.setTextColorOut(activity.getResources().getColor(R.color.timepicker_timeslot_out));
//        hour2.setTextColorOut(activity.getResources().getColor(R.color.timepicker_timeslot_out));
//        min2.setTextColorOut(activity.getResources().getColor(R.color.timepicker_timeslot_out));
//
//        hour1.setTextSize(16);
//        min1.setTextSize(16);
//        hour2.setTextSize(16);
//        min2.setTextSize(16);
//
//        hour1.setAlphaGradient(true);
//        min1.setAlphaGradient(true);
//        hour2.setAlphaGradient(true);
//        min2.setAlphaGradient(true);
//
//        hour1.setAdapter(new ArrayWheelAdapter(hour));
//        min1.setAdapter(new ArrayWheelAdapter(min));
//        hour2.setAdapter(new ArrayWheelAdapter(hour));
//        min2.setAdapter(new ArrayWheelAdapter(min));
//
//        hour1.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                h1 = hour.get(index);
//            }
//        });
//
//        min1.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                m1 = min.get(index);
//            }
//        });
//
//        hour2.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                h2 = hour.get(index);
//            }
//        });
//
//        min2.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                m2 = min.get(index);
//            }
//        });
//
//        return dialog;
//    }

    /*
     *   获取完整的起始时间和终止时间
     */
    public String getTime(){
        StringBuffer buffer = new StringBuffer();
        if (h1 < 10){
            buffer.append(mDate + "_" + "0" + h1);
        }else {
            buffer.append(mDate + "_" + h1);
        }

        if (m1 < 10){
            buffer.append(":" + "0" + m1 + ":00");
        }else {
            buffer.append(":" + m1 + ":00");
        }

        buffer.append("/");

        if (h2 < 10){
            buffer.append(mDate + "_" + "0" + h2);
        }else {
            buffer.append(mDate + "_" + h2);
        }

        if (m2 < 10){
            buffer.append(":" + "0" + m2 + ":00");
        }else {
            buffer.append(":" + m2 + ":00");
        }
        return buffer.toString();
    }

    /*
     * 获取时间段
     */
    public String getTimeslot(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("" + h1);

        if (m1 < 10){
            buffer.append("：" + "0" + m1);
        }else {
            buffer.append("：" + m1);
        }

        buffer.append(" - ");
        buffer.append("" + h2);
        if (m2 < 10){
            buffer.append("：" + "0" + m2);
        }else {
            buffer.append("：" + m2);
        }
        return buffer.toString();
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void reSetTime(){
        hour1.setCurrentItem(h1);
        hour2.setCurrentItem(h2);
        min1.setCurrentItem(m1);
        min2.setCurrentItem(m2);
    }

}

