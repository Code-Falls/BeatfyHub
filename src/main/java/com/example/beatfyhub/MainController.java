package com.example.beatfyhub;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private HBox homeContainer, exploreContainer, recentPlayedContainer;

    @FXML
    private Button moreButton, homeButton, exploreButton, recentButton, ftbButton, likedButton, artistsButton, albumsButton, loginButton;

    @FXML
    private ImageView playButton, previousButton, nextButton, loopButton, shuffleButton, favoriteButton;

    @FXML
    private void moreButtonClick(ActionEvent event) {
        System.out.println("botão more clicado");
    }

    @FXML
    private void homeButtonClick(ActionEvent event) {
        System.out.println("botão home clicado");
    }

    @FXML
    private void exploreButtonClick(ActionEvent event) {
        System.out.println("botão explore clicado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        selectedAudioFile = fileChooser.showOpenDialog(exploreButton.getScene().getWindow());

        String nome = sc.next();
        contPL.adicionarMusica(nome, selectedAudioFile);
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
    private void recentButtonClick(ActionEvent event) {
        System.out.println("botão recent played clicado");
        String nome = sc.nextLine();
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(stage);

        contPL.removerMusica(f);
    }

    @FXML
    private void ftbButtonClick(ActionEvent event) {
        System.out.println("botão feel the beat clicado");
    }

    @FXML
    private void likedButtonClick(ActionEvent event) {
        System.out.println("botão liked clicado");
    }

    @FXML
    private void artistsButtonClick(ActionEvent event) {

        System.out.println("botão artists clicado");
        contPL.listarMusicas();
    }

    @FXML
    private void albumsButtonClick(ActionEvent event) {
        System.out.println("botão albums clicado");
    }

    @FXML
    private void loginButtonClick(ActionEvent event) {
        System.out.println("botão login spotify clicado");
    }

    @FXML
    private void favoriteMedia() {
        System.out.println("media favoritada");
    }

    @FXML
    private void shuffleMedia() {
        System.out.println("modo aleatorio");
    }

    @FXML
    private void previousMedia() {
        System.out.println("media anterior");
    }

    @FXML
    private void playMedia() {
        System.out.println("media tocando");
        if (selectedAudioFile != null) {
            if (!isPlaying) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedAudioFile);
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


    private void applyColorAnimationDownBar(ImageView imageView) {
        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent1) {
                System.out.println("Entrou");
                imageView.setOpacity(0.5);
            }
        });

        imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Saiu");
                imageView.setOpacity(1.0);
            }
        });
    }

    @FXML
    private void toColorPlayButton() {
        applyColorAnimationDownBar(playButton);
    }

    @FXML
    private void toColorFavoriteButton() {
        applyColorAnimationDownBar(favoriteButton);
    }

    @FXML
    private void toColorPreviousButton() {
        applyColorAnimationDownBar(previousButton);
    }

    @FXML
    private void toColorNextButton() {
        applyColorAnimationDownBar(nextButton);
    }

    @FXML
    private void toColorShuffleButton() {
        applyColorAnimationDownBar(shuffleButton);
    }

    @FXML
    private void toColorLoopButton() {
        applyColorAnimationDownBar(loopButton);
    }

    private void applyColorAnimationSideBar(HBox hBox) {
        hBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent2) {
                System.out.println("Entrou");
                hBox.setStyle("-fx-background-color: #060606");
            }
        });

        hBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Saiu");
                hBox.setStyle("-fx-background-color: #121212");
            }
        });
    }

    @FXML
    private void toColorHomeButton() {
        applyColorAnimationSideBar(homeContainer);
    }

    @FXML
    private void toColorExploreButton() {
        applyColorAnimationSideBar(exploreContainer);
    }

    @FXML
    private void toColorRecentPlayedButton() {
        applyColorAnimationSideBar(recentPlayedContainer);
    }
}