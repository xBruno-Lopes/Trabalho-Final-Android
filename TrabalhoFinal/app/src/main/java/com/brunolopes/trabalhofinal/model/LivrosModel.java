package com.brunolopes.trabalhofinal.model;



//Classe modelo para os livros
public class LivrosModel {
    private String titulo;
    private String autor;
    private String ano;
    private String id;

    //Esse construtor existe, pois ao adicionar um novo livro apenar o ID
    //Será passado para o usuário, os demais campos serão preenchido pelo o usuário
    public LivrosModel(){
    }

    //Construtor padrão
    public LivrosModel(String titulo, String autor, String ano, String id) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.id = id;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item Selecionado:\n"+"Titulo: " + titulo +"\n"+
                "Autor: " + autor +"\n"+
                "Ano: " + ano;
    }
}

