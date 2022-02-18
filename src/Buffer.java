import java.util.List;
import java.util.LinkedList;

public class Buffer {
    private List<String> buffer;//Contains the messages that are being sent
    private int size;//Capacidad del buffer

    public Buffer(int size){
        this.size = size;
        this.buffer = new LinkedList<>();
    }

    public synchronized boolean hasMessages(){
        return this.buffer.size() > 0;
    }

    public synchronized void insertMessage(String message){
        while(this.buffer.size() == this.size){
            try {
                wait();//protected wait
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Guardar mensaje
        this.buffer.add(message);

        //Notificar a los consumidores
        notify();
    }

    public synchronized  String retrieveMessages(){
        while(this.buffer.size() == 0){
            try{
                wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        //Retirar el mensaje
        String message = this.buffer.remove(0);
        notify();

        //Entregar
        return message;
    }
}
