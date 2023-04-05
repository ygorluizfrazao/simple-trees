interface Printable<D> {
    fun print(data: D)
}

class ConsolePrintable<D>: Printable<D>{

    override fun print(data: D) {
        kotlin.io.print(data)
    }

}
