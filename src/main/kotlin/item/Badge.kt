package org.example.item

import org.example.dresseur.Entraineur

/**
 * Représente un badge, un type particulier d'[Item] remporté en battant un champion.
 *
 * @property champion L'entraîneur champion qui remet ce badge.
 */
class Badge(
    id: Int,
    nom: String,
    description: String,
    var champion: Entraineur,
) : Item(id, nom, description)
