package com.example.jc.timemanager;



/**
 * Created by JC on 6/4/2018.
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by JC on 6/3/2018.
 */



public class statsTab2 extends Fragment {

        ListView listView;
        final String TAG = "map";
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stats_tabbed2, container, false);
            listView = rootView.findViewById(R.id.listView);
            final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode

            final TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
            int day;
            final ArrayList<String> list = new ArrayList<>();
            final String[] arr = new String[31];
            int ct=0;

            //map items form shared preferences to arr
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    Log.i("map", entry.getKey());

                        arr[ct] = entry.getKey();
                        ct++;

                }

            //revers(swap) arr

            for(int i = 0; i < (pref.getInt("Day",0) / 2); i++)
            {
                String  temp = arr[i];
                arr[i] = arr[pref.getInt("Day",0) - i - 1];
                arr[pref.getInt("Day",0) - i - 1] = temp;
            }

            //case where day is max + 1
            if(pref.getInt("Day",0)==31) {
                for (int i = 1; i < arr.length; i++) {

                    if (arr[i] != null && arr[i].contains("_")) { //i < pref.getInt("Day", 0) &&
                        list.add(arr[i].substring(4, 6) + "/" + arr[i].substring(6, 8) + "/" + arr[i].substring(0, 4) + " @ " + arr[i].substring(9, 11) + ":" + arr[i].substring(11, 13) + ":" + arr[i].substring(13));
                    } else if(!arr[i].equals("Day"))
                        list.add("Day " + i);

                }
            }
            else {
                for (int i = 0; i < arr.length-1 ; i++) {

                    if (arr[i] != null && arr[i].contains("_")) { //i < pref.getInt("Day", 0) &&

                        list.add(arr[i].substring(4, 6) + "/" + arr[i].substring(6, 8) + "/" + arr[i].substring(0, 4) + " @ " + arr[i].substring(9, 11) + ":" + arr[i].substring(11, 13) + ":" + arr[i].substring(13));
                    } else
                        list.add("Day " + i);

                }
            }


          //set list view adapter
            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.my_list_view00,list);
            listView.setAdapter(arrayAdapter);



            if (!keys.isEmpty()) {
                //when item is pressed change to appropriate activity
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), "Opening day", Toast.LENGTH_SHORT).show();

                        if(i<pref.getInt("Day", 0)) {

                            Intent intent = new Intent(getActivity(), singleDay.class);
                            intent.putExtra("day", list.get(i));
                            String tmp="";
                            if(pref.getInt("Day",0)==31)
                                tmp = arr[i+1];
                            else
                                tmp = arr[i];
                            intent.putExtra("value", keys.get(tmp).toString());
                            intent.putExtra("key", tmp);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else{
                            Toast.makeText(getActivity(), "There is no save data for this day yet", Toast.LENGTH_LONG).show();
                        }



                    }
                });
            }

            return rootView;
        }
}
