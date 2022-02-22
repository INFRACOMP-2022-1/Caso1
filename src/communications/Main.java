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

    public final static String fileName = "Caso1/inputFile.txt";

    public static HashMap<String,Buffer> bufferList;

    public static Proceso1 process1;

    public static HashMap<String,ProducerConsumer> producerConsumersList;

    public static int numberOfMessages;
    
    public static int buffersMaxCapacity = 0;
    
    public static ArrayList<String> wordList = new ArrayList<>();

    public static ArrayList<String> rawInput;

    /*
    TO TEST
     */
    public static boolean debugMode = true;


    //-------------------------------------------------------------------------------------------------
    // MAIN METHOD
    //-------------------------------------------------------------------------------------------------

    public static void main(String[] args){
        //RUN CONSOLE AND RETURN THE RESULTING INFORMATION

        System.out.println("THE CONFIGURATION FOR THE PROCESSES AND THE BUFFERS MUST BE PUT IN THE inputFile.txt WITH THE SPECIFIED PATTERN GIVEN IN THE CASE SPECIFICATIONS ");


        //Parses the input file txt content and creates the producerConsumers and the Buffers
        parseConsoleInput();

        System.out.println("The input configuration has been completed/n");

        //Reads the number of messages that are going to be sent by process 1
        
        scanConsoleInputNumberMessages();

        //checks if the number of messages to be sent goes over total buffer capacity

        while(numberOfMessages> buffersMaxCapacity) {
        	 System.out.println("the number of messages you want to send is greater than the total buffer storage");
        	 scanConsoleInputNumberMessages();
        }
        
        //receives the message strings that are going to be sent

        for(int i = 0; i< numberOfMessages;i++) {
	    	   Scanner sc = new Scanner(System.in);
		       System.out.println("Input word "+ Integer.toString(i) +" :");
		       String word = sc.nextLine();
               wordList.add(word);
        }
        //wordList.add("FIN");//This will be sent in process 1 sendMessage method

        /*
        This is where the fun begins
         */
        System.out.println("All threads are started....");
        System.out.println("Waiting for all messages to arrive back at process 1....");

        startAllThreads();

        System.out.println("");
        //Once the messages have finished cycling through a message will be printed from Process 1 with all the messages and the trace produced
    }

    //-------------------------------------------------------------------------------------------------
    // AUXILIARY METHODS
    //-------------------------------------------------------------------------------------------------

    /**
     * All threads are started, 1 to 4.
     * First the process 1 thread is started (this will send all the messages)
     * Then the remaining threads 2 to 4 are started in sequence.
     */
    public static void startAllThreads(){
        process1.start();//starts process 1 allowing it to send the messages to buffer A and then set it self to wait mode
        for(ProducerConsumer normalProcess: producerConsumersList.values()){
            normalProcess.start();
        }
    }
    /**
     * Parses the content of the inputFile.txt creates the corresponding buffers and producer consumers
     */
    public static void parseConsoleInput(){
        rawInput = readInputFile();
        bufferList = new HashMap<>();
        producerConsumersList = new HashMap<>();


        //Create the buffers
        for(int i = 0; i< 4;i++){
            Buffer newBuffer = createBuffer(rawInput.get(i));
            bufferList.put(newBuffer.getBufferId(),newBuffer);
            buffersMaxCapacity +=newBuffer.getMaxCapacity();
        }

        //Create process 1
        createProcess1(rawInput.get(4));

        //Create the processes 2-4
        createProducerConsumer(rawInput);
    }


    public static void createProcess1(String prodConRaw){
        Buffer originBufferP1 = bufferList.get(Character.toString('D'));
        Buffer destinationBufferP1 = bufferList.get(Character.toString('A'));
        String currentProducerConsumerRaw = prodConRaw;
        String[] splitProducerConsumer = currentProducerConsumerRaw.split(" ");
        long pcId = Long.parseLong(splitProducerConsumer[0]);
        int sleepTimeMilli = Integer.parseInt(splitProducerConsumer[1]);
        boolean activeReception = Boolean.parseBoolean(splitProducerConsumer[2]);
        boolean activeEmission = Boolean.parseBoolean(splitProducerConsumer[3]);

        //Creates new process 1 and intiializes the atribute
        process1 = new Proceso1(originBufferP1,destinationBufferP1,pcId,"",activeReception,activeEmission,sleepTimeMilli,wordList,rawInput, debugMode);
    }

    public static void createProducerConsumer(ArrayList<String> rawInput){
        for(int i = 5; i<rawInput.size();i++){
            Buffer originBuffer;
            Buffer destinationBuffer;

            int j = i - 5;
            originBuffer = bufferList.get(Character.toString((char)('A'+ j)));
            destinationBuffer = bufferList.get(Character.toString((char)('B'+ j)));

            String currentProducerConsumerRaw = rawInput.get(i);
            String[] splitProducerConsumer = currentProducerConsumerRaw.split(" ");
            int pcId = Integer.parseInt(splitProducerConsumer[0]);
            int sleepTimeMilli = Integer.parseInt(splitProducerConsumer[1]);
            boolean activeReception = Boolean.parseBoolean(splitProducerConsumer[2]);
            boolean activeEmission = Boolean.parseBoolean(splitProducerConsumer[3]);

            ProducerConsumer newProducerConsumer = new ProducerConsumer(originBuffer,destinationBuffer,"",pcId,activeReception,activeEmission, sleepTimeMilli, debugMode );
            //This only contains producerConsumers 2-4
            producerConsumersList.put(Long.toString(newProducerConsumer.getPcId()),newProducerConsumer);
        }
    }

    /**
     * Scans the console input for the number of messages
     * @return 
     */
    public static  void scanConsoleInputNumberMessages()
    {
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter the number of messages to be sent by process 1: ");
       numberOfMessages = sc.nextInt();
    }

    public static void scanConsoleInputBoolean(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter werther you want to acces debug mode: ");
        debugMode = sc.nextBoolean();
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