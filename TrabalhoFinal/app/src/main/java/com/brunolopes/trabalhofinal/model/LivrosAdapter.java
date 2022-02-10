package com.brunolopes.trabalhofinal.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brunolopes.trabalhofinal.R;

import java.util.ArrayList;

//Adapter Customizado
public class LivrosAdapter extends ArrayAdapter<LivrosModel> {
    public LivrosAdapter(@NonNull Context context, ArrayList<LivrosModel> livros) {
        super(context, 0, livros);
    }


    //Método para converter o Adapter Customizado para o meu Layout costomizado (livros_layout)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LivrosModel livro = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.livros_layout, parent, false);
        }
        TextView livroNome = convertView.findViewById(R.id.nomeLivro);
        TextView livroAutor = convertView.findViewById(R.id.nomeAutor);
        TextView livroAno = convertView.findViewById(R.id.anoLivro);
        TextView livroID = convertView.findViewById(R.id.idLivro);

        livroNome.setText(livro.getTitulo());
        livroAutor.setText(livro.getAutor());
        livroAno.setText(livro.getAno());
        livroID.setText(livro.getId());

        return convertView;
    }
}
