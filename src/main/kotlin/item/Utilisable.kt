package org.example.item

import org.example.monstre.IndividuMonstre

/**
 * Contrat pour les objets utilisables en combat (potions, kubes...).
 * Toute classe qui implémente cette interface doit fournir la méthode [utiliser].
 */
interface Utilisable {
    /**
     * Utilise l'objet sur un individu monstre et applique son effet.
     *
     * @param cible L'individu monstre sur lequel l'objet est utilisé.
     * @return true si l'action a réussi, false sinon.
     */
    fun utiliser(cible: IndividuMonstre): Boolean
}
