package com.mystudiopersonal.calcoloossigeno;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editVol, editBar, editFlusso, editViaggio;
    private LinearLayout resultContainer;
    private TextView txtAutonomia, txtRichiesto, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        editVol = findViewById(R.id.vol);
        editBar = findViewById(R.id.bar);
        editFlusso = findViewById(R.id.flusso);
        editViaggio = findViewById(R.id.viaggio);
        
        resultContainer = findViewById(R.id.risultato_container);
        txtAutonomia = findViewById(R.id.txt_autonomia);
        txtRichiesto = findViewById(R.id.txt_richiesto);
        txtStatus = findViewById(R.id.txt_status);
        
        Button btnCalcola = findViewById(R.id.btn_calcola);
        btnCalcola.setOnClickListener(v -> calcola());
    }

    private void calcola() {
        String volStr = editVol.getText().toString();
        String barStr = editBar.getText().toString();
        String flussoStr = editFlusso.getText().toString();
        String viaggioStr = editViaggio.getText().toString();

        if (volStr.isEmpty() || barStr.isEmpty() || flussoStr.isEmpty() || viaggioStr.isEmpty()) {
            Toast.makeText(this, R.string.error_missing_data, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double v = Double.parseDouble(volStr);
            double bPiena = Double.parseDouble(barStr);
            double f = Double.parseDouble(flussoStr);
            double viaggioMin = Double.parseDouble(viaggioStr);

            if (f <= 0) {
                Toast.makeText(this, R.string.error_missing_data, Toast.LENGTH_SHORT).show();
                return;
            }

            double bUtili = bPiena - 20; 
            double autonomiaMinTotal = (v * bUtili) / f;
            double tempoConSicurezza = viaggioMin * 1.2; 

            int ore = (int) (autonomiaMinTotal / 60);
            int min = (int) Math.round(autonomiaMinTotal % 60);

            resultContainer.setVisibility(View.VISIBLE);
            
            txtAutonomia.setText(getString(R.string.result_autonomia, ore, min));
            txtRichiesto.setText(getString(R.string.result_required, (int) Math.round(tempoConSicurezza)));

            if (autonomiaMinTotal >= tempoConSicurezza) {
                resultContainer.setBackgroundResource(R.drawable.bg_result_success);
                txtStatus.setText(R.string.status_possible);
                txtStatus.setTextColor(ContextCompat.getColor(this, R.color.success_text_light));
                txtAutonomia.setTextColor(ContextCompat.getColor(this, R.color.success_text_light));
                txtRichiesto.setTextColor(ContextCompat.getColor(this, R.color.success_text_light));
            } else {
                resultContainer.setBackgroundResource(R.drawable.bg_result_danger);
                txtStatus.setText(R.string.status_insufficient);
                txtStatus.setTextColor(ContextCompat.getColor(this, R.color.danger_text_light));
                txtAutonomia.setTextColor(ContextCompat.getColor(this, R.color.danger_text_light));
                txtRichiesto.setTextColor(ContextCompat.getColor(this, R.color.danger_text_light));
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_missing_data, Toast.LENGTH_SHORT).show();
        }
    }
}