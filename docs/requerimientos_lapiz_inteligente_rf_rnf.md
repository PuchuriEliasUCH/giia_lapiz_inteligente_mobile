# Requerimientos Funcionales, No Funcionales y Validaciones - Lápiz Inteligente

**Proyecto:** Lápiz Inteligente  
**Tipo de producto:** Sistema IoT educativo con analítica de datos, machine learning e interfaz Android  
**Versión:** 1.1  
**Fecha:** 2026-07-07

---

## 1. Requerimientos Funcionales

### 1.1 Autenticación y gestión de tutores

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-01 | M | El sistema debe permitir el registro, consulta y actualización del perfil del tutor mediante nombre, apellido, correo electrónico, contraseña y teléfono opcional. | El tutor puede crear su cuenta, consultar su perfil y actualizar datos permitidos sin exponer la contraseña. |
| RF-02 | M | El sistema debe permitir el inicio de sesión del tutor mediante correo electrónico y contraseña. | Al iniciar sesión correctamente, el sistema devuelve un token de autenticación vigente. |
| RF-03 | M | El sistema debe proteger las funcionalidades privadas mediante un mecanismo de autenticación basado en token. | Las funcionalidades privadas se acceden mediante una sesión autenticada. |

### 1.2 Gestión de alumnos

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-04 | M | El sistema debe permitir al tutor gestionar sus alumnos mediante registro, listado, consulta de detalle, actualización y desactivación lógica sin eliminar historial. | El tutor puede realizar la gestión de alumnos desde su cuenta. |
| RF-05 | M | El registro del alumno debe permitir nombre, fecha de nacimiento o edad, mano dominante, grado escolar y notas opcionales. | La ficha del alumno almacena los datos definidos para seguimiento educativo. |
| RF-06 | M | La aplicación Android debe operar bajo control del tutor, sin perfil operativo para el alumno. | El alumno usa físicamente el lápiz, pero no controla actividades desde la aplicación. |

### 1.3 Gestión de ejercicios

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-07 | M | El sistema debe incluir el catálogo base de 7 ejercicios: líneas verticales, líneas horizontales, curvas simples, ondas, círculos, espirales y arcos. | La API y la app muestran el catálogo de ejercicios del MVP. |
| RF-08 | M | El sistema debe permitir gestionar ejercicios mediante registro, listado, consulta de detalle, actualización, activación y desactivación lógica sin eliminar historial. | El administrador técnico puede mantener el catálogo y el tutor puede consultar ejercicios. |
| RF-09 | M | Cada ejercicio debe tener nombre, descripción, tipo de trazo, estado activo y patrón esperado. | El detalle del ejercicio expone información suficiente para iniciar y evaluar una sesión. |
| RF-10 | S | El sistema debe permitir asociar umbrales, características esperadas o referencia de evaluación a cada ejercicio. | Las reglas y el modelo pueden identificar el ejercicio evaluado y sus parámetros. |

### 1.4 Gestión de lápices inteligentes

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-11 | M | El sistema debe permitir gestionar lápices inteligentes mediante registro, listado, consulta de estado, actualización de datos operativos y cambio de estado. | El administrador técnico puede mantener lápices y el tutor puede consultar lápices disponibles. |
| RF-12 | M | Cada lápiz debe registrarse mediante un identificador único de dispositivo. | El identificador del dispositivo queda asociado al lápiz registrado. |
| RF-13 | M | El sistema debe mantener estado operativo del lápiz: disponible, en uso, inactivo, en mantenimiento o perdido. | La API devuelve el estado actualizado de cada lápiz. |
| RF-14 | M | El sistema debe registrar la última actividad conocida de cada lápiz. | Cada lectura o mensaje de estado actualiza la marca temporal correspondiente. |
| RF-15 | M | Los lápices deben tratarse como recursos compartidos del laboratorio o institución, no como propiedad fija de un tutor. | Los lápices se administran como recursos compartidos. |

### 1.5 Gestión de sesiones de escritura

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-16 | M | El tutor debe poder crear una sesión asociando un alumno, un ejercicio y un lápiz inteligente. | La sesión se crea en estado activo con fecha/hora de inicio. |
| RF-17 | M | El backend debe mantener la asociación activa entre sesión, alumno, ejercicio y lápiz. | La sesión conserva la relación entre sus entidades principales. |
| RF-18 | M | El sistema debe permitir consultar sesiones mediante detalle de sesión e historial paginado por alumno. | La consulta retorna resultados ordenados y paginados. |
| RF-19 | M | El tutor debe poder cerrar manualmente una sesión desde la aplicación Android. | La sesión queda finalizada con fecha/hora de cierre y motivo manual. |
| RF-20 | M | El sistema debe cerrar automáticamente sesiones abandonadas cuando no reciba actividad durante un tiempo configurable. | La sesión queda finalizada con motivo de cierre por inactividad. |
| RF-21 | M | El sistema debe registrar motivo de cierre de sesión. | Todo registro de sesión cerrada incluye motivo de cierre. |
| RF-22 | S | El sistema debe permitir consultar una sesión activa desde Android para recuperar el estado si la aplicación se reconecta. | La app vuelve a mostrar estado, lápiz, ejercicio y alertas disponibles de la sesión. |

### 1.6 Ingesta de datos IoT del lápiz

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-23 | M | El sistema debe recibir lecturas del lápiz mediante un contrato IoT documentado, preferentemente MQTT para el backend. | Existen tópico, payload, frecuencia y credenciales documentados. |
| RF-24 | M | El publicador de lecturas debe ser el lápiz con conectividad directa o la aplicación Android actuando como puente, según arquitectura aprobada. | Las lecturas llegan al backend sin que la aplicación altere los valores crudos. |
| RF-25 | M | Cada lectura debe incluir timestamp, aceleración en tres ejes, giroscopio en tres ejes y presión del trazo. | El payload contiene `ts`, `ax`, `ay`, `az`, `gx`, `gy`, `gz` y `fsr` o equivalentes aprobados. |
| RF-26 | M | El sistema debe asociar cada lectura recibida a la sesión activa del lápiz correspondiente. | La trazabilidad sesión-lápiz-lectura queda registrada. |
| RF-27 | M | El sistema debe mantener lecturas crudas de sesiones activas en un buffer temporal durante la ejecución. | La sesión acumula lecturas sin insertar cada muestra en la base relacional. |
| RF-28 | M | Al finalizar la sesión, el sistema debe persistir las lecturas crudas en archivo de dataset con identificador de sesión y metadatos. | Se genera un archivo recuperable y vinculado a la sesión. |
| RF-29 | M | El sistema debe almacenar las lecturas crudas fuera del modelo de inserción individual en la base de datos relacional. | La base relacional almacena métricas, metadatos y rutas/referencias al dataset. |
| RF-30 | S | El sistema debe registrar eventos de conectividad, pérdida de lecturas y reconexión. | Los eventos quedan disponibles para auditoría técnica. |

### 1.7 Análisis en tiempo real y alertas

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-31 | M | El sistema debe evaluar lecturas durante una sesión activa mediante reglas educativas de tiempo real. | Se generan alertas ante presión excesiva, presión insuficiente o movimiento inestable. |
| RF-32 | M | El sistema debe enviar alertas en tiempo real a la aplicación Android del tutor conectado a la sesión. | La alerta aparece en la sesión correspondiente. |
| RF-33 | S | Las alertas deben incluir tipo, severidad, mensaje educativo, timestamp y lectura o ventana asociada. | La app puede ordenar y representar visualmente las alertas. |
| RF-34 | S | Las reglas de tiempo real deben ser configurables por ejercicio o versión de configuración. | Un cambio de umbral queda versionado y documentado. |

### 1.8 Machine learning e IA con Random Forest

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-35 | M | El sistema debe extraer características desde las lecturas crudas para análisis de escritura. | Se calculan características de presión, estabilidad, movimiento, variación y duración por sesión o ventana. |
| RF-36 | M | El sistema debe soportar un modelo de clasificación basado en Random Forest para detectar trazos o patrones fuera de lo establecido. | Una sesión o ventana puede recibir clasificación, score/confianza y versión de modelo. |
| RF-37 | M | El modelo Random Forest debe estar versionado con fecha, dataset usado, variables de entrada y métricas de evaluación. | El sistema conserva metadatos de cada versión del modelo. |
| RF-38 | M | El resultado del modelo debe almacenarse como parte de los resultados de la sesión. | La consulta de sesión finalizada muestra clasificación, score y versión del modelo cuando aplique. |
| RF-39 | M | El sistema debe diferenciar entre alertas educativas de reglas y resultados de inferencia ML. | La interfaz y la API identifican el origen de cada resultado. |
| RF-40 | S | El sistema debe permitir gestionar versiones del modelo Random Forest mediante registro, consulta, activación y retiro de versiones obsoletas. | La versión activa queda registrada y se usa en nuevas inferencias. |
| RF-41 | S | El sistema debe permitir recalcular métricas o inferencias desde lecturas crudas conservadas. | Un reproceso reproduce o actualiza resultados dejando trazabilidad. |

### 1.9 Cálculo de métricas de sesión

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-42 | M | Al finalizar una sesión, el sistema debe calcular métricas resumen a partir de las lecturas crudas registradas. | Las métricas se guardan en la sesión finalizada. |
| RF-43 | M | El sistema debe calcular presión promedio, presión máxima, estabilidad de presión, estabilidad de movimiento, indicador de temblor o variación motriz y conteo de eventos relevantes. | Las métricas aparecen en el resultado de sesión y sus fórmulas quedan documentadas. |
| RF-44 | S | El sistema debe calcular un indicador de postura o inclinación cuando los sensores disponibles lo permitan. | El resultado se almacena cuando existan datos suficientes. |
| RF-45 | M | El sistema debe conservar trazabilidad entre métricas calculadas, lecturas crudas, ejercicio, alumno, lápiz y modelo usado. | Es posible ubicar el archivo de lecturas y metadatos desde la sesión. |

### 1.10 Interfaz Android para tutor

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-46 | M | La aplicación Android debe permitir al tutor autenticarse, gestionar alumnos propios, seleccionar alumno, ejercicio y lápiz disponible, iniciar sesiones y cerrar sesiones. | El tutor completa el flujo principal desde Android. |
| RF-47 | M | La aplicación Android debe mostrar estado de conexión del lápiz y de la sesión. | El tutor distingue conectado, desconectado, en uso, inactivo y finalizado. |
| RF-48 | M | La aplicación Android debe mostrar alertas en tiempo real durante la sesión. | Las alertas aparecen sin recargar manualmente la pantalla. |
| RF-49 | M | La aplicación Android debe mostrar métricas visuales de la sesión finalizada e historial por alumno. | El tutor visualiza fecha, ejercicio, presión, estabilidad, movimiento, eventos y resultado IA cuando aplique. |
| RF-50 | S | La aplicación Android debe presentar gráficos simples para comparar sesiones de un alumno. | El tutor puede visualizar evolución por fecha o ejercicio. |

### 1.11 Retroalimentación física del lápiz

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-51 | S | El lápiz inteligente debe poder emitir retroalimentación física mediante vibración o señal visual. | El lápiz responde ante eventos críticos definidos. |
| RF-52 | S | El sistema debe permitir enviar comandos de retroalimentación al lápiz mediante el canal IoT aprobado. | El comando se publica mediante el canal definido. |
| RF-53 | S | El lápiz debe poder mantener reglas locales mínimas para eventos críticos si la red no está disponible. | La retroalimentación básica no depende totalmente del backend. |
| RF-54 | C | El tutor debe poder activar o desactivar retroalimentación física para una sesión cuando el protocolo educativo lo permita. | La configuración queda registrada en la sesión. |

### 1.12 Administración técnica y documentación operativa

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RF-55 | S | El administrador técnico debe poder gestionar de forma centralizada lápices, catálogo de ejercicios, configuración de reglas y versión activa del modelo. | Las operaciones administrativas agrupan altas, consultas, actualizaciones, activaciones y desactivaciones correspondientes. |
| RF-56 | M | El sistema debe exponer documentación de API, contratos IoT, payloads, tópicos y errores. | Los equipos Android, backend y firmware pueden integrarse usando documentación vigente. |
| RF-57 | M | El sistema debe registrar eventos relevantes de autenticación, sesiones, dispositivos, ingesta y modelos. | Los eventos se pueden consultar para soporte técnico y auditoría. |

---

## 2. Requerimientos No Funcionales

### 2.1 Seguridad y privacidad

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-01 | M | Las contraseñas deben almacenarse con hash seguro y nunca en texto plano. | La base de datos no contiene contraseñas legibles. |
| RNF-02 | M | Los tokens de autenticación deben tener expiración configurable. | Un token expirado no permite acceder a recursos protegidos. |
| RNF-03 | M | Todo endpoint protegido debe validar autenticación y autorización por propietario o rol. | Las pruebas cubren acceso no autorizado entre tutores. |
| RNF-04 | M | Las comunicaciones fuera de una red local controlada deben usar cifrado adecuado. | API, WebSocket y MQTT seguro usan TLS cuando se despliegan fuera de laboratorio. |
| RNF-05 | M | El broker o canal IoT debe autenticar dispositivos o publicadores autorizados. | Un dispositivo no autorizado no puede publicar lecturas aceptadas. |
| RNF-06 | M | El canal IoT debe aplicar autorización por tópico, dispositivo o sesión. | Un lápiz no publica ni recibe mensajes de otro lápiz. |
| RNF-07 | M | Los mensajes de error no deben exponer detalles internos del sistema. | Las respuestas no muestran stack traces, secretos ni consultas internas. |
| RNF-08 | M | Los datos de alumnos deben tratarse como información sensible de menores. | El acceso queda limitado, auditado y documentado. |
| RNF-09 | M | El sistema debe registrar eventos de seguridad relevantes. | Intentos fallidos, tokens inválidos y publicaciones no autorizadas quedan registrados. |

### 2.2 Rendimiento y capacidad

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-10 | M | El MVP debe soportar al menos 5 sesiones simultáneas a una frecuencia objetivo de 50 Hz por lápiz en entorno de laboratorio. | Una prueba de carga demuestra ingesta y procesamiento sin caída del servicio. |
| RNF-11 | M | La latencia entre recepción de lectura y generación de alerta debe ser menor o igual a 500 ms en red local para el percentil 95. | La medición de prueba cumple el umbral definido. |
| RNF-12 | M | La API debe mantener tiempos de respuesta adecuados durante la ingesta continua. | Los endpoints principales responden en menos de 1 s p95 en prueba MVP. |
| RNF-13 | M | Las lecturas crudas deben procesarse en memoria y persistirse por lote o archivo al cierre de sesión. | No se generan inserciones individuales masivas por muestra en la base relacional. |
| RNF-14 | S | La arquitectura debe permitir incrementar sesiones simultáneas sin rediseñar el flujo principal. | Se puede escalar backend, broker o almacenamiento mediante configuración/despliegue modular. |

### 2.3 Disponibilidad y tolerancia a fallos

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-15 | M | El sistema debe manejar desconexiones temporales del lápiz sin afectar otras sesiones activas. | Una desconexión se registra y no interrumpe otras sesiones. |
| RNF-16 | M | El sistema debe evitar que una sesión quede activa indefinidamente. | El watchdog o proceso equivalente cierra sesiones por inactividad. |
| RNF-17 | M | El sistema debe preservar lecturas acumuladas ante cierre controlado. | Los datos recibidos antes del cierre quedan persistidos. |
| RNF-18 | S | El sistema debe manejar fallas temporales del broker o canal IoT mediante reconexión o recuperación definida. | La reconexión queda registrada y documentada. |
| RNF-19 | S | La aplicación Android debe informar pérdida de conexión del lápiz o backend. | El tutor visualiza estado y recomendación operativa. |

### 2.4 Usabilidad

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-20 | M | La interfaz Android debe estar en español claro y orientado a tutores. | Los textos son comprensibles y no técnicos salvo pantallas administrativas. |
| RNF-21 | M | El flujo para iniciar sesión de escritura debe ser guiado. | El tutor selecciona alumno, ejercicio y lápiz antes del inicio sin pasos ambiguos. |
| RNF-22 | M | Los mensajes al usuario deben ser educativos, consistentes y no clínicos. | No se usan términos como diagnóstico, enfermedad, tratamiento o conclusión clínica. |
| RNF-23 | S | Las visualizaciones deben diferenciar alertas, métricas y resultados IA. | El tutor identifica qué dato es métrica, alerta o clasificación. |

### 2.5 Mantenibilidad y portabilidad

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-24 | M | El backend debe organizarse por módulos funcionales y separar rutas, servicios, modelos y esquemas. | La estructura del código permite ubicar lógica de negocio fuera de controladores. |
| RNF-25 | M | Los cambios de base de datos deben gestionarse mediante migraciones controladas. | Cada cambio estructural tiene migración y documentación. |
| RNF-26 | M | El sistema debe ejecutarse en un entorno reproducible mediante contenedores para el backend y servicios de soporte. | Un despliegue local se levanta con configuración documentada. |
| RNF-27 | M | Los contratos entre Android, backend, firmware y broker deben documentarse antes de integración. | API, tópicos, payloads y errores están versionados. |
| RNF-28 | S | La arquitectura debe permitir agregar sensores o nuevas características sin reescribir el flujo principal de ingesta. | Los cambios se incorporan mediante contratos y módulos extendibles. |

### 2.6 Calidad de datos e IA

| ID | Prioridad | Requerimiento | Criterio de aceptación |
|---|---|---|---|
| RNF-29 | M | El dataset debe conservar metadatos mínimos: sesión, alumno anonimizado o seudonimizado, ejercicio, lápiz, fecha, firmware, modelo y versión de contrato. | Cada archivo de lecturas puede rastrearse sin exponer datos innecesarios. |
| RNF-30 | M | Las métricas deben poder reproducirse desde las lecturas crudas almacenadas. | Un reproceso obtiene resultados equivalentes o explica diferencias por versión. |
| RNF-31 | M | El modelo Random Forest debe contar con evaluación documentada sobre datos representativos antes de usarse como aprobado. | Se documentan métricas de evaluación, matriz de confusión y limitaciones. |
| RNF-32 | M | Las salidas IA deben incluir advertencia de apoyo educativo y limitación no clínica. | Los resultados no se presentan como diagnóstico. |
| RNF-33 | S | El sistema debe registrar drift, baja confianza o datos insuficientes cuando el modelo no pueda inferir confiablemente. | La interfaz muestra resultado no concluyente educativo cuando corresponda. |

---

## 3. Validaciones del Sistema

Las validaciones no se consideran requerimientos funcionales independientes. En este documento se registran como condiciones obligatorias de aceptación, control y prueba asociadas a los requerimientos que correspondan.

### 3.1 Autenticación y tutores

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-AUT-01 | RF-01 | El sistema debe rechazar el registro de tutores con correo electrónico duplicado. |
| VAL-AUT-02 | RF-01 | El sistema debe validar que los campos obligatorios del tutor estén completos y tengan formato válido. |
| VAL-AUT-03 | RF-02 | El sistema debe rechazar inicios de sesión con credenciales inválidas, usuario inexistente o usuario inactivo. |
| VAL-AUT-04 | RF-03 | El sistema debe rechazar solicitudes a funcionalidades protegidas cuando el token esté ausente, expirado o sea inválido. |
| VAL-AUT-05 | RF-03 | El sistema debe impedir que un usuario inactivo acceda a funcionalidades protegidas. |

### 3.2 Alumnos

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-ALU-01 | RF-04 | El sistema debe permitir gestionar únicamente alumnos asociados al tutor autenticado. |
| VAL-ALU-02 | RF-04 | El sistema debe conservar el historial de sesiones cuando un alumno sea desactivado. |
| VAL-ALU-03 | RF-05 | El sistema debe validar que el alumno esté dentro del rango objetivo de 5 a 7 años o tenga justificación registrada de excepción. |
| VAL-ALU-04 | RF-05 | El sistema debe validar valores permitidos para mano dominante. |
| VAL-ALU-05 | RF-06 | La aplicación Android no debe exponer al alumno funciones de autenticación, selección de ejercicio, inicio de actividad ni cierre de sesión. |

### 3.3 Ejercicios

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-EJE-01 | RF-07 | El catálogo activo del MVP debe contener los 7 ejercicios base. |
| VAL-EJE-02 | RF-08 | El sistema debe impedir que la desactivación o modificación de ejercicios elimine historial de sesiones. |
| VAL-EJE-03 | RF-09 | Todo ejercicio debe estar asociado a un tipo de trazo válido. |
| VAL-EJE-04 | RF-16 | El sistema debe permitir iniciar sesiones únicamente con ejercicios existentes y activos. |

### 3.4 Lápices inteligentes

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-LAP-01 | RF-12 | El sistema debe rechazar lápices con identificador de dispositivo duplicado. |
| VAL-LAP-02 | RF-13 | El sistema debe permitir iniciar sesiones únicamente con lápices en estado disponible. |
| VAL-LAP-03 | RF-13 | El sistema debe impedir el uso de lápices inactivos, perdidos, en mantenimiento o en uso. |
| VAL-LAP-04 | RF-16 | El sistema debe impedir que un mismo lápiz tenga más de una sesión activa simultánea. |
| VAL-LAP-05 | RF-14 | El sistema debe actualizar la última actividad conocida cuando recibe lecturas o mensajes de estado del lápiz. |

### 3.5 Sesiones

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-SES-01 | RF-16 | El sistema debe crear sesiones solo cuando el alumno pertenezca al tutor autenticado. |
| VAL-SES-02 | RF-16 | El sistema debe crear sesiones solo cuando el ejercicio exista y esté activo. |
| VAL-SES-03 | RF-16 | El sistema debe crear sesiones solo cuando el lápiz exista y esté disponible. |
| VAL-SES-04 | RF-18 | El sistema debe permitir consultar únicamente sesiones asociadas a alumnos del tutor autenticado. |
| VAL-SES-05 | RF-19 | El sistema debe impedir cerrar sesiones ya finalizadas. |
| VAL-SES-06 | RF-21 | Toda sesión cerrada debe tener motivo de cierre registrado. |

### 3.6 Ingesta IoT

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-IOT-01 | RF-23 | El sistema debe procesar solo mensajes recibidos por tópicos, canales o rutas aprobadas en el contrato IoT. |
| VAL-IOT-02 | RF-25 | El sistema debe validar estructura, tipos y rangos esperados de cada lectura antes de procesarla. |
| VAL-IOT-03 | RF-25 | El sistema debe descartar lecturas malformadas sin interrumpir el procesamiento general del servicio. |
| VAL-IOT-04 | RF-26 | El sistema debe rechazar o ignorar lecturas de dispositivos inexistentes, inactivos o no autorizados. |
| VAL-IOT-05 | RF-26 | El sistema debe rechazar o ignorar lecturas de dispositivos sin sesión activa asociada. |
| VAL-IOT-06 | RF-28 | El archivo de lecturas crudas debe quedar vinculado al identificador de sesión y sus metadatos. |

### 3.7 Alertas en tiempo real

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-ALT-01 | RF-32 | El canal de tiempo real debe autenticar al tutor conectado. |
| VAL-ALT-02 | RF-32 | El canal de tiempo real debe validar que la sesión solicitada pertenezca a un alumno del tutor autenticado. |
| VAL-ALT-03 | RF-32 | El sistema debe enviar alertas únicamente a clientes asociados a la sesión correspondiente. |
| VAL-ALT-04 | RF-32 | El sistema debe evitar difundir alertas de una sesión a tutores no asociados. |

### 3.8 Machine learning e IA

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-IA-01 | RF-37 | Ninguna versión del modelo Random Forest debe marcarse como aprobada sin registro de validación. |
| VAL-IA-02 | RF-37 | Toda versión del modelo debe registrar dataset usado, variables de entrada, fecha y métricas de evaluación. |
| VAL-IA-03 | RF-38 | El sistema debe registrar versión de modelo y score/confianza cuando almacene resultados de inferencia. |
| VAL-IA-04 | RF-41 | El reproceso debe dejar trazabilidad de versión de métricas, versión de modelo y fecha de ejecución. |
| VAL-IA-05 | RF-36 | El sistema debe identificar datos insuficientes o baja confianza antes de presentar una inferencia como resultado educativo. |

### 3.9 Métricas, Android y lenguaje educativo

| ID | Requerimiento relacionado | Validación |
|---|---|---|
| VAL-MET-01 | RF-42 | Las métricas deben calcularse a partir de lecturas crudas asociadas a la sesión. |
| VAL-MET-02 | RF-43 | Las fórmulas de métricas deben estar documentadas y versionadas. |
| VAL-MET-03 | RF-45 | Debe existir trazabilidad entre sesión, alumno, ejercicio, lápiz, lecturas crudas, métricas e inferencia. |
| VAL-AND-01 | RF-46 | La aplicación Android debe permitir iniciar una sesión solo después de seleccionar alumno, ejercicio y lápiz. |
| VAL-AND-02 | RF-49 | La aplicación Android debe mostrar resultados únicamente de alumnos asociados al tutor autenticado. |
| VAL-AND-03 | RF-46 a RF-50 | La aplicación Android no debe usar lenguaje médico, diagnóstico ni clínico en pantallas, alertas o resultados. |
