package communications;

import java.util.ArrayList;
import java.util.HashMap;

public class ProducerConsumer  extends Thread {
    //-------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------------------------------

    /*
    debug mode
     */
    private boolean debugMode;

    /*
    colors
     */
    private HashMap<Long,String> colors = new HashMap<Long, String>();

    /*
    The buffer from which the message is received by the producer consumer
     */
    private Buffer originBuffer;

    /*
    The buffer at which the message is being sent to by the producer consumer
     */
    private Buffer destinationBuffer;

    /*
    The current message being held by the producer consumer
     */
    private String currentMessage;

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


    //-------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------------------------------

    /**
     * Creates a producer consumer instance
     * @param originBuffer the buffer from which the PC will be receiving the message
     * @param destinationBuffer the buffer to which the PC will be sending the message
     * @param currentMessage the message that the PC is currently processing
     * @param pcId the id of the PC
     * @param activeReception if the communication type for reception of messages from the origin buffer is active, if it's not its passive
     * @param activeEmission if the communication type for emission of messages from the origin buffer is active, if it's not its passive
     * @param sleepTime the time in milliseconds that the thread is sent to sleep when it's "processing" the message before sending it
     */
    public ProducerConsumer(Buffer originBuffer, Buffer destinationBuffer, String currentMessage, long pcId, boolean activeReception, boolean activeEmission, int sleepTime, boolean debugMode) {
        this.originBuffer = originBuffer;
        this.destinationBuffer = destinationBuffer;
        this.currentMessage = currentMessage;
        this.pcId = pcId;
        this.activeReception = activeReception;
        this.activeEmission = activeEmission;
        this.sleepTime = sleepTime;
        this.debugMode = debugMode;

        //Assigns colour
        colors.put(2L,ANSI_BLUE);
        colors.put(3L,ANSI_GREEN);
        colors.put(4L,ANSI_YELLOW);
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
    public void receiveMessage() throws InterruptedException {
        currentMessage = (activeReception) ? originBuffer.popMessageActive() : originBuffer.popMessagePassive();

        if(debugMode){
            System.out.println(String.format("%s Message received from process %d: %s",colors.get(getPcId()), getPcId(), currentMessage));
        }
    }

    /**
     * The message is modified to add a registry that it has passed by this particular process
     * with a particular set of protocols.Additionally, the thread simulates a processing time
     * by sending the thread to sleep by a determined time length.
     * @throws InterruptedException 
     */
    public void processMessage() throws InterruptedException {
        //In this case the process message won't do anything if the message content is FIN
        if(!currentMessage.equalsIgnoreCase("FIN")) {
                Thread.sleep(getSleepTime());
                currentMessage = formatMessage();//modifies the currentMessage string
        }

        if(debugMode){
            System.out.println(String.format("%s Message processed at process %d : %s",colors.get(getPcId()), getPcId() , currentMessage));
        }
    }

    /**
     * The message is emitted to the destination buffer after its been modified and the needed time has passed.
     * The synchronization is handled by the buffers.
     * @throws InterruptedException
     */
    public void emmitMessage() throws InterruptedException {
        if(activeEmission)
            destinationBuffer.putMessageActive(currentMessage);
        else
            destinationBuffer.putMessagePassive(currentMessage);

        if(debugMode){
            System.out.println(String.format("%s Message emmited at process %d : %s",colors.get(getPcId()), getPcId(), currentMessage));
        }
    }

    /**
     * The run method that the thread will execute
     */
    public void run(){
        try {
            //Untill weve updated current message to fin were going to keep receiving messages,
            //Once fin message is received the while is executed one last time and its then exited and the whole execution stops,
            while(!currentMessage.equals("FIN")){
                receiveMessage();
                processMessage();
                emmitMessage();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------------------------------------
    // GETTERS,SETTERS AND SUPPORT METHODS
    //-------------------------------------------------------------------------------------------------

    public String formatMessage(){

        //TODO: Ask about the specific nomenclature for the message
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

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
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
