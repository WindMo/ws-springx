package ws.spring.testdemo.util.structure;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ws.spring.util.structure.ForwardNode;
import ws.spring.util.structure.Nodes;
import ws.spring.util.structure.TreeNode;
import ws.spring.util.structure.WritableTreeNode;

import java.util.*;

/**
 * @author WindShadow
 * @version 2023-06-27.
 */

@Slf4j
public class NodesTests {

    private static final Random RANDOM = new Random();

    private static void appendChildren(CustomEntity<String> node, List<CustomEntity<String>> list, int size, int deep) {

        if (deep <= 0) return;
        for (int i = 0; i < size; i++) {

            CustomEntity<String> n = new CustomEntity<>(node.getV() + "-" + i, node.getV());
            list.add(n);
            appendChildren(n, list, RANDOM.nextInt(size) + 1, deep - 1);
        }
    }

    private List<CustomEntity<String>> genEntities(String rootName, int size, int deep) {

        List<CustomEntity<String>> list = new ArrayList<>();
        CustomEntity<String> root = new CustomEntity<>(rootName, null);
        list.add(root);
        appendChildren(root, list, size, deep);
        return list;
    }

    @Test
    void transTest() {

        List<CustomEntity<String>> list1 = genEntities("root1", 2, 3);
        WritableTreeNode<CustomEntity<String>> node1 = Nodes.transToSingleTree(list1);
        WritableTreeNode<CustomEntity<String>> node2 = Nodes.adapterTransToSingleTree(list1, CustomEntity::getV, CustomEntity::getPv);
        Assertions.assertEquals(node1, node2);

        List<CustomEntity<String>> list2 = genEntities("root2", 2, 3);
        list2.addAll(list1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> Nodes.transToSingleTree(list2));

        List<WritableTreeNode<CustomEntity<String>>> treeNodes = Nodes.transformToTrees(list2);
        Assertions.assertSame(2, treeNodes.size());
    }

    @Test
    void bfsAndDfsTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3);
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);

        CustomEntity<String> entity = list.get(RANDOM.nextInt(list.size()));
        String v = entity.getV();
        CustomEntity<String> bfsRes = Nodes.bfs(tree, e -> v.equals(e.getV()));
        Assertions.assertSame(entity, bfsRes);
        CustomEntity<String> dfsRes = Nodes.dfs(tree, e -> v.equals(e.getV()));
        Assertions.assertSame(entity, dfsRes);
    }

    @Test
    void collectToCollectionTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3);
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        ArrayList<CustomEntity<String>> collectList = Nodes.collectToCollection(tree, ArrayList::new);
        list.sort(Comparator.comparingInt(Object::hashCode));
        collectList.sort(Comparator.comparingInt(Object::hashCode));
        Assertions.assertEquals(list, collectList);
    }

    @Test
    void collectToListTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3);
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        List<CustomEntity<String>> entities = Nodes.collectToList(tree);
        list.sort(Comparator.comparingInt(Object::hashCode));
        entities.sort(Comparator.comparingInt(Object::hashCode));
        Assertions.assertEquals(list, entities);
    }

    @Test
    void collectToSetTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3);
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        Set<CustomEntity<String>> entities = Nodes.collectToSet(tree);
        Assertions.assertEquals(new HashSet<>(list), entities);
    }

    @Test
    void collectToMapTest() {

        List<CustomEntity<String>> list = genEntities("root", 2, 3);
        TreeNode<CustomEntity<String>> tree = Nodes.transToSingleTree(list);
        Map<String, CustomEntity<String>> entityMap = Nodes.collectToMap(tree, CustomEntity::fetchKey);
        list.forEach(e -> Assertions.assertSame(e, entityMap.get(e.fetchKey())));
    }

    @ToString
    private static class CustomEntity<K> implements ForwardNode<K> {

        private final K v;
        private final K pv;

        private String name;

        private CustomEntity(K v, K pv) {
            this.v = v;
            this.pv = pv;
            this.name = "name" + v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public K getV() {
            return v;
        }

        public K getPv() {
            return pv;
        }

        @Override
        public K fetchKey() {
            return getV();
        }

        @Override
        public K fetchParentKey() {
            return getPv();
        }
    }
}
