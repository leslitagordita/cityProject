/* CityProject.java  -- project class file with the project's main method   
 *  
 * This software package creates a graph of cities in the Unitied States with
 * links between the cities. Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph.   The data for the cities and
 * links are read into arrays from data files, which should be in the project folder.
 * The files are CSV files, which can be read and edited in Excel.
 *
 * The main class for the project is the CityProject class.   Other class include:
 * 
 *   Vertex - clas for each Vertex in a graph.
 *   City extends Vertex - Each City is a Vertex with added properties.  Each City
 *      has a unique name, and X and Y cooordinates for location on a 1500 by 900 Canvas.
 *   Edge - an edge in the graph, with a source, destination, and length.
 *   AjacencyNode - a node for a linked list of cities directly connected to each City.
 *      Each City has a linked list of adjacnt cities, created from the info in the 
 *      data files, with destination City and distance data in the node, and a 
 *      link to the next node. 
 *   CityMap - extends Canvas, a map of the graph on a 1500 by 900 GUI Canvas.
 *      A CityMap object in instantiated in the drawMap method in the CityProject class.
 * 
 * The main method in the CityProject class calls methods to reads City and Edge 
 * data from data files into arrays, set up the adjacency list in each instance 
 * of City, print a list of Vertex cities and their Edges, then draw a map of the graph.
 *
 * created for use by students in CSCI 211 at Community Colle of Philadelphia
 * copyright 2014 by C. herbert.  last edited Dec. 18, 2015 by Leslie Salazar
 */

package cityproject;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class CityProject {


    // main metod for the project
    public static void main(String[] args) {
        
        /**************Scanner Code************************/
        String source;
        String destination;
        //sets up input stream from the keyboard
        Scanner keyboardInput; 
        keyboardInput = new Scanner(System.in);
         //get the full loan amount
        System.out.print("Please enter your start location\n");
        source = keyboardInput.nextLine();
        
        //get the annual interest rate
        System.out.print("Please enter your destination\n");
        destination = keyboardInput.nextLine();
        /**************Scanner Code************************/
        
        City[] cities = new City[123]; //array of cities (Vertices) max = 200
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new City();
        }

        Edge[] links = new Edge[2000];// array of links  (Edges)  max = 2000
        for (int i = 0; i < links.length; i++) {
            links[i] = new Edge();
        }

        int cityCount; //    actual number of cities
        int linkCount; //    actual number of links

        // load cities into an array from a datafile
        cityCount = readCities(cities);
     

        // load links into an array from a datafile
        linkCount = readLinks(links, cities);

        // create the adjacency list for each city based on the link array
        createAdjacencyLists(cityCount, cities, linkCount, links);

        // print adjacency lists for all cities
        //PrintAdjacencyLists(cityCount, cities);

        // draw a map of the cities and links
        //drawMap(cityCount, cities,linkCount, links); 
        
       //call dijkstra method to run algorithm 
        dijkstra(source, cities);
        
       
        //create stack to hold stack of destination to source city StackElems
        Stack testStack = new Stack();
        testStack= addToStack(source, destination, cities );
        
        //print the route of the shortest path for source and destination city
        printShortestPath(testStack, source, destination);
        
    } // end main
    //************************************************************************

    
    
    // method to read city data into an array from a data file
    public static int readCities(City[] cities) {

        int count = 0; // number of cities[] elements with data

        String[][] cityData = new String[123][3]; // holds data from the city file
        String delimiter = ",";                   // the delimiter in a csv file
        String line;                              // a String to hold each line from the file
        
        String fileName = "cities.csv";           // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there is another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                cityData[count] = line.split(delimiter);
                
                // read data from the 2D array into an array of City objects
                cities[count].setName(cityData[count][0]);
                cities[count].setX(Integer.parseInt(cityData[count][1]));
                cities[count].setY(Integer.parseInt(cityData[count][2]));

                count++;
            }// end while

            infile.close();

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
        return count;

    } // end loadCities()
    //*************************************************************************

    // method to read link data into an array from a data file
    public static int readLinks(Edge[] links, City[] cities) {
        int count = 0; // number of links[] elements with data

        String[][] CityLinkArray = new String[695][3]; // holds data from the link file
        String delimiter = ",";                       // the delimiter in a csv file
        String line;				      // a String to hold each line from the file

        String fileName = "links.csv";                // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();
                

                // split the line into separate objects and store them in a row in the array
                CityLinkArray[count] = line.split(delimiter);

                // read link data from the 2D array into an array of Edge objects
                // set source to vertex with city name in source column
                links[count].setSource(findCity(cities, CityLinkArray[count][0]));
                // set destination to vertex with city name in destination column
                links[count].setDestination(findCity(cities, CityLinkArray[count][1]));
                //set length to integer valuein length column
                links[count].setLength(Integer.parseInt(CityLinkArray[count][2]));
                
                count++;

            }// end while

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
        return count;
    } // end loadLinks()
    //*************************************************************************

    // emthod to find the City onject with the given city name
    public static City findCity(City[] cities, String n) {
        int index = 0;  // loop counter
        // go through the cities array until the name is found
        // the name will be in the list

        while (cities[index].getName().compareTo(n) != 0) {

            index++;
        }// end while()
        return cities[index];

    } // end  findCity()

// method to create an adjacency lists for each city
    public static void createAdjacencyLists(int cityCount, City[] cities, int linkCount, Edge[] links) {

        AdjacencyNode temp = new AdjacencyNode();

        // iterate city array
        for (int i = 0; i < cityCount; i++) {

            //iterate link array
            for (int j = 0; j < linkCount; j++) {
                // if the currentl link's source is the current city
                if (links[j].getSource() == cities[i]) {

                    /* create a node for the link and inseert it into the adjancency list
                     * as the new head of the list. 
                     */
                    // temporarily store the current value of the list's head
                    temp = cities[i].getAdjacencyListHead();
                    //create a new node
                    AdjacencyNode newNode = new AdjacencyNode();
                    // add city and distance data
                    newNode.setCity(links[j].getDestination());
                    newNode.setDistance(links[j].getLength());
                    // point newNode to the previous list head
                    newNode.setNext(temp);

                    // set the new head of the list to newNode
                    cities[i].setAdjacencyListHead(newNode);

                }  // end if
            } // end for j
        } // end for i

    } // end createAdjacencyLists()

    // method to print adjacency lists
    public static void PrintAdjacencyLists(int cityCount, City[] cities) {

        System.out.println("List of Edges in the Graph of Cities by Source City");
        // iterate array of cities
        for (int i = 0; i < cityCount; i++) {

            // set current to adjacency list for this city    
            AdjacencyNode current = cities[i].getAdjacencyListHead();

            // print city name
            System.out.println("\nFrom " + cities[i].getName());

            // iterate adjacency list and print each node's data
            while (current != null) {
                System.out.println("\t"+ current.toString());
                current = current.getNext();
            } // end while (current != null) 

        }   // end for i 

    } // end PrintAdjacencyLists()

    
    // method to draw the graph (map of cities and links)
   static void drawMap(int cCount, City[] c, int lCount, Edge[] l)
   {
       CityMap canvas1 = new CityMap(cCount,  c, lCount, l);
       

        int width = 1500; // width of the Canvas
        int height = 900; // heightof the Canvas 
        
        
        // set up a JFrame to hold the canvas
        JFrame frame = new JFrame();
        frame.setTitle("U.S. Cities");
        frame.setSize(width, height);
        frame.setLocation(10, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the canvas to the frame as a content panel
        frame.getContentPane().add(canvas1);
        frame.setVisible(true);

   } // end drawMap() 
   
 /******methods written by Leslie Salazar ****************************/ 
   
   //method takes source city and an array and implements dijkstra's algorithm to find shortest path for
   //all cities in the array.
   public static void dijkstra(String source, City[] allCities)
   {
        //will hold sourceCity
        City sourceCity;
        //will hold currentCity
        City currentCity;
        

       //Step 1
       //find the city from array of all cities assign to sourceCity variable
       //set its bestDistance to 0 & its immediatePredecessor to null
       sourceCity = findCity(allCities, source);
       sourceCity.setBestDistance(0);
       sourceCity.setImmediatePredecessor(null);
      
       //set currentCity to point to sourceCity
       currentCity = sourceCity;
       
       while(!visited(allCities))
       {
           //System.out.println("name from dijk "+currentCity.getName());
           setEdgeBstDist(currentCity);
           currentCity.setVisited(true);
           currentCity = citiesBestDist(allCities);
       }//end while
       
       
   }//end dijkstra()
  
   //for every edge in the adjacency list, find the bestdistance through the current city.  
   //The method will do this only if the calculated bestdistance is better than what's
   //already been found.
  public static void setEdgeBstDist(City curCity)
   { 
       AdjacencyNode currentNode = curCity.getAdjacencyListHead();
       //System.out.println(currentNode.toString()); 
       while(currentNode != null)
       {
           //System.out.println("inside dij while");
           //current Edge's best distance previously
           int prevBstDist = currentNode.getCity().getBestDistance();
           //current Edge's best distance through current city
           int BstDistThruCurCity = curCity.getBestDistance() + currentNode.getcDistance();
           
           //if bestDistance through current city is less than before
           //set the new bestdistance for that Edge's city and set the 
           //immediate predecessor to be the current City
           if(BstDistThruCurCity < prevBstDist)
           {
               currentNode.getCity().setBestDistance(BstDistThruCurCity);
               //System.out.println("line 349 setEdgeBstDist"+currentNode.getCity().getBestDistance());
               currentNode.getCity().setImmediatePredecessor(curCity);
               //currentNode = currentNode.getNext();
               //System.out.println(currentNode.toString());
           }
           
           //to avoid null pointer exception
          //if(currentNode.getNext()!=null)
           //{
            currentNode = currentNode.getNext();
           //} 
           //if(temp)
       }//end while
       
       curCity.setVisited(true);
   }//end setEdgeBstDist
  
  //for all cities in cities[], will find city with best distance
   public static City citiesBestDist(City[] allCities)
   {
       City bDistCity = null;
       
       int minVal = Integer.MAX_VALUE;
       int tempVal;
       
       for(int i = 0; i < allCities.length; i++)
       {
           if(allCities[i].getBestDistance() <= minVal && allCities[i].getVisited() != true)
           { 
               //System.out.println("citiesBestcalled line 380");
               minVal = allCities[i].getBestDistance();
               bDistCity = allCities[i];
               //System.out.println("best distance city: "+bDistCity.getName());
           }//end if
       }//end for loop
       
       return bDistCity;
   }//end citiesBestDist()
   
   //method will find if all cities have been visited
   public static boolean visited(City[] allCities)
   {
        boolean beenVisited = true;
        
        for(int i = 0; i < allCities.length; i++)
        {
            if(allCities[i].getVisited() == false)
            {
                beenVisited = false;
            }//end if
        }//end for loop
       
       return beenVisited;
   }//end visited
   
   //method will use the provided source and destination string to add the immediate predecessor
   //city chain beginnning from the destination city and ending at the source city.
   public static Stack addToStack(String source, String destination, City[] citiesArray)
   {
       City nodeToAddToStack;
       City currentCity;
       Stack shortestPath = new Stack();
       //find the destination city
       nodeToAddToStack = findCity(citiesArray,destination);
       currentCity = nodeToAddToStack;
       StackElem stackDestCity = new StackElem(currentCity);
       shortestPath.push(stackDestCity);
       //System.out.println(currentCity.getName());
       boolean done = false;
       while(!done)
       {
           
           //System.out.println(currentCity.getName());
           currentCity = currentCity.getImmediatePredecessor();
           StackElem stackCity = new StackElem(currentCity);
           shortestPath.push(stackCity);
           //if(source == currentCity.getName())
           if(currentCity.getName().compareTo(source) == 0)
           {
               done = true;
           }
           
       }//end while loop
       
       return shortestPath;
   }//end addToStack
   
   //method will take a stack with each city from the shortest path found for a 
   //source and destination city and pop each StackElem  and print the city's name.
   public static void printShortestPath(Stack spStack, String source, String destination)
   {
       StackElem cityInfo;
       int i = 0;
       System.out.println("Here is your shortest path from " + source + " to " + destination + "\n" );
       while(i < spStack.stackSize())
       {
            cityInfo = spStack.pop();
            System.out.println(cityInfo.getStackData().getName() + "\n");
       }
   }//printShortestPath()
    
  /****************************************************************************/ 

   
   
    
    
} // end class cityProject
