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
}
