package org.example.monstre

import java.io.File

/**
 * Représente une espèce de monstre dans le contexte du jeu.
 *
 * Une espèce décrit le modèle partagé par tous les individus d'un même type de monstre
 * (par exemple Flamkip) : son type, ses statistiques de base, les multiplicateurs
 * appliqués à ces statistiques, ainsi que des informations descriptives comme sa
 * description, ses particularités et son caractère.
 *
 * @property id L'identifiant unique de l'espèce de monstre.
 * @property nom Le nom de l'espèce de monstre.
 * @property type L'élément (type) de l'espèce de monstre (ex : Feu, Eau, Plante...).
 * @property baseAttaque La statistique de base d'Attaque de l'espèce.
 * @property baseDefense La statistique de base de Défense de l'espèce.
 * @property baseVitesse La statistique de base de Vitesse de l'espèce.
 * @property baseAttaqueSpe La statistique de base d'Attaque Spéciale de l'espèce.
 * @property baseDefenseSpe La statistique de base de Défense Spéciale de l'espèce.
 * @property basePv La statistique de base de Points de Vie (PV) de l'espèce.
 * @property modAttaque Le multiplicateur appliqué à la statistique d'Attaque.
 * @property modDefense Le multiplicateur appliqué à la statistique de Défense.
 * @property modVitesse Le multiplicateur appliqué à la statistique de Vitesse.
 * @property modAttaqueSpe Le multiplicateur appliqué à la statistique d'Attaque Spéciale.
 * @property modDefenseSpe Le multiplicateur appliqué à la statistique de Défense Spéciale.
 * @property modPv Le multiplicateur appliqué à la statistique de Points de Vie.
 * @property description La description de l'espèce de monstre.
 * @property particularites Les particularités propres à l'espèce de monstre.
 * @property caractères Le ou les traits de caractère typiques de l'espèce de monstre.
 */
class EspeceMonstre (var id : Int,
                     var nom: String,
                     var type: String,
                     val baseAttaque: Int,
                     val baseDefense: Int,
                     val baseVitesse: Int,
                     val baseAttaqueSpe: Int,
                     val baseDefenseSpe: Int,
                     val basePv: Int,
                     val modAttaque: Double,
                     val modDefense: Double,
                     val modVitesse: Double,
                     val modAttaqueSpe: Double,
                     val modDefenseSpe: Double,
                     val modPv: Double,
                     val description: String = "",
                     val particularites: String = "",
                     val caractères: String = "",
) {
    /**
     * Affiche la représentation artistique ASCII du monstre.
     *
     * @param deFace Détermine si l'art affiché est de face (true) ou de dos (false).
     *               La valeur par défaut est true.
     * @return Une chaîne de caractères contenant l'art ASCII du monstre avec les codes couleur ANSI.
     *         L'art est lu à partir d'un fichier texte dans le dossier resources/art.
     */
    fun afficheArt(deFace: Boolean=true): String{
        val nomFichier = if(deFace) "front" else "back";
        val art=  File("src/main/resources/art/${this.nom.lowercase()}/$nomFichier.txt").readText()
        val safeArt = art.replace("/", "∕")
        return safeArt.replace("\\u001B", "\u001B")
    }

}