package estados;

import componentes.CombateAlumno;
import componentes.CombateProfesor;
import componentes.DataManager;
import componentes.PanelInfo;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
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
public class Combate extends World {

    private StateBasedGame stateBasedGame;
    private final Color COLOR_RED = new Color(235, 30, 35);
    private final Color COLOR_GREEN = new Color(50, 190, 20);
    private final Color COLOR_YELLOW = new Color(245, 245, 55);
    private MouseOverArea[] botones = new MouseOverArea[3];
    private Image responder;
    private Image estudiar;
    private Image divertirse;
    private boolean turnoAlumno = false;
    private CombateAlumno alumno;
    private Image alumnoHud;
    private Rectangle alumnoPsDisplay;
    private CombateProfesor profesor;
    private String profesorName = "";
    private Image profesorHud;
    private Rectangle profesorPsDisplay;
    private String accionProfesor = "";
    private PanelInfo actionArea;
    private Timer actualizaAlumnoPSTimer;
    private Timer actualizaProfesorPSTimer;
    private static final int PS_DELAY = 50;
    private static final String PREGUNTA = "pregunta";
    private static final String EXAMEN = "examen";
    private static final String PREGUNTA_CORRECTA = "¡Es muy efectivo!";
    private static final String PREGUNTA_INCORRECTA = "No es muy efectivo...";
    private static final String DIVERTISE_EXTRA = "¡DIVERTIRSE hace que tus puntos de salud aumenten!";
    private static final String DIVERTIRSE_FULL = "¡Tus puntos de salud están al máximo!";
    private static final String CONOCIMIENTO_EXTRA = "¡ESTUDIAR hace que tu conocimiento aumente!";
    private static final String CONOCIMIENTO_FULL = "ESTUDIAR no puede aumentar más tu conocimiento...";
    private static final String NADA_RESPONDER = "¡No hay nada que responder!";
    private static final String ALGO_RESPONDER = "¡Tienes algo que responder!";
    private static final int VALOR_PREGUNTA = 20;
    private static final int VALOR_EXAMEN = 45;
    private static final int VALOR_ESTUDIAR = 13;
    private static final int VALOR_BROMEAR = 8;
    private static final int VALOR_DIVERTIRSE = 35;
    private boolean gameReady = true; //Controla cuando el juego esta preparado para recibir input del usuario.
    private boolean continuar = false; //Controla si el usuario quiere pasar al siguiente mensaje.
    private boolean preparandoExamen = false; //Marca cuando el profesor ha empezado a preparar un examen.
    private boolean examenListo = false; //Controla si es el turno adecuado para contestar el examen.
    private boolean puntosDeSaludUp = false; //Controla si debe subir o bajar los puntos de salud.
    private boolean turnoResponder = false; //Controla si es un turno en el que el jugador puede responder.
    private boolean gameOver = false; //Controla si el juego ha terminado.
    private boolean victoria = false; //Controla si hemos ganado.
    private boolean vidaRoja = false; //Controla si la vida está baja.
    private Music musicaBatalla1;
    private Music musicaBatalla2;
    private Music musicaBatalla3;
    private Music musicaVictoria1;
    private Music musicaVictoria2;
    private Music musicaVictoria3;
    private Sound lowPS;
    private Sound conoDOWN;
    private Sound conoUP;
    private Sound hit;
    private Sound recuperaPS;
    private Image fondo;
    private Image fondoGris;
    private Image spriteCombateAlumno;
    private Image spriteCombateProfesor;

    // Constructores //
    /**
     * Constructor del combate.
     *
     * @param id int - Identificador del estado.
     * @param container GameContainer - El Contenedor del juego creado en el
     * main.
     */
    public Combate(final int id, final GameContainer container) {
        super(id, container);
    }

    // Overrides //
    /**
     * @see org.newdawn.slick.state.GameState#init
     */
    @Override
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.init(container, game);

        //Botón responder
        responder = ResourceManager.getImage("responder");
        responder.setAlpha(0);
        final MouseOverArea responderButton = new MouseOverArea(container, responder,
                (container.getWidth() / 2) - (responder.getWidth() / 2) - 300, 665, responder.getWidth(), responder.getHeight(),
                new ResponderButtonListener());
        responderButton.setMouseOverImage(ResourceManager.getImage("responder2"));
        responderButton.setAcceptingInput(false);

        //Botón estudiar
        estudiar = ResourceManager.getImage("estudiar");
        estudiar.setAlpha(0);
        final MouseOverArea estudiarButton = new MouseOverArea(container, estudiar,
                (container.getWidth() / 2) - (estudiar.getWidth() / 2), 665, estudiar.getWidth(), estudiar.getHeight(),
                new EstudiarButtonListener());
        estudiarButton.setMouseOverImage(ResourceManager.getImage("estudiar2"));
        estudiarButton.setAcceptingInput(false);


        //Botón divertirse
        divertirse = ResourceManager.getImage("divertirse");
        divertirse.setAlpha(0);
        final MouseOverArea divertirseButton = new MouseOverArea(container, divertirse,
                (container.getWidth() / 2) - (divertirse.getWidth() / 2) + 300, 665, divertirse.getWidth(), divertirse.getHeight(),
                new DivertirseButtonListener());
        divertirseButton.setMouseOverImage(ResourceManager.getImage("divertirse2"));
        divertirseButton.setAcceptingInput(false);

        botones[0] = responderButton;
        botones[1] = estudiarButton;
        botones[2] = divertirseButton;

        musicaBatalla1 = ResourceManager.getMusic("1batalla");
        musicaBatalla2 = ResourceManager.getMusic("2batalla");
        musicaBatalla3 = ResourceManager.getMusic("3batalla");
        musicaVictoria1 = ResourceManager.getMusic("1victoria");
        musicaVictoria2 = ResourceManager.getMusic("2victoria");
        musicaVictoria3 = ResourceManager.getMusic("3victoria");
        lowPS = ResourceManager.getSound("lowPS");
        conoDOWN = ResourceManager.getSound("conoDOWN");
        conoUP = ResourceManager.getSound("conoUP");
        hit = ResourceManager.getSound("hit");
        recuperaPS = ResourceManager.getSound("recuperaPS");
        fondoGris = ResourceManager.getImage("fondoGris");
    }

    /**
     * @see org.newdawn.slick.state.GameState#enter
     */
    @Override
    public final void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
        super.enter(container, game);

        //Interfaz del profesor
        profesorName = DataManager.getRival();
        profesor = new CombateProfesor(profesorName);
        profesorHud = profesor.getProfesorHud();
        spriteCombateProfesor = profesor.getSpriteCombate();
        profesorPsDisplay = new Rectangle(134, 202, 200, 10);

        //Interfaz del alumno
        alumno = new CombateAlumno();
        alumnoHud = ResourceManager.getImage("alumnoHUD");
        spriteCombateAlumno = ResourceManager.getImage("Combate_alumno");
        alumnoPsDisplay = new Rectangle(734, 502, 200, 10);

        //Zona para las acciones del combate
        actionArea = PanelInfo.getInstance();

        actualizaAlumnoPSTimer = new Timer(PS_DELAY, new ActualizaAlumnoPSTimerListener());
        actualizaProfesorPSTimer = new Timer(PS_DELAY, new ActualizaProfesorPSTimerListener());
        actionArea.writeInfo(profesor.getFraseEntrada());

        //Fondo de pantalla
        fondo = ResourceManager.getImage("combate");

        //Música
        switch (profesorName) {
            case "julio1":
                musicaBatalla1.loop();
                break;
            case "monica":
                musicaBatalla1.loop();
                break;
            case "nacho":
                musicaBatalla2.loop();
                break;
            case "quim":
                musicaBatalla2.loop();
                break;
            case "julio2":
                musicaBatalla3.loop();
                break;
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
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g) {

        //Ponemos la fuente y el fondo
        g.setFont(ResourceManager.getFont("pokeGB"));
        g.drawImage(fondo, 0, 0);

        //Interfaz del profesor
        final int posxHudProfesor = (container.getWidth() / 2) - (profesorHud.getWidth() / 2) - 300;
        final int posyHudProfesor = 100;
        //Barra gris de fondo
        g.drawImage(fondoGris, posxHudProfesor + 84, posyHudProfesor + 92);
        //Puntos de salud del profesor
        if (profesor.getPuntosDeSalud() > 60) {
            g.setColor(COLOR_GREEN);
        } else if (profesor.getPuntosDeSalud() <= 60 && profesor.getPuntosDeSalud() > 20) {
            g.setColor(COLOR_YELLOW);
        } else {
            g.setColor(COLOR_RED);
        }
        g.fill(profesorPsDisplay);
        //Hud del profesor
        g.drawImage(profesorHud, posxHudProfesor, posyHudProfesor);
        //Sprite de combate del profesor
        g.drawImage(spriteCombateProfesor, 645, 46);


        //Interfaz del alumno
        final int posxHudAlumno = (container.getWidth() / 2) - (alumnoHud.getWidth() / 2) + 300;
        final int posyHudAlumno = 400;
        //Barra gris de fondo
        g.drawImage(fondoGris, posxHudAlumno + 84, posyHudAlumno + 92);
        //Puntos de salud del alumno
        if (alumno.getPuntosDeSalud() > 60) {
            g.setColor(COLOR_GREEN);
        } else if (alumno.getPuntosDeSalud() <= 60 && alumno.getPuntosDeSalud() > 20) {
            g.setColor(COLOR_YELLOW);
            vidaRoja = true;
            lowPS.stop();
        } else {
            g.setColor(COLOR_RED);
            if (vidaRoja) {
                lowPS.loop();
                vidaRoja = false;
            }
        }
        g.fill(alumnoPsDisplay);
        //Hud del profesor
        g.drawImage(alumnoHud, posxHudAlumno, posyHudAlumno);
        //Sprite de combate del alumno
        g.drawImage(spriteCombateAlumno, 167, 336);

        //Zona de textos y menu acciones
        g.drawImage(actionArea.getBackground(), -5, 615);
        g.setColor(Color.black);
        g.drawString(actionArea.getShowInfo(), 60, 675);

        //Botones de acción del jugador
        for (MouseOverArea mouseOverArea : botones) {
            mouseOverArea.render(container, g);
        }
    }

    /**
     * @see org.newdawn.slick.state.GameState#update
     */
    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta) {
        if (turnoAlumno && continuar) {
            // Turno del jugador
            actionArea.writeInfo("");
            //Control de los botones de acción disponibles para el jugador
            if (profesor.getAccion().equals(PREGUNTA) || profesor.getAccion().equals(EXAMEN) && examenListo) {
                turnoResponder = true;
            } else {
                turnoResponder = false;
            }
            showButtons();
            continuar = false;
        } else {
            if (actionArea.isReady() && gameReady && continuar) {
                //Turno del profesor 
                if (!accionProfesor.equalsIgnoreCase(EXAMEN) || accionProfesor.equalsIgnoreCase(EXAMEN) && examenListo) {
                    profesor.elegirAccion();
                    preparandoExamen = false;
                    examenListo = false;
                }
                accionProfesor = profesor.getAccion().toUpperCase();

                if (accionProfesor.equalsIgnoreCase(PREGUNTA)) {
                    actionArea.writeInfo("¡" + profesor.getNombre() + " te ha hecho una PREGUNTA!");
                } else if (accionProfesor.equalsIgnoreCase(EXAMEN) && !preparandoExamen) {
                    actionArea.writeInfo(profesor.getNombre() + " prepara un EXAMEN...");
                    preparandoExamen = true;
                } else if (accionProfesor.equalsIgnoreCase(EXAMEN) && preparandoExamen) {
                    actionArea.writeInfo("¡" + profesor.getNombre() + " utiliza EXAMEN! ");
                    examenListo = true;
                } else if (accionProfesor.equalsIgnoreCase("bromear")) {
                    actionArea.writeInfo("BROMEAR de " + profesor.getNombre() + " hace que tu conocimiento disminuya.");
                    conoDOWN.play();
                    actualizaConocimiento(false);
                }
                continuar = false;
                turnoAlumno = true;
            }
        }
    }

    /**
     * @see org.newdawn.slick.InputListener#keyPressed
     */
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == Input.KEY_SPACE && actionArea.isReady() && gameReady && !gameOver && !continuar) {
            continuar = true;
        }

        if (key == Input.KEY_SPACE && gameOver) {
            gameOver = false;
            actionArea.setShowInfo("");
            actionArea.getBackground().setAlpha(0);
            if (victoria) {
                Pasillo.borraNPC(profesorName);
            } else {
                Pasillo.retrocedeJugador(profesorName);
            }
            stateBasedGame.enterState(4, new FadeOutTransition(), new FadeInTransition());
        }
    }

    // Utilidades //
    /**
     * Método que muestra el cuadro de diálogo.
     */
    private void showButtons() {
        responder.setAlpha(1);
        estudiar.setAlpha(1);
        divertirse.setAlpha(1);

        for (MouseOverArea mouseOverArea : botones) {
            mouseOverArea.setAcceptingInput(true);
        }
    }

    /**
     * Método que esconde el cuadro de diálogo.
     */
    private void hideButtons() {
        responder.setAlpha(0);
        estudiar.setAlpha(0);
        divertirse.setAlpha(0);

        for (MouseOverArea mouseOverArea : botones) {
            mouseOverArea.setAcceptingInput(false);
        }
    }

    /**
     * Método que actualiza y controla la vida de los personajes.
     *
     * @param esAlumno boolean - Si es el alumno o no.
     * @param valor int - Indica cuanta vida recuperan o pierden los personajes.
     */
    private void actualizaPuntosDeSalud(final boolean esAlumno, final int valor) {
        //Actualiza la vida de los personajes en el combate
        if (esAlumno) {
            //Controla un exceso de vida por encima de 100
            if (alumno.getPuntosDeSalud() + valor >= 100) {
                alumno.setPuntosDeSalud(100);
            } else if (alumno.getPuntosDeSalud() + valor < 0) {
                alumno.setPuntosDeSalud(0);
            } else {
                alumno.setPuntosDeSalud(alumno.getPuntosDeSalud() + valor);
            }
            //Información a mostrar segun los cambios en los puntos de salud
            if (puntosDeSaludUp) {
                if (alumno.getPuntosDeSalud() != 100) {
                    actionArea.writeInfo(DIVERTISE_EXTRA);
                    recuperaPS.play();
                } else {
                    actionArea.writeInfo(DIVERTIRSE_FULL);
                }
            } else {
                hit.play();
                actionArea.writeInfo(PREGUNTA_INCORRECTA);
            }
            actualizaAlumnoPSTimer.start();
            gameReady = false;

            if (alumno.getPuntosDeSalud() <= 0) {
                gameOver(true);
            }

        } else {
            hit.play();
            profesor.setPuntosSalud(profesor.getPuntosDeSalud() + valor);
            if (profesor.getPuntosDeSalud() < 0) {
                profesor.setPuntosSalud(0);
            }
            actionArea.writeInfo(PREGUNTA_CORRECTA);
            actualizaProfesorPSTimer.start();
            gameReady = false;
            if (profesorName.equals("julio1") && profesor.getPuntosDeSalud() <= 75) {
                gameOver(false);
            } else if (profesor.getPuntosDeSalud() <= 0) {
                gameOver(false);
            }
        }
    }

    /**
     * Método que actualiza y controla el conocimiento de los personajes.
     *
     * @param esAlumno boolean - Si es el alumno o no.
     */
    private void actualizaConocimiento(final boolean esAlumno) {
        if (esAlumno) {
            if (alumno.getConocimiento() + VALOR_ESTUDIAR <= 100) {
                alumno.setConocimiento(alumno.getConocimiento() + VALOR_ESTUDIAR);
                actionArea.writeInfo(CONOCIMIENTO_EXTRA);
                conoUP.play();
            } else {
                alumno.setConocimiento(100);
                actionArea.writeInfo(CONOCIMIENTO_FULL);
            }
        } else {
            alumno.setConocimiento(alumno.getConocimiento() - VALOR_BROMEAR);
            conoDOWN.play();
        }
    }

    /**
     * Método que hace las acciones necesarias al acabar un combate.
     *
     * @param esAlumno boolean - Si es el alumno o no.
     */
    private void gameOver(final boolean esAlumno) {
        if (esAlumno) {
            actionArea.writeInfo(profesor.getFraseDerrota());
            victoria = false;
        } else {
            actionArea.writeInfo(profesor.getFraseVictoria());
            victoria = true;
            switch (profesorName) {
                case "julio1":
                    musicaVictoria1.loop();
                    break;
                case "monica":
                    musicaVictoria1.loop();
                    break;
                case "nacho":
                    musicaVictoria2.loop();
                    break;
                case "quim":
                    musicaVictoria2.loop();
                    break;
                case "julio2":
                    musicaVictoria3.loop();
            }
        }
        lowPS.stop();
        gameOver = true;
    }

    // Timers //
    private class ActualizaAlumnoPSTimerListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!puntosDeSaludUp && alumnoPsDisplay.getWidth() > alumno.getPuntosDeSalud() * 2) {
                alumnoPsDisplay.setWidth(alumnoPsDisplay.getWidth() - 4);
            } else if (puntosDeSaludUp && alumnoPsDisplay.getWidth() < alumno.getPuntosDeSalud() * 2) {
                alumnoPsDisplay.setWidth(alumnoPsDisplay.getWidth() + 4);
            } else {
                actualizaAlumnoPSTimer.stop();
                puntosDeSaludUp = false;
                gameReady = true;
            }
        }
    }

    private class ActualizaProfesorPSTimerListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (profesorPsDisplay.getWidth() > profesor.getPuntosDeSalud() * 2) {
                profesorPsDisplay.setWidth(profesorPsDisplay.getWidth() - 4);
            } else {
                actualizaProfesorPSTimer.stop();
                gameReady = true;
            }
        }
    }

    private class ResponderButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            hideButtons();

            if (!turnoResponder) {
                actionArea.writeInfo(NADA_RESPONDER);
            } else {
                final boolean esCorrecto = alumno.getConocimiento() >= profesor.getDificultad();
                //Si la respuesta es correcta, reduce los puntos de vida del profesor
                if (esCorrecto) {
                    if (profesor.getAccion().equals(PREGUNTA)) {
                        actualizaPuntosDeSalud(false, -VALOR_PREGUNTA);
                    } else {
                        actualizaPuntosDeSalud(false, -VALOR_EXAMEN);
                    }
                } else {
                    //Si la respuesta no es correcta, reduce los puntos de vida del alumno
                    if (profesor.getAccion().equals(PREGUNTA)) {
                        actualizaPuntosDeSalud(true, -VALOR_PREGUNTA);
                    } else {
                        actualizaPuntosDeSalud(true, -VALOR_EXAMEN);
                    }
                }
                turnoAlumno = false;
            }
        }
    }

    private class DivertirseButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            hideButtons();
            if (turnoResponder) {
                actionArea.writeInfo(ALGO_RESPONDER);
            } else {
                puntosDeSaludUp = true;
                actualizaPuntosDeSalud(true, VALOR_DIVERTIRSE);
                turnoAlumno = false;
            }

        }
    }

    private class EstudiarButtonListener implements ComponentListener {

        @Override
        public void componentActivated(final AbstractComponent source) {
            hideButtons();
            actualizaConocimiento(true);
            if (turnoResponder) {
                if (profesor.getAccion().equals(PREGUNTA)) {
                    actualizaPuntosDeSalud(true, -VALOR_PREGUNTA);
                } else {
                    actualizaPuntosDeSalud(true, -VALOR_EXAMEN);
                }
            }
            turnoAlumno = false;
        }
    }
}
