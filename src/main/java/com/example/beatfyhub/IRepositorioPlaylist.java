package com.example.beatfyhub;

public interface IRepositorioPlaylist {
    void criarPLaylist(String nome);

    void listarPlaylists();

    void adicionarMusica(String nomePlaylist, Musica m);

    void removerMusica(String nomePlaylist, Musica m);

    void destruirPlaylist(String nome);
}
