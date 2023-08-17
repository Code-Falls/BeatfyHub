package com.example.beatfyhub;

import java.io.File;

public class ControladorPlayerLocal {

        private RepositorioMusica mySongs;
        private RepositorioPlaylist myPlaylists;

        public ControladorPlayerLocal(){
            this.myPlaylists = new RepositorioPlaylist();
            this.mySongs = new RepositorioMusica();
        }

        public void adicionarMusica(String nome, File f){
            if (f!=null && !nome.isBlank() && !nome.isEmpty()){
                Musica m = new Musica(nome, f);
                mySongs.criarMusica(m);
            }
        }

        public void removerMusica(String nome){
            if(nome!=null){
                mySongs.destruirMusica(nome);
            }
        }

    public void removerMusica(File f){
        if(f!=null){
            mySongs.destruirMusica(f);
        }
    }

        public void listarMusicas(){
            mySongs.listarMusicas();
        }
}
