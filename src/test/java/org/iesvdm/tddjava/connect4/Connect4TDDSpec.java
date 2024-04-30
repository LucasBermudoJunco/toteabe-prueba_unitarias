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

    // Atributos para todos los tests
    private static int discosParaGanar = 4;
    private static int cantFilas = 6;
    private static int cantColumnas = 7;

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

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            // para volver a tener el juego recién empezado
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

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            // para que sus columnas estén vacías
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Llenado de una columna para que luego podamos probar
            // a poner un disco en dicha columna llena

            // Elección de una columna al azar dentro de las columnas posibles
            int cantidadDeColumnasDelJuego = 7;
            int columnaInsertadaDentroDeLimites = (int)(Math.random()*cantidadDeColumnasDelJuego);

            int cantidadDeFilasDelJuego = 6;

            // Llenado de esa columna
            for(int i=0; i<cantidadDeFilasDelJuego; i++){
                tested.putDiscInColumn(columnaInsertadaDentroDeLimites);
            }

            // Elección de una columna al azar fuera de las columnas posibles
            int columnaInsertadaFueraLimites;
            do{
                columnaInsertadaFueraLimites = (int)(Math.random()*201-100);
            } while(columnaInsertadaFueraLimites >=0 && columnaInsertadaFueraLimites<cantidadDeColumnasDelJuego);

            // DO & THEN

            // Comprobación de que se lanza una RuntimeException cuando intentamos
            // poner un disco en una columna llena
            assertThatThrownBy(() -> tested.putDiscInColumn(columnaInsertadaDentroDeLimites)).isInstanceOf(RuntimeException.class);

            // Comprobación de que se lanza una RuntimeException cuando intentamos
            // poner un disco en una columna que no existe
            int finalColumnaInsertadaFueraLimites = columnaInsertadaFueraLimites;
            assertThatThrownBy(() -> tested.putDiscInColumn(finalColumnaInsertadaFueraLimites)).isInstanceOf(RuntimeException.class);

        }


    }

    @Test
    public void whenFirstDiscInsertedInColumnThenPositionIsZero() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
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

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
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

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
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

    /**
     * Este test es igual que el ´´whenDiscOutsideBoardThenRuntimeException`` pero solamente usamos la parte
     * de las columnas desbordadas y no de las columnas que no existen
     */
    @Test
    public void whenNoMoreRoomInColumnThenRuntimeException() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            // para que sus columnas estén vacías
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Llenado de una columna para que luego podamos probar
            // a poner un disco en dicha columna llena

            // Elección de una columna al azar dentro de las columnas posibles
            int cantidadDeColumnasDelJuego = 7;
            int columnaInsertadaDentroDeLimites = (int)(Math.random()*cantidadDeColumnasDelJuego);

            int cantidadDeFilasDelJuego = 6;

            // Llenado de esa columna
            for(int i=0; i<cantidadDeFilasDelJuego; i++){
                tested.putDiscInColumn(columnaInsertadaDentroDeLimites);
            }

            // Inhabilitación del test de las columnas que no existen

//            // Elección de una columna al azar fuera de las columnas posibles
//            int columnaInsertadaFueraLimites;
//            do{
//                columnaInsertadaFueraLimites = (int)(Math.random()*201-100);
//            } while(columnaInsertadaFueraLimites >=0 && columnaInsertadaFueraLimites<cantidadDeColumnasDelJuego);

            // DO & THEN

            // Comprobación de que se lanza una RuntimeException cuando intentamos
            // poner un disco en una columna llena
            assertThatThrownBy(() -> tested.putDiscInColumn(columnaInsertadaDentroDeLimites)).isInstanceOf(RuntimeException.class);

            // Inhabilitación del test de las columnas que no existen

//            // Comprobación de que se lanza una RuntimeException cuando intentamos
//            // poner un disco en una columna que no existe
//            int finalColumnaInsertadaFueraLimites = columnaInsertadaFueraLimites;
//            assertThatThrownBy(() -> tested.putDiscInColumn(finalColumnaInsertadaFueraLimites)).isInstanceOf(RuntimeException.class);

        }
    }

    /*
     * It is a two-person game so there is one colour for each player.
     * One player uses red ('R'), the other one uses green ('G').
     * Players alternate turns, inserting one disc every time
     */

    @Test
    public void whenFirstPlayerPlaysThenDiscColorIsRed() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // DO

            String colorJugadorPrimero = tested.getCurrentPlayer();

            // THEN

            assertThat(colorJugadorPrimero).isEqualTo("R");

        }

    }

    @Test
    public void whenSecondPlayerPlaysThenDiscColorIsGreen() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // DO

            // Tirada del primer jugador (que es el jugador rojo)
            tested.putDiscInColumn((int)(Math.random()*7));

            // Asignación al String ´´`colorJugadorSegundo` del siguiente jugador
            // (que ahora es el segundo jugador)
            String colorJugadorSegundo = tested.getCurrentPlayer();

            // THEN

            assertThat(colorJugadorSegundo).isEqualTo("G");

        }

    }

    /*
     * We want feedback when either, event or error occur within the game.
     * The output shows the status of the board on every move
     */

    @Test
    public void whenAskedForCurrentPlayerTheOutputNotice() {

        String currentPlayer = tested.getCurrentPlayer();

        String mensajeOutput = String.format("Player %s turn%n", currentPlayer);

        assertThat(output.toString()).isEqualTo(mensajeOutput);

    }

    @Test
    public void whenADiscIsIntroducedTheBoardIsPrinted() {

        // 1º turno
        tested.putDiscInColumn(0);

        String expected1 = """
                | | | | | | | |
                | | | | | | | |
                | | | | | | | |
                | | | | | | | |
                | | | | | | | |
                |R| | | | | | |
                """;

        assertThat(output.toString().replaceAll("\r", "")).isEqualTo(expected1);

        // Impresión del contenido del output
        System.out.println("1ª jugada:");
        System.out.println(output.toString());

//        // Otra forma de ver el salto de línea ´´\r\n`` en Windows
//
//        String expected1 = """
//                | | | | | | | |\r
//                | | | | | | | |\r
//                | | | | | | | |\r
//                | | | | | | | |\r
//                | | | | | | | |\r
//                |R| | | | | | |\r
//                """;
//
//        assertThat(output.toString()).isEqualTo(expected1);

        // 2º turno
        tested.putDiscInColumn(0);

        String expected2 = """
                | | | | | | | |
                | | | | | | | |
                | | | | | | | |
                | | | | | | | |
                |G| | | | | | |
                |R| | | | | | |
                """;

        assertThat(output.toString().replaceAll("\r", "").replace(expected1,"")).isEqualTo(expected2);

        // Impresión del contenido del output
        System.out.println("2ª jugada:");
        System.out.println(output.toString().replaceAll("\r", "").replace(expected1,""));

    }

    /*
     * When no more discs can be inserted, the game finishes and it is considered a draw
     */

    @Test
    public void whenTheGameStartsItIsNotFinished() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN & DO

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            // para volver a tener el juego recién empezado
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // THEN

            assertThat(tested.isFinished()).isFalse();

        }

    }

    @Test
    public void whenNoDiscCanBeIntroducedTheGamesIsFinished() {

        // En este caso solo realizamos 2 comprobaciones debido a la forma en que se va a llenar el tablero
        // para simular la partida (en la cual van a haber 2 lugares de comienzo: la columna 0 y la 1)
        int cantComprob = 2;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // DO

            // Llenado del tablero de tal forma se llene por completo antes de que ningún jugador gane
            int primeraColumna;

            if(h==0){
                // Queremos que se llene de la siguiente forma:
                //   XOXOXOO
                //   XOXOXOX
                //   OXOXOXO
                //   XXOXOXO
                //   XOXOXOO
                //   XOXOXOX
                for(int i=0; i<(cantFilas*cantColumnas); i++){
                    if(i<=5){
                        tested.putDiscInColumn(i);
                    } else if(i>=6 && i<=11){
                        tested.putDiscInColumn(i-6);
                    } else if(i>=12 && i<=13){
                        tested.putDiscInColumn(6);
                    } else if(i>=14 && i<=19) {
                        // Cambio de la columna 0 a la columna 1
                        tested.putDiscInColumn(i - 13);
                    } else if(i>=20 && i<=25){
                        tested.putDiscInColumn(i - 19);
                    } else if(i>=26 && i<=27){
                        tested.putDiscInColumn(0);
                    } else if(i>=28 && i<=33){
                        // Vuelta a la columna 0
                        tested.putDiscInColumn(i-28);
                    } else if(i>=34 && i<=39){
                        tested.putDiscInColumn(i-34);
                    } else{
                        tested.putDiscInColumn(6);
                    }
                }
            } else{
                // Queremos que se llene de la siguiente forma:
                //   OXOXOXO
                //   XXOXOXO
                //   XOXOXOO
                //   XOXOXOX
                //   OXOXOXO
                //   XXOXOXO
                for(int i=0; i<(cantFilas*cantColumnas); i++){
                    if(i<=5){
                        tested.putDiscInColumn(i+1);
                    } else if(i>=6 && i<=11){
                        tested.putDiscInColumn(i-5);
                    } else if(i>=12 && i<=13){
                        tested.putDiscInColumn(0);
                    } else if(i>=14 && i<=19) {
                        // Cambio de la columna 1 a la columna 0
                        tested.putDiscInColumn(i - 14);
                    } else if(i>=20 && i<=25){
                        tested.putDiscInColumn(i - 20);
                    } else if(i>=26 && i<=27){
                        tested.putDiscInColumn(6);
                    } else if(i>=28 && i<=33){
                        // Vuelta a la columna 1
                        tested.putDiscInColumn(i-27);
                    } else if(i>=34 && i<=39){
                        tested.putDiscInColumn(i-33);
                    } else{
                        tested.putDiscInColumn(0);
                    }
                }
            }

            // THEN

            assertThat(tested.isFinished()).isTrue();

        }

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight vertical line then that player wins
     */

    @Test
    public void when4VerticalDiscsAreConnectedThenThatPlayerWins() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección al azar del jugador que queremos que gane en esta comprobación
            String jugadorGanador="";
            int eleccion = (int)(Math.random()*2);
            switch(eleccion){
                case 0: jugadorGanador = "R"; break;
                default: jugadorGanador = "G"; break;
            }

            // Elección al azar de una columna del tablero a la que va a jugar
            // el jugador que queremos que gane
            int columnaDelJugadorGanador = (int)(Math.random()*7);

            // Elección al azar de una columna del tablero a la que va a jugar
            // el jugador que queremos que pierda (excluyendo de las columnas
            // la columna a la que va a jugar el jugador que queremos que gane)
            int columnaDelJugadorPerdedor;
            do {
                columnaDelJugadorPerdedor = (int) (Math.random()*7);
            } while (columnaDelJugadorPerdedor == columnaDelJugadorGanador);

            // DO

            // Ajuste de la partida en caso de que el jugador que queramos que gane
            // sea el segundo jugador (por lo que habrá que elegir una columna distinta
            // a la columna elegida para el jugador que queramos que pierda
            // para que así dicho primer jugador no consiga los 4 discos juntos
            // antes que el segundo jugador)
            if(jugadorGanador.equals("G")){
                int columnaDeLaPrimeraTirada;
                do{
                    columnaDeLaPrimeraTirada = (int) (Math.random()*7);
                } while(columnaDeLaPrimeraTirada == columnaDelJugadorPerdedor);

                // Tirada del primer jugador
                tested.putDiscInColumn(columnaDeLaPrimeraTirada);
            }

            // Jugada 7 veces (4 del jugador que queremos que gane
            // más las jugadas de en medio del otro jugador (que serán 3 jugadas))
            for(int i=0; i<(discosParaGanar*2)-1; i++){
                if(i%2 == 0) {
                    // Jugada del jugador que queremos que gane
                    tested.putDiscInColumn(columnaDelJugadorGanador);
                } else {
                    // Jugada del otro jugador
                    tested.putDiscInColumn(columnaDelJugadorPerdedor);

                }
            }

            // THEN

            assertThat(tested.getWinner()).isEqualTo(jugadorGanador);

        }

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight horizontal line then that player wins
     */

    @Test
    public void when4HorizontalDiscsAreConnectedThenThatPlayerWins() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección al azar del jugador que queremos que gane en esta comprobación
            String jugadorGanador="";
            int eleccion = (int)(Math.random()*2);
            switch(eleccion){
                case 0: jugadorGanador = "R"; break;
                default: jugadorGanador = "G"; break;
            }

            // Elección al azar de una columna del tablero a la que va a jugar
            // el jugador que queremos que gane (dicho jugador va a jugar a esa columna
            // y a las 3 columnas a la derecha de esta columna, por lo que la primera
            // columna de estas 4 columnas tiene que estar entre la 0 y la 3)
            int columnaDelJugadorGanador = (int)(Math.random()*4);

            // Creación de una primera tirada en caso de que el jugador que queramos
            // que gane sea el segundo jugador
            int columnaDeLaPrimeraTirada = 0;
            if(jugadorGanador.equals("G")) {
                // Elección al azar de una columna del tablero a la que va a jugar
                // el jugador que queremos que pierda en su primera tirada
                // (excluyendo de las columnas las 4 columnas a las que va a jugar
                // el jugador que queremos que gane)
                do {
                    columnaDeLaPrimeraTirada = (int) (Math.random() * 7);
                } while (columnaDeLaPrimeraTirada >= columnaDelJugadorGanador &&
                        columnaDeLaPrimeraTirada <= columnaDelJugadorGanador+3);
            }

            // DO

            // Ajuste de la partida en caso de que el jugador que queramos que gane
            // sea el segundo jugador
            if(jugadorGanador.equals("G")){
                // Tirada del primer jugador
                tested.putDiscInColumn(columnaDeLaPrimeraTirada);
            }

            // Jugada 7 veces (4 del jugador que queremos que gane
            // más las jugadas de en medio del otro jugador (que serán 3 jugadas))
            for(int i=0; i<(discosParaGanar*2)-1; i++){
                // El jugador perdedor va a jugar a la misma columna
                // que el jugador ganador, pero como el jugador ganador
                // va a conseguir la línea de 4 antes que el segundo,
                // dicho segundo jugador no va a tener la oportunidad de ganar
                int columnaDeEstaTirada = columnaDelJugadorGanador+(i/2);

                tested.putDiscInColumn(columnaDeEstaTirada);
            }

            // THEN

            assertThat(tested.getWinner()).isEqualTo(jugadorGanador);

        }

    }

    /*
     * If a player inserts a disc and connects more than 3 discs of his colour
     * in a straight diagonal line then that player wins
     */

    /*
     * Diagonal desde abajo-izquierda hasta arriba-derecha
     */
    @Test
    public void when4Diagonal1DiscsAreConnectedThenThatPlayerWins() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección al azar del jugador que queremos que gane en esta comprobación
            String jugadorGanador="";
            int eleccion = (int)(Math.random()*2);
            switch(eleccion){
                case 0: jugadorGanador = "R"; break;
                default: jugadorGanador = "G"; break;
            }

            // Elección al azar de una columna del tablero a la que va a jugar
            // el jugador que queremos que gane (dicho jugador va a jugar a esa columna
            // y a las 3 columnas a la derecha de esta columna, por lo que la primera
            // columna de estas 4 columnas tiene que estar entre la 0 y la 3)
            int columnaDelJugadorGanador = (int)(Math.random()*4);

            // Creación de una primera tirada en caso de que el jugador que queramos
            // que gane sea el segundo jugador
            int columnaDeLaPrimeraTirada = 0;
            if(jugadorGanador.equals("G")) {
                // Elección al azar de una columna del tablero a la que va a jugar
                // el jugador que queremos que pierda en su primera tirada
                // (excluyendo de las columnas las 4 columnas a las que va a jugar
                // el jugador que queremos que gane)
                do {
                    columnaDeLaPrimeraTirada = (int) (Math.random() * 7);
                } while (columnaDeLaPrimeraTirada >= columnaDelJugadorGanador &&
                        columnaDeLaPrimeraTirada <= columnaDelJugadorGanador+3);
            }

            // DO

            // Ajuste de la partida en caso de que el jugador que queramos que gane
            // sea el segundo jugador
            if(jugadorGanador.equals("G")){
                // Tirada del primer jugador
                tested.putDiscInColumn(columnaDeLaPrimeraTirada);
            }

            // Jugada 11 veces para que se construya la diagonal del jugador ganador
            for(int i=0; i<11; i++){
                // Alternado entre las columnas para que el jugador ganador
                // construya su diagonal ganadora de esta forma:
                //      X
                //    OXO
                //    XOX
                //   XOXO
                if(i==0){
                    tested.putDiscInColumn(columnaDelJugadorGanador);
                } else if(i>=1 && i<=3){
                    tested.putDiscInColumn(columnaDelJugadorGanador+1);
                } else if(i>=4 && i<=6){
                    tested.putDiscInColumn(columnaDelJugadorGanador+2);
                } else {
                    tested.putDiscInColumn(columnaDelJugadorGanador+3);
                }
            }

            // THEN

            assertThat(tested.getWinner()).isEqualTo(jugadorGanador);

        }

    }

    /*
     * Diagonal desde arriba-izquierda hasta abajo-derecha
     */
    @Test
    public void when4Diagonal2DiscsAreConnectedThenThatPlayerWins() {

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // Elección al azar del jugador que queremos que gane en esta comprobación
            String jugadorGanador="";
            int eleccion = (int)(Math.random()*2);
            switch(eleccion){
                case 0: jugadorGanador = "R"; break;
                default: jugadorGanador = "G"; break;
            }

            // Elección al azar de una columna del tablero a la que va a jugar
            // el jugador que queremos que gane (dicho jugador va a jugar a esa columna
            // y a las 3 columnas a la derecha de esta columna, por lo que la primera
            // columna de estas 4 columnas tiene que estar entre la 0 y la 3)
            int columnaDelJugadorGanador = (int)(Math.random()*4);

            // En este caso, debe empezar a jugar a las columnas correctas
            // el jugador que queremos que pierda
            // (al contrario que en los métodos anteriores)

            // Creación de una primera tirada en caso de que el jugador que queramos
            // que gane sea el primer jugador
            int columnaDeLaPrimeraTirada = 0;
            if(jugadorGanador.equals("R")) {
                // Elección al azar de una columna del tablero a la que va a jugar
                // el jugador que queremos que pierda en su primera tirada
                // (excluyendo de las columnas las 4 columnas a las que va a jugar
                // el jugador que queremos que gane)
                do {
                    columnaDeLaPrimeraTirada = (int) (Math.random() * 7);
                } while (columnaDeLaPrimeraTirada >= columnaDelJugadorGanador &&
                        columnaDeLaPrimeraTirada <= columnaDelJugadorGanador+3);
            }

            // DO

            // Ajuste de la partida en caso de que el jugador que queramos que gane
            // sea el primer jugador
            if(jugadorGanador.equals("R")){
                // Tirada del primer jugador
                tested.putDiscInColumn(columnaDeLaPrimeraTirada);
            }

            // Jugada 12 veces para que se construya la diagonal del jugador ganador
            for(int i=0; i<12; i++){
                // Alternado entre las columnas para que el jugador ganador
                // construya su diagonal ganadora de esta forma:
                //   X
                //   OX
                //   XOX
                //   OXOX
                if(i<=4){
                    tested.putDiscInColumn(columnaDelJugadorGanador);
                } else if(i>=5 && i<=7){
                    tested.putDiscInColumn(columnaDelJugadorGanador+1);
                } else if(i>=8 && i<=10){
                    tested.putDiscInColumn(columnaDelJugadorGanador+2);
                } else {
                    tested.putDiscInColumn(columnaDelJugadorGanador+3);
                }
            }

            // THEN

            assertThat(tested.getWinner()).isEqualTo(jugadorGanador);

        }

    }



    // PLANTILLA DE LAS COMPROBACIONES

/*

        int cantComprob = 100;

        // Comprobaciones
        for(int h=0; h<cantComprob; h++){

            // WHEN

            // Reinicialización del objeto Connect4TDD en cada iteración mayor a la primera
            if(h>0){
                tested = new Connect4TDD(new PrintStream(output));
            }

            // DO



            // THEN



        }

 */

}
