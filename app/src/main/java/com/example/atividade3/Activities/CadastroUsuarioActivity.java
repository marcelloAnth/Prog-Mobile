package com.example.atividade3.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.databinding.ActivityCadastroUsuarioBinding;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private ActivityCadastroUsuarioBinding binding;
    private byte[] fotoBytes = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        binding.imageView.setImageBitmap(bitmap);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        fotoBytes = stream.toByteArray();
                    } catch (IOException e) {
                        Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        criarUsuario();
        cancelar();
        selecionarFoto();
    }

    private void criarUsuario() {
        binding.btnEntrar.setOnClickListener(v -> {
            String login = binding.edtLogin.getText().toString().trim();
            String senha = binding.edtSenha.getText().toString().trim();
            String repetirSenha = binding.edtSenhaNovamente.getText().toString().trim();

            if (login.isEmpty() || senha.isEmpty() || repetirSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(repetirSenha)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fotoBytes == null) {
                Toast.makeText(this, "Por favor, selecione uma foto", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                if (AppDatabase.getDatabase(this).usuarioDAO().buscarUsuarioPorLogin(login) != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Login já existe", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String hashedPassword = BCrypt.hashpw(senha, BCrypt.gensalt());
                Usuario novoUsuario = new Usuario(0, login, hashedPassword, fotoBytes);
                novoUsuario.setAdmin(false);

                AppDatabase.getDatabase(this).usuarioDAO().inserir(novoUsuario);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void cancelar() {
        binding.btnCancelar.setOnClickListener(v -> finish());
    }

    private void selecionarFoto() {
        binding.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }
}
