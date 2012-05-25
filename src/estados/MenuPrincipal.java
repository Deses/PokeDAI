/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package estados;

import componentes.DataManager;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class MenuPrincipal extends World {

    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private Animation logo;
    private Image fondo;
    private Image jugar;
    private Image opciones;
    private Image salir;
    private Timer timer;
    private static final int FADE_DELAY = 100;
    private float alphaFactor = 0;
    private boolean gameReady = false;
    private MouseOverArea[] botones = new MouseOverArea[3];
    private Music musicaIntro;

    // Constructores //
    /**
     * Constructor del Menú Principal.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public MenuPrincipal(final int id, final GameContainer container) {
        super(id, container);
    }

    // Overrides //
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    @Override
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);
        gameContainer = container;
        stateBasedGame = game;

        //Fondo
        fondo = ResourceManager.getImage("fondo");

        //Animación del logo
        logo = new Animation(new SpriteSheet(ResourceManager.getImage("logo"), 620, 250), 50);
        logo.setLooping(false);
        timer = new Timer(FADE_DELAY, new FadeTimerListener());

        //Botón jugar
        jugar = ResourceManager.getImage("jugar");
        jugar.setAlpha(0);
        final MouseOverArea jugarButton = new MouseOverArea(container, jugar,
                (container.getWidth() / 2) - (jugar.getWidth() / 2), 260, jugar.getWidth(), jugar.getHeight(),
                new JugarButtonListener());
        jugarButton.setMouseOverImage(ResourceManager.getImage("jugar2"));
        jugarButton.setAcceptingInput(false);


        //Botón opciones
        opciones = ResourceManager.getImage("opciones");
        opciones.setAlpha(0);
        final MouseOverArea opcionesButton = new MouseOverArea(container, opciones,
                (container.getWidth() / 2) - (opciones.getWidth() / 2), 370, opciones.getWidth(), opciones.getHeight(),
                new OpcionesButtonListener());
        opcionesButton.setMouseOverImage(ResourceManager.getImage("opciones2"));
        opcionesButton.setAcceptingInput(false);


        //Botón salir
        salir = ResourceManager.getImage("salir");
        salir.setAlpha(0);
        final MouseOverArea salirButton = new MouseOverArea(container, salir,
                (container.getWidth() / 2) - (salir.getWidth() / 2), 470, salir.getWidth(), salir.getHeight(),
                new SalirButtonListener());
        salirButton.setMouseOverImage(ResourceManager.getImage("salir2"));
        salirButton.setAcceptingInput(false);

        botones[0] = jugarButton;
        botones[1] = opcionesButton;
        botones[2] = salirButton;

        container.setMusicVolume(0.2f);
        musicaIntro = ResourceManager.getMusic("intro");
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Reseteamos algunos valores
        if (DataManager.getLastState() != 1) {
            musicaIntro.loop();
        }
    }

    /**
     * @see org.newdawn.slick.state.GameState#leave
     */
    @Override
    public void leave(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.leave(container, game);

        //Último estado en el que estaba.
        DataManager.setLastState(game.getCurrentStateID());
    }

    /**
     * @see org.newdawn.slick.state.GameState#render
     */
    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
        super.render(container, game, g);

        //Dibujamos el fondo
        g.drawImage(fondo, 0, 0);

        //Dibujamos el logo
        g.drawAnimation(logo, (container.getWidth() / 2) - (logo.getWidth() / 2), 0);

        //Una vez terminada la animación del logo, aparecen los items del menú
        if (logo.isStopped()) {
            timer.start();

            jugar.setAlpha(alphaFactor);
            opciones.setAlpha(alphaFactor - (float) 0.3);
            salir.setAlpha(alphaFactor - (float) 0.6);

            for (MouseOverArea mouseOverArea : botones) {
                mouseOverArea.render(container, g);
                if (gameReady) {
                    mouseOverArea.setAcceptingInput(true);
                }
            }
        }
    }

    /**
     * @see org.newdawn.slick.state.GameState#update
     */
    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
        super.update(container, game, delta);
    }

    // Listeners //
    private class FadeTimerListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (alphaFactor < 2.0) {
                alphaFactor += 0.1;
            } else {
                gameReady = true;
                timer.stop();
            }
        }
    }

    private class JugarButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            stateBasedGame.enterState(4, new FadeOutTransition(), new FadeInTransition());
        }
    }

    private class OpcionesButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            stateBasedGame.enterState(1, new FadeOutTransition(), new FadeInTransition());
        }
    }

    private class SalirButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            gameContainer.exit();
        }
    }
}
