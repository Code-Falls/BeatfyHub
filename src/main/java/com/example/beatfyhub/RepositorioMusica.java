package com.example.beatfyhub;

import javafx.scene.control.Button;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;

public class RepositorioMusica implements IRepositorioMusica, Serializable{
    private ArrayList<Musica> musicas;


    private static RepositorioMusica instance;

    private RepositorioMusica(){
        this.musicas = new ArrayList<>();
    }


    static RepositorioMusica getInstance() {
        if (RepositorioMusica.instance == null) {
            RepositorioMusica.instance = lerDoArquivo();
        }
        return RepositorioMusica.instance;
    }

    static RepositorioMusica lerDoArquivo() {
        RepositorioMusica instanciaLocal = null;

        File in = new File("musicas.dat");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(in);
            ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            instanciaLocal = (RepositorioMusica) o;
        } catch (Exception e) {
            instanciaLocal = new RepositorioMusica();
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
        File out = new File("musicas.dat");
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
    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    @Override
    public void criarMusica(File f) {
        try{
            Musica m = new Musica(f);
            this.musicas.add(m);
        }
        catch(Exception e){

        }
    }

    @Override
    public void adicionarPorDiretorio(File[] files){
        for (File f: files){
            try {
                Musica m = new Musica(f);
                musicas.add(m);
            }
            catch(Exception e){

            }
        }
    }

    @Override
    public void destruirMusica(String nome){
        System.out.println(nome);
        for(Musica m: this.musicas){
            if(m.getNome().equals(nome)){
                System.out.println(m.getNome()+" aqui");
                this.musicas.remove(m);
            }
        }
    }

    @Override
    public void listarMusicas(){
        for(Musica m: this.musicas){
            System.out.println(m);
        }
    }

    @Override
    public Musica procurarMusica(String nome){
        for(Musica m: musicas){
            if(m.getNome().equals(nome)){
                return m;
            }
        }
        return null;
    }

@Override
    public void favoritarMusica(File f){
        for (Musica m: musicas){
            if(m.getMp3().equals(f)){
                System.out.println("aqui");
                m.setFavorita(!m.getFavorita());
                System.out.println(m.getFavorita());
            }
        }
}


}

