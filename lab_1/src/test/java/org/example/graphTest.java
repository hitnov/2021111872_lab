package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class graphTest {
    private Graph<String, DefaultWeightedEdge> graphTest;

    @Before
    public void setUp() throws IOException {
        // 初始化图，这里使用 test.txt 文件来构建图
        String filePath = "D:\\桌面\\软件工程\\lab\\lab_1\\test.txt";
        graphTest = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        // 假设有一个GraphUtil类，其中包含buildGraph方法
        graph.buildGraph(filePath, graphTest);
    }

    public List<String> calcShortestPath(String startWord, String endWord) {
        // 调用 graph 类的 calcShortestPath 方法
        return graph.calcShortestPath(graphTest, startWord, endWord);
    }

    @Test
    public void testOne() {
        List<String> shortestPath = calcShortestPath("to", "life");
        // 将期望的路径转换为列表
        List<String> expectedPath = Arrays.asList("to", "explore", "strange", "new", "life");
        assertEquals(expectedPath, shortestPath); // 比较两个列表是否相等
    }
    @Test
    public void testTwo() {
        List<String> shortestPath = calcShortestPath("civilizations", "worlds");
        assertTrue(shortestPath.isEmpty()); // 假设没有路径时返回空列表
    }

    @Test
    public void testThree() {
        List<String> shortestPath = calcShortestPath("too", "seek");
        assertTrue(shortestPath.isEmpty()); // 假设部分输入不在图中时返回空列表
    }

    @Test
    public void testFour() {
        List<String> shortestPath = calcShortestPath("out", "leave");
        assertTrue(shortestPath.isEmpty()); // 同上
    }
}