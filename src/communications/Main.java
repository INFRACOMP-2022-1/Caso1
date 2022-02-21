package communications;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This is the main class from which the buffer and producerConsumer classes are executed
 */
public class Main {

    //-------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------------------------------

    //TODO ; Terminar configuracion y poner algo para poner a correr los processos y coordinarlos
    //TODO: TERMINAR LA EJECUCION DE LOS PROCESOS ,USAR BARRERASCICLICAS O ALGO

    public final static String fileName = "Caso1/inputFile.txt";

    public static HashMap<String,Buffer> bufferList;

    public static HashMap<String,ProducerConsumer> producerConsumersList;

    public static int numberOfMessages;


    //-------------------------------------------------------------------------------------------------
    // MAIN METHOD
    //-------------------------------------------------------------------------------------------------


    //TODO : Declare parameters for all the qualities of the buffers and the processes
    //TODO: These parameters will be initializes by the parseConsole method
    public static void main(String[] args){
        //RUN CONSOLE AND RETURN THE RESULTING INFORMATION

        System.out.println("THE CONFIGURATION FOR THE PROCESSES AND THE BUFFERS MUST BE PUT IN THE inputFile.txt WITH THE SPECIFIED PATTERN GIVEN IN THE CASE SPECIFICATIONS ");

        //Parses the input file txt content and creates the producerConsumers and the Buffers
        parseConsoleInput();

        System.out.println("The input configuration has been completed/n");

        //Reads the number of messages that are going to be sent by process 1
        scanConsoleInputNumberMessages();

        //TODO: START CYCLIC BARRIER HERE

        //TODO: START THE THREADS

            //Start todos los threads

            //artificialmente

            //TODO: START PROCESS-CONSUMER 1 AND SEND THE INITIAL NUMBER OF MESSAGES


            //TODO: START ALL THE OTHER THREADS

            //TODO: ONCE ALL THREADS ARRIVE TO THE BEGGINING MAKE THEM PRINT THE MESSAGE (WITH THE TRACE) AND ADD IT TO AN OUTPUT FILE
            //THIS CAN BE DONE BY SPECIFING THAT WHEN IN D BUFFER AND SOME ONE TRIES TO TAKE THEM OUT THEY SHOULD INSTEAD BE PRINTED, BUEN CONONDRUM

        //TODO: JOIN THE THREADS HERE TO KILL THEM

        //TODO: WRITE RESULTS SOMEWHERE?
    }

    //-------------------------------------------------------------------------------------------------
    // AUXILIARY METHODS
    //-------------------------------------------------------------------------------------------------

    /**
     * Parses the content of the inputFile.txt creates the corresponding buffers and producer consumers
     */
    public static void parseConsoleInput(){
        ArrayList<String> rawInput = readInputFile();
        bufferList = new HashMap<>();
        producerConsumersList = new HashMap<>();

        //Create the buffers
        for(int i = 0; i< 4;i++){
            Buffer newBuffer = createBuffer(rawInput.get(i));
            bufferList.put(newBuffer.getBufferId(),newBuffer);
            System.out.println(newBuffer.toString());
        }

        //Create the processes
        for(int i = 4; i<rawInput.size();i++){

            Buffer originBuffer;
            Buffer destinationBuffer;

            if(i==4){
                originBuffer = bufferList.get(Character.toString('D'));
                destinationBuffer = bufferList.get(Character.toString('A');
            }
            else{
                int j = i - 5;
                originBuffer = bufferList.get(Character.toString((char)('A'+ j)));
                destinationBuffer = bufferList.get(Character.toString((char)('B'+ j)));
            }


            String currentProducerConsumerRaw = rawInput.get(i);
            String[] splitProducerConsumer = currentProducerConsumerRaw.split(" ");
            int pcId = Integer.parseInt(splitProducerConsumer[0]);
            int sleepTimeMilli = Integer.parseInt(splitProducerConsumer[1]);
            boolean activeReception = Boolean.parseBoolean(splitProducerConsumer[2]);
            boolean activeEmission = Boolean.parseBoolean(splitProducerConsumer[3]);

            //TODO: Decide what im going to do about the messages
            ProducerConsumer newProducerConsumer = new ProducerConsumer(originBuffer,destinationBuffer,"",pcId,activeReception,activeEmission, sleepTimeMilli );
            producerConsumersList.put(Long.toString(newProducerConsumer.getPcId()),newProducerConsumer);
            System.out.println(newProducerConsumer.toString());
        }
    }

    /**
     * Scans the console input for the number of messages
     */
    public static void scanConsoleInputNumberMessages(){
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter the number of messages to be sent by process 1: ");
       numberOfMessages = sc.nextInt();
    }

    /**
     * This takes a string with a buffer configuration and then creates a buffer reflecting those parameters
     * @param specifications a string containing all the information to create a buffer
     * @return A buffer with the parameters specified by the string.
     */
    public static Buffer createBuffer(String specifications){
        String[] formattedStr = specifications.split(" ");
        String bufferId = formattedStr[0];
        int maxCapacity = Integer.parseInt(formattedStr[1]);

        Buffer buffer = new Buffer(bufferId,maxCapacity);
        return buffer;
    }

    /**
     * Reads the input file with all the configuration for the buffers and the processes (threads)
     * @return ArrayList with the raw, but trimmed, content of the file (which has to be in the correct format to be valid)
     */
    public static ArrayList<String> readInputFile(){
        ArrayList<String> rawInputArray = new ArrayList<>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()){
                String data = myReader.nextLine().trim();
                if(!data.isEmpty()){
                    rawInputArray.add(data);
                }
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rawInputArray;
    }
}
