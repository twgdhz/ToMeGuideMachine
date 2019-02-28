package com.guidemachine.ui.superadmin.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.guidemachine.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作为dialog弹出日历选择控件
 * https://blog.csdn.net/sinstar1/article/details/53149121
 */
public class CalendarViewFragment extends DialogFragment implements OnDateSelectedListener {
    DialogFragment mContext = this;

    private View view;
    MaterialCalendarView mcv;
    //接收从Activity传来的数据
    Bundle timeBundle;

    OnFrgDataListener IListener;


    public CalendarViewFragment() {
        // Required empty public constructor
    }

    public void setIListener(OnFrgDataListener IListener) {
        this.IListener = IListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        mcv = (MaterialCalendarView) view.findViewById(R.id.mcv);
        timeBundle = getArguments();
        initData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //下面这些都是为了让dialog宽度全充满
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        // params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

        /*if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
        }*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这个主题 背景全透明 没有半透明
        // setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    public void initData() {
        // 显示兴起补全的整个礼拜的上个月或者下个月的日期 一般会多出一行整个礼拜
        // 点击补全出来的另外一个月的信息 可以直接跳到那个月
        mcv.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // 设置日历默认的时间为当前的时间
        mcv.setSelectedDate(new Date());

        // 日历的主要设置
        mcv.state().edit()
                // 设置你的日历 第一天是周一还是周一
                .setFirstDayOfWeek(Calendar.SUNDAY)
                // 设置你的日历的最小的月份  月份为你设置的最小月份的下个月 比如 你设置最小为1月 其实你只能滑到2月
                .setMinimumDate(CalendarDay.from(2010, 1, 1))
                // 同最小 设置最大
                .setMaximumDate(CalendarDay.from(2020, 1, 1))
                // 设置你的日历的模式  是按月 还是按周
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

// 设置你选中日期的背景底色
        mcv.setSelectionColor(getResources().getColor(R.color.text_color44));
//        mcv.setSelectionColor(0xff4285f4);
        mcv.setOnDateChangedListener(this);

    }

    /**
     * 日期选择 回调函数
     *
     * @param widget
     * @param date
     * @param selected
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//        Toast.makeText(getContext(), getSelectedDatesString(), Toast.LENGTH_SHORT).show();
        IListener.progress(mcv.getSelectedDate().getDate());

        mContext.dismiss();


    }

    /**
     * 将日期转换为字符串
     *
     * @return
     */
    private String getSelectedDatesString() {
        CalendarDay date = mcv.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        Log.i("sinstar", "getSelectedDatesString: " + date.toString());
        return FORMATTER.format(date.getDate());
    }

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();


    /**
     * 定义接口，向Activity传递数据
     */
    public interface OnFrgDataListener {
        public void progress(Date date);
    }

    /**
     * 注入实例方法
     *
     * @param
     */
   /* public void setOnFrgDataListener(OnFrgDataListener listener){
        IListener=listener;
    }*/

}