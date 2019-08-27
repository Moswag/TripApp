package com.alium.niboexample.fragments;


import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alium.niboexample.R;
import com.alium.niboexample.Register;
import com.alium.niboexample.util.MessageToast;
import com.alium.niboexample.util.PlusSingleton;
import com.alium.niboexample.util.UrlConstants;
import com.alium.niboexample.util.UserSessionManager;
import com.alium.niboexample.util.VariableConstants;
import com.alium.niboexample.util.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadReceipt extends Fragment {
    private ImageView imageView;
    private Button btnChoose, btnUpload;
    private ProgressBar progressBar;

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    UserSessionManager userSessionManager;


    public UploadReceipt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_upload_receipt, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        btnChoose = (Button) view.findViewById(R.id.button_choose);
        btnUpload = (Button) view.findViewById(R.id.button_upload);

        userSessionManager=new UserSessionManager(getActivity());

        HashMap<String,String> session=userSessionManager.getUserDetails();
        String employeeId=session.get(UserSessionManager.KEY_User);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(employeeId);

            }
        });

        return view;
    }

    private void selectimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                ContentResolver resolver = getActivity().getContentResolver();
                bitmap = MediaStore.Images.Media.getBitmap(resolver, path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final  String employeeId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants.UPLOAD_RECEIPT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString(VariableConstants.RESPONSE);
                            if(res.equals(VariableConstants.RESPONSE_SUCCESS)){
                                imageView.setImageResource(0);
                                imageView.setVisibility(View.GONE);
                                MessageToast.show(getActivity(),"Receipt successfully added");

                            }
                            else{
                                MessageToast.show(getActivity(),res);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MessageToast.show(getActivity(),"Failed to connect to database, please check if you hve internet connection or contact admin");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageToString(bitmap));
                params.put("employee_id", employeeId);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);


    }


}
