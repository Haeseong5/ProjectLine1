package com.example.haeseong.projectline1.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haeseong.projectline1.R;
import com.example.haeseong.projectline1.activity.LoginActivity;
import com.example.haeseong.projectline1.activity.MapsActivity;
import com.example.haeseong.projectline1.activity.UpdateProfileActivity;
import com.example.haeseong.projectline1.adapter.ProfileAdapter;
import com.example.haeseong.projectline1.adapter.ProfileAdapter2;
import com.example.haeseong.projectline1.helper.GlobalUser;
import com.example.haeseong.projectline1.item.ProfileItem;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

public class FragmentProfile extends Fragment {
    private String TAG = "FragmentProfile";
    public static int CAMERA_REQUEST = 100;
    FirebaseUser user;
    private StorageReference mStorageRef;

    View rootView;
    Button btLogout;
    TextView tvName;
    ImageView ivProfileImage;
    ListView listView1;
    ListView listView2;
    ListView listView3;
    ListView listView4;
    ListView listView5;

    ScrollView scrollView;

    ArrayList<ProfileItem> profileItems;
    ArrayList<String> profileItems2;
    ProfileAdapter profileAdapter;
    ProfileAdapter2 profileAdapter2;

    String name;
    String email;
    String image;
    Bitmap bitmap;

    @Nullable
    @Override //Fragment가 자신의 UI를 그릴 때 호출합니다. UI를 그리기 위해 메서드에서는 View를 Return 해야 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        findView();
        buttonListener();
        setProfile();
        setMyInfoList();
        setCustomerCenterList();

        mStorageRef = FirebaseStorage.getInstance().getReference();




        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CAMERA_REQUEST);
            }
        });
        return rootView;
    }
    protected void setProfile() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            image = String.valueOf(user.getPhotoUrl());
            ivProfileImage.setImageURI(user.getPhotoUrl());
            Log.d("image uri :", String.valueOf(user.getPhotoUrl()));
            if(name !="" && name != null){
                tvName.setText(name);
            }else{
                tvName.setText(email);
            }
        }
    }
    protected void facebookLogout(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null) && (!accessToken.isExpired()); //액세스토큰이 null이 아니고 만료되지 않았다면

        GlobalUser.getInstance().setPushToken(String.valueOf(accessToken));
        GlobalUser.getInstance().setLogin(isLoggedIn);
        println("로그인 여부:" + String.valueOf(isLoggedIn)+"/"+GlobalUser.getInstance().isLogin());
        if(GlobalUser.getInstance().isLogin() == false)
        {
            println("페이스북 로그아웃 success");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    protected void buttonListener()
    {
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                facebookLogout();
            }
        });
    }
    protected void setMyInfoList() {
        profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem("프로필 수정", R.mipmap.ic_app, getString(R.string.blank)));
        profileItems.add(new ProfileItem("학교인증", R.mipmap.ic_app, getString(R.string.blank)));
        profileItems.add(new ProfileItem("내가 쓴 글", R.mipmap.ic_app, "0"));
        profileItems.add(new ProfileItem("내가 댓글 단 글", R.mipmap.ic_app, "0"));

        profileAdapter = new ProfileAdapter(getActivity(), profileItems);
        listView1.setAdapter(profileAdapter);
        listViewHeightSet(profileAdapter, listView1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), profileItems.get(position).getText(),Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                        intent.putExtra("name", name);
//                        intent.putExtra("school", school);
//                        intent.putExtra("grade", grade);
                        intent.putExtra("image", image);

                        startActivity(intent);
                }

            }
        });
        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
    }
    protected void setCustomerCenterList(){
        profileItems2 = new ArrayList<>();
        profileItems2.add("공지사항 / 이벤트");
        profileItems2.add("친구에게 소개하기");
        profileItems2.add("도움말");
        profileItems2.add("문의하기");

        profileAdapter2 = new ProfileAdapter2(getActivity(), profileItems2);
        listView2.setAdapter(profileAdapter2);
        listViewHeightSet(profileAdapter2,listView2);
        listView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void findView(){
        btLogout = rootView.findViewById(R.id.profile_facebook_logout);
        tvName = rootView.findViewById(R.id.profile_name_text);
        ivProfileImage = rootView.findViewById(R.id.profile_image);
        listView1 = rootView.findViewById(R.id.profile_listview);
        scrollView = rootView.findViewById(R.id.profile_scrollview);
        listView2 = rootView.findViewById(R.id.profile_listview2);
    }

    void println(String message){
        Log.d(TAG, message);
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    private void listViewHeightSet(Adapter listAdapter, ListView listView){
        //        스크롤뷰안에 리스트뷰가 있어서 리스트뷰 높이값 계산 필요.
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
            if (requestCode == CAMERA_REQUEST) {
                Uri image = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                    ivProfileImage.setImageBitmap(bitmap);
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    protected void updatePhotoUri(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference usersRef = db.collection("users").document(user.getUid());
//        db.collection("users").document(firebaseUser.getUid()).set(userData)

// Set the "isCapital" field of the city 'DC'
        usersRef
                .update("photoUri", )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    protected getPhoroUri(){
        final StorageReference ref = storageRef.child("images/mountains.jpg");
        uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void uploadImage(){
        StorageReference mountainsRef = mStorageRef.child("profile_images/"+user.getUid()).child(name+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String photoUri =  String.valueOf(downloadUrl);
                Log.d("url", photoUri);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userPhoto");

                Hashtable<String, String> profile = new Hashtable<String, String>();
                profile.put("name", name);
                profile.put("photo",  photoUri);
                myRef.child(name).setValue(profile);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = dataSnapshot.getValue().toString();
                        Log.d("Profile", s);
                        if (dataSnapshot != null) {
                            Toast.makeText(getActivity(), "사진 업로드가 잘 됐습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } //변경이 아닌 추가 코드..

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "사진 업로드를 취소했습니다.", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }


//    protected void setCustomerCenterList(){
//        profileItems2 = new ArrayList<>();
//
//        profileItems2.add("동아리 정보");
//        profileItems2.add("학원");
//        profileItems2.add("스터디 카페");
//        profileItems2.add("독서실");
//        profileItems2.add("급식");
//
//        profileAdapter2 = new ProfileAdapter2(getActivity(), profileItems2);
//        listView2.setAdapter(profileAdapter2);
//        listViewHeightSet(profileAdapter2,listView2);
//        listView2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}
