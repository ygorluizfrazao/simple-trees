class GeneralTree<DataType>(override val data: DataType) : Tree<DataType> {

    private val _children = mutableListOf<Tree<DataType>>()
    override val children: Collection<Tree<DataType>>
        get() = _children.toList()

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun addAll(nodes: Collection<Tree<DataType>>): Tree<DataType> {
        _children.addAll(nodes)
        return this
    }

    override fun addChild(node: Tree<DataType>): Tree<DataType> {
        _children.add(node)
        return this
    }

    override fun removeFirst(node: Tree<DataType>): Tree<DataType> {
        TODO("Not yet implemented")
    }

    override fun removeAll(predicate: (Tree<DataType>) -> Boolean): Tree<DataType> {
        TODO("Not yet implemented")
    }

    override fun search(predicate: (Tree<DataType>) -> Boolean): List<Tree<DataType>> {
        var response = listOf<Tree<DataType>>()

        if (predicate(this))
            response = response + this

        if (_children.isEmpty())
            return response

        _children.forEach {
            response = response + it.search(predicate)
        }
        return response
    }

    override fun first(predicate: (Tree<DataType>) -> Boolean): Tree<DataType>? {

        if (predicate(this))
            return this

        if (_children.isEmpty())
            return null

        _children.forEach {
            it.first(predicate)?.let { branchResult ->
                return branchResult
            }
        }

        return null
    }

    override fun toString(): String {
        return buildTreeString()
    }

}