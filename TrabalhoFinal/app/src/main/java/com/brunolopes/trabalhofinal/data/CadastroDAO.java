package com.brunolopes.trabalhofinal.data;

import com.google.firebase.auth.FirebaseAuth;

public class CadastroDAO {
    private static final CadastroDAO instanciaUnica = new CadastroDAO();

    FirebaseAuth fAuth;

    private CadastroDAO(){}

    public static CadastroDAO getInstance() {
        return instanciaUnica;
    }

    public void iniciarFirebaseAuth(){
        fAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth eventAuth(){
        return fAuth;
    }
}
