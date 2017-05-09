package com.example.a2sebaj04.pointsofinterests;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 2sebaj04 on 25/04/2017.
 */
public class SetLocation extends Activity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sla);

        Button regular = (Button)findViewById(R.id.submitButton);
        regular.setOnClickListener(this);

    }

    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle bundle= new Bundle();

        EditText latitudeEditText = (EditText) findViewById(R.id.latitude);
        double latitude = Double.parseDouble(latitudeEditText.getText().toString());

        EditText longitudeEditText = (EditText) findViewById(R.id.longitude);
        double longitude = Double.parseDouble(longitudeEditText.getText().toString());

        bundle.putDouble("com.example.latitude",latitude);
        bundle.putDouble("com.example.longitude",longitude);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();

    }

}
