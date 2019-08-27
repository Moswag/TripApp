package com.alium.niboexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alium.niboexample.model.User;
import com.alium.niboexample.util.MessageToast;
import com.alium.niboexample.util.SHA1;
import com.alium.niboexample.util.UrlConstants;
import com.alium.niboexample.util.VariableConstants;
import com.alium.niboexample.util.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    StringRequest request;

    private EditText employeeId;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button register;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        employeeId=(EditText) findViewById(R.id.employee_id);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        confirmPassword=(EditText) findViewById(R.id.confirm_password);

        register=(Button) findViewById(R.id.btnRegister);

        backToLogin=(TextView) findViewById(R.id.backToLogin);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTvs()){
                    MessageToast.show(Register.this,"Please fill in all fields");
                }
                else{
                    User user=new User();
                    user.setEmployeeId(employeeId.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(SHA1.sha(password.getText().toString()));

                    adduser(user);


                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));
            }
        });



    }


    private boolean checkTvs(){
        if(employeeId.getText().toString().equals("")){
            employeeId.setFocusable(true);
            employeeId.setError("Employee ID required");
            return true;
        }
        if(email.getText().toString().equals("")){
            email.setFocusable(true);
            email.setError("Email required");
            return true;
        }

        if(password.getText().toString().equals("")){
            password.setFocusable(true);
            password.setError("Password required");
            return true;
        }
        if(confirmPassword.getText().toString().equals("")){
            confirmPassword.setFocusable(true);
            confirmPassword.setError("Confirm Password required");
            return true;
        }
        if(password.getText().toString().length()<6){
            password.setFocusable(true);
            password.setError("Password should be at lest 6 characters");
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            password.setFocusable(true);
            password.setError("Passwords should match");
            return true;
        }

        else{
            return false;
        }
    }


    private boolean adduser(User user){
        request =new StringRequest(Request.Method.POST, UrlConstants.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(VariableConstants.RESPONSE);
                    if(res.equals(VariableConstants.RESPONSE_SUCCESS)){
                        MessageToast.show(Register.this,"You have been successfully registered, please login to proceed");
                        startActivity(new Intent(Register.this,LoginActivity.class));
                    }
                    else{
                        MessageToast.show(Register.this,res);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(Register.this,"Failed to connect to database, please check if you hve internet connection or contact admin");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("employee_id",user.getEmployeeId());
                params.put("email",user.getEmail());
                params.put("password",user.getPassword());
                return params;

            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        return true;
    }
}
