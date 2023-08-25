package com.example.beatfyhub;

import javafx.scene.control.Button;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;

public class RepositorioMusica implements IRepositorioMusica, Serializable{
    private ArrayList<Musica> musicas;

//singleton

    // instance

    public RepositorioMusica(){
        this.musicas = new ArrayList<>();
    }

    @Override
    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    @Override
    public void criarMusica(File f, Button btn) {
        try{
            Musica m = new Musica(f, btn);
            this.musicas.add(m);
        }
        catch(Exception e){

        }
    }

    @Override
    public void adicionarPorDiretorio(File[] files, Button btn){
        for (File f: files){
            try {
                Musica m = new Musica(f, btn);
                musicas.add(m);
            }
            catch(Exception e){

            }
        }
    }

    @Override
    public void destruirMusica(String nome){
        System.out.println(nome);
        for(Musica m: this.musicas){
            if(m.getNome().equals(nome)){
                System.out.println(m.getNome()+" aqui");
                this.musicas.remove(m);
            }
        }
    }

    @Override
    public void listarMusicas(){
        for(Musica m: this.musicas){
            System.out.println(m);
        }
    }

    @Override
    public Musica procurarMusica(String nome){
        for(Musica m: musicas){
            if(m.getNome().equals(nome)){
                return m;
            }
        }
        return null;
    }




}

