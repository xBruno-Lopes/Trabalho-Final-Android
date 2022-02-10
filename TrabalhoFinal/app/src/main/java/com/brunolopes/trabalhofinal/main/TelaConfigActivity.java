package com.brunolopes.trabalhofinal.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.brunolopes.trabalhofinal.R;
import com.brunolopes.trabalhofinal.Constants;

public class TelaConfigActivity extends AppCompatActivity {

    TextView nomeEntrada;
    TextView emailEntrada;
    TextView telefoneEntrada;
    Button cameraButton;
    Button galeriaButton;
    ImageView userImg;

    ActivityResultLauncher<Intent> iniciarActivityCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        userImg.setImageBitmap(image);
                    }
                }
            });

    ActivityResultLauncher<Intent> iniciarActivityGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        userImg.setImageURI(data.getData());
                    }
                }
            }
    );



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        nomeEntrada = findViewById(R.id.nomeConfigEntrada);
        emailEntrada = findViewById(R.id.emailConfigEntrada);
        telefoneEntrada = findViewById(R.id.telefoneConfigEntrada);
        cameraButton = findViewById(R.id.buttonCamera);
        galeriaButton = findViewById(R.id.buttonGaleria);
        userImg = findViewById(R.id.userImg);

        cameraButtonClick();
        galeriaButtonClick();
    }

    public void cameraButtonClick(){
        cameraButton.setOnClickListener(v -> {
            cameraPermission();
        });
    }
    public void galeriaButtonClick(){
        galeriaButton.setOnClickListener(v -> {
            galeriaPermission();
        });
    }

    private void cameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, Constants.CAMERA_CODE_PERMISSION);
        }else {
            abrirCamera();
        }

    }

    private void galeriaPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, Constants.GALERIA_COD_PERMISSION);
            }else {
                abrirGaleria();
            }
        }else {
            abrirGaleria();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CAMERA_CODE_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirCamera();
            }else {
                Toast.makeText(this, "Para usar a camera é necessario garantir permissão", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == Constants.GALERIA_COD_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria();
            }else {
                Toast.makeText(this, "Para usar a galeria é necessario garantir permissão", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void abrirCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        iniciarActivityCamera.launch(camera);
    }
    private void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        iniciarActivityGaleria.launch(intent);
    }

}
