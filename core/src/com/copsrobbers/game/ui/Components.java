package com.copsrobbers.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.copsrobbers.game.CopsAndRobbers;

public class Components {
    public static Label createLabel(String text, int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (fontSize * Gdx.graphics.getDensity());
        parameter.color = Color.WHITE;
        BitmapFont font24 = generator.generateFont(parameter); // font size in pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        return new Label(text, labelStyle);
    }

    public static TextArea createTextArea(String text, int fontSize, boolean disable) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (fontSize * Gdx.graphics.getDensity());
        parameter.color = Color.WHITE;
        BitmapFont font24 = generator.generateFont(parameter); // font size in pixels
        generator.dispose();

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font24;
        tfStyle.fontColor = Color.WHITE;
        TextArea textArea = new TextArea(text, tfStyle);
        textArea.setDisabled(disable);
        return textArea;
    }

    public static TextButton createTextButton(String text, InputListener listener) {
        TextButton button = new TextButton(text, CopsAndRobbers.gameSkin);
        button.setLabel(createLabel(text, 20));
        button.getLabel().setAlignment(Align.center);
        button.addListener(listener);
        return button;
    }
}
