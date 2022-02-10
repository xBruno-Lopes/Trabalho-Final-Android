package com.brunolopes.trabalhofinal.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brunolopes.trabalhofinal.R;
import com.brunolopes.trabalhofinal.data.LoginDAO;

public class LoginActivity extends AppCompatActivity {
    ImageView profileImg;
    EditText email;
    EditText senha;
    Button fazerLogin;
    TextView novoUsuario;
    ProgressBar progressBar;
    LoginDAO loginAuth = LoginDAO.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        profileImg = findViewById(R.id.imageViewUSer);
        email = findViewById(R.id.loginEmailAddress);
        senha = findViewById(R.id.loginPassword);
        fazerLogin = findViewById(R.id.buttonLogin);
        novoUsuario = findViewById(R.id.cadastrarUsertextView);
        progressBar = findViewById(R.id.progressBar2);

        loginAuth.iniciarFirebaseAuth();
        loginUsuario();
        criarUsuario();
    }

    public void loginUsuario(){
        fazerLogin.setOnClickListener(view -> {
            String auxEmail = email.getText().toString().trim();
            String password = senha.getText().toString().trim();

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

            //Autenticar usuário no banco
            loginAuth.eventAuth().signInWithEmailAndPassword(auxEmail, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logado com Sucesso!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(this, TelaPrincipalActivity.class);
                    intent.putExtra("userID", loginAuth.eventAuth().getCurrentUser().getUid());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        });
    }

    public void criarUsuario(){
        novoUsuario.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), CadastrarUsuarioActivity.class));
            finish();
        });
    }
}
