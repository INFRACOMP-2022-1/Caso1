
    public class ProducerConsumer extends Thread
{
	private Buffer bfmensajero;
	private Buffer bfreceptor;
	private String mensaje;
	private int ID;

	
	
	
	public ProducerConsumer( int ID,Buffer bfmensajero,Buffer bfreceptor,String mensaje) 
	{
		this.mensaje=mensaje;
		this.bfreceptor=bfreceptor;
		this.bfmensajero=bfmensajero;
		this.ID=ID;
	
	
	}
	
	
	public synchronized void procesar_mensaje() 
	{
		String mensajeC=bfmensajero.retrieveMessages();
		
		if (mensajeC !="FIN")
		{
			String marcador= Integer.toString(ID);
			// DEFINIR Y PENSAR COMO Y CUANDO SE DEBE PONER PASIVO Y ACTIVO   3P-2A-hola
			
			mensajeC=marcador+"AP"+"-"+mensajeC;
			
		}
		
		
		
		bfreceptor.insertMessage(mensajeC);
	
	}
	
	

	
	
	
	
	
	
}


