package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Collections;
//修改3e3e

/**.
 * 图类，用于表示和操作图结构。
 * 实现图的相关操作。
 *
 * @author wangchao
 * @version 1.2
 */
public class graph { //修改graph为Graph
    public static void main(String[] args) {
        String filePath = "D:\\桌面\\软件工程\\lab\\lab_1\\test.txt";
        //String filePath = "D:\\桌面\\软件工程\\lab_1\\test1.txt";
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        int choice;
        try {
            graph = buildGraph(filePath, graph);// 通过传入文档名的串，返回创建的图
            // printGraph(graph); // 打印图的内容
            Scanner scanner = new Scanner(System.in); // 创建Scanner对象用于获取输入
            while (true) { // 使用while循环允许多次选择功能
                System.out.println("请选择要实现的选项");
                System.out.println("2.读入文本生成有向图");
                System.out.println("3.计算桥接词");
                System.out.println("4.根据桥接词生成新文本");
                System.out.println("5.计算两个词直接的最短路");
                System.out.println("6.随机游走");
                System.out.println("7.退出");
                choice = getInt(scanner);
                switch (choice) {
                    case 2: printGraph(graph); break;
                    case 3: {
                        System.out.println("输入两个词:");
                        String word1 = scanner.next();
                        String word2 = scanner.next();
                        Set<String> bridgeWord = queryBridgeWord(graph, word1, word2);
                        if (bridgeWord.isEmpty()) {// 检查 bridgeWord 是否为空
                            if (!graph.containsVertex(word1) || !graph.containsVertex(word2)) { //检查word1，2是否在图上
                                System.out.println("No " + word1 + " or " + word2 + " in the graph!");
                            } else {System.out.println("No bridge words from " + word1 + " to " + word2 + "!");}
                        } else {System.out.print("The bridge words from " + word1 + " to " + word2 + " are: ");
                            for (String words : bridgeWord) System.out.print(words + " ");}
                        break;
                    }
                    case 4: {
                        System.out.println("输入一行文本:");
                        scanner.nextLine();
                        String inputText = scanner.nextLine();
                        String modifiedText = generateNewText(graph, inputText);
                        System.out.println("基于桥接词生成文本: " + modifiedText);
                        break;
                    }
                    case 5: {
                        System.out.println("输入求最短路径的两个词:");
                        String startWord = scanner.next();
                        String endWord = scanner.next();
                        if (!graph.containsVertex(startWord) || !graph.containsVertex(endWord)) {// 检查起点和终点是否在图中
                            System.out.println("部分输入未在图中");break;}
                        List<String> shortestPath = calcShortestPath(graph, startWord, endWord); // 使用 BFS找最短路径
                        if (!shortestPath.isEmpty()) {// 输出结果
                            System.out.print("The shortest path from \"" + startWord + "\" to \"" + endWord + "\" is: ");
                            for (String word : shortestPath) {
                                System.out.print(word + " ");
                            }
                            System.out.println();
                        } else {System.out.println("There is no path from \"" + startWord + "\" to \"" + endWord + "\"");}
                        break;
                    }
                    case 6:randomWalk(graph,"D:\\桌面\\软件工程\\lab\\lab_1\\test.txt");break;
                    //case 6:randomWalk(graph,"D:\\桌面\\软件工程\\lab_1\\test1.txt");break;
                    case 7:System.out.println("退出");System.exit(0);
                    default:System.out.println("无效的选择，请输入一个2到7之间的数");break;
                }
            }
        } catch (IOException e) {
            System.err.println("错误的文件:" + e.getMessage());
        }
    }
    public static Graph<String, DefaultWeightedEdge> buildGraph(String filePath, Graph<String, DefaultWeightedEdge> graph) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath)); // 读取所有行
        StringBuilder text = new StringBuilder(); // 使用StringBuilder来累加处理后的文本
        for (String line : lines) {
            text.append(line.replaceAll("\\n", " ")).append(" ");// 将当前行的换行符替换为一个空格
        }
        String[] words = text.toString().replaceAll("[^a-zA-Z\\s]", " ").toLowerCase().split("\\s+");//匹配所有非字母和非空格的字符
        for (int i = 0; i < words.length - 1; i++) {// 构建图
            String word1 = words[i];
            String word2 = words[i + 1];
            if (!graph.containsVertex(word1)) {graph.addVertex(word1);}// 添加顶点
            if (!graph.containsVertex(word2)) {graph.addVertex(word2);}
            DefaultWeightedEdge edge = graph.getEdge(word1, word2);// 添加边并更新权重
            if (edge == null) {
                edge = graph.addEdge(word1, word2);
                graph.setEdgeWeight(edge, 1.0);
            } else {graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) + 1.0);}//权重加1
        }
        return graph;
    }
    private static void printGraph(Graph<String, DefaultWeightedEdge> graph) {
        //graph.vertexSet().forEach(System.out::println);打印所有顶点
        for (DefaultWeightedEdge edge : graph.edgeSet()) {// 打印所有边及其权重
            String word1 = graph.getEdgeSource(edge).toString();
            String word2 = graph.getEdgeTarget(edge).toString();
            double weight = graph.getEdgeWeight(edge);
            System.out.println("\"" + word1 + " -> " + word2 + " (Weight: " + weight + ")\",");
        }
    }
    public static Set<String> queryBridgeWord(Graph<String, DefaultWeightedEdge> graph, String startWord,
                                               String endWord) {
        Set<String> bridgeWord = new HashSet<>();
        Set<String> nextPoint = new HashSet<>(); // 从起始词出发寻找所有的下一个点
        if (!graph.containsVertex(startWord) || !graph.containsVertex(endWord)) {
            return bridgeWord; // 如果startWord或endWord不在图中，返回空集合
        }
        for (DefaultWeightedEdge edge : graph.edgesOf(startWord)) {//循环遍历从startWord出发的所有边（edgesOf方法返回的是一个边集合）
            String toPoint = graph.getEdgeTarget(edge).toString();
            nextPoint.add(toPoint);
        }
        for (String midWord : nextPoint) {// 检查这些中间点中是否有能到达endWord的
            if (graph.containsEdge(midWord ,endWord)) {
                bridgeWord.add(midWord);
            }
        }
        return bridgeWord; // 返回桥接词
    }
    public static String generateNewText(Graph<String, DefaultWeightedEdge> graph, String text) {
        StringBuilder modifiedText = new StringBuilder();
        String[] words = text.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase().split("\\s+"); // 分割输入文本为单词数组
        for (int i = 0; i < words.length - 1; i++) {
            modifiedText.append(words[i]).append(" "); // 添加当前单词到modifiedText
            Set<String> bridgeWords = queryBridgeWord(graph, words[i], words[i + 1]);
            if (!bridgeWords.isEmpty()) {//存在桥接词，使用迭代器随机选择一个插入
                int random = (int) (Math.random() * bridgeWords.size());//产生0-size随机值(Math.random:0-1)
                int count = 0;
                for (String bridgeWord : bridgeWords) {
                    if (count++ == random) {modifiedText.append(bridgeWord).append(" ");break;}
                }
            }
        }
        modifiedText.append(words[words.length - 1]); // 添加最后一个单词
        return modifiedText.toString();
    }
    public static List<String> calcShortestPath(Graph<String, DefaultWeightedEdge> graph, String startWord, String endWord) {
        if (!graph.containsVertex(startWord) || !graph.containsVertex(endWord)) {// 检查起点和终点是否在图中
            return Collections.emptyList();}
        Set<String> visited = new HashSet<>();
        Queue<NodeInfo> queue = new LinkedList<>();
        Map<String, String> predecessor = new HashMap<>();
        Map<String, Double> weightMap = new HashMap<>();//保存权重

        visited.add(startWord);
        queue.add(new NodeInfo(startWord, null, 0.0)); // 初始化队列，起始节点的权重为0
        predecessor.put(startWord, null);
        weightMap.put(startWord, 0.0);
        while (!queue.isEmpty()) {
            NodeInfo current = queue.poll();
            String currentPoint = current.node;
            if (currentPoint.equals(endWord)) { // 如果到达终点，构建并返回路径
                return buildPath(predecessor, endWord);
            }
            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(currentPoint)) {//遍历当前点的所有 有向边
                String nextPoint = graph.getEdgeTarget(edge).toString();//获得相邻的指向的下一个点
                if (!visited.contains(nextPoint)) {//未被访问，加入到visited
                    visited.add(nextPoint);
                    double edgeWeight = graph.getEdgeWeight(edge);
                    double weightCurrent = current.weight + edgeWeight;//计算权值（费用）
                    if (!weightMap.containsKey(nextPoint) || weightCurrent < weightMap.get(nextPoint)) {
                        //检查weightMap中是否不包含键nextPoint
                        //如果nextPoint已经在weightMap中，且当前记录的权重是否大于通过当前节点current的权重，应该更新weightMap中的记录
                        queue.add(new NodeInfo(nextPoint, currentPoint, weightCurrent));
                        predecessor.put(nextPoint, currentPoint);
                        weightMap.put(nextPoint, weightCurrent);
                    }
                }
            }
        }
        return Collections.emptyList(); // 如果没有路径，则返回空
    }
    private static List<String> buildPath(Map<String, String> predecessor, String endWord) { //根据前驱节点映射构建路径
        List<String> path = new ArrayList<>();
        String current = endWord;
        while (current != null) {
            path.add(0, current); // 将当前节点添加到路径的开始
            current = predecessor.get(current);
        }
        return path;
    }
    static class NodeInfo {// 用于存储节点信息的内部类
        String node;
        String prev; // 到达当前节点的前一个节点
        double weight; // 从起始节点到当前节点的路径权重
        public NodeInfo(String node, String prev, double weight) {
            this.node = node;
            this.prev = prev;
            this.weight = weight;
        }
    }
    private static void randomWalk(Graph<String, DefaultWeightedEdge> graph, String filePath) {
        List<String> allWords = new ArrayList<>();
        try (Scanner scanner = new Scanner(Files.newInputStream(Path.of(filePath)))) {
            // 读取文件并分割成单词，同时转换为小写
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase().replaceAll("[^a-z]", "");// 移除单词中的特殊字符
                if (!word.isEmpty()) {allWords.add(word);}
            }
            Random random = new Random(); // 从列表中随机选择一个单词作为起始点
            String startNode = allWords.get(random.nextInt(allWords.size()));
            if (!graph.containsVertex(startNode)) {
                System.out.println("所选的起始点为 " + startNode);
                System.out.println("所选的起始点不在图中");
                return;
            }
            if (startNode == null) {System.out.println("图是空的");return;}

            Map<DefaultWeightedEdge, Boolean> visitedEdges = new HashMap<>();// 用于存储访问过的边
            StringBuilder walkPath = new StringBuilder(); // 用于存储随机游走的路径输出到控制台
            List<String> walk = new ArrayList<>();  // 用于存储随机游走的路径到txt文件
            walkPath.append(startNode);
            walk.add(startNode);
            String currentNode = startNode;// 当前节点
            while (true) {
                List<DefaultWeightedEdge> unvisitedEdges = new ArrayList<>();
                for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(currentNode)) {// 获得未访问的出边集合
                    if (!visitedEdges.containsKey(edge)) {unvisitedEdges.add(edge);}
                }
                if (unvisitedEdges.isEmpty()) {break;} // 如果没有出边，结束游走
                DefaultWeightedEdge edge = unvisitedEdges.get(new Random().nextInt(unvisitedEdges.size()));
                if (!graph.getEdgeTarget(edge).toString().equals(currentNode)) {//测试的时候不知道为什么出现new，new重复
                    String nextNode = graph.getEdgeTarget(edge).toString();
                    visitedEdges.put(edge, true);// 标记边为已访问
                    currentNode = nextNode;// 移动到下一个节点
                    walk.add(currentNode);
                    walkPath.append(" -> "+currentNode);
                }
            }
            System.out.println("随机游走路径: " + walkPath);// 输出游走路径
            try {  // 将游走路径写入文件
                Path path = Path.of("D:\\桌面\\软件工程\\lab_1\\walk.txt");
                Files.write(path, walk);
                System.out.println("随机游走已写入文件");
            } catch (IOException e) {
                System.err.println("错误的写入: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("错误的文件： " + e.getMessage());
        }
    }
    private static int getInt(Scanner scanner) {
        System.out.println("输入你的选择 (2-7): ");
        while (!scanner.hasNextInt()) {
            System.out.print("请输入一个有效的整数. ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

  /*
   List<String> allWords = new ArrayList<>();
        try (Scanner scanner = new Scanner(Files.newInputStream(Path.of(filePath)))) {
            // 读取文件并分割成单词，同时转换为小写
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                // 移除单词中的特殊字符
                word = word.replaceAll("[^a-z]", "");
                if (!word.isEmpty()) {
                    allWords.add(word);
                }
            }
            // 从列表中随机选择一个单词作为起始点
            Random random = new Random();
            String startNode = allWords.get(random.nextInt(allWords.size()));
            if (!graph.containsVertex(startNode)) {
                System.out.println("The selected start node is "+startNode);
                System.out.println("The selected start node is not in the graph.");
                return;
            }
            if (startNode == null) {
            System.out.println("Graph is empty.");
            return;
        }
        Map<DefaultWeightedEdge, Boolean> visitedEdges = new HashMap<>();// 用于存储访问过的边
        StringBuilder walkPath = new StringBuilder(startNode);
        List<String> walk = new ArrayList<>(); // 用于存储随机游走的路径
        walk.add(startNode);
        String currentNode = "civilizations";//startNode;// 当前节点
        while (!graph.outgoingEdgesOf(currentNode).isEmpty()) {
            List<DefaultWeightedEdge> outEdges = new ArrayList<>();//出边集合
            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(currentNode)) {
                outEdges.add(edge);
            }
            DefaultWeightedEdge edge = outEdges.get(new Random().nextInt(outEdges.size()));// 选择一个随机的出边
            if (!graph.getEdgeTarget(edge).toString().equals(currentNode)) {//测试的时候不知道为什么总是出现new，new重复
                if (visitedEdges.containsKey(edge)) {
                    currentNode = graph.getEdgeTarget(edge).toString();
                    walkPath.append(" -> ").append(currentNode);//-------
                    break;
                }
                String nextNode = graph.getEdgeTarget(edge).toString();
                visitedEdges.put(edge, true);  // 标记边为已访问
                if (currentNode == nextNode) {
                    break;
                }
                currentNode = nextNode;   // 移动到下一个节点
                walk.add(currentNode);
                walkPath.append(" -> ").append(currentNode);
            }
        }
            System.out.println("Random walk path: " + walkPath);// 输出游走路径
            try {  // 将游走路径写入文件
                Path path = Path.of("D:\\桌面\\软件工程\\lab_1\\walk.txt");
                Files.write(path, walk);
                System.out.println("Random walk path has been written to file.");
            } catch (IOException e) {System.err.println("Error writing to file: " + e.getMessage());}
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
     } */


