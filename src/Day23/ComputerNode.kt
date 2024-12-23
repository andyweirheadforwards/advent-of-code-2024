package Day23

typealias NodeName = String

data class ComputerNode(val name: NodeName, val neighbours: List<NodeName>) {
    override fun toString(): String = this.name
}
