
    public class ProducerConsumer extends Thread
{
	/**
	 * The buffer from which the message is recieved from
	 */
	private Buffer bfMessenger;

	/**
	 * The buffer to which the message is sent to
	 */
	private Buffer bfReceiver;

	/**
	 * The message that is being colected and sent
	 */
	private String message;

	/**
	 * Id of the thread responsible for transporting the message between buffers
	 */
	private int id;

	/**
	 * Boolean that represents what form of comunication is the thread suposed to use. If true its active comunication, else its pasive.
	 */
	private boolean activeCommunication;

	/**
	 * The time in miliseconds that the thread is sent to sleep when procesing the transport of the message between buffers
	 */
	private int sleepTime;



	//-------------------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------------------

	/**
	 * It creates a new producer consumer object that is responsible for the reception and
	 * transmision of messages through the network of buffers
	 * @param id the id of the current thread (it goes from 1 to 4)
	 * @param bfMessenger the messenger buffer, from where the message is recieved by the producerConsumer
	 * @param bfReceiver the reciever buffer, where the producerConsumer will send the message once its proccesed
	 * @param message the message that is going to be transmited by the thread from one buffer to the other
	 * @param sleepTime the time that the thread will be sent to sleep while it "processes" the message transmition from buffer to buffer
	 * @param activeCommunication it specifies if the current thread type of comunication. If true its active comunicatio, if false its pasive comunication.
	 */
	public ProducerConsumer( int id,Buffer bfMessenger,Buffer bfReceiver,String message, int sleepTime,boolean activeCommunication)
	{
		this.message=message;
		this.bfReceiver=bfReceiver;
		this.bfMessenger=bfMessenger;
		this.id=id;
		this.sleepTime = sleepTime;
		this.activeCommunication = activeCommunication;
	}

	//-------------------------------------------------------------------------------------------------
	// SETTERS AND GETTERS
	//-------------------------------------------------------------------------------------------------

	public Buffer getBfMessenger() {
		return bfMessenger;
	}

	public void setBfMessenger(Buffer bfMessenger) {
		this.bfMessenger = bfMessenger;
	}

	public Buffer getBfReciever() {
		return bfReceiver;
	}

	public void setBfReciever(Buffer bfReceiver) {
		this.bfReceiver = bfReceiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public boolean isActiveComunication() {
		return activeCommunication;
	}

	public void setActiveCommunication(boolean activeCommunication) {
		this.activeCommunication = activeCommunication;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}


	//-------------------------------------------------------------------------------------------------
	// METHODS
	//-------------------------------------------------------------------------------------------------

	/**
	 * Processes a message recieved from the messenger buffer, once on is available to be colected,
	 * then the thread will sleep for the specified time and then it will proceed to send the message to the next buffer if possible
	 */
	public synchronized void process_message() throws InterruptedException {
		//Retrieves the message from the messenger buffer
		String messageC=bfMessenger.retrieveMessages();

		//In the case that this is the last message then the thread final message will be finalized
		//TODO: Revisar que dice exactamente que hage el enunciado respecto a esto
		// DEFINIR Y PENSAR COMO Y CUANDO SE DEBE PONER PASIVO Y ACTIVO   3P-2A-hola
		if (messageC !="FIN")
		{
			String marcador= Integer.toString(id);
			messageC=marcador+"AP"+"-"+messageC;
		}

		//Thread sleep
		Thread.sleep(sleepTime);

		//Sends modified message to the new
		bfReceiver.insertMessage(messageC);
	}
	
	

	
	
	
	
	
	
}


