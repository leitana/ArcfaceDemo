package com.arcsoft.arcfacedemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.common.Constants;
import com.arcsoft.arcfacedemo.faceserver.FaceServer;
import com.arcsoft.arcfacedemo.model.UserInfo;
import com.arcsoft.arcfacedemo.service.RequestService;
import com.arcsoft.arcfacedemo.service.RetrofitCreateHelper;
import com.arcsoft.arcfacedemo.service.RxSchedulerHelper;
import com.arcsoft.arcfacedemo.service.RxSubscriber;
import com.arcsoft.arcfacedemo.widget.TitleBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.yjx.sharelibrary.Share;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterInfoActivity extends AppCompatActivity {
    private Context context = RegisterInfoActivity.this;
//    private String ROOT_DIR = context.getFilesDir().getAbsolutePath();
//    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
//    public  String SAVE_IMG_DIR = ROOT_DIR + File.separator + "register" + File.separator + "imgs";
    public String ROOT_PATH;
    public  String SAVE_IMG_DIR = "";

    private EditText name;
    private MaterialSpinner gender;
    private EditText organization;
    private Button register;
    private Context mContext = RegisterInfoActivity.this;
    private TitleBar titleBar;

    private int trackId;
    private String faceInfo;

    private String[] genderData = {"男", "女"};

    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerinfo);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.spinner);
        organization = findViewById(R.id.organization);
        register = findViewById(R.id.bt_register);
        titleBar = findViewById(R.id.title);
        titleBar.setTitle("设置");
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

        gender.setItems(genderData);

        trackId = getIntent().getIntExtra("trackId", -1);
        faceInfo = getIntent().getStringExtra("FaceInfo");

        if (trackId == -1) {
            Toast.makeText(mContext, "发生一个错误，code：-1", Toast.LENGTH_SHORT).show();
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText())
                        || TextUtils.isEmpty(organization.getText())) {
                    Toast.makeText(mContext, "信息不完整", Toast.LENGTH_SHORT).show();
                } else {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setTrackId(trackId);
                    userInfo.setUser_name(name.getText().toString());
                    userInfo.setGender(gender.getText().toString());
                    userInfo.setOrganization(organization.getText().toString());
                    userInfo.setFace_aspect(faceInfo);

                    List<UserInfo> userInfoList = new ArrayList<>();
                    String json = Share.getString(Constants.USERINFO);
                    Gson gson = new Gson();
                    List<UserInfo> jsonList = gson.fromJson(json, new TypeToken<ArrayList<UserInfo>>() {
                    }.getType());
                    if (jsonList != null) {
                        userInfoList.addAll(jsonList);
                    }
                    boolean isExist = false;
                    for (int i = 0; i < userInfoList.size(); i++) {
                        if (trackId == userInfoList.get(i).getTrackId()) {
                            isExist = true;
                        }
                    }
                    if (!isExist) {
                        userInfoList.add(userInfo);
                        String nowJson = gson.toJson(userInfoList);
                        Share.putString(Constants.USERINFO, nowJson);
                        if (ROOT_PATH == null) {
                            ROOT_PATH = context.getExternalCacheDir().getAbsolutePath();
                        }
                        File imgFile = new File(ROOT_PATH + File.separator + "register" + File.separator + "imgs" + File.separator + "registered " + trackId + FaceServer.IMG_SUFFIX);
//                        if (imgFile.exists()) {
//                            upLoadPic(imgFile, userInfo);
//                        } else {
//                            Toast.makeText(mContext,"发生错误，找不到照片",Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
                    } else {
                        Toast.makeText(mContext,"库中存在",Toast.LENGTH_SHORT).show();
                    }

//                    upLoad(null,userInfo.getUser_name(),userInfo.getGender(),userInfo.getOrganization(),
//                            null,userInfo.getFace_aspect());
//                    Toast.makeText(mContext, "录入成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private Disposable disposables;
//    private void upLoad(String face_id, String user_name, String gender, String organization,
//                        String face_imgs, String face_aspect) {
//        if (sweetAlertDialog != null) {
//            sweetAlertDialog.dismissWithAnimation();
//            sweetAlertDialog = null;
//        }
//        sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setContentText("正在注册...");
//        sweetAlertDialog.show();
//
//        RetrofitCreateHelper.createApi(RequestService.class, RequestService.URL)
//                .getUserInfo(face_id, user_name, gender, organization, face_imgs, face_aspect)
//                .compose(RxSchedulerHelper.<JsonObject>io_main())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        disposables = disposable;
//                    }
//                }).subscribe(new RxSubscriber<JsonObject>() {
//                    @Override
//                    public void _onNext(JsonObject jsonObject) {
//                        String s = jsonObject.toString();
//                        if (!s.startsWith("{") || !s.endsWith("}")) {
//                            Toast.makeText(mContext, "数据返回格式错误", Toast.LENGTH_SHORT).show();
//                        } else {
//                            JSONObject json = JSON.parseObject(s);
//                            String code = json.getString("code");
//                            if (code.equals("0")) {
//                                Toast.makeText(mContext, "录入成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(mContext, "录入失败", Toast.LENGTH_SHORT).show();
//                            }
//                            if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
//                                sweetAlertDialog.dismissWithAnimation();
//                            }
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
//                            sweetAlertDialog.dismissWithAnimation();
//                        }
//                        finish();
//                    }
//                });
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (disposables !=null && !disposables.isDisposed()){
//            disposables.dispose();
//        }
//        super.onDestroy();
//    }
//
//    public static MultipartBody filesToMultipartBody(List<File> files) {
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//
//        for (File file : files) {
//            RequestBody requestBody;
//            requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
//            builder.addFormDataPart("file", file.getName(), requestBody);
//        }
//
//        builder.setType(MultipartBody.FORM);
//        MultipartBody multipartBody = builder.build();
//        return multipartBody;
//    }
//
//    private void upLoadPic(File file, final UserInfo userInfo) {
//        sweetAlertDialog = null;
//        sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
//        sweetAlertDialog.setContentText("正在注册...");
//        sweetAlertDialog.show();
//        List<File> mFileList = new ArrayList<>();
//        mFileList.add(file);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl(RequestService.URL)
//                .build();
//        final RequestService service = retrofit.create(RequestService.class);
////
//        MultipartBody body = filesToMultipartBody(mFileList);
//
//        service.multiUpload(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(new Action() {
//                    @Override
//                    public void run() throws Exception {
//
//                    }
//                })
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody jsonObject) {
//                        String s = null;
//                        try {
//                            s = jsonObject.string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        upLoad(null,userInfo.getUser_name(),userInfo.getGender(),userInfo.getOrganization(),
//                                s,userInfo.getFace_aspect());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(mContext, "上传照片失败", Toast.LENGTH_SHORT).show();
//                        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
//                            sweetAlertDialog.dismissWithAnimation();
//                        }
//                        finish();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
////        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
////        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
////        service.singleUpload(filePart)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .doFinally(new Action() {
////                    @Override
////                    public void run() throws Exception {
////
////                    }
////                })
////                .subscribe(new Observer<String>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////
////                    }
////
////                    @Override
////                    public void onNext(String jsonObject) {
////                        upLoad(null,userInfo.getUser_name(),userInfo.getGender(),userInfo.getOrganization(),
////                                jsonObject,userInfo.getFace_aspect());
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        Toast.makeText(mContext, "上传照片失败", Toast.LENGTH_SHORT).show();
////                        finish();
////                    }
////
////                    @Override
////                    public void onComplete() {
////
////                    }
////                });
//
//    }
}
