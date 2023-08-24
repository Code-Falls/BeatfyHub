package com.example.beatfyhub;

import com.example.beatfyhub.ControladorPlayerLocal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.io.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.net.URL;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    private MediaPlayer player;
    private File selectedAudioFile;
    private int numMusicLabels = 0;
    private boolean isPlaying = false;
    private ControladorPlayerLocal contPL = new ControladorPlayerLocal();


    @FXML
    private Button moreButton, homeButton, newFolderButton, newSongButton, recentButton, ftbButton, likedButton, artistsButton, albumsButton, loginButton, newPlaylistButton, createPlaylistButton, cancelPlaylistButton;
    @FXML
    private ImageView playButton, previousButton, nextButton, loopButton, shuffleButton, favoriteButton;
    @FXML
    private HBox homeContainer, exploreContainer, recentPlayedContainer, newPlaylistContainer, newSongContainer, newFolderContainer;
    @FXML
    private TextField playlistNameTextField, searchTextField;
    @FXML
    private GridPane musicGridPane;
    @FXML
    private ScrollPane musicScrollPane;
    @FXML
    private ProgressBar musicProgressBar;
    @FXML
    private Pane musicPane;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask task;

//    public void initialize (URL url, ResourceBundle resourceBundle) {
//        media = new Media(contPL.getMySongs().get(songNumber).getMp3().toURI().toString());
//        player = new MediaPlayer(media);
//    }

    public void beginTimer () {
        timer = new Timer();

        task = new TimerTask() {
            public void run() {

                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end); //only needed to see the progress value in the console
                musicProgressBar.setProgress(current/end);

                if (current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void cancelTimer(){
        isPlaying = false;
        timer.cancel();
    }


    @FXML
    private void homeButtonClick(ActionEvent event) {
        System.out.println("botão home clicado");
    }

    @FXML
    private void newSongButtonClick(ActionEvent event) {
        System.out.println("botão explore clicado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        selectedAudioFile = fileChooser.showOpenDialog(newSongButton.getScene().getWindow());
        contPL.adicionarMusica(selectedAudioFile);
        if (selectedAudioFile != null) {
            System.out.println("Arquivo MP3 selecionado: " + selectedAudioFile.getName());
            if (player != null) {
                player.pause();
            }
            isPlaying = false;
        }
    }


    @FXML
    private void newFolderButtonClick(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(newFolderButton.getScene().getWindow());

        if (selectedFolder != null) {
            File[] musicFiles = selectedFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (musicFiles != null) {
                for (File musicFile : musicFiles) {
                    contPL.adicionarMusica(musicFile);

                    Label musicLabel = new Label(musicFile.getName());
                    int column = numMusicLabels % 3;
                    int row = numMusicLabels / 3;

                    if (row > musicGridPane.getRowCount()) {
                        musicGridPane.addColumn(column);
                    }

                    musicGridPane.add(musicLabel, column, row);
                    musicLabel.setPrefHeight(30);
                    numMusicLabels++;

                    double newHeight = musicScrollPane.getPrefHeight() + 30;
                    musicScrollPane.setPrefHeight(newHeight);
                    musicPane.setPrefHeight(newHeight);
                }
            }
        }
    }

    @FXML
    private void recentButtonClick(ActionEvent event) {
        System.out.println("botão recent played clicado");
    }

    @FXML
    private void searchTextFieldEnter() {
        String search = searchTextField.getText();
        System.out.println("Nome da playlist: " + search);
    }

    @FXML
    private void likedButtonClick(ActionEvent event) {
        System.out.println("botão liked clicado");
    }

    @FXML
    private void artistsButtonClick(ActionEvent event) {
        System.out.println("botão artists clicado");
    }

    @FXML
    private void albumsButtonClick(ActionEvent event) {
        System.out.println("botão albums clicado");
    }

    @FXML
    private void playlistsButtonClick(ActionEvent event) {
        System.out.println("botão playlists clicado");
    }

    @FXML
    private void loginButtonClick(ActionEvent event) {
        System.out.println("botão login spotify clicado");
    }

    @FXML
    private void newPlaylistButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/new-playlist.fxml"));
            Parent root2 = fxmlLoader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Nova Janela");
            secondStage.setScene(new Scene(root2));
            secondStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void favoriteMedia() {
        System.out.println("media favoritada");
        favoriteButton.setImage(new Image("images/fullLikedButton.png"));
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

        //selectedAudioFile = contPL.procurarMusica(label.getText()).getMp3();

        if (selectedAudioFile != null) {
            if (player == null) {
                media = new Media(selectedAudioFile.toURI().toString());
                player = new MediaPlayer(media);
                player.setOnEndOfMedia(() -> {
                    player.stop();
                    playButton.setImage(new Image("images/playButton.png"));
                    isPlaying = false;
                });
                playButton.setImage(new Image("images/pauseButton.png"));
                player.play();
                contPL.salvarReproducao(selectedAudioFile, LocalDateTime.now());
                isPlaying = true;
                beginTimer();
            } else if (isPlaying) {
                player.pause();
                playButton.setImage(new Image("images/playButton.png"));
                isPlaying = false;
            } else {
                player.play();
                playButton.setImage(new Image("images/pauseButton.png"));
                isPlaying = true;
            }
        } else {
            System.out.println("A midia não é válida");
        }
    }

    @FXML
    private void nextMedia() {
        System.out.println("proxima media");
    }

    @FXML
    private void loopMedia() {
        System.out.println("modo loop");
    }

    @FXML
    private void inputPlaylistNameTextFieldContent() {
        playlistNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Texto digitado:" + newValue);
        });
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

    private void applyColorAnimationButton(Button button) {

        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Entrou");
                button.setStyle("-fx-background-color: #060606");
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Saiu");
                button.setStyle("-fx-background-color: #121212");
            }
        });
    }

    @FXML
    private void toColorLikedSongsButton() {
        applyColorAnimationButton(likedButton);
    }

    @FXML
    private void toColorArtistButton() {
        applyColorAnimationButton(artistsButton);
    }

    @FXML
    private void toColorAlbumsButton() {
        applyColorAnimationButton(albumsButton);
    }

    private void applyColorAnimationSideBar(HBox hBox) {
        hBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent2) {
                System.out.println("Entrou");
                hBox.setStyle("-fx-background-color: #060606");
            }
        });

        hBox.setOnMouseExited(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Saiu");
                hBox.setStyle("-fx-background-color: #121212");
            }
        });
    }

    @FXML
    private void toColorNewPlaylistButton() {
        applyColorAnimationSideBar(newPlaylistContainer);
    }

    @FXML
    private void toColorHomeButton() {
        applyColorAnimationSideBar(homeContainer);
    }

    @FXML
    private void toColorNewSongButton() {
        applyColorAnimationSideBar(newSongContainer);
    }

    @FXML
    private void toColorNewFolderButton(){
        applyColorAnimationSideBar(newFolderContainer);
    }

    @FXML
    private void toColorRecentPlayedButton() {
        applyColorAnimationSideBar(recentPlayedContainer);
    }

// #####################################################################################################
// ----------------------------------NEW-PLAYLIST PAGE--------------------------------------------------
// #####################################################################################################

    @FXML
    private void createPlaylistButtonClick() {
        System.out.println("Playlist criada");
        String playlistName = playlistNameTextField.getText();
        System.out.println("Nome da playlist: " + playlistName);
        Stage stage = (Stage) createPlaylistButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelPlaylistButtonClick() {
        System.out.println("Cancelado");
        Stage stage = (Stage) cancelPlaylistButton.getScene().getWindow();
        stage.close();
    }
}