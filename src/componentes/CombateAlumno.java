package componentes;

import java.util.Random;

/**
 * @author Héctor Pérez Pedrosa - WIDA46976866
 * @author  Carlos Romero Delgado - WIDA48022963
 */
public class CombateAlumno {

    private int conocimiento = 50;
    private int puntosDeSalud = 100;

    // Utilidades //
    /**
     * Método que devuelve un número aleatório.
     *
     * @return int - Un número aleatório entre 0 y 100.
     */
    public final int numeroAleatorio() {
        return new Random().nextInt(100) + 1;
    }

    // Getters & Setters //
    public final int getConocimiento() {
        return conocimiento;
    }

    public final void setConocimiento(final int conocimiento) {
        this.conocimiento = conocimiento;
    }

    public final int getPuntosDeSalud() {
        return puntosDeSalud;
    }

    public final void setPuntosDeSalud(final int puntosDeSalud) {
        this.puntosDeSalud = puntosDeSalud;
    }
}
