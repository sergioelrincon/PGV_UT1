/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package es.ieselrincon.sergioramos.pgv_ut1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author sergioramos
 */
public class PGV_UT1 {
    String classpathDescargaFichero = "/Users/sergioramos/Downloads/repasojava";
    String claseDescargaFichero = "DescargaFichero";
    String rutaLog = "/Users/sergioramos/Downloads/";
    
    /**
     * Necesitas descargar 10 ficheros de diferentes servidores y en lugar de descargarlos secuencialmente vamos a implementar un programa en Java que cree,
     * en un bucle, 10 procesos que ejecuten la descarga de forma concurrente. 
     * La URL de la descarga se la debes pasar a la aplicación como argumento.
     * 
     * OJO: Al ejecutar este método no veremos ninguna salida por pantalla, ya que no estamos accediendo la salida del proceso hijo. Por lo tanto, es correcto
     * que ejecutemos el método y no veamos nada por la consola
     * 
     * @throws IOException 
     */
    public void a1_1() throws IOException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[10];
        Process arrayP[] = new Process[10]; 
        
        for(int i=0; i<10; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");        
            arrayP[i] = arrayPB[i].start();
        }        
    }
    
    /**
     * Modifica el programa (1.1) para que, tanto los mensajes de éxito como los de error se muestren por pantalla.
     * 
     * OJO: No se visualiza la salida de todos los hijos, ya que no esperamos a que finalice su ejecución
     * 
     * @throws IOException 
     */
    public void a1_2() throws IOException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[10];
        Process arrayP[] = new Process[10]; 
        
        for(int i=0; i<10; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");        
            arrayPB[i].inheritIO();
            arrayP[i] = arrayPB[i].start();
        }        
    }    

    /**
     * Modifica el programa (1.1) para que los mensajes de éxito se muestren por pantalla y los mensajes de error se almacenen en un fichero 
     * de forma que se mantenga el histórico de mensajes.
     * 
     * OJO: Los mensajes de error se siguen añadiendo al log aunque termine el proceso padre, porque los hijos se siguen ejecutando.
     * 
     * @throws IOException 
     */
    public void a1_3() throws IOException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[10];
        Process arrayP[] = new Process[10]; 
        
        for(int i=0; i<10; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");        
            arrayPB[i].redirectOutput(ProcessBuilder.Redirect.INHERIT); 
            arrayPB[i].redirectError(ProcessBuilder.Redirect.appendTo(new File(rutaLog + "errorHistorico.log")));
            arrayP[i] = arrayPB[i].start();
        }        
    }    

    
    /**
     * Modifica el programa (1.3) para que los mensajes de éxito y de error se redirijan a dos ficheros independientes de forma que cada ejecución reemplace 
     * el contenido anterior de dichos ficheros.
     * 
     * OJO: Después de que el padre finalice, se siguen generando los ficheros de log a medida que los hijos van generando salidas
     * 
     * @throws IOException 
     */
    public void a1_4() throws IOException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[10];
        Process arrayP[] = new Process[10]; 
        
        for(int i=0; i<10; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");        
            
            // Creamos un fichero por cada hilo para poder acceder a la salida de todos los hijos
            arrayPB[i].redirectOutput(new File(rutaLog + "salidaHijo" + Integer.toString(i) + ".log"));  
            arrayPB[i].redirectError(new File(rutaLog + "errorHijo" + Integer.toString(i) + ".log"));
            arrayP[i] = arrayPB[i].start();
        }        
    }   
    
    /**
     * Implementa un programa que lance un proceso hijo que ejecute el programa DescargaFichero (pasándole una URL aleatoria a través de un argumento) 
     * de forma que si tarda más de 5 segundos en descargar la URL, mate a dicho proceso y muestre un mensaje de error por la salida estándar.
     * 
     * @throws IOException 
     */
    public void a2_1() throws IOException, InterruptedException {
        ProcessBuilder PB = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero.jpg");
        PB.inheritIO();
        Process P = PB.start();
        
        /**
         * waitFor espera 5 segundos o hasta que termine el proceso antes de los 5 segundos. Devuelve TRUE si el proceso ha terminado por sí mismo antes 
         * de los 5 segundos y FALSE en caso contrario. En este último caso entra en el IF para destruir el proceso
         */
        if (!P.waitFor(5, TimeUnit.SECONDS)) {
            P.destroy();
            System.out.println("Matamos al proceso porque no terminó transcurridos 5 segundos");
        }
        else {
            System.out.println("El proceso finalizó sin neceidad de matarlo");
        }
         
    }
    
    /**
     * Implementa un programa que lance 5 procesos hijo (espaciados entre ellos por 1 segundo) que ejecuten el programa DescargaFichero 
     * (pasándole una URL aleatoria a través de un argumento). Después de ejecutar el último de los procesos debe consultar el estado de 
     * todos sus hijos y todos los que quede en ejecución los debe matar, informando de ello mediante un mensaje en pantalla.
     * 
     * @throws IOException
     * @throws InterruptedException 
     */
    public void a2_2() throws IOException, InterruptedException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[5];
        Process arrayP[] = new Process[5]; 
        
        // Creamos los 5 procesos
        for(int i=0; i<5; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");
            arrayPB[i].inheritIO();
            arrayP[i] = arrayPB[i].start();
            TimeUnit.SECONDS.sleep(1);
        }    
        
        /**
         * Después de crear todos los procesos, averiguamos cuáles siguen vivos para matarlos. 
         * En la mayoría de las ocasiones todos los procesos seguirán vivos, pero ejecútalo varias veces para que veas que no siempre sucede
         * Si aumentamos el tiempo de espera del buble anterior es más probable que los procesos finalicen antes de llegar a este punto
         */
        for(int i=0; i<5; i++) {
            if (arrayP[i].isAlive()) {
                arrayP[i].destroy();
                System.out.println("Hemos tenido que matar al proceso " + Integer.toString(i) + " porque seguía ejecutándose");
            }
        }            
    }
    
    /**
     * Implementa un programa que lance 5 procesos hijo que ejecuten el programa DescargaFichero (pasándole una URL aleatoria a través de un argumento) 
     * de forma que no comience la ejecución del siguiente proceso hijo hasta que no haya finalizado el anterior.
     * 
     * @throws InterruptedException
     * @throws IOException 
     */
    public void a2_3() throws InterruptedException, IOException {
        ProcessBuilder arrayPB[] = new ProcessBuilder[5];
        Process arrayP[] = new Process[5]; 
        
        // Creamos los 5 procesos de forma secuencial esperando por la finalización del anterior
        for(int i=0; i<5; i++) {
            arrayPB[i] = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/fichero" + Integer.toString(i) + ".jpg");
            arrayPB[i].inheritIO();
            arrayP[i] = arrayPB[i].start();
            arrayP[i].waitFor();
        }
    }
    
    /**
     * Crear un programa que ejecute el proceso hijo DescargaFichero de forma que, en lugar de pasarle la URL por un argumento, la obtenga del fichero "url.txt". 
     * Muestra la salida por pantalla
     */
    public void a3_1() throws IOException, InterruptedException {
        // Ruta donde está el fichero que contiene únicamente una URL
        String ficheroURL = "/Users/sergioramos/NetBeansProjects/PGV_UT1/src/main/java/es/ieselrincon/sergioramos/pgv_ut1/url.txt";
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero);  
        
        // La entrada del proceso hijo será el fichero que contiene la ruta
        pb.redirectInput(new File(ficheroURL));
        
        // La salida del proceso hijo será la misma que la del padre, para poder ver el mensaje por pantalla
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT); 
        
        // Lanzamos el proceso hijo y esperamos a que termine, para comprobar que se ha ejecutado tomando como entrada la ruta almacenada en el fichero
        Process p = pb.start();
        p.waitFor();
               
    }
    
    /**
     * Vamos a implementar un programa denominado "BuscayDescargaRecursosJava". Suponemos que está constantemente visitando portales donde localiza 
     * recursos relacionados con Java y cada vez que encuentra uno le pasa la URL al proceso hijo "DescargaFichero" para que lo descargue. 
     * En este caso no va a realizar ninguna búsqueda: solo va a generar URLs de forma aleatoria. Dichas URL se las va a pasar a través de Streams a 
     * la entrada estándar del proceso hijo "DescargaFichero". Los mensajes de éxito y de error los mostrará el proceso hijo por pantalla.
     */
    public void a4_1() throws IOException, InterruptedException {
        String URL;
        
        // En este caso no estamos constantemente descargando ficheros. Lo hacemos en 5 ocasiones
        for(int i=0; i<5; i++) {
            URL = "http://www.ieselrincon.org/" + UUID.randomUUID().toString() + ".pdf";    // Generamos una URL aleatoria
       
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero);
            
            // Mostramos la salida estándar y de error por pantalla
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process p = pb.start();

            // Escribimos en la entrada estándar del proceso hijo
            try ( OutputStream osp = p.getOutputStream(); // Obtenemos un stream de salida conectado con la entrada estándar del proceso hijo que acabamos de lanzar
                OutputStreamWriter oswp = new OutputStreamWriter(osp, "UTF-8")) {
                oswp.write(URL);                        // Escribimos en el stream anterior la URL generada 
            } 

            p.waitFor();
        }
    }
    
    /**
     * Modifica el programa (4.1) para que ambas salidas las redirija a un fichero.
     */
    public void a4_2() throws IOException, InterruptedException {
        String URL;
        
        // En este caso no estamos constantemente descargando ficheros. Lo hacemos en 5 ocasiones
        for(int i=0; i<5; i++) {
            URL = "http://www.ieselrincon.org/" + UUID.randomUUID().toString() + ".pdf";    // Generamos una URL aleatoria
       
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero);
            
            // Mostramos la salida estándar y de error por pantalla
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(rutaLog + "error_a42.log")));
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(rutaLog + "salida_a42.log")));

            Process p = pb.start();

            // Escribimos en la entrada estándar del proceso hijo
            try ( OutputStream osp = p.getOutputStream(); // Obtenemos un stream de salida conectado con la entrada estándar del proceso hijo que acabamos de lanzar
                OutputStreamWriter oswp = new OutputStreamWriter(osp, "UTF-8")) {
                oswp.write(URL);                        // Escribimos en el stream anterior la URL generada 
            } 

            p.waitFor();
        }        
    }
    
    /**
     * Implementa un programa que lance el proceso hijo "DescargaFichero" pasándole la URL mediante un argumento. 
     * En este caso, la salida estándar y de error del proceso hijo la va a leer el proceso padre y va a escribir por pantalla el mensaje 
     * "El proceso hijo se ha ejecutado exitosamente y nos ha devuelto este mensaje: <mensaje devuelto por el hijo>". 
     * En caso de error: "El proceso hijo ha generado un error y nos ha devuelto este mensaje: <mensaje devuelto por el hijo>".
     * 
     * @throws IOException
     * @throws InterruptedException 
     */
    public void a4_3() throws IOException, InterruptedException {        
        String linea;
        
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpathDescargaFichero, claseDescargaFichero, "http://www.ieselrincon.org/ficheroX.jpg");
        Process p = pb.start();
        
        // Esperamos a que finalice el proceso
        int codRet = p.waitFor();        
        System.out.println("La ejecución devuelve " + codRet);

        // En función de si el proceso se ha ejecutado o no correctamente, leemos una salida u otra (estándar o de error)
        if (codRet == 0) {
            try(InputStream is = p.getInputStream();            // Obtenemos un stream binario de ENTRADA conectado con la SALIDA estándar del proceso
            InputStreamReader isr = new InputStreamReader(is);  // Sobre el InputStream (stream binario) construimos un stream de texto
            BufferedReader br = new BufferedReader(isr))        // Sobre el InputStreamReader (stream de texto) construimos un stream con buffering para leer líneas enteras
            {
                while ((linea = br.readLine()) != null) {   // Mostramos cada una de las líneas de la salida estándar
                    System.out.println("El proceso hijo se ha ejecutado exitosamente y nos ha devuelto este mensaje: " + linea);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            } 
        }  
        else {
            try(InputStream isError = p.getErrorStream();            // Obtenemos un stream binario de ENTRADA conectado con la SALIDA de error del proceso
                InputStreamReader isrError = new InputStreamReader(isError);  // Sobre el InputStream (stream binario) construimos un stream de texto
                BufferedReader brError = new BufferedReader(isrError))
            {
                while ((linea = brError.readLine()) != null) {   // Mostramos cada una de las líneas de la salida estándar
                    System.out.println("El proceso hijo ha generado un error y nos ha devuelto este mensaje: " + linea);
                } 
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }            
         }        
       
               
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        
        PGV_UT1 objRepaso = new PGV_UT1();
        
        // Descomenta la línea de la actividad que deseas probar
        //objRepaso.a1_1();
        //objRepaso.a1_2();
        //objRepaso.a1_3();
        //objRepaso.a1_4();
        //objRepaso.a2_1();
        //objRepaso.a2_2();
        //objRepaso.a2_3();
        //objRepaso.a3_1();
        //objRepaso.a4_1();
        //objRepaso.a4_2();
        objRepaso.a4_3();
    }
    
}
