package org.example.combat

import org.example.item.MonsterKube
import org.example.item.Utilisable
import org.example.monstre.IndividuMonstre
import kotlin.random.Random

/**
 * Représente un combat entre le monstre actif du joueur et un monstre sauvage.
 *
 * @property monstreJoueur Le monstre du joueur actuellement en combat.
 * @property monstreSauvage Le monstre sauvage affronté.
 */
class CombatMonstre(
    var monstreJoueur: IndividuMonstre,
    var monstreSauvage: IndividuMonstre,
) {
    /**
     * @property round Le round courant du combat, commence à 1.
     */
    var round: Int = 1

    /**
     * Indique si le combat est perdu pour le joueur.
     *
     * @return true si aucun monstre de l'équipe du joueur n'a plus de PV, false sinon.
     */
    fun gameOver(): Boolean {
        val equipe = monstreJoueur.entraineur?.equipeMonstre ?: return true
        return equipe.all { it.pv <= 0 }
    }

    /**
     * Indique si le joueur a gagné le combat.
     *
     * Si les PV du monstre sauvage sont à 0, le joueur gagne : son monstre reçoit 20% de
     * l'expérience du monstre sauvage. Sinon, la victoire est acquise si le monstre sauvage
     * a été capturé, c'est-à-dire si son entraîneur est devenu celui du joueur.
     *
     * @return true si le combat est gagné, false sinon.
     */
    fun joueurGagne(): Boolean {
        if (monstreSauvage.pv <= 0) {
            println("${monstreJoueur.entraineur?.nom} a gagné !")
            val gainExp = monstreSauvage.exp * 0.20
            monstreJoueur.exp += gainExp
            println("${monstreJoueur.nom} gagne $gainExp exp")
            return true
        }
        if (monstreSauvage.entraineur == monstreJoueur.entraineur) {
            println("${monstreSauvage.nom} a été capturé !")
            return true
        }
        return false
    }

    /**
     * Fait attaquer le monstre sauvage s'il a encore des PV.
     */
    fun actionAdversaire() {
        if (monstreSauvage.pv > 0) {
            monstreSauvage.attaquer(monstreJoueur)
        }
    }

    /**
     * Demande au joueur l'action à effectuer et l'exécute.
     *
     * 1. Attaquer 2. Utiliser un objet 3. Changer de monstre 4. Fuir 5. Ne rien faire.
     * Si le combat est déjà terminé (défaite), retourne false immédiatement. Sinon, une fois
     * l'action jouée, affiche un message si le monstre sauvage est K.O., puis retourne true
     * (sauf en cas de capture réussie avec un objet ou de fuite réussie, qui retournent false).
     *
     * @return false si le combat doit s'arrêter (capture ou fuite réussie), true sinon.
     */
    fun actionJoueur(): Boolean {
        if (gameOver()) {
            return false
        }

        println("Afficher menu d'actions (1,2,3...)")
        println("1. Attaquer")
        println("2. Utiliser un objet")
        println("3. Changer de monstre")
        println("4. Fuir")
        println("5. Ne rien faire")
        val choixAction = readlnOrNull()?.trim()?.toIntOrNull()

        when (choixAction) {
            1 -> {
                monstreJoueur.attaquer(monstreSauvage)
            }
            2 -> {
                val entraineur = monstreJoueur.entraineur
                if (entraineur != null) {
                    println("Afficher sacAItems")
                    entraineur.sacAItems.forEachIndexed { index, objet -> println("${index + 1}. ${objet.nom}") }
                    val indexChoix = readlnOrNull()?.trim()?.toIntOrNull()
                    val objetChoisi = indexChoix?.let { entraineur.sacAItems.getOrNull(it - 1) }
                    if (objetChoisi is Utilisable) {
                        val cible = if (objetChoisi is MonsterKube) monstreSauvage else monstreJoueur
                        val captureReussie = objetChoisi.utiliser(cible)
                        if (captureReussie) {
                            return false
                        } else {
                            println("Continuer le combat")
                        }
                    } else {
                        println("Objet non utilisable")
                    }
                }
            }
            3 -> {
                val entraineur = monstreJoueur.entraineur
                if (entraineur != null) {
                    println("Afficher équipe de monstres (pv > 0)")
                    val equipeVivante = entraineur.equipeMonstre.filter { it.pv > 0 }
                    equipeVivante.forEachIndexed { index, monstre -> println("${index + 1}. ${monstre.nom}") }
                    val indexChoix = readlnOrNull()?.trim()?.toIntOrNull()
                    val choixMonstre = indexChoix?.let { equipeVivante.getOrNull(it - 1) }
                    if (choixMonstre != null) {
                        println("${choixMonstre.nom} remplace ${monstreJoueur.nom}")
                        monstreJoueur = choixMonstre
                    }
                }
            }
            4 -> {
                if (fuir()) {
                    return false
                }
            }
            5 -> {
                println("${monstreJoueur.nom} ne fait rien.")
            }
            else -> {
                println("Choix invalide")
            }
        }

        if (monstreSauvage.pv <= 0) {
            println("${monstreSauvage.nom} K.O !")
        }
        return true
    }

    /**
     * Tente de fuir le combat. La chance de fuite dépend de la vitesse comparée des deux monstres :
     * 90% si le monstre du joueur est au moins aussi rapide que le monstre sauvage, 50% sinon.
     *
     * @return true si la fuite a réussi, false sinon.
     */
    private fun fuir(): Boolean {
        val chanceFuite = if (monstreJoueur.vitesse >= monstreSauvage.vitesse) 90.0 else 50.0
        val reussite = Random.nextDouble(0.0, 100.0) < chanceFuite
        println(if (reussite) "Vous prenez la fuite !" else "La fuite a échoué !")
        return reussite
    }

    /**
     * Affiche l'état du combat : le round courant, le niveau et les PV du monstre sauvage,
     * son art ASCII de face, puis l'art ASCII de dos du monstre du joueur ainsi que son niveau
     * et ses PV.
     */
    fun afficheCombat() {
        println("======== Début Round : $round ========")
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")
        println(monstreSauvage.espece.afficheArt(true))
        println(monstreJoueur.espece.afficheArt(false))
        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")
    }

    /**
     * Joue un round de combat : le monstre le plus rapide agit en premier.
     *
     * Si le monstre du joueur est le plus rapide, il joue d'abord ; si son action arrête
     * le combat, le round s'arrête sans faire jouer le monstre sauvage. Sinon, le monstre
     * sauvage joue d'abord ; s'il met alors fin au combat (défaite du joueur), le round
     * s'arrête sans faire jouer le joueur.
     *
     * @return false si le combat doit s'arrêter, true sinon.
     */
    fun jouer(): Boolean {
        val joueurPlusRapide = monstreJoueur.vitesse >= monstreSauvage.vitesse
        afficheCombat()
        if (joueurPlusRapide) {
            val continuer = actionJoueur()
            if (!continuer) {
                return false
            }
            actionAdversaire()
        } else {
            actionAdversaire()
            if (!gameOver()) {
                val continuer = actionJoueur()
                if (!continuer) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }

    /**
     * Lance et gère le combat complet, round par round, jusqu'à la victoire, la défaite ou la fuite.
     * Si le joueur perd (tous ses monstres K.O.), son équipe retrouve tous ses PV à la fin du combat.
     */
    fun lanceCombat() {
        while (!gameOver() && !joueurGagne()) {
            if (!jouer()) {
                return
            }
            println("======== Fin du Round : $round ========")
            round += 1
        }
        if (gameOver()) {
            monstreJoueur.entraineur?.equipeMonstre?.forEach { it.pv = it.pvMax }
            println("Game Over!")
        }
    }
}
