package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Compra;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.databinding.ActivityTelaCompraBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TelaCompraActivity extends AppCompatActivity {

    private ActivityTelaCompraBinding binding;
    private Evento evento;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTelaCompraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("USER_ID", -1);
        if(userId == -1) {
            Toast.makeText(this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        buscarEvento();
        ativarSpinner();
        comprarIngresso();
        cancelar();
    }

    private void buscarEvento() {
        int idEvento = getIntent().getIntExtra("idEvento", -1);

        evento = AppDatabase.getDatabase(this)
                .eventoDAO()
                .buscarEventoSemFoto(idEvento);

        if (evento == null) {
            Toast.makeText(this, "Evento não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.txtTituloEvento.setText(evento.getNome());
        binding.txtQuantidade.setText("Ingressos disponíveis: " + evento.getNumIngressos());
    }

    private void ativarSpinner() {
        if (evento == null) return;

        int ingressosDisponiveis = evento.getNumIngressos();
        int maxOpcoes = Math.min(ingressosDisponiveis, 10);

        List<String> opcoes = new ArrayList<>();
        opcoes.add("Selecione");
        for (int i = 1; i <= maxOpcoes; i++) {
            opcoes.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opcoes
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerQuantidade.setAdapter(adapter);

        if (ingressosDisponiveis == 0) {
            binding.spinnerQuantidade.setEnabled(false);
            binding.btnComprar.setEnabled(false);
            Toast.makeText(this, "Ingressos esgotados!", Toast.LENGTH_SHORT).show();
        }
    }

    private void comprarIngresso() {
        binding.btnComprar.setOnClickListener(v -> {
            int selectedPosition = binding.spinnerQuantidade.getSelectedItemPosition();

            if (selectedPosition == 0) {
                Toast.makeText(this, "Selecione a quantidade", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantidade = Integer.parseInt(binding.spinnerQuantidade.getSelectedItem().toString());

            if (quantidade > evento.getNumIngressos()) {
                Toast.makeText(this, "Quantidade indisponível", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getDatabase(TelaCompraActivity.this);

                Compra novaCompra = new Compra(userId, evento.getIdEvento(), quantidade);
                db.compraDAO().inserir(novaCompra);

                int linhasAtualizadas = db.eventoDAO().decrementarIngressos(
                        evento.getIdEvento(),
                        quantidade
                );

                runOnUiThread(() -> {
                    if (linhasAtualizadas > 0) {
                        irParaCompraConfirmada(evento.getNome(), quantidade);
                    } else {
                        Toast.makeText(
                                TelaCompraActivity.this,
                                "Falha na compra. Tente novamente.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            });
        });
    }

    private void irParaCompraConfirmada(String nomeEvento, int quantidade) {
        Intent intent = new Intent(this, CompraConfirmadaActivity.class);
        intent.putExtra("evento_nome", nomeEvento);
        intent.putExtra("quantidade", quantidade);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void cancelar() {
        binding.btnCancelar.setOnClickListener(v -> finish());
    }
}