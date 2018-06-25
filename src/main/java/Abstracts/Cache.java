package Abstracts;

import java.util.Vector;

/**
 * Created by J.A Rodríguez on 14/06/2018.
 */
public abstract class Cache {

    protected Vector<Integer> tags;
    protected Vector<Character> states;

    public void setSate(int numBlock, char state){
        this.states.set(numBlock, state);
    }

    public char getState(int numBlock){
        return this.states.get(numBlock);
    }

    public void setTag(int numBlock, int tag){
        this.tags.set(numBlock, tag);
    }

    public int getTag(int numBlock){
        return this.tags.get(numBlock);
    }

    public Vector<Integer> getTags(){
        return this.tags;
    }

    public Vector<Character> getStates(){
        return this.states;
    }
}
