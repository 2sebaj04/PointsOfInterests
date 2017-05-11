package com.example.a2sebaj04.pointsofinterests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 2sebaj04 on 11/05/2017.
 */
public class AddPOI extends Activity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apoi);
        Button B = (Button)findViewById(R.id.bt1);
        B.setOnClickListener(this);
    }

    public void onClick(View view){
        EditText placename = (EditText)findViewById(R.id.et1);
        String nameplace = placename.getText().toString();
        EditText placetype = (EditText)findViewById(R.id.et2);
        String typeplace = placetype.getText().toString();
        EditText placedes = (EditText)findViewById(R.id.et3);
        String desplace = placedes.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("com.example.et1", nameplace);
        bundle.putString("com.example.et2", typeplace);
        bundle.putString("com.example.et3", desplace);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();


    }
}

