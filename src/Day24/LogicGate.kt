package Day24

data class LogicGate(val input: Pair<String,String>, val out: String, val type: GateType) {
    override fun toString(): String = "${input.first} $type ${input.second} -> $out"
}
