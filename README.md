# crypto-taxes

Crypto-taxes es una herramienta para ayudarte a calcular los impuestos que debes pagar por tus operaciones de criptomonedas en la declaración de la Renta.

> [!IMPORTANT]
> **DISCLAIMER**: Este programa se ofrece **TAL Y COMO ESTÁ**, no se ofrece ningún tipo de garantía ni responsabilidad.

# Limitaciones

El programa actualmente únicamente está preparado para recoger la información de Bitstamp exportada en CSV. Desconozco otros formatos de otras empresas y no es mi intención adaptarlo pero se admiten sugerencias (con ejemplos) y ayuda.

El programa está adaptado a presentar la declaración de la Renta en España. En concreto se ha preparado para la campaña de 2024.

# Uso

TODO

Utilizando el jar, sólo hay que ejecutar este comando desde una consola (Linux, Windows o Mac):
`java -jar cryptotax.jar <csv_file>`

Es necesario ejecutar el programa y pasarle como único argumento la ruta del CSV original. El programa generará otros tres ficheros:

- *\<original\>-simple-taxes.csv*: Es el que más te importará, aparece la información justa y necesaria para rellenar la información de la declaración de la Renta. También incluye el campo de beneficios, para que compruebes que los datos en el borrador son correctos (pueden variar un céntimo arriba o abajo por los redondeos).
- *\<original\>-taxes.csv*: Parecido al anterior pero contiene más información de las operaciones y no está redondeado.
- *\<original\>-calc.csv*: Puedes ignorarlo. Simplemente añade un campo más para indicar cuál es el beneficio de cada operación descontando las tasas pagadas. Las operaciones de compra son siempre negativas por el valor de las tasas. Aparece también un campo de taxes con ciertos valores. Indican la cantidad de operaciones de impuestos que genera cada una separadas por | (puede haber más de una).

> [!WARNING]
> Es necesario que aparezcan todas las operaciones desde una situación inicial "cero". Es decir, desde que abriste la cuenta, desde que vendiste todas tus monedas o, al menos, una situación en la que tuvieses exactamente las mismas monedas que al fin del periodo que quieras calcular. Esto es porque las operaciones se calculan en modo FIFO: para relacionar una venta con su/s compra/s es necesario conocer la situación inicial. NO SIRVE contar únicamente desde principio de año si ya tenías monedas compradas, porque el importe de compra debe descontarse del importe de venta.

# Cómo funciona

1. Primero se lee el CSV original entero para poder operar con él.
2. Se recorre cada operación
	2.1 Por operación de compra se guardan sus datos por moneda, como si fuera una hucha, en orden.
	2.2. Para cada operación de venta, se van descontando los importes de la hucha:
		2.2.1. Si la compra asociada es mayor que la venta, se descuenta de la operación de compra y se genera un registro de impuestos.
		2.2.2. Si la compra asociada es menor que la de venta, se elimina, se genera un registro de impuestos y se pasa a la siguiente operación de compra, así hasta cubrir el importe de la venta.
3. Finalmente, los registros de impuestos se devuelven en dos ficheros, uno simplificado con los datos a rellenar en las casillas de la renta y otro más completo por si quieres analizar las operaciones.

Todas las operaciones matemáticas se realizan con objetos BigDecimal de Java, que aportan mayor exactitud que los float o double.
En el fichero simplificado, se redondean los importes (siguiendo las reglas generales de HALF_UP), lo que puede llevar a algún desvío en el cálculo de las operaciones, tanto por defecto como por exceso. El desvío es aleatorio (para una muestra aleatoria de operaciones, si se realiza siempre la misma operación con los mismo importes podría acumularse un error) y más o menos acabará cuadrando con el total.

# Historia

Al hacer la declaración vi varias noticias sobre que desde 2023 es obligatorio declarar las operaciones. 
Además, hacía poco había recibido un correo de la empresa con la que opero diciendo que para simplificar el pago de impuestos, nos iban a facilitar una exportación de los datos. Sin ello veía imposible hacerlo pero ahora ya no tenía excusa para cumplir con mis obligaciones.

Lo descargué, jugue un poco con ellos en Excel y por fin me metí en el borrador para ver los datos que pedían... eran casi imposibles de calcular. Se pide declarar el importe y tasas de compra y el importe y tasas de venta de cada operación. Para eso necesitas relacionar ventas con compras. En mi caso tenía varias operaciones de distintos importes que, obviamente, no cuadraban. Además, existe una regla para hacerlo, la regla FIFO (First In, First Out). Es decir, no puedes elegir a que compra la relacionas, tienes que empezar por la primera... a descargar el listado de operaciones enteras, de todos los años que tengo la cuenta.

Primero intenté alguna cosa con fórmulas en Excel y es "imposible" (sin recurrir a VB, al menos para mí), así que decidí hacer un programa para ello. Creo que desde Hacienda deberían simplificar este proceso. Lo que yo he hecho no está al alcance de cualquier ciudadano e intentar hacerlo a mano es una tarea muy tediosa y propensa a errores. No me extraña que la mayoría de los que se deciden a pagar acaben abandonándolo por imposible, exponiéndose a multas cuando las empresas empiecen a pasar los datos a Hacienda (lo cuál veo muy factible en los próximos 5 años, al menos para ciertos operadores, especialmente europeos, como es mi caso).

Conclusión: Hice el programa y presenté la declaración de 2024. Creedme que me aseguré mucho de que los cálculos fueran correctos, yo era el primer usuario y me afectaba directamente.

Aún así, yo me he basado en mis propios datos, es posible que existan otras situaciones. Revisad los resultados y comprobad que os cuadra. Los más importante es que la situación inicial se haga sin monedas (desde que abristeis la cuenta) para que incluya la primera compra. Si vendéis todo, es como un resteo, desde ahí también vale.

Espero que esta herramienta pueda ayudar a alguien que quiera pagar los impuestos y no quiera volverse loco para calcular todos los datos que piden.


#Other languages

This is a tool for calculating taxes adapted to Bitstamp and Spanish tax schema. I don't know if this could be used in other countries, but I bet it could at least be useful as cryptocurrency taxes are based in European directives. Feel free to fork to adapt it or propose improvements.
