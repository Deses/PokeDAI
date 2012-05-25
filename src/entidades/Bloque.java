package entidades;

import org.newdawn.slick.Image;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class Bloque extends AbstractBloque {

    // Constructores //
    /**
     * Constructor para los bloques sin imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param colisionable boolean - Si la entidad será sólida o no.
     */
    public Bloque(final float x, final float y, final String tipo, final boolean colisionable) {
        super(x, y, tipo, colisionable);
    }

    /**
     * Constructor para los bloques con imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param colisionable boolean - Si la entidad será sólida o no.
     * @param imagen Imagen - Imagen del bloque.
     */
    public Bloque(final float x, final float y, final String tipo, final boolean colisionable, final Image imagen) {
        super(x, y, tipo, colisionable, imagen);
    }

    /**
     * Constructor abstracto para los bloques sin imagen.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param hitboxX int - Tamaño horizontal del hitbox.
     * @param hitboxY int - Tamaño vertical del hitbox.
     */
    public Bloque(final float x, final float y, final String tipo, final int hitboxX, final int hitboxY) {
        super(x, y, tipo, hitboxX, hitboxY);
    }
}
