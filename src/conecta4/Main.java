package conecta4;

import conecta4.Estado;
import conecta4.Profesor;
import static conecta4.Estado.*;

/**
 * En esta clase se implementa el algoritmo minimax y se realiza una partida
 * entre dos jugadores Min y Max en el método main.
 *
 */
public class Main
{

/**
 * Estados que se exploran durante una partida.
 */
private static int nodosExplorados;

/**
 * Busca y devuelve el estado sucesor que tenga el valor minimax más
 * alto, de entre todos los posibles estados sucesores. La búsqueda 
 * minimax se realiza con el límite de profundidad indicado.
 * 
 * @param e Estado del que se parte.
 * @param limite Límite de profundidad.
 * @return Mejor estado sucesor posible (mejor jugada).
 */
private static MiEstado maxValor(MiEstado e, int limite)
{
    nodosExplorados++;
    
    if(limite == 0 || e.esFinal())
    {
        e.setMinimax(e.getUtilidad());
        return e;
    }
    
    MiEstado r = null; //guardar el mejor estado sucesor
    
    for(int x = 0; x < DIMX; x++) //cada posible jugada
    {
        MiEstado s = e.getSucesor(x,true);
        
        if(s != null) 
        {
            int minimax = minValor(s, limite - 1).getMinimax();
            
            if(r == null || r.getMinimax() < minimax)
            {
                r = s;
                r.setMinimax(minimax);
            }
        }
    }
    
    return r;
}

/**
 * Busca y devuelve el estado sucesor que tenga el valor minimax más
 * bajo, de entre todos los posibles estados sucesores. La búsqueda 
 * minimax se realiza con el límite de profundidad indicado.
 * 
 * @param e Estado del que se parte.
 * @param limite Límite de profundidad.
 * @return Mejor estado sucesor posible (mejor jugada).
 */
private static MiEstado minValor(MiEstado e, int limite)
{
    nodosExplorados++;
    
    if(limite == 0 || e.esFinal())
    {
        e.setMinimax(e.getUtilidad());
        return e;
    }
    
    MiEstado r = null;
    
    for(int x = 0; x < DIMX; x++) //cada posible jugada
    {
        MiEstado s = e.getSucesor(x, false); 
        
        if(s != null)
        {
            int minimax = maxValor(s, limite - 1).getMinimax();
            
            if(r == null || r.getMinimax() > minimax)
            {
                r = s; 
                r.setMinimax(minimax);
            }
        }
    }
    
    return r;
}

/**
 * Este método ejecuta una partida entre 2 jugadores Min y Max. Min utiliza
 * los algoritmos de esta clase y la clase MiEstado, mientras que Max
 * utiliza la librería Profesor.jar (oculta). Se puede elegir la profundidad 
 * con la que cada jugador busca su siguiente jugada al utilizar el algoritmo
 * minimax (por defecto 9).
 * 
 * @param args Profundidad de Min y de Max.
 */
public static void main(String args[])
{
    int limiteMax = 9,
        limiteMin = 9;

    if(args.length != 2)
    {
        System.out.println("Argumentos:  limiteMin  limiteMax");
    }
    else
    {
        limiteMin = Integer.parseInt(args[0]);
        limiteMax = Integer.parseInt(args[1]);
    }

    System.out.println("limiteMin: "+ limiteMin);
    System.out.println("limiteMax: "+ limiteMax);

    Estado e = new MiEstado();

    while(!e.esFinal())
    {
        e = Profesor.maxValor(e, limiteMax); //juega Max (algoritmo profesor)
        e = new MiEstado(e);
        System.out.println(e);

        if(!e.esFinal())
        {
            e = minValor((MiEstado)e, limiteMin); //juega Min (nuestro algoritmo)
            System.out.println(e);
        }
    }

    //Resumen de la partida
    System.out.println(" "+ nodosExplorados +" nodos explorados");
    System.out.println("Utilidad: " + e.getUtilidad());
    System.out.println("limiteMin: "+ limiteMin);
    System.out.println("limiteMax: "+ limiteMax);
}

} // Main
