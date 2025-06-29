package com.example.atividade3.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.databinding.ActivityCadastroEventoBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroEventoActivity extends AppCompatActivity {

    private ActivityCadastroEventoBinding binding;
    private byte[] imagemEventoBytes = null;
    private Uri imageUri;
    private Bitmap fotoBitmap;
    private static final int CAMERA_PERMISSION_CODE = 101;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registrarLaunchers();
        configurarBotoes();
    }

    private void configurarBotoes() {
        binding.btnSalvarEvento.setOnClickListener(v -> salvarEvento());
        binding.btnCancelarEvento.setOnClickListener(v -> finish());

        binding.imgEvento.setOnClickListener(v -> mostrarOpcoesImagem());
    }

    private void mostrarOpcoesImagem() {
        new AlertDialog.Builder(this)
                .setTitle("Selecionar imagem")
                .setItems(new CharSequence[]{"Galeria", "Câmera"}, (dialog, which) -> {
                    if (which == 0) {
                        selecionarImagemGaleria();
                    } else {
                        capturarImagem();
                    }
                }).show();
    }

    private void selecionarImagemGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void capturarImagem() {
        checkCameraPermissionAndOpenCamera();
    }

    private void salvarEvento() {
        String nome = binding.edtNomeEvento.getText().toString().trim();
        String local = binding.edtLocalEvento.getText().toString().trim();
        String descricao = binding.edtDescricaoEvento.getText().toString().trim();
        String dataStr = binding.edtDataEvento.getText().toString().trim();
        String numIngressosStr = binding.edtNumIngressosEvento.getText().toString().trim();

        if (nome.isEmpty() || local.isEmpty() || descricao.isEmpty() || dataStr.isEmpty() || numIngressosStr.isEmpty() || imagemEventoBytes == null) {
            Toast.makeText(this, "Preencha todos os campos e selecione uma imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        int numIngressos;
        try {
            numIngressos = Integer.parseInt(numIngressosStr);
            if (numIngressos <= 0) {
                Toast.makeText(this, "Número de ingressos deve ser maior que zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de ingressos inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dataStr);

            Evento evento = new Evento(nome, local, data, descricao, numIngressos, imagemEventoBytes);

            new Thread(() -> {
                AppDatabase.getDatabase(this).eventoDAO().inserir(evento);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Evento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } catch (Exception e) {
            Toast.makeText(this, "Data inválida (use dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarLaunchers() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            binding.imgEvento.setImageBitmap(bitmap);
                            imagemEventoBytes = converterBitmapParaBytes(bitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                (ActivityResultCallback<Boolean>) result -> {
                    if (result) {
                        try {
                            binding.imgEvento.setImageURI(imageUri);
                            fotoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imagemEventoBytes = converterBitmapParaBytes(fotoBitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Erro ao capturar imagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private byte[] converterBitmapParaBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        } else {
            imageUri = createUri();
            takePictureLauncher.launch(imageUri);
        }
    }

    private Uri createUri() {
        File imageFile = new File(getFilesDir(), "evento_foto.jpg");
        return FileProvider.getUriForFile(
                getApplicationContext(),
                "com.example.atividade3.fileprovider",
                imageFile
        );
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
