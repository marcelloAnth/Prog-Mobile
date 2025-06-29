package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Compra;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.R;
import com.example.atividade3.databinding.ActivityComprasRealizadasBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ComprasRealizadasActivity extends AppCompatActivity {

    private ActivityComprasRealizadasBinding binding;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComprasRealizadasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        carregarCompras();
        configurarVoltar();
    }

    private void carregarCompras() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Compra> compras = db.compraDAO().getComprasPorUsuario(userId);

            runOnUiThread(() -> {
                binding.lnlComprasRealizadas.removeAllViews();

                if (compras.isEmpty()) {
                    Toast.makeText(this, "Você ainda não realizou nenhuma compra", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Compra c : compras) {
                    View item = getLayoutInflater().inflate(R.layout.item_compra, binding.lnlComprasRealizadas, false);

                    TextView txtNome = item.findViewById(R.id.txtNomeEventoCompra);
                    TextView txtData = item.findViewById(R.id.txtDataCompra);
                    TextView txtQtd  = item.findViewById(R.id.txtQuantidadeCompra);


                    Evento e = db.eventoDAO().buscarEventoSemFoto(c.getIdEvento());
                    txtNome.setText(e != null ? e.getNome() : "Evento removido");
                    String dataStr = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(new Date(c.getDataCompra()));
                    txtData.setText(dataStr);

                    txtQtd.setText("Quantidade: " + c.getQuantidade());

                    binding.lnlComprasRealizadas.addView(item);
                }
            });
        });
    }

    private void configurarVoltar() {
        binding.btnVoltarEventos.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
