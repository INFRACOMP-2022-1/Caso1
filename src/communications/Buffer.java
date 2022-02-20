package communications;

import java.util.LinkedList;
import java.util.List;

public class Buffer {

    //-------------------------------------------------------------------------------------------------
    // ATTRIBUTES
    //-------------------------------------------------------------------------------------------------

    /*
    Maximum capacity of messages that the buffer can store at a given time
     */
    public int maxCapacity;

    /*
    List containing the message content of the buffer
     */
    private List<String> bufferContent;

    /*
    Buffer id string (A-D)
     */
    private String bufferId;

    //TODO: Remove this before submitting
    //public final Object monitor;//redundant I just need to synchronize over the objects linked list size (or the linked list itself)

    //-------------------------------------------------------------------------------------------------
    // CONSTRUCTOR
    //-------------------------------------------------------------------------------------------------

    /**
     * Creates a buffer with a set maximum capacity. Current capacity starts at zero elements.
     * @param bufferId the string id that identifies the buffer
     * @param maxCapacity the maximum capacity of messages that a given buffer can hold
     */
    public Buffer(String bufferId,int maxCapacity){
        this.bufferId = bufferId;
        this.maxCapacity = maxCapacity;
        this.bufferContent = new LinkedList<>();
        //this.monitor = new Object();//TODO: Remove this before submitting
    }

    //-------------------------------------------------------------------------------------------------
    // CORE METHODS
    //-------------------------------------------------------------------------------------------------

    //TODO: JUSTIFY THE NOTIFY -> Basically at any given time i think its imposible for both a putmessage and pop message to send the threads to the monitor this ensures that there is never a chance of a deadlock (both waitning to be woken up, if they are passive). Another thing is that as producer ocnsumer is a thread and a buffer concets two trheads at a time only this implies that notify is enough , because at any given time there can only be one thread in the monitors waiting list so it will wake up exactly the thread that we want.
    //TODO: LAST CHECKS ON SYNCHRONIZED

    //The communication type of the incoming producerConsumer is active -> yield()
    //The communication type of the incoming producerConsumer is passive -> wait()

    /**
     * Checks if a message given by parameter can be stored in the buffer,
     * if there is no space then the thread is yielded and sent back to the ready
     * to execute state until its able to store the message (this is determined by the OS Scheduler).
     * After finishing, it wakes up a single thread
     * @param message String containing the message to send
     * @throws InterruptedException exception
     */
    public synchronized void putMessageActive(String message) throws InterruptedException{
        while(getCurrentCapacity() == getMaxCapacity()){
            Thread.yield();
        }

        storeMessage(message); //Stores the received message in the buffer content linked list

        notify(); //notifies a random producerConsumer in monitor queue to wake up
    }

    /**
     * Checks if a message given by parameter can be stored in the buffer,if it is then it's added to the buffer content
     * if there is no space then the thread is put to sleep(wait) until it's woken up.
     * After finishing, it wakes up a single thread that might be waiting in the monitor.
     * @param message String containing the message to send
     * @throws InterruptedException exception
     */
    public synchronized void putMessagePassive(String message) throws  InterruptedException{
        while(getCurrentCapacity() == getMaxCapacity()){
            try {
                wait();// Makes the incoming message wait (passive) until someone wakes it up
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        storeMessage(message); //Stores the received message in the buffer content linked list

        notify(); //notifies a random producerConsumer in monitor queue to wake up
    }

    /**
     * Checks if there is a message in the buffer content available to be removed,
     * if there is then it's removed from the buffer content and passed on to whomever called the method,
     * if there is no message to be removed then the thread is yielded, and it will keep on checking werther
     * there is a message until one appears in the buffer. After finishing, it wakes up a single thread that
     * might be waiting in the monitor.
     * @return String containing the message that was removed from the buffer
     * @throws InterruptedException exception
     */
    //TODO: Whom ever calls pop message needs to do it in a synchronized block on the monitor object of buffer
    public synchronized String popMessageActive()throws InterruptedException{
        while(getCurrentCapacity() == 0){
            Thread.yield();
        }

        String message = sendMessage();

        notify();//As we don't know if the previous thread is passive or active we have to notify just in case it was passive and its in waiting mode

        return message;
    }

    /**
     * Checks if there is a message in the buffer content available to be removed,
     * if there is then it's removed from the buffer content and passed on to whomever called the method,
     * if there is no message to be removed then the thread is put to sleep(wait) until someone wakes it up.
     * After finishing, it wakes up a single thread that might be waiting in the monitor.
     * @return String containing the message that was removed from the buffer
     * @throws InterruptedException exception
     */
    public synchronized String popMessagePassive() throws InterruptedException{
        while(getCurrentCapacity() == 0){
            try {
                wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        String message = sendMessage();
        //As we don't know if the previous thread is passive or active we have to notify just in case it was passive and its in waiting mode
        notify();

        return message;
    }

    //-------------------------------------------------------------------------------------------------
    // SETTERS, GETTERS AND SUPPORT METHODS
    //-------------------------------------------------------------------------------------------------

    /**
     * This returns the current number of elements contained in the linked list.
     * Each instance of the class gets syncronized over the value of the current size of the linked list
     * @return int, the number of elements in the bufferContent linkedList
     */
    //TODO: REVISAR SI ENSERIO TOCA TENER ESTE SYNCRONIZED (O SI USO REFERENCIA DIRECTA DENTRO DE LOS CORE METHODS Y DEJO ESTE PARA QUE LO USEN PARA OTROS PROPOSITOS)
    public synchronized int getCurrentCapacity() {
        return getBufferContent().size();
    }

    /**
     * This method stores a message given by parameter in the bufferContent Linked List
     */
    public void storeMessage(String message){
        getBufferContent().add(message);
    }

    /**
     * This method sends a message in the index position 0
     * @return
     */
    public String sendMessage(){
        return getBufferContent().remove(0);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<String> getBufferContent() {
        return bufferContent;
    }

    public void setBufferContent(List<String> bufferContent) {
        this.bufferContent = bufferContent;
    }

    public String getBufferId() {
        return bufferId;
    }

    public void setBufferId(String bufferId) {
        this.bufferId = bufferId;
    }
}
