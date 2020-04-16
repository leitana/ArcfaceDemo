package com.arcsoft.arcfacedemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.model.UserInfo;
import com.arcsoft.arcfacedemo.widget.TitleBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjx.sharelibrary.Share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FaceListActivity extends AppCompatActivity {
    private Context context = FaceListActivity.this;
//    private String ROOT_DIR = context.getFilesDir().getAbsolutePath();
//    public String SAVE_IMG_DIR = ROOT_DIR + File.separator + "register" + File.separator + "imgs";
    public String SAVE_IMG_DIR = "";
    private RecyclerView recyclerView;
    private TitleBar titleBar;
    private List<UserInfo> userInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facelist);
        titleBar = findViewById(R.id.title);
        titleBar.setTitle("人脸库");
        titleBar.addLeftAction(new TitleBar.Action() {
            @Override
            public void onClick() {
                finish();
            }

            @Override
            public int setDrawable() {
                return R.mipmap.ic_left;
            }
        });
        recyclerView = findViewById(R.id.recycler_faceslist);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        List<UserInfo> userInfoList = new ArrayList<>();
        String json = Share.getString(Constants.USERINFO);
        Gson gson = new Gson();
        List<UserInfo> jsonList = gson.fromJson(json, new TypeToken<ArrayList<UserInfo>>() {}.getType());
        if (jsonList != null) {
            userInfoList.addAll(jsonList);
        }
        if (userInfoList != null) {
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(userInfoList, FaceListActivity.this);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }



    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private List<UserInfo> userInfoList;

        public RecyclerAdapter(List<UserInfo> userInfoList, Context context) {
            this.userInfoList = userInfoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = getLayoutInflater().inflate(R.layout.item_face, null, false);
            RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(itemView);
            viewHolder.textView = itemView.findViewById(R.id.tv_item_name);
            viewHolder.imageView = itemView.findViewById(R.id.iv_item_head_img);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            if (userInfoList == null) {
                return;
            }
            SAVE_IMG_DIR = FaceServer.ROOT_PATH + File.separator+ FaceServer.SAVE_IMG_DIR;
            File imgFile = new File(SAVE_IMG_DIR + File.separator + "registered " + userInfoList.get(i).getTrackId() + FaceServer.IMG_SUFFIX);
            Glide.with(holder.imageView)
                    .load(imgFile)
                    .into(holder.imageView);
            holder.textView.setText(userInfoList.get(i).getUser_name());
        }

        @Override
        public int getItemCount() {
            return userInfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            ImageView imageView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
