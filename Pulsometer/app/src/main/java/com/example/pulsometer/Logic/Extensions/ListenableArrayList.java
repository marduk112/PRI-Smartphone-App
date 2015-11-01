package com.example.pulsometer.Logic.Extensions;

import com.example.pulsometer.Logic.Interfaces.ListListener;
import com.example.pulsometer.Logic.Interfaces.Listenable;

import java.util.ArrayList;

public class ListenableArrayList<T> extends ArrayList<T>
        implements Listenable<T> {

    private ArrayList<T> internalList = new ArrayList<>();
    private ListListener<T> listener;

    /* .. */

    @Override
    public boolean add(T item) {
        boolean result = internalList.add(item);
        if (listener != null && result)
            listener.afterAdd(item);
        return result;
    }

    public ArrayList<T> getList() {
        return internalList;
    }
/* .. */
    public boolean isSetListener(){
        return this.listener != null;
    }

    public void setListener(ListListener<T> listener) {
        this.listener = listener;
    }

}
