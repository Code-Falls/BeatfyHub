package com.example.beatfyhub;

import java.io.File;

public interface IRepositorioMusica {
    void criarMusica(File f);

    void adicionarPorDiretorio(File[] files);

    void destruirMusica(String nome);

    void listarMusicas();

    Musica procurarMusica(String nome);
}