import networkx as nx
import matplotlib.pyplot as plt

# 初始化一个有向图
G = nx.DiGraph()

# 模拟您提供的文本格式数据
text_lines = [
"to -> explore (Weight: 1.0)",
"explore -> strange (Weight: 1.0)",
"strange -> new (Weight: 1.0)",
"new -> worlds (Weight: 1.0)",
"worlds -> to (Weight: 1.0)",
"to -> seek (Weight: 1.0)",
"seek -> out (Weight: 1.0)",
"out -> new (Weight: 1.0)",
"new -> life (Weight: 1.0)",
"life -> and (Weight: 1.0)",
"and -> new (Weight: 1.0)",
"new -> civilizations (Weight: 1.0)",
"civilizations -> unlock (Weight: 1.0)",
"unlock -> secrets (Weight: 2.0)",
"secrets -> of (Weight: 2.0)",
"of -> human (Weight: 1.0)",
"human -> mind (Weight: 1.0)",
"mind -> unlock (Weight: 1.0)",
"of -> the (Weight: 1.0)",
"the -> world (Weight: 1.0)",
"world -> discover (Weight: 1.0)",
"discover -> unseen (Weight: 1.0)",
"unseen -> beauty (Weight: 1.0)",
"beauty -> capture (Weight: 1.0)",
"capture -> the (Weight: 1.0)",
"the -> essence (Weight: 1.0)",
"essence -> of (Weight: 1.0)",
"of -> art (Weight: 1.0)",
"art -> and (Weight: 1.0)",
"and -> nature (Weight: 1.0)",
"nature -> achieve (Weight: 1.0)",
"achieve -> personal (Weight: 1.0)",
"personal -> growth (Weight: 1.0)",
"growth -> discover (Weight: 1.0)",
"discover -> the (Weight: 1.0)",
"the -> spark (Weight: 1.0)",
"spark -> of (Weight: 1.0)",
"of -> creativity (Weight: 1.0)",
"creativity -> and (Weight: 1.0)",
"and -> imagination (Weight: 1.0)",
"imagination -> create (Weight: 1.0)",
"create -> innovative (Weight: 1.0)",
"innovative -> idea (Weight: 1.0)"
]

# 解析文本行并添加边和权重到图中
for line in text_lines:
    parts = line.split(" -> ")
    start_node = parts[0].strip()
    end_node_info = parts[1].split(" (")
    end_node = end_node_info[0].strip()
    weight = float(end_node_info[1].split(":")[1].strip().strip(')'))

    # 添加节点和边
    if not G.has_node(start_node):
        G.add_node(start_node)
    if not G.has_node(end_node):
        G.add_node(end_node)
    G.add_edge(start_node, end_node, weight=weight)

# 绘制图形
pos = nx.spring_layout(G)  # 为图形设置布局
nx.draw(G, pos, with_labels=True, node_color='gray', node_size=450, font_size=6, font_weight='normal')

# 绘制边的权重
edge_labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

# 显示图形
plt.show()