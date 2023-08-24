package com.example.beatfyhub;

import java.io.*;
import java.util.ArrayList;

public interface IRepositorioMusica {


    ArrayList<Musica> getMusicas();

    void criarMusica(File f);

    void adicionarPorDiretorio(File[] files);

    void destruirMusica(String nome);

    void listarMusicas();

    Musica procurarMusica(String nome);

    void salvarArquivo();
}
