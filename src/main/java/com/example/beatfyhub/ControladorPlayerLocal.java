package com.example.beatfyhub;

import java.io.File;

public class ControladorPlayerLocal {

    private IRepositorioMusica mySongs;
    private IRepositorioPlaylist myPlaylists;

    public ControladorPlayerLocal() {
        this.myPlaylists = new RepositorioPlaylist();
        this.mySongs = new RepositorioMusica();
    }

    public void adicionarMusica(File f) {
        if (f != null) {
            mySongs.criarMusica(f);
        }
    }

    public void removerMusica(String nome) {
        if (nome != null) {
            mySongs.destruirMusica(nome);
        }
    }

    public void listarMusicas() {
        mySongs.listarMusicas();
    }

    public void criarPLaylist(String nome) {
        if (nome != null) {
            myPlaylists.criarPLaylist(nome);
        }
    }

    public void listarPlaylists() {
        myPlaylists.listarPlaylists();
    }

    public void adicionarMusicaNaPlaylist(String nomePlaylist, String nomeMusica){
        myPlaylists.adicionarMusica(nomePlaylist, mySongs.procurarMusica(nomeMusica));
    }

    public void destruirPlaylist(String nome){
        if(!nome.isEmpty() && !nome.isBlank()){
            myPlaylists.destruirPlaylist(nome);
        }
    }

    public void removerMusicaDaPlaylist(String nomePlaylist, String nomeMusica){
        myPlaylists.removerMusica(nomePlaylist, mySongs.procurarMusica(nomeMusica));
    }

    public void adicionarMusicasDaPasta(String caminho){
        File diretorio = new File(caminho);
        File[] files = diretorio.listFiles();
        if (files!=null && files.length>0) {
            mySongs.adicionarPorDiretorio(files);
        }
    }

    public ArrayList<Musica> getMySongs() {
        return mySongs.getMusicas();
    }

    public Musica procurarMusica(String nome) {
        return mySongs.procurarMusica(nome);
    }
}
