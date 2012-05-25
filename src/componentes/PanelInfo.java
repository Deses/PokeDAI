package componentes;

import it.marteEngine.ResourceManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.newdawn.slick.Image;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public final class PanelInfo {

    private static PanelInfo infoDisplay = new PanelInfo();
    private static final int INFO_DELAY = 20;
    private final Image background;
    private final Timer infoTimer;
    private String showInfo = "";
    private String info = "";
    private int counter;
    private boolean ready = true;

    /**
     * Patrón Singleton.
     * @return Instancia de la clase.
     */
    public static PanelInfo getInstance() {
        return infoDisplay;
    }

    // Constructores //
    /**
     * Constructor de PanelInfo.
     */
    private PanelInfo() {
        background = ResourceManager.getImage("textarea");
        infoTimer = new Timer(INFO_DELAY, new InfoTimerListener());
    }

    // Utilidades //
    /**
     * Método que escribe en el area de texto un mensaje letra a letra.
     *
     * @param informacion String - El texto que quieres escribir.
     */
    public void writeInfo(final String informacion) {
        //Regula la velocidad a la que aprecen los mensajes por pantalla
        ready = false;
        this.background.setAlpha(1);
        info = informacion;
        showInfo = "";
        counter = 0;
        infoTimer.start();
    }

    // Timers //
    private class InfoTimerListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {

            if (info.toCharArray().length > counter) {
                showInfo = showInfo + info.toCharArray()[counter];
                counter++;
            } else {
                infoTimer.stop();
                ready = true;
            }
        }
    }

    // Getters & Setters //
    public boolean isReady() {
        return ready;
    }

    public void setReady(final boolean ready) {
        this.ready = ready;
    }

    public void setShowInfo(final String showInfo) {
        this.showInfo = showInfo;
    }

    public Image getBackground() {
        return background;
    }

    public String getShowInfo() {
        return showInfo;
    }
}
