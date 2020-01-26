package com.example.girass;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ZikrDetails extends Fragment {

    TextView zikr, narriated, timeToRepeat;
    ImageView click, repeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_zikr_details, container, false);

        zikr = rootView.findViewById(R.id.zikr);
        narriated = rootView.findViewById(R.id.narriated);
        timeToRepeat = rootView.findViewById(R.id.time_repeat);
        ///-------------------------------------------
        click = rootView.findViewById(R.id.click);
        repeat = rootView.findViewById(R.id.repeat);

        Intent intent = getActivity().getIntent();
        String title = intent.getStringExtra("array");
        DataService dataService = new DataService();
        final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();
        for (int i = 0; i < headZikrObjects.length; i++) {

            if (headZikrObjects[i].TITLE.equals(title)) {

                //    HeadZikrObject h = headZikrObjects[i];
                ZikrObject[] zikrObject = headZikrObjects[i].AllAzkar;

          //      int index =new ZikrDetails().getId();
                for (int j = 0; j < zikrObject.length; j++) {


                    //Toast.makeText(getContext(), "this is "+ new ZikrDetails().getId(), Toast.LENGTH_SHORT).show();

                        zikr.setText(zikrObject[j].Details);
                        narriated.setText(zikrObject[j].Narriated);
                        timeToRepeat.setText(Integer.valueOf(zikrObject[j].TimesToRepeat).toString());


                }


                break;
            }
        }
        return rootView;
    }

}
