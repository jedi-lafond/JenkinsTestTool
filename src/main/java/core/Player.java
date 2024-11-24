package core;

/**
 * Node class used to create a player or cpu Node object.
 *
 * @author jedidiahlafond
 * @version 0.04
 * @see Connect4Logic
 * @see Connect4ComputerPlayer
 */
public class Player {
    private String name;
    private final char marking;
    private final int[] record;

    /**
     * Set name.
     *
     * @param s the s
     */
    public void setName(String s){
        this.name = s;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets marking.
     *
     * @return the marking
     */
    public char getMarking() {
        return marking;
    }


    /**
     * Get record int [ ].
     *
     * @return the int [ ]
     */
    public int[] getRecord() {
        return record;
    }

    /**
     * Set record.
     *
     * @param i the
     */
    public void setRecord(int i){
        this.record[i]++;
    }

    /**
     * Instantiates a new Player.
     */
    public Player(){
        this.name = "Default";
        this.marking = 'O';
        this.record = new int[]{0,0,0};
    }

    /**
     * Instantiates a new Player.
     *
     * @param name    the name
     * @param marking record initialized to [0, 0, 0] upon instantiation
     */
    public Player(String name, char marking){
        this.name = name;
        this.marking = marking;
        this.record = new int[]{0,0,0};
    }
}
