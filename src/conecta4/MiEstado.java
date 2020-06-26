package conecta4;

import conecta4.Estado;
import static java.lang.Boolean.*;

/**
 * Estado del juego conecta 4.
 */
public class MiEstado extends Estado {

    /**
     * Matriz con DIMX columnas y DIMY filas que representa el tablero 
     * de fichas. Se utiliza el valor true, false, y null para representar
     * fichas del jugador max, min o sin ficha.
     */
    private Boolean[][] tablero;
    
    /**
     * Indica lo favorable que es un estado para un jugador. Se calcula 
     * como el numero de cuatros en raya que puede realizar Max menos los
     * que puede realizar Min. Por ejemplo, al principio del juego cuando 
     * el tablero esta vacio, tanto Min como Max pueden hacer hasta 69 
     * cuatros en raya posibles, por lo que el valor de utilidad sería:
     * U = combinaciones(Max) - combinaciones(Min) = 69 - 69 = 0. 
     */
    private int utilidad;
    
    /**
     * Variable utilizada para almacenar los valores de utilidad de cada rama
     * a medida que se explora el arbol de posibles jugadas.
     */ 
    private int minimax;

    /**
     * Crea un estado vacío, es decir, sin fichas en el tablero.
     */
    public MiEstado() {
        tablero = new Boolean[DIMX][DIMY];
        calcularUtilidad();
    }

    /**
     * Crea un estado idéntico a otro.
     *
     * @param e Estado a copiar.
     */
    public MiEstado(Estado e) {
        tablero = new Boolean[DIMX][DIMY];
        for (int x = 0; x < DIMX; x++) {
            for (int y = 0; y < DIMY; y++) {
                tablero[x][y] = e.getFicha(x, y);
            }
        }
        utilidad = e.getUtilidad();
    }

    /**
     * Calcula un sucesor poniendo una ficha en la columna x.
     *
     * @param x columna
     * @param max ficha: true para MAX, false para MIN.
     * @return Estado sucesor o null si no caben más fichas en la columna x.
     */
    @Override
    public MiEstado getSucesor(int x, boolean max) {
        MiEstado sucesor = new MiEstado(this);
        int fichas = this.getFichas(x);
        if (fichas < DIMY) {
            sucesor.tablero[x][fichas] = max;
            
            sucesor.calcularUtilidad();
            return sucesor;
        } else {
            return null;
        }
    }

    /**
     * Indica la ficha en la columna x, posición y.
     *
     * @param x columna
     * @param y posición de la ficha en la columna. La posición del fondo es 0.
     * @return true, false o null si hay una ficha MAX, MIN, o ninguna.
     */
    @Override
    public Boolean getFicha(int x, int y) {
        return tablero[x][y];
    }

    /**
     * Indica cuántas fichas hay en la columna x.
     *
     * @param x columna
     * @return Número de fichas en la columna x.
     */
    @Override
    public int getFichas(int x) {
        int y = 0;
        while (y != 6 && tablero[x][y] != null) {
            y++;
        }
        return y;
    }

    /**
     * Devuelve el valor de utilidad de un estado.
     *
     * @return Valor de utilidad. GANADOR si gana MAX. -GANADOR si gana MIN.
     */
    @Override
    public int getUtilidad() {
        return utilidad;
    }

    /**
     * Indica si es un estado final (ganador o empate).
     *
     * @return {@code true} si es un estado final
     */
    @Override
    public boolean esFinal() {
        if (utilidad >= GANADOR || utilidad <= -GANADOR || estaLleno())
        	return true;
        else
        	return false;
    }
    
    /**
     * Comprueba si un tablero está lleno.
     * 
     * @return true si esta lleno, false si no lo está.
     */
    public boolean estaLleno(){
        for(int i = 0; i < DIMX; i++) {
            if(this.getFichas(i) != DIMY) return false;
        }
        return true;
    }

    /**
     * Convierte el estado en una cadena.
     *
     * @return cadena que representa el estado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = DIMY-1; y >= 0; y--) 
        {
            for (int x = 0; x < DIMX; x++)
            {
                Boolean ficha = tablero[x][y];
                if (ficha == null) sb.append("   ");
                else if (ficha == true) sb.append(String.format("%3s", "X"));
                else if (ficha == false) sb.append(String.format("%3s", "O"));        
            }
            sb.append('\n');
        }  
        return sb.toString();
    }

    /**
     * Establece una ficha del tablero a un valor Boolean.
     * 
     * @param b valor de la ficha.
     * @param x columna.
     * @param y fila.
     */
    public void setFicha(Boolean b, int x, int y) {
        tablero[x][y] = b;
    }

    /**
     * Calcula y establece la utilidad de un estado.
     */
    public void calcularUtilidad() 
    {
        int htal = calcularUtilidad (0, 3, 0, 5, 1, 0), //combs. horizontales
	        vert = calcularUtilidad(0, 6, 0, 2, 0, 1),  //verticales
	        dizq = calcularUtilidad(3, 6, 0, 2, -1, 1), //diagonal izquierda
	        dder = calcularUtilidad(0, 3, 0, 2, 1, 1);  //diagonal derecha
        
        //Si alguna combinación es ganadora, la utilidad es igual a GANADOR
        if (Math.abs(htal) == GANADOR)
            utilidad = htal;
        else if(Math.abs(vert) == GANADOR)
            utilidad = vert;
        else if(Math.abs(dizq) == GANADOR)
            utilidad = dizq;
        else if(Math.abs(dder) == GANADOR)
            utilidad = dder;
        //Sino, la utilidad es igual a la suma de todas las combinaciones
        else
            utilidad = htal + vert + dizq + dder;
        
    }

    /**
     * Calcula el numero de combinaciones ganadoras de Max menos las de Min 
     * (utilidad), hallando las combinaciones horizontales, verticales, o diagonolaes 
     * (izquierda o derecha) en función de los parámetros suministrados. 
     * 
     * @param xIni columna inicial para buscar combinaciones.
     * @param xFin columna final.
     * @param yIni fila inicial.
     * @param yFin fila final. 
     * @param kx -1 si se busca hacia izquierda, 1 si hacia derecha, 0 y si se 
     * busca en vertical.
     * @param ky -1 si se busca hacia abajo, 1 si hacia arriba, 0 y si se 
     * busca en horizontal.
     * @return Combinaciones de un tipo de concreto (horizontal, vertical...)
     * de Max menos las de Min.
     */
    public int calcularUtilidad(
            int xIni, int xFin, int yIni, int yFin, int kx, int ky) 
    {
        int util = 0; 
        
        //Puntos de inicio de posibles combinaciones ganadoras:
        for (int x = xIni; x <= xFin; x++) 
        {
            for (int y = yIni; y <= yFin; y++)
            {
            	//asumimos de entrada que tanto min como max pueden hacer
            	//una combinación ganadora desde el punto de partida x,y
                boolean min = true;
                boolean max = true;
                boolean win = true;
                
                for (int i = 0; i < 4; i++) 
                {
                    int xi = x + kx * i, //determinar posición a analizar
                        yi = y + ky * i;
             
                    if (TRUE.equals(this.getFicha(xi, yi))) 
                        min = false;
                    else if (FALSE.equals(this.getFicha(xi, yi)))
                        max = false;
                    else
                        win = false;                    
                }
                
                if (min && win) 
                    return -GANADOR;
                else if (min)
                    util--;                

                if (max && win)
                    return GANADOR;
                else if (max)
                    util++;                
            }
        }
        return util;
    }
    
    /**
     * Devuelve el valor minimax de un estado.
     * 
     * @return Entero minimax de un estado.
     */
    public int getMinimax()
    {
        return minimax;
    }
    
    /**
     * Establece el valor minimax de un estado. Este método lo utilizarán 
     * las funciones minValor y maxValor desde la clase Main.
     * 
     * @param m Valor minimax a introducir, que será la utilidad del estado.
     */
    public void setMinimax(int m)
    {
        minimax = m;
    }

} // MiEstado
