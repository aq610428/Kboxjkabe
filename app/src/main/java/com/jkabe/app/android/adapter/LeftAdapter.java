package com.jkabe.app.android.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jkabe.app.android.R;
import com.jkabe.app.android.bean.LeftVo;
import com.jkabe.app.android.bean.UserInfo;
import com.jkabe.app.android.ui.LocationActivity;
import com.jkabe.app.android.ui.PreviewActivity;
import com.jkabe.app.android.ui.StoreActivity;
import com.jkabe.app.android.ui.StoreListActivity;
import com.jkabe.app.android.ui.fragment.CarLeftFragment;
import com.jkabe.app.android.util.Constants;
import com.jkabe.app.android.util.Md5Util;
import com.jkabe.app.android.util.SaveUtils;
import com.jkabe.app.android.util.ToastUtil;
import com.jkabe.app.android.weight.DialogUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zt
 * @date: 2020/5/26
 * @name:清单
 */
public class LeftAdapter extends AutoRVAdapter {
    private List<LeftVo> inventories = new ArrayList<>();
    private CarLeftFragment fragment;

    public LeftAdapter(CarLeftFragment fragment, List<LeftVo> inventories) {
        super(fragment.getContext(), inventories);
        this.fragment = fragment;
        this.inventories = inventories;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_lefe;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        LeftVo leftVo = inventories.get(position);
        vh.getTextView(R.id.text_name).setText(leftVo.getTag());
        GridLayoutManager manager = new GridLayoutManager(fragment.getContext(), 4);
        RecyclerView recyclerView = vh.getRecyclerView(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);

        List<LeftVo.ItemsBean> itemsBeans = leftVo.getItems();
        if (itemsBeans != null && itemsBeans.size() > 0) {
            CarLiftAdapter adapter = new CarLiftAdapter(fragment, itemsBeans);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = itemsBeans.get(position).getName();
                    Intent intent;
                    switch (name) {
                        case "惠保养":
                            intent = new Intent(fragment.getContext(), StoreListActivity.class);
                            fragment.startActivity(intent);
                            break;
                        case "惠购车":
                            String url = itemsBeans.get(position).getUrl();
                            intent = new Intent(fragment.getContext(), PreviewActivity.class);
                            intent.putExtra("name", "惠购车");
                            intent.putExtra("url", url);
                            fragment.startActivity(intent);
                            break;
                        case "惠保险":
                            fragment.startActivity(new Intent(fragment.getContext(), LocationActivity.class));
                            break;
                        case "车油惠":
                            checkLogin();
                            break;
                        case "万车品":
                            intent = new Intent(fragment.getContext(), PreviewActivity.class);
                            intent.putExtra("name", "万车品");
                            intent.putExtra("url", itemsBeans.get(position).getUrl() + getWcp());
                            fragment.startActivity(intent);
                            break;
                        case "一键救援":
                            intent = new Intent(fragment.getContext(), PreviewActivity.class);
                            intent.putExtra("name", "一键救援");
                            intent.putExtra("url", itemsBeans.get(position).getUrl());
                            fragment.startActivity(intent);
                            break;
                    }
                }
            });
        }

    }

    private void checkLogin() {
        UserInfo info = SaveUtils.getSaveInfo();
        if ((info.getIsreal() == 0 || info.getIsreal() == 3)) {
            DialogUtils.showTipDialog(fragment.getContext(), "您的信息不全,请先认证");
        } else if ((info.getIsreal() == 2)) {
            ToastUtil.showToast("还在审核中");
        }else{
            Intent  intent = new Intent(fragment.getContext(), PreviewActivity.class);
            intent.putExtra("name", "车油惠");
            intent.putExtra("url", Constants.oilUrl + "?memberid=" + SaveUtils.getSaveInfo().getId());
            fragment.startActivity(intent);
        }
    }

    private String getWcp() {
        String phone = SaveUtils.getSaveInfo().getMobile();
        String source = "king";
        String uid = SaveUtils.getSaveInfo().getId();
        long timeM = System.currentTimeMillis();
        String sign = Md5Util.encode(phone + uid + timeM + source + "wcp20200402");
        return "?phone=" + phone + "&source=" + source + "&uid=" + uid + "&timestamp=" + timeM + "&sign=" + sign;
    }
}