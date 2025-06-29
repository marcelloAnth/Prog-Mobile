package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.R;
import com.example.atividade3.databinding.ActivityEventosBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventosActivity extends AppCompatActivity {

    private ActivityEventosBinding binding;
    private Evento eventoSelecionado;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preencherEventos();
        configurarComprarIngresso();
        configurarVerificarCompras();
    }

    private void preencherEventos() {
        new Thread(() -> {
            List<Evento> eventos = AppDatabase.getDatabase(this)
                    .eventoDAO()
                    .buscarEventosSemFoto();

            runOnUiThread(() -> {
                binding.lnlEventosDisponiveis.removeAllViews();

                if (eventos.isEmpty()) {
                    Toast.makeText(this, "Nenhum evento disponível", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Evento evento : eventos) {
                    View eventoView = getLayoutInflater()
                            .inflate(R.layout.item_evento, binding.lnlEventosDisponiveis, false);

                    TextView txtNomeEvento = eventoView.findViewById(R.id.txtNomeEvento);
                    TextView txtLocalEvento = eventoView.findViewById(R.id.txtLocalEvento);
                    TextView txtDataEvento = eventoView.findViewById(R.id.txtDataEvento);
                    TextView txtDescricaoEvento = eventoView.findViewById(R.id.txtDescricaoEvento);

                    txtNomeEvento.setText(evento.getNome());
                    txtLocalEvento.setText(evento.getLocal());
                    String dataFormatada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(evento.getData());
                    txtDataEvento.setText(dataFormatada);
                    txtDescricaoEvento.setText(evento.getDescricao());

                    eventoView.setOnClickListener(v -> {
                        eventoSelecionado = evento;
                        Toast.makeText(this, "Selecionado: " + evento.getNome(), Toast.LENGTH_SHORT).show();
                        resetarSelecao();
                        eventoView.setBackgroundColor(
                                ContextCompat.getColor(this, R.color.cinza_desabilitado));
                    });

                    binding.lnlEventosDisponiveis.addView(eventoView);
                }
            });
        }).start();
    }

    private void resetarSelecao() {
        for (int i = 0; i < binding.lnlEventosDisponiveis.getChildCount(); i++) {
            binding.lnlEventosDisponiveis.getChildAt(i)
                    .setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        }
    }

    private void configurarComprarIngresso() {
        binding.btnComprarIngresso.setOnClickListener(v -> {
            if (eventoSelecionado == null) {
                Toast.makeText(this, "Selecione um evento antes de comprar", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, TelaCompraActivity.class);
            intent.putExtra("idEvento", eventoSelecionado.getIdEvento());
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void configurarVerificarCompras() {
        binding.btnVerificarCompras.setOnClickListener(v -> {
            Intent intent = new Intent(this, ComprasRealizadasActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }
}
