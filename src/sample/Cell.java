package sample;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static sample.Game.*;

public class Cell extends Rectangle{

    private Piece piece;
    // Cell constructor
    public Cell(int x,int y,Color color){
        setFill(color);
        setWidth(CELL_SIZE);
        setHeight(CELL_SIZE);
        setStroke(Color.BLACK);
        setStrokeWidth(1);
        relocate(x*CELL_SIZE,y*CELL_SIZE);

    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty(){
        return piece==null;
    }
}
