package org.example.monde

import org.example.monstre.EspeceMonstre
import java.time.LocalDateTime

/**
 * Représente une zone dans le contexte du jeu.
 *
 * Une zone peut représenter une route, une caverne, une mer ... C'est un endroit où
 * on peut chercher un monstre sauvage et où on peut se déplacer à la zone suivante ou
 * à la zone précédente si il y en a une. Les zones forment ainsi une chaîne de routes.
 *
 * @property id L'identifiant unique de la zone.
 * @property nom Le nom de la zone.
 * @property expZone La quantité d'expérience gagnée dans la zone.
 * @property especesMonstres La liste mutable des espèces de monstre présentes dans la zone.
 * @property zoneSuivante La zone suivante dans la chaîne de routes, ou null si il n'y en a pas.
 * @property zonePrecedante La zone précédente dans la chaîne de routes, ou null si il n'y en a pas.
 */
class Zone(
    var id: Int,
    var nom: String,
    var expZone: Int,
    var especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
    var zoneSuivante: Zone? = null,
    var zonePrecedante: Zone? = null,
) {
    //TODO faire la méthode genereMonstre()
    //TODO faire la méthode rencontreMonstre()
}
