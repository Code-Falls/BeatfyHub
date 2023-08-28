package com.example.beatfyhub;

import java.time.LocalDateTime;

public class ReproducaoMusica {

    private Musica musica;
    private LocalDateTime horario;
    private String nome;
    private String artista;
    private String album;

    public ReproducaoMusica(Musica m, LocalDateTime h){
        this.horario = h;
        this.musica = m;
        this.nome = musica.getNome();
        this.album = musica.getAlbum();
        this.artista = musica.getArtista();
    }

    public Musica getMusica() {
        return musica;
    }

    public String getNome() {
        return nome;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

    public LocalDateTime getHorario() {
        return horario;
    }
}
