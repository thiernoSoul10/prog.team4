# Lacuna

Jeu Lacuna avec implémentation IA

## Compilation

placer vous dans le repertoire "main_files"

Donnez le droit d'execution au fichier compile.sh

Lancer le fichier compile.sh (il fait la compilation )

pour lancer il suffit de taper la commande :

```
java -cp bin main.Lacuna
```

ou lancer le fichier run.sh en lui donnant les droit d'execution

# A faire 
Correction des bugs suivants : 
- Avantage initial n'est pas dans undoStack : DONE :)

# Faycal

- Placement des pions (séléction des fleurs, ligne blanche et drag and drop du pion)
- Création des boxs pour les scores des joueurs avec les fleurs mangées
- S'organiser avec Layan pour le controleur souris

# Tout le monde

- Correction des bugs envoyés sur whatsapp (important)
- Réduction de la taille des fleurs
- Uniformisation de le répartition aléatoire des fleurs au début de partie

# Niels

- Partie IA

# Layan

- S'organiser avec Faycal pour le controleur souris
- Undo/redo/reset 


#######perso oury#################
logique: à partir de la detection de fin de jeu:
    -les 2 joueurs ont placé tous leurs pions
Il faudra attribué les fleurs restantes aux 2 joueurs 
    -Utilsation de la methode brute au debut
    -plus tard ajout d'une animation.

--apres implementaton de cette logique, vu que c'est dans mousePressed secondePhase() 
 est appélée, alors elle n'est déclenchée qu'après un clic de la souris
--Donc il faudra que ça se fasse automatiquement plus tard (ça sera pour l'amelioration que je ferai après)

--Amelioration eventuelle: faire une animation pour la seconde phase car elle est brute pour l'instant
