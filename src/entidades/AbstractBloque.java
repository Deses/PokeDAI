package entidades;

import it.marteEngine.entity.Entity;
import org.newdawn.slick.Image;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public abstract class AbstractBloque extends Entity {

    private static final int TAMANO_CASILLA = 24;
    public static final String PARED = "pared";
    public static final String CAPA1 = "capa1";
    public static final String CAPA2 = "capa2";
    public static final String PUERTA = "puerta";
    public static final String TRIGGERH = "triggerh";
    public static final String TRIGGERV = "triggerv";
    public static final String NPC = "npc";

    // Constructores //
    /**
     * Constructor abstracto para los bloques sin imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param colisionable boolean - Si la entidad será sólida o no.
     */
    public AbstractBloque(final float x, final float y, final String tipo, final boolean colisionable) {
        super(x * TAMANO_CASILLA, y * TAMANO_CASILLA);

        this.name = tipo;
        addType(tipo);

        if (tipo.equalsIgnoreCase(PARED) || tipo.equalsIgnoreCase(PUERTA)) {
            this.depth = -1;
        } else if (tipo.equalsIgnoreCase(CAPA1) || tipo.equalsIgnoreCase(TRIGGERH) || tipo.equalsIgnoreCase(TRIGGERV)) {
            this.depth = 0;
        } else if (tipo.equalsIgnoreCase(CAPA2)) {
            this.depth = 2;
        }

        if (colisionable) {
            setHitBox(0, 0, TAMANO_CASILLA - 1, TAMANO_CASILLA - 1);
        }
    }

    /**
     * Constructor abstracto para los bloques con imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param colisionable boolean - Si la entidad será sólida o no.
     * @param imagen Imagen - Imagen del bloque.
     */
    public AbstractBloque(final float x, final float y, final String tipo, final boolean colisionable, final Image imagen) {
        super(x * TAMANO_CASILLA, y * TAMANO_CASILLA);

        setGraphic(imagen);

        addType(tipo);

        if (tipo.equalsIgnoreCase(PARED) || tipo.equalsIgnoreCase(PUERTA)) {
            depth = -1;
        } else if (tipo.equalsIgnoreCase(CAPA1)) {
            depth = 0;
        } else if (tipo.equalsIgnoreCase(CAPA2)) {
            depth = 2;
        }

        if (colisionable) {
            setHitBox(0, 0, TAMANO_CASILLA - 1, TAMANO_CASILLA - 1);
        }
    }

    /**
     * Constructor abstracto para los bloques sin imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param hitboxX int - Tamaño horizontal del hitbox.
     * @param hitboxY int - Tamaño vertical del hitbox.
     */
    public AbstractBloque(final float x, final float y, final String tipo, final int hitboxX, final int hitboxY) {
        super(x * TAMANO_CASILLA, y * TAMANO_CASILLA);

        this.name = tipo;
        addType(tipo);

        if (tipo.equalsIgnoreCase(PARED) || tipo.equalsIgnoreCase(PUERTA)) {
            this.depth = -1;
        } else if (tipo.equalsIgnoreCase(CAPA1) || tipo.equalsIgnoreCase(TRIGGERH) || tipo.equalsIgnoreCase(TRIGGERV)) {
            this.depth = 0;
        } else if (tipo.equalsIgnoreCase(CAPA2)) {
            this.depth = 2;
        }

        setHitBox(0, 0, hitboxX, hitboxY);
    }
}
