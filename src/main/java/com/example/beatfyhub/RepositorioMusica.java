package com.example.beatfyhub;

import java.io.File;
import java.util.ArrayList;

public class RepositorioMusica {
    private ArrayList<Musica> musicas;

    public RepositorioMusica(){
        this.musicas = new ArrayList<>();
    }

    public void criarMusica(Musica m){
        this.musicas.add(m);
        System.out.println(m.getNome());
    }

    public void destruirMusica(String nome){
        System.out.println(nome);
        for(Musica m: this.musicas){
            if(m.getNome().equals(nome)){
                System.out.println(m.getNome()+" aqui");
                this.musicas.remove(m);
            }
        }
    }

    public void destruirMusica(File f){
        for(Musica m: this.musicas){
            if(m.getMp3().equals(f)){
                System.out.println(m.getNome()+" aqui");
                this.musicas.remove(m);
            }
        }
    }

    public void listarMusicas(){
        for(Musica m: this.musicas){
            System.out.println(m);
        }
    }
}
