/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cityproject;

/**
 *
 * @author lesliesalazar
 */
public class Stack 
{
     //tail StackElem of Stack
    private StackElem tail;
    //top StackElem of Stack
    private StackElem top;
    
    //Stack contructor assigns null to tail and top properties
    public Stack()
    {
        this.tail = null;
        this.top = null;
    }
    
    //Accessor methods     
    public StackElem getTop()
    {
        return top;
    }
    
    public StackElem getTail()
    {
        return tail;
    }
    
    //Mutator methods
    public void setTop(StackElem elemToAdd)
    {
        this.top = elemToAdd;
    }
    
    public void setTail(StackElem elemToAdd)
    {
        this.tail = elemToAdd;
        StackElem temp;
        temp = elemToAdd;
        StackElem currentElem = top;

        if(top == null)
        {
            this.top = temp;
        }
        else
        {
            while(currentElem.getNextStacElem() != null)
            {
              currentElem = currentElem.getNextStacElem();
            }//end while
            currentElem.setNextStackElem(temp);
            
        }
    }//end setTail()
    
    //will crawl Stack and return integer of number of StackElems in Stack
    public int stackSize()
    {
        int i;
        StackElem currentElem = top;
        
        for(i = 0; currentElem != null; i++)
            {
                currentElem = currentElem.getNextStacElem();
            }
        return (i);
    }
     
    //push method takes as parameter a StackElem to add to the top of the Stack
    //method keeps track of tail poistion withing Stack
    public void push(StackElem elemToAdd)
    {
        StackElem currentElem = tail;
        if(top == null)
        {
            top = elemToAdd;
            tail = top;
        }else
        {
            elemToAdd.setNextStackElem(top);
            top = elemToAdd; 
        }
    }//end push()
    
    //method returns Stack's top StackElem and removes it from Stack
    public StackElem pop()
    {
        StackElem tempTop= top;
        
        if(top == null)
        {
            System.out.println("The Stack is empty");
        }else
        {
            
            tempTop = top;
            top = top.getNextStacElem();
            
            //return tempTop;
        } 
        return tempTop;
    }
    
    //method crawls Stack from top and prints to console data from StackElem
    public void printStack()
    {
        StackElem currentElem = top;
        String wordString;
        
        if(this.top ==null)
        {
            System.out.println("Stack is empty");
        }else if (currentElem != null)
            {

                while(currentElem != null)
                {
                    wordString = currentElem.getStackData().getName();
                    System.out.println("String from Stack's printStack method "+wordString);

                    currentElem = currentElem.getNextStacElem();

                }//end while
                
            }//end else if
        
    }//end printStack()
    
    
}
