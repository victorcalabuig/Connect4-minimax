package conecta4;

/**
 * Clase base para representar los estados de conecta 4.
 */
public abstract class Estado
{
/**
 * Valor de utilidad ganador (positivo para MAX y negativo para MIN).
 */
public static final int GANADOR = 1000;

/**
 * Número de columnas en el tablero.
 * Coincide con el número máximo de sucesores de un estado.
 */
public static final int DIMX = 7;

/**
 * Número de fichas que caben en una columna.
 */
public static final int DIMY = 6;

/**
 * Calcula un sucesor poniendo una ficha en la columna x.
 * @param x   columna
 * @param max ficha: true para MAX, false para MIN.
 * @return Estado sucesor o null si no caben más fichas en la columna x.
 */
public abstract Estado getSucesor(int x, boolean max);

/**
 * Indica la ficha en la columna x, posición y.
 * @param x columna
 * @param y posición de la ficha en la columna. La posición del fondo es 0.
 * @return true, false o null si hay una ficha MAX, MIN, o ninguna.
 */
public abstract Boolean getFicha(int x, int y);

/**
 * Indica cuántas fichas hay en la columna x.
 * @param x columna
 * @return Número de fichas en la columna x.
 */
public abstract int getFichas(int x);

/**
 * Calcula el valor de utilidad de un estado.
 * @return Valor de utilidad. GANADOR si gana MAX. -GANADOR si gana MIN.
 */
public abstract int getUtilidad();

/**
 * Indica si es un estado final (ganador o empate).
 * @return {@code true} si es un estado final
 */
public abstract boolean esFinal();

} // Estado
