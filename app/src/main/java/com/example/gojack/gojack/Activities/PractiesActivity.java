package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Im033 on 1/9/2017.
 */

public class PractiesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Raja", 54);
        hashtable.put("Raja", 7);
        hashtable.put("gf", 7);
        hashtable.put("gf", 8);
        hashtable.put("gf", 8);
        hashtable.put("fghjkygk", 98);
        hashtable.put("fghjkygk", 90);
        hashtable.put("fghjkygk", 90);
        hashtable.put("Raja", 6);


//        hashtable.put(null, null);
      /*  System.out.println(hashtable.get(10));
        System.out.println(hashtable.get(100));
        System.out.println(hashtable.get(106));
        System.out.println(hashtable.get(99));*/
        Log.d("MerchatActivity", hashtable.toString());
    }


}
