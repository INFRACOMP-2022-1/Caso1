# Caso 1 
Caso 1 del curso de Infraestructura Computacional (ISIS2203), hecho en el primer semestre de 2022.
Autores: Santiago Vela, Verónica Escobar

## Objetivos del Projecto
Diseñar un mecanismo de comunicación para implementar la arquitectura descrita. Para este caso, los procesos serán
threads en la misma máquina (en realidad debería ser un sistema distribuido; este es solo un prototipo). La comunicación siempre pasa a través de los buzones. Por ejemplo, para comunicar el proceso 1 con el proceso 2, el proceso 1 debe almacenar el mensaje en el buzón A y el proceso 2 debe retirar el mensaje de ese mismo buzón.
El sistema recibe (por teclado) el número de mensajes a transmitir y una vez implementada la arquitectura, los
mensajes empiezan a circular desde el proceso 1 hasta regresar al origen. Cuando un proceso recibe un mensaje, le
agrega un texto para indicar que el mensaje pasó por ahí. El texto debe incluir el identificador del proceso y una
marca para identificar el tipo de envío y recepción que realiza el proceso. Por ejemplo “1AS” sería un texto que podría
indicar que el proceso 1 recibió el mensaje de forma PASIVA y lo transmitió de forma ACTIVA.
El proyecto debe ser realizado en java, usando threads. Para la sincronización solo pueden usar directamente las
funcionalidades básicas de Java: synchronized, wait, notify, notifyAll, sleep, yield, join y las
CyclicBarrier.

## Descripción
El projecto consiste en implementar una arquitectura de buzones y threads en el cual se evalua el uso y conocimiento de concurrencia.

## Datos adicionales
El reporte de este caso se puede encontrar en la ruta reporte-caso/ReporteCaso3.pdf
Si se quiere consultar el enunciado del caso este se puede encontrar en la ruta reporte-caso/Enunciado.pdf
