package com.brunolopes.trabalhofinal.data;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LivrosDAO {
    private static final LivrosDAO instanciaUnica = new LivrosDAO();

    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;

    private LivrosDAO(){}

    public static LivrosDAO getInstance(){
        return instanciaUnica;
    }

    public void iniciarFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public DocumentReference eventDatabase(String userID, String livroID){
        documentReference = firebaseFirestore.collection("Usuarios").document(userID).collection("Livros")
        .document(livroID);
        return documentReference;
    }

    public DocumentReference getAll(String userID){
        documentReference = firebaseFirestore.collection("Usuarios").document(userID);
        return documentReference;
    }
}
