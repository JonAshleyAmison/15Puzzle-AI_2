//test

public class Amison2 {

    public static KeyboardInputClass input = new KeyboardInputClass();// access to KeyBoardInputClass()
    public static LList list = new LList();// list to use the greatest path

    public static void main(String[] args) {
        while (true) {
            char[] x = new char[16];// convert the string to an array of chars
            while (true) {
                String goal = input.getString("", "Input the 16 Character String to Use For The Goal, "
                        + "\nInclude Only Letters and Atleast 1 Space");
                x = goal.toCharArray();// convert the string to an array of chars
                boolean spacer = false;// initialoze spacer
                for (int i = 0; i < x.length; i++) {// space checker for
                    if (x[i] == ' ') {// space checker if
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
            int[][] test = mapper1(x);
            for (int i = 0; i < test.length; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print(test[i][j]+" ");

                }
                System.out.println("");

            }
            System.out.println("");
            char[][] test2 = mapper2(test);
            for (int i = 0; i < test.length; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print(test2[i][j]+" ");

                }
                System.out.println("");

            }
            String quitMe = input.getString("", "Exit? (Y/N Default=N)");// asks the user if they want to exit the program
            if (quitMe.equals("Y") || quitMe.equals("y")) {// if user wants to exit
                break;// terminates the program
            }// end if user wants to exit

        }// end of total while
    }// end of main

    public static int[][] mapper1(char[] passer) {
        int[] usedInside = new int[16];
        for (int i = 0; i < passer.length; i++) {
            if (passer[i] == ' ') {
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

    public static char[][] mapper2(int[][] passer) {
        char[][] usedInside = new char[4][4];
        for (int i = 0; i < passer.length; i++) {
            for (int j = 0; j < passer[0].length; j++) {
                if (passer[i][j] == 0) {
                    usedInside[i][j] = ' ';
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
