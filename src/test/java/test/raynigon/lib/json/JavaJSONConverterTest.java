package test.raynigon.lib.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.raynigon.lib.json.Expose;
import com.raynigon.lib.json.JavaJSONConverter;
import com.raynigon.lib.json.SerializedName;

public class JavaJSONConverterTest{

    @Test
    public void SimpleJavaToJSON(){
        JSONObject obj = JavaJSONConverter.fromJavaObject(new TestEntity());
        assertEquals("{\"a\":5,\"b\":\"#1#\",\"c\":0.5}", obj.toString());
    }

    @Test
    public void AnnotationsTest(){
        JSONObject obj = JavaJSONConverter.fromJavaObject(new TestEntity2());
        assertEquals("{\"FileName\":\"TestFileName\"}", obj.toString());
    }
    
    @Test
    public void InheritanceTest(){
        JSONObject obj = JavaJSONConverter.fromJavaObject(new TestEntity3());
        assertEquals("{\"a\":5,\"b\":\"#1#\",\"c\":0.5,\"id\":\"Mist\"}", obj.toString());
    }
    
    @Test
    public void CollectionJavaToJSON(){
        Collection<TestEntity> collection = new ArrayList<>();
        collection.add(new TestEntity());
        collection.add(new TestEntity());
        collection.add(new TestEntity());
        JSONArray obj = JavaJSONConverter.fromJavaCollection(collection);
        StringBuilder expected = new StringBuilder();
        expected.append("[");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5}");
        expected.append("]");
        assertEquals(expected.toString(), obj.toString());
    }
    
    @Test
    public void ArrayMultiDimensionalJavaToJSON(){
        TestEntity[][] array = new TestEntity[3][3];
        for(TestEntity[] a : array){
            Arrays.fill(a, new TestEntity());
        }
        JSONArray obj = JavaJSONConverter.fromJavaArray(array);
        StringBuilder expected = new StringBuilder();
        expected.append("[[");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5}");
        expected.append("],");
        expected.append("[");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5}");
        expected.append("],");
        expected.append("[");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5},");
        expected.append("{\"a\":5,\"b\":\"#1#\",\"c\":0.5}");
        expected.append("]]");
        assertEquals(expected.toString(), obj.toString());
    }
    
    @SuppressWarnings("unused")
    private static class TestEntity implements Serializable{
        
        private static final long serialVersionUID = -3709895569229813808L;
        
        int a = 5;
        String b = "#1#";
        float c = 0.5f;
    }
    
    //@SuppressWarnings("unused")
    private static class TestEntity2 implements Serializable{
        
        private static final long serialVersionUID = -3709895569229813808L;
    
        @Expose
        File file = new File("");
        
        @SerializedName(name = "FileName")
        public String getFileName(){
            return "TestFileName";
        }
    }
    
    @SuppressWarnings("unused")
    private static class TestEntity3 extends TestEntity implements Serializable{
        
        private static final long serialVersionUID = -3709895569229813808L;
    
        String id = "Mist";
        
        @Expose
        String text = "Mist";
    }
}
