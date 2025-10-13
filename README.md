# 🏥 Sistema de Gestión Hospitalaria - Hospital Gral Mendoza

## 📋 Descripción
Sistema de gestión hospitalaria desarrollado en Java con JPA/Hibernate que permite administrar médicos, pacientes, departamentos, salas, historias clínicas y citas médicas. El sistema utiliza una base de datos H2 embebida y presenta una interfaz de consola profesional con tablas ASCII.

## 🚀 Tecnologías Utilizadas
- **Java 21 LTS** - Lenguaje de programación principal
- **JPA/Hibernate 6.4.4** - Framework de persistencia y mapeo objeto-relacional
- **H2 Database 2.2.224** - Base de datos embebida para desarrollo y pruebas
- **Lombok 1.18.38** - Reducción de código boilerplate con anotaciones
- **Gradle 8.10.2** - Herramienta de construcción y gestión de dependencias
- **Jakarta Persistence API 3.1.0** - Especificación estándar para JPA

## 📁 Estructura del Proyecto

```
ProyectoHospitalJpa/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── entidades/           # Entidades JPA del dominio
│   │   │   │   ├── Persona.java     # Clase abstracta base (@MappedSuperclass)
│   │   │   │   ├── Medico.java      # Hereda de Persona
│   │   │   │   ├── Paciente.java    # Hereda de Persona
│   │   │   │   ├── Hospital.java    # Aggregate Root del sistema
│   │   │   │   ├── Departamento.java
│   │   │   │   ├── Sala.java
│   │   │   │   ├── Cita.java
│   │   │   │   ├── HistoriaClinica.java
│   │   │   │   ├── Matricula.java   # Value Object (@Embeddable)
│   │   │   │   ├── EspecialidadMedica.java  # Enum
│   │   │   │   ├── EstadoCita.java  # Enum
│   │   │   │   └── TipoSangre.java  # Enum
│   │   │   ├── servicio/            # Capa de servicios de negocio
│   │   │   │   ├── CitaManager.java # Gestión de citas médicas
│   │   │   │   └── CitaException.java # Excepciones personalizadas
│   │   │   ├── Main.java            # Aplicación principal con demo
│   │   │   └── H2ConsoleStarter.java # Utilidad para H2 Console
│   │   └── resources/
│   │       └── META-INF/
│   │           └── persistence.xml  # Configuración JPA/Hibernate
│   └── test/
│       └── java/                    # Tests unitarios (pendiente)
├── data/                            # Base de datos H2 (generada automáticamente)
│   └── hospital-db.mv.db
├── build/                           # Archivos de construcción
├── gradle/                          # Wrapper de Gradle
├── build.gradle                     # Configuración de dependencias
├── gradlew                          # Script Gradle (Unix/Linux)
├── gradlew.bat                      # Script Gradle (Windows)
├── h2-console.jar                   # JAR de H2 Console (descargado)
└── README.md                        # Este archivo
```

## 🏗️ Arquitectura y Patrones de Diseño

### Patrones Implementados
- **Aggregate Root Pattern**: Hospital como raíz agregada
- **Value Object Pattern**: Matricula como objeto de valor embebido
- **Template Method Pattern**: Persona como clase abstracta
- **Builder Pattern**: Lombok @Builder y @SuperBuilder para construcción fluida

### Relaciones JPA
- **@OneToMany**: Hospital → Departamentos, Hospital → Pacientes
- **@ManyToOne**: Departamento → Hospital, Medico → Departamento
- **@OneToOne**: Paciente ↔ HistoriaClinica
- **@Embedded**: Medico contiene Matricula
- **@ElementCollection**: HistoriaClinica contiene listas de diagnósticos/alergias

## 💿 Base de Datos

### Modelo de Datos
El sistema gestiona las siguientes entidades principales:
- **Hospital**: Entidad raíz que contiene departamentos y pacientes
- **Departamentos**: Cardiología, Pediatría, Traumatología
- **Médicos**: Profesionales con matrícula y especialidad
- **Pacientes**: Personas con historia clínica y tipo de sangre
- **Salas**: Consultorios asignados por departamento
- **Citas**: Programación de atenciones médicas
- **Historias Clínicas**: Registro médico con diagnósticos y alergias

### Configuración H2
- **URL JDBC**: `jdbc:h2:file:./data/hospital-db`
- **Usuario**: `sa`
- **Contraseña**: (vacía)
- **Modo**: Archivo persistente con auto-creación de esquema

## 🛠️ Instalación y Configuración

### Prerrequisitos
- **Java 21 LTS** o superior
- **Git** para clonar el repositorio

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-repositorio>
   cd ProyectoHospitalJpa
   ```

2. **Compilar el proyecto**
   ```bash
   # En Windows
   .\gradlew.bat build
   
   # En Unix/Linux/Mac
   ./gradlew build
   ```

3. **Ejecutar la aplicación**
   ```bash
   # En Windows
   .\gradlew.bat run
   
   # En Unix/Linux/Mac
   ./gradlew run
   ```

## 🚀 Ejecución y Uso

### Ejecutar la Aplicación Principal
```bash
.\gradlew.bat run
```

La aplicación creará automáticamente:
- ✅ Hospital Gral Mendoza con 3 departamentos
- ✅ 3 médicos especialistas (Cardiología, Pediatría, Traumatología)
- ✅ 2 pacientes con historias clínicas
- ✅ 3 salas de consulta
- ✅ 3 citas médicas programadas

### Ver la Base de Datos con H2 Console

1. **Iniciar H2 Console** (en PowerShell independiente):
   ```powershell
   java -jar h2-console.jar -webPort 8090
   ```

2. **Conectar en el navegador**: http://localhost:8090
   - **JDBC URL**: `jdbc:h2:file:C:/ruta/completa/al/proyecto/data/hospital-db`
   - **User Name**: `sa`
   - **Password**: (vacío)

### Consultas SQL Útiles

```sql
-- Ver todos los médicos por especialidad
SELECT nombre, apellido, especialidad, numero_matricula 
FROM MEDICOS ORDER BY especialidad;

-- Ver departamentos con sus médicos
SELECT d.nombre as DEPARTAMENTO, m.nombre, m.apellido, m.especialidad 
FROM DEPARTAMENTOS d 
LEFT JOIN MEDICOS m ON d.id = m.departamento_id 
ORDER BY d.nombre;

-- Ver citas programadas
SELECT c.id, p.nombre as paciente, m.nombre as medico, 
       c.fecha_hora, c.estado, c.costo
FROM CITAS c
JOIN PACIENTES p ON c.paciente_id = p.id  
JOIN MEDICOS m ON c.medico_id = m.id
ORDER BY c.fecha_hora;

-- Ver pacientes con sus tipos de sangre
SELECT nombre, apellido, dni, tipo_sangre 
FROM PACIENTES;

-- Estadísticas generales
SELECT 'Médicos' as tipo, COUNT(*) as cantidad FROM MEDICOS
UNION ALL
SELECT 'Pacientes', COUNT(*) FROM PACIENTES
UNION ALL
SELECT 'Departamentos', COUNT(*) FROM DEPARTAMENTOS
UNION ALL
SELECT 'Salas', COUNT(*) FROM SALAS;
```

## 🎯 Funcionalidades Principales

### ✅ Gestión de Médicos
- Registro con matrícula profesional
- Asignación a departamentos por especialidad
- Validación de especialidades médicas

### ✅ Gestión de Pacientes
- Registro con historia clínica automática
- Clasificación por tipo de sangre
- Seguimiento de diagnósticos y alergias

### ✅ Programación de Citas
- Sistema de reservas por médico y sala
- Estados de cita (Programada, En Curso, Completada, Cancelada)
- Validación de disponibilidad

### ✅ Administración Hospitalaria
- Organización por departamentos y especialidades
- Gestión de salas y recursos
- Reportes y estadísticas

## 🔧 Configuración Avanzada

### Modificar Base de Datos
Editar `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
<!-- Opciones: create, create-drop, update, validate -->
```

### Agregar Nuevas Especialidades
En `EspecialidadMedica.java`:

```java
public enum EspecialidadMedica {
    // ... especialidades existentes
    NUEVA_ESPECIALIDAD("Descripción de la Nueva Especialidad");
}
```

## 📊 Salida del Sistema

Al ejecutar la aplicación, verás una presentación profesional como esta:

```
================================================================================
                    SISTEMA DE GESTION HOSPITALARIA
                         Hospital Gral Mendoza
================================================================================

+------------------------------------------------------------------------------+
| [HOSPITAL] HOSPITAL GRAL MENDOZA                                             |
| [DIRECCION] Av. San Martin 1234, Mendoza                                     |
| [TELEFONO] 0261-4567-8900                                                    |
+------------------------------------------------------------------------------+
| DEPARTAMENTO              | MEDICO                    | ESPECIALIDAD              |
+-------------------------+-------------------------+-------------------------+
| Cardiologia               | Dr. Luciano Mas           | Cardiologia               |
| Pediatria                 | Dr. Pipo Vernier          | Pediatria                 |
| Traumatologia             | Dr. Leandro Nunez         | Traumatologia             |
+------------------------------------------------------------------------------+

================================================================================
                   SISTEMA EJECUTADO EXITOSAMENTE
================================================================================
```

## 🧪 Testing

### Ejecutar Tests (Pendiente)
```bash
.\gradlew.bat test
```

### Cobertura de Código (Pendiente)
```bash
.\gradlew.bat jacocoTestReport
```

## 🐛 Solución de Problemas

### Error: "Table not found"
- Ejecuta primero `.\gradlew.bat run` para crear las tablas
- Verifica que H2 Console apunte al archivo correcto

### Error: "Port already in use"
- Cambia el puerto de H2 Console: `-webPort 8091`
- Mata procesos Java: `taskkill /f /im java.exe`

### Error de compilación Lombok
- Verifica que tu IDE tenga el plugin de Lombok instalado
- Rebuilda el proyecto: `.\gradlew.bat clean build`

## 📚 Documentación Técnica

### Dependencias Principales
```gradle
dependencies {
    compileOnly 'org.projectlombok:lombok:1.18.38'
    annotationProcessor 'org.projectlombok:lombok:1.18.38'
    implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    implementation 'org.hibernate.orm:hibernate-core:6.4.4.Final'
    runtimeOnly 'com.h2database:h2:2.2.224'
    implementation 'org.slf4j:slf4j-simple:2.0.9'
}
```

### Configuración JPA
- **Provider**: Hibernate 6.4.4.Final
- **Dialect**: H2Dialect (auto-detectado)
- **DDL**: Auto-update para desarrollo
- **SQL Logging**: Habilitado para depuración

## 👥 Contribución

### Estructura de Commits
- `feat:` Nueva funcionalidad
- `fix:` Corrección de errores
- `docs:` Documentación
- `refactor:` Refactorización de código
- `test:` Pruebas

### Codificación
- Java Coding Standards (Oracle)
- Nomenclatura en español para entidades de negocio
- Comentarios en español para mejor comprensión

## 📄 Licencia

Este proyecto es de uso educativo para el curso de Programación y Paradigmas Orientado a Objetos de la Universidad Tecnológica Nacional (UTN) - Argentina.

---

## 📞 Contacto y Soporte

**Proyecto desarrollado para:**
- **Institución**: Universidad Tecnológica Nacional (UTN) - Argentina
- **Materia**: Programación y Paradigmas Orientado a Objetos
- **Año**: 2025

**Tecnologías Clave**: Java 21, JPA/Hibernate, H2 Database, Lombok, Gradle

---

*¡Gracias por usar el Sistema de Gestión Hospitalaria Hospital Gral Mendoza! 🏥*