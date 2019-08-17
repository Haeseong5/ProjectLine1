package com.example.haeseong.projectline1.market;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.firebase_helper.FireBaseApi;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.example.haeseong.projectline1.helper.ProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

import static com.example.haeseong.projectline1.firebase_helper.FireBaseApi.storageReference;

public class MarketWriteActivity extends AppCompatActivity {
    ImageView ivBackButton;
    TextView tvUpload;
    ImageView ivPhoto;
    EditText etTitle;
    EditText etPrice;
    EditText etContent;
    ProgressDialog progressDialog;
    ArrayList<Uri> selectedUriList;
    ArrayList<Bitmap> bitmaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_write);
        ivBackButton = findViewById(R.id.market_write_backbutton);
        tvUpload = findViewById(R.id.market_write_ok);
        ivPhoto = findViewById(R.id.market_write_image);
        etTitle = findViewById(R.id.market_write_title);
        etPrice = findViewById(R.id.market_write_price);
        etContent = findViewById(R.id.market_write_content);
        progressDialog = new ProgressDialog(MarketWriteActivity.this);

        selectedUriList = new ArrayList<>();
        bitmaps = new ArrayList<>();
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = Timestamp.now();
                String title = etTitle.getText().toString();
                String price = etPrice.getText().toString();
                String content = etContent.getText().toString();
                ArrayList<Uri> images = selectedUriList;

                if((title.length()) < 1){
                    Toast.makeText(getApplicationContext(),"두 글자 이상 입력하세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(content.length() < 1){
                    Toast.makeText(getApplicationContext(),"두 글자 이상 입력하세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(isStringToInteger(price) == false){
                    Toast.makeText(getApplicationContext(),"숫자만 입력하세요.",Toast.LENGTH_SHORT).show();
                    if(content.length() < 1)
                    Toast.makeText(getApplicationContext(),"두 글자 이상 입력하세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(images.size()<=0 || images == null) {
                    Toast.makeText(getApplicationContext(),"사진을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    try {
                        uploadLocalFileStorage(timestamp,title,price,content,images);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

//        https://github.com/ParkSangGwon/TedBottomPicker
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker.with(MarketWriteActivity.this)
                                .setPeekHeight(1600)
                                .showTitle(false)
                                .setCompleteButtonText("Done")
                                .setEmptySelectionText("No Select")
                                .setSelectedUriList(selectedUriList)
                                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                                    @Override
                                    public void onImagesSelected(List<Uri> uriList) {
                                        // here is selected image uri list
                                        selectedUriList = (ArrayList<Uri>) uriList;
                                        for (int i=0; i<selectedUriList.size(); i++) {
                                            Log.d("selected List ", selectedUriList.get(i).toString());
                                            Glide.with(getApplicationContext()).load(selectedUriList.get(i)).into(ivPhoto);


                                            Uri image = selectedUriList.get(i);
                                            try {
                                                bitmaps.add( MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), image) );
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MarketWriteActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }

                };
                checkPermission(permissionListener);
            }
        });
    }
    public boolean isStringToInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public  void uploadLocalFileStorage(Timestamp timestamp, String title, String price, String content, ArrayList<Uri> images) throws FileNotFoundException {
        showDialog();
        for(int i=0; i<images.size(); i++) {
//            String filePath = getRealPathFromURI(images.get(i));
//            Uri file = Uri.fromFile(new File(filePath));
//            Log.d("file pathlog", images.get(i).toString());
//            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = storageReference.child("market_images/"+title+"/"+i+"jpg");
            Log.d("file uri log", images.get(i).toString());

//            InputStream stream = new FileInputStream(new File(images.get(i).toString()));
            UploadTask uploadTask = ref.putBytes(data);

// Register observers to listen for when the download is done or if it fails
            int finalI = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(MarketWriteActivity.this, "Permission Denied\n" + "사진올리기 실패", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d("SUCCESS MARKET PHOTO", String.valueOf(finalI));
//                    FireBaseApi.getUriStorage("");
                    writeMarketData(timestamp, title, price, content, images);

                }
            });
        }

    }
//    public void getUriStorage(){
//            StorageReference ref = storageReference.child("market_images/"+title+"/"+i+"jpg");
//            uploadTask = ref.putFile(file);
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return ref.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                    } else {
//                        // Handle failures
//                        // ...
//                    }
//                }
//            });
//    }
    public void writeMarketData(Timestamp timestamp, String title, String price, String content, ArrayList<Uri> images){
        MarketData marketData = new MarketData();
        marketData.setTitle(title);
        marketData.setContent(content);
        marketData.setPrice(price);
        marketData.setTime(timestamp);
        marketData.setUid(FireBaseApi.firebaseUser.getUid());
        marketData.setWriter(GlobalUser.getInstance().getName());
//        marketData.setPhoto(ima);
        FireBaseApi.firestore
                .collection("market_board")
                .add(marketData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("market", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        println("게시물 작성 성공");
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK,resultIntent);
                        dismissDialog();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fff", "Error adding document", e);
//                        println("게시물 작성 실패");
                        dismissDialog();
                        finish();
                    }
                });
    }
    private void checkPermission(PermissionListener permissionlistener) {
        TedPermission.with(MarketWriteActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    public void showDialog(){
        //요청 이 다이어로그를 종료할 수 있게 지정함
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();
    }
    public void dismissDialog(){
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();

    }
}
