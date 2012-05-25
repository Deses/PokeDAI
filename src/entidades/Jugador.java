package entidades;

import componentes.DataManager;
import estados.Aula;
import estados.Pasillo;
import it.marteEngine.ME;
import it.marteEngine.actor.TopDownActor;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class Jugador extends TopDownActor {

    private static final int TAMANO_CASILLA = 24;
    private final String[] colisionables = {Bloque.PARED, Bloque.PUERTA, Bloque.NPC};
    private boolean colliding = false;

    // Constructores //
    /**
     * Constructor del personaje protagonista.
     *
     * @param x float - Posición horizontal inicial en el mundo.
     * @param y float - Posición vertical inicial en el mundo.
     * @param imagen String - Ruta de la imagen.
     * @param width int - Tamaño horizontal del sprite.
     * @param height int - Tamaño vertical del sprite.
     */
    public Jugador(final float x, final float y, final String imagen, final int width, final int height) {
        super(x * TAMANO_CASILLA, y * TAMANO_CASILLA, imagen);
        this.width = width;
        this.height = height;
        this.depth = 1;
        this.mySpeed = new Vector2f(4f, 4f);

        setHitBox(0, 37, this.width, this.height - 37);
        addType(PLAYER);
    }

    // Overrides //
    /**
     * @see it.marteEngine.actor.TopDownActor#setupAnimations
     */
    @Override
    public void setupAnimations(final String ref) {
        try {
            setGraphic(new SpriteSheet(ref, 42, 57));
            duration = 250;
            addAnimation(STAND_DOWN, false, 0, 0);
            addAnimation(STAND_UP, false, 0, 6);
            addAnimation(STAND_RIGHT, false, 0, 3);
            addAnimation(STAND_LEFT, false, 0, 9);
            addAnimation(ME.WALK_DOWN, true, 0, 1, 0, 2, 0);
            addAnimation(ME.WALK_RIGHT, true, 0, 4, 3, 5, 3);
            addAnimation(ME.WALK_UP, true, 0, 7, 6, 8, 6);
            addAnimation(ME.WALK_LEFT, true, 0, 10, 9, 10, 9);
        } catch (SlickException ex) {
        }
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#moveLeft
     */
    @Override
    public void moveLeft() {
        if (active && collide(colisionables, x - mySpeed.x, y) == null) {
            x -= mySpeed.x;
            colliding = false;
        }
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#moveRight
     */
    @Override
    public void moveRight() {
        if (active && collide(colisionables, x + mySpeed.x, y) == null) {
            x += mySpeed.x;
            colliding = false;
        }
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#moveDown
     */
    @Override
    public void moveDown() {
        if (active && collide(colisionables, x, y + mySpeed.y) == null) {
            y += mySpeed.y;
            colliding = false;
        }
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#moveUp
     */
    @Override
    public void moveUp() {
        if (active && collide(colisionables, x, y - mySpeed.y) == null) {
            y -= mySpeed.y;
            colliding = false;
        }
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#update
     */
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
        super.update(container, delta);
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#render
     */
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        super.render(container, g);
    }

    /**
     * @see it.marteEngine.actor.TopDownActor#collisionResponse
     */
    @Override
    public final void collisionResponse(final Entity other) {
        //Comprobamos con qué instancia o tipo de entidad estamos en contacto.
        if (other instanceof NPC) {
            final NPC npc = (NPC) other;
            colliding = true;
            if (world.getID() == 4) {
                Pasillo.setNpcText(npc.getFrase());
            } else if (world.getID() == 2) {
                Aula.setNpcText(npc.getFrase());
            }

        }
        if (other.isType(Bloque.PUERTA)) {
            if (world.getID() == 4) {
                Pasillo.entraClase();
            } else if (world.getID() == 2) {
                Aula.entraPasillo();
            }
        }
    }

    // Getters & Setters //
    public boolean isColliding() {
        return colliding;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
