package com.onewaree.sispu.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onewaree.sispu.R;

/**
 * Created by jordao on 13/11/16.
 */

public class FormFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form, null);
        final EditText nome = (EditText) rootView.findViewById(R.id.form_nome);
        Button bnt = (Button) rootView.findViewById(R.id.form_bnt);
        final TextView label = (TextView) rootView.findViewById(R.id.form_value);

        bnt.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                label.setText(nome.getText().toString());
            }
        });

        Log.d("FORM:", nome.getText().toString());

        return inflater.inflate(R.layout.fragment_form,container,false);
    }
}
