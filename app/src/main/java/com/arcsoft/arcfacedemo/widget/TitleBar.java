package com.arcsoft.arcfacedemo.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * <p>自定义标题栏</p>
 * <p>创建人：Coder-LJ</p>
 * <p>创建时间：2016年8月28日19:50:29</p>
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {
    public static final int DEFAULT_MAIN_TEXT_SIZE = 20;// 主标题字体大小
    public static final int DEFAULT_SUB_TEXT_SIZE = 14;// 副标题字体大小
    public static final int DEFAULT_ACTION_TEXT_SIZE = 15;// 动作按钮字体大小
    public static final int DEFAULT_ACTION_IMG_SIZE = 20;// 动作按钮图片大小
    public static final int DEFAULT_TITLEBAR_HEIGHT = 48;// 标题栏高度
    public static final int DEFAULT_TOPLINE_HEIGHT = 1;// 分割线高度
    public static final int DEFAULT_BOTTOMLINE_HEIGHT = 1;// 分割线高度
    private Context context;
    private View StatusBar;
    private View TopLine;
    private LinearLayout LeftLayout;
    private LinearLayout CenterLayout;
    private MarqueeTextView CenterText;
    private MarqueeTextView SubTitleText;
    private View CustomCenterView;
    private LinearLayout RightLayout;
    private View BottomLine;

    private int ScreenWidth;// 屏幕宽度
    private int ScreenHeight;// 屏幕高度
    private int StatusBarColor = 0;// 状态栏背景
    private int StatusBarHeight = 0;// 状态栏高度
    private int ActionPadding = dpTopx(4);// 动作按钮内边距
    private int OutPadding = dpTopx(8);// 动作按钮外边距
    private int TitleTextColor = Color.WHITE;// 标题文本字体颜色
    private int ActionTextColor = Color.WHITE;// 动作按钮字体颜色
    private int TitleBarHeight = dpTopx(DEFAULT_TITLEBAR_HEIGHT);// 标题栏高度
    private int TopLineHeight = dpTopx(DEFAULT_TOPLINE_HEIGHT);// 顶部分割线高度
    private int BottomLineHeight = dpTopx(DEFAULT_BOTTOMLINE_HEIGHT);// 底部分割线高度
    private int ParentHeight;// 全局高度

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * dp&px单位转换
     *
     * @param dpValue
     * @return
     */
    public static int dpTopx(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    private int getStatusBarHeight(Context context) {
        try {
            Object obj = Class.forName("com.android.internal.R$dimen").newInstance();
            Field field = Class.forName("com.android.internal.R$dimen").getField("status_bar_height");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void init(Context context) {
        this.context = context;
        ScreenWidth = getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = getResources().getDisplayMetrics().heightPixels;
        ParentHeight = TitleBarHeight + StatusBarHeight + TopLineHeight + BottomLineHeight;
        initView(context);
    }

    private void initView(Context context) {
        StatusBar = new View(context);
        StatusBarHeight = getStatusBarHeight(context);
        StatusBar.setBackgroundColor(Color.TRANSPARENT);

        TopLine = new View(context);
        TopLine.setBackgroundColor(Color.TRANSPARENT);

        LeftLayout = new LinearLayout(context);
        LeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        LeftLayout.setPadding(OutPadding, 0, OutPadding, 0);
        LeftLayout.setBackgroundColor(Color.TRANSPARENT);

        CenterText = new MarqueeTextView(context);
        CenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        CenterText.setSingleLine();
        CenterText.setGravity(Gravity.CENTER);
        CenterText.setTextColor(TitleTextColor);
        CenterText.setBackgroundColor(Color.TRANSPARENT);
        CenterText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        SubTitleText = new MarqueeTextView(context);
        SubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        SubTitleText.setSingleLine();
        SubTitleText.setGravity(Gravity.CENTER);
        SubTitleText.setTextColor(TitleTextColor);
        SubTitleText.setBackgroundColor(Color.TRANSPARENT);
        SubTitleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        CenterLayout = new LinearLayout(context);
        CenterLayout.addView(CenterText);
        CenterLayout.addView(SubTitleText);
        CenterLayout.setGravity(Gravity.CENTER);
        CenterLayout.setBackgroundColor(Color.TRANSPARENT);

        RightLayout = new LinearLayout(context);
        RightLayout.setGravity(Gravity.CENTER_VERTICAL);
        RightLayout.setPadding(OutPadding, 0, OutPadding, 0);
        RightLayout.setBackgroundColor(Color.TRANSPARENT);

        BottomLine = new View(context);
        BottomLine.setBackgroundColor(Color.TRANSPARENT);

        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusBarColor = ((Activity) context).getWindow().getStatusBarColor();
            }
            setImmersive((Activity) context, false);
        }

        addView(StatusBar, new LayoutParams(LayoutParams.MATCH_PARENT, StatusBarHeight));
        addView(TopLine, new LayoutParams(LayoutParams.MATCH_PARENT, TopLineHeight));
        addView(LeftLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(CenterLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(RightLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, TitleBarHeight));
        addView(BottomLine, new LayoutParams(LayoutParams.MATCH_PARENT, BottomLineHeight));
    }

    /**
     * ======================================== 状态栏 ========================================
     */
    public TitleBar setImmersive(Activity activity, boolean immersive) {// 设置系统状态栏是否可见，安卓系统版本大于等于19
        if (immersive) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                StatusBarHeight = 0;
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                StatusBarHeight = getStatusBarHeight(context);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                StatusBarHeight = 0;
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                StatusBarHeight = getStatusBarHeight(context);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    activity.getWindow().setStatusBarColor(StatusBarColor);
                }
            }
        }
        onRefresh();
        return this;
    }

    public TitleBar setStatusBarBackground(int statusBarBackground) {
        try {
            StatusBar.setBackgroundResource(statusBarBackground);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * ======================================== 顶部分割线 ========================================
     */
    public TitleBar setTopLineHeight(int dividerHeight) {// 顶部分割线高度
        if (dividerHeight >= 0) {
            TopLineHeight = dpTopx(dividerHeight);
            TopLine.getLayoutParams().height = TopLineHeight;
        } else {
            Log.e("MyTitleBar", " TopLineHeight 设置无效");
        }
        onRefresh();
        return this;
    }

    public TitleBar setTopLineBackground(int backgroundresId) {// 顶部分割线背景
        try {
            TopLine.setBackgroundResource(backgroundresId);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * ======================================== 标题栏 ========================================
     */
    public TitleBar setTitleBarHeight(int height) {// 标题栏高度
        if (height >= 0) {
            TitleBarHeight = dpTopx(height);
        } else {
            TitleBarHeight = dpTopx(DEFAULT_TITLEBAR_HEIGHT);
            Log.e("MyTitleBar", " TitleBarHeight 设置无效，恢复为默认高度");
        }
        onRefresh();
        return this;
    }

    public TitleBar setTitleBarBackground(int resID) {// 标题栏背景
        try {
            setBackgroundResource(resID);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * ======================================== 标题栏 左侧按钮 ========================================
     */
    public void addLeftActions(ActionList actionList) {// 新增多个动作按钮
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addLeftAction(actionList.get(i));
        }
    }

    public View addLeftAction(Action action) {// 新增单个动作按钮
        final int index = LeftLayout.getChildCount();
        return addLeftAction(action, index);
    }

    public View addLeftAction(Action action, int index) {// 指定位置新增动作按钮
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        LeftLayout.addView(view, index, params);
        return view;
    }

    public void removeAllLeftActions() {// 移除全部动作按钮
        LeftLayout.removeAllViews();
    }

    public void removeLeftActionAt(int index) {// 移除指定位置动作按钮
        LeftLayout.removeViewAt(index);
    }

    public void removeLeftAction(Action action) {// 移除单个动作按钮
        int childCount = LeftLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = LeftLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    LeftLayout.removeView(view);
                }
            }
        }
    }

    public int getLeftActionCount() {// 获取动作按钮数量
        return LeftLayout.getChildCount();
    }

    /**
     * ======================================== 标题栏 —— 标题内容 ========================================
     */
    @SuppressLint("SetTextI18n")
    public TitleBar setTitle(CharSequence title) {// 标题内容设置（主标题与副标题用"\n"或"\t"分割）
        CenterText.setVisibility(View.VISIBLE);
        SubTitleText.setVisibility(View.VISIBLE);
        CenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        SubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            CenterLayout.setOrientation(LinearLayout.VERTICAL);
            CenterText.setText(title.subSequence(0, index));
            SubTitleText.setText(title.subSequence(index + 1, title.length()));
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                CenterLayout.setOrientation(LinearLayout.HORIZONTAL);
                CenterText.setText(title.subSequence(0, index));
                SubTitleText.setText("" + title.subSequence(index + 1, title.length()));
            } else {
                CenterText.setText(title);
                CenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
                SubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public MarqueeTextView getCenterTextView() {
        return CenterText;
    }

    public MarqueeTextView getSubTitleTextView() {
        return SubTitleText;
    }

    public String getCenterText() {
        return CenterText.getText().toString();
    }

    public String getSubTitleText() {
        return SubTitleText.getText().toString();
    }

    public TitleBar setTitle(int resid) {// 标题内容设置（主标题与副标题用"\n"或"\t"分割）
        setTitle(getResources().getString(resid));
        return this;
    }

    public TitleBar setCenterClickListener(OnClickListener l) {// 标题内容点击事件
        CenterLayout.setOnClickListener(l);
        return this;
    }

    public TitleBar setTitleColor(int resid) {// 主标题字体颜色
        CenterText.setTextColor(resid);
        return this;
    }

    public TitleBar setTitleSize(float size) {// 主标题字体大小
        CenterText.setTextSize(size);
        return this;
    }

    public TitleBar setTitleBackground(int resid) {// 主标题背景
        try {
            CenterText.setBackgroundResource(resid);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        return this;
    }

    public TitleBar setTitleOnClickListener(OnClickListener listener) {// 主标题点击事件
        CenterText.setOnClickListener(listener);
        return this;
    }

    public TitleBar setSubTitleColor(int resid) {// 副标题字体颜色
        SubTitleText.setTextColor(resid);
        return this;
    }

    public TitleBar setSubTitleSize(float size) {// 副标题字体大小
        SubTitleText.setTextSize(size);
        return this;
    }

    public TitleBar setSubTitleBackground(int resid) {// 副标题背景
        try {
            SubTitleText.setBackgroundResource(resid);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        return this;
    }

    public TitleBar setSubTitleOnClickListener(OnClickListener listener) {// 主标题点击事件
        SubTitleText.setOnClickListener(listener);
        return this;
    }

    public TitleBar setCustomTitle(View titleView) {// 自定义标题内容样式
        if (titleView == null) {
            CenterText.setVisibility(View.VISIBLE);
            if (CustomCenterView != null)
                CenterLayout.removeView(CustomCenterView);
        } else {
            if (CustomCenterView != null)
                CenterLayout.removeView(CustomCenterView);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            CustomCenterView = titleView;
            CenterLayout.addView(titleView, layoutParams);
            CenterText.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * ======================================== 标题栏 —— 右侧按钮 ========================================
     */
    public TitleBar setActionTextColor(int colorResId) {// 动作按钮字体颜色
        ActionTextColor = colorResId;
        return this;
    }

    public void addRightActions(ActionList actionList) {// 新增多个动作按钮
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addRightAction(actionList.get(i));
        }
    }

    public View addRightAction(Action action) {// 新增单个动作按钮
        final int index = RightLayout.getChildCount();
        return addRightAction(action, index);
    }

    public View addRightAction(Action action, int index) {// 指定位置新增动作按钮
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        RightLayout.addView(view, index, params);
        return view;
    }

    public void removeRightActionAt(int index) {// 移除指定位置动作按钮
        RightLayout.removeViewAt(index);
    }

    public void removerightAction(Action action) {// 移除单个动作按钮
        int childCount = RightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = RightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    RightLayout.removeView(view);
                }
            }
        }
    }

    public void removeAllRightActions() {// 移除全部动作按钮
        RightLayout.removeAllViews();
    }

    public int getRightActionCount() {// 获取动作按钮数量
        return RightLayout.getChildCount();
    }

    /**
     * ======================================== 底部分割线 ========================================
     */
    public TitleBar setBottomLineHeight(int dividerHeight) {// 底部分割线高度
        if (dividerHeight >= 0) {
            BottomLineHeight = dpTopx(dividerHeight);
            BottomLine.getLayoutParams().height = BottomLineHeight;
        } else {
            Log.e("MyTitleBar", " BottomLineHeight 设置无效");
        }
        onRefresh();
        return this;
    }

    public TitleBar setBottomLineBackground(int backgroundresId) {// 底部分割线背景
        try {
            BottomLine.setBackgroundResource(backgroundresId);
        } catch (Exception e) {
            Log.d("MyTitleBar", "资源ID设置有误");
        }
        onRefresh();
        return this;
    }

    /**
     * ======================================== 界面UI绘制 ========================================
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(StatusBar, widthMeasureSpec, heightMeasureSpec);
        measureChild(TopLine, widthMeasureSpec, heightMeasureSpec);
        measureChild(LeftLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(RightLayout, widthMeasureSpec, heightMeasureSpec);
        if (LeftLayout.getMeasuredWidth() > RightLayout.getMeasuredWidth()) {
            CenterLayout.measure(MeasureSpec.makeMeasureSpec(ScreenWidth - 2 * LeftLayout.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            CenterLayout.measure(MeasureSpec.makeMeasureSpec(ScreenWidth - 2 * RightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        }
        measureChild(BottomLine, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), ParentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        StatusBar.layout(0, 0, getMeasuredWidth(), StatusBar.getMeasuredHeight());
        TopLine.layout(0, StatusBarHeight, getMeasuredWidth(), StatusBar.getMeasuredHeight() + TopLine.getMeasuredHeight());
        LeftLayout.layout(0, StatusBarHeight + TopLineHeight, LeftLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        RightLayout.layout(ScreenWidth - RightLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth, getMeasuredHeight() - BottomLine.getMeasuredHeight());
        if (LeftLayout.getMeasuredWidth() > RightLayout.getMeasuredWidth()) {
            CenterLayout.layout(LeftLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth - LeftLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        } else {
            CenterLayout.layout(RightLayout.getMeasuredWidth(), StatusBarHeight + TopLineHeight, ScreenWidth - RightLayout.getMeasuredWidth(), getMeasuredHeight() - BottomLine.getMeasuredHeight());
        }
        BottomLine.layout(0, getMeasuredHeight() - BottomLine.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * ======================================== 新增动作按钮 共用方法 ========================================
     */
    private View inflateAction(Action action) {// 初始化动作按钮布局控件
        LinearLayout view = new LinearLayout(getContext());
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(ActionPadding, 0, ActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);

        if (action.setDrawable() != 0) {
            ImageView img = new ImageView(getContext());//添加动作按钮的图片
            LayoutParams imglp = action.setDrawableSize() == 0 ? new LayoutParams(dpTopx(DEFAULT_ACTION_IMG_SIZE), dpTopx(DEFAULT_ACTION_IMG_SIZE)) : new LayoutParams(dpTopx(action.setDrawableSize()), dpTopx(action.setDrawableSize()));
            img.setLayoutParams(imglp);
            img.setImageResource(action.setDrawable());
            view.addView(img);
        }

        if (!TextUtils.isEmpty(action.setText())) {//若文字内容不为空，添加动作按钮的文字
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.setText());
            text.setPadding(dpTopx(4), 0, 0, 0);//动作按钮中文字举例图片4dp
            text.setTextSize(action.setTextSize() == 0 ? DEFAULT_ACTION_TEXT_SIZE : action.setTextSize());
            if (action.setTextColor() == 0) {
                text.setTextColor(ActionTextColor);
            } else {
                text.setTextColor(action.setTextColor());
            }
            view.addView(text);
        }
        return view;
    }

    @Override
    public void onClick(View view) {// 动作按钮点击事件（回调）
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.onClick();
        }
    }

    public View getViewByAction(Action action) {// 获取单个动作按钮布局容器
        View view = findViewWithTag(action);
        return view;
    }

    /**
     * 刷新控件
     */
    public void onRefresh() {
        ParentHeight = TitleBarHeight + StatusBarHeight + TopLineHeight + BottomLineHeight;
        setMeasuredDimension(getMeasuredWidth(), ParentHeight);
        requestLayout();
        invalidate();
    }

    /**
     * 获取标题栏全局高度
     */
    public int getParentHeight() {
        return ParentHeight;
    }

    /**
     * 自定义对象类
     */
    public static abstract class Action {
        public abstract void onClick();

        public String setText() {
            return null;
        }

        public int setTextColor() {
            return Color.WHITE;
        }

        public int setTextSize() {
            return DEFAULT_ACTION_TEXT_SIZE;
        }

        public int setDrawable() {
            return 0;
        }

        public int setDrawableSize() {
            return DEFAULT_ACTION_IMG_SIZE;
        }
    }

    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    public class MarqueeTextView extends TextView {
        public MarqueeTextView(Context context) {
            super(context);
        }

        public MarqueeTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean isFocused() {
            return true;
        }
    }
}
