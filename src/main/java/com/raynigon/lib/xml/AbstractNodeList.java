package com.raynigon.lib.xml;

import java.util.Iterator;

import org.w3c.dom.Node;

public abstract class AbstractNodeList<T extends Node>{


    public abstract T get(int index);
    public abstract int size();
    

    public T item(int index){
        return get(index);
    }

    public int getLength(){
        return size();
    }

    protected static class NodeListIterator<U extends Node> implements Iterator<U>{

        private AbstractNodeList<U> nodeList;
        private int pos;
        
        public NodeListIterator(AbstractNodeList<U> nodeListEx){
            nodeList = nodeListEx;
            pos = -1;
        }

        @Override
        public boolean hasNext(){
            if((pos+1)>=nodeList.size())
                return false;
            return true;   
        }
        
        @Override
        public U next(){
            if(!hasNext())
                return null;
            pos++;
            return nodeList.get(pos);
        }

		@Override
		public void remove() {
			throw new RuntimeException("Not implemented");
		}
    }
}
