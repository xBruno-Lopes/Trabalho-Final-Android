package com.brunolopes.trabalhofinal.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brunolopes.trabalhofinal.R;
import com.brunolopes.trabalhofinal.data.CadastroDAO;
import com.brunolopes.trabalhofinal.data.UsuariosDAO;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    EditText nomeCompleto;
    EditText email;
    EditText senha;
    EditText telefone;
    Button cadastrar;
    TextView fazerLogin;
    ProgressBar progressBar;
    String userID;

    CadastroDAO cadastroAuth = CadastroDAO.getInstance();
    UsuariosDAO usuarios = UsuariosDAO.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        nomeCompleto = findViewById(R.id.cadastrarFullName);
        email = findViewById(R.id.cadastrarEmail);
        senha = findViewById(R.id.cadastrarPassword);
        telefone = findViewById(R.id.cadastrarPhone);
        cadastrar = findViewById(R.id.cadastrarButton);
        fazerLogin = findViewById(R.id.cadastrarLoginText);
        progressBar = findViewById(R.id.progressBar);

        cadastroAuth.iniciarFirebaseAuth();
        usuarios.iniciarFirebase();

        cadastrarUsuario();
        logarUsuario();

    }

    public void cadastrarUsuario(){
        cadastrar.setOnClickListener(view -> {
            String auxEmail = email.getText().toString().trim();
            String password = senha.getText().toString().trim();
            String nome = nomeCompleto.getText().toString();
            String auxTelefone = telefone.getText().toString();

            if(TextUtils.isEmpty(auxEmail)){
                email.setError("Campo Email não pode ser vazio");
                return;
            }
            if(TextUtils.isEmpty(password)){
                senha.setError("Campo senha não pode ser vazio");
                return;
            }
            if(password.length() <= 6){
                senha.setError("Senha deve ter mais que 6 caracteres");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //Criar usuários no banco
            cadastroAuth.eventAuth().createUserWithEmailAndPassword(auxEmail, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(CadastrarUsuarioActivity.this, "Usuário Cadastrado!", Toast.LENGTH_SHORT).show();
                    userID = cadastroAuth.eventAuth().getCurrentUser().getUid();
                    DocumentReference user = usuarios.addUsuario(userID);
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("Nome", nome);
                    userData.put("Telefone", auxTelefone);
                    userData.put("Email", auxEmail);
                    user.set(userData).addOnSuccessListener(unused -> {
                        Log.d("TAG", "onSuccess: usuario criado para id" + userID);
                    });
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else {
                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        });
    }

    public void logarUsuario(){
        fazerLogin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}
