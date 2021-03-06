package com.example.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.ToastUtils;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketCallback;
import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketCallback {

    private ITicketPresenter mTicketPresenter;
    private boolean mHasTaobaoApp = false;

    @BindView(R.id.ticket_cover)
    public ImageView mTicketCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_code)
    public MaterialTextView mTicketCopyCode;

    @BindView(R.id.ticket_back)
    public ImageView ticketBack;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_load_retry)
    public TextView retryLoadText;

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        ticketBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTicketCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????? ????????????
                String ticketCode = mTicketCode.getText().toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //??????????????????
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);
                //?????????????????????
                if(mHasTaobaoApp){
                    //??????????????????
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    intent.setComponent(componentName);
                    startActivity(intent);
                }else {
                    ToastUtils.showToast("????????????");
                }
            }
        });
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        //?????????????????????
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            if(packageInfo != null){
                mHasTaobaoApp = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }

        LogUtils.d(this,"mHasTaobaoApp --->" + mHasTaobaoApp);
        mTicketCopyCode.setText(mHasTaobaoApp ? "??????????????????" : "???????????????");
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mTicketCover != null && !TextUtils.isEmpty(cover)) {
            Glide.with(this).load(UrlUtils.getCoverPath(cover)).into(mTicketCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String code = result.getData().getTbk_tpwd_create_response().getData().getModel();
            mTicketCode.setText(code.substring(0,code.indexOf(" "))); //????????????????????????????????????????????????
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
