package com.example.girass;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ZikrDetails extends Fragment {

TextView zikr , narriated,timeToRepeat;
ImageView click , repeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_zikr_details, container, false);

        zikr = rootView.findViewById(R.id.zikr);
        narriated = rootView.findViewById(R.id.narriated);
        timeToRepeat= rootView.findViewById(R.id.time_repeat);
        click = rootView.findViewById(R.id.click);
        repeat = rootView.findViewById(R.id.repeat);


        Intent intent = getActivity().getIntent();
        String title = intent.getStringExtra("array");
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(title)) {

                HeadZikrObject h = headZikrObjects[i];
                ZikrObject zikrObject = h.AllAzkar[i];
                zikr.setText(zikrObject.Details);
                narriated.setText(zikrObject.Narriated);
                timeToRepeat.setText(zikrObject.TimesToRepeat);


            }
        }
        return rootView;
    }

}
