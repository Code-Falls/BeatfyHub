package com.example.beatfyhub;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IRepositorioReproducaoMusica {
    void criarReproducao(Musica m, LocalDateTime t);

    ArrayList<ReproducaoMusica> getMusicasTocadas();
    void salvarArquivo();
}
