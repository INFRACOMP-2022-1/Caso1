
    public class ProducerConsumer extends Thread
{
	//The buffer from which the message is recieved from
	private Buffer bfMessenger;

	//The buffer to which the message is sent to 
	private Buffer bfReciever;

	//The message that is being colected and sent
	private String message;

	//Id of the thread responsible for transporting the message between buffers
	private int ID;

	//Boolean that represents what form of comunication is the thread suposed to use. If true its active comunication, else its pasive.
	private boolean activeComunication;

	//The time in miliseconds that the thread is sent to sleep when procesing the transport of the message between buffers
	private int sleepTime;

	
	
	
	public ProducerConsumer( int ID,Buffer bfMessenger,Buffer bfReciever,String message, int sleepTime,boolean comunicacionActiva) 
	{
		this.message=message;
		this.bfReciever=bfReciever;
		this.bfMessenger=bfMessenger;
		this.ID=ID;
		this.sleepTime = sleepTime;
		this.comunicacionActiva = comunicacionActiva;
	}
	
	
	public synchronized void procesar_message() 
	{
		String messageC=bfMessenger.retrieveMessages();
		
		if (messageC !="FIN")
		{
			String marcador= Integer.toString(ID);
			// DEFINIR Y PENSAR COMO Y CUANDO SE DEBE PONER PASIVO Y ACTIVO   3P-2A-hola
			
			messageC=marcador+"AP"+"-"+messageC;
			
		}
		
		
		
		bfReciever.insertMessage(messageC);
	
	}
	
	

	
	
	
	
	
	
}


