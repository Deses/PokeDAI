package main;

import estados.*;
import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import java.io.IOException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class PokeDAI extends StateBasedGame {

    private static boolean ressourcesInited;

    // Constructores //
    /**
     * Constructor del juego.
     * @param titulo String - El título del juego.
     */
    public PokeDAI(final String titulo) {
        super(titulo);
    }

    // Overrides //
    @Override
    public void initStatesList(final GameContainer container) throws SlickException {
        initResources();
        container.setMouseCursor(ResourceManager.getImage("cursor"), 0, 40);

        // Añadimos los estados que se usarán en el juego
        addState(new MenuPrincipal(0, container));
        addState(new Opciones(1, container));
        addState(new Pasillo(4, container));
        addState(new Combate(5, container));
        addState(new Aula(2, container));
        addState(new Pausa(3, container));

        enterState(0);
    }

    // Utilidades //
    private static void initResources() throws SlickException {
        if (ressourcesInited) {
            return;
        }
        try {
            ResourceManager.loadResources("res/resources.xml");
        } catch (IOException e) {
            Log.error("failed to load ressource file 'data/resources.xml': " + e.getMessage());
            throw new SlickException("Resource loading failed!");
        }

        ressourcesInited = true;
    }

    public static void main(String[] args) throws SlickException {
        try {
            ME.keyToggleDebug = Input.KEY_1;
            final AppGameContainer app = new AppGameContainer(new PokeDAI("PokéDAI"));
            app.setVSync(true);
            app.setIcon("res/img/icon.tga");
            app.setDisplayMode(1024, 768, false);
            app.start();
        } catch (SlickException e) {
        }
    }
}
