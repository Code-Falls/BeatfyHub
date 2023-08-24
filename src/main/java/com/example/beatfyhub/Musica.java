package com.example.beatfyhub;

import java.io.File;
import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

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

    public Musica(File mp) throws IOException, TikaException,  org.xml.sax.SAXException{
        this.nome = mp.getName();
        this.mp3 = mp;
        this.path = mp.getAbsolutePath();

        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(mp);
        ParseContext context = new ParseContext();

        parser.parse(inputstream, handler, metadata, context);
        System.out.println(handler.toString());

        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
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

    public File getMp3() {
        return mp3;
    }
}
