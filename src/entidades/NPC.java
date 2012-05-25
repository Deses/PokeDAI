package entidades;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class NPC extends Entity {

    private static final int TAMANO_CASILLA = 24;
    public static final String STAND_DOWN = "STAND_DOWN";
    public static final String STAND_UP = "STAND_UP";
    public static final String STAND_RIGHT = "STAND_RIGHT";
    public static final String STAND_LEFT = "STAND_LEFT";
    private final String frase;

    // Constructores //
    /**
     * Constructor de los NPC.
     *
     * @param x float - Posición horizontal inicial en el mundo.
     * @param y float - Posición vertical inicial en el mundo.
     * @param imagen String - Ruta de la imagen.
     * @param width int - Tamaño horizontal del sprite.
     * @param height int - Tamaño vertical del sprite.
     * @param posicion String - Dirección a la que mirará el NPC.
     * @param name String - Nombre del NPC.
     * @param frase String - Frase del NPC.
     */
    public NPC(final float x, final float y, final String imagen, final int width, final int height, final String posicion, final String name, final String frase) {
        super(x * TAMANO_CASILLA, y * TAMANO_CASILLA);
        this.width = width;
        this.height = height;
        this.depth = 0;
        this.name = name;
        this.frase = ResourceManager.getParameter(frase);

        setHitBox(0, 0, width, height);
        
        addType(Bloque.NPC);

        setupAnimations(imagen);

        switch (posicion) {
            case "izquierda":
                this.setAnim(STAND_LEFT);
                break;
            case "derecha":
                this.setAnim(STAND_RIGHT);
                break;
            case "arriba":
                this.setAnim(STAND_UP);
                break;
            default:
                this.setAnim(STAND_DOWN);
                break;
        }
    }

    // Utilidades //
    /**
     * Método que crea las animaciones del personaje.
     *
     * @param ref String - Ruta de la imagen.
     */
    private void setupAnimations(final String ref) {
        try {
            setGraphic(new SpriteSheet(ref, this.width, this.height));
            addAnimation(STAND_DOWN, false, 0, 0);
            addAnimation(STAND_RIGHT, false, 0, 1);
            addAnimation(STAND_UP, false, 0, 2);
            addAnimation(STAND_LEFT, false, 0, 3);
        } catch (SlickException ex) {
        }
    }

    // Getters & Setters //
    public final String getFrase() {
        return frase;
    }
}
