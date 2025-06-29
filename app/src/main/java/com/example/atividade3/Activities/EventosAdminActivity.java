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
import com.example.atividade3.databinding.ActivityEventosAdminBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class EventosAdminActivity extends AppCompatActivity {

    private ActivityEventosAdminBinding binding;
    private Evento eventoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventosAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        carregarEventos();
        configurarBotaoAdicionar();
        configurarBotaoExcluir();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEventos();
    }

    private void carregarEventos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Evento> eventos = AppDatabase.getDatabase(this).eventoDAO().buscarEventosSemFoto();

            runOnUiThread(() -> {
                binding.lnlEventosDisponiveisAdmin.removeAllViews();

                if (eventos.isEmpty()) {
                    Toast.makeText(this, "Nenhum evento cadastrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Evento evento : eventos) {
                    View eventoView = getLayoutInflater().inflate(R.layout.item_evento, binding.lnlEventosDisponiveisAdmin, false);

                    TextView txtNomeEvento = eventoView.findViewById(R.id.txtNomeEvento);
                    TextView txtLocalEvento = eventoView.findViewById(R.id.txtLocalEvento);
                    TextView txtDataEvento = eventoView.findViewById(R.id.txtDataEvento);
                    TextView txtDescricaoEvento = eventoView.findViewById(R.id.txtDescricaoEvento);

                    txtNomeEvento.setText(evento.getNome());
                    txtLocalEvento.setText(evento.getLocal());
                    String dataFormatada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(evento.getData());
                    txtDataEvento.setText(dataFormatada);
                    txtDescricaoEvento.setText(evento.getDescricao());

                    eventoView.setOnClickListener(v -> {
                        eventoSelecionado = evento;
                        Toast.makeText(this, "Selecionado: " + evento.getNome(), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < binding.lnlEventosDisponiveisAdmin.getChildCount(); i++) {
                            binding.lnlEventosDisponiveisAdmin.getChildAt(i)
                                    .setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                        }
                        eventoView.setBackgroundColor(ContextCompat.getColor(this, R.color.cinza_desabilitado));
                    });

                    binding.lnlEventosDisponiveisAdmin.addView(eventoView);
                }
            });
        });
    }

    private void configurarBotaoAdicionar() {
        binding.btnAdicionarEvento.setOnClickListener(v -> abrirTelaCadastroEvento());
    }

    private void configurarBotaoExcluir() {
        binding.btnExcluirEvento.setOnClickListener(v -> excluirEventoSelecionado());
    }

    private void abrirTelaCadastroEvento() {
        Intent intent = new Intent(this, CadastroEventoActivity.class);
        startActivity(intent);
    }

    private void excluirEventoSelecionado() {
        if (eventoSelecionado == null) {
            Toast.makeText(this, "Selecione um evento para excluir", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getDatabase(this).eventoDAO().excluir(eventoSelecionado);

            runOnUiThread(() -> {
                Toast.makeText(this, "Evento excluído com sucesso", Toast.LENGTH_SHORT).show();
                eventoSelecionado = null;
                carregarEventos();
            });
        });
    }
}
