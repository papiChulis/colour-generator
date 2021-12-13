package com.example.colourchangerv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rl;
    Button hexBtn;
    Button copyClipboardBtn;

    CSVReader reader;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new HashMap<String, String>();
        rl = findViewById(R.id.rl);
        hexBtn = findViewById(R.id.hexBtn);
        copyClipboardBtn = findViewById(R.id.copyClipboardBtn);

        try {
            reader = new CSVReader(new FileReader("wikipedia_color_names.csv")); //Insert file name here
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                nextLine = reader.readNext();
                map.put(nextLine[0], nextLine[1]);
            }
        } catch (IOException e) {
            e.getMessage();
        }

    }

    public void setRandomARGBBackground(View view) {
        int[] rgb = getRandomRGB();
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];
        String hex = RGBToHex(r, g, b);

        rl.setBackgroundColor(Color.argb(255, r, g, b));
        if (isDark(r, g, b)) {
            hexBtn.setTextColor(Color.argb(255, 0, 0, 0));
        } else {
            hexBtn.setTextColor(Color.argb(255, 255, 255, 255));
        }
        hexBtn.setText(hex);
    }

    public void setClipboard(View view) {
        String hex = (String) hexBtn.getText();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Hex code", hex);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(MainActivity.this, hex + " has been copied to your clipboard!", Toast.LENGTH_SHORT).show();
    }

    private boolean isDark(int r, int g, int b) {
        return (r * 0.299 + g * 0.587 + b * 0.114) > 150;
    }

    private int[] getRandomRGB() {
        Random rand = new Random();
        int[] rgb = new int[3];

        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = rand.nextInt(256);
        }

        return rgb;
    }

    private String RGBToHex(int r, int g, int b) {
        String hex = String.format("#%02X%02X%02X", r, g, b);
        return hex;
    }
}