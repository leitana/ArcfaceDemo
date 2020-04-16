package com.arcsoft.arcfacedemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.faceserver.CompareResult;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.model.UserInfo;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjx.sharelibrary.Share;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaceSearchResultAdapter extends RecyclerView.Adapter<FaceSearchResultAdapter.CompareResultHolder> {
    private List<CompareResult> compareResultList;
    private LayoutInflater inflater;

    public FaceSearchResultAdapter(List<CompareResult> compareResultList, Context context) {
        inflater = LayoutInflater.from(context);
        this.compareResultList = compareResultList;
    }

    @NonNull
    @Override
    public CompareResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_showmessage, null, false);
        CompareResultHolder compareResultHolder = new CompareResultHolder(itemView);
        compareResultHolder.textView = itemView.findViewById(R.id.tv_name);
        compareResultHolder.imageView = itemView.findViewById(R.id.iv_face);
        compareResultHolder.time = itemView.findViewById(R.id.tv_time);
        compareResultHolder.ll_message = itemView.findViewById(R.id.ll_message);
        compareResultHolder.tv_org = itemView.findViewById(R.id.tv_org);

        return compareResultHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompareResultHolder holder, int position) {
        if (compareResultList == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = holder.ll_message.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        }

        File imgFile = new File(FaceServer.ROOT_PATH + File.separator + FaceServer.SAVE_IMG_DIR + File.separator + compareResultList.get(position).getUserName() + FaceServer.IMG_SUFFIX);
        Glide.with(holder.imageView)
                .load(imgFile)
                .into(holder.imageView);
//        holder.textView.setText(compareResultList.get(position).getUserName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd\nHH:mm");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        holder.time.setText(simpleDateFormat.format(date));
        List<UserInfo> userInfoList = new ArrayList<>();
        String json = Share.getString(Constants.USERINFO);
        Gson gson = new Gson();
        List<UserInfo> jsonList = gson.fromJson(json, new TypeToken<ArrayList<UserInfo>>() {}.getType());
        if (jsonList != null) {
            userInfoList.addAll(jsonList);
        }
        String str = compareResultList.get(position).getUserName()+"";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        m.find();
        int target = Integer.parseInt(m.group());
        for (int i = 0; i < userInfoList.size(); i++) {
            if (userInfoList.get(i).getTrackId() == target) {
                holder.textView.setText(userInfoList.get(i).getUser_name());
                holder.tv_org.setText(userInfoList.get(i).getOrganization());
            }

        }
    }

    @Override
    public int getItemCount() {
        return compareResultList == null ? 0 : compareResultList.size();
    }

    class CompareResultHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView textView;
        ImageView imageView;
        LinearLayout ll_message;
        TextView tv_org;

        CompareResultHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
