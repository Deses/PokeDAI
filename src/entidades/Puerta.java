package entidades;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class Puerta extends AbstractBloque {

    // Constructores //
    /**
     * Constructor para los bloques que actuan como puerta.
     * @param x float - Posición horizontal en el mapa.
     * @param y float - Posición vertical en el mapa.
     * @param tipo String - Tipo de entidad.
     * @param colisionable boolean - Si la entidad será sólida o no.
     */
    public Puerta(final float x, final float y, final String tipo, final boolean colisionable) {
        super(x, y, tipo, colisionable);
    }
}
