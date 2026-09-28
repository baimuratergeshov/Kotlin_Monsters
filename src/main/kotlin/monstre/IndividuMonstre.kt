package org.example.monstre

import org.example.dresseur.Entraineur
import kotlin.random.Random

/**
 * Représente un individu monstre, c'est-à-dire un monstre avec lequel le joueur va
 * interagir (les monstres sauvages, les monstres de l'équipe du joueur, les monstres
 * des autres dresseurs).
 *
 * Deux individus peuvent appartenir à la même espèce, par exemple deux individus
 * peuvent tous les deux être des Canaros.
 *
 * @property id L'identifiant unique de l'individu monstre.
 * @property nom Le nom de l'individu monstre.
 * @property espece L'espèce de l'individu monstre.
 * @property entraineur L'entraîneur possédant l'individu monstre, ou null si il n'en a pas.
 * @param expInit L'expérience initiale de l'individu monstre.
 */
class IndividuMonstre(
    var id: Int,
    var nom: String,
    var espece: EspeceMonstre,
    var entraineur: Entraineur?,
    expInit: Double,
) {
    var niveau: Int = 1

    var attaque: Int = espece.baseAttaque + Random.nextInt(-2, 3)

    var defense: Int = espece.baseDefense + Random.nextInt(-2, 3)

    var vitesse: Int = espece.baseVitesse + Random.nextInt(-2, 3)

    var attaqueSpe: Int = espece.baseAttaqueSpe + Random.nextInt(-2, 3)

    var defenseSpe: Int = espece.baseDefenseSpe + Random.nextInt(-2, 3)

    var pvMax: Int = espece.basePv + Random.nextInt(-5, 6)

    var potentiel: Double = Random.nextDouble(0.5, 2.0)

    /**
     * @property exp Expérience actuelle.
     * Tant que l'expérience atteint le palier du niveau courant, l'individu gagne un niveau.
     */
    var exp: Double = 0.0
        get() = field
        set(value) {
            field = value
            val estNiveau1 = niveau == 1
            while (field >= palierExp(niveau)) {
                levelUp()
                if (!estNiveau1) {
                    println("Le monstre $nom est maintenant niveau $niveau !")
                }
            }
        }

    /**
     *  @property pv  Points de vie actuels.
     * Ne peut pas être inférieur à 0 ni supérieur à [pvMax].
     */
    var pv: Int = pvMax
        get() = field
        set(nouveauPv) {
            field = nouveauPv.coerceIn(0, pvMax)
        }


    init {
        exp = expInit
    }

    /**
     * Calcule l'expérience totale nécessaire pour atteindre un niveau donné.
     *
     * @param niveau Niveau cible.
     * @return Expérience cumulée nécessaire pour atteindre ce niveau.
     */
    fun palierExp(niveau: Int): Double {
        return 100 * Math.pow((niveau - 1).toDouble(), 2.0)
    }

    /**
     * Augmente le niveau de l'individu monstre.
     *
     * Incrémente le niveau et recalcule les nouvelles valeurs des caractéristiques
     * (attaque, défense, vitesse, attaqueSpe, defenseSpe et pvMax). Chaque caractéristique
     * (sauf pvMax) gagne (modCaractéristique de l'espèce * potentiel), arrondi, plus un
     * nombre aléatoire entre -2 et 2. pvMax gagne (modPv de l'espèce * potentiel), arrondi,
     * plus un nombre aléatoire entre -5 et 5. Le pv actuel est augmenté du nombre de pvMax
     * gagnés lors de l'augmentation du niveau.
     */
    fun levelUp() {
        niveau += 1

        val gainAttaque = Math.round(espece.modAttaque * potentiel).toInt() + Random.nextInt(-2, 3)
        val gainDefense = Math.round(espece.modDefense * potentiel).toInt() + Random.nextInt(-2, 3)
        val gainVitesse = Math.round(espece.modVitesse * potentiel).toInt() + Random.nextInt(-2, 3)
        val gainAttaqueSpe = Math.round(espece.modAttaqueSpe * potentiel).toInt() + Random.nextInt(-2, 3)
        val gainDefenseSpe = Math.round(espece.modDefenseSpe * potentiel).toInt() + Random.nextInt(-2, 3)
        val gainPvMax = Math.round(espece.modPv * potentiel).toInt() + Random.nextInt(-5, 6)

        attaque += gainAttaque
        defense += gainDefense
        vitesse += gainVitesse
        attaqueSpe += gainAttaqueSpe
        defenseSpe += gainDefenseSpe
        pvMax += gainPvMax
        pv += gainPvMax
    }

    /**
     * Attaque un autre [IndividuMonstre] et lui inflige des dégâts.
     * Les dégâts valent attaque - (défense de la cible / 2), avec un minimum de 1.
     * Un message indique les dégâts réellement infligés (différence de PV de la cible).
     *
     * @param cible L'individu monstre attaqué.
     */
    fun attaquer(cible: IndividuMonstre) {
        val degatBrut = this.attaque
        var degatTotal = degatBrut - (cible.defense / 2)
        if (degatTotal < 1) {
            degatTotal = 1
        }
        val pvAvant = cible.pv
        cible.pv -= degatTotal
        val pvApres = cible.pv
        println("$nom inflige ${pvAvant - pvApres} dégâts à ${cible.nom}")
    }

    /**
     * Demande à l'utilisateur s'il veut renommer l'individu monstre.
     * Si la saisie est vide, le nom reste inchangé.
     */
    fun renommer() {
        println("Renommer $nom ?")
        val nouveauNom = readlnOrNull() ?: ""
        if (nouveauNom.isNotEmpty()) {
            this.nom = nouveauNom
        }
    }

    /**
     * Affiche l'art ASCII de face de l'individu monstre, avec ses caractéristiques
     * (nom, niveau, PV, stats...) affichées à droite, ligne par ligne.
     */
    fun afficheDetail() {
        val artLines = espece.afficheArt(true).lines()
        val details = listOf(
            "[$id] $nom (${espece.nom}, type ${espece.type})",
            "Niveau : $niveau",
            "Exp : ${exp.toInt()} / ${palierExp(niveau + 1).toInt()}",
            "PV : $pv/$pvMax",
            "Attaque : $attaque",
            "Défense : $defense",
            "Vitesse : $vitesse",
            "Attaque spé : $attaqueSpe",
            "Défense spé : $defenseSpe",
            "Potentiel : ${"%.2f".format(potentiel)}",
        )
        // Les codes couleur ANSI ne prennent pas de place à l'écran : on les ignore pour la largeur.
        val ansi = Regex("\u001B\\[[0-9;]*m")
        val maxArtWidth = artLines.maxOf { it.replace(ansi, "").length }
        val maxLines = maxOf(artLines.size, details.size)

        for (i in 0 until maxLines) {
            val artLine = if (i < artLines.size) artLines[i] else ""
            val detailLine = if (i < details.size) details[i] else ""
            val padding = " ".repeat(maxArtWidth + 4 - artLine.replace(ansi, "").length)
            println(artLine + padding + detailLine)
        }
    }
}
