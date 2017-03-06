//Program:	Amison2.java
//Course:	COSC470
//Description:	
//Author:	Reagan JonAshley Amison 
//Revised:	3/5/2017
//Language:	Java
//IDE:		NetBeans 8.1
//Notes:	
//*************************************************************************************************************************
//*************************************************************************************************************************
// class:       Amison2
// Description: refer to the program description
// Data Values: none
// Constant:    none 
// Globals:     input- access to keyboard input class
//              closed- used by bestFirst as the closed priority queue
//              open - used by bestFirst as the open priority queue
//*************************************************************************************************************************
//*************************************************************************************************************************
public class Amison2 {
    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LinkedQueue closed = new LinkedQueue();// used by the bestFirst algorithm as the closed priority queue
    public static LinkedQueue open = new LinkedQueue();// used by the bestFirst algorithm as the open priority queue
    public static LList parentChild = new LList();//holds the boardClass() of each board, used to trace the path in breadthFirst
//*************************************************************************************************************************
//Method:	main
//Description:	handles the flow of the program, gets the start and goal state
//Parameters:   none
//Returns:     	none
//Calls:        KeyBoardInputClass.getString(), String.toCharArray(), String.length()
//              mapper1(), scramble(), mapper2(), KeyboardInputClass.getInteger()
//              KeyboardInputClass.getCharacter(), bestFirst(), breadthFirst()
//Globals:	input- access to keyboard input class
    public static void main(String[] args) {
        System.out.println("Reagan JonAshley Amison");
        System.out.println("This program solves the 15 puzzle with 16 characters including atleast 1 blank");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("");
        while (true) {
            char[] charArrayGoalFirst = new char[16];// convert the string to an array of chars
            String goal = "";
            while (true) {
                goal = input.getString("", "Input the 16 Character String to Use For The Goal,\nInclude Only Letters and Atleast 1 Space");
                charArrayGoalFirst = goal.toCharArray();                // convert the string to an array of chars
                boolean spacer = false;
                for (int i = 0; i < charArrayGoalFirst.length; i++) {   
                    if (charArrayGoalFirst[i] == ' ') {                 
                        spacer = true;
                        break; 
                    }
                }// end of space checker for
                if (spacer == true && goal.length() == 16) {            // if there is a space && there is 16 characters
                    break;
                }
                if (spacer == false) {                                  // if there is not a space
                    System.out.println("Be Sure You Enter a Space");
                }// end of space if
                if (goal.length() != 16) {                              // if length is not 16
                    System.out.println("You Must Use 16 Characters, You Entered " + goal.length() + " Characters");
                }
            }
            int[][] goalToInts = mapper1(charArrayGoalFirst);           //map the first char array to ints
            int[][] scrambledInts = scramble(goalToInts, goal);         //scramble the goal state
            char[][] goalChar = mapper2(goalToInts);                    //map the goal state to chars
            char[][] startChar = mapper2(scrambledInts);                //map the scrambled state to chars
            for (int i = 0; i < 2; i++) {                               // used to print out the start and goal states
                if (i == 0) {                                           // print the goal state
                    System.out.println("Goal State");
                } else {                                                // if the goal state has already been printed
                    System.out.println("");
                    System.out.println("Start State");                  // print the start stae
                }
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        if (i == 0) {
                            System.out.print(goalChar[j][k] + " ");     // print the goal stae
                        } else {
                            System.out.print(startChar[j][k] + " ");    // print the start state
                        }

                    }
                    System.out.println("");
                }
            }
            System.out.println("--------------------------------");
            int breadthBest = input.getInteger(true, 2, 1, 2, "Breadth-Frist-Search(1) or Best-First-Search(2)? Default=2");
            if (breadthBest == 1) {                                     // if the user wants breadthFirst
                breadthFirst(scrambledInts, goalToInts);
            } else if (breadthBest == 2) {                              // if the user wants bestFirst
                bestFirst(scrambledInts, goalToInts);
            }
            char quitMe = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Exit The Program? (Y/N Default=N)");
            if (quitMe == 'Y') {                                        // exit the program
                break;
            }
        }
    }// end of main
//*************************************************************************************************************************
//Method:	scramble
//Description:	scrambles the board an amount of random times, if the user doesn't want to
//              scramble the board, they enter 0 and then input the desired foal string
//Parameters:   int[][] passed - pass goal state to be scrambled
//              String goal - passed goal string, used to print for the user
//Returns:     	int[][] scrambledArray - scrambled array to be searched on
//Calls:        KeyboardInputClass.getInteger(), KeyboardInputClass.getString(), String.toCharArray()
//              mapper1(), new LList(), deepCopy(), getChildren(), Math.random(), LList.getLength(),
//              isOnOpenClosed(), LList.add(), LList.remove(), LList.isEmpty(), 
//Globals:	input- access to KeyboardInputClass()

    public static int[][] scramble(int[][] passed, String goal) {
        int[][] scrambledArray = new int[4][4];                 // make a new array to hold the scrambled array
        String entered = "";
        int numScrambles = input.getInteger(true, 0, 0, 999999999, "How Many Random Scrambles Would "
                + "You Like? 0 = Enter Start String(default)"); // gets the number of scrambles from the user
        if (numScrambles == 0) {                                // if the user wants to enter the start state
            while (true) {
                String start = input.getString("", "Input the 16 Character String to Use For The Start, \nInclude The Same Type(case sensative) And Same Number of Characters From The Goal String\n(Goal String: "+ goal + ")"); 
                                                                // gets the start state from the user
                if (start.length() == 16) {                     // checks to see if the length is 16
                    entered = start;
                    break;
                }
            }
            char[] enteredChar = entered.toCharArray();         // turn entered to a char array to be mapped to ints
            scrambledArray = mapper1(enteredChar);              // map enteredChar to ints
        } else {                                                // if user wants atleast 1 random scramble
            LList blankList = new LList();                      // will stay blank so that isOnOpenClosed() can be utilized
            LList closed = new LList();
            int[][] current = deepCopy(passed);                 // make a deep copy of current, not an alias
            int counter = 0;
            while (numScrambles > 0) {                          // while there are still scrambles to be made
                LList children = getChildren(current);          // get the children of the last scramble               
                while (true) {
                    int random = (int) (1 + Math.random() * children.getLength());  // random # 1- number of Children
                    int[][] passer = (int[][]) children.getEntry(random);           // get the entry at the random number from children
                    if (!isOnOpenClosed(blankList, closed, passer)) {               // if passer is not on closed
                        current = passer;
                        closed.add(current);
                        counter++;
                        numScrambles--;
                        break;
                    } else {                                    // if it has already been used
                        children.remove(random);
                    }
                    if (children.isEmpty()) {                   // if there are no children to be considered
                        numScrambles = 0;                       // set numScrambles to 0 so the while loop will end
                        break;
                    }
                }
            }
            scrambledArray = current;
        }
        return scrambledArray;                                  // returns the scrambled array
    }  
//*************************************************************************************************************************
//Method:	breadthFirst
//Description:	does the breadthFirst search if the user wants to
//Parameters:   int[][] passed - passed array as start state
//              int[][] goal - passed goal state
//Returns:     	none
//Calls:        parentChild.clear(), LList.isEmpty(), new LList(), LList.remove(), intComparer(),
//              LList.getEntry(), KeyboardInputClass.getString(), getChildren(), LList.add(), new boardClass
//Globals:	parentChild- access to the parentChild list
//              input - access to keyboardInputClass()
    public static void breadthFirst(int[][] passed, int[][] goal) {
        System.out.println("working....");
        parentChild.clear();                                     // clear the parent child data structure
        boolean success = false;
        int[][] found = new int[passed.length][passed[0].length];// new array to hold the final state
        LList open = new LList();                                // make a new open and closed list
        LList closed = new LList();
        open.add(passed);                                        // add the first state to open
        while (!open.isEmpty()) {                                // while open isn't empty
            int[][] x = (int[][]) open.getEntry(1);              // get and remove leftmost from open
            open.remove(1);
            if (intComparer(goal, x) == true) {                  // if x is a goal state
                found = deepCopy(x);
                input.getString("", "Success! Press Enter To Show All Boards");
                success = true;
                break;
            } else {
                LList children = getChildren(x);                 // get the children of x
                closed.add(x);                                   // add x to closed
                for (int i = 1; i <= children.getLength(); i++) {// for each child do
                    int[][] currentChild = (int[][]) children.getEntry(i);
                    if (!isOnOpenClosed(open, closed, currentChild)) { // check if the current child is on open or closed
                        open.add(currentChild);                        // add the current child to open
                        boardClass newBoard = new boardClass(x, currentChild, 0, 0, 0);
                        parentChild.add(newBoard);                     // add the boardClass object to parentChild
                    }
                }
            }
        }
        if (success == true) {                                      // if valid path was found
            int numMoves = traceBackBreadth(found, passed);         // trace the moves
            System.out.println(numMoves + " Moves Out Of " + closed.getLength() + " Considered (" + open.getLength() + " Left On Open)");
        } else {                                                    // if valid path was not found
            System.out.println("No Possible Path Found");
        }
        System.out.println("**************************************************");
    }
//*************************************************************************************************************************
//Method:	isOnOpenClosed
//Description:	checks to see if the current array is on the Open or Closed lists for breadth-first search
//Parameters:   LList open- passed open List
//              LList closed - passed closed List
//              int[][] passed - passed array to be checked agains open and closed lists
//Returns:     	boolean returner - if the array is in either list
//Calls:        LList.getLength(), intComparer(), LList.getEntry()
//Globals:	none

    public static boolean isOnOpenClosed(LList open, LList closed, int[][] passed) {
        boolean returner = false;
        int greater = open.getLength();                 // get the length of open
        if (closed.getLength() > greater) {             // if the closed list is longer
            greater = closed.getLength();
        }
        for (int i = greater; i > 0; i--) {             // backwards for to get from the end
            if (i <= open.getLength()) {                // if open is longer
                int[][] openArray = (int[][]) open.getEntry(i);// current index in open
                returner = intComparer(openArray, passed);// see if passed equals openArray
                if (returner == true) {
                    break;
                }
            }
            if (i <= closed.getLength()) {                         // if closed is longer
                int[][] closedArray = (int[][]) closed.getEntry(i);// current index in closed
                returner = intComparer(closedArray, passed);       // see if passed equals closedArray
                if (returner == true) {
                    break;
                }
            }

        }
        return returner;                                        // return if passed was found or not
    }
//*************************************************************************************************************************
//Method:	traceBackBreadth
//Description:	traces back the path taken in the breadth-first search in order to print it out for the user to see
//Parameters:   int[][] current - passed last state of the search algorithm
//              int[][] goal - goal state
//Returns:     	int numMoves - the number of moves it took to find the goal state
//Calls:        newLList(), keyboardInputClass.getCharacter(), LList.add(), intComparer(), findParentBreadth(),
//              LList.getLength(), mapper2(), KeyboardInputClass.getString()
//Globals:	input- access to keyBoardInputClass()
//              parentChild - access to the parentChild list

    public static int traceBackBreadth(int[][] current, int[][] start) {
        char showMoves = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Step Through The Moves? (Y/N Default=N)");
        int numMoves = 0;
        LList movesInOrder = new LList();                       // used to print out the moves at the end
        movesInOrder.add(1, current);
        while (!intComparer(current, start)) {                  // while the current state does not equal the start state
            current = findParentBreadth(current, parentChild);  // find the parent of the current
            movesInOrder.add(1, current);
            numMoves++;
        }
        for (int i = 1; i <= movesInOrder.getLength(); i++) {   // used to get the moves
            int[][] printArray = (int[][]) movesInOrder.getEntry(i);// get current index in movesInOrder
            char[][] charMoves = mapper2(printArray);           // map the current move to chars
            for (int j = 0; j < printArray.length; j++) {       // used to print the moves
                for (int k = 0; k < printArray.length; k++) {
                    System.out.print(charMoves[j][k] + " ");
                }
                System.out.println("");
            }
            if (showMoves == 'Y') {
                input.getString("", "Press Enter To Step:");    // make user acknowledge the previous move
            } else {
                System.out.println("");
            }
        }
        return numMoves;
    }
//*************************************************************************************************************************
//Method:	findParentBreadth
//Description:	finds the parent of the current board for the traceBackBreadth algorithm
//Parameters:   int[][] passed - array passed that needs to find the parent
//Returns:     	int[][] returner - the found parent
//Calls:        LList.getLength(), LList.getEntry(), intComparer(), deepCopy()
//Globals:	none
    public static int[][] findParentBreadth(int[][] passed, LList passedList) {
        int[][] returner = new int[passed.length][passed.length];           // new array to hold the parent
        for (int i = passedList.getLength(); i > 0; i--) {                  
            boardClass boardObject = (boardClass) passedList.getEntry(i);   // get the current index in the passedList
            int[][] child = (int[][]) boardObject.actual;                   // make an int array of the child
            if (intComparer(passed, child)) {                               // if the parent is found
                returner = deepCopy((int[][]) boardObject.parent);          // return the parent
                break;
            }
        }
        return returner;                                                    // return the parent
    }
//*************************************************************************************************************************
//Method:	getChildren
//Description:	gets the children of any passed board
//Parameters:   int[][] passed - array that needs to have the children
//Returns:     	LList children - a list of arrays of children for the passed array
//Calls:        new LList(), deepCopy(), LList.add(), 
//Globals:	none
    public static LList getChildren(int[][] passed) {
        LList children = new LList();                       //LList to hold the children
        for (int i = 0; i < passed.length; i++) {
            for (int j = 0; j < passed[0].length; j++) {
                if (passed[i][j] != 0) {                    // if the current index is not a blank
                    if (i != 0 && passed[i - 1][j] == 0) {  // if the index is not in the first row, look north
                        int[][] up = deepCopy(passed);      // make a deep copy of the passed array
                        up[i][j] = 0;                       // set the current index to 0
                        up[i - 1][j] = passed[i][j];        // set the north index to the current index in the passed array
                        children.add(up);                   // add the new child to children
                    }
                    if (i != passed.length - 1 && passed[i + 1][j] == 0) {// if the index is not in the last row, look south
                        int[][] down = deepCopy(passed);    // make a deep copy of the passed array
                        down[i][j] = 0;                     // set the current index to 0
                        down[i + 1][j] = passed[i][j];      // set the south index to the current index in the passed array
                        children.add(down);                 // add the new child to children
                    }
                    if (j != 0 && passed[i][j - 1] == 0) {  // if the index is not in the first column, look west
                        int[][] left = deepCopy(passed);    // make a deep copy of the passed array
                        left[i][j] = 0;                     // set the current index to 0
                        left[i][j - 1] = passed[i][j];      // set the west index to the current index in the passed array
                        children.add(left);                 // add the new child to children
                    }// end west if
                    if (j != passed.length - 1 && passed[i][j + 1] == 0) {// if the index is not in the last column, look east
                        int[][] right = deepCopy(passed);   // make a deep copy of the passed array
                        right[i][j] = 0;                    // set the current index to 0
                        right[i][j + 1] = passed[i][j];     // set the east index to the current index in the passed array
                        children.add(right);                // add the new child to children
                    }
                }
            }
        }
        return children;                                    // return the children list
    }
//*************************************************************************************************************************
//Method:	bestFirst
//Description:	best-first-search algorithm
//Parameters:   int[][] start - passed start state
//              int[][] goal - passed goal state
//Returns:     	none
//Calls:        LList.clear(), keyboardInputClass.getCharacter(), new boardClass(), LinkedQueue.priorityEnqueue()
//              LList.isEmpty(), LinkedQueue.dequeue, queueToList(), tracebackBest(), LList.getLength(), 
//              LList.getEntry(), LList.add(), LList.getLength(), isOnOpenBest(), isOnClosedBest(), 
//Globals:	

    public static void bestFirst(int[][] start, int[][] goal) {
        boolean success = false;
        char depth = input.getCharacter(true, 'Y', "Y,N", 1, "Use Depth-Penalty? (Y/N Default:Y)");
        System.out.println("Using Sum Of Tiles Out Of Place");
        System.out.println("working...");
        int sum1 = sumTiles(start, goal);                                // get the heuristic of the start state
        boardClass startBoard = new boardClass(start, start, 0, sum1, 0);// generate the startboard
        open.priorityEnqueue(startBoard);                                // add the startBoard to open
        while (open.isEmpty() == false) {                                // while open is not empty
            boardClass x = (boardClass) open.dequeue();                  // get the first board in the queue
            if (intComparer((int[][]) x.actual, goal) == true) {         // if the current is a goal state
                closed.priorityEnqueue(x);                                
                LList closedList = queueToList(closed);                  // convert the queue to a list
                success = true;
                input.getString("", "Success! Press Enter To Show All Boards");
                int numMoves = traceBackBest(x, start, closedList);     // trace the moves
                System.out.println(numMoves + " Moves Out Of " + closedList.getLength() + " Considered (" + open.getLength() + " Boards Left In Open)");
                System.out.println("**********************");
                return;
            } else {                                                    // if the current state is not the goal sate
                LList childrenX = getChildren((int[][]) x.actual);      // get the children of the current board
                LList childrenBoards = new LList();                     // new list for the children boards
                for (int i = 1; i <= childrenX.getLength(); i++) {      // used to make boardClass's of all the children arrays
                    int[][] child = (int[][]) childrenX.getEntry(i);    // get the current board in children
                    if (depth == 'Y') {                                 // if the user wants the depth penalty
                        boardClass childBoard = new boardClass(x.actual, child, x.depth + 1, sumTiles(goal, child), x.depth + 1);
                        childrenBoards.add(childBoard);
                    } else if (depth == 'N') {                          // if the user does not want the depth penalty
                        boardClass childBoard = new boardClass(x.actual, child, x.depth + 1, sumTiles(goal, child), 0);
                        childrenBoards.add(childBoard);
                    }
                }
                for (int i = 1; i <= childrenBoards.getLength(); i++) { // for each child do
                    boardClass current = (boardClass) childrenBoards.getEntry(i);// get the current board
                    boolean isOnOpen = isOnOpenBest(current);           // check if the current board is on open
                    boolean isOnClosed = isOnClosedBest(current);       // check if the current board is on closed
                    if (isOnOpen == false && isOnClosed == false) {     // if the current board is not on open or closed
                        open.priorityEnqueue(current);
                    }
                }               
            }
            closed.priorityEnqueue(x);
        }
        if (success == false) {
            System.out.println("No Possible Path Found");
            System.out.println("**********************************************");
        }
    }
//*************************************************************************************************************************
//Method:	queueToList
//Description:	changes a queue to a Linked List
//Parameters:   LinkedQueue passed - passed queue to change to list
//Returns:     	LList newList - the copied queue to a list
//Calls:        LinkedQueue.isEmpty(), LList.add(), LinkedQueue.dequeue(), new LList()
//Globals:	none
    public static LList queueToList(LinkedQueue passed) {
        LList newList = new LList();                // new list to return
        while (passed.isEmpty() == false) {         // while the linkedQueue is not empty
            boardClass board = (boardClass) passed.dequeue();// get the first board in the list
            newList.add(board);                     // add that to the new list
        }
        return newList;
    }
//*************************************************************************************************************************
//Method:	isOnOpenBest
//Description:	checks to see if the current board is on the Open list, for bestFirst
//Parameters:   boardClass current - passed boardClass object to see if it is on the open queue
//Returns:     	boolean returner - if current is on the list or not
//Calls:        new LList(), LinkedQueue.isEmpty(), LinkedQueue.dequeue(), intComparer(), LinkedQueue.priorityEnqueue()
//              LList.add(), LList.getEntry()
//Globals:	open - queue of the open list
    public static boolean isOnOpenBest(boardClass current) {
        boolean returner = false;
        LList holder = new LList();
        while (open.isEmpty() == false) {
            boardClass opened = (boardClass) open.dequeue();
            boolean isOn = intComparer((int[][]) opened.actual, (int[][]) current.actual);
            if (isOn == true) {
                returner = true;
                if (current.actualDepth < opened.actualDepth) {
                    opened.depth = current.depth;
                    open.priorityEnqueue(current);
                    break;
                }
            } else {
                holder.add(opened);
            }
        }
        for (int i = holder.getLength(); i > 0; i--) {
            open.priorityEnqueue((boardClass) holder.getEntry(i));
        }
        return returner;
    }
//*************************************************************************************************************************
//Method:	isOnClosedBest
//Description:	checks to see if the current board is on the closed list, for bestFirst
//Parameters:   boardClass current - passed boardClass object to see if it is on the closed queue
//Returns:     	boolean returner - if the current boardClass is on the closed queue
//Calls:        new LList(), LinkedQueue.getLength(), LinkedQueue.isEmpty(), LinkedQueue.dequeue(),
//              LinkedQueue..priorityEnqueue(), LList.add, new boardClass()
//Globals:	closed - queue of the closed list
    public static boolean isOnClosedBest(boardClass current) {
        boolean returner = false;
        LList holder = new LList();
        if (closed.getLength() != 0) {
            while (closed.isEmpty() == false) {
                boardClass compare = (boardClass) closed.dequeue();
                boolean isOn = intComparer((int[][]) compare.actual, (int[][]) current.actual);
                if (isOn == true) {
                    returner = true;
                    if ((int) compare.actualDepth > (int) current.actualDepth) {
                        boardClass newBoard = new boardClass(current.parent, current.actual, current.depth, current.tiles, current.depth);
                        open.priorityEnqueue(newBoard);
                        break;
                    } else {
                        holder.add(compare);
                    }
                } else {
                    holder.add(compare);
                }
            }
            for (int i = 1; i <= holder.getLength(); i++) {
                closed.enqueue((boardClass) holder.getEntry(i));
            }
        }
        return returner;
    }
//*************************************************************************************************************************
//Method:	traceBackBest
//Description:	traces back the path taken in the bestFirst search
//Parameters:   boardClass current- passed last board of the bestFirst search
//              int[][] goal - goal board
//              LList closedList - passed list of all the moves in bestFirst search
//Returns:     	int numMoves - number of moves bestFirst search took
//Calls:        keyboardInputClass.getCharacter(), keyboardInputClass.getString(), new LList(),
//              LList.add(), intComparer(), findParentBest(), LList.getEntry(), mapper2()
//Globals:	input - access to keyboardInputClass()
    public static int traceBackBest(boardClass current, int[][] goal, LList closedList) {
        char showMoves = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Step Through The Moves? (Y/N Default=N)");
        if (showMoves == 'Y') {
            System.out.println("Press Enter To Step Through");
        }
        LList movesInOrder = new LList();
        movesInOrder.add(current);
        int numMoves = 0;
        while (intComparer(current.actual, goal) == false) {
            current = findParentBest(current, closedList);
            movesInOrder.add(1, current);
            numMoves++;
        }
        for (int i = 1; i <= movesInOrder.getLength(); i++) {
            boardClass first = (boardClass) movesInOrder.getEntry(i);
            char[][] chars = mapper2((int[][]) first.actual);
            for (int j = 0; j < chars.length; j++) {
                for (int k = 0; k < chars.length; k++) {
                    System.out.print(chars[j][k] + " ");
                }
                System.out.println("");
            }
            System.out.println("(Raw Score = " + first.tiles + " Depth = " + first.depth + " Total Score = " + first.score + ")");
            if (showMoves == 'Y') {
                input.getString("", "Press Enter To Step:");
            } else {
                System.out.println("");
            }
        }
        return numMoves;
    }
//*************************************************************************************************************************
//Method:	findParentBest
//Description:	finds the parent in the passed closed list of the current board
//Parameters:   boardClass current - the current board that needs it's parent
//              LList closedList - closed list of the moves used
//Returns:     	boardClass returner - the boardClass containing the parent of the passed current
//Calls:        LList.getEntry(), intComparer(), LList.getLength()
//Globals:	none

    public static boardClass findParentBest(boardClass current, LList closedList) {
        boardClass returner = current;
        for (int i = closedList.getLength(); i > 0; i--) {
            boardClass board = (boardClass) closedList.getEntry(i);
            if (intComparer(current.parent, board.actual) == true) {
                returner = board;
            }
        }
        return returner;
    }
//*************************************************************************************************************************
//Method:	sumTiles
//Description:	counts the number of tiles out of place, excluding spaces
//Parameters:   int[][] goal - goal state
//              int[][] current - current array that needs to checked for tiles out of place
//Returns:     	int counter - number of tiles out of place
//Calls:        none  
//Globals:	none

    public static int sumTiles(int[][] goal, int[][] current) {
        int counter = 0;// initialize a counter
        for (int i = 0; i < current.length; i++) {// row for
            for (int j = 0; j < current.length; j++) {// column for
                if (current[i][j] != 0) {
                    if (current[i][j] != goal[i][j]) {//if tile is not equal to goal 
                        counter++;// increment the count
                    }// end if tile is not equal to goal  
                }
            } //end column for           
        }//end row for
        return counter;// return the number of tiles out of place
    }// end sumTiles
//*************************************************************************************************************************
//Method:	deepCopy
//Description:	makes a deep copy of an int[][]
//Parameters:   int[][] passed - passed array that needs to be copied
//Returns:     	int[][] copied - copied passed array
//Calls:        none
//Globals:	none
    public static int[][] deepCopy(int[][] passed) {
        int[][] copied = new int[passed.length][passed.length];// initialize a new 4x4 int array to be copied to
        for (int i = 0; i < passed.length; i++) {// row for
            for (int j = 0; j < passed.length; j++) {// column for
                copied[i][j] = passed[i][j];//copy the passed index to the new index
            }// end column for
        }// end row for
        return copied;// return the new copy
    }// end deep copy
//*************************************************************************************************************************
//Method:	intComparer
//Description:	compares 2 int[][]'s to see if they match each other
//Parameters:   int[][] orig - first passed array to be compared
//              int[][] compare - second passed array to be compared
//Returns:     	boolean returner - if the arrays equal each other
//Calls:        none
//Globals:	none

    public static boolean intComparer(int[][] orig, int[][] compare) {
        boolean returner = true;// assume they are the same
        for (int i = 0; i < compare.length; i++) {// row for
            for (int j = 0; j < compare[0].length; j++) {// column for
                if (compare[i][j] != orig[i][j]) {// if the indexes differ
                    returner = false;// set returner to false
                    break;// break column for
                }// end if indexes differ
                if (returner == false) {// if indexes differ row for break
                    break;// break row for
                }// end if indexes differ row for
            }// end column for        
        }// end row for
        return returner;// return if they differ or not
    }// end intComparer
//*************************************************************************************************************************
//Method:	mapper1
//Description:	maps characters to numbers and returns a 2D array of integers, it is redundant but it works
//Parameters:   char[] passed - passed array of chars that needs to be mapped to integers
//Returns:     	int[][] returner - array that is mapped to numbers from the passed char array, to be used in 
//              the search algorithms
//Calls:        none
//Globals:	none
    public static int[][] mapper1(char[] passer) {
        int[] usedInside = new int[16];
        for (int i = 0; i < passer.length; i++) {
            if (passer[i] == '.') {
                usedInside[i] = 0;
            } else if (passer[i] == 'a') {
                usedInside[i] = 1;
            } else if (passer[i] == 'A') {
                usedInside[i] = 27;
            } else if (passer[i] == 'b') {
                usedInside[i] = 2;
            } else if (passer[i] == 'B') {
                usedInside[i] = 28;
            } else if (passer[i] == 'c') {
                usedInside[i] = 3;
            } else if (passer[i] == 'C') {
                usedInside[i] = 29;
            } else if (passer[i] == 'd') {
                usedInside[i] = 4;
            } else if (passer[i] == 'D') {
                usedInside[i] = 30;
            } else if (passer[i] == 'e') {
                usedInside[i] = 5;
            } else if (passer[i] == 'E') {
                usedInside[i] = 31;
            } else if (passer[i] == 'f') {
                usedInside[i] = 6;
            } else if (passer[i] == 'F') {
                usedInside[i] = 32;
            } else if (passer[i] == 'g') {
                usedInside[i] = 7;
            } else if (passer[i] == 'G') {
                usedInside[i] = 33;
            } else if (passer[i] == 'h') {
                usedInside[i] = 8;
            } else if (passer[i] == 'H') {
                usedInside[i] = 34;
            } else if (passer[i] == 'i') {
                usedInside[i] = 9;
            } else if (passer[i] == 'I') {
                usedInside[i] = 35;
            } else if (passer[i] == 'j') {
                usedInside[i] = 10;
            } else if (passer[i] == 'J') {
                usedInside[i] = 36;
            } else if (passer[i] == 'k') {
                usedInside[i] = 11;
            } else if (passer[i] == 'K') {
                usedInside[i] = 37;
            } else if (passer[i] == 'l') {
                usedInside[i] = 12;
            } else if (passer[i] == 'L') {
                usedInside[i] = 38;
            } else if (passer[i] == 'm') {
                usedInside[i] = 13;
            } else if (passer[i] == 'M') {
                usedInside[i] = 39;
            } else if (passer[i] == 'n') {
                usedInside[i] = 14;
            } else if (passer[i] == 'N') {
                usedInside[i] = 40;
            } else if (passer[i] == 'o') {
                usedInside[i] = 15;
            } else if (passer[i] == 'O') {
                usedInside[i] = 41;
            } else if (passer[i] == 'p') {
                usedInside[i] = 16;
            } else if (passer[i] == 'P') {
                usedInside[i] = 42;
            } else if (passer[i] == 'q') {
                usedInside[i] = 17;
            } else if (passer[i] == 'Q') {
                usedInside[i] = 43;
            } else if (passer[i] == 'r') {
                usedInside[i] = 18;
            } else if (passer[i] == 'R') {
                usedInside[i] = 44;
            } else if (passer[i] == 's') {
                usedInside[i] = 19;
            } else if (passer[i] == 'S') {
                usedInside[i] = 45;
            } else if (passer[i] == 't') {
                usedInside[i] = 20;
            } else if (passer[i] == 'T') {
                usedInside[i] = 46;
            } else if (passer[i] == 'u') {
                usedInside[i] = 21;
            } else if (passer[i] == 'U') {
                usedInside[i] = 47;
            } else if (passer[i] == 'v') {
                usedInside[i] = 22;
            } else if (passer[i] == 'V') {
                usedInside[i] = 48;
            } else if (passer[i] == 'w') {
                usedInside[i] = 23;
            } else if (passer[i] == 'W') {
                usedInside[i] = 49;
            } else if (passer[i] == 'x') {
                usedInside[i] = 24;
            } else if (passer[i] == 'X') {
                usedInside[i] = 50;
            } else if (passer[i] == 'y') {
                usedInside[i] = 25;
            } else if (passer[i] == 'Y') {
                usedInside[i] = 51;
            } else if (passer[i] == 'z') {
                usedInside[i] = 26;
            } else if (passer[i] == 'Z') {
                usedInside[i] = 52;
            }
        }
        int[][] returner = new int[4][4];
        for (int j = 0; j < usedInside.length; j++) {
            if (j < 4) {
                returner[0][j] = usedInside[j];
            }
            if (j > 3 && j < 8) {
                returner[1][j - 4] = usedInside[j];
            }
            if (j > 7 && j < 12) {
                returner[2][j - 8] = usedInside[j];
            }
            if (j > 11 && j < 16) {
                returner[3][j - 12] = usedInside[j];
            }
        }
        return returner;
    }// end of mapper
//*************************************************************************************************************************
//Method:	mapper2
//Description:	maps numbers to characters, it is redundant but it works
//Parameters:   int[][] passer - the passed int array that needs to be changed to characters
//Returns:     	char[][] usedInside - the array that has been mapped to chars from the passed int[][]
//Calls:        none
//Globals:	none

    public static char[][] mapper2(int[][] passer) {
        char[][] usedInside = new char[4][4];
        for (int i = 0; i < passer.length; i++) {
            for (int j = 0; j < passer[0].length; j++) {
                if (passer[i][j] == 0) {
                    usedInside[i][j] = '.';
                } else if (passer[i][j] == 1) {
                    usedInside[i][j] = 'a';
                } else if (passer[i][j] == 27) {
                    usedInside[i][j] = 'A';
                } else if (passer[i][j] == 2) {
                    usedInside[i][j] = 'b';
                } else if (passer[i][j] == 28) {
                    usedInside[i][j] = 'B';
                } else if (passer[i][j] == 3) {
                    usedInside[i][j] = 'c';
                } else if (passer[i][j] == 29) {
                    usedInside[i][j] = 'C';
                } else if (passer[i][j] == 4) {
                    usedInside[i][j] = 'd';
                } else if (passer[i][j] == 30) {
                    usedInside[i][j] = 'D';
                } else if (passer[i][j] == 5) {
                    usedInside[i][j] = 'e';
                } else if (passer[i][j] == 31) {
                    usedInside[i][j] = 'E';
                } else if (passer[i][j] == 6) {
                    usedInside[i][j] = 'f';
                } else if (passer[i][j] == 32) {
                    usedInside[i][j] = 'F';
                } else if (passer[i][j] == 7) {
                    usedInside[i][j] = 'g';
                } else if (passer[i][j] == 33) {
                    usedInside[i][j] = 'G';
                } else if (passer[i][j] == 8) {
                    usedInside[i][j] = 'h';
                } else if (passer[i][j] == 34) {
                    usedInside[i][j] = 'H';
                } else if (passer[i][j] == 9) {
                    usedInside[i][j] = 'i';
                } else if (passer[i][j] == 35) {
                    usedInside[i][j] = 'I';
                } else if (passer[i][j] == 10) {
                    usedInside[i][j] = 'j';
                } else if (passer[i][j] == 36) {
                    usedInside[i][j] = 'J';
                } else if (passer[i][j] == 11) {
                    usedInside[i][j] = 'k';
                } else if (passer[i][j] == 37) {
                    usedInside[i][j] = 'K';
                } else if (passer[i][j] == 12) {
                    usedInside[i][j] = 'l';
                } else if (passer[i][j] == 38) {
                    usedInside[i][j] = 'L';
                } else if (passer[i][j] == 13) {
                    usedInside[i][j] = 'm';
                } else if (passer[i][j] == 39) {
                    usedInside[i][j] = 'M';
                } else if (passer[i][j] == 14) {
                    usedInside[i][j] = 'n';
                } else if (passer[i][j] == 40) {
                    usedInside[i][j] = 'N';
                } else if (passer[i][j] == 15) {
                    usedInside[i][j] = 'o';
                } else if (passer[i][j] == 41) {
                    usedInside[i][j] = 'O';
                } else if (passer[i][j] == 16) {
                    usedInside[i][j] = 'p';
                } else if (passer[i][j] == 42) {
                    usedInside[i][j] = 'P';
                } else if (passer[i][j] == 17) {
                    usedInside[i][j] = 'q';
                } else if (passer[i][j] == 43) {
                    usedInside[i][j] = 'Q';
                } else if (passer[i][j] == 18) {
                    usedInside[i][j] = 'r';
                } else if (passer[i][j] == 44) {
                    usedInside[i][j] = 'R';
                } else if (passer[i][j] == 19) {
                    usedInside[i][j] = 's';
                } else if (passer[i][j] == 45) {
                    usedInside[i][j] = 'S';
                } else if (passer[i][j] == 20) {
                    usedInside[i][j] = 't';
                } else if (passer[i][j] == 46) {
                    usedInside[i][j] = 'T';
                } else if (passer[i][j] == 21) {
                    usedInside[i][j] = 'u';
                } else if (passer[i][j] == 47) {
                    usedInside[i][j] = 'U';
                } else if (passer[i][j] == 22) {
                    usedInside[i][j] = 'v';
                } else if (passer[i][j] == 48) {
                    usedInside[i][j] = 'V';
                } else if (passer[i][j] == 23) {
                    usedInside[i][j] = 'w';
                } else if (passer[i][j] == 49) {
                    usedInside[i][j] = 'W';
                } else if (passer[i][j] == 24) {
                    usedInside[i][j] = 'x';
                } else if (passer[i][j] == 50) {
                    usedInside[i][j] = 'X';
                } else if (passer[i][j] == 25) {
                    usedInside[i][j] = 'y';
                } else if (passer[i][j] == 51) {
                    usedInside[i][j] = 'Y';
                } else if (passer[i][j] == 26) {
                    usedInside[i][j] = 'z';
                } else if (passer[i][j] == 52) {
                    usedInside[i][j] = 'Z';
                }
            }
        }
        return usedInside;
    }// end of mapper2   
}// end of Amison2
//*************************************************************************************************************************
//*************************************************************************************************************************
// class:       boardClass
// Description: constructor used to make boardClass objects
// Data Values: none
// Constant:    none 
// Globals:     int[][] parent - parent board of the new board
//              int[][] actual - actual board of the new board
//              int depth - depth of the new board
//              int tiles - number of tiles summed up from the current board
//              int actualDepth - the passed depth used to be scored
//              Comparable  score - the score to be sorted on, computed by actualDepth+tiles
//*************************************************************************************************************************
//*************************************************************************************************************************
class boardClass implements Comparable<boardClass> {
    public int[][] parent;
    public int[][] actual;
    public int depth;
    public int tiles;
    public int actualDepth;
    public Comparable score;
//*************************************************************************************************************************
//Method:	boardClass
//Description:	used to construct board objects for boardClass
//Parameters:   int[][] passedParent - parent board of the new board
//              int[][] passedActual - actual board of the new board
//              int passedDepth - depth of the new board
//              int passedTiles - number of tiles summed up from the current board
//              int scoringDepth - the passed depth used to be scored
//Returns:     	none
//Calls:        none
//Globals:	int[][] parent - parent board of the new board
//              int[][] actual - actual board of the new board
//              int depth - depth of the new board
//              int tiles - number of tiles summed up from the current board
//              int actualDepth - the passed depth used to be scored
//              Comparable  score - the score to be sorted on, computed by actualDepth+tiles
    public boardClass(int[][] passedParent, int[][] passedActual, int passedDepth, int passedTiles, int scoringDepth) {
        parent = passedParent;
        actual = passedActual;
        depth = passedDepth;
        tiles = passedTiles;
        actualDepth = scoringDepth;
        score = (Comparable) (scoringDepth + passedTiles);
    }
//*************************************************************************************************************************
//Method:	compareTo
//Description:	overrides the default comareTo so that the boards can be sorted in the priority
//              queue by their score
//Parameters:   boardClass next - next board in the queue
//Returns:     	this.score.compareTo(next.score) - using the integer compareTo() method
//Calls:        compareTo()
//Globals:	score
    @Override
    public int compareTo(boardClass next) {
        return this.score.compareTo(next.score);
    }// end of compareTo
}
