package com.example.beatfyhub;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;

public class MainController {

    @FXML
    private Button newFolderButton, newSongButton, createPlaylistButton, cancelPlaylistButton;
    @FXML
    private ImageView playButton, previousButton, nextButton, loopButton, shuffleButton, favoriteButton, albumCoverImage;
    @FXML
    private HBox homeContainer, recentPlayedContainer, newPlaylistContainer, newSongContainer, newFolderContainer;
    @FXML
    private TextField playlistNameTextField, searchTextField;
    @FXML
    private ProgressBar musicProgressBar = new ProgressBar();
    @FXML
    private Slider volumeSlider = new Slider();
    @FXML
    private Label songLabel, artistLabel;
    //========TabelaPrincipal============//
    @FXML
    private TableView<Musica> musicTableView = new TableView<>();
    @FXML
    private TableColumn<Musica, Void> playColumn = new TableColumn<>("Play");
    @FXML
    private TableColumn<Musica, Void> optionColumn = new TableColumn<>("Options");
    @FXML
    private TableColumn<Musica, String> nameColumn = new TableColumn<>("Nome");
    @FXML
    private TableColumn<Musica, String> albumColumn = new TableColumn<>("Album");
    @FXML
    private TableColumn<Musica, String> artistColumn = new TableColumn<>("Artista");
    //===================================//

    //=========TabelaPlaylists===========//
    @FXML
    private TableView<Playlist> playlistTableView = new TableView<>();
    @FXML
    private TableColumn<Playlist, Void> playPlaylistColumn = new TableColumn<>("Select");
    @FXML
    private TableColumn<Playlist, String> namePlaylistColumn = new TableColumn<>("Nome");
    @FXML
    private TableColumn<Playlist, Void> removePlaylistColumn = new TableColumn<>("Remove");
    //===================================//

    //=========TabelaHistorico===========//

    @FXML
    private TableView<ReproducaoMusica> lastPlayedTableView = new TableView<>();
    @FXML
    private TableColumn<ReproducaoMusica, String> lastPlayedColumnNome = new TableColumn<>("Nome");
    @FXML
    private TableColumn<ReproducaoMusica, String> lastPlayedColumnArtista = new TableColumn<>("Artista");
    @FXML
    private TableColumn<ReproducaoMusica, String> lastPlayedColumnAlbum = new TableColumn<>("Album");
    @FXML
    private TableColumn<ReproducaoMusica, LocalDateTime> lastPlayedColumnHorario = new TableColumn<>("Horario");


    //===================================//
    private Media media;
    private Timer timer = new Timer();
    private MediaPlayer player;
    private File selectedAudioFile;
    private boolean isPlaying;
    private final ControladorPlayerLocal contPL = new ControladorPlayerLocal();
    private File musicaTocando;
    private static final ObservableList<Musica> musicList = FXCollections.observableArrayList();
    private static final ObservableList<Playlist> playlistList = FXCollections.observableArrayList();
    private static final ObservableList<Musica> musicasPlaylist = FXCollections.observableArrayList();
    private static final ObservableList<ReproducaoMusica> historicoList = FXCollections.observableArrayList();
    private static boolean trocar = false;
    private Playlist playlistSelecionada;
    private final Playlist favoritas = new Playlist("Favoritas");
    private ArrayList<Musica> playlistTocarInteira = new ArrayList<>();
    private int songNumber;

    @FXML
    private void initialize() {
        addButtonToTable();
        addButtonEraseToTable();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        if (trocar) {

            if (!playlistSelecionada.getPlaylist().isEmpty()) {
                musicasPlaylist.removeAll(musicasPlaylist);
                musicasPlaylist.addAll(playlistSelecionada.getPlaylist());
            } else {
                musicasPlaylist.removeAll(musicasPlaylist);
            }
            musicTableView.setItems(musicasPlaylist);
            trocar = false;
        }

        //Tabela historico

        lastPlayedColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        lastPlayedColumnArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        lastPlayedColumnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        lastPlayedColumnHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));

        //================

        //Tabela de Playlist==================

        for (Playlist p : contPL.getMyPlaylists()) {
            if (!playlistList.contains(p)) {
                playlistList.add(p);
            }
        }

        playlistTableView.setItems(playlistList);

        addButtonToTablePlaylist();
        addButtonToTableRemovePlaylist();
        namePlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //====================================

        musicProgressBar.setStyle("-fx-accent: #8a1cff");
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> player.setVolume(volumeSlider.getValue() * 0.01));

        if (!playlistTocarInteira.isEmpty() && !isPlaying) {
            media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
            player = new MediaPlayer(media);
            songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
            artistLabel.setText(playlistTocarInteira.get(songNumber).getArtista());
            albumCoverImage.setImage(playlistTocarInteira.get(songNumber).getImage());
            musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
            contPL.salvarReproducao(musicaTocando, LocalDateTime.now());
            if (contPL.procurarMusica(musicaTocando.getName()).getFavorita()) {
                favoriteButton.setImage(new Image("images/fullLikedButton.png"));
            } else {
                favoriteButton.setImage(new Image("images/favoritar.png"));
            }
        }

    }
    //adicionar botoes tabela playlist

    private void addButtonToTablePlaylist() {
        Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Playlist, Void> call(final TableColumn<Playlist, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("▶");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Playlist p = getTableView().getItems().get(getIndex());
                            playlistSelecionada = p;
                            playlistTocarInteira = p.getPlaylist();
                            songNumber = 0;
                            trocar = true;
                            initialize();
                        });
                        btn.setStyle("-fx-background-color: #232323; -fx-background-radius: 10; -fx-text-fill: #fff");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        playPlaylistColumn.setCellFactory(cellFactory);

    }

    private void addButtonToTableRemovePlaylist() {
        Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Playlist, Void> call(final TableColumn<Playlist, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("✖");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Playlist p = getTableView().getItems().get(getIndex());
                            musicList.removeAll();
                            playlistList.remove(p);
                            playlistTableView.setItems(playlistList);
                            contPL.destruirPlaylist(p.getNome());
                        });
                        btn.setStyle("-fx-background-color: #232323; -fx-background-radius: 10; -fx-text-fill: #ff0000");

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        removePlaylistColumn.setCellFactory(cellFactory);

    }

    //============================================================


    //=============Adicionar botoes tabela principal====================
    private void addButtonToTable() {
        Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Musica, Void> call(final TableColumn<Musica, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("▶");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Musica m = getTableView().getItems().get(getIndex());
                            selectedAudioFile = m.getMp3();
                            songNumber = contPL.getMySongs().indexOf(m);
                            initialize();
                            playMedia();
                        });
                        btn.setStyle("-fx-background-color: #232323; -fx-background-radius: 10; -fx-text-fill: #fff");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        playColumn.setCellFactory(cellFactory);

    }


    private void addButtonEraseToTable() {
        Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Musica, Void> call(final TableColumn<Musica, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("✖");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Musica m = getTableView().getItems().get(getIndex());
                            if (player != null) {
                                player.stop();
                            }
                            musicList.remove(m);
                            contPL.removerMusica(m.getNome());
                        });
                        btn.setStyle("-fx-background-color: #232323; -fx-background-radius: 10; -fx-text-fill: #ff0000");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        optionColumn.setCellFactory(cellFactory);

    }

//========================================================

    public void beginTimer() {
        timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {

                isPlaying = true;
                double current = player.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current / end);
                musicProgressBar.setProgress(current / end);
//                Platform.runLater(() -> musicProgressBar.setProgress(progress));

                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }


    public void cancelTimer() {
        isPlaying = false;
        timer.cancel();
    }


    @FXML
    private void homeButtonClick() {
        musicTableView.setItems(musicList);
        playlistTocarInteira = contPL.getMySongs();
    }

    @FXML
    private void newSongButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        selectedAudioFile = fileChooser.showOpenDialog(newSongButton.getScene().getWindow());
        contPL.adicionarMusica(selectedAudioFile);
        musicList.add(contPL.procurarMusica(selectedAudioFile.getName()));
        musicTableView.setItems(musicList);
        playlistTocarInteira = contPL.getMySongs();
        initialize();
    }

    @FXML
    private void newFolderButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(newFolderButton.getScene().getWindow());

        if (selectedFolder != null) {
            File[] musicFiles = selectedFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (musicFiles != null) {
                for (File mf : musicFiles) {
                    contPL.adicionarMusica(mf);
                    if (!(musicList.contains(contPL.procurarMusica(mf.getName())))) {
                        musicList.add(contPL.procurarMusica(mf.getName()));
                    }
                }
            }
        }
        musicTableView.setItems(musicList);
        playlistTocarInteira = contPL.getMySongs();
        initialize();
    }

    @FXML
    private void recentButtonClick() throws IOException {
            FXMLLoader fxml2Loader = new FXMLLoader(getClass().getResource("fxml/last-played-view.fxml"));
            Parent root3 = fxml2Loader.load();
            Stage thirdStage = new Stage();
            thirdStage.setTitle("Historico");
            thirdStage.setScene(new Scene(root3));
            thirdStage.show();

    }

    @FXML
    private void loadButtonClick(){
        for(ReproducaoMusica rp: contPL.getMyHistory()){
            if(!historicoList.contains(rp)){
                historicoList.add(rp);
            }
        }
        lastPlayedTableView.setItems(historicoList);
    }

    @FXML
    private void searchTextFieldEnter() {
    }

    @FXML
    private void editarPlaylistsButtonClick() {
        if (!searchTextField.getText().isBlank() && !searchTextField.getText().isEmpty()) {
            playlistSelecionada.setNome(searchTextField.getText());
        }
        trocar = true;
        initialize();
    }

    @FXML
    private void removerMusicaPlaylistButtonClick() {
        playlistSelecionada.removerMusica(contPL.procurarMusica(selectedAudioFile.getName()));
        trocar = true;
        initialize();
    }

    @FXML
    private void adicionarMusicaPlaylistButtonClick() {
        playlistSelecionada.adicionarMusica(contPL.procurarMusica(selectedAudioFile.getName()));
        trocar = true;
        initialize();
    }

//    @FXML
//    private void playlistsButtonClick(ActionEvent event) {
//        System.out.println("botão playlists clicado");
//        System.out.println("aqui"+playlistList);
//        System.out.println("kivbads"+playlistTableView);
//    }

    @FXML
    private void newPlaylistButtonClick() throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/new-playlist.fxml"));
            Parent root2 = fxmlLoader.load();
            Stage secondStage = new Stage();
            secondStage.setTitle("Nova Janela");
            secondStage.setScene(new Scene(root2));
            secondStage.show();

    }

    @FXML
    private void favoriteMedia() {

        System.out.println(musicaTocando.getName());

        contPL.favoritar(musicaTocando);
        if (contPL.procurarMusica(musicaTocando.getName()).getFavorita()) {
            favoriteButton.setImage(new Image("images/fullLikedButton.png"));
        } else {
            favoriteButton.setImage(new Image("images/favoritar.png"));
        }

        playlistList.remove(favoritas);
        for (Musica m : contPL.getMySongs()) {
            if (m.getFavorita()) {
                if (!favoritas.getPlaylist().contains(m)) {
                    favoritas.adicionarMusica(m);
                }
            } else {
                if (favoritas.getPlaylist().contains(m)) {
                    favoritas.removerMusica(m);
                }
            }
        }
        playlistList.add(favoritas);
    }

    @FXML
    private void shuffleMedia() throws IOException {

        Image image = new Image(new FileInputStream("image.jpg"));
        albumCoverImage.setImage(image);
        albumCoverImage.setFitWidth(70);
        albumCoverImage.setFitHeight(70);
        albumCoverImage.setPreserveRatio(false);
        albumCoverImage.setSmooth(true);
        albumCoverImage.setCache(true);
    }


    @FXML
    private void previousMedia() {
        if (songNumber > 0) {
            songNumber--;

        } else {
            songNumber = playlistTocarInteira.size() - 1;

        }
        player.stop();
        if (isPlaying) {
            cancelTimer();
        }
//        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
//        player = new MediaPlayer(media);
//        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
//        artistLabel.setText(playlistTocarInteira.get(songNumber).getArtista());
//        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        initialize();
        playMedia();
    }


    @FXML
    private void playMedia() {
        player.setVolume(volumeSlider.getValue() * 0.01);
        if (isPlaying) {
            pausarMusica();
        } else {
            resumirMusica();
            beginTimer();
        }
    }

    @FXML
    private void nextMedia() {
        if (songNumber < playlistTocarInteira.size() - 1) {
            songNumber++;

        } else {
            songNumber = 0;

        }
        player.stop();
        if (isPlaying) {
            cancelTimer();
        }
//        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
//        player = new MediaPlayer(media);
//        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
//        artistLabel.setText(playlistTocarInteira.get(songNumber).getArtista());
//        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        initialize();
        playMedia();
    }

    @FXML
    private void loopMedia() {
    }

    @FXML
    private void saveButtonClick() {
        contPL.salvar();
    }

    @FXML
    private void inputPlaylistNameTextFieldContent() {
        playlistNameTextField.textProperty().addListener((observable, oldValue, newValue) -> System.out.println("Texto digitado:" + newValue));
    }

    private void applyColorAnimationDownBar(ImageView imageView) {
        imageView.setOnMouseEntered(mouseEvent1 -> {
            imageView.setOpacity(0.5);
        });

        imageView.setOnMouseExited(mouseEvent -> {
            imageView.setOpacity(1.0);
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
        hBox.setOnMouseEntered(mouseEvent2 -> {
            hBox.setStyle("-fx-background-color: #060606");
        });

        hBox.setOnMouseExited(mouseEvent -> {
            hBox.setStyle("-fx-background-color: #121212");
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
    private void toColorNewFolderButton() {
        applyColorAnimationSideBar(newFolderContainer);
    }

    @FXML
    private void toColorRecentPlayedButton() {
        applyColorAnimationSideBar(recentPlayedContainer);
    }


    // ===============FUNCOES PLAYER=======================

//    public void definirMusicaaTocar(Musica m){
//        if (m != null) {
//            musicaTocando = m.getMp3();
//        }
//    }
//
//    public void tocarMusicaSelecionada(){
//        if(musicaTocando!=null){
//            media = new Media(musicaTocando.toURI().toString());
//            player = new MediaPlayer(media);
//            player.setOnEndOfMedia(() -> {
//                player.stop();
//                playButton.setImage(new Image("images/playButton.png"));
//                isPlaying = false;
//            });
//            playButton.setImage(new Image("images/pauseButton.png"));
//            player.play();
//            contPL.salvarReproducao(musicaTocando, LocalDateTime.now());
//            isPlaying = true;
//            beginTimer();
//        }
//        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
//        player = new MediaPlayer(media);
//        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
//        artistLabel.setText(playlistTocarInteira.get(songNumber).getArtista());
//        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
//        playMedia();
//    }

    public void pausarMusica() {
        player.pause();
        playButton.setImage(new Image("images/playButton.png"));
        isPlaying = false;
    }

    public void resumirMusica() {
        player.play();
        playButton.setImage(new Image("images/pauseButton.png"));
        isPlaying = true;
    }

    //=====================================================

// #####################################################################################################
// ----------------------------------NEW-PLAYLIST PAGE--------------------------------------------------
// #####################################################################################################

    @FXML
    private void createPlaylistButtonClick() {
        Stage stage = (Stage) createPlaylistButton.getScene().getWindow();
        if (!playlistNameTextField.getText().isEmpty() && !playlistNameTextField.getText().isBlank()) {
            contPL.criarPLaylist(playlistNameTextField.getText());
            initialize();
        }
        stage.close();
    }

    @FXML
    private void cancelPlaylistButtonClick() {
        Stage stage = (Stage) cancelPlaylistButton.getScene().getWindow();
        stage.close();
    }
}