package estados;

import componentes.DataManager;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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
public class Opciones extends World {

    private Image fondo;
    private Image pantallaCompleta;
    private Image pantallaCompletaOff;
    private Image sonido;
    private Image sonidoOff;
    private boolean showControles;
    private MouseOverArea[] botones = new MouseOverArea[4];
    private StateBasedGame stateBasedGame;
    private GameContainer gameContainer;
    private MouseOverArea imgControlesButton;

    // Constructores //
    /**
     * Constructor del menú de Opciones.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public Opciones(final int id, final GameContainer container) {
        super(id, container);
    }

    // Overrides //
    @Override
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);

        //Fondo
        fondo = ResourceManager.getImage("fondo");

        //Botón de sonido
        sonidoOff = ResourceManager.getImage("sonido3");
        sonido = ResourceManager.getImage("sonido");
        final MouseOverArea sonidoButton = new MouseOverArea(container, sonido,
                (container.getWidth() / 2) - (sonido.getWidth() / 2), 50, sonido.getWidth(), sonido.getHeight(),
                new SonidoButtonListener());
        sonidoButton.setMouseOverImage(ResourceManager.getImage("sonido2"));

        //Botón de pantalla completa
        pantallaCompletaOff = ResourceManager.getImage("pantalla_completa3");
        pantallaCompleta = ResourceManager.getImage("pantalla_completa");
        final MouseOverArea pantallaCompletaButton = new MouseOverArea(container, pantallaCompletaOff,
                (container.getWidth() / 2) - (pantallaCompletaOff.getWidth() / 2), 200, pantallaCompletaOff.getWidth(), pantallaCompletaOff.getHeight(),
                new PantallaCompletaButtonListener());
        pantallaCompletaButton.setMouseOverImage(ResourceManager.getImage("pantalla_completa2"));

        //Botón de controles
        Image controles = ResourceManager.getImage("controles");
        final MouseOverArea controlesButton = new MouseOverArea(container, controles,
                (container.getWidth() / 2) - (controles.getWidth() / 2), 350, controles.getWidth(), controles.getHeight(),
                new ControlesButtonListener());
        controlesButton.setMouseOverImage(ResourceManager.getImage("controles2"));

        //Botón atrás
        Image atras = ResourceManager.getImage("atras");
        final MouseOverArea atrasButton = new MouseOverArea(container, atras,
                (container.getWidth() / 2) - (atras.getWidth() / 2), 500, atras.getWidth(), atras.getHeight(),
                new AtrasButtonListener());
        atrasButton.setMouseOverImage(ResourceManager.getImage("atras2"));
        //Boton de la imagen de controles
        Image imgControles = ResourceManager.getImage("imgControles");
        imgControlesButton = new MouseOverArea(container, imgControles,
                (container.getWidth() / 2) - (imgControles.getWidth() / 2), (container.getHeight() / 2) - (imgControles.getHeight() / 2), imgControles.getWidth(), imgControles.getHeight(),
                new ImgControlesButtonListener());

        botones[0] = pantallaCompletaButton;
        botones[1] = sonidoButton;
        botones[2] = controlesButton;
        botones[3] = atrasButton;
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Variables de control
        stateBasedGame = game;
        gameContainer = container;
    }

    /**
     * @see org.newdawn.slick.state.GameState#leave
     */
    @Override
    public void leave(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.leave(container, game);

        //Último estado en el que estaba.
        if (DataManager.getLastState() == 0) {
            DataManager.setLastState(game.getCurrentStateID());
        }

    }

    /**
     * @see org.newdawn.slick.state.GameState#render
     */
    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
        super.render(container, game, g);

        g.drawImage(fondo, 0, 0);

        for (MouseOverArea mouseOverArea : botones) {
            mouseOverArea.render(container, g);
        }

        if (showControles) {
            imgControlesButton.render(container, g);
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
    private class PantallaCompletaButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            try {
                //Si la pantalla completa está activada, la desactivamos, y viceversa.
                if (DataManager.isFullscreen()) {
                    botones[0].setNormalImage(pantallaCompletaOff);
                    DataManager.setFullscreen(false);
                    gameContainer.setFullscreen(false);
                } else {
                    botones[0].setNormalImage(pantallaCompleta);
                    DataManager.setFullscreen(true);
                    gameContainer.setFullscreen(true);
                }
            } catch (SlickException ex) {
            }
        }
    }

    private class SonidoButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            //Si el sonido está activo, lo desactivamos, y viceversa.
            if (DataManager.isSonido()) {
                botones[1].setNormalImage(sonidoOff);
                DataManager.setSonido(false);
                gameContainer.setMusicOn(false);
                gameContainer.setSoundOn(false);
            } else {
                botones[1].setNormalImage(sonido);
                DataManager.setSonido(true);
                gameContainer.setMusicOn(true);
            }
        }
    }

    private class ControlesButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            showControles = true;
        }
    }

    private class ImgControlesButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            showControles = false;
        }
    }

    private class AtrasButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            if (DataManager.getLastState() == 0) {
                stateBasedGame.enterState(0, new FadeOutTransition(), new FadeInTransition());
            } else {
                stateBasedGame.enterState(3, new FadeOutTransition(), new FadeInTransition());
            }
        }
    }
}
