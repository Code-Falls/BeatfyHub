package com.example.beatfyhub;

import java.io.File;
import java.time.LocalDate;

public class Musica {

    private String nome;
    private int duracao;
    private String artista;
    private String genero;
    private LocalDate anoLancamento;
    private Boolean favorita;
    private String album;
    private String path;
    private File mp3;

    public Musica(String nome, File mp3){
        this.nome = nome;
        this.mp3 = mp3;
        this.path = mp3.getAbsolutePath();
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "\nNome: " + nome + "\nArtista: "+ artista + "\nGenero: " + genero + "\nAlbum: " + album;
    }

    public File getMp3() {
        return mp3;
    }
}
