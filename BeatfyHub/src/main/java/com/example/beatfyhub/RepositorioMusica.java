package com.example.beatfyhub;

import java.io.File;
import java.util.ArrayList;

public class RepositorioMusica implements IRepositorioMusica {
    private ArrayList<Musica> musicas;

    public RepositorioMusica(){
        this.musicas = new ArrayList<>();
    }

    @Override
    public void criarMusica(File f){
        Musica m = new Musica(f);
        this.musicas.add(m);
    }

    @Override
    public void adicionarPorDiretorio(File[] files){
        for (File f: files){
            Musica m = new Musica(f);
            musicas.add(m);
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

//    public void destruirMusica(File f){
//        for(Musica m: this.musicas){
//            if(m.getMp3().equals(f)){
//                System.out.println(m.getNome()+" aqui");
//                this.musicas.remove(m);
//            }
//        }
//    }

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
