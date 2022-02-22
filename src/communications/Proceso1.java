package communications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.Date;

public class Proceso1  extends Thread
{
    //-------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------------------------------

    /*
     * When on all the sysouts are enabled
     */
    private static boolean debugMode;

    /*
    Asigns colour to the debug lines
     */
    private String colour = ANSI_RED;

    /*
    The buffer from which the message is received by the producer consumer
     */
    private Buffer originBuffer;

    /*
    The buffer at which the message is being sent to by the producer consumer
     */
    private Buffer destinationBuffer;

    /*
    The id of the producer consumer
     */
    private long pcId;

    /*
    If the communication type to receive a message is active or not. Active = true, Passive = false
     */
    private boolean activeReception;

    /*
    If the communication type to send a message is active or not. Active = true, Passive = false
     */
    private boolean activeEmission;

    /*
    The time that the thread is sent to sleep when its "processing" a message before sending it to the next buffer
     */
    private int sleepTime;

    /*
    The list of words that are going to be initially sent by Process1
     */
    private  ArrayList<String> wordListSend;

    /*
    The list of words that process 1 has recieved
     */
    private ArrayList<String> wordListReceived;

    /*
    The current message that is being processed
     */
    private String currentMessage;

    /*
    Has the specified parameters for which the class was ran with
     */
    private ArrayList<String> rawInput;


    //-------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------------------------------

    /**
     * Creates a producer consumer instance
     * @param originBuffer the buffer from which the PC will be receiving the message
     * @param destinationBuffer the buffer to which the PC will be sending the message
     * @param pcId the id of the PC
     * @param activeReception if the communication type for reception of messages from the origin buffer is active, if it's not its passive
     * @param activeEmission if the communication type for emission of messages from the origin buffer is active, if it's not its passive
     * @param sleepTime the time in milliseconds that the thread is sent to sleep when it's "processing" the message before sending it
     * @param rawInput the raw input with all the parameters used to set up the buffers and proceses
     */
    public Proceso1(Buffer originBuffer, Buffer destinationBuffer, long pcId,String currentMessage, boolean activeReception, boolean activeEmission, int sleepTime, ArrayList<String> wordListSend, ArrayList<String>rawInput, boolean debugMode)
    {
        this.originBuffer = originBuffer;
        this.destinationBuffer = destinationBuffer;
        this.pcId = pcId;
        this.activeReception = activeReception;
        this.activeEmission = activeEmission;
        this.sleepTime = sleepTime;
        this.wordListSend = wordListSend;
        this.currentMessage = currentMessage;
        this.wordListReceived = new ArrayList<>();//Starts out empty
        this.rawInput = rawInput;
        this.debugMode = debugMode;
    }

    //-------------------------------------------------------------------------------------------------
    // CORE METHODS
    //-------------------------------------------------------------------------------------------------

    /**
     * The message is received from the origin buffer once one is available to be taken.
     * Reception communication protocol is defined by the specifications of the thread itself.
     * The synchronization is handled exclusively by the buffer itself
     * @throws InterruptedException exception
     */
    public void sendMessage() throws InterruptedException
    {
    	for(int i = 0; i< wordListSend.size(); i++)
    	{
    		String message= wordListSend.get(i);
    		emmitMessage(message);
    	}
        emmitMessage("FIN");
    }

    /**
     * Waits and receives a message incoming from the origin buffer, in this case buffer D.
     * Adds the received message to the wordListReceived.
     * @throws InterruptedException
     */
    public void receiveMessage() throws InterruptedException {
        currentMessage = (activeReception) ? originBuffer.popMessageActive() : originBuffer.popMessagePassive();

        if(debugMode){
            System.out.println(String.format("%s Message received from process 1: %s",colour, currentMessage));
        }
    }

    /**
     * The message is modified to add a registry that it has passed by this particular process
     * with a particular set of protocols.Additionally, the thread simulates a processing time
     * by sending the thread to sleep by a determined time length.
     * @throws InterruptedException 
     */
    public void processMessage() throws InterruptedException {
        if(!currentMessage.equals("FIN")){
            Thread.sleep(getSleepTime());
            currentMessage = formatMessage();
            wordListReceived.add(currentMessage);
        }

        if(debugMode){
            System.out.println(String.format("%s Message processed at process 1: %s",colour, currentMessage));
        }
    }

    /**
     * The message is emitted to the destination buffer after its been modified and the needed time has passed.
     * The synchronization is handled by the buffers.
     * @throws InterruptedException
     */
    public void emmitMessage(String message) throws InterruptedException {
        if(activeEmission)
            destinationBuffer.putMessageActive(message);
        else
            destinationBuffer.putMessagePassive(message);

        if(debugMode){
            System.out.println(String.format("%s Message emmited at process 1: %s", colour, message));
        }
    }

    /**
     * The run method that the thread will execute
     */
    @Override
    public void run(){
        try {
            //This sends all the messages including FIN
        	sendMessage();

            //The process is set to receive all the messages it sent including the FIN
            for(int i = 0; i < wordListSend.size() + 1; i++){
                receiveMessage();//gets the message out of the buffer and adds to the received list
                processMessage();//does the respective wait times specified for sleep, it doesnt sleep when wait is done
            }

            //Responsible for printing the results of re received messages
            printResults();

            //Store results in outputFiles
            writeResultsToFile();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------------------------------------
    // GETTERS,SETTERS AND SUPPORT METHODS
    //-------------------------------------------------------------------------------------------------

    public void writeResultsToFile(){
        Date currDate = new Date();
        String fileName = String.format("results-%d",currDate.getTime());//gets date time in miliseconds
        createNewResultsFile(fileName);
        writeToNewResultsFile(fileName);
    }

    public void createNewResultsFile(String fileName){
        try {
            String fileRoute = String.format("Caso1/outputFiles/%s.txt", fileName);
            File resultFile = new File(fileRoute);
            if (resultFile.createNewFile()) {
                System.out.println("You can see the results in the following file in the outputfiles folder: " + resultFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToNewResultsFile(String fileName){
        try {
            //GET FILE
            String fileRoute = String.format("Caso1/outputFiles/%s.txt", fileName);
            FileWriter fileWriter = new FileWriter(fileRoute);

            fileWriter.write("\n");
            fileWriter.write("CONFIGURATION");
            fileWriter.write("\n");

            //WRITE CONFIGURATION
            System.out.println("These are the parameters for the buffers and producer consumers");
            for(int i = 0; i<rawInput.size();i++){
                fileWriter.write(rawInput.get(i) + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write("WRITE SENT MESSAGES");
            fileWriter.write("\n");

            //WRITE SENT MESSAGES
            System.out.println("These are the messages that were sent");
            for(int i = 0; i<wordListSend.size();i++){
                fileWriter.write(String.format("Original Message %d: %s \n", i + 1, wordListSend.get(i) ));
            }

            fileWriter.write("\n");
            fileWriter.write("RESULTS");
            fileWriter.write("\n");

            //WRITE RESULT
            for(int i = 0 ; i < wordListReceived.size();i++){
                fileWriter.write(String.format( "Message %d : %s \n", i + 1 ,wordListReceived.get(i)));
            }

            fileWriter.write("\n");

            //CLOSE FILE
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printResults(){
        System.out.println("");
        System.out.println( ANSI_RESET + "EXECUTION RESULTS");
        System.out.println("");
        System.out.println("These are the messages received , in order:");
        for (int i = 0 ; i < wordListReceived.size();i++){
            String formattedOutput = String.format("Message %d conent: %s",i+1,wordListReceived.get(i));
            System.out.println(formattedOutput);
        }

    }

    public String formatMessage(){
        String processId = Long.toString(getPcId());
        String receptionType = (activeReception)?"A":"P";//A is for active reception, P is for passive reception
        String emissionType = (activeEmission)?"A":"P";//A is for active emission, P is for passive emission

        String newMessage = currentMessage + " " + String.format("%s%s%s", processId, receptionType, emissionType);
        return newMessage;
    }

    public Buffer getOriginBuffer() {
        return originBuffer;
    }

    public void setOriginBuffer(Buffer originBuffer) {
        this.originBuffer = originBuffer;
    }

    public Buffer getDestinationBuffer() {
        return destinationBuffer;
    }

    public void setDestinationBuffer(Buffer destinationBuffer) {
        this.destinationBuffer = destinationBuffer;
    }


    public long getPcId() {
        return pcId;
    }

    public void setPcId(long pcId) {
        this.pcId = pcId;
    }

    public boolean isActiveReception() {
        return activeReception;
    }

    public void setActiveReception(boolean activeReception) {
        this.activeReception = activeReception;
    }

    public boolean isActiveEmission() {
        return activeEmission;
    }

    public void setActiveEmission(boolean activeEmission) {
        this.activeEmission = activeEmission;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    //-------------------------------------------------------------------------------------------------
    // PRETTY COLOURS
    //-------------------------------------------------------------------------------------------------

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
}


