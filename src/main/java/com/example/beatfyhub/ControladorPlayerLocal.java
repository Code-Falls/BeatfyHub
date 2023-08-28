package com.example.beatfyhub;

import javafx.scene.control.Button;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.*;

public class ControladorPlayerLocal {

    private IRepositorioMusica mySongs;
    private IRepositorioPlaylist myPlaylists;
    private IRepositorioReproducaoMusica myHistory;


    public ControladorPlayerLocal() {
        this.myPlaylists = RepositorioPlaylist.getInstance();
        this.mySongs = RepositorioMusica.getInstance();
        this.myHistory = RepositorioReproducaoMusica.getInstance();
    }

    public void adicionarMusica(File f) {
        if (f != null) {
            mySongs.criarMusica(f);
        }
    }

    public ArrayList<Playlist> getMyPlaylists() {
        return myPlaylists.getPlaylists();
    }

    public void salvarReproducao(File f, LocalDateTime t){
        boolean achou = false;
        for(Musica m: mySongs.getMusicas()){
            if(m.getMp3().equals(f) && !achou){
                myHistory.criarReproducao(m, t);
                achou = true;
            }
        }
    }

    public void removerMusica(String nome) {
        if (nome != null) {
            mySongs.destruirMusica(nome);
        }
    }

    public Playlist procurarPlaylist(String nome){
        if(!nome.isBlank() && !nome.isEmpty()) {
            return myPlaylists.procurarPlaylist(nome);
        }
        else{
            return null;
        }
    }

    public void favoritar(File f){
        if (f!=null){
            mySongs.favoritarMusica(f);
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

    public void adicionarMusicasDaPasta(String caminho, Button b){
        File diretorio = new File(caminho);
        File[] files = diretorio.listFiles();
        if (files!=null && files.length>0) {
            mySongs.adicionarPorDiretorio(files);
        }
    }

    public void salvar(){
        mySongs.salvarArquivo();
        myPlaylists.salvarArquivo();
        myHistory.salvarArquivo();
    }

    public ArrayList<ReproducaoMusica> getMyHistory() {
        return myHistory.getMusicasTocadas();
    }

    public ArrayList<Musica> getMySongs() {
        return mySongs.getMusicas();
    }

    public Musica procurarMusica(String nome) {
        return mySongs.procurarMusica(nome);
    }
}
