package org.iesvdm.tddjava.connect4;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class Connect4TDDSpec {

    private Connect4TDD tested;

    private OutputStream output;

    @BeforeEach
    public void beforeEachTest() {
        output = new ByteArrayOutputStream();

        //Se instancia el juego modificado para acceder a la salida de consola
        tested = new Connect4TDD(new PrintStream(output));
    }

    /*
     * The board is composed by 7 horizontal and 6 vertical empty positions
     */

    @Test
    public void whenTheGameStartsTheBoardIsEmpty() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++) {

            // WHEN & DO

            // Reinicialización del objeto Connect4TDD en cada iteración
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            int numeroDeDiscos = tested.getNumberOfDiscs();

            // THEN

            // Comprobación de que el número de discos del tablero es 0
            assertThat(numeroDeDiscos).isZero();

        }

    }

    /*
     * Players introduce discs on the top of the columns.
     * Introduced disc drops down the board if the column is empty.
     * Future discs introduced in the same column will stack over previous ones
     */

    @Test
    public void whenDiscOutsideBoardThenRuntimeException() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración
            // para que sus columnas estén vacías
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Llenado de una columna para que luego podamos probar
            // a poner un disco en dicha columna llena

            // Elección de una columna al azar dentro de las columnas posibles
            int cantidadDeColumnasDelJuego = 7;
            int columnaInsertada = (int)(Math.random()*cantidadDeColumnasDelJuego);

            int cantidadDeFilasDelJuego = 6;

            for(int i=0; i<cantidadDeFilasDelJuego; i++){
                tested.putDiscInColumn(columnaInsertada);
            }

            // DO & THEN

            // Comprobación de que se lanza una RuntimeException cuando intentamos
            // poner un disco en una columna llena
            Assertions.assertThrows(RuntimeException.class, () -> tested.putDiscInColumn(columnaInsertada));

        }


    }

    @Test
    public void whenFirstDiscInsertedInColumnThenPositionIsZero() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración
            // para que sus columnas estén vacías
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección de una columna al azar dentro de las columnas posibles
            int cantidadDeColumnasDelJuego = 7;
            int columnaInsertada = (int)(Math.random()*cantidadDeColumnasDelJuego);

            // DO & THEN

            // Comprobación de que el número de discos de dicha columna es 0
            assertThat(tested.putDiscInColumn(columnaInsertada)).isZero();

        }

    }

    @Test
    public void whenSecondDiscInsertedInColumnThenPositionIsOne() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración
            // para que sus columnas estén vacías
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección de una columna al azar dentro de las columnas posibles
            int cantidadDeColumnasDelJuego = 7;
            int columnaInsertada = (int)(Math.random()*cantidadDeColumnasDelJuego);

            // Añadido de un disco a la columnaInsertada (que estaba vacía y que
            // despues de insertarle este disco va a tener 1 disco)
            tested.putDiscInColumn(columnaInsertada);

            // DO & THEN

            // Comprobación de que el número de discos de la columna a la que
            // se le había insertado 1 disco es 1
            assertThat(tested.putDiscInColumn(columnaInsertada)).isOne();

        }


    }

    @Test
    public void whenDiscInsertedThenNumberOfDiscsIncreases() {

        int cantComprob = 100;
        int numeroDeDiscosAnteriores = 0;
        int cantColumnasInsertadas = 4;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
                numeroDeDiscosAnteriores = tested.getNumberOfDiscs();
            }

            // DO & THEN

            // Inserción de todos los discos de cada columna en las columnas pares
            // (porque si se insertan en todas las columnas, se alcanzará el fin de la partida
            // antes de terminar esta comprobación)
            for(int g=0; g<cantColumnasInsertadas*2; g+=2){
                for(int f=0; f<6; f++){
                    tested.putDiscInColumn(g);

                    // Comprobación de que se ha aumentado el número de discos totales
                    assertThat(tested.getNumberOfDiscs()).isEqualTo(numeroDeDiscosAnteriores+1);

                    // Actualización de ´´numeroDeDiscosAnteriores``
                    numeroDeDiscosAnteriores = tested.getNumberOfDiscs();
                }

            }

        }

    }

    @Test
    public void whenNoMoreRoomInColumnThenRuntimeException() {



    }

    /*
     * It is a two-person game so there is one colour for each player.
     * One player uses red ('R'), the other one uses green ('G').
     * Players alternate turns, inserting one disc every time
     */

    @Test
    public void whenFirstPlayerPlaysThenDiscColorIsRed() {

    }

    @Test
    public void whenSecondPlayerPlaysThenDiscColorIsGreen() {

    }

    /*
     * We want feedback when either, event or error occur within the game.
     * The output shows the status of the board on every move
     */

    @Test
    public void whenAskedForCurrentPlayerTheOutputNotice() {



    }

    @Test
    public void whenADiscIsIntroducedTheBoardIsPrinted() {

    }

    /*
     * When no more discs can be inserted, the game finishes and it is considered a draw
     */

    @Test
    public void whenTheGameStartsItIsNotFinished() {

    }

    @Test
    public void whenNoDiscCanBeIntroducedTheGamesIsFinished() {

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight vertical line then that player wins
     */

    @Test
    public void when4VerticalDiscsAreConnectedThenThatPlayerWins() {

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight horizontal line then that player wins
     */

    @Test
    public void when4HorizontalDiscsAreConnectedThenThatPlayerWins() {

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight diagonal line then that player wins
     */

    @Test
    public void when4Diagonal1DiscsAreConnectedThenThatPlayerWins() {

    }

    @Test
    public void when4Diagonal2DiscsAreConnectedThenThatPlayerWins() {

    }



    // PLANTILLA DE LAS COMPROBACIONES

//    // Comprobaciones
//    int cantComprob = 100;
//
//        for(int h=0; h<cantComprob; h++){
//
//        // WHEN
//
//        // Reinicialización del objeto Connect4TDD en cada iteración
//        if(h>0){
//            tested = new Connect4TDD(new PrintStream(output));
//        }
//
//        // DO
//
//
//
//        // THEN
//
//
//
//    }

}
