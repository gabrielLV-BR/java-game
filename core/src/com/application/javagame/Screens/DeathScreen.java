package com.application.javagame.Screens;

import java.security.Key;
import java.util.HashMap;

import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import com.application.javagame.GameState;
import com.application.javagame.Data.DBConnection;
import com.application.javagame.Data.Score;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Managers.InputManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DeathScreen extends ScreenAdapter {

    private final GameState state;

    InputManager inputManager;

    Stage stage;
    FitViewport viewport;
    OrthographicCamera camera;

    HashMap<Integer, Character> intToChar; 

    Label nameLabel;

    LabelStyle red, white;
    float timer = 3;

    int i = 0;
    final int NAME_SIZE = 7;

    public DeathScreen(GameState s) {
        state = s;
        i = 0;

        // letters = new char[5];
        // for(int i = 0; i < letters.length; i++)
        //     letters[i] = ' ';

        inputManager = InputManager.GetInputManager();
        Gdx.input.setInputProcessor(inputManager);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/eternal.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24; // font size
        parameter.color = Color.WHITE;
        BitmapFont doomFont = generator.generateFont(parameter);
        red = new LabelStyle(doomFont, Color.RED);
        white = new LabelStyle(doomFont, Color.WHITE);

        nameLabel = new Label("", white);

        stage = new Stage();
        firstScreen();

        intToChar = new HashMap<>();

        intToChar.put(Keys.SPACE, ' ');
        intToChar.put(Keys.Z, 'Z');
        intToChar.put(Keys.Y, 'Y');
        intToChar.put(Keys.X, 'X');
        intToChar.put(Keys.W, 'W');
        intToChar.put(Keys.V, 'V');
        intToChar.put(Keys.U, 'U');
        intToChar.put(Keys.T, 'T');
        intToChar.put(Keys.S, 'S');
        intToChar.put(Keys.R, 'R');
        intToChar.put(Keys.Q, 'Q');
        intToChar.put(Keys.P, 'P');
        intToChar.put(Keys.O, 'O');
        intToChar.put(Keys.N, 'N');
        intToChar.put(Keys.M, 'M');
        intToChar.put(Keys.L, 'L');
        intToChar.put(Keys.K, 'K');
        intToChar.put(Keys.J, 'J');
        intToChar.put(Keys.I, 'I');
        intToChar.put(Keys.H, 'H');
        intToChar.put(Keys.G, 'G');
        intToChar.put(Keys.F, 'F');
        intToChar.put(Keys.E, 'E');
        intToChar.put(Keys.D, 'D');
        intToChar.put(Keys.C, 'C');
        intToChar.put(Keys.B, 'B');
        intToChar.put(Keys.A, 'A');
    }

    private void firstScreen() {
        stage.clear();
        Table t = new Table();
        t.setWidth(Gdx.graphics.getWidth());
        t.setHeight(Gdx.graphics.getHeight());
        stage.addActor(t);
        Label l = new Label("VOCÊ MORREU", white);
        t.center().add(l);
    }


    private void secondScreen() {
        stage.clear();
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setWidth(Gdx.graphics.getWidth());
        verticalGroup.setHeight(Gdx.graphics.getHeight());

        stage.addActor(verticalGroup);

        Label l = new Label("QUAL SEU NOME?", red);
        nameLabel.setText("");
        // HorizontalGroup g = new HorizontalGroup();

        // for(int i = 0; i < nameLabels.length; i++) {
        //     nameLabels[i] = new Label(" ", red);
        //     nameLabels[i].setColor(Color.WHITE);
        //     g.addActor(nameLabels[i]);
        // }

        verticalGroup.center().addActor(l);
        verticalGroup.center().addActor(nameLabel);
        // verticalGroup.center().addActor(g);

    }

    @Override
    public void render(float delta) {
        Gdx.input.setCursorCatched(false);
        ScreenUtils.clear(0, 0, 0, 1);

        if(timer > 0) {
            timer -= delta;
        } else if (timer <= 0 && timer > -1000) {
            timer = -999999;
            secondScreen();
        } else {
            // for(Label l : nameLabels) l.setStyle(red);
            int k = inputManager.getLastKey();
            if(k != -1) {
                if(k == Keys.ENTER) {
                    while(nameLabel.getText().length < NAME_SIZE) {
                        nameLabel.getText().append(" ");
                    }
                    try {
                        DBConnection dbConnection = new DBConnection();
                        String name = nameLabel.getText().toString();
                        System.out.println(name);
                        dbConnection.insertScore(new Score(name, state.getPoints()));
                        state.game.setScreen(new MenuScreen(state));
                        return;
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                } else if (k == Keys.BACKSPACE && nameLabel.getText().length > 0) {
                    int size = nameLabel.getText().length;
                    StringBuilder n = new StringBuilder(nameLabel.getText());
                    n.delete(size - 1, size);
                    nameLabel.setText(n.toString());
                }  else {
                    char letter = intToChar.getOrDefault(k, '0');
                    if(letter != '0' && nameLabel.getText().length < NAME_SIZE) {
                        // nameLabel.getText().append("" + letter);
                        nameLabel.setText(nameLabel.getText().toString() + letter);
                    }
                }
    
            }
            // nameLabels[(i + 1) % nameLabels.length].setStyle(white);
        }

        // nameLabel.setText(nameLabel.getText().toString());
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int newW, int newH) {
        viewport.update(newW, newH);
    }
}
