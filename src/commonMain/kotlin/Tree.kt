interface Tree<DataType> {

    val data: DataType
    val children: Collection<Tree<DataType>>

    fun addChild(node: Tree<DataType>): Tree<DataType>
    fun copy(): Tree<DataType>
    fun addAll(nodes: Collection<Tree<DataType>>): Tree<DataType>
    fun addAll(vararg nodes: Tree<DataType>): Tree<DataType>
    fun remove(predicate: (Tree<DataType>) -> Boolean): Tree<DataType>
    fun removeFirst(node: Tree<DataType>): Tree<DataType>
    fun removeAll(predicate: (Tree<DataType>) -> Boolean): Tree<DataType>
    fun search(predicate: (Tree<DataType>) -> Boolean): List<Tree<DataType>>
    fun first(predicate: (Tree<DataType>) -> Boolean): Tree<DataType>?
    fun depth(): Int
    fun size(): Int
    infix fun <R> map(mapper: (Tree<DataType>) -> R): R = mapper(this)

}

fun <D> Tree<D>.asString(level: Int = 0): String {
    val asString = StringBuilder("\t".repeat(level) + if (level > 0) "->" else "")
    asString.append(this.data.toString() + "\n")
    children.forEach {
        asString.append(it.asString(level + 1))
    }
    return asString.toString()
}