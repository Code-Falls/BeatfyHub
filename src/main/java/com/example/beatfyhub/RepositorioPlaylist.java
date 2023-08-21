package com.example.beatfyhub;

import java.util.ArrayList;

public class RepositorioPlaylist implements IRepositorioPlaylist {

    private ArrayList<Playlist> playlists;

    public RepositorioPlaylist() {
        this.playlists = new ArrayList<>();
    }

    @Override
    public void criarPLaylist(String nome) {
        Playlist p = new Playlist(nome);
        this.playlists.add(p);
    }

    @Override
    public void listarPlaylists() {
        for (Playlist p : playlists) {
            System.out.println(p);
        }
    }

    @Override
    public void adicionarMusica(String nomePlaylist, Musica m) {
        for (Playlist p : playlists) {
            if (p.getNome().equals(nomePlaylist)) {
                p.adicionarMusica(m);
            }
        }
    }

    @Override
    public void removerMusica(String nomePlaylist, Musica m) {
        for (Playlist p : playlists) {
            if (p.getNome().equals(nomePlaylist)) {
                p.removerMusica(m);
            }
        }
    }

    @Override
    public void destruirPlaylist(String nome){
        for (Playlist p: playlists){
            if(p.getNome().equals(nome)){
                playlists.remove(p);
            }
        }
    }

}
