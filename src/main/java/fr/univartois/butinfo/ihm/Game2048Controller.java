package fr.univartois.butinfo.ihm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Random;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

public class Game2048Controller {
    private final Label[][] interieurGrillage = new Label[4][4];
    private int score = 0;
    private int bestScore = 0;
    private final Random random = new Random();
    private Grid grid = new Grid();


    @FXML
    private VBox background;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private Label gameOverText;

    @FXML
    private GridPane grillage;

    @FXML
    private Button newGame;

    @FXML
    private Label scoreLabel;

    @FXML
    void restart(ActionEvent event) {
        restartGame();
    }
    public void restartGame() {
        // Réinitialiser le score
        score = 0;
        scoreLabel.setText(String.valueOf(score));

        // Effacer le contenu de la grille
        grid.clear();

        // Générer de nouvelles tuiles
        generateNewTile();
        generateNewTile();

        // Réinitialiser le texte du label gameOverText
        gameOverText.setText("");

        // Mettre à jour l'affichage de la grille
        updateUI();

        // Réactiver le bouton pour démarrer une nouvelle partie
        newGame.setDisable(false);

        // Réactiver le bouton pour démarrer une nouvelle partie
        newGame.setDisable(false);
        newGame.setFocusTraversable(false);
    }

    @FXML
    void initialize() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Label label = new Label();
                label.setFont(new Font(20)); // Changer la taille de la police à 20

                BorderPane borderPane = new BorderPane();
                borderPane.setPrefSize(60, 60); // Contrôler la taille du BorderPane

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(borderPane, label);
                grillage.add(stackPane, j, i);

                interieurGrillage[i][j] = label;
            }
        }
        newGame.setOnAction(event -> {
            restartGame();
            event.consume();
        });
        newGame.setFocusTraversable(false);
    }
    // Ajouter une méthode pour mettre à jour le score
    private void updateScore(int value) {
        score += value;
        scoreLabel.setText(String.valueOf(score));
        if (score > bestScore) {
            bestScore = score;
            bestScoreLabel.setText(String.valueOf(bestScore));
        }
    }
    private void generateNewTile() {
        List<Tile> availableTiles = grid.availableTiles();
        if (!availableTiles.isEmpty()) {
            Tile randomTile = availableTiles.get(random.nextInt(availableTiles.size()));
            int value = (random.nextDouble() < 0.9) ? 2 : 4;
            randomTile.setValue(value);
        }
    }

    private void updateUI() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int value = grid.get(i, j).getValue();
                if (value != 0) {
                    interieurGrillage[i][j].setText(String.valueOf(value));
                } else {
                    interieurGrillage[i][j].setText(""); // Ne pas afficher les zéros
                }
                ((StackPane)interieurGrillage[i][j].getParent()).setBackground(computeBackground(value));
            }
        }
    }
    private static javafx.scene.layout.Background computeBackground(int value) {
        Color color;
        switch (value) {
            case 2:
                color = Color.web("#EEE4DA"); // Couleur pour la tuile 2
                break;
            case 4:
                color = Color.web("#EDE0C8"); // Couleur pour la tuile 4
                break;
            case 8:
                color = Color.web("#F2B179"); // Couleur pour la tuile 8
                break;
            case 16:
                color = Color.web("#F59563"); // Couleur pour la tuile 16
                break;
            case 32:
                color = Color.web("#F67C5F"); // Couleur pour la tuile 32
                break;
            case 64:
                color = Color.web("#F65E3B"); // Couleur pour la tuile 64
                break;
            case 128:
                color = Color.web("#EDCF72"); // Couleur pour la tuile 128
                break;
            case 256:
                color = Color.web("#EDCC61"); // Couleur pour la tuile 256
                break;
            case 512:
                color = Color.web("#EDC850"); // Couleur pour la tuile 512
                break;
            case 1024:
                color = Color.web("#EDC53F"); // Couleur pour la tuile 1024
                break;
            case 2048:
                color = Color.web("#EDC22E"); // Couleur pour la tuile 2048
                break;
            default:
                color = Color.web("#CDC1B4"); // Couleur par défaut pour les autres tuiles
                break;
        }

        // On crée enfin l'arrière-plan à partir de cette couleur.
        return new javafx.scene.layout.Background(
                new BackgroundFill(
                        color,
                        CornerRadii.EMPTY,
                        new Insets(7))); // Changez cette valeur pour ajuster l'espace
    }
    public void startGame() {
        // Réinitialiser la grille et le score
        grid = new Grid();
        score = 0;

        // Ajouter une valeur à deux tuiles de la grille
        generateNewTile();
        generateNewTile();

        // Mettre à jour l'affichage de la grille
        updateUI();
        // ce qui permet d'utiliser les flèches directionnelles
        setupKeyEventListeners();
    }
    private void setupKeyEventListeners() {
        background.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    move(MoveDirection.UP);
                    break;
                case DOWN:
                    move(MoveDirection.DOWN);
                    break;
                case LEFT:
                    move(MoveDirection.LEFT);
                    break;
                case RIGHT:
                    move(MoveDirection.RIGHT);
                    break;
            }
        });
    }
    public void move(MoveDirection direction) {
        MoveResult result = grid.move(direction);
        if (result.hasMoved()) {
            updateScore(result.getMergeScore());
            generateNewTile();
            updateUI();
            if (grid.isBlocked()) {
                newGame.setDisable(false);
                gameOverText.setText("Game Over !");
            }
        }
    }



}