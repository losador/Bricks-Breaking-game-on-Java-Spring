package consoleUI;

import core.Field;
import core.GameState;
import lombok.Data;

import java.util.Scanner;

@Data
public class ConsoleUI {

    private Field field;
    private int rowCount;
    private int columnCount;

    public void printField(){
        System.out.println("Your score: " + this.field.getScore());

        printStars();
        printCoordinates();

        for(int i = 0; i < this.field.getROWS(); i++){
            for(int j = 0; j < this.field.getCOLUMNS(); j++) {
                System.out.print(this.field.getFieldArray()[i][j].toString() + "    ");
            }
            System.out.print(i);
            System.out.println();
        }
    }

    private void printCoordinates() {
        for(int i = 0; i < this.field.getCOLUMNS(); i++){
            if(i < 9) System.out.print(i + "    ");
            if(i >= 9) System.out.print(i + "   ");
        }
        System.out.println();
    }

    private void printStars(){
        for(int i = 0; i < field.getSingleDeleteCount(); i++){
            System.out.print("* ");
        }
        System.out.println();
    }

    public void play(){
        do{
            printField();
            handleInput();
            if(field.isSolved()){
                field.setScore(field.getScore() + 500);
                field.generateTiles();
            }
            field.isFailed();
        } while(field.getState() == GameState.PLAYING);
    }

    public void handleInput(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter tile coordinates of tile you want to delete: ");
        int row = sc.nextInt(), column = sc.nextInt();
        field.markTiles(row, column);
        field.deleteTiles();
        field.updateField();
    }

    private void startText(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to BricksBreaking game");
        System.out.println("Enter size of field (y,x)");
        rowCount = sc.nextInt();
        columnCount = sc.nextInt();
    }

    public void initializeField(){
        Scanner sc = new Scanner(System.in);
        startText();
        String mode = getGeneratingMode();
        StringBuilder path = new StringBuilder("src/main/java/");
        if(mode.equals("l")){
            System.out.println("Put your file in src/main/java and enter name of file");
            path.append(sc.nextLine());
            field = new Field(rowCount, columnCount, path.toString());
        } else{
            field = new Field(rowCount, columnCount);
        }
    }

    private String getGeneratingMode(){
        Scanner sc = new Scanner(System.in);
        System.out.println("If you want to generate field randomly type 'g', if you want to load field from file type 'l'");
        return sc.nextLine();
    }

}