# JavaGlaspyramid

Programmet räknar ut tiden (i sekunder) som det tar för ett givet glas i en glaspyramid att svämma över.
Antalet glas per rad i pyramiden ökar med 1 för varje rad och bildar på det sättet en pyramid.

Programmet frågar efter (1) vilken rad glaset finns på och (2) vilket glas räknat från vänster på den
angivna raden som glaset står.

Programmet skapar en graf av noder (nodes) som utgör glasen och länkar (edges) som innehåller flödet mellan två
specifika glas. För att räkna ut tiden det tar att fylla ett specifikt glas simulerar programmet att vätskan
fylls på uppifrån (till första glaset) med en hastighet på 0,4 dl/s. Därefter fylls underliggande glas på i en
takt av totalflödet till respektive glas.
