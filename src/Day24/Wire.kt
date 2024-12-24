package Day24

data class Wire(val name: String, var value: Int = 0b0) {
    override fun toString(): String = "${name}: ${value}"
}

infix fun Wire.and(other: Wire) = this.value and other.value
infix fun Wire.or(other: Wire) = this.value or other.value
infix fun Wire.xor(other: Wire) = this.value xor other.value
