INVOICE RECONCILER - ROADMAP DEL PROYECTO
===============================================

Objetivo
--------
Construir un módulo de ERP que reciba una nota de pedido y una factura,
extraiga/normalice sus datos y detecte diferencias de productos, cantidades,
precios y totales.

Principio del proyecto
-----------------------
No se comparará texto crudo de documentos.

Nota de pedido PDF / factura foto
        -> extracción de texto u OCR
        -> interpretación y normalización
        -> estructuras comunes de datos
        -> motor de conciliación
        -> diferencias y estado de revisión


ETAPA 1 - NÚCLEO DEL NEGOCIO
----------------------------
Objetivo:
Crear la lógica de comparación en Java puro, sin base de datos, sin API,
sin OCR y sin AWS.

Entidades iniciales:
- PurchaseOrderLine
- InvoiceLine
- Discrepancy
- DiscrepancyType
- InvoiceReconciliationService

Reglas iniciales:
- Detectar cantidad distinta.
- Detectar precio unitario distinto.
- Detectar producto pedido que falta en la factura.
- Detectar producto facturado que no estaba en el pedido.
- Usar BigDecimal para importes.

Pruebas:
- Pedido y factura iguales -> no hay diferencias.
- Cantidad distinta -> QUANTITY_MISMATCH.
- Precio distinto -> UNIT_PRICE_MISMATCH.
- Producto faltante -> MISSING_FROM_INVOICE.
- Producto extra -> UNEXPECTED_IN_INVOICE.

Criterio de terminado:
El motor de conciliación funciona con tests automatizados y todos pasan.

Commits sugeridos:
- feat: add reconciliation domain models
- test: add quantity mismatch test
- feat: detect quantity mismatches
- test: add unit price mismatch test
- feat: detect unit price mismatches


ETAPA 2 - API REST Y POSTGRESQL
-------------------------------
Objetivo:
Exponer el sistema como API y persistir pedidos, facturas y conciliaciones.

Tecnologías:
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker Compose

Módulos:
- PurchaseOrder
- PurchaseOrderLine
- Invoice
- InvoiceLine
- Reconciliation
- Discrepancy

Endpoints iniciales:
- CRUD de notas de pedido.
- CRUD de facturas cargadas manualmente.
- Ejecutar conciliación entre una factura y una nota de pedido.
- Consultar resultado y diferencias.

Criterio de terminado:
Una nota y una factura se pueden crear mediante API, guardar en PostgreSQL
y conciliarse correctamente.


ETAPA 3 - CARGA Y ALMACENAMIENTO DE DOCUMENTOS
-----------------------------------------------
Objetivo:
Permitir cargar PDFs de notas de pedido e imágenes/PDF de facturas.

Primer alcance:
- Endpoint de carga de archivos.
- Validar tipo y tamaño de archivo.
- Guardar documentos localmente.
- Asociar documento a nota de pedido o factura.
- Estados de procesamiento:
  PENDING, PROCESSING, PROCESSED, FAILED.

Más adelante:
- Reemplazar el almacenamiento local por Amazon S3.

Criterio de terminado:
El sistema puede guardar y recuperar el documento asociado a una factura
o nota de pedido.


ETAPA 4 - EXTRACCIÓN, OCR Y AWS
-------------------------------
Objetivo:
Obtener datos estructurados desde documentos reales.

Flujo:
- PDF con texto -> extracción directa.
- PDF escaneado o imagen -> OCR.
- OCR -> normalización -> Invoice / PurchaseOrder.

Diseño escalable:
- DocumentStorage
  - LocalDocumentStorage
  - S3DocumentStorage

- InvoiceExtractor
  - FakeInvoiceExtractor
  - TextractInvoiceExtractor
  - SupplierSpecificInvoiceParser

Tecnologías previstas:
- Amazon S3
- Amazon Textract
- AWS SDK for Java v2

Reglas importantes:
- No subir facturas reales de la empresa a GitHub.
- Usar archivos ficticios o anonimizados.
- No guardar claves AWS en el repositorio.
- Usar variables de entorno y perfiles.

Criterio de terminado:
Se carga una imagen de factura, se extraen sus líneas y se concilia contra
una nota de pedido guardada.


ETAPA 5 - FRONTEND Y REVISIÓN OPERATIVA
----------------------------------------
Objetivo:
Crear una interfaz para que una persona revise resultados y corrija errores.

Tecnologías:
- React
- TypeScript

Pantallas:
- Lista de notas de pedido.
- Lista de facturas.
- Carga de documento.
- Detalle de conciliación.
- Tabla de diferencias.
- Corrección manual de datos extraídos.
- Filtros por proveedor, fecha y estado.

Estados posibles:
- APPROVED
- REQUIRES_REVIEW
- REJECTED

Criterio de terminado:
Un usuario puede cargar documentos, revisar diferencias y dejar una
conciliación aprobada o pendiente de revisión.


ETAPA 6 - CALIDAD, SEGURIDAD Y PORTFOLIO
----------------------------------------
Objetivo:
Dejar el proyecto presentable para GitHub y entrevistas.

Agregar:
- Spring Security + JWT.
- Roles: ADMIN, OPERATOR, REVIEWER.
- Swagger / OpenAPI.
- Tests de integración.
- Testcontainers.
- GitHub Actions.
- Docker Compose completo.
- Logging y manejo global de errores.
- README con arquitectura, capturas y guía de instalación.
- Colección Bruno o Postman.
- Datos de ejemplo anonimizados.

Criterio de terminado:
Cualquier persona puede clonar el repositorio, levantarlo con Docker,
ejecutar tests y entender el flujo desde el README.


ORDEN DE TRABAJO ACTUAL
-----------------------
[EN CURSO] ETAPA 1 - Núcleo del negocio

No avanzar a base de datos, OCR o AWS hasta tener:
- Modelo de dominio claro.
- Motor de comparación funcionando.
- Tests verdes.
