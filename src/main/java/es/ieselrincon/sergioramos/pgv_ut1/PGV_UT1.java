/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package es.ieselrincon.sergioramos.pgv_ut1;

import java.io.File;
import java.io.IOException;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        
        PGV_UT1 objRepaso = new PGV_UT1();
        
        // Descomenta la línea de la actividad que deseas probar
        //objRepaso.a1_1();
        //objRepaso.a1_2();
        //objRepaso.a1_3();
        objRepaso.a1_4();

    }
}
