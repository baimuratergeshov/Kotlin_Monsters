package org.example.monde

import org.example.combat.CombatMonstre
import org.example.dresseur.Entraineur
import org.example.monstre.EspeceMonstre
import org.example.monstre.IndividuMonstre
import kotlin.random.Random

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
    /**
     * Génère un monstre sauvage appartenant à l'une des espèces de la zone, choisie au hasard.
     * Son expérience initiale vaut l'expérience de la zone, modifiée aléatoirement de ±20%.
     *
     * @return Le nouvel individu monstre sauvage généré, sans entraîneur.
     */
    fun genereMonstre(): IndividuMonstre {
        val espece = especesMonstres.random()
        val exp = expZone * Random.nextDouble(0.8, 1.2)
        return IndividuMonstre(0, espece.nom, espece, null, exp)
    }

    /**
     * Démarre un combat entre un monstre sauvage généré (grâce à [genereMonstre]) et le premier
     * monstre de l'équipe du joueur qui a des PV > 0.
     *
     * @param joueur L'entraîneur qui affronte le monstre sauvage.
     */
    fun rencontreMonstre(joueur: Entraineur) {
        val monstreSauvage = genereMonstre()
        val premierPokemon = joueur.equipeMonstre.firstOrNull { it.pv > 0 }
        if (premierPokemon == null) {
            println("Vous n'avez aucun monstre en état de combattre !")
            return
        }
        val combat = CombatMonstre(premierPokemon, monstreSauvage)
        combat.lancerCombat()
    }
}
