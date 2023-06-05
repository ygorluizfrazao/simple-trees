import com.google.common.truth.Truth.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

class GeneralTreeTest {

    private lateinit var tree: GeneralTree<String>

    @Before
    fun setUp() {
        tree = GeneralTree("Root")
    }

    @Test
    fun `tree is properly created`() {
        assertThat(tree.data).isEqualTo("Root")
    }

    @Test
    fun `tree, returns parent in search`() {
        val searchResult = tree.search {
            it.data == "Root"
        }
        assertThat(searchResult).contains(tree)
    }

    @Test
    fun `tree, returns all tree nodes when search is always true`() {
        val newNode1 = GeneralTree("node1Data")
        val newNode2 = GeneralTree("node2Data")
        val newNode3 = GeneralTree("node3Data")
        val newNode4 = GeneralTree("node4Data")
        tree.addChild(newNode1).addChild(newNode2)
        newNode1.addChild(newNode3).addChild(newNode4)
        val searchResult = tree.search { true }
        assertThat(searchResult).contains(tree)
        assertThat(searchResult).contains(newNode1)
        assertThat(searchResult).contains(newNode2)
        assertThat(searchResult).contains(newNode3)
        assertThat(searchResult).contains(newNode4)
    }

    @Test
    fun `tree, returns found items on search`() {
        val newNode1 = GeneralTree("node1Data")
        val newNode2 = GeneralTree("node2Data")
        val newNode3 = GeneralTree("node3Data")
        val newNode4 = GeneralTree("node4Data")
        tree.addChild(newNode1).addChild(newNode2)
        newNode1.addChild(newNode3).addChild(newNode4)
        val searchResult = tree.search { it.data.startsWith("node") }
        assertThat(searchResult).contains(newNode1)
        assertThat(searchResult).contains(newNode2)
        assertThat(searchResult).contains(newNode3)
        assertThat(searchResult).contains(newNode4)
        assertThat(searchResult).doesNotContain(tree)
    }

    @Test
    fun `tree, returns empty list when search fails`() {
        val searchTerm = UUID.randomUUID().toString()
        val searchResult = tree.search {
            it.data == searchTerm
        }
        assertThat(searchResult).isEmpty()
    }

    @Test
    fun `tree, after adding node, it is present in tree`() {
        val newNode1 = GeneralTree("node1Data")
        val newNode2 = GeneralTree("node2Data")
        tree.addChild(newNode1).addChild(newNode2)
        val searchResult = tree.search {
            it.data in listOf(newNode1.data, newNode2.data)
        }
        assertThat(searchResult).hasSize(2)
        assertThat(searchResult).contains(newNode1)
        assertThat(searchResult).contains(newNode2)
    }

    @Test
    fun `tree, string representation contains all nodes data`() {
        val newNode1 = GeneralTree("node1Data")
        val newNode2 = GeneralTree("node2Data")
        val newNode3 = GeneralTree("node3Data")
        val newNode4 = GeneralTree("node4Data")
        val treeString = tree.addAll(newNode1, newNode2, newNode3, newNode4).asString()
        assertThat(treeString).contains(tree.data)
        assertThat(treeString).contains(newNode1.data)
        assertThat(treeString).contains(newNode2.data)
        assertThat(treeString).contains(newNode3.data)
        assertThat(treeString).contains(newNode4.data)
    }

    @Test
    fun `tree, try to remove a node of an empty tree, the same tree returns`() {
        val newTree = tree.remove { true }
        assertThat(newTree).isEqualTo(tree)
    }

    @Test
    fun `tree, assure copy returns a new tree`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        newNode1.addAll(newNode2, newNode3)
        tree.addAll(newNode1, newNode4)

        val copiedTree = tree.copy()
        assertThat(copiedTree).isNotEqualTo(tree)

        GeneralTree("5").also { newNode4.addChild(it) }
        assertThat(copiedTree.asString()).isNotEqualTo(tree.asString())
    }

    @Test
    fun `tree, try to remove a leaf, content remains the same minus the leaf`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")

        newNode1.addAll(newNode3)
        tree.addAll(newNode1, newNode4)
        val expectedTree = tree.copy()
        newNode1.addAll(newNode2)

        println("Initial Tree:\n" + tree.asString())

        tree.remove { it.data == newNode2.data }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())
    }

    @Test
    fun `tree, try to remove leafs with same data, content remains the same minus the leafs`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newnode22 = GeneralTree("2")
        val newNode4 = GeneralTree("4")

        tree.addAll(newNode1, newNode4)
        val expectedTree = tree.copy()
        newNode1.addAll(newNode2, newnode22)

        println("Initial Tree:\n" + tree.asString())

        tree.remove { it.data == newNode2.data }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())
    }

    @Test
    fun `tree, try to remove a branch head, children content is reassigned to the parent of the removed node`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        newNode1.addAll(newNode2, newNode3)
        tree.addAll(newNode1, newNode4)

        val expectedTree = GeneralTree(tree.data)
        expectedTree.addAll(newNode2.copy(), newNode3.copy(), newNode4.copy())

        println("Initial Tree:\n" + tree.asString())

        tree.remove { it.data == newNode1.data }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())
    }

    @Test
    fun `tree, try to remove multiple branch heads, children content is reassigned to the parent of the removed node`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        newNode1.addAll(newNode2, newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)
        val expectedTree = GeneralTree(tree.data)
        expectedTree.addAll(newNode2.copy(), newNode5.copy(), newNode6.copy(), newNode4.copy())


        println("Initial Tree:\n" + tree.asString())

        tree.remove { it.data == newNode1.data || it.data == newNode3.data }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())
    }

    @Test
    fun `tree, try to remove first found leaf which corresponds to predicate condition, content remains the same minus the first found leaf`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newnode22 = GeneralTree("2")
        val newNode4 = GeneralTree("4")

        val expectedNode1 = newNode1.copy()

        newNode1.addAll(newNode2, newnode22)
        tree.addAll(newNode1, newNode4)

        val expectedTree = GeneralTree(tree.data)
        expectedNode1.addAll(newnode22.copy())
        expectedTree.addAll(expectedNode1, newNode4.copy())

        println("Initial Tree:\n" + tree.asString())

        tree.removeFirst { it.data == "2" }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())
    }

    @Test
    fun `tree, try to remove first found branch head, children content is reassigned to the parent of the removed node`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        newNode1.addAll(newNode2, newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)
        val expectedTree = GeneralTree(tree.data)
        expectedTree.addAll(newNode2.copy(), newNode3.copy(), newNode4.copy())


        println("Initial Tree:\n" + tree.asString())

        tree.removeFirst { it.data == newNode1.data || it.data == newNode3.data }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())

        println(tree.removeFirst { it.data == newNode3.data }.asString())
    }

    @Test
    fun `tree, 3-depth tree, depth() should return 3`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        newNode1.addAll(newNode2, newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)

        println("Initial Tree:\n" + tree.asString())
        assertThat(tree.depth()).isEqualTo(3)
        tree.removeFirst { true }
        println("Tree:\n"+tree.asString())
        assertThat(tree.depth()).isEqualTo(2)
    }

    @Test
    fun `tree, 0-depth tree, depth() should return0`() {
        println("Initial Tree:\n" + tree.asString())
        assertThat(tree.depth()).isEqualTo(0)
    }

    @Test
    fun `tree, 7 nodes size() should return 7`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        newNode1.addAll(newNode2, newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)

        println("Initial Tree:\n" + tree.asString())
        assertThat(tree.size()).isEqualTo(7)
    }

    @Test
    fun `tree, root tree size should return 1`() {
        println("Initial Tree:\n" + tree.asString())
        assertThat(tree.size()).isEqualTo(1)
    }

    @Test
    fun `tree, prune root leave only root in tree`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        newNode1.addAll(newNode2, newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)
        val expectedTree = GeneralTree(tree.data)

        println("Initial Tree:\n" + tree.asString())

        tree.prune {
            it.data == "Root"
        }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())

        println(tree.removeFirst { it.data == newNode3.data }.asString())
    }

    @Test
    fun `tree, prune intermediate branch, remove all branch including its head`() {
        val newNode1 = GeneralTree("1")
        val newNode2 = GeneralTree("2")
        val newNode3 = GeneralTree("3")
        val newNode4 = GeneralTree("4")
        val newNode5 = GeneralTree("5")
        val newNode6 = GeneralTree("6")

        val expectedTree = GeneralTree(tree.data)
        newNode1.addAll(newNode2)
        expectedTree.addAll(newNode1.copy())
        newNode1.addAll(newNode3)
        newNode3.addAll(newNode5, newNode6)
        tree.addAll(newNode1, newNode4)
        expectedTree.addAll(newNode4)

        println("Initial Tree:\n" + tree.asString())

        tree.prune {
            it.data == "3"
        }

        println("End Tree:\n" + tree.asString())
        println("Expected Tree:\n" + expectedTree.asString())

        assertThat(tree.asString()).isEqualTo(expectedTree.asString())

        println(tree.removeFirst { it.data == newNode3.data }.asString())
    }

}