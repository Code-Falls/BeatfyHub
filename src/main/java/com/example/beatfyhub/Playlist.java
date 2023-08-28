package com.example.beatfyhub;

import java.util.ArrayList;

public class Playlist {
    private ArrayList<Musica> playlist;
    private ArrayList<Playlist> subplaylists;
    private String nome;

    public Playlist(String nome){
        this.playlist = new ArrayList<>();
        this.subplaylists = new ArrayList<>();
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Nome da Playlist: " + nome + "\nMúsicas da Playlist=" + playlist;
    }

    public void adicionarMusica(Musica m){
        this.playlist.add(m);
    }

    public void removerMusica(Musica m){
        this.playlist.remove(m);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Musica> getPlaylist() {
        return playlist;
    }
}
