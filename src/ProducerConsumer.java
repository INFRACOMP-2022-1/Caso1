/**
 * Class that represents the entity of a producer consumer thread, which is in charge of transporting a message from
 * a messenger buffer to a receiver buffer whilst doing a modification and obeying the type of transmission specified
 * by parameter
 */
public class ProducerConsumer extends Thread
{
	/**
	 * The buffer from which the message is received from
	 */
	private Buffer bfMessenger;

	/**
	 * The buffer to which the message is sent to
	 */
	private Buffer bfReceiver;

	/**
	 * The message that is being collected and sent
	 */
	private String message;

	/**
	 * Id of the thread responsible for transporting the message between buffers
	 */
	private int id;

	/**
	 * Boolean that represents what form of communication is used to receive the message from the messenger buffer. If true its active, else its passive
	 */
	private boolean senderIsActiveCommunication;

	/**
	 * Boolean that represents what form of communication is used to send the message to the receiver buffer. If true its active ,else its passive
	 */
	private boolean receiverIsActiveCommunication;

	/**
	 * The time in milliseconds that the thread is sent to sleep when processing the transport of the message between buffers
	 */
	private int sleepTime;



	//-------------------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------------------

	/**
	 * It creates a new producer consumer object that is responsible for the reception and
	 * transmission of messages through the network of buffers
	 * @param id the id of the current thread (it goes from 1 to 4)
	 * @param bfMessenger the messenger buffer, from where the message is received by the producerConsumer
	 * @param bfReceiver the receiver buffer, where the producerConsumer will send the message once its processed
	 * @param message the message that is going to be transmitted by the thread from one buffer to the other
	 * @param sleepTime the time that the thread will be sent to sleep while it "processes" the message transmission from buffer to buffer
	 * @param senderIsActiveCommunication it specifies if the current thread the communication type when receiving a message from the sender buffer. If true its active, else its passive.
	 * @param receiverIsActiveCommunication it specifies the current thread the communication type when sending a message to the receiver buffer. If ture its active, else its passive.
	 */
	public ProducerConsumer( int id,Buffer bfMessenger,Buffer bfReceiver,String message, int sleepTime,boolean senderIsActiveCommunication,boolean receiverIsActiveCommunication)
	{
		this.message=message;
		this.bfReceiver=bfReceiver;
		this.bfMessenger=bfMessenger;
		this.id=id;
		this.sleepTime = sleepTime;
		this.senderIsActiveCommunication = senderIsActiveCommunication;
		this.receiverIsActiveCommunication = receiverIsActiveCommunication;
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

	public Buffer getBfReceiver() {
		return bfReceiver;
	}

	public void setBfReceiver(Buffer bfReceiver) {
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

	public void setId(int id) {
		this.id = id;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public boolean isSenderIsActiveCommunication() {
		return senderIsActiveCommunication;
	}

	public void setSenderIsActiveCommunication(boolean senderIsActiveCommunication) {
		this.senderIsActiveCommunication = senderIsActiveCommunication;
	}

	public boolean isReceiverIsActiveCommunication() {
		return receiverIsActiveCommunication;
	}

	public void setReceiverIsActiveCommunication(boolean receiverIsActiveCommunication) {
		this.receiverIsActiveCommunication = receiverIsActiveCommunication;
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
			//TODO: Append message Cuando un proceso recibe un mensaje, le
			//agrega un texto para indicar que el mensaje pasó por ahí. El texto debe incluir el identificador del proceso y una
			//marca para identificar el tipo de envío y recepción que realiza el proceso. Por ejemplo “1AS” sería un texto que podría
			//indicar que el proceso 1 recibió el mensaje de forma PASIVA y lo transmitió de forma ACTIVA
			String marcador= Integer.toString(id);
			messageC=marcador+"AP"+"-"+messageC;
		}

		//Thread sleep
		Thread.sleep(sleepTime);

		//Sends modified message to the new
		bfReceiver.insertMessage(messageC);
	}
	
	

	
	
	
	
	
	
}


