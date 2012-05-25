package componentes;

import it.marteEngine.ResourceManager;
import java.util.Locale;
import java.util.Random;
import org.newdawn.slick.Image;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class CombateProfesor {

    private int puntosDeSalud = 100;
    private final Image profesorHud;
    private final Image spriteCombate;
    private final String nombre;
    private final String fraseEntrada;
    private final String fraseDerrota;
    private final String fraseVictoria;
    private String accion;
    private int dificultad = 0;

    /**
     * Constructor de CombateProfesor.
     *
     * @param text String - El nombre del profesor al que te enfrentas.
     */
    public CombateProfesor(final String text) {
        final String profesorName = text.replaceAll("1|2", "");
        profesorHud = ResourceManager.getImage(profesorName + "HUD");
        spriteCombate = ResourceManager.getImage("Combate_"+profesorName);
        nombre = profesorName.toUpperCase(Locale.getDefault());
        if (text.equals("julio1")) {
            fraseEntrada = ResourceManager.getParameter(nombre + "_FRASE_ENTRADA1");
            fraseVictoria = ResourceManager.getParameter(nombre + "_FRASE_VICTORIA1");
        } else {
            fraseEntrada = ResourceManager.getParameter(nombre + "_FRASE_ENTRADA");
            fraseVictoria = ResourceManager.getParameter(nombre + "_FRASE_VICTORIA");
        }
        fraseDerrota = ResourceManager.getParameter(nombre + "_FRASE_DERROTA");

    }

    // Utilidades //
    /**
     * Método que elige la acción que hará el profesor durante el combate.
     */
    public final void elegirAccion() {
        final int aleatorio = numeroAleatorio();
        final int nivel = numeroAleatorio();
        if (aleatorio > 0 && aleatorio <= 55) {
            accion = "pregunta";
            dificultad = nivel;
        } else if (aleatorio > 55 && aleatorio <= 70) {
            accion = "examen";
            dificultad = nivel;
        } else {
            accion = "bromear";
        }
    }

    /**
     * Método que devuelve un número aleatório.
     *
     * @return int - Un número aleatório entre 0 y 100.
     */
    public final int numeroAleatorio() {
        return new Random().nextInt(100) + 1;
    }

    // Getters & Setters //
    public final int getPuntosDeSalud() {
        return puntosDeSalud;
    }

    public final void setPuntosSalud(final int puntosSalud) {
        this.puntosDeSalud = puntosSalud;
    }

    public final String getAccion() {
        return this.accion;
    }

    public final int getDificultad() {
        return dificultad;
    }

    public final Image getProfesorHud() {
        return profesorHud;
    }

    public Image getSpriteCombate() {
        return spriteCombate;
    }
    
    public final String getNombre() {
        return nombre;
    }

    public final String getFraseEntrada() {
        return fraseEntrada;
    }

    public final String getFraseDerrota() {
        return fraseDerrota;
    }

    public final String getFraseVictoria() {
        return fraseVictoria;
    }
}