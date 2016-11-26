package com.raynigon.lib.xml;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class NodeListEx extends AbstractNodeList<Node> implements Iterable<Node>{

    private NodeList nodes;
    
    public NodeListEx(NodeList nl){
        nodes = nl;
    }
    
    public NodeListEx(Element element){
        nodes = element.getChildNodes();
    }
    
    public NodeListEx(Element element, String name){
        nodes = element.getElementsByTagName(name);
    }

    public Node get(int pos){
        return nodes.item(pos);
    }
    
    public int size(){
        return nodes.getLength();
    }
    
    public int getLength(){
        return size();
    }
    
    public Node item(int index){
        return get(index);
    }
    
    @Override
    public Iterator<Node> iterator(){
        return new NodeListIterator<Node>(this);
    }
}
