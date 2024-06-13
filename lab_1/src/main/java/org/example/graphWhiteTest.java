package org.example;

import junit.framework.TestCase;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class graphWhiteTest extends TestCase {

    private Graph<String, DefaultWeightedEdge> graphTest;

    @Before
    public void setUp() throws IOException {      // 初始化图并加载测试数据
        graphTest = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        String filePath = "D:\\桌面\\软件工程\\lab\\lab_1\\test.txt";
        graphTest = graph.buildGraph(filePath, graphTest);
    }

    @Test
    public void testFirst() {// 测试用例1: graphTest, strange, world
        Set<String> expectedBridgeWords1 = new HashSet<>();
        Set<String> BridgeWords1 = graph.queryBridgeWord(graphTest, "strange", "world");
        assertEquals(expectedBridgeWords1, BridgeWords1);
    }

    @Test
    public void testSecond() {// 测试用例2: graphTest, worlds, strange
        Set<String> expectedBridgeWords2 = new HashSet<>();
        Set<String> BridgeWords2 = graph.queryBridgeWord(graphTest, "worlds", "strange");
        assertEquals(expectedBridgeWords2,BridgeWords2);
    }
    @Test
    public void testThird() {// 测试用例3: graphTest, strange, worlds
        Set<String> expectedBridgeWords3 = new HashSet<>();
        expectedBridgeWords3.add("new");
        Set<String> BridgeWords3 = graph.queryBridgeWord(graphTest, "strange", "worlds");
        assertEquals(expectedBridgeWords3,BridgeWords3);
    }


}