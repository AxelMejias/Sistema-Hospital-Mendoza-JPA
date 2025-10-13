package org.example;

import org.h2.tools.Server;

/**
 * Utilitario para iniciar H2 Console y ver la base de datos
 * Ejecuta este main para acceder a la consola web en http://localhost:8082
 */
public class H2ConsoleStarter {
    
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Iniciando H2 Console...");
            
            // Iniciar H2 Web Console en puerto 8082
            Server webServer = Server.createWebServer(
                "-web", 
                "-webAllowOthers", 
                "-webPort", "8082"
            ).start();
            
            System.out.println("✅ H2 Console iniciado exitosamente!");
            System.out.println("🌐 Accede en tu navegador: http://localhost:8082");
            System.out.println();
            System.out.println("📋 CONFIGURACION DE CONEXION:");
            System.out.println("   JDBC URL: jdbc:h2:file:./data/hospital-db");
            System.out.println("   User Name: sa");
            System.out.println("   Password: (dejar vacio)");
            System.out.println();
            System.out.println("⚠️  IMPORTANTE: Ejecuta primero 'gradlew run' para crear la base de datos");
            System.out.println("💡 Presiona CTRL+C para detener el servidor");
            
            // Mantener el servidor corriendo
            System.out.println("\n🔄 Servidor H2 Console ejecutándose...");
            Thread.currentThread().join(); // Esperar indefinidamente
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar H2 Console: " + e.getMessage());
            e.printStackTrace();
        }
    }
}