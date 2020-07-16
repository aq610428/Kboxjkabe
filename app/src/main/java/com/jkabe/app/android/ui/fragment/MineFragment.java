package com.jkabe.app.android.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.jkabe.app.android.R;
import com.jkabe.app.android.base.BaseApplication;
import com.jkabe.app.android.base.BaseFragment;
import com.jkabe.app.android.bean.UserInfo;
import com.jkabe.app.android.glide.GlideUtils;
import com.jkabe.app.android.ui.AboutActivity;
import com.jkabe.app.android.ui.InvitationActivity;
import com.jkabe.app.android.ui.LoginActivity;
import com.jkabe.app.android.ui.PreviewActivity;
import com.jkabe.app.android.ui.UserActivity;
import com.jkabe.app.android.util.LogUtils;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.Utility;
import com.jkabe.app.android.weight.PreferenceUtils;
import com.jkabe.app.android.weight.RuntimeRationale;
import com.jkabe.app.android.zxing.android.CaptureActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import crossoverone.statuslib.StatusUtil;
import static android.app.Activity.RESULT_OK;

/**
 * @author: zt
 * @date: 2020/7/2
 * @name:我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView text_name, text_edit, text_invitation, text_code, text_contacts, text_bind, text_car, text_about, text_out;
    private UserInfo info;
    private ImageView icon_head;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
            initView();
            lazyLoad();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusUtil.setUseStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        StatusUtil.setSystemStatus(getActivity(), false, true);
        info = SaveUtils.getSaveInfo();
        if (Utility.isEmpty(info.getNickname())) {
            text_name.setText(info.getMobile());
        } else {
            text_name.setText(info.getNickname());
        }
        GlideUtils.CreateImageCircular(info.getUsericon(), icon_head, 5);
    }


    private void initView() {
        icon_head = getView(rootView, R.id.icon_head);
        text_name = getView(rootView, R.id.text_name);
        text_edit = getView(rootView, R.id.text_edit);
        text_invitation = getView(rootView, R.id.text_invitation);
        text_code = getView(rootView, R.id.text_code);
        text_contacts = getView(rootView, R.id.text_contacts);
        text_bind = getView(rootView, R.id.text_bind);
        text_car = getView(rootView, R.id.text_car);
        text_about = getView(rootView, R.id.text_about);
        text_out = getView(rootView, R.id.text_out);
        text_out.setOnClickListener(this);
        text_edit.setOnClickListener(this);

        text_invitation.setOnClickListener(this);
        text_code.setOnClickListener(this);
        text_contacts.setOnClickListener(this);
        text_about.setOnClickListener(this);
        text_edit.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_edit:
                startActivity(new Intent(getContext(), UserActivity.class));
                break;

            case R.id.text_invitation:
                startActivity(new Intent(getContext(), InvitationActivity.class));
                break;
            case R.id.text_code:
                checkPermission();
                break;
            case R.id.text_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.text_contacts:
                Intent intent = new Intent(getContext(), PreviewActivity.class);
                intent.putExtra("name", "加入社群");
                intent.putExtra("url", "http://openapi.jkabe.com/golo/about");
                startActivity(intent);
                break;
            case R.id.text_out:
                SaveUtils.clealCacheDisk();
                final SharedPreferences sharedPreferences =getActivity().getSharedPreferences("north", Context.MODE_PRIVATE);
                PreferenceUtils.clearPreference(getContext(),sharedPreferences);
                BaseApplication.activityTaskManager.closeAllActivityExceptOne("LoginActivity");
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void checkPermission() {
        AndPermission.with(this).runtime().permission(Permission.CAMERA)
                .rationale(new RuntimeRationale())
                .onGranted(permissions -> {
                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                })
                .onDenied(permissions -> {
                    if (AndPermission.hasAlwaysDeniedPermission(getContext(), permissions)) {
                        showSettingDialog(getContext(), permissions);
                    }
                })
                .start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                LogUtils.e("解码结果： \n" + content);
            }
        }
    }
}
