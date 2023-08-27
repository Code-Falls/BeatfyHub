package com.example.beatfyhub;

import com.example.beatfyhub.ControladorPlayerLocal;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.util.Callback;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.xml.sax.SAXException;

import java.net.URL;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;

public class MainController {

    @FXML private Button homeButton, newFolderButton, newSongButton, recentButton, likedButton, adicionarMusicaPlaylistButton, editarPlaylistButton, removerMusicaPlaylistButton, newPlaylistButton, createPlaylistButton, cancelPlaylistButton;
    @FXML private ImageView playButton, previousButton, nextButton, loopButton, shuffleButton, favoriteButton;
    @FXML private HBox homeContainer, exploreContainer, recentPlayedContainer, newPlaylistContainer, newSongContainer, newFolderContainer;
    @FXML private TextField playlistNameTextField, searchTextField;
    @FXML private ScrollPane musicScrollPane;
    @FXML private ProgressBar musicProgressBar = new ProgressBar();
    @FXML private Slider volumeSlider = new Slider();
    @FXML private Label songLabel, artistLabel;
    //========TabelaPrincipal============//
    @FXML private TableView<Musica> musicTableView = new TableView<>();
    @FXML private TableColumn<Musica, Void> playColumn = new TableColumn<>("Play");
    @FXML private TableColumn<Musica, Void> optionColumn = new TableColumn<>("Options");
    @FXML private TableColumn<Musica, String> nameColumn = new TableColumn<>("Nome");
    @FXML private TableColumn<Musica, String> albumColumn = new TableColumn<>("Album");
    @FXML private TableColumn<Musica, String> artistColumn = new TableColumn<>("Artista");
    //===================================//

    //=========TabelaPlaylists===========//
    @FXML private TableView<Playlist> playlistTableView = new TableView<>();
    @FXML private TableColumn<Playlist, Void> playPlaylistColumn = new TableColumn<>("Select");
    @FXML private TableColumn<Playlist, String> namePlaylistColumn = new TableColumn<>("Nome");
    @FXML private TableColumn<Playlist, Void> removePlaylistColumn = new TableColumn<>("Remove");
    //===================================//
    private Media media;
    private MediaPlayer mediaPlayer;
    private Timer timer = new Timer();
    private TimerTask task;
    private MediaPlayer player;
    private File selectedAudioFile;
    private int numMusicLabels = 0;
    private ArrayList<File> songs;
    private boolean isPlaying;
    private ControladorPlayerLocal contPL = new ControladorPlayerLocal();
    private File musicaTocando;
    private static ObservableList<Musica> musicList = FXCollections.observableArrayList();
    private static ObservableList<Playlist> playlistList = FXCollections.observableArrayList();
    private static ObservableList<Musica> musicasPlaylist = FXCollections.observableArrayList();

    private static boolean trocar = false;

    private Playlist playlistSelecionada;

    private ArrayList<Musica> playlistTocarInteira = new ArrayList<>();
    private int songNumber;

    @FXML
    private void initialize() {

        addButtonToTable();
        addButtonEraseToTable();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));


        if(trocar){

            if(!playlistSelecionada.getPlaylist().isEmpty()){
                musicasPlaylist.removeAll(musicasPlaylist);
                musicasPlaylist.addAll(playlistSelecionada.getPlaylist());
                System.out.println("caiu");
            }
            else{
                musicasPlaylist.removeAll(musicasPlaylist);
                System.out.println("oi"+musicasPlaylist);
            }
            musicTableView.setItems(musicasPlaylist);
            trocar = false;
        }

        //Tabela de Playlist==================
        System.out.println("Aqui3");
        for (Playlist p: contPL.getMyPlaylists()){
            if(!playlistList.contains(p)){
                playlistList.add(p);
            }
        }

        playlistTableView.setItems(playlistList);

        addButtonToTablePlaylist();
        addButtonToTableRemovePlaylist();
        namePlaylistColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        //====================================

        musicProgressBar.setStyle("-fx-accent: #8a1cff");
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                player.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        if(!playlistTocarInteira.isEmpty()){
            media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
            player = new MediaPlayer(media);
            songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
            musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        }

    }
    //adicionar botoes tabela playlist

    private void addButtonToTablePlaylist(){
        Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>> cellFactory = new Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>>() {
            @Override
            public TableCell<Playlist, Void> call(final TableColumn<Playlist, Void> param) {
                final TableCell<Playlist, Void> cell = new TableCell<Playlist, Void>() {

                    private final Button btn = new Button("Play");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Playlist p = getTableView().getItems().get(getIndex());
                            playlistSelecionada = p;
                            playlistTocarInteira = p.getPlaylist();
                            songNumber = 0;
                            if(!playlistTocarInteira.isEmpty()){
                                media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
                                player = new MediaPlayer(media);
                            }
                            System.out.println(p);
                            trocar = true;
                            initialize();
                        });
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
                return cell;
            }
        };

        playPlaylistColumn.setCellFactory(cellFactory);

    }

    private void addButtonToTableRemovePlaylist(){
        Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>> cellFactory = new Callback<TableColumn<Playlist, Void>, TableCell<Playlist, Void>>() {
            @Override
            public TableCell<Playlist, Void> call(final TableColumn<Playlist, Void> param) {
                final TableCell<Playlist, Void> cell = new TableCell<Playlist, Void>() {

                    private final Button btn = new Button("Remove");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Playlist p = getTableView().getItems().get(getIndex());
                            musicList.removeAll();
                            playlistList.remove(p);
                            playlistTableView.setItems(playlistList);
                            contPL.destruirPlaylist(p.getNome());
                        });
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
                return cell;
            }
        };

        removePlaylistColumn.setCellFactory(cellFactory);

    }

    //============================================================


    //=============Adicionar botoes tabela principal====================
    private void addButtonToTable(){
        Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>> cellFactory = new Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>>() {
            @Override
            public TableCell<Musica, Void> call(final TableColumn<Musica, Void> param) {
                final TableCell<Musica, Void> cell = new TableCell<Musica, Void>() {

                    private final Button btn = new Button("Play");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Musica m = getTableView().getItems().get(getIndex());
                            if(isPlaying){
                                player.stop();
                            }
                            selectedAudioFile = m.getMp3();
                            //ToDo arrumar song number na playlist==================================
                            songNumber = contPL.getMySongs().indexOf(m);
                            //playMedia();
//                            definirMusicaaTocar(m);
//                            songLabel.setText(m.getNome().replace(".mp3", ""));
                            tocarMusicaSelecionada();
                        });
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
                return cell;
            }
        };

        playColumn.setCellFactory(cellFactory);

    }


    private void addButtonEraseToTable(){
        Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>> cellFactory = new Callback<TableColumn<Musica, Void>, TableCell<Musica, Void>>() {
            @Override
            public TableCell<Musica, Void> call(final TableColumn<Musica, Void> param) {
                final TableCell<Musica, Void> cell = new TableCell<Musica, Void>() {

                    private final Button btn = new Button("Apagar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Musica m = getTableView().getItems().get(getIndex());
                            if(player!=null){
                                player.stop();
                            }
                            musicList.remove(m);
                            contPL.removerMusica(m.getNome());
                        });
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
                return cell;
            }
        };

        optionColumn.setCellFactory(cellFactory);

    }

//========================================================

    public void beginTimer () {
        timer = new Timer();

        task = new TimerTask() {
            public void run() {

                isPlaying = true;
                double current = player.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end);
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
        musicTableView.setItems(musicList);
        playlistTocarInteira = contPL.getMySongs();
    }

    @FXML
    private void newSongButtonClick(ActionEvent event){
        System.out.println("botão explore clicado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        selectedAudioFile = fileChooser.showOpenDialog(newSongButton.getScene().getWindow());
        contPL.adicionarMusica(selectedAudioFile);
        musicList.add(contPL.procurarMusica(selectedAudioFile.getName()));
//        selectedAudioFile = null;
        musicTableView.setItems(musicList);
        initialize();
    }
    //toDO Arrumar um local para colocar, ele está funcionando, mas não sei onde chamar a funçaõ
    //toDo por enquanto ele está no last Played
    public void metadataCatcher(){
        File mp3File = selectedAudioFile;

        try {
            AudioFile audioFile = AudioFileIO.read(mp3File);
            Tag tag = audioFile.getTag();

            String artist = tag.getFirst(FieldKey.ARTIST);
            String title = tag.getFirst(FieldKey.TITLE);

            songLabel.setText(title);
            artistLabel.setText(artist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void newFolderButtonClick(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(newFolderButton.getScene().getWindow());

        if (selectedFolder != null) {
            File[] musicFiles = selectedFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
            if (musicFiles != null) {
                for (File mf : musicFiles) {
                    contPL.adicionarMusica(mf);
                    if(!(musicList.contains(contPL.procurarMusica(mf.getName())))){
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
    private void recentButtonClick(ActionEvent event) {
        System.out.println("botão recent played clicado");
        metadataCatcher();
    }

    @FXML
    private void searchTextFieldEnter() {
        String search = searchTextField.getText();
        System.out.println("Nome da playlist: " + search);
    }

    @FXML
    private void editarPlaylistsButtonClick(ActionEvent event) {
        System.out.println("botão editar");
    }

    @FXML
    private void removerMusicaPlaylistButtonClick(ActionEvent event) {
        System.out.println("botão remover");
    }

    @FXML
    private void adicionarMusicaPlaylistButtonClick(ActionEvent event) {
        System.out.println("botão adicionar");
        playlistSelecionada.adicionarMusica(contPL.procurarMusica(selectedAudioFile.getName()));
        trocar = true;
        initialize();
    }

    @FXML
    private void playlistsButtonClick(ActionEvent event) {
        System.out.println("botão playlists clicado");
        System.out.println("aqui"+playlistList);
        System.out.println("kivbads"+playlistTableView);
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

        System.out.println(musicaTocando.getName());

        contPL.favoritar(musicaTocando);

        if(contPL.procurarMusica(musicaTocando.getName()).getFavorita()){
            System.out.println("aqui3");
            favoriteButton.setImage(new Image("images/fullLikedButton.png"));
        }
        else {
            favoriteButton.setImage(new Image("images/favoritar.png"));
        }
    }

    @FXML
    private void shuffleMedia() {
        System.out.println("modo aleatorio");

    }

    @FXML
    private void previousMedia() {
        if (songNumber > 0) {
            songNumber--;

        }
        else {
            songNumber = playlistTocarInteira.size() - 1;

        }
        player.stop();
        if(isPlaying) {
            cancelTimer();
        }
        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
        player = new MediaPlayer(media);
        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        playMedia();
    }


    @FXML
    private void playMedia() {
        if(isPlaying){
            pausarMusica();
        }
        else{
            resumirMusica();
        }
    }

    @FXML
    private void nextMedia() {
        if (songNumber < playlistTocarInteira.size() -1) {
            songNumber++;

        }
        else {
            songNumber = 0;

        }
        player.stop();
        if(isPlaying) {
            cancelTimer();
        }
        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
        player = new MediaPlayer(media);
        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        playMedia();
    }

    @FXML
    private void loopMedia() {
        System.out.println("modo loop");
    }

    @FXML
    private void saveButtonClick() {
        System.out.println("Arquivos Salvos");
        contPL.salvar();
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



    // ===============FUNCOES PLAYER=======================

    public void definirMusicaaTocar(Musica m){
        if (m != null) {
            musicaTocando = m.getMp3();
        }
    }

    public void tocarMusicaSelecionada(){
        if(musicaTocando!=null){
            media = new Media(musicaTocando.toURI().toString());
            player = new MediaPlayer(media);
            player.setOnEndOfMedia(() -> {
                player.stop();
                playButton.setImage(new Image("images/playButton.png"));
                isPlaying = false;
            });
            playButton.setImage(new Image("images/pauseButton.png"));
            player.play();
            contPL.salvarReproducao(musicaTocando, LocalDateTime.now());
            isPlaying = true;
            beginTimer();
        }
        media = new Media(playlistTocarInteira.get(songNumber).getMp3().toURI().toString());
        player = new MediaPlayer(media);
        songLabel.setText(playlistTocarInteira.get(songNumber).getNome());
        musicaTocando = playlistTocarInteira.get(songNumber).getMp3();
        playMedia();
    }

    public void pausarMusica(){
        player.pause();
        playButton.setImage(new Image("images/playButton.png"));
        isPlaying = false;
    }

    public void resumirMusica(){
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
        if(!playlistNameTextField.getText().isEmpty() && !playlistNameTextField.getText().isBlank()){
            contPL.criarPLaylist(playlistNameTextField.getText());
            initialize();
        }
        stage.close();
    }

    @FXML
    private void cancelPlaylistButtonClick() {
        System.out.println("Cancelado");
        Stage stage = (Stage) cancelPlaylistButton.getScene().getWindow();
        stage.close();
    }
}