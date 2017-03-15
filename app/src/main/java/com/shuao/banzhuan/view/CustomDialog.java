package com.shuao.banzhuan.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuao.banzhuan.R;

/**
 * Created by flyonthemap on 2017/3/8.
 */
public class CustomDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private ButtonRespond respond;

    private TextView tvDialogtitle;// 对话框标题
    private TextView tvDialogMassage;// 对话框信息
    private Button btLeft;// 左边按钮
    private Button btRight;// 右边按钮
    private LinearLayout llFreame;// 空壳view对象

    public CustomDialog(Context context, ButtonRespond respond) {
        super(context, R.style.custom_dialog);
        this.context = context;
        this.respond = respond;
        setContentView(R.layout.custom_dialog);
        initView();
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // WindowManager m = (WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE);

        // Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();


        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.CENTER);

    }

    @Override
    public void cancel() {
        // TODO Auto-generated method stub
        super.cancel();
    }

    /**
     * 组件初始化
     */
    private void initView() {

        tvDialogtitle = (TextView) this.findViewById(R.id.tv_dialog_title);
        tvDialogMassage = (TextView) this.findViewById(R.id.tv_dialog_massage);
        llFreame = (LinearLayout) this.findViewById(R.id.ll_frame);
        btLeft = (Button) this.findViewById(R.id.bt_left);
        btLeft.setOnClickListener(this);
        btRight = (Button) this.findViewById(R.id.bt_right);
        btRight.setOnClickListener(this);
        tvDialogMassage.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * 设置对话框标题
     *
     * @param resouseId
     *            资源id
     */
    public void setDialogTitle(int resouceId) {
        tvDialogtitle.setText(resouceId);
    }

    /**
     * 设置对话框标题
     *
     * @param titleStr
     *            标题
     */
    public void setDialogTitle(String titleStr) {
        tvDialogtitle.setText(titleStr);
    }

    /**
     * 设置对话框提示信息
     *
     * @param resouceID
     *            资源ID
     */
    public void setDialogMassage(int resouceID) {
        tvDialogMassage.setText(resouceID);
    }

    /**
     * 设置对话框提示信息
     *
     * @param massage
     *            提示信息
     */
    public void setDialogMassage(String massage) {
        tvDialogMassage.setText(massage);
    }

    /**
     * 设置左边按钮文字
     *
     * @param resouceId
     *            资源id
     */
    public void setLeftButtonText(int resouceId) {
        btLeft.setText(resouceId);
    }

    /**
     * 设置左边按钮文字
     *
     * @param text
     *            按钮文字
     */
    public void setLeftButtonText(String text) {
        btLeft.setText(text);
    }

    /**
     * 设置右边按钮文字
     *
     * @param resuoceId
     *            资源id
     */
    public void setRightButtonText(int resuoceId) {
        btRight.setText(resuoceId);
    }

    /**
     * 设置右边按钮文字
     *
     * @param text
     *            按钮文字
     */
    public void setRightButtonText(String text) {
        btRight.setText(text);
    }

    /**
     * 设置文字信息界面是否可见
     *
     * @param visibility
     */
    public void setMagssageViewVisibility(int visibility) {
        tvDialogMassage.setVisibility(visibility);
    }

    /**
     * 框架界面是否可见
     *
     * @param visibility
     */
    public void setFrameViewVisibility(int visibility) {
        llFreame.setVisibility(visibility);
    }

    /**
     * 给空壳LinearLayout添加布局文件
     *
     * @param view
     */
    public void addView2Frame(View view) {
        llFreame.addView(view, 0);
    }

    /**
     * 设置左边按钮的背景
     *
     * @param resid
     */
    public void setLeftButonBackgroud(int resid) {
        btLeft.setBackgroundResource(resid);
    }

    /**
     * 设置右边按钮的背景
     *
     * @param resid
     */
    public void setRightButonBackgroud(int resid) {
        btRight.setBackgroundResource(resid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                respond.buttonLeftRespond();
                break;
            case R.id.bt_right:
                respond.buttonRightRespond();
                break;
            default:
                break;
        }

    }

    public interface ButtonRespond {
        public void buttonLeftRespond();

        public void buttonRightRespond();

    }
}
