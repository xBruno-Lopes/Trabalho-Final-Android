package com.brunolopes.trabalhofinal.data;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsuariosDAO {
    private static final UsuariosDAO instanciaUnica = new UsuariosDAO();

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;

    private UsuariosDAO(){}

    public static UsuariosDAO getInstance(){
        return instanciaUnica;
    }

    public void iniciarFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public DocumentReference addUsuario(String userID){
        documentReference = firebaseFirestore.collection("Usuarios").document(userID);
        return  documentReference;
    }
}