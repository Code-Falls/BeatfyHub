package com.example.beatfyhub;

import java.util.ArrayList;

public interface IRepositorioPlaylist {
    void criarPLaylist(String nome);

    void listarPlaylists();

    void adicionarMusica(String nomePlaylist, Musica m);

    void removerMusica(String nomePlaylist, Musica m);

    void destruirPlaylist(String nome);
    void salvarArquivo();
    Playlist procurarPlaylist(String nome);
    ArrayList<Playlist> getPlaylists();
}
