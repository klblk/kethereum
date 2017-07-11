package org.kethereum.bip44

/*
BIP44 as in https://github.com/bitcoin/bips/blob/master/bip-0044.mediawiki
 */

data class BIP44Element(val hardened: Boolean, val number: Int)
class BIP44(val path: List<BIP44Element>) {
    companion object {

        private val HARDENING_FLAG = 0x80000000.toInt()
        fun fromPath(path: String): BIP44 {
            if (!path.startsWith("m/")) {
                throw (IllegalArgumentException("Must start with m/"))
            }
            val cleanPath = path.replace("m/", "").replace(" ", "")
            return BIP44(cleanPath.split("/").map {
                BIP44Element(
                        hardened = it.contains("'"),
                        number = it.replace("'", "").toIntOrNull() ?:
                                throw IllegalArgumentException("not a number " + it)
                )
            })
        }

    }

    fun toIntList() = path.map { if (it.hardened) it.number or HARDENING_FLAG else it.number }
    override fun toString() = "m/" + path.map {
        if (it.hardened) "${it.number}'" else "${it.number}"
    }.joinToString("/")
}