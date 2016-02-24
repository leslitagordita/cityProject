/*
 StackElem to hold City objects in a Stack
 */
package cityproject;

/**
 *
 * @author lesliesalazar
 */
public class StackElem 
{ //pointer to String
    private City data;
    //pointer to next StackElem
    private StackElem nextElem;
    
    //constructor sets property pointers to null
    public StackElem()
    {
        this.data = null;
        this.nextElem = null;
    }
    
    //constructor with pointer to String
    public StackElem(City dataElem)
    {
        this.data = dataElem;
    }
    
    //constructor with pointer to string and pointer to next StackElem
    public StackElem(City dataElem, StackElem nextDataElem)
    {
        this.data = dataElem;
        this.nextElem = nextDataElem;
    }
    
    //accessor methods
    public City getStackData()
    {
        return data;
    }
    
    public StackElem getNextStacElem()
    {
        return nextElem;
    }
    
    //mutator methods
    public void setStackData(City dataElem)
    {
        this.data = dataElem;
    }
    
    public void setNextStackElem(StackElem nextStackElem)
    {
        this.nextElem = nextStackElem;
    }
    
}
