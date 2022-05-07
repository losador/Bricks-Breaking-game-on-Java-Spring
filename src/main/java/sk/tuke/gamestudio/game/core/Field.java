package sk.tuke.gamestudio.game.core;

import lombok.Data;

import java.util.Random;

@Data
public class Field {

    private final int ROWS; //size of y coord
    private final int COLUMNS;// size of x coord
    private int score;
    private Tile[][] fieldArray;
    private GameState state;
    private Random rand = new Random();
    private int singleDeleteCount; // count of possibility to deleting single tile
    private int tilesToDelete; //tiles counter to be deleted, used in the methods markTiles() and deleteTiles()

    public Field(int rowCount, int columnCount){
        this.ROWS = rowCount;
        this.COLUMNS = columnCount;
        this.score = 0;
        this.fieldArray = new Tile[ROWS][COLUMNS];
        this.singleDeleteCount = 5;
        this.tilesToDelete = 0;
        generateTiles();
    }

    /**
     * Generates random color tile on each position in array
     */
    public void generateTiles(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                this.fieldArray[i][j] = new Tile(getRandomColor());
            }
        }
    }

    /**
     * Deletes marked tiles from fieldArray
     */
    public void deleteTiles(){
        //check if you can delete only one tile
        if(this.tilesToDelete == 1) {
            if(this.singleDeleteCount > 0) this.singleDeleteCount--;
            else if (this.singleDeleteCount == 0){
                System.out.println("You are not able to delete one tile");
                this.tilesToDelete = 0;
                return;
            }
        }

        int deleted = 0;

        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                if(fieldArray[i][j].isMarked()) { //if tile is marked, unmark and delete it
                    fieldArray[i][j].unmark();
                    fieldArray[i][j].setTileColor(Color.NONE);
                    deleted++;
                }
            }
        }
        this.score += (3 * deleted) * deleted;
        this.tilesToDelete = 0;
    }

    /**
     * Marks tiles to delete. First tile is tile which user specifies
     * @param row y coordinate of first tile
     * @param column x coordinate of first tile
     */
    public void markTiles(int row, int column){
        fieldArray[row][column].mark();
        this.tilesToDelete++;
        //checks upper tile
        if(row != 0 && fieldArray[row-1][column].getTileColor() == fieldArray[row][column].getTileColor() && !fieldArray[row-1][column].isMarked()) markTiles(row - 1, column);
        //checks lower tile
        if(row != ROWS - 1 && fieldArray[row+1][column].getTileColor() == fieldArray[row][column].getTileColor() && !fieldArray[row+1][column].isMarked()) markTiles(row + 1, column);
        //checks left tile
        if(column != 0 && fieldArray[row][column-1].getTileColor() == fieldArray[row][column].getTileColor() && !fieldArray[row][column-1].isMarked()) markTiles(row, column - 1);
        //checks right tile
        if(column != COLUMNS - 1 && fieldArray[row][column+1].getTileColor() == fieldArray[row][column].getTileColor() && !fieldArray[row][column+1].isMarked()) markTiles(row, column + 1);
    }

    /**
     * Checks if level is solved
     * @return if there are mo colored tile true else false
     */
    public boolean isSolved(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++) {
                if(fieldArray[i][j].getTileColor() == Color.BLUE || fieldArray[i][j].getTileColor() == Color.RED || fieldArray[i][j].getTileColor() == Color.YELLOW) return false;
            }
        }
        return true;
    }

    /**
     * Checks if level is failed. Count the number of tiles of each color and see if it possible to remove them.
     */
    public void isFailed(){
        int[] colors = new int[3];
        int allSum = 0;

        for(int i = 0; i < ROWS; i++){  //count the number of tiles of each color like (red = x, blue = x, yellow = x)
            for(int j = 0; j < COLUMNS; j++){
                if(fieldArray[i][j].getTileColor() == Color.RED) colors[0]++;
                else if(fieldArray[i][j].getTileColor() == Color.YELLOW) colors[1]++;
                else if(fieldArray[i][j].getTileColor() == Color.BLUE) colors[2]++;
            }
        }

        for(int c : colors){ // if count of tiles < 2, add it to the total sum
            if(c < 2) allSum+=c;
        }

        if(singleDeleteCount < allSum) this.state = GameState.FAILED; // if total sum is less than the number of singleCountDelete, then game if failed
    }

    /**
     * Updates field, firstly in y coord, then in x coord.
     * Moves tiles down if it possible and offsets columns towards each other if there ia an empty column between them
     */
    public void updateField(){
        // Check rows
        for(int i = ROWS - 1; i > 0; i--){ //checks each line starting from the last one
            for(int j = 0; j < COLUMNS; j++){
                if(fieldArray[i][j].getTileColor() == Color.NONE){ // if there are no tile checks upper tile
                    checkUpperTile(i - 1, j);
                }
            }
        }
        // Check columns
        int columnsToDelete = checkForEmptyColumns() + 1; //checks how many empty columns are in field
        while(columnsToDelete != 0) {
            for (int j = 1; j < COLUMNS; j++) {
                if(fieldArray[ROWS-1][j].getTileColor() == Color.NONE) deleteColumn(j); // if there is an empty column, delete it
            }
            columnsToDelete--;
        }
    }

    private int checkForEmptyColumns(){
        int count = 0;
        for(int j = 1; j < COLUMNS; j++){
            if(fieldArray[ROWS-1][j].getTileColor() == Color.NONE) count++;
        }
        return count;
    }

    private void deleteColumn(int column){
        for(int j = column + 1; j < COLUMNS; j++) {
            for(int i = ROWS-1; i >= 0; i--){
                if (fieldArray[i][j - 1].getTileColor() != Color.NONE || fieldArray[i][j].getTileColor() == Color.NONE) break;
                fieldArray[i][j - 1].setTileColor(fieldArray[i][j].getTileColor());
                fieldArray[i][j].setTileColor(Color.NONE);

            }
        }
    }

    private void checkUpperTile(int row, int column){
        if(fieldArray[row][column].getTileColor() == Color.NONE) return; //if there are no tile stops
        for(int i = row; i < ROWS - 1; i++){ //if there are tile, moves it to the line below
            if(fieldArray[i+1][column].getTileColor() == Color.NONE){
                fieldArray[i+1][column].setTileColor(fieldArray[i][column].getTileColor());
                fieldArray[i][column].setTileColor(Color.NONE);
            }
        }
    }

    /**
     * Generates random color from Color enum
     * @return generated color
     */
    private Color getRandomColor(){
        switch(rand.nextInt(3)){
            case 0: return Color.RED;
            case 1: return Color.YELLOW;
            case 2: return Color.BLUE;
            default: break;
        }
        return null;
    }
}
