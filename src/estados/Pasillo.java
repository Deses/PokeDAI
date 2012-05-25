package estados;

import componentes.DataManager;
import componentes.Nivel;
import componentes.PanelInfo;
import entidades.Bloque;
import entidades.Jugador;
import entidades.NPC;
import it.marteEngine.Camera;
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
public class Pasillo extends World {

    private static final int ANCHO_MAPA = 128;
    private static final int ALTO_MAPA = 98;
    private static final int TAMANO_CASILLA = 24;
    private static final String DIR_ABA = "abajo";
    private static final String DIR_ARR = "arriba";
    private static final String DIR_DER = "derecha";
    private static final String DIR_IZQ = "izquierda";
    private final Nivel nivel;
    private static Camera camara;
    private static Jugador player;
    private static NPC julio1;
    private static NPC julio2;
    private static NPC monica;
    private static NPC nacho;
    private static NPC quim;
    private PanelInfo actionArea;
    private static StateBasedGame stateBasedGame;
    private Graphics graphics;
    private static String npcText;
    private boolean gameReady = false; //Controla si el movimiento del personaje esta activo.
    private boolean gameReady2 = true; //Controla si el movimiento del personaje esta activo.
    private boolean profeReto = false; //Controla si el profesor te ha retado a una batalla.
    private Music musicaPasillo;
    private Image fondoPausa;
    private final List<Entity> arrayNPC = new ArrayList<Entity>();
    private final List<Entity> arrayProfes = new ArrayList<Entity>();

    // Constructores //
    /**
     * Constructor del Pasillo.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public Pasillo(final int id, final GameContainer container) {
        super(id, container);
        //Creamos el mapa y generamos el nivel
        TiledMap tiledMap = ResourceManager.getMap("pasilloTiled");
        nivel = new Nivel(tiledMap);

        //Asignamos el tamaño al mundo
        setWidth(ANCHO_MAPA * TAMANO_CASILLA);
        setHeight(ALTO_MAPA * TAMANO_CASILLA);
    }

    // Overrides //
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    @Override
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);

        //Fondo para el menú de pausa
        fondoPausa = new Image(1024, 768);

        //Creamos los personajes
        crearNPC();

        //Creamos el área donde van los textos
        actionArea = PanelInfo.getInstance();
        actionArea.getBackground().setAlpha(0);

        //Creamos la música
        musicaPasillo = ResourceManager.getMusic("pasillo");

        //Creamos la cámara que seguira al jugador y le cambiamos la posición inicial
        camara = new Camera(this, player, container.getWidth(), container.getHeight(), 712, 712, player.mySpeed);
        setCamera(camara);
        camara.cameraX = 120 * TAMANO_CASILLA;
        camara.cameraY = 92 * TAMANO_CASILLA;
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Borramos todo lo que había y volvemos a añadir el mapa, al jugador y a los NPC al mundo
        this.clear();
        this.addAll(nivel.getEntities(), World.GAME);
        this.addAll(arrayNPC, World.GAME);
        this.addAll(arrayProfes, World.GAME);
        this.add(player, World.GAME);

        //Acciones a tomar dependiendo del estado anterior.
        if (DataManager.getLastState() == 0) {
            musicaPasillo.loop();
            player.x = 120 * TAMANO_CASILLA;
            player.y = 92 * TAMANO_CASILLA;
            camara.cameraX = 120 * TAMANO_CASILLA;
            camara.cameraY = 92 * TAMANO_CASILLA;
            for (Entity npc : arrayProfes) {
                npc.visible = true;
            }
        } else if (DataManager.getLastState() == 5) {
            musicaPasillo.loop();
            player.setActive(true);
            profeReto = false;
            gameReady2 = true;
        } else if (DataManager.getLastState() == 2) {
            musicaPasillo.loop();
        }

        //Variables de control
        stateBasedGame = game;
        DataManager.setLastState(4);
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
     * @see org.newdawn.slick.state.GameState#update
     */
    @Override
    public final void update(final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
        super.update(container, game, delta);

        //Generamos alrededor de cada profesor un área donde se el profesor se activará si entramos
        for (Entity npc : arrayProfes) {
            if (npc.getDistance(player) <= 7 * 24 && npc.visible) {
                retoCombate(npc);
            }
        }
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
        if (key == Input.KEY_SPACE && actionArea.isReady() && profeReto) {
            escondeDialogo();
            stateBasedGame.enterState(5, new FadeOutTransition(), new EmptyTransition());
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
     * Método que borra al profesor del mapa en caso de victoria.
     *
     * @param profesorName String - El nombre del profesor.
     */
    public static void borraNPC(final String profesorName) {
        switch (profesorName) {
            case "quim":
                quim.destroy();
                quim.clearTypes();
                break;
            case "monica":
                monica.destroy();
                monica.clearTypes();
                break;
            case "julio1":
                julio1.destroy();
                julio1.clearTypes();
                break;
            case "julio2":
                julio2.destroy();
                julio2.clearTypes();
                break;
            case "nacho":
                nacho.destroy();
                nacho.clearTypes();
                break;
        }
    }

    /**
     * Método que cambia al estado de Aula.
     */
    public static void entraClase() {
        stateBasedGame.enterState(2, new FadeOutTransition(), new EmptyTransition());
    }

    /**
     * Método que cambia al estado de combate dependiendo del profesor con quién
     * hables.
     *
     * @param profesor NPC - El profesor que te ha retado.
     */
    private void retoCombate(final Entity param) {
        NPC profesor = (NPC) param;
        DataManager.setRival(profesor.name);
        profeReto = true;
        if (gameReady2) {
            ResourceManager.getMusic("reto").loop();
            setNpcText(profesor.getFrase());
            muestraDialogo(npcText);
            gameReady2 = false;
        }
    }

    /**
     * Método que en caso de derrota ante un profesor, te devuelve unos pasos
     * hacia atrás.
     *
     * @param profesorName String - El nombre del profesor.
     */
    public static void retrocedeJugador(final String profesorName) {
        if (profesorName.equalsIgnoreCase("monica") || profesorName.equalsIgnoreCase("julio1") || profesorName.equalsIgnoreCase("nacho")) {
            player.y += 3 * TAMANO_CASILLA;
            camara.cameraY += 3 * TAMANO_CASILLA;
        } else {
            player.x += 3 * TAMANO_CASILLA;
            camara.cameraX += 3 * TAMANO_CASILLA;
        }
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
     * Método para crear y añadir los NPC
     */
    private void crearNPC() {
        player = new Jugador(120, 92, ResourceManager.getParameter("SpriteProtagonista"), 42, 57);
        NPC npc1 = new NPC(124, 80, ResourceManager.getParameter("SpriteNPC_mas01"), 45, 57, DIR_ABA, "npc1", "npc1_p");
        NPC npc2 = new NPC(116, 48, ResourceManager.getParameter("SpriteNPC_mas02"), 48, 60, DIR_DER, "npc2", "npc2_p");
        NPC npc3 = new NPC(115, 54, ResourceManager.getParameter("SpriteNPC_fem01"), 48, 57, DIR_IZQ, "npc3", "npc3_p");
        NPC npc4 = new NPC(115, 76, ResourceManager.getParameter("SpriteNPC_fem02"), 42, 60, DIR_DER, "npc4", "npc4_p");
        NPC npc5 = new NPC(115, 42, ResourceManager.getParameter("SpriteNPC_mas06"), 48, 60, DIR_DER, "npc5", "npc5_p");
        NPC npc6 = new NPC(125, 20, ResourceManager.getParameter("SpriteNPC_mas07"), 42, 60, DIR_IZQ, "npc6", "npc6_p");
        NPC npc7 = new NPC(106, 6, ResourceManager.getParameter("SpriteNPC_mas08"), 42, 60, DIR_IZQ, "npc7", "npc7_p");
        NPC npc8 = new NPC(104, 6, ResourceManager.getParameter("SpriteNPC_fem05"), 42, 60, DIR_DER, "npc8", "npc8_p");
        NPC npc9 = new NPC(121, 5, ResourceManager.getParameter("SpriteNPC_mas13"), 42, 60, DIR_ARR, "npc9", "npc9_p");
        NPC npc10 = new NPC(83, 13, ResourceManager.getParameter("SpriteNPC_mas03"), 42, 57, DIR_ARR, "npc10", "npc10_p");
        NPC npc11 = new NPC(47, 4, ResourceManager.getParameter("SpriteNPC_fem04"), 42, 60, DIR_ABA, "npc11", "npc11_p");
        NPC npc12 = new NPC(45, 13, ResourceManager.getParameter("SpriteNPC_mas14"), 42, 60, DIR_ARR, "npc12", "npc12_p");
        NPC npc13 = new NPC(43, 13, ResourceManager.getParameter("SpriteNPC_mas04"), 42, 57, DIR_ARR, "npc13", "npc13_p");
        NPC npc14 = new NPC(41, 13, ResourceManager.getParameter("SpriteNPC_fem03"), 45, 57, DIR_ARR, "npc14", "npc14_p");
        NPC npc15 = new NPC(37, 13, ResourceManager.getParameter("SpriteNPC_mas05"), 42, 60, DIR_IZQ, "npc15", "npc15_p");
        NPC npc16 = new NPC(2, 7, ResourceManager.getParameter("SpriteNPC_mas12"), 42, 60, DIR_IZQ, "npc16", "npc16_p");
        NPC npc17 = new NPC(124, 63, ResourceManager.getParameter("SpriteNPC_mas11"), 45, 57, DIR_IZQ, "npc17", "npc17_p");
        NPC npc18 = new NPC(9, 13, ResourceManager.getParameter("SpriteNPC_mas10"), 42, 60, DIR_ARR, "npc18", "npc18_p");
        NPC npc19 = new NPC(7, 13, ResourceManager.getParameter("SpriteNPC_mas09"), 48, 60, DIR_ARR, "npc19", "npc19_p");
        NPC npc20 = new NPC(57, 7, ResourceManager.getParameter("SpriteNPC_mas15"), 42, 60, DIR_ABA, "npc20", "npc20_p");
        julio1 = new NPC(120, 67, ResourceManager.getParameter("SpriteNPC_Profe_Julio"), 42, 57, DIR_ABA, "julio1", "julio1_p");
        monica = new NPC(120, 32, ResourceManager.getParameter("SpriteNPC_Profe_Monica"), 42, 57, DIR_ABA, "monica", "monica_p");
        nacho = new NPC(120, 10, ResourceManager.getParameter("SpriteNPC_Profe_Nacho"), 42, 57, DIR_ABA, "nacho", "nacho_p");
        quim = new NPC(71, 8, ResourceManager.getParameter("SpriteNPC_Profe_Quim"), 42, 57, DIR_DER, "quim", "quim_p");
        julio2 = new NPC(38, 8, ResourceManager.getParameter("SpriteNPC_Profe_Julio"), 42, 57, DIR_DER, "julio2", "julio2_p");

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
        arrayNPC.add(npc14);
        arrayNPC.add(npc15);
        arrayNPC.add(npc16);
        arrayNPC.add(npc17);
        arrayNPC.add(npc18);
        arrayNPC.add(npc19);
        arrayNPC.add(npc20);
        arrayProfes.add(julio1);
        arrayProfes.add(julio2);
        arrayProfes.add(monica);
        arrayProfes.add(nacho);
        arrayProfes.add(quim);
    }

    // Getters & Setters //
    public static void setNpcText(final String text) {
        npcText = text;
    }
}
