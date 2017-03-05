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
// Globals:      list
//*************************************************************************************************************************
//*************************************************************************************************************************
public class Amison2 {
    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LinkedQueue closed = new LinkedQueue();// used by the bestFirst algorithm as the closed priority queue
    public static LinkedQueue open = new LinkedQueue();// used by the bestFirst algorithm as the open priority queue
//*************************************************************************************************************************
//Method:	main
//Description:	handles the flow of the program, gets the start and goal state
//Parameters:   none
//Returns:     	none
//Calls:        KeyBoardInputClass.getString(), String.toCharArray(), String.length()
//              mapper1(), scramble(), mapper2(), KeyboardInputClass.getInteger()
//              KeyboardInputClass.getCharacter(), bestFirst(), breadthFirst()
//Globals:	

    public static void main(String[] args) {
        while (true) {
            char[] charArrayGoalFirst = new char[16];// convert the string to an array of chars
            String goal = "";
            while (true) {
                goal = input.getString("", "Input the 16 Character String to Use For The Goal,\nInclude Only Letters and Atleast 1 Space");
                charArrayGoalFirst = goal.toCharArray();// convert the string to an array of chars
                boolean spacer = false;// initialoze spacer
                for (int i = 0; i < charArrayGoalFirst.length; i++) {// space checker for
                    if (charArrayGoalFirst[i] == ' ') {// space checker if
                        spacer = true;// set spacer to true
                        break;// break the for to save machine cycles(extremely minute savings)
                    }// end of is space if
                }// end of space checker for
                if (spacer == true && goal.length() == 16) {// if there is a space
                    break;// break the validating while
                }// end if there is a space
                if (spacer == false) {// if there is not a space
                    System.out.println("Be Sure You Enter a Space");
                }// end of space if
                if (goal.length() != 16) {// if length is not 16
                    System.out.println("You Must Use 16 Characters, You Entered " + goal.length() + " Characters");
                }// end of length if
            }// end of parsing while
            int[][] goalToInts = mapper1(charArrayGoalFirst);
            int[][] scrambledInts = scramble(goalToInts, goal);
            char[][] goalChar = mapper2(goalToInts);
            char[][] startChar = mapper2(scrambledInts);
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    System.out.println("Goal State");
                } else {
                    System.out.println("");
                    System.out.println("Start State");
                }
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        if (i == 0) {
                            System.out.print(goalChar[j][k] + " ");
                        } else {
                            System.out.print(startChar[j][k] + " ");
                        }

                    }
                    System.out.println("");
                }
            }
            System.out.println("--------------------------------");
            char[][] mappedToChars = mapper2(goalToInts);
            int breadthBest = input.getInteger(true, 2, 1, 2, "Breadth-Frist-Search(1) or Best-First-Search(2)? Default=2");
            if (breadthBest == 1) {
                breadthFirst(scrambledInts, goalToInts);
            } else if (breadthBest == 2) {
                bestFirst(scrambledInts, goalToInts);
            }
            char quitMe = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Exit The Program? (Y/N Default=N)");
            if (quitMe == 'Y') {
                break;
            }
        }// end of total while
    }// end of main
//*************************************************************************************************************************
//Method:	scramble
//Description:	scrambles the board an amount of random times, if the user doesn't want to
//              scramble the board, they enter 0 and then input the desired foal string
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static int[][] scramble(int[][] passed, String goal) {
        int[][] scrambledArray = new int[4][4];// make a new array to hold the scrambled array
        String entered = "";// initialize entered
        int numScrambles = input.getInteger(true, 0, 0, 999999999, "How Many Random Scrambles Would "
                + "You Like? 0 = Enter Start String(default)");// gets the number of scrambles from the user
        if (numScrambles == 0) {// if the user wants to enter the start state
            while (true) {// infinite while
                String start = input.getString("", "Input the 16 Character String to Use For The Start, \n"
                        + "Include The Same Type(case sensative) And Same Number of Characters From The Goal String\n(Goal String: "
                        + goal + ")");// gets the start state from the user
                if (start.length() == 16) {// checks to see if the length is 16
                    entered = start;// assign start to entered so it can be referenced outside of the while loop
                    break;// break the while loope
                }// end if length is 16
            }// end infinite while
            char[] enteredChar = entered.toCharArray();// turn entered to a char array to be mapped to ints
            scrambledArray = mapper1(enteredChar);// map enteredChar to ints
        } else {// if user wants atleast 1 random scramble
            LList blankList = new LList();// will stay blank so that isOnOpenClosed() can be utilized
            LList closed = new LList();// intialized a closed LList
            int[][] current = deepCopy(passed);// make a deep copy of current, not an alias
            int counter = 0;// intialize a counter
            while (numScrambles > 0) {// while there are still scrambles to be made
                LList children = getChildren(current);// get the children of the last scramble               
                while (true) {// infinite while
                    int random = (int) (1 + Math.random() * children.getLength());// random # 1- number of Children
                    int[][] passer = (int[][]) children.getEntry(random);// get the entry at the random number from children
                    if (!isOnOpenClosed(blankList, closed, passer)) {// if passer is not on closed
                        current = passer;// current is now the passer
                        closed.add(current);// add current to closed
                        counter++;// increment the number of actual scrambles
                        numScrambles--;// decrement number of remaining scrambles
                        break;// brek the while loop
                    } else {// if it has already been used
                        children.remove(random);// remove the entry from the list
                    }// end else
                    if (children.isEmpty()) {// if there are no children to be considered
                        numScrambles = 0;// set numScrambles to 0 so the while loop will end
                        break;// break the current while
                    }// end if there are no children to be considered
                }// end first infinite while
            }
            scrambledArray = current;// set the last scramble to scrambledArray
        }// end else
        return scrambledArray;// returns the scrambled array
    }// end randomScramble   
    public static LList parentChild = new LList();// used to store the parent and child objects
//*************************************************************************************************************************
//Method:	breadthFirst
//Description:	does the breadthFirst search if the user wants to
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static int[][] breadthFirst(int[][] passed, int[][] goal) {
        System.out.println("working....");
        parentChild.clear();
        boolean success = false;
        int[][] found = new int[passed.length][passed[0].length];
        LList open = new LList();
        LList closed = new LList();
        open.add(passed);
        while (!open.isEmpty()) {
            int[][] x = (int[][]) open.getEntry(1);
            open.remove(1);
            if (intComparer(goal, x) == true) {
                found = deepCopy(x);
                input.getString("", "Success! Press Enter To Show All Boards");
                success = true;
                break;
            } else {
                LList children = getChildren(x);
                closed.add(x);
                for (int i = 1; i <= children.getLength(); i++) {
                    int[][] passer = (int[][]) children.getEntry(i);
                    if (!isOnOpenClosed(open, closed, passer)) {
                        open.add(passer);
                        boardClass boardObject = new boardClass(x, passer, 0, 0, 0);
                        parentChild.add(boardObject);
                    }
                }
            }
        }
        if (success == true) {
            int numMoves = traceBackBreadth(found, passed);// used the flag to indicate which find parent method
            System.out.println(numMoves + " Moves Out Of " + closed.getLength() + " Considered (" + open.getLength() + " Left On Open)");
        } else {
            System.out.println("No Possible Path Found");
        }
        System.out.println("**************************************************");
        return found;
    }
//*************************************************************************************************************************
//Method:	isOnOpenClosed
//Description:	checks to see if the current array is on the Open or Closed lists for breadth-first search
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static boolean isOnOpenClosed(LList open, LList closed, int[][] passed) {
        boolean returner = false;
        int greater = open.getLength();
        if (closed.getLength() > greater) {
            greater = closed.getLength();
        }
        for (int i = greater; i > 0; i--) {
            if (i <= open.getLength()) {
                int[][] openArray = (int[][]) open.getEntry(i);
                returner = intComparer(openArray, passed);
                if (returner == true) {
                    break;
                }
            }
            if (i <= closed.getLength()) {
                int[][] closedArray = (int[][]) closed.getEntry(i);
                returner = intComparer(closedArray, passed);
                if (returner == true) {
                    break;
                }
            }

        }
        return returner;
    }
//*************************************************************************************************************************
//Method:	traceBackBreadth
//Description:	traces back the path taken in the breadth-first search in order to print it out for the user to see
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static int traceBackBreadth(int[][] current, int[][] start) {
        char showMoves = input.getCharacter(true, 'N', "Y,N", 1, "Would You Like To Step Through The Moves? (Y/N Default=N)");
        int numMoves = 0;
        LList movesInOrder = new LList();
        movesInOrder.add(1, current);
        while (!intComparer(current, start)) {
            current = findParentBreadth(current, parentChild);
            movesInOrder.add(1, current);
            numMoves++;
        }
        for (int i = 1; i <= movesInOrder.getLength(); i++) {
            int[][] printArray = (int[][]) movesInOrder.getEntry(i);
            char[][] charMoves = mapper2(printArray);
            for (int j = 0; j < printArray.length; j++) {
                for (int k = 0; k < printArray.length; k++) {
                    System.out.print(charMoves[j][k] + " ");
                }
                System.out.println("");
            }
            if (showMoves == 'Y') {
                input.getString("", "Press Enter To Step:");
            } else {
                System.out.println("");
            }
        }
        return numMoves;
    }
//*************************************************************************************************************************
//Method:	findParentBreadth
//Description:	finds the parent of the current board for the traceBackBreadth algorithm
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static int[][] findParentBreadth(int[][] passed, LList passedList) {
        int[][] returner = new int[passed.length][passed.length];
        for (int i = passedList.getLength(); i > 0; i--) {
            boardClass boardObject = (boardClass) passedList.getEntry(i);
            int[][] child = (int[][]) boardObject.actual;
            if (intComparer(passed, child)) {
                returner = deepCopy((int[][]) boardObject.parent);
                break;
            }
        }
        return returner;
    }
//*************************************************************************************************************************
//Method:	getChildren
//Description:	gets the children of any passed board
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static void bestFirst(int[][] start, int[][] goal) {
        parentChild.clear();
        boolean success = false;
        char depth = input.getCharacter(true, 'Y', "Y,N", 1, "Use Depth-Penalty? (Y/N Default:Y)");
        System.out.println("Using Sum Of Tiles Out Of Place");
        System.out.println("working...");
        int sum1 = sumTiles(start, goal);
        boardClass startBoard = new boardClass(start, start, 0, sum1, 0);// generate the startboard
        open.priorityEnqueue(startBoard);// add the startBoard to open
        while (open.isEmpty() == false) {
            boardClass x = (boardClass) open.dequeue();
            if (intComparer((int[][]) x.actual, goal) == true) {
                closed.priorityEnqueue(x);
                LList closedList = queueToList(closed);
                success = true;
                input.getString("", "Success! Press Enter To Show All Boards");
                int numMoves = traceBackBest(x, start, closedList);
                System.out.println(numMoves + " Moves Out Of " + closedList.getLength() + " Considered (" + open.getLength() + " Boards Left In Open)");
                System.out.println("**********************");
                return;
            } else {
                LList childrenX = getChildren((int[][]) x.actual);
                LList childrenBoards = new LList();
                for (int i = 1; i <= childrenX.getLength(); i++) {
                    int[][] child = (int[][]) childrenX.getEntry(i);
                    if (depth == 'Y') {
                        boardClass childBoard = new boardClass(x.actual, child, x.depth + 1, sumTiles(goal, child), x.depth + 1);
                        childrenBoards.add(childBoard);
                    } else if (depth == 'N') {
                        boardClass childBoard = new boardClass(x.actual, child, x.depth + 1, sumTiles(goal, child), 0);
                        childrenBoards.add(childBoard);
                    }
                }
                for (int i = 1; i <= childrenBoards.getLength(); i++) {
                    boardClass current = (boardClass) childrenBoards.getEntry(i);
                    boolean isOnOpen = isOnOpenBest(current);
                    boolean isOnClosed = isOnClosedBest(current);
                    if (isOnOpen == false && isOnClosed == false) {
                        open.priorityEnqueue(current);
                    }
                }// end for each child do                
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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    public static LList queueToList(LinkedQueue passed) {
        LList newList = new LList();
        while (passed.isEmpty() == false) {
            boardClass board = (boardClass) passed.dequeue();
            newList.add(board);
        }
        return newList;
    }
//*************************************************************************************************************************
//Method:	isOnOpenBest
//Description:	checks to see if the current board is on the Open list, for bestFirst
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
                    //opened.parent=current.parent;
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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Description:	maps characters to numbers, it is redundant but it works
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

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
//Parameters:   
//Returns:     	
//Calls:        
//Globals:	

    @Override
    public int compareTo(boardClass next) {
        return this.score.compareTo(next.score);
    }// end of compareTo
}
