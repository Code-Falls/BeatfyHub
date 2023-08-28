package com.example.beatfyhub;

import java.io.*;
import java.util.ArrayList;

public class RepositorioPlaylist implements IRepositorioPlaylist {

    private ArrayList<Playlist> playlists;

    private RepositorioPlaylist() {
        this.playlists = new ArrayList<>();
    }

    private static RepositorioPlaylist instance;

    @Override
    public void criarPLaylist(String nome) {
        Playlist p = new Playlist(nome);
        this.playlists.add(p);
    }

    static RepositorioPlaylist getInstance() {
        if (RepositorioPlaylist.instance == null) {
            RepositorioPlaylist.instance = lerDoArquivo();
        }
        return RepositorioPlaylist.instance;
    }

    static RepositorioPlaylist lerDoArquivo() {
        RepositorioPlaylist instanciaLocal = null;

        File in = new File("playlists.dat");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(in);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            instanciaLocal = (RepositorioPlaylist) o;
        } catch (Exception e) {
            instanciaLocal = new RepositorioPlaylist();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {/* Silent exception */
                }
            }
        }
        return instanciaLocal;
    }

    @Override
    public void salvarArquivo() {
        if (instance == null) {
            return;
        }
        File out = new File("playlists.dat");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(out);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(instance);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    /* Silent */}
            }
        }
    }

    @Override
    public void listarPlaylists() {
        for (Playlist p : playlists) {
            System.out.println(p);
        }
    }

    @Override
    public void adicionarMusica(String nomePlaylist, Musica m) {
        for (Playlist p : playlists) {
            if (p.getNome().equals(nomePlaylist)) {
                p.adicionarMusica(m);
            }
        }
    }

    @Override
    public void removerMusica(String nomePlaylist, Musica m) {
        for (Playlist p : playlists) {
            if (p.getNome().equals(nomePlaylist)) {
                p.removerMusica(m);
            }
        }
    }

    @Override
    public void destruirPlaylist(String nome){
        for (Playlist p: playlists){
            if(p.getNome().equals(nome)){
                playlists.remove(p);
            }
        }
    }

    public Playlist procurarPlaylist(String nome){
        for(Playlist p: playlists){
            if(p.getNome().equals(nome)){
                return p;
            }
        }
        return null;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
}
