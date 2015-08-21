/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android4dev.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android4dev.helpers.AppController;
import com.android4dev.navigationview.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * This shows how to place markers on a map.
 */
public class MarkerDemoActivity extends Fragment {

    private static final String URL_Link = "http://192.168.1.104/Asos/get_all_products.php";

    private GoogleMap googleMap;
    private static ArrayList<String> products = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_fragment, container, false);

        googleMap= ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();


        //products = new ArrayList<MarkerM>();

        makeJsonReq();
        //Move the camera instantly to hamburg with a zoom of 15.


        // Zoom in, animating the camera.


        /*Marker kiel = map.addMarker(new MarkerOptions()
                        .position(KIEL)
                        .title("Kiel")
                        .snippet("Kiel is cool")
        );


        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
*/
        //...



        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        return v;
    }

    private void makeJsonReq() {
        //showProgressDialog();
       // Log.e("inside","inside makeJsonReq with "+URL_Link);
        JsonObjectRequest req = new JsonObjectRequest(URL_Link,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {
                    String Link = response.toString();
           JSONObject  jsonRootObject = new JSONObject(Link);

         //Get the instance of JSONArray that contains JSONObjects
         JSONArray jsonArray = jsonRootObject.optJSONArray("markers");
                            String name_link ;
                            String  address_link;
                            double lat_link=0;
                            double lng=0;
         //Iterate the jsonArray and print the info of JSONObjects
         for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int pid_link= Integer.parseInt(jsonObject.optString("id").toString());
             name_link = jsonObject.optString("name").toString();
              address_link = jsonObject.optString("address").toString();
             lat_link = Double.parseDouble(jsonObject.optString("lat").toString());
             lng = Double.parseDouble(jsonObject.optString("lng").toString());
             Log.d("lang", Double.toString(lng));
            products.add(Double.toString(lat_link));
             products.add(Double.toString(lng));
             googleMap.addMarker(new MarkerOptions().position(new LatLng(
                     lat_link, lng))
                     .title(name_link)
                     .snippet(address_link));

                            }

                            LatLngBounds California = new LatLngBounds(
                                    new LatLng(Double.parseDouble(products.get(products.size() - 1)), Double.parseDouble(products.get(products.size() - 2))),
                                    new LatLng(Double.parseDouble(products.get(products.size()-3)), Double.parseDouble(products.get(products.size() - 4))));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(California.getCenter(), 10));

                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Calofirnia, 15));
                            // Zoom in, animating the camera.
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                          //  LatLng SYDNEY = new LatLng(37.387138,-122.083237);
                            // Move the camera instantly to Sydney with a zoom of 15.
                           // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 1));
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((lat_link lng), 15));
                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(products.get(products.size() - 1).name, 15));
                        }catch (JSONException e){
                            Log.d("Jsonerror", e.toString());
                        }
                        //Log.d("product",products.toString());


                        //setView();
                        //msgResponse.setText(response.toString());
                        //hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.getMessage());
                //hideProgressDialog();
            }
        });
        //Log.d("req",req.toString());
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(req, "jSon_obj");

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

}
