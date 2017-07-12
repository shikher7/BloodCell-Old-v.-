package com.example.shikher.bloodcell.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shikher.bloodcell.Background.Background_Request;
import com.example.shikher.bloodcell.Background.city_JSONResponse;
import com.example.shikher.bloodcell.Background.city_RequestInterface;
import com.example.shikher.bloodcell.R;
import com.example.shikher.bloodcell.Utils.Adapter.DataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shikher on 14-06-2017.
 */

public class FragmentRequest extends Fragment {
    @BindView(R.id.spinner_bloodbank)
    Spinner bloodbank;
    @BindView(R.id.spinner_component)
    Spinner component;
    @BindView(R.id.spinner_bloodgroup)
    Spinner bloogroup;
    @BindView(R.id.city_spinner)
    Spinner city;
    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.age)
    EditText age;/*
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.female)
    RadioButton female;
    @BindView(R.id.others)
    RadioButton others;
    @BindView(R.id.date)
    EditText  date;
*/  @BindView(R.id.datePicker)
    DatePicker date;
    @BindView(R.id.doctor_name)
    EditText  doctorName;
    @BindView(R.id.hospital_name)
    EditText  hospitalName;

    public static final String BASE_URL = "http://weberservice.co.in";

    private ArrayList<city_JSONResponse.AndroidVersion> mArrayList;



    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_request, container, false);

            ButterKnife.bind(this, rootView);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            city_RequestInterface request = retrofit.create(city_RequestInterface.class);
            Call<city_JSONResponse> call = request.getJSON();
            call.enqueue(new Callback<city_JSONResponse>() {

                @Override
                public void onResponse(Call<city_JSONResponse> call, Response<city_JSONResponse> response) {

                    city_JSONResponse jsonResponse = response.body();
                    mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
                    String Blood_Banks[]=new String[mArrayList.size()];
                    for(int i=0;i<mArrayList.size();i++)
                    {
                        Blood_Banks[i]=mArrayList.get(i).getBankName().toString();
                    }

            ArrayAdapter<String> adapter1= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Blood_Banks);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bloodbank.setAdapter(adapter1);
            ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Blood_Group));
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bloogroup.setAdapter(adapter2);
            ArrayAdapter<String> adapter3= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Cities));
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            city.setAdapter(adapter3);
            ArrayAdapter<String> adapter4= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Blood_Component));
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            component.setAdapter(adapter4);
                }

                @Override
                public void onFailure(Call<city_JSONResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
            return rootView;

        }
    @OnClick(R.id.button_request)
    public void onDonateSubmit(View v) {
        String citys = city.getSelectedItem().toString();
        String bloodbanks = bloodbank.getSelectedItem().toString();
        String bloodgroups = bloogroup.getSelectedItem().toString();
        String first_name=firstName.getText().toString();
        String last_name=lastName.getText().toString();
        String descriptions=description.getText().toString();
        String ages=age.getText().toString();
        String doctor_name=doctorName.getText().toString();
        String hospital_name=hospitalName.getText().toString();
        String components=component.getSelectedItem().toString();
        int   day  = date.getDayOfMonth();
        int   month= date.getMonth();
        int   year = date.getYear();
        year=year-1900;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dates = sdf.format(new Date(year, month, day));


        if(citys.matches("")||bloodbanks.matches("")||bloodgroups.matches("")||first_name.matches("")||
                last_name.matches("")||descriptions.matches("")||ages.matches("")||doctor_name.matches("")||hospital_name.matches(""))
            Toast.makeText(getActivity(), "All Fields are not filled.", Toast.LENGTH_LONG).show();
        else {

            String type = "request_submit";
            Background_Request backgroundRequest = new Background_Request(getActivity());
            backgroundRequest.execute(type, citys, bloodbanks, bloodgroups, first_name, last_name,
                    descriptions, ages, doctor_name, hospital_name, dates,components);
            getActivity().finish();
        }
    }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }


