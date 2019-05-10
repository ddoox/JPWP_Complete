package com.example.jpwp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Zad 1 - Dodanie przycisku oraz nadanie mu id "btn" oraz przypisanie go do zmiennej

        Button btn = (Button) findViewById(R.id.btn);

        //Zad 2 utworzenie onClick dla przycisku


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click();

            }
        });





    }

    //Zad 2 stworzenie funkcji, a w niej obiektu intent przenoszącego do nowej activity
    public void click()
    {
        Intent intent = new Intent(this,com.example.jpwp.MapsActivity.class);
        intent.putExtra("id_wiadomosci","tresc_wiadomosci");
        startActivity(intent);


    }
}
