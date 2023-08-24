package com.example.beatfyhub;

import java.time.LocalDateTime;

public class ReproducaoMusica {

    private Musica musica;
    private LocalDateTime horario;

    public ReproducaoMusica(Musica m, LocalDateTime h){
        this.horario = h;
        this.musica = m;
    }

    public Musica getMusica() {
        return musica;
    }

    public LocalDateTime getHorario() {
        return horario;
    }
}
