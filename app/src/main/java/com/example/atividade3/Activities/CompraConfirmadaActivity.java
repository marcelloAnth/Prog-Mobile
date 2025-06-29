package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.R;

public class CompraConfirmadaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_confirmada);


        String eventoNome = getIntent().getStringExtra("evento_nome");
        int quantidade = getIntent().getIntExtra("quantidade", 0);

        TextView txtResumo = findViewById(R.id.txtResumoCompra);
        txtResumo.setText(String.format("Você comprou %d ingresso(s) para:\n%s", quantidade, eventoNome));


        Button btnVoltar = findViewById(R.id.btnVoltarEventos);
        btnVoltar.setOnClickListener(v -> voltarEventos());
    }

    private void voltarEventos() {
        Intent intent = new Intent(this, EventosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}