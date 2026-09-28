package org.example.item

import org.example.joueur
import org.example.monstre.IndividuMonstre
import kotlin.random.Random

/**
 * Représente un MonsterKube, un objet utilisable pour tenter de capturer un monstre sauvage.
 *
 * @property chanceCapture La chance de capture de base du kube, en pourcentage (0 à 100).
 */
class MonsterKube(
    id: Int,
    nom: String,
    description: String,
    var chanceCapture: Double,
) : Item(id, nom, description), Utilisable {

    /**
     * Tente de capturer l'individu monstre ciblé.
     *
     * Un monstre qui a déjà un entraîneur ne peut pas être capturé. Sinon, la chance effective vaut
     * chanceCapture * (1.5 - ratioVie), avec ratioVie = pv / pvMax, et au minimum 5.0 : plus le monstre
     * est blessé, plus la capture est facile. On tire ensuite un nombre aléatoire entre 0 et 100 :
     * si il est inférieur à la chance effective, la capture réussit.
     * Le joueur peut alors renommer le monstre, qui est ajouté à son équipe (6 monstres maximum)
     * ou sinon à sa boîte, et son entraîneur devient le joueur.
     *
     * @param cible L'individu monstre visé.
     * @return true si la capture a réussi, false sinon.
     */
    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("Vous lancez le Monster Kube !")
        if (cible.entraineur != null) {
            println("Le monstre ne peut pas être capturé.")
            return false
        }
        val ratioVie = cible.pv.toDouble() / cible.pvMax
        var chanceEffective = chanceCapture * (1.5 - ratioVie)
        chanceEffective = chanceEffective.coerceAtLeast(5.0)
        val nbAleatoire = Random.nextDouble(0.0, 100.0)
        if (nbAleatoire >= chanceEffective) {
            println("Presque ! Le Kube n'a pas pu capturer le monstre !")
            return false
        }
        println("Le monstre est capturé !")
        println("Nouveau nom pour ${cible.nom} (vide pour conserver) :")
        val nouveauNom = readlnOrNull() ?: ""
        if (nouveauNom.isNotEmpty()) {
            cible.nom = nouveauNom
        }
        if (joueur.equipeMonstre.size >= 6) {
            joueur.boiteMonstre.add(cible)
        } else {
            joueur.equipeMonstre.add(cible)
        }
        cible.entraineur = joueur
        return true
    }
}
