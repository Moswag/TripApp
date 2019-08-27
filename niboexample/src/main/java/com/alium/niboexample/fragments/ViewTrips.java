package com.alium.niboexample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alium.niboexample.R;
import com.alium.niboexample.Register;
import com.alium.niboexample.adapter.TripAdapter;
import com.alium.niboexample.model.Trip;
import com.alium.niboexample.util.MessageToast;
import com.alium.niboexample.util.UrlConstants;
import com.alium.niboexample.util.UserSessionManager;
import com.alium.niboexample.util.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTrips extends Fragment {
    private StringRequest request;
    private UserSessionManager sessionManager;
    private RecyclerView recyclerView;
    private TripAdapter adapter;


    public ViewTrips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_trips, container, false);

        sessionManager=new UserSessionManager(getActivity());

        HashMap<String,String> session=sessionManager.getUserDetails();
        String employeeId=session.get(UserSessionManager.KEY_User);

        recyclerView=(RecyclerView) view.findViewById(R.id.listshow);
        recyclerView.setHasFixedSize(true);

        getTrips(employeeId);

        return view;
    }

    private void getTrips(final String employee_id){
        final List<Trip> myTrips=new ArrayList<>();
        request=new StringRequest(Request.Method.POST, UrlConstants.GET_TRIPS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                        Trip trip=new Trip();
                        trip.setTripId(jsonObject.getString("tripId"));
                        trip.setEmployeeId(jsonObject.getString("employeeId"));
                        trip.setOrigin(jsonObject.getString("origin"));
                        trip.setDestination(jsonObject.getString("destination"));
                        trip.setTripStartDate(jsonObject.getString("tripStartDate"));
                        trip.setTripStartTime(jsonObject.getString("tripStartTime"));
                        trip.setTripEndDate(jsonObject.getString("tripEndDate"));
                        trip.setDistance(jsonObject.getString("distance"));
                        trip.setStatus(jsonObject.getString("status"));
                        myTrips.add(trip);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setuprecyclerview(myTrips);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(getActivity(),"Failed to connect to database, please check if you hve internet connection or contact admin");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("employee_id",employee_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void setuprecyclerview(List<Trip> lstTrips) {
        //
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new TripAdapter(getActivity(),lstTrips);
        recyclerView.setAdapter(adapter);


    }

}
