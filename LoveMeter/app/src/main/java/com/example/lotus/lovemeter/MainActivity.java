package com.example.lotus.lovemeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    Button button1;
    TextView textHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        button1 = (Button)findViewById(R.id.button);
        textHasil = (TextView)findViewById(R.id.textView2);


        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name1 = editText1.getText().toString();
                String name2 = editText2.getText().toString();

                System.out.println("nama 1= "+name1);
                System.out.println("nama 2= "+name2);
                HitungLove(name1, name2);
            }
        });
    }

    public void HitungLove(String name1, String name2)
    {
        char[] cName1 = name1.toCharArray();
        char[] cName2 = name2.toCharArray();
        int angka1 = 0;
        int angka2 = 0;
        int total1=0;
        int total2=0;

        for (int i =0; i<cName1.length; i++)
        {
            //ascii1[i] = (int) cName1[i];
            angka1 = charToNumber(cName1[i]);
            total1 = total1 + angka1;
        }

        for (int j =0; j<cName2.length; j++)
        {
            angka2 = charToNumber(cName2[j]);
            total2 = total2 + angka2;
        }

        System.out.println(total1);
        System.out.println(total2);
        int totalInt= 0, totalInt2=0;

        while (String.valueOf(total1).length() > 1) {

            while (total1 > 0) {
                totalInt += total1 % 10;
                total1 /= 10;
            }

            total1 = totalInt;
            totalInt = 0;
        }
        System.out.println("Total iNt asep "+ total1);

        while (String.valueOf(total2).length() > 1) {

            while (total2 > 0) {
                totalInt2 += total2 % 10;
                total2 /= 10;
            }

            total2 = totalInt2;
            totalInt2 = 0;
        }
        System.out.println("Total iNt asep "+ total2);

        if (total1>total2)
            textHasil.setText(total1+""+total2+" %");
        else
            textHasil.setText(total2+""+total1+" %");
    }

    public int charToNumber(char huruf){

        switch(Character.toLowerCase(huruf)) {
            case 'a':
            case 'j':
            case 's':
                return 1;
            case 'b':
            case 'k':
            case 't':
                return 2;
            case 'c':
            case 'l':
            case 'u':
                return 3;
            case 'd':
            case 'm':
            case 'v':
                return 4;
            case 'e':
            case 'n':
            case 'w':
                return 5;
            case 'f':
            case 'o':
            case 'x':
                return 6;
            case 'g':
            case 'p':
            case 'y':
                return 7;
            case 'h':
            case 'q':
            case 'z':
                return 8;
            default:
                return 9;
        }
    }
}
