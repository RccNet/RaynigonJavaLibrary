package com.raynigon.lib.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementNodeList extends AbstractNodeList<Element> implements Iterable<Element>{
    
    private List<Element> elements;
    private NodeListEx nlEx;
    
    public ElementNodeList(NodeList nl){
        nlEx = new NodeListEx(nl);
        init();
    }

    public ElementNodeList(Element element){
        nlEx = new NodeListEx(element);
        init();
    }
    
    public ElementNodeList(Element element, String name){
        nlEx = new NodeListEx(element, name);
        init();
    }
    
    private void init(){
        elements = new ArrayList<>();
        for(Node node : nlEx){
            if(node instanceof Element)
                elements.add((Element) node);
        }
    }
    
    public Element get(int pos){
        return elements.get(pos);
    }
    
    public int size(){
        return elements.size();
    }

    @Override
    public Iterator<Element> iterator(){
        return new NodeListIterator<Element>(this);
    }
}
