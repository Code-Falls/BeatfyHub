package com.example.beatfyhub;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepositorioReproducaoMusica implements IRepositorioReproducaoMusica {

    private ArrayList<ReproducaoMusica> musicasTocadas;

    public RepositorioReproducaoMusica(){
        this.musicasTocadas = new ArrayList<>();
    }

    @Override
    public void criarReproducao(Musica m, LocalDateTime t){
        ReproducaoMusica rm = new ReproducaoMusica(m, t);
        this.musicasTocadas.add(rm);
    }

    @Override
    public ArrayList<ReproducaoMusica> getMusicasTocadas() {
        return musicasTocadas;
    }

}
