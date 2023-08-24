package com.example.beatfyhub;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepositorioReproducaoMusica implements IRepositorioReproducaoMusica {

    private ArrayList<ReproducaoMusica> musicasTocadas;

    private RepositorioReproducaoMusica(){
        this.musicasTocadas = new ArrayList<>();
    }

    private static RepositorioReproducaoMusica instance;

    static RepositorioReproducaoMusica getInstance() {
        if (RepositorioReproducaoMusica.instance == null) {
            RepositorioReproducaoMusica.instance = lerDoArquivo();
        }
        return RepositorioReproducaoMusica.instance;
    }

    static RepositorioReproducaoMusica lerDoArquivo() {
        RepositorioReproducaoMusica instanciaLocal = null;

        File in = new File("historico.dat");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(in);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            instanciaLocal = (RepositorioReproducaoMusica) o;
        } catch (Exception e) {
            instanciaLocal = new RepositorioReproducaoMusica();
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

    public void salvarArquivo() {
        if (instance == null) {
            return;
        }
        File out = new File("historico.dat");
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
    public void criarReproducao(Musica m, LocalDateTime t){
        ReproducaoMusica rm = new ReproducaoMusica(m, t);
        this.musicasTocadas.add(rm);
    }

    @Override
    public ArrayList<ReproducaoMusica> getMusicasTocadas() {
        return musicasTocadas;
    }

}
