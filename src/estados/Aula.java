package estados;

import componentes.DataManager;
import componentes.Nivel;
import componentes.PanelInfo;
import entidades.Bloque;
import entidades.Jugador;
import entidades.NPC;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class Aula extends World {

    private static final int ANCHO_MAPA = 42;
    private static final int ALTO_MAPA = 31;
    private static final int TAMANO_CASILLA = 24;
    private static final String DIR_ABA = "abajo";
    private static final String DIR_ARR = "arriba";
    private static final String DIR_DER = "derecha";
    private static final String DIR_IZQ = "izquierda";
    private final Nivel nivel;
    private final List<Entity> arrayNPC = new ArrayList<Entity>();
    private Jugador player;
    private final List<Animation> animations;
    private PanelInfo actionArea;
    private static StateBasedGame stateBasedGame;
    private Graphics graphics;
    private static String npcText;
    private boolean gameReady = false; //Controla si el movimiento del personaje esta activo
    private Image fondoPausa;
    private Music musicaClase;

    // Constructores //
    /**
     * Constructor de Aula.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public Aula(final int id, final GameContainer container) {
        super(id, container);
        //Creamos el mapa y generamos el nivel
        final TiledMap tiledMap = ResourceManager.getMap("claseTiled");
        nivel = new Nivel(tiledMap);

        //Generamos las animaciones que hemos definido en el Tiled Map.
        nivel.findAnimations();
        animations = nivel.getAnimations();

        //Asignamos el tamaño al mundo
        setWidth(ANCHO_MAPA * TAMANO_CASILLA);
        setHeight(ALTO_MAPA * TAMANO_CASILLA);
    }

    // Overrides //
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    @Override
    public final void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);

        //Fondo para el menú de pausa
        fondoPausa = new Image(1024, 768);

        //Creamos los personajes
        crearNPC();

        //Creamos el área donde van los textos
        actionArea = PanelInfo.getInstance();
        actionArea.getBackground().setAlpha(0);

        //Creamos la música
        musicaClase = ResourceManager.getMusic("clase");
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Borramos todo lo que había y volvemos a aañadir el mapa, al jugador y a los NPC al mundo
        clear();
        addAll(nivel.getEntities(), World.GAME);
        addAll(arrayNPC, World.GAME);
        add(player, World.GAME);

        //Acciones a tomar dependiendo del estado anterior.
        if (DataManager.getLastState() == 4) {
            musicaClase.loop();
        }

        //Variables de control
        stateBasedGame = game;
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
    public final void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
        super.render(container, game, g);

        //Variables de control
        graphics = g;

        //Ponemos la fuente y el fondo
        g.setFont(ResourceManager.getFont("pokeGB"));
        g.setBackground(Color.black);

        //Creamos el area de texto, y escribimos de color negro
        g.drawImage(actionArea.getBackground(), -5, 615);
        g.setColor(Color.black);
        g.drawString(actionArea.getShowInfo(), 60, 675);

        //Dibujamos las animaciones del mapa
        for (int i = 0; i < animations.size(); i++) {
            if (nivel.getAnimationLayer().get(i) == 3) {
                animations.get(i).draw(nivel.getAnimationXPositions().get(i), nivel.getAnimationYPositions().get(i));
            }
        }
    }

    /**
     * @see org.newdawn.slick.state.GameState#update
     */
    @Override
    public final void update(final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
        super.update(container, game, delta);
    }

    /**
     * @see org.newdawn.slick.InputListener#keyPressed
     */
    @Override
    public final void keyPressed(final int key, final char c) {

        //Si pulsas escape se abre el menu de pausa
        if (key == Input.KEY_ESCAPE) {
            graphics.copyArea(fondoPausa, 0, 0);
            DataManager.setFondoPausa(fondoPausa);
            stateBasedGame.enterState(3);
        }
        //Cuando las condiciones se cumplen, muestra/ocula el cuadro de texto, escribe el texto y activa/desactiva el movimiento del personaje
        if (key == Input.KEY_SPACE && actionArea.isReady() && player.isColliding()) {
            if (!gameReady) {
                miraAlJugador();
                muestraDialogo(npcText);
                gameReady = true;
            } else {
                escondeDialogo();
                gameReady = false;
            }
        }
    }

    // Utilidades //
    /**
     * Método que muestra el cuadro de diálogo.
     */
    private void muestraDialogo(final String texto) {
        player.setActive(false);
        actionArea.getBackground().setAlpha(1);
        actionArea.writeInfo(texto);
    }

    /**
     * Método que esconde el cuadro de diálogo.
     */
    private void escondeDialogo() {
        player.setActive(true);
        actionArea.setShowInfo("");
        actionArea.getBackground().setAlpha(0);
    }

    /**
     * Método que hace que los NPC miren al personaje cuando les hable.
     */
    private void miraAlJugador() {
        if (player.collide(Bloque.NPC, player.x + player.mySpeed.x, player.y) != null) {
            ((NPC) player.collide(Bloque.NPC, player.x + player.mySpeed.x, player.y)).setAnim(NPC.STAND_LEFT);
        } else if (player.collide(Bloque.NPC, player.x - player.mySpeed.x, player.y) != null) {
            ((NPC) player.collide(Bloque.NPC, player.x - player.mySpeed.x, player.y)).setAnim(NPC.STAND_RIGHT);
        } else if (player.collide(Bloque.NPC, player.x, player.y + player.mySpeed.y) != null) {
            ((NPC) player.collide(Bloque.NPC, player.x, player.y + player.mySpeed.y)).setAnim(NPC.STAND_UP);
        } else if (player.collide(Bloque.NPC, player.x, player.y - player.mySpeed.y) != null) {
            ((NPC) player.collide(Bloque.NPC, player.x, player.y - player.mySpeed.y)).setAnim(NPC.STAND_DOWN);
        }
    }

    /**
     * Método que cambia al estado de Pasillo.
     */
    public static void entraPasillo() {
        stateBasedGame.enterState(4, new FadeOutTransition(), new EmptyTransition());
    }

    /**
     * Método para crear y añadir los NPC
     */
    private void crearNPC() {
        player = new Jugador(20, 5, ResourceManager.getParameter("SpriteProtagonista"), 42, 57);
        NPC julio = new NPC(17, 8, ResourceManager.getParameter("SpriteNPC_Profe_Julio"), 42, 57, DIR_DER, "julio", "julio_c");
        NPC monica = new NPC(17, 11, ResourceManager.getParameter("SpriteNPC_Profe_Monica"), 42, 57, DIR_DER, "monica", "monica_c");
        NPC quim = new NPC(23, 8, ResourceManager.getParameter("SpriteNPC_Profe_Quim"), 42, 57, DIR_IZQ, "quim", "quim_c");
        NPC nacho = new NPC(23, 11, ResourceManager.getParameter("SpriteNPC_Profe_Nacho"), 42, 57, DIR_IZQ, "nacho", "nacho_c");
        NPC npc1 = new NPC(13, 11, ResourceManager.getParameter("SpriteNPC_mas11"), 45, 57, DIR_ARR, "npc1", "npc1_c");
        NPC npc2 = new NPC(27, 11, ResourceManager.getParameter("SpriteNPC_fem05"), 42, 60, DIR_IZQ, "npc2", "npc2_c");
        NPC npc3 = new NPC(31, 11, ResourceManager.getParameter("SpriteNPC_mas08"), 42, 60, DIR_IZQ, "npc3", "npc3_c");
        NPC npc4 = new NPC(5, 17, ResourceManager.getParameter("SpriteNPC_mas07"), 42, 60, DIR_ABA, "npc4", "npc4_c");
        NPC npc5 = new NPC(9, 17, ResourceManager.getParameter("SpriteNPC_mas02"), 48, 60, DIR_DER, "npc5", "npc5_c");
        NPC npc6 = new NPC(13, 17, ResourceManager.getParameter("SpriteNPC_mas04"), 42, 57, DIR_DER, "npc6", "npc6_c");
        NPC npc7 = new NPC(27, 17, ResourceManager.getParameter("SpriteNPC_mas05"), 42, 60, DIR_IZQ, "npc7", "npc7_c");
        NPC npc8 = new NPC(31, 17, ResourceManager.getParameter("SpriteNPC_mas10"), 42, 60, DIR_ABA, "npc8", "npc8_c");
        NPC npc9 = new NPC(35, 17, ResourceManager.getParameter("SpriteNPC_mas09"), 48, 60, DIR_ABA, "npc9", "npc9_c");
        NPC npc10 = new NPC(5, 23, ResourceManager.getParameter("SpriteNPC_mas01"), 45, 57, DIR_ABA, "npc10", "npc10_c");
        NPC npc11 = new NPC(9, 23, ResourceManager.getParameter("SpriteNPC_mas14"), 42, 60, DIR_ABA, "npc11", "npc11_c");
        NPC npc12 = new NPC(13, 23, ResourceManager.getParameter("SpriteNPC_mas13"), 42, 60, DIR_ABA, "npc12", "npc12_c");
        NPC npc13 = new NPC(27, 23, ResourceManager.getParameter("SpriteNPC_mas12"), 42, 60, DIR_IZQ, "npc13", "npc13_c");

        arrayNPC.add(npc1);
        arrayNPC.add(npc2);
        arrayNPC.add(npc3);
        arrayNPC.add(npc4);
        arrayNPC.add(npc5);
        arrayNPC.add(npc6);
        arrayNPC.add(npc7);
        arrayNPC.add(npc8);
        arrayNPC.add(npc9);
        arrayNPC.add(npc10);
        arrayNPC.add(npc11);
        arrayNPC.add(npc12);
        arrayNPC.add(npc13);
        arrayNPC.add(julio);
        arrayNPC.add(monica);
        arrayNPC.add(quim);
        arrayNPC.add(nacho);
    }

    // Getters & Setters //
    public static void setNpcText(final String text) {
        npcText = text;
    }
}
