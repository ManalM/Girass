package com.example.girass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;


public class ListDialog extends DialogFragment {

    String chozenZikr;

    public  static  String selected ;
    public ListDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.custom_dialog, null);
        ListView lv;

        lv = v.findViewById(R.id.dialog_list);
        DataService dataService = new DataService();
        String[] headZikrObjects = dataService.GetChosenAzkar();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dialog_item, R.id.choose_zikr_item, headZikrObjects);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chozenZikr = parent.getItemAtPosition(position).toString();
                if(chozenZikr != null){

                }
            }
        });
        if (lv.getParent() != null) {
            ((ViewGroup) lv.getParent()).removeView(lv);
        }

        builder.setView(lv);


        return builder.create();
    }


}

