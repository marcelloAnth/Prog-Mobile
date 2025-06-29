package com.example.atividade3.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.R;
import com.example.atividade3.databinding.ActivityCadastroUsuarioBinding;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private ActivityCadastroUsuarioBinding binding;
    private byte[] fotoBytes = null;
    private Uri imageUri;
    private Bitmap fotoBitmap;

    private final ActivityResultLauncher<Intent> imageGalleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        binding.imageView.setImageBitmap(bitmap);
                        fotoBytes = bitmapToBytes(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    try {
                        binding.imageView.setImageURI(null); // força atualização
                        binding.imageView.setImageURI(imageUri);
                        fotoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        fotoBytes = bitmapToBytes(fotoBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
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
                Usuario novoUsuario = new Usuario(login, hashedPassword, fotoBytes);
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
            String[] opcoes = {"Galeria", "Câmera"};
            new AlertDialog.Builder(this)
                    .setTitle("Selecionar imagem")
                    .setItems(opcoes, (dialog, which) -> {
                        if (which == 0) abrirGaleria();
                        else capturarImagem();
                    })
                    .show();
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageGalleryLauncher.launch(intent);
    }

    public void capturarImagem() {
        checkCameraPermissionAndOpenCamera();
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            imageUri = createUri();
            takePictureLauncher.launch(imageUri);
        }
    }

    private Uri createUri() {
        File imageFile = new File(getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(this, "com.example.atividade3.fileprovider", imageFile);
    }

    private byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return stream.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageUri = createUri();
                takePictureLauncher.launch(imageUri);
            } else {
                Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
