package com.alium.niboexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.alium.niboexample.model.User;
import com.alium.niboexample.util.MessageToast;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp;
    Button btnSignIn;
    AutoCompleteTextView employeeId,password;
    StringRequest request;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tvSignUp=(TextView) findViewById(R.id.tvSignUp);
        btnSignIn=(Button) findViewById(R.id.btnSignIn);
        employeeId=(AutoCompleteTextView) findViewById(R.id.employee_id);
        password=(AutoCompleteTextView) findViewById(R.id.password);
        userSessionManager=new UserSessionManager(getApplicationContext());


        if(userSessionManager.isUserLoggedIn()){
            startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
        }
        else{
            MessageToast.show(LoginActivity.this,"Please login to proceed");
        }


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Register.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!employeeId.getText().toString().equals("")) {
                    if (!password.getText().toString().equals("")) {
                        User user = new User();
                        user.setEmployeeId(employeeId.getText().toString());
                        user.setPassword(password.getText().toString());

                        request = new StringRequest(Request.Method.POST, UrlConstants.LOGIN_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String res = jsonObject.getString(VariableConstants.RESPONSE);
                                    if (res.equals(VariableConstants.RESPONSE_SUCCESS)) {
                                        userSessionManager.createUserLoginSession(user.getEmployeeId(), user.getPassword());
                                        MessageToast.show(LoginActivity.this, "Welcome to Trip App");
                                        startActivity(new Intent(LoginActivity.this, NavigationDrawer.class));
                                    } else {
                                        MessageToast.show(LoginActivity.this, res);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                MessageToast.show(LoginActivity.this, "Failed to connect to database, please check if you hve internet connection or contact admin");
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("employee_id", user.getEmployeeId());
                                params.put("password", user.getPassword());
                                return params;
                            }
                        };
                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

                    } else {
                        //empty fields
                        employeeId.setFocusable(true);
                        employeeId.setError("Employee ID required");
                    }
                }
                else{
                    employeeId.setFocusable(true);
                    employeeId.setError("Password required");
                }
            }
        });

    }



}
