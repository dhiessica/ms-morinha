package br.pucpr.authserver.pets


enum class Genre {
    F, M;
    companion object {
        fun findOrNull(genre: String?) =
            entries.find { it.name == genre?.uppercase() }
    }
}