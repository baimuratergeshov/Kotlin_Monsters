package org.example.partie

import org.example.dresseur.Entraineur
import org.example.especeAquamy
import org.example.especeFlamkip
import org.example.especeSpringleaf
import org.example.monde.Zone
import org.example.monstre.IndividuMonstre

/**
 * Représente une partie du jeu.
 *
 * Une partie associe un entraîneur (le joueur) à la zone dans laquelle il se trouve
 * actuellement. Plusieurs parties peuvent coexister.
 *
 * @property id L'identifiant unique de la partie.
 * @property joueur L'entraîneur qui joue cette partie.
 * @property zone La zone courante dans laquelle se trouve le joueur.
 */
class Partie(
    var id: Int,
    var joueur: Entraineur,
    var zone: Zone,
) {
    /**
     * Propose au joueur trois monstres de départ (Springleaf, Flamkip, Aquamy) parmi lesquels
     * choisir. Le monstre choisi peut être renommé, est ajouté à l'équipe du joueur, et son
     * entraîneur devient le joueur.
     */
    fun choixStarter() {
        val monstre1 = IndividuMonstre(1, especeSpringleaf.nom, especeSpringleaf, null, 0.0)
        val monstre2 = IndividuMonstre(2, especeFlamkip.nom, especeFlamkip, null, 0.0)
        val monstre3 = IndividuMonstre(3, especeAquamy.nom, especeAquamy, null, 0.0)

        var starter: IndividuMonstre? = null
        while (starter == null) {
            monstre1.afficheDetail()
            monstre2.afficheDetail()
            monstre3.afficheDetail()
            println("Choisissez votre monstre de départ (1..3) :")
            val choixSelection = readlnOrNull()?.trim()?.toIntOrNull()
            if (choixSelection == null || choixSelection !in 1..3) {
                continue
            }
            starter = when (choixSelection) {
                1 -> monstre1
                2 -> monstre2
                else -> monstre3
            }
        }

        starter.renommer()
        joueur.equipeMonstre.add(starter)
        starter.entraineur = joueur
    }

    /**
     * Demande au joueur la position d'un monstre de son équipe et la position où le déplacer,
     * puis échange les deux monstres à ces positions.
     */
    fun modifierOrdreEquipe() {
        if (joueur.equipeMonstre.size < 2) {
            println("Vous devez avoir au moins deux monstres pour changer l'ordre de votre équipe.")
            return
        }
        println("Position du monstre à déplacer :")
        val position1 = readlnOrNull()?.trim()?.toIntOrNull()
        println("Nouvelle position :")
        val position2 = readlnOrNull()?.trim()?.toIntOrNull()
        if (position1 == null || position2 == null ||
            position1 !in 1..joueur.equipeMonstre.size || position2 !in 1..joueur.equipeMonstre.size
        ) {
            println("Position invalide.")
            return
        }
        val temp = joueur.equipeMonstre[position1 - 1]
        joueur.equipeMonstre[position1 - 1] = joueur.equipeMonstre[position2 - 1]
        joueur.equipeMonstre[position2 - 1] = temp
        println("Ordre de l'équipe mis à jour.")
    }

    /**
     * Affiche l'équipe du joueur et permet de consulter les détails d'un monstre (par son
     * numéro), de modifier l'ordre de l'équipe ('m'), ou de quitter ('q').
     */
    fun examineEquipe() {
        while (true) {
            println("=== Équipe de ${joueur.nom} ===")
            joueur.equipeMonstre.forEachIndexed { index, monstre ->
                println("${index + 1}. ${monstre.nom} (Niveau ${monstre.niveau})")
            }
            println("Entrez un numéro pour voir les détails, 'm' pour modifier l'ordre, 'q' pour quitter :")
            val saisie = readlnOrNull()?.trim()
            when (saisie) {
                "q" -> return
                "m" -> modifierOrdreEquipe()
                else -> {
                    val monstre = saisie?.toIntOrNull()?.let { joueur.equipeMonstre.getOrNull(it - 1) }
                    if (monstre != null) {
                        monstre.afficheDetail()
                    } else {
                        println("Choix invalide.")
                    }
                }
            }
        }
    }

    /**
     * Boucle principale de jeu : affiche la zone courante et propose de chercher un monstre
     * sauvage, d'examiner l'équipe, ou de se déplacer vers la zone suivante ou précédente.
     */
    fun jouer() {
        var enJeu = true
        while (enJeu) {
            println("Vous êtes dans la zone : ${zone.nom}")
            println("1. Chercher un monstre sauvage")
            println("2. Examiner l'équipe")
            println("3. Zone suivante")
            println("4. Zone précédente")
            println("5. Quitter")
            when (readlnOrNull()?.trim()) {
                "1" -> zone.rencontreMonstre(joueur)
                "2" -> examineEquipe()
                "3" -> {
                    val suivante = zone.zoneSuivante
                    if (suivante != null) {
                        zone = suivante
                        println("Vous entrez dans ${zone.nom}.")
                    } else {
                        println("Il n'y a pas de zone suivante.")
                    }
                }
                "4" -> {
                    val precedente = zone.zonePrecedante
                    if (precedente != null) {
                        zone = precedente
                        println("Vous entrez dans ${zone.nom}.")
                    } else {
                        println("Il n'y a pas de zone précédente.")
                    }
                }
                "5" -> enJeu = false
                else -> println("Choix invalide.")
            }
        }
    }
}

/**
 * Crée une nouvelle partie : accueille le joueur, lui demande son nom (conservé si la saisie
 * est vide), puis crée et retourne la partie associant ce joueur à sa zone de départ.
 *
 * @param joueur L'entraîneur qui va jouer cette partie.
 * @param zoneDepart La zone dans laquelle la partie commence.
 * @return La nouvelle partie créée.
 */
fun nouvellePartie(joueur: Entraineur, zoneDepart: Zone): Partie {
    println("Bienvenue dans Kotlin Monsters !")
    println("Quel est votre nom ?")
    val nom = readlnOrNull() ?: ""
    if (nom.isNotEmpty()) {
        joueur.nom = nom
    }
    return Partie(joueur.id, joueur, zoneDepart)
}
