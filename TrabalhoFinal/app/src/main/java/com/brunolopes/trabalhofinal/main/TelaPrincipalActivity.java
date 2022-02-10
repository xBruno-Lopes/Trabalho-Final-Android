package com.brunolopes.trabalhofinal.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.brunolopes.trabalhofinal.Constants;
import com.brunolopes.trabalhofinal.R;
import com.brunolopes.trabalhofinal.data.LivrosDAO;
import com.brunolopes.trabalhofinal.model.LivrosAdapter;
import com.brunolopes.trabalhofinal.model.LivrosModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TelaPrincipalActivity extends AppCompatActivity {

    private Button addItem;
    private Button deletarITem;
    private Button editarItem;
    private Button procurarItem;
    private int identificador = -1;
    String userID;

    //Utilizando random para gerar um ID "único" para cada livro, imagine isso como um fake ISBN
    private Random rand = new Random();

    //Sigleton do DAO
    LivrosDAO livrosData = LivrosDAO.getInstance();

    ArrayList<LivrosModel> listaLivros;
    ListView viewLivros;
    LivrosAdapter adapter;

    //Aqui é onde inicia-se o laucher para comunicação da tela principal e da tela
    // adicionar novo item(MainActivivy2) ou editar item(MainActivity3) e onde recemos os dados delas,
    //de acordo com o code recebido. Poderia estar no onCreate também,
    ActivityResultLauncher<Intent> gerenciadorLivros = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.w("TAG", "Volta para tela principal");

                if (result.getResultCode() == Constants.RESULT_ADD) {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        String titulo = (String) data.getExtras().get("nome");
                        String autor = (String) data.getExtras().get("autor");
                        String ano = (String) data.getExtras().get("ano");
                        String livroId = (String) data.getExtras().get("id");

                        DocumentReference user = livrosData.eventDatabase(userID, livroId);
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("Titulo", titulo);
                        userData.put("Autor", autor);
                        userData.put("Ano", ano);
                        userData.put("id", livroId);

                        user.set(userData).addOnSuccessListener(unused -> {
                            Log.d("LivroAdd", "Livro adicionado com sucesso para usuario: " + userID);
                        }).addOnFailureListener(e -> {
                            Log.w("AddErro", "Erro ao adicionar livro", e);
                        });
                        Log.w("TAG", "alem do set no firestore");

                        Toast.makeText(TelaPrincipalActivity.this, "Livro Adicionado", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        identificador = -1;
                    } else {
                        Toast.makeText(TelaPrincipalActivity.this, "Erro ao adicionar livro.", Toast.LENGTH_SHORT).show();
                    }
                } else if (result.getResultCode() == Constants.RESULT_EDIT) {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        String titulo = (String) data.getExtras().get("nome");
                        String autor = (String) data.getExtras().get("autor");
                        String ano = (String) data.getExtras().get("ano");
                        String livroId = (String) data.getExtras().get("id");

                        DocumentReference user = livrosData.eventDatabase(userID, livroId);
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("Titulo", titulo);
                        userData.put("Autor", autor);
                        userData.put("Ano", ano);
                        userData.put("id", livroId);

                        user.set(userData).addOnSuccessListener(unused -> {
                            Log.d("EditOK", "Livro editado com sucesso para usuario: " + userID);
                        }).addOnFailureListener(e -> {
                            Log.w("EditErro", "Erro ao editar livro", e);
                        });
                        Toast.makeText(TelaPrincipalActivity.this, "Livro Editado", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        identificador = -1;
                    }
                } else if (result.getResultCode() == Constants.RESULT_CANCEL) {
                    Toast.makeText(TelaPrincipalActivity.this, "Operação cancelada", Toast.LENGTH_SHORT).show();
                    identificador = -1;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        listaLivros = new ArrayList<>();
        viewLivros = findViewById(R.id.listLivros);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getString("userID");
        }

        adapter = new LivrosAdapter(TelaPrincipalActivity.this, listaLivros);
        viewLivros.setAdapter(adapter);

        //método para escutar um click em um item da lista
        viewLivros.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(TelaPrincipalActivity.this, "" + listaLivros.get(position)
                    .toString(), Toast.LENGTH_SHORT).show();
            identificador = position;
        });

        //métodos para funcionamento do app sendo chamados no onCreate
        addNovoLivro();
        editarLivro();
        deletarLivro();
        procurarLivroMap();

        //método para iniciar o firebase
        iniciarFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.configuracao:
                startActivity(new Intent(getApplicationContext(), TelaConfigActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregarLivrosDatabase() {
        listaLivros.clear();
        DocumentReference db = livrosData.getAll(userID);
        db.collection("Livros").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listaLivros.add(new LivrosModel(document.getString("Titulo"), document.getString("Autor"),
                            document.getString("Ano"), document.getString("id")));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void iniciarFirebase() {
        livrosData.iniciarFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLivrosDatabase();
    }

    //Método para adicionar um novo livro a partir do botão "adcionar" da tela principal
    public void addNovoLivro() {
        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(v -> {
            int id = rand.nextInt(100000);
            Intent intent = new Intent(this, TelaAddActivity.class);
            intent.putExtra("id", "" + id);
            gerenciadorLivros.launch(intent);
        });
    }

    //Método para editar um item da lista a partir do botão "editar" da tela principal
    public void editarLivro() {
        editarItem = findViewById(R.id.editarItem);
        editarItem.setOnClickListener(v -> {
            if (identificador >= 0) {
                Intent intent = new Intent(this, TelaEditActivity.class);
                LivrosModel livro = listaLivros.get(identificador);
                intent.putExtra("titulo", livro.getTitulo());
                intent.putExtra("autor", livro.getAutor());
                intent.putExtra("ano", livro.getAno());
                intent.putExtra("id", livro.getId());

                gerenciadorLivros.launch(intent);

            } else {
                Toast.makeText(TelaPrincipalActivity.this, "Clique em um livro para selecionar primeiro", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Método para remover um item da lista a partir do botão "remover" da tela principal,
    // precisa primeiro clicar em um item para remove-lo.
    public void deletarLivro() {
        deletarITem = findViewById(R.id.deletarItem);
        deletarITem.setOnClickListener(v -> {
            if (identificador >= 0) {
                //método antigo
                //listaLivros.remove(identificador);

                //método usando firebase
                //livrosData.eventDatabase().child("Livros").child(""+listaLivros.get(identificador).getId()).removeValue();
                DocumentReference db = livrosData.getAll(userID);
                db.collection("Livros").document(listaLivros.get(identificador).getId()).delete().
                        addOnSuccessListener(unused -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(TelaPrincipalActivity.this, "Livro deletado", Toast.LENGTH_SHORT).show();
                            identificador = -1;
                            carregarLivrosDatabase();
                        });
            } else {
                Toast.makeText(TelaPrincipalActivity.this, "Clique em um livro para selecionar primeiro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void procurarLivroMap() {
        procurarItem = findViewById(R.id.procurarItem);
        procurarItem.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        });
    }

}