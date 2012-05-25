package componentes;

import org.newdawn.slick.Image;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class DataManager {

    private static String rival = "quim";
    private static boolean fullscreen = false;
    private static boolean sonido = true;
    private static Image fondoPausa;
    private static int lastState = 0;

    // Getters & Setters //
    public static int getLastState() {
        return lastState;
    }

    public static void setLastState(final int lastState) {
        DataManager.lastState = lastState;
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void setFullscreen(final boolean fullscreen) {
        DataManager.fullscreen = fullscreen;
    }

    public static boolean isSonido() {
        return sonido;
    }

    public static void setSonido(final boolean sonido) {
        DataManager.sonido = sonido;
    }

    public static String getRival() {
        return rival;
    }

    public static void setRival(final String rival) {
        DataManager.rival = rival;
    }

    public static Image getFondoPausa() {
        return fondoPausa;
    }

    public static void setFondoPausa(final Image fondoPausa) {
        DataManager.fondoPausa = fondoPausa;
    }
}