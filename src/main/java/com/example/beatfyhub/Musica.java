package com.example.beatfyhub;

import java.io.File;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

public class Musica {

    private String nome;
    private int duracao;
    private String artista;
    private String genero;
    private LocalDate anoLancamento;
    private Boolean favorita;
    private String album;
    private String path;
    private File mp3;

    public Musica(File mp) throws IOException, TikaException,  org.xml.sax.SAXException {
        this.nome = mp.getName();
        this.mp3 = mp;
        this.path = mp.getAbsolutePath();
        this.favorita = false;

        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(mp);
        ParseContext context = new ParseContext();

        parser.parse(inputstream, handler, metadata, context);
        System.out.println(handler.toString());

        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }
    }


    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "\nNome: " + nome + "\nArtista: "+ artista + "\nGenero: " + genero + "\nAlbum: " + album;
    }

    public void setFavorita(Boolean favorita) {
        this.favorita = favorita;
    }

    public File getMp3() {
        return mp3;
    }

    public int getDuracao() {
        return duracao;
    }

    public String getArtista() {
        return artista;
    }

    public String getGenero() {
        return genero;
    }

    public LocalDate getAnoLancamento() {
        return anoLancamento;
    }

    public Boolean getFavorita() {
        return favorita;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

//    public Button getPlay() {
//        return play;
//    }
//
//    public Button getOptions() {
//        return options;
//    }
}
