package com.example.ecom.utils;

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
public class Event<T> {
    private boolean hasBeenHandled = false;
    private T content;

    /**
     * Returns the content and prevents its use again.
     */
    public Event(T content){
      this.content = content;
    }

    public boolean isHasBeenHandled() {
        return hasBeenHandled;
    }

    public T getContentIfNotHandled(){
        if(hasBeenHandled)
            return null;
        else{
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */

    public T peekContent(){
        return content;
    }
}
