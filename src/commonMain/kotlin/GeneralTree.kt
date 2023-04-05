class GeneralTree<DataType>(override val data: DataType) : Tree<DataType> {

    private val _children = mutableListOf<Tree<DataType>>()
    override val children: Collection<Tree<DataType>>
        get() = _children.toList()

    override fun copy(): Tree<DataType> {
        var newChildren = emptyList<Tree<DataType>>()
        _children.forEach {
            newChildren = newChildren + it.copy()
        }
        return GeneralTree(this.data).apply { addAll(newChildren) }
    }

    override fun prune(predicate: (Tree<DataType>) -> Boolean): Tree<DataType> {
        TODO("Not yet implemented")
    }

    /**
     * This method will not remove the own node, the search happens from its children downwards, left to right.
     * The default behavior when removing a node is to assign the remaining branch to the removed node parent.
     * The removing of nodes stops when the first node is removed, or no nodes are removed
     *
     * @param predicate lambda that evaluates if the node should be removed.
     * @return The same node with items removed from its branches.
     */
    override fun removeFirst(predicate: (Tree<DataType>) -> Boolean): Tree<DataType> {
        var newChildren = emptyList<Tree<DataType>>()

        run exitForEach@{
            _children.forEachIndexed { index, node ->
                val (removalDone, remainingNodes) = removeFirstInBranch(node, predicate)
                newChildren = newChildren + remainingNodes

                if (removalDone) {
                    if (index < _children.size - 1)
                        newChildren = newChildren + _children.subList(index + 1, _children.size).toList()
                    return@exitForEach
                }
            }
        }

        _children.clear()
        _children.addAll(newChildren)
        return this
    }

    private fun removeFirstInBranch(
        branchHead: Tree<DataType>,
        predicate: (Tree<DataType>) -> Boolean
    ): Pair<Boolean, List<Tree<DataType>>> {
        var newChildren = emptyList<Tree<DataType>>()
        var removed = false

        if (predicate(branchHead)) {
            newChildren = branchHead.children.toList()
            removed = true
            return Pair(removed, newChildren)
        }

        branchHead.children.forEachIndexed { index, node ->
            val (removalDone, returnedBranch) = removeFirstInBranch(node, predicate)
            newChildren = newChildren + returnedBranch
            if (removalDone) {
                if (index < branchHead.children.size - 1)
                    newChildren =
                        newChildren + branchHead.children.toMutableList().subList(index + 1, _children.size).toList()
                removed = true
                val newNode = GeneralTree(branchHead.data)
                newNode.addAll(newChildren)
                return Pair(removed, listOf(newNode))
            }
        }

        return Pair(removed, listOf(branchHead))
    }

    /**
     * This method will not remove the own node, the search happens from its children downwards.
     * The default behavior when removing a node is to assign the remaining branch to the removed node parent.
     *
     * @param predicate lambda that evaluates if the node should be removed.
     * @return The same node with items removed from its branches.
     */
    override fun remove(predicate: (Tree<DataType>) -> Boolean): Tree<DataType> {
        var newChildren = emptyList<Tree<DataType>>()

        _children.forEach {
            newChildren = newChildren + removeInBranch(it, predicate)
        }
        _children.clear()
        _children.addAll(newChildren)
        return this
    }

    private fun removeInBranch(
        branchHead: Tree<DataType>,
        predicate: (Tree<DataType>) -> Boolean
    ): List<Tree<DataType>> {
        var newBranch = emptyList<Tree<DataType>>()

        branchHead.children.forEach {
            val returnedBranch = removeInBranch(it, predicate)
            newBranch = newBranch + returnedBranch
        }

        if (predicate(branchHead)) {
            return newBranch
        }

        val newNode = GeneralTree(branchHead.data)
        newNode.addAll(newBranch)
        return listOf(newNode)
    }

    override fun addAll(vararg nodes: Tree<DataType>): Tree<DataType> {
        return addAll(nodes.toList())
    }

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
        return this.asString()
    }

}