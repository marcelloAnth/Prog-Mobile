package com.example.atividade3.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.Security.SecurityUtils;
import com.example.atividade3.databinding.ActivityLoginBinding;
import com.example.atividade3.ui.UsuarioManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UsuarioManager usuarioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usuarioManager = new UsuarioManager(this);

        fazerLogin();
        esqueciSenha();
        criarUsuario();
    }

    private void fazerLogin() {
        binding.btnEntrar.setOnClickListener(v -> {
            String login = binding.edtLogin.getText().toString().trim();
            String senha = binding.edtSenha.getText().toString().trim();

            if (login.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Usuario usuario = usuarioManager.usuarioDAO.buscarUsuarioPorLogin(login);
                runOnUiThread(() -> {
                    if (usuario != null && SecurityUtils.verifyPassword(senha, usuario.getHashedPassword())) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                        Intent intent;
                        if (usuario.isAdmin()) {
                            intent = new Intent(this, EventosAdminActivity.class); // Tela Admin
                        } else {
                            intent = new Intent(this, EventosActivity.class); // Tela padrão
                        }

                        intent.putExtra("USER_ID", usuario.getIdUsuario());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Login ou senha inválidos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }


    private void criarUsuario() {
        binding.btnCriarCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
    }

    private void esqueciSenha() {
        binding.btnEsqueciSenha.setOnClickListener(v -> {
            Intent intent = new Intent(this, TrocaSenhaActivity.class);
            startActivity(intent);
        });
    }
}
