package screens.main_menu_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.mygdx.crawl.GameDriver;
import com.mygdx.helpers.AudioHelper;
import com.mygdx.weapons.WeaponHelper;
import screens.MainMenuScreen;
import screens.main_menu_screens.weapon_buttons.*;

import java.util.ArrayList;

public class WeaponSelectScreen extends AbstractMenuScreen{
    private int MAX_ACTIVE_WEAPONS = 2;
    ArrayList<AbstractWeaponButton> inactiveWeaponButtons;
    ArrayList<AbstractWeaponButton> activeWeaponButtons;
    CharSequence weaponChoiceText, playText, backText, yourArsenalText;
    CharSequence defaultGunText, fingerGunText, launcherGunText, sniperGunText, slowGunText;

    DefaultGunButton defaultGunButton;
    FingerGunsButton fingerGunsButton;
    LauncherGunButton launcherGunButton;
    SniperGunButton sniperGunButton;
    SlowGunButton slowGunButton;

    BitmapFont underButtonFont;

    public WeaponSelectScreen(MainMenuScreen mainMenuScreen, Vector3 realPos) {
        super(mainMenuScreen);
        setRealPos(realPos);
        inactiveWeaponButtons = new ArrayList<>();
        activeWeaponButtons = new ArrayList<>();

        defaultGunButton = new DefaultGunButton();
        fingerGunsButton = new FingerGunsButton();
        launcherGunButton = new LauncherGunButton();
        sniperGunButton = new SniperGunButton();
        slowGunButton = new SlowGunButton();

        inactiveWeaponButtons.add(defaultGunButton);
        inactiveWeaponButtons.add(fingerGunsButton);
        inactiveWeaponButtons.add(launcherGunButton);
        inactiveWeaponButtons.add(sniperGunButton);
        inactiveWeaponButtons.add(slowGunButton);

        weaponChoiceText = "Choose Two Weapons";
        playText = "Play";
        backText = "Back";
        yourArsenalText = "Your Current Arsenal";

        defaultGunText = "vanilla weapon";
        fingerGunText = "finger guns";
        launcherGunText = "love launcher";
        sniperGunText = "bow of Eros";
        slowGunText = "charmer";

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(new FileHandle("assets/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 18;
        fontParameter.color = new Color(1,1,1,1);
        fontParameter.shadowColor = Color.BLACK;
        fontParameter.shadowOffsetY = 2;
        fontParameter.shadowOffsetX = 2;

        underButtonFont = fontGenerator.generateFont(fontParameter);

        hitBoxes.add(new Rectangle(realPos.x + 75, realPos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/6 - 50, 300, 100));
        hitBoxes.add(new Rectangle(realPos.x + Gdx.graphics.getWidth() - 175, realPos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/6 - 50, 300, 100));

        int i = 0;
        for (AbstractWeaponButton button : inactiveWeaponButtons){
            hitBoxes.add(button.hitBox);
            button.index = i;
            i++;
        }
        placeButtons(inactiveWeaponButtons, (int) realPos.y - 320);
    }

    @Override
    protected void onExit() {

    }

    private void placeButtons(ArrayList<AbstractWeaponButton> buttons, int y){
        int i = (int) (realpos.x + Gdx.graphics.getWidth()/2 - 50 - ((buttons.size() -1) * 75));
        for (AbstractWeaponButton button : buttons) {
            button.x = i;
            button.y = y;

            i+= 150;
            button.update();
        }
    }

    private void setButton(AbstractWeaponButton weaponButton){
        if (activeWeaponButtons.size() < MAX_ACTIVE_WEAPONS) {
            if (inactiveWeaponButtons.remove(weaponButton)){
                activeWeaponButtons.add(weaponButton);
                placeButtons(inactiveWeaponButtons, (int) realpos.y - 320);
                placeButtons(activeWeaponButtons, (int) realpos.y - 670);
                return;
            }
        }

        if (activeWeaponButtons.remove(weaponButton)) {
            if (weaponButton.index <= inactiveWeaponButtons.size()) {
                inactiveWeaponButtons.add(weaponButton.index, weaponButton);
            }
            else {
                inactiveWeaponButtons.add(weaponButton);
            }
        }

        placeButtons(inactiveWeaponButtons, (int) (realpos.y - 320));
        placeButtons(activeWeaponButtons, (int) realpos.y - 670);
    }

    @Override
    public void update() {
        int touch = menuHelper.getTouch(hitBoxes);
        switch (touch){
            case 0:
                mainMenuScreen.setCurrentMenuState(MainMenuScreen.MainMenuState.MAIN);
                break;
            case 1:
                if (activeWeaponButtons.size() < MAX_ACTIVE_WEAPONS) {
                    break;
                }

                WeaponHelper.getWeaponHelper().resetPlayerWeapons();
                for (AbstractWeaponButton button : activeWeaponButtons){
                    WeaponHelper.getWeaponHelper().addPlayerWeapon(button.getWeapon());
                }

                AudioHelper.getInstance().stopMainMenuTheme();
                AudioHelper.getInstance().playMainGameTheme();
                GameDriver.getINSTANCE().setGameScreen();
                break;
            case 2: //Default Gun
                setButton(defaultGunButton);
                break;
            case 3: //Finger Gun
                setButton(fingerGunsButton);
                break;
            case 4: //Launcher
                setButton(launcherGunButton);
                break;
            case 5: //sniper
                setButton(sniperGunButton);
                break;
            case 6: //slow
                setButton(slowGunButton);
                break;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        buttonFont.draw(spriteBatch, weaponChoiceText, realpos.x + Gdx.graphics.getWidth()/2 - 230, realpos.y - 150);
        buttonFont.draw(spriteBatch, yourArsenalText, realpos.x + Gdx.graphics.getWidth()/2 - 230, realpos.y- 500);
        buttonFont.draw(spriteBatch, backText, realpos.x + 75, realpos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/6);
        buttonFont.draw(spriteBatch, playText, realpos.x + Gdx.graphics.getWidth() - 175, realpos.y - Gdx.graphics.getHeight() + Gdx.graphics.getHeight()/6);

        // text under buttons
        underButtonFont.draw(spriteBatch, defaultGunText, defaultGunButton.getX(), defaultGunButton.getY() - 10, defaultGunButton.getWidth(), Align.center, true);
        underButtonFont.draw(spriteBatch, fingerGunText, fingerGunsButton.getX(), fingerGunsButton.getY() - 10, fingerGunsButton.getWidth(), Align.center, true);
        underButtonFont.draw(spriteBatch, launcherGunText,  launcherGunButton.getX(), launcherGunButton.getY() - 10, launcherGunButton.getWidth(), Align.center, true);
        underButtonFont.draw(spriteBatch, sniperGunText,  sniperGunButton.getX(), sniperGunButton.getY() - 10, sniperGunButton.getWidth(), Align.center, true);
        underButtonFont.draw(spriteBatch, slowGunText,  slowGunButton.getX(), slowGunButton.getY() - 10, slowGunButton.getWidth(), Align.center, true);

        for (AbstractWeaponButton button : inactiveWeaponButtons) {
            button.render(spriteBatch);
        }
        for (AbstractWeaponButton button : activeWeaponButtons) {
            button.render(spriteBatch);
        }

    }

    public void setNumOfChosenWeapons(int numOfWeapons) {
        MAX_ACTIVE_WEAPONS += numOfWeapons;
    }
}
