package com.example.beatfyhub;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import javax.imageio.ImageIO;

public class Musica {

    private String nome;
    private String artista;
    private String genero;
    private String anoLancamento;
    private Boolean favorita;
    private String album;
    private String path;
    private Image image;
    private File mp3;

    public Musica(File mp) throws URISyntaxException {
        this.nome = mp.getName();
        this.mp3 = mp;
        this.path = mp.getAbsolutePath();
        this.favorita = false;
        metadataCatcher();
    }

    public void metadataCatcher(){
        File mp3File = this.mp3;

        try {
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();

            String artist = tag.getFirst(FieldKey.ARTIST);
            //String title = tag.getFirst(FieldKey.TITLE);
            String album = tag.getFirst(FieldKey.ALBUM);
            String genre = tag.getFirst(FieldKey.GENRE);
            String ano = tag.getFirst(FieldKey.YEAR);

            this.album = album;
            this.artista = artist;
            this.genero = genre;
            this.anoLancamento = ano;

            albumCoverCatcher();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void albumCoverCatcher() {
        File mp3file = this.mp3;

        try{
            AudioFile audioFile = AudioFileIO.read(mp3file);
            Tag tag = audioFile.getTag();

            for (Artwork artwork : tag.getArtworkList()) {
                byte[] imageData = artwork.getBinaryData();

                System.out.println("aqui5");
                saveImageToFile(imageData, "image.jpg");
                System.out.println("aqui");

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void saveImageToFile(byte[] imageData, String filePath) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(filePath)){
            outputStream.write(imageData);
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

    public String getArtista() {
        return artista;
    }

    public String getGenero() {
        return genero;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public Boolean getFavorita() {
        return favorita;
    }

    public Image getImage() {
        return image;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

}
