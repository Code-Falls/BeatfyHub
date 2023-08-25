package com.example.beatfyhub;

import javafx.scene.control.Button;
import java.io.*;
import java.util.ArrayList;

public interface IRepositorioMusica extends Serializable {

    ArrayList<Musica> getMusicas();

    void criarMusica(File f, Button btn);

    void adicionarPorDiretorio(File[] files, Button btn);

    void destruirMusica(String nome);

    void listarMusicas();

    Musica procurarMusica(String nome);
}
