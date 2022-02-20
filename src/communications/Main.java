package communications;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This is the main class from which the buffer and producerConsumer classes are executed
 */
public class Main {

    //TODO ; Terminar configuracion y poner algo para poner a correr los processos y coordinarlos
    //TODO: TERMINAR LA EJECUCION DE LOS PROCESOS ,USAR BARRERASCICLICAS O ALGO
    public final static String fileName = "Caso1/inputFile.txt";

    public static HashMap<String,Buffer> bufferList;

    public static HashMap<String,ProducerConsumer> producerConsumersList;



    //TODO : Declare parameters for all the qualities of the buffers and the processes
    //TODO: These parameters will be initializes by the parseConsole method
    public static void main(String[] args){
        //RUN CONSOLE AND RETURN THE RESULTING INFORMATION

        System.out.println("THE CONFIGURATION FOR THE PROCESSES AND THE BUFFERS MUST BE PUT IN THE inputFile.txt WITH THE SPECIFIED PATTERN GIVEN IN THE CASE SPECIFICATIONS ");

        //Parses the input file txt content and creates the producerConsumers and the Buffers
        parseConsoleInput();

        System.out.println("The input configuration has been completed");

        //TODO: START CYCLIC BARRIER HERE

        //TODO: START THE THREADS

            //TODO: START PROCESS-CONSUMER 1 AND SEND THE INITIAL NUMBER OF MESSAGES


            //TODO: START ALL THE OTHER THREADS

        //TODO: JOIN THE THREADS HERE TO KILL THEM

        //TODO: WRITE RESULTS SOMEWHERE?
    }


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
            int j = i - 4;
            Buffer originBuffer = bufferList.get(Character.toString((char)('A'+ j)));
            Buffer destinationBuffer = bufferList.get(Character.toString((char)('B'+ j)));

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
