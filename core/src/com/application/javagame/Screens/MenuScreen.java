package com.application.javagame.Screens;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen extends ScreenAdapter {

    private final GameState state;

    Stage stage;
    Sound activeBig, active;
    FitViewport viewport;
    OrthographicCamera camera;
    String[] texts = {
        "VOCÊ CAIU NO INFERNO",
        "VOCÊ SÓ TEM A SI MESMO",
        "E ARMAS PODEROSAS",
        "SOBREVIVA",
        ""
    };

    int menuClicks = 0;
    int maxMenuClicks = texts.length + 1;
    boolean changed = false;
    Label text;

    public MenuScreen(GameState s) {
        state = s;

        active = Assets.Get("sounds/next.mp3");

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float fWidth = Gdx.graphics.getWidth();
        float fHeight = Gdx.graphics.getHeight();

        stage = new Stage();
        Stack stack = new Stack();
        stack.setWidth(fWidth);
        stack.setHeight(fHeight);

        stage.addActor(stack);

        Image image = new Image(Assets.<Texture>Get("hellrise black.png"));       
        
        Image white = new Image(Assets.<Texture>Get("white.png"));       
        Table table = new Table();
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight());

        stack.add(white);
        stack.add(image);
        stack.add(table);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/eternal.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24; // font size
        BitmapFont doomFont = generator.generateFont(parameter);
        LabelStyle labelStyle = new LabelStyle(doomFont, Color.RED);

        text = new Label("ENTER to START", labelStyle);

        table.center().add(text);

        Gdx.input.setInputProcessor(stage);
    }

    float a = 0;

    @Override
    public void render(float delta) {
        Gdx.input.setCursorCatched(false);
        ScreenUtils.clear(0, 0, 0, 1);

        a += delta;

        if (menuClicks == 1 && !changed) {
            changed = true;
            stage = new Stage();
            Table t = new Table();
            t.setWidth(Gdx.graphics.getWidth());
            t.setHeight(Gdx.graphics.getHeight());
            stage.addActor(t);
            text.setColor(1, 1, 1, 1);
            t.center().add(text);
        } else if(menuClicks > 0) {
            text.setText(texts[menuClicks - 1]);   
        }

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            menuClicks++;
            if (menuClicks < maxMenuClicks - 1) active.play();
            else state.game.setScreen(new PlayScreen(state));
        }
    }

    @Override
    public void resize(int newW, int newH) {
        viewport.update(newW, newH);
    }
}
