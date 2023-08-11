package com.example.beatfyhub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.util.Scanner;

public class MainController {

    private Player player;
    private File selectedAudioFile;
    private int numMusicLabels = 0;
    private boolean isPlaying = false;

    private ControladorPlayerLocal contPL = new ControladorPlayerLocal();

    Scanner sc = new Scanner(System.in);
    @FXML
    private GridPane musicGridPane;

    @FXML
    private Button moreButton, homeButton, newPlaylistButton, exploreButton, recentButton, ftbButton, likedButton, artistsButton, albumsButton, loginButton;
    @FXML
    private void moreButtonClick(ActionEvent event){
        System.out.println("botão more clicado");
    }

    @FXML
    private void homeButtonClick(ActionEvent event){
        System.out.println("botão home clicado");
        contPL.listarPlaylists();
    }

    public void newPlaylistButtonClick(){
        contPL.criarPLaylist(sc.nextLine());
    }

    @FXML
    private void exploreButtonClick(ActionEvent event){
        System.out.println("botão explore clicado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        selectedAudioFile = fileChooser.showOpenDialog(exploreButton.getScene().getWindow());
        contPL.adicionarMusica(selectedAudioFile);
        if (selectedAudioFile != null) {
            System.out.println("Arquivo MP3 selecionado: " + selectedAudioFile.getName());
            if (player != null) {
                player.close();
            }
            isPlaying = false;

            // Adicionar label da música à GridPane
            Label musicLabel = new Label(selectedAudioFile.getName()); // Nome do arquivo como nome da música (ajuste conforme necessário)

            // Definir posição na GridPane
            int colIndex = numMusicLabels % 3; // Alternar entre coluna 0 e 1
            int rowIndex = numMusicLabels / 3; // Aumentar a linha a cada 2 elementos

            musicGridPane.add(musicLabel, colIndex, rowIndex);

            numMusicLabels++;
        }
    }

    @FXML
    private void recentButtonClick(ActionEvent event){
        System.out.println("botão recent played clicado");
        contPL.removerMusica(sc.nextLine());
    }

    @FXML
    private void ftbButtonClick(ActionEvent event){
        System.out.println("botão feel the beat clicado");
    }

    @FXML
    private void likedButtonClick(ActionEvent event){
        System.out.println("botão liked clicado");
        contPL.removerMusicaDaPlaylist(sc.nextLine(), sc.nextLine());
    }

    @FXML
    private void artistsButtonClick(ActionEvent event){
        System.out.println("botão artists clicado");
        contPL.listarMusicas();
    }

    @FXML
    private void albumsButtonClick(ActionEvent event){
        System.out.println("botão albums clicado");
        contPL.adicionarMusicaNaPlaylist(sc.nextLine(), sc.nextLine());
    }

    @FXML
    private void loginButtonClick(ActionEvent event){
        System.out.println("botão login spotify clicado");
        DirectoryChooser ch = new DirectoryChooser();
        contPL.adicionarMusicasDaPasta(ch.showDialog(exploreButton.getScene().getWindow()).getAbsolutePath());
    }

    @FXML
    private void favoriteMedia(){
        System.out.println("media favoritada");
        contPL.destruirPlaylist(sc.nextLine());
    }

    @FXML
    private void shuffleMedia(){
        System.out.println("modo aleatorio");
    }

    @FXML
    private void previousMedia(){
        System.out.println("media anterior");
    }

    @FXML
    private void playMedia(){
        System.out.println("media tocando");

        File musica = contPL.procurarMusica(sc.nextLine()).getMp3();

        if (musica != null) {
            if (!isPlaying) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(musica);
                    player = new Player(fileInputStream);

                    new Thread(() -> {
                        try {
                            player.play();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                    isPlaying = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                player.close();
                isPlaying = false;
            }
        } else {
            System.out.println("Nenhum arquivo MP3 selecionado.");
        }
    }

    @FXML
    private void nextMedia(){
        System.out.println("proxima media");
    }

    @FXML
    private void loopMedia(){
        System.out.println("modo loop");
    }
}