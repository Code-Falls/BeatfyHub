package com.example.beatfyhub;

import javafx.scene.control.Button;
import java.io.*;
import java.util.ArrayList;

public interface IRepositorioMusica extends Serializable {

    ArrayList<Musica> getMusicas();

    void criarMusica(File f);

    void adicionarPorDiretorio(File[] files);

    void destruirMusica(String nome);

    void listarMusicas();

    Musica procurarMusica(String nome);
    void favoritarMusica(File f);
    void salvarArquivo();
}
