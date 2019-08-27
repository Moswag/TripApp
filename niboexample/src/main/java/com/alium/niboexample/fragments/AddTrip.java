package com.alium.niboexample.fragments;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alium.nibo.models.NiboSelectedOriginDestination;
import com.alium.nibo.models.NiboSelectedPlace;
import com.alium.nibo.origindestinationpicker.NiboOriginDestinationPickerActivity;
import com.alium.nibo.utils.NiboConstants;
import com.alium.nibo.utils.NiboStyle;
import com.alium.niboexample.NavigationDrawer;
import com.alium.niboexample.R;
import com.alium.niboexample.model.Trip;
import com.alium.niboexample.util.CustomProgressDialog;
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
import java.util.jar.Attributes;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTrip extends Fragment {
    StringRequest request;
    UserSessionManager userSessionManager;


    public AddTrip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_trip, container, false);
        userSessionManager=new UserSessionManager(getActivity());
        launchStartFinishActivity();
        return view;
    }

    private void launchStartFinishActivity() {
        Intent intent = new Intent(getActivity(), NiboOriginDestinationPickerActivity.class);

        NiboOriginDestinationPickerActivity.NiboOriginDestinationPickerBuilder config = new NiboOriginDestinationPickerActivity.NiboOriginDestinationPickerBuilder()
                .setDestinationMarkerPinIconRes(R.drawable.ic_map_marker_black_36dp)
                .setOriginMarkerPinIconRes(R.drawable.ic_map_marker_black_36dp)
                //.setBackButtonIconRes(R.drawable.arrow_left)
                .setOriginEditTextHint("Input pick up location")
//                .setPrimaryPolyLineColor(R.color.colorPrimary)
//                .setSecondaryPolyLineColor(R.color.colorAccent)
                .setDestinationEditTextHint("Input destination")
                .setStyleEnum(NiboStyle.SUBTLE_GREY_SCALE);

        NiboOriginDestinationPickerActivity.setBuilder(config);
        startActivityForResult(intent, 200);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 300) {
            NiboSelectedPlace selectedPlace = data.getParcelableExtra(NiboConstants.SELECTED_PLACE_RESULT);
            Toast.makeText(getActivity(), selectedPlace.getPlaceAddress(), Toast.LENGTH_LONG).show();
        } else if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            NiboSelectedOriginDestination selectedOriginDestination =data.getParcelableExtra(NiboConstants.SELECTED_ORIGIN_DESTINATION_RESULT);



        float distance=getDistance(
                selectedOriginDestination.getOriginLatLng().latitude,
                 selectedOriginDestination.getOriginLatLng().longitude
                ,selectedOriginDestination.getDestinationLatLng().latitude,
                selectedOriginDestination.getDestinationLatLng().latitude);
        float distanceInKm=(float)distance/1000;
//            Toast.makeText(getActivity(),
//                    selectedOriginDestination.getOriginFullName() +
//                            " - " +
//                            selectedOriginDestination.getDestinationFullName()+
//                            " and the distance is: "+distanceInKm+" KM"
//                    ,        Toast.LENGTH_LONG).show();
//
//            Toast.makeText(getActivity(), selectedOriginDestination.getStartDate() +" and end date is "+
//                    selectedOriginDestination.getEndDate()+" and Time of jorney "+ selectedOriginDestination.getJourneyTime(), Toast.LENGTH_LONG).show();
            HashMap<String,String> session=userSessionManager.getUserDetails();
            String employeeId=session.get(UserSessionManager.KEY_User);

            Trip trip=new Trip();
            trip.setEmployeeId(employeeId);
            trip.setOrigin(selectedOriginDestination.getOriginFullName());
            trip.setDestination(selectedOriginDestination.getDestinationFullName());
            trip.setDistance(String.valueOf(distanceInKm)+" KM");
            trip.setTripStartDate(selectedOriginDestination.getStartDate());
            trip.setTripEndDate(selectedOriginDestination.getEndDate());
            trip.setTripStartTime(selectedOriginDestination.getJourneyTime());

            addTrip(trip);



        } else {
            MessageToast.show(getActivity(),"Error getting results");
        }

    }

    private float getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] distance = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }


    public void addTrip(Trip trip){
        request=new StringRequest(Request.Method.POST, UrlConstants.ADD_TRIP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String res=jsonObject.getString(VariableConstants.RESPONSE);
                    if(res.equals(VariableConstants.RESPONSE_SUCCESS)){
//                        CustomProgressDialog.showDialog(getActivity(),"Reporting",view,
//                                "You successfully reported an emergency, wait for your response",
//                                R.layout.dialog_success);
                        MessageToast.show(getActivity(),"Trip successfully added");
                        startActivity(new Intent(getActivity(),NavigationDrawer.class));
                    }
                    else{
                        MessageToast.show(getActivity(),"Failed to add trip");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MessageToast.show(getActivity(),"Failed to connect to database");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("employee_id",trip.getEmployeeId());
                params.put("origin",trip.getOrigin());
                params.put("destination",trip.getDestination());
                params.put("distance",trip.getDistance());
                params.put("tripStartDate",trip.getTripStartDate());
                params.put("tripEndDate",trip.getTripEndDate());
                params.put("tripStartTime",trip.getTripStartTime());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}
