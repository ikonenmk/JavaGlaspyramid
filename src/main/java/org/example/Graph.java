package org.example;
import java.util.*;

/* Programmet räknar ut tiden (i sekunder) som det tar för ett givet glas i en glaspyramid att svämma över.
Antalet glas per rad i pyramiden ökar med 1 för varje rad och bildar på det sättet en pyramid.

Programmet frågar efter (1) vilken rad glaset finns på och (2) vilket glas räknat från vänster på den
angivna raden som glaset står.

Programmet skapar en graf av noder (nodes) som utgör glasen och länkar (edges) som innehåller flödet mellan två
specifika glas. För att räkna ut tiden det tar att fylla ett specifikt glas simulerar programmet att vätskan
fylls på uppifrån (till första glaset) med en hastighet på 0,4 dl/s. Därefter fylls underliggande glas på i en
takt av totalflödet till respektive glas.

Glas som fylls på läggs till i en LinkedList ("filling"). När ett glas är fullt raderas det från "filling" och
läggs till i en HashMap ("filled") med data för vilken tid (räknat från det att första glaset börjar fyllas)
som glaset är fullt. När det glas som användaren angett läggs till i "filled" stoppas påfyllningen av pyramiden
och programmet returnerar tiden det tog att fylla det givna glaset. */


public class Graph {

    // ArrayList där varje element är en rad i pyramiden och tillhörande glas (noder)
    private ArrayList<ArrayList<Node>> rows = new ArrayList<>();
    // LinkedList över länkar till intilliggande noder, lagrar också värde för flöden
    private LinkedList<Edge> edges = new LinkedList<>();
    // HashMap som innehåller data över fyllda glas (nodens nummer och tiden för full)
    private HashMap<Integer, Double> filled = new HashMap<>();
    // LinkedList som innehåller glas (noder) som fylls
    private LinkedList<Node> filling = new LinkedList<>();

    Graph(int numberOfRows, int glassToFill) {
        // Skapa första raden i pyramiden
        rows.add(new ArrayList<>());
        Node firstNode = new Node(1);
        rows.getFirst().add(firstNode);

        // Skapa resterande rader
        for (int i = 0; i < numberOfRows; i++) {
            int lastNode = rows.getLast().getLast().getNodeLabel(); // Hämta nummer för senast tillagda noden
            int nextNode = lastNode + 1; // Värde för första noden i raden
            rows.add(new ArrayList<>()); //lägg till ny rad i rows

            // Lägg till noder i nya raden
            int numberOfNodesToAdd = rows.size();
            for (int nodeNumber = 0; nodeNumber < numberOfNodesToAdd; nodeNumber++) {
                Node newNode = new Node(nextNode);
                rows.getLast().add(newNode);
                nextNode++;
            }

        }

        // Skapa länkar mellan skapade noder
        addEdge(rows);

        // Räkna ut tid för att fylla angivet glas
        int glassNumber = rows.get(numberOfRows-1).get(glassToFill-1).getNodeLabel();
        System.out.println("###################################################################");
        System.out.println("Räknar ut tiden det tar att fylla glas nummer: " + glassNumber);
        calculateTimeToFill(glassNumber);
    }

    /* Metod för att lägga till länkar mellan noderna */
    void addEdge(ArrayList<ArrayList<Node>> rows) {
        // Varje nod (förutom de på sista raden) har länkar till noderna [i+1] och [i+2] på raden under
        int rowsToAdd = rows.size() - 1;

        for (int i = 0; i < rowsToAdd; i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                Node node = rows.get(i).get(j);
                int firstAdjacentNode = rows.get(i + 1).get(j).getNodeLabel();
                int secondAdjacentNode = rows.get(i + 1).get(j + 1).getNodeLabel();
                // Skapa nya länkar
                Edge firstEdge = new Edge(node.getNodeLabel(), firstAdjacentNode, calculateFlow(node, rows.get(i), i));
                edges.add(firstEdge);
                Edge secondEdge = new Edge(node.getNodeLabel(), secondAdjacentNode, calculateFlow(node, rows.get(i), i));
                edges.add(secondEdge);
            }
        }
    }

    /* Metod för att räkna ut tiden (s) det tar att fylla ett visst glas */
    void calculateTimeToFill(int glassToFill) {
        filling.add(rows.getFirst().getFirst()); // Lägg till översta glaset i filling
        filling.getFirst().setFillSpeed(400); // Sätt fill speed för översta glaset till det kända värdet 400 ml/s (0,4 dl/s)
        double timeLapsed = 0.000;

        while(!filled.containsKey(glassToFill)) {
            timeLapsed += 0.001;
            LinkedList<Node> nextRowOfGlasses = new LinkedList<>();

            // Loopar igenom samtliga element i filling och fyller på glasen tills fullt
            Iterator<Node> iterator = filling.iterator();

            while(iterator.hasNext() && !filled.containsKey(glassToFill)) {
                Node node = iterator.next();
                node.setVolume(node.getVolume() + (node.getFillSpeed() / 1000));
                // Kontrollera om glaset är fullt, om fullt lägg till i filled
                if(node.getVolume() >= node.getMAX_VOLUME()) {
                    filled.put(node.getNodeLabel(), timeLapsed*10); // multiplicera med 10 för att lagra fyllningstid i s
                    // Hämta de glas som det nu fulla glaset ska svämma över till
                    List<Edge> nextGlasses = findNextGlasses(node.getNodeLabel());
                    // Ta bort det fulla glaset
                    RemoveUsedEdges(node.getNodeLabel());
                    iterator.remove();
                    // Lägg till nya glas i en temporär lista för att undvika att ändra på befintlig under iteration
                    for (int i = 0; i < nextGlasses.size(); i++) {
                        for (int j = 0; j < rows.size(); j++) {
                            for (int k = 0; k < rows.get(j).size(); k++) {
                                Node nextGlass = rows.get(j).get(k);
                                if (nextGlass != null) {
                                    if (nextGlass.getNodeLabel() == nextGlasses.get(i).getSecondNode()) {
                                        if (!filling.contains(nextGlass)) { // Om glaset inte redan tillagt, lägg till
                                            nextGlass.setFillSpeed(nextGlasses.get(i).getFlow());
                                            nextRowOfGlasses.add(rows.get(j).get(k));
                                        } else if (filling.contains(nextGlass)) { // om tillagd, öka fill speed
                                            int glassIndex = filling.indexOf(nextGlass);
                                            Node existingGlass = filling.get(glassIndex);
                                            existingGlass.setFillSpeed(existingGlass.getFillSpeed() + nextGlasses.get(i).getFlow());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            filling.addAll(nextRowOfGlasses);
        }
        // Print out av resultat till användaren
        System.out.println("Det tog " + filled.get(glassToFill).toString() + " sekunder att fylla glas nummer: " + glassToFill);
    }

    /* Metod för att ta bort edges (länkar) som programmet redan har gått igenom
    Syftet med metoden är att optimera sökningen*/
    private void RemoveUsedEdges(int nodeLabel) {
        edges.pollFirst();
    }

    /* Metod för att söka efter ett element i en LinkedList
    Returnerar en LinkedList med edges */
    private List<Edge> findNextGlasses(int nodeLabel) {
        List<Edge> nextGlasses = edges.stream()
                .filter(edge -> edge.getFirstNode() ==  nodeLabel)
                .limit(2)
                .toList();

        return nextGlasses;
    }

    /* Metod för att räkna ut flödet för en viss länk (edge)
    * Beskriver flödet per sekund för länken */
    double calculateFlow(Node node, ArrayList<Node> nodesOnRow, int rowNumber) {
        // Om endast en rad
        if (rowNumber == 0) {
            return 400 / 2.0; // Värde för första raden med ett inflöde på 400 ml / s
        }

        // För resterande rader, hämta föregående rads flöden med aktuell nod som intilliggande nod
        List<Edge> previousEdges = edges.stream()
                .filter(edge -> edge.getSecondNode() == node.getNodeLabel())
                .toList();
        if (previousEdges.size() == 1) {
            return previousEdges.getFirst().getFlow() / 2;
        }
            double totalInFlow = 0.0;
            for (int i = 0; i < previousEdges.size(); i++) {
                totalInFlow += previousEdges.get(i).getFlow();
            }
        return totalInFlow / 2;

    }

    /* Metod som promptar användaren att mata in värde för radnummer
    Om värdet inte är mellan 2 och 50 promptas användaren tills det blir rätt */
    private static int setNumberOfRows(Scanner input) {
        System.out.println("Programmet räknar ut hur lång tid det tar att fylla glaset du anger.");
        String prompt = "Mata in vilken rad glaset är på, ange radnummer mellan 2 och 50: ";
        int numberOfRows;
        do {
            System.out.print(prompt);
            numberOfRows = validateInput(input, prompt);
            if (numberOfRows > 50 || numberOfRows < 2) {
                System.out.println("FELAKTIG INMATNING: radnummer måste vara mellan 2 och 50");
            }
        } while (numberOfRows > 50 || numberOfRows < 2);
        return numberOfRows;
    }

    /* Metod som promptar användaren att välja glas på tidigare angiven rad
    Användaren promptas tills rätt värde (1 <= g <= r) anges */
    private static int setGlasToFill(int numberOfRows, Scanner input) {
        System.out.println("Du ska nu välja vilket glas på raden " + numberOfRows + " som du vill välja");
        String prompt = "Ange nummer för glaset mellan 1 och " + numberOfRows + " (där 1 är längst till vänster): ";
        int glassToFill;
        do {
            System.out.print(prompt);
            glassToFill = validateInput(input, prompt);
            if (glassToFill < 1 || glassToFill > numberOfRows) {
                System.out.println("FELAKTIG INMATNING: siffran för glaset måste vara mellan 1 och " +numberOfRows);
            }
        } while(glassToFill < 1 || glassToFill > numberOfRows);
        return glassToFill;
    }

    /* Metod för att validera input (endast heltal tillåtna) */
    private static int validateInput(Scanner input, String prompt) {
        while (!input.hasNextInt()) {
            System.out.println("FELAKTIGT VÄRDE: ange ett heltal mellan 2 och 50");
            input.next();
            System.out.print(prompt);

        }
        return input.nextInt();
    }

    /* Main metod */
    public static void main(String[] args) {
        // Skapa Scanner-objekt för input
        Scanner input = new Scanner(System.in);

        // Prompt för antal rader
        int numberOfRows = setNumberOfRows(input);

        // Prompt för att välja glas på vald rad
        int glassToFill = setGlasToFill(numberOfRows, input);

        // Skapa grafen (glaspyramiden) med validerad input
        Graph myGraph = new Graph(numberOfRows, glassToFill);

        input.close();
    }
}
