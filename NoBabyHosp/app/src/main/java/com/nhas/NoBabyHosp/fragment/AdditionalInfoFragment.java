package com.nhas.NoBabyHosp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nhas.NoBabyHosp.R;

public class AdditionalInfoFragment extends Fragment {

    private View view = null;

    public AdditionalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_additional_info, container, false);





        return view;
    }
}
