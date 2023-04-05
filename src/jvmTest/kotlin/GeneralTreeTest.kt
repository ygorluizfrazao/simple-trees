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
    fun `tree, returns all tree when search is always true`() {
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
    fun `print tree`(){
        println("Start")
        val newNode1 = GeneralTree("node1Data")
        val newNode2 = GeneralTree("node2Data")
        val newNode3 = GeneralTree("node3Data")
        val newNode4 = GeneralTree("node4Data")
        tree.addChild(newNode1).addChild(newNode2)
        newNode1.addChild(newNode3).addChild(newNode4)
        println(tree)
    }
}