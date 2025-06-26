package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.R;
import com.example.atividade3.databinding.ActivityTrocaSenhaBinding;

import org.mindrot.jbcrypt.BCrypt;

public class TrocaSenhaActivity extends AppCompatActivity {

    private ActivityTrocaSenhaBinding binding;
    private Usuario usuarioEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrocaSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        habilitarBotoes(false);
        buscarCadastro();
        alterarSenha();
        cancelar();
    }

    private void buscarCadastro() {
        binding.btnBuscarCadastro.setOnClickListener(v -> {
            String login = binding.edtLogin.getText().toString().trim();

            if (login.isEmpty()) {
                Toast.makeText(this, "Digite o login", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Usuario usuario = AppDatabase.getDatabase(this).usuarioDAO().buscarUsuarioPorLogin(login);
                runOnUiThread(() -> {
                    if (usuario != null) {
                        usuarioEncontrado = usuario;
                        Toast.makeText(this, "Usuário encontrado", Toast.LENGTH_SHORT).show();
                        habilitarBotoes(true);
                    } else {
                        Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                        habilitarBotoes(false);
                    }
                });
            }).start();
        });
    }

    private void alterarSenha() {
        binding.btnEntrar.setOnClickListener(v -> {
            String senha = binding.edtSenha.getText().toString().trim();
            String repetirSenha = binding.edtSenhaNovamente.getText().toString().trim();

            if (senha.isEmpty() || repetirSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(repetirSenha)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                String hashed = BCrypt.hashpw(senha, BCrypt.gensalt());
                usuarioEncontrado.setHashedPassword(hashed);
                AppDatabase.getDatabase(this).usuarioDAO().update(usuarioEncontrado);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Senha alterada com sucesso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
            }).start();
        });
    }

    private void cancelar() {
        binding.btnCancelar.setOnClickListener(v -> finish());
    }

    private void habilitarBotoes(boolean habilitar) {
        binding.edtSenha.setEnabled(habilitar);
        binding.edtSenhaNovamente.setEnabled(habilitar);
        binding.btnEntrar.setEnabled(habilitar);


        int corBotao = habilitar ? getResources().getColor(R.color.botao_confirmar, null)
                : getResources().getColor(R.color.cinza_desabilitado, null);
        binding.btnEntrar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(corBotao));

        int corCampo = habilitar ? getResources().getColor(android.R.color.white, null)
                : getResources().getColor(R.color.cinza_desabilitado, null);

        binding.edtSenha.setBackgroundColor(corCampo);
        binding.edtSenhaNovamente.setBackgroundColor(corCampo);
    }
}
