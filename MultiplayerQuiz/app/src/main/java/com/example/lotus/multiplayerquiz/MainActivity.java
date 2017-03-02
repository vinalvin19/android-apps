package com.example.lotus.multiplayerquiz;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String[][] bank = new String[][]{
            {"Bumi itu bulat", "Ya"},
            {"Mars bukan planet", "Tidak"},
            {"Matahari itu panas", "Ya"},
            {"Matahari itu dingin", "Tidak"},
            {"Awan warnanya putih", "Ya"},
            {"Tulang warnanya putih", "Ya"},
            {"Sapi minumnya susu", "Tidak"},
            {"Nasi dibuat dari beras", "Ya"},
            {"Kucing itu mengeong", "Ya"},
            {"Anjing itu mengembik", "Tidak"},
            {"Macan itu mengaum", "Ya"},
    };

    TextView soal, soalbawah, hasil, pointp1, pointp2;
    Button kiriAtas, kiriBawah, kananAtas, kananBawah, startGame;
    Timer buttonTimer = new Timer();

    private static String jawaban="oo";
    //private static String jawabanPlayer2="bb";
    public String namaButton="kiriAtas";
    public int counter=0, pointPlayer1=0, pointPlayer2=0, i=0, player=0, kode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soal = (TextView)findViewById(R.id.soal);
        soalbawah = (TextView)findViewById(R.id.soal2);
        //jawaban = (TextView)findViewById(R.id.jawaban);
        //hasil = (TextView)findViewById(R.id.hasil);
        pointp1 = (TextView)findViewById(R.id.pointp1);
        pointp2 = (TextView)findViewById(R.id.pointp2);
        kiriAtas = (Button)findViewById(R.id.kiriatas);
        kiriBawah = (Button)findViewById(R.id.kiribawah);
        kananAtas = (Button)findViewById(R.id.kananatas);
        kananBawah = (Button)findViewById(R.id.kananbawah);
        startGame = (Button)findViewById(R.id.mulai);

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soal.setText(bank[0][0]);
                soalbawah.setText(bank[0][0]);
                //jawaban.setText(bank[0][1]);
                startGame.setVisibility(View.GONE);
            }
        });

        kiriAtas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jawaban="Ya";
                player=1;
                update(player, jawaban);
                Log.i("TAG", "jawaban " + jawaban + " : counter= " + i);
                Log.i("TAG", "kiriatas");

                //soal.setText("Tombol kiri atas");
                kananAtas.setEnabled(false);
                kananAtas.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kiriBawah.setEnabled(false);
                kiriBawah.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kananBawah.setEnabled(false);
                kananBawah.setBackgroundColor(Color.parseColor("#E1F4FB"));

                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                kananAtas.setEnabled(true);
                                kananAtas.setBackgroundColor(Color.parseColor("#EE4444"));
                                kiriBawah.setEnabled(true);
                                kiriBawah.setBackgroundColor(Color.parseColor("#EE4444"));
                                kananBawah.setEnabled(true);
                                kananBawah.setBackgroundColor(Color.parseColor("#33B5E5"));
                            }
                        });
                    }
                }, 2000);
            }
        });

        kananAtas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jawaban="Tidak";
                player=1;
                update(player, jawaban);
                Log.i("TAG", "jawaban " + jawaban + " : counter= " + i);
                Log.i("TAG", "kananatas");

                kiriAtas.setEnabled(false);
                kiriAtas.setBackgroundColor(Color.parseColor("#E1F4FB"));
                kiriBawah.setEnabled(false);
                kiriBawah.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kananBawah.setEnabled(false);
                kananBawah.setBackgroundColor(Color.parseColor("#E1F4FB"));

                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                kiriAtas.setEnabled(true);
                                kiriAtas.setBackgroundColor(Color.parseColor("#33B5E5"));
                                kiriBawah.setEnabled(true);
                                kiriBawah.setBackgroundColor(Color.parseColor("#EE4444"));
                                kananBawah.setEnabled(true);
                                kananBawah.setBackgroundColor(Color.parseColor("#33B5E5"));
                            }
                        });
                    }
                }, 2000);
            }
        });

        // biru biasa: 33B5E5
        // biru soft: E1F4FB
        // merah biasa: EE4444
        // merah soft: FFE1E1

        kiriBawah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jawaban="Tidak";
                player=2;
                update(player, jawaban);
                Log.i("TAG", "jawaban " + jawaban+ " : counter= " + i);
                Log.i("TAG", "kiribawah");

                kananAtas.setEnabled(false);
                kananAtas.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kiriAtas.setEnabled(false);
                kiriAtas.setBackgroundColor(Color.parseColor("#E1F4FB"));
                kananBawah.setEnabled(false);
                kananBawah.setBackgroundColor(Color.parseColor("#E1F4FB"));

                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                kananAtas.setEnabled(true);
                                kananAtas.setBackgroundColor(Color.parseColor("#EE4444"));
                                kiriAtas.setEnabled(true);
                                kiriAtas.setBackgroundColor(Color.parseColor("#33B5E5"));
                                kananBawah.setEnabled(true);
                                kananBawah.setBackgroundColor(Color.parseColor("#33B5E5"));

                            }
                        });
                    }
                }, 2000);
            }
        });

        kananBawah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jawaban="Ya";
                player=2;
                update(player, jawaban);
                Log.i("TAG", "jawaban " + jawaban + " : counter= " + i);
                Log.i("TAG", "kananbawah");

                kananAtas.setEnabled(false);
                kananAtas.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kiriBawah.setEnabled(false);
                kiriBawah.setBackgroundColor(Color.parseColor("#FFE1E1"));
                kiriAtas.setEnabled(false);
                kiriAtas.setBackgroundColor(Color.parseColor("#E1F4FB"));

                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                kananAtas.setEnabled(true);
                                kananAtas.setBackgroundColor(Color.parseColor("#EE4444"));
                                kiriBawah.setEnabled(true);
                                kiriBawah.setBackgroundColor(Color.parseColor("#EE4444"));
                                kiriAtas.setEnabled(true);
                                kiriAtas.setBackgroundColor(Color.parseColor("#33B5E5"));
                            }
                        });
                    }
                }, 2000);
            }
        });
    }

    public void update(int player, String answer){

        Log.i("TAG", answer + bank[i][1]);

        if (answer == bank[i][1])
        {
            if (player==1)
            {
                pointPlayer1++;
                pointp1.setText(String.valueOf(pointPlayer1));
            }

            else {
                pointPlayer2++;
                pointp2.setText(String.valueOf(pointPlayer2));
            }
        }
        else
        {
            if (player==1)
            {
                pointPlayer1--;
                pointp1.setText(String.valueOf(pointPlayer1));
            }

            else {
                pointPlayer2--;
                pointp2.setText(String.valueOf(pointPlayer2));
            }
        }

        i++;
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        soal.setText(bank[i][0]);
                        soalbawah.setText(bank[i][0]);
                    }
                });
            }
        }, 2000);
        //jawaban.setText(bank[i][1]);
    }
}
