package com.brunolopes.trabalhofinal.data;

import com.google.firebase.auth.FirebaseAuth;

public class LoginDAO {
    private static final LoginDAO instanciaUnica = new LoginDAO();

    FirebaseAuth fAuth;

    private LoginDAO(){}

    public static LoginDAO getInstance() {
        return instanciaUnica;
    }

    public void iniciarFirebaseAuth(){
        fAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth eventAuth(){
        return fAuth;
    }
}
