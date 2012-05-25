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
public class Pausa extends World {

    private StateBasedGame stateBasedGame;
    private Image fondo;
    private MouseOverArea[] botones = new MouseOverArea[3];

    // Constructores //
    /**
     * Constructor del menú de Pausa.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public Pausa(final int id, final GameContainer container) {
        super(id, container);
    }

    // Overrides //
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    @Override
    public final void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);

        //Botón de reanudar el juego
        final Image reanudar = ResourceManager.getImage("reanudar");
        final MouseOverArea reanudarButton = new MouseOverArea(container, reanudar,
                (container.getWidth() / 2) - (reanudar.getWidth() / 2), 260, reanudar.getWidth(), reanudar.getHeight(),
                new ReanudarButtonListener());
        reanudarButton.setMouseOverImage(ResourceManager.getImage("reanudar2"));

        //Botón de salir al manu principal
        final Image opciones = ResourceManager.getImage("opciones");
        final MouseOverArea opcionesButton = new MouseOverArea(container, opciones,
                (container.getWidth() / 2) - (opciones.getWidth() / 2), 370, opciones.getWidth(), opciones.getHeight(),
                new OpcionesButtonListener());
        opcionesButton.setMouseOverImage(ResourceManager.getImage("opciones2"));

        //Botón de salir al manu principal
        final Image salir = ResourceManager.getImage("salir");
        final MouseOverArea salirButton = new MouseOverArea(container, salir,
                (container.getWidth() / 2) - (salir.getWidth() / 2), 470, salir.getWidth(), salir.getHeight(),
                new SalirButtonListener());
        salirButton.setMouseOverImage(ResourceManager.getImage("salir2"));

        botones[0] = reanudarButton;
        botones[1] = opcionesButton;
        botones[2] = salirButton;
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Fondo
        fondo = DataManager.getFondoPausa();
        fondo.setAlpha(.2f);

        //Variables de control
        stateBasedGame = game;
    }

    /**
     * @see org.newdawn.slick.state.GameState#render
     */
    @Override
    public final void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
        super.render(container, game, g);

        g.drawImage(fondo, 0, 0);

        for (MouseOverArea mouseOverArea : botones) {
            mouseOverArea.render(container, g);
        }
    }

    /**
     * @see org.newdawn.slick.state.GameState#update
     */
    @Override
    public final void update(final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
        super.update(container, game, delta);
    }

    // Listeners //
    private class ReanudarButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            if (DataManager.getLastState() == 2) {
                stateBasedGame.enterState(2);
            } else if (DataManager.getLastState() == 4) {
                stateBasedGame.enterState(4);
            }
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
            stateBasedGame.enterState(0, new FadeOutTransition(), new FadeInTransition());
        }
    }
}
