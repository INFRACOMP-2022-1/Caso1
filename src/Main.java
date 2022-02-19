import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class from which the buffer and producerConsumer classes are executed
 */
public class Main {

    //TODO : Declare parameters for all the qualities of the buffers and the processes
    //TODO: These parameters will be initializes by the parseConsole method
    public static void main(String[] args){
        //RUN CONSOLE AND RETURN THE RESULTING INFORMATION

        //CREATE THE BUFFERS AND THE PRODUCER/CONSUMERS

    }

    /**
     * This method is in charge of parsing the input from the console and starting the threads.
     * In charge of creating the buffers, the producer-consumers and starting them acording to the given parameters
     */
    public static List parseConsoleInput(){
        //TODO: Console text. Look at specified format.
        ArrayList<String> rawInput = consoleInput();

        //Process each line and extract information

        for(int i = 0 ; i < rawInput.size();i++){
            //NUMBER OF MESSAGES (location 0)
            if(i==0){
                //TODO: Parse this in another method
            }
            //BUFFER INFORMATION (locations 1 to 4)
            else if(i > 0 && i < 5){
                //TODO: Parse this in another method
            }
            //PROCESS INFORMATION (locations 5 to 8)
            else{
                //TODO: Parse this in another method
            }
        }

    }

    /**
     * Responsible for reading the input from console and parsing it
     * @return ArrayList containing each line that was given in the console
     */
    public static ArrayList<String> consoleInput(){
        //INPUT FORMAT
        //<id Buzón A> <tamaño buzón A>
        //…
        //<id Buzón D> <tamaño buzón D>
        //<id Proceso 1> <tiempo espera de transformación> <tipo de envío> <tipo de recepción>
        //…
        //<id Proceso N> <tiempo espera de transformación> <tipo de envío> <tipo de recepción>

        ArrayList<String> consoleInput= new ArrayList <String>();//Contains the entirety of the input in the console

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //Tries to read the entire input stream
        try {
            String line = null;
            while (!(line = bufferedReader.readLine()).equals("")) {
                consoleInput.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return consoleInput;
    }


    /**
     * In charge of creating the buffers, the producer-consumers and starting them acording to the given parameters
     */
    public static void setUp(){
        //TODO: Create the entities and start the threads in order

        /**
         * BUFFER CREATION
         */
        //Buffer bufferA = new Buffer();
        //Buffer bufferB = new Buffer();
        //Buffer bufferC = new Buffer();
        //Buffer bufferD = new Buffer();

        /**
         * PRODUCER/CONSUMER CREATION
         */
        //TODO: Crear los cuatro productores-consumidores que conectan los buffers

        /**
         * START THREADS
         */
        //TODO: Una vez todos los threads esten toca incialrizarlos aqui con todos los parametros indicados


    }


    //RETURN RESULTS

    //NEED TO HAVE DATA
    //BUZON
    //buffer size
    //PROCESS
    //tiempo de demora
    //estilo de comunicacion (pasivo/activo) (yield o wait)
    //Proceso 1 es el que sabe cuantos mensajes se deben mandar


}
