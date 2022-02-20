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
    public final static String fileName = "../inputFile.txt";

    public static void parseConsoleInput(){
        ArrayList<String> rawInput = readInputFile();
        HashMap<String,Buffer> bufferList = new HashMap<>();
        HashMap<String,ProducerConsumer> producerConsumersList = new HashMap<>();

        //Create the buffers
        for(int i = 0; i< 3;i++){
            Buffer newBuffer = createBuffer(rawInput.get(i));
            bufferList.put(newBuffer.getBufferId(),newBuffer);
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
        }
    }

    public static Buffer createBuffer(String specifications){
        String[] formatedStr = specifications.split(" ");
        String bufferId = formatedStr[0];
        int maxCapacity = Integer.parseInt(formatedStr[1]);

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
