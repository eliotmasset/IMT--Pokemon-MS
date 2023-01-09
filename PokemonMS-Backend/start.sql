CREATE TABLE IF NOT EXISTS Pokemon_type (  
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    defense INT NOT NULL,
    attack INT NOT NULL,
    defense_special INT NOT NULL,
    attack_special INT NOT NULL,
    speed INT NOT NULL,
    hp INT NOT NULL,
    is_legendary BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS Trainer (  
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    gender BOOLEAN NOT NULL,
    money_to_give INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Pokemon (  
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_pokemon_type INTEGER NOT NULL,
    id_trainer INTEGER NOT NULL,
    level INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    defense INT NOT NULL,
    attack INT NOT NULL,
    defense_special INT NOT NULL,
    attack_special INT NOT NULL,
    speed INT NOT NULL,
    hp INT NOT NULL,
    gender BOOLEAN NOT NULL,
    current_exp INT NOT NULL,
    exp_to_next_level INT NOT NULL,
    is_shiny BOOLEAN NOT NULL,
    FOREIGN KEY (id_pokemon_type) REFERENCES Pokemon_type(id),
    FOREIGN KEY (id_trainer) REFERENCES Trainer(id)
);

CREATE TABLE IF NOT EXISTS User (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_trainer INTEGER NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    money INT NOT NULL,
    FOREIGN KEY (id_trainer) REFERENCES Trainer(id)
);

CREATE TABLE IF NOT EXISTS Tower_type (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_trainer INTEGER NOT NULL,
    difficulty INT NOT NULL,
    FOREIGN KEY (id_trainer) REFERENCES Trainer(id)
);

CREATE TABLE IF NOT EXISTS Tower (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_tower_type INTEGER NOT NULL,
    id_user INTEGER,
    floor INT,
    FOREIGN KEY (id_tower_type) REFERENCES Tower_type(id),
    FOREIGN KEY (id_user) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Egg_type (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    type TEXT CHECK( type IN ('common', 'epic', 'legendary') ) NOT NULL
);

CREATE TABLE IF NOT EXISTS Egg (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_egg_type INTEGER NOT NULL,
    id_pokemon_type INTEGER NOT NULL,
    id_user INTEGER NOT NULL,
    time_to_incubate BIGINT NOT NULL,
    FOREIGN KEY (id_egg_type) REFERENCES Egg_type(id),
    FOREIGN KEY (id_pokemon_type) REFERENCES Pokemon_type(id),
    FOREIGN KEY (id_user) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Incubator_type (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    speed TEXT CHECK( speed IN ('slow', 'medium', 'fast')) NOT NULL
);


CREATE TABLE IF NOT EXISTS Incubator (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_incubator_type INTEGER NOT NULL,
    id_egg INTEGER,
    id_user INTEGER NOT NULL,
    FOREIGN KEY (id_incubator_type) REFERENCES Incubator_type(id),
    FOREIGN KEY (id_egg) REFERENCES Egg(id),
    FOREIGN KEY (id_user) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Store (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    id_user INTEGER NOT NULL,
    id_pokemon_type_1 INTEGER NOT NULL,
    id_pokemon_type_2 INTEGER NOT NULL,
    id_pokemon_type_3 INTEGER NOT NULL,
    price_1 INT NOT NULL,
    price_2 INT NOT NULL,
    price_3 INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES User(id),
    FOREIGN KEY (id_pokemon_type_1) REFERENCES Pokemon_type(id),
    FOREIGN KEY (id_pokemon_type_2) REFERENCES Pokemon_type(id),
    FOREIGN KEY (id_pokemon_type_3) REFERENCES Pokemon_type(id)
);

INSERT OR REPLACE INTO "Egg_type" (id, type) VALUES (1, 'common');
INSERT OR REPLACE INTO "Egg_type" (id, type) VALUES (2, 'epic');
INSERT OR REPLACE INTO "Egg_type" (id, type) VALUES (3, 'legendary');

INSERT OR REPLACE INTO "Incubator_type" (id, speed) VALUES (1, 'slow');
INSERT OR REPLACE INTO "Incubator_type" (id, speed) VALUES (2, 'medium');
INSERT OR REPLACE INTO "Incubator_type" (id, speed) VALUES (3, 'fast');

INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (1, 1, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (2, 2, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (3, 3, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (4, 4, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (5, 5, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (6, 6, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (7, 7, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (8, 8, 1);

INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (1, 'Tortipouss', 'Son corps produit de l’oxygène par photosynthèse.La feuille sur sa tête flétrit quand il a soif.', 55,68,64,31,55,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (2, 'Boskara', 'Il sait d’instinct où trouver une source d’eau pure.Il y transporte d’autres Pokémon sur son dos.', 75,89,85,36,65,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (3, 'Torterra', 'Il arrive que de petits Pokémon se rassemblent sur son dosimmobile pour y faire leur nid.', 95,109,105,56,85,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (4, 'Ouisticram', 'La flamme de son postérieur brûle grâce à un gaz de sonestomac. Elle faiblit quand il ne va pas bien.', 44,58,44,61,44,58,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (5, 'Chimpenfeu', 'Il attaque en prenant appui sur les murs et les plafonds.Sa queue ardente n’est pas son seul atout.', 64,78,52,81,52,78,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (6, 'Simiabraz', 'Il fait voltiger ses ennemis grâce à sa vitesse et son stylede combat spécial qui utilise ses quatre membres.', 76,104,71,108,71,104,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (7, 'Tiplouf', 'Il est fier et déteste accepter la nourriture qu’on lui offre.Son pelage épais le protège du froid.', 53,51,53,40,56,61,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (8, 'Prinplouf', 'C’est un Pokémon solitaire. Un seul coup de ses puissantesailes peut briser un arbre en deux.', 64,66,68,50,76,81,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (9, 'Pingoléon', 'Les trois cornes de son bec sont le symbole de sa force.Celles du chef sont plus grosses que les autres.', 84,86,88,60,101,111,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (10, 'Étourmi', 'Ce Pokémon très bruyant parcourt champs et forêts en nuéespour chasser les Pokémon Insecte.', 40,55,30,60,30,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (11, 'Étourvol', 'Il peuple les champs et les forêts. Lorsque deux voléesse croisent, elles luttent pour le territoire.', 55,75,50,80,40,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (12, 'Étouraptor', 'Lorsque Étourvol évolue en Étouraptor, il quitte son groupepour vivre seul. Ses ailes sont très développées.', 85,120,70,100,60,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (13, 'Keunotor', 'Il ronge constamment des troncs et des pierres pour se faireles incisives. Il niche le long de l’eau.', 59,45,40,31,40,35,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (14, 'Castorno', 'Il construit des barrages de boue et d’écorce le longdes fleuves. C’est un ouvrier de renom.', 79,85,60,71,60,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (15, 'Crikzik', 'Quand ses antennes s’entrechoquent, elles laissent échapperun bruit de xylophone.', 37,25,41,25,41,25,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (16, 'Mélokrik', 'Il exprime ses émotions par des mélodies. Les scientifiquesétudient actuellement leur structure.', 77,85,51,65,51,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (17, 'Lixy', 'Sa fourrure s’illumine grâce à l’électricité.Il agite l’extrémité lumineuse de sa queuepour envoyer des signes à ses congénères.', 45,65,34,45,34,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (18, 'Luxio', 'Quand il croise un ennemi, il se prépareau combat en sortant ses griffes dont les coupsont une puissance qui atteint un million de volts.', 60,85,49,60,49,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (19, 'Luxray', 'Il dort beaucoup pour récupérer la quantitéd’énergie électrique énorme qu’il consommeafin de voir à travers la matière.', 80,120,79,70,79,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (20, 'Rozbouton', 'Il est sensible aux variations de température.Quand son bourgeon commence à s’ouvrir,l’arrivée du printemps est imminente.', 40,30,35,55,70,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (21, 'Roserade', 'Sa main gauche administre un poison lent,et la droite, un poison foudroyant.Les deux peuvent être fatals.', 60,70,65,90,105,125,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (22, 'Kranidos', 'Son crâne extrêmement solide est sa principalecaractéristique. Il s’en servait pour faire tomberdes arbres les Baies dont il se nourrissait.', 67,125,40,58,30,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (23, 'Charkos', 'Pokémon préhistorique célèbre pour ses coupsde tête puissants et son petit cerveau, sonextinction serait due à son extrême bêtise.', 97,165,60,58,50,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (24, 'Dinoclier', 'On retrouve des fossiles de Dinoclier dansles couches sédimentaires préhistoriques, maisseule sa tête a résisté à l’épreuve du temps.', 30,42,118,30,88,42,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (25, 'Bastiodon', 'Un Pokémon éteint depuis environ 100 millionsd’années. Sa tête incroyablement solide étaitplus dure que l’acier.', 60,52,168,30,138,47,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (26, 'Cheniti', 'S’il perd sa cape au combat, il en tisse rapidementune nouvelle avec ce qui lui tombe sous la main.', 40,29,45,36,45,29,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (27, 'Cheniselle', 'Quand Cheniti a évolué, sa cape a fusionné avec son corps.Cheniselle ne s’en sépare jamais.', 60,59,85,36,105,79,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (28, 'Papilord', 'Ce Pokémon s’active à la nuit tombée pour voler le mieldes Apitrini pendant leur sommeil.', 70,94,50,66,50,94,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (29, 'Apitrini', 'Du lever du soleil à la tombée de la nuit,il récolte du nectar pour Apireine et sa colonie.', 30,30,42,70,42,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (30, 'Apireine', 'Plus la quantité de phéromones qu’il libèreest élevée, plus le nombre d’Apitrini qu’il peutdiriger est grand.', 70,80,102,40,102,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (31, 'Pachirisu', 'Il arrive que deux Pachirisu se frottent les joues pour partagerl’électricité qu’ils ont accumulée.', 60,45,70,95,90,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (32, 'Mustébouée', 'Il utilise la bouée autour de son cou pour passer sa tête horsde l’eau et observer les alentours.', 55,65,35,85,30,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (33, 'Mustéflott', 'Il a développé une bouée pour poursuivre ses proiesaquatiques. Elle fait office de canot gonflable.', 85,105,55,115,50,85,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (34, 'Ceribou', 'Plus son corps est rouge, plus la saveurde sa petite boule gorgée de nutrimentsest sucrée et délicieuse.', 45,35,45,35,53,62,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (35, 'Ceriflor', 'Lorsqu’il se ferme, ses pétales sont si durs qu’ilne craint plus les attaques des Pokémon oiseaux.', 70,60,70,85,78,87,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (36, 'Sancoki', 'Si on le caresse trop fort, il sécrète un étrangeliquide mauve qui est inoffensif, mais dontla texture est très gluante.', 76,48,48,34,62,57,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (37, 'Tritosor', 'Son corps est caoutchouteux, mais très résistant,car la puissance des chocs qu’il subit se répandsur toute sa surface.', 111,83,68,39,82,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (38, 'Capidextre', 'Il utilise toujours ses deux queues pour fairela moindre tâche. Il s’en sert aussi pour enlacerles gens qu’il affectionne tout particulièrement.', 75,100,66,115,66,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (39, 'Baudrive', 'Nés d’une concentration d’âmes, ces Pokémonpeuvent apparaître en grand nombre les joursoù le taux d’humidité est élevé.', 90,50,34,70,44,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (40, 'Grodrive', 'Il attrape des personnes et des Pokémon pourles transporter vers une destination inconnue.', 150,80,44,80,54,90,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (41, 'Laporeille', 'Plus il s’entraîne, plus les coups qu’il assèneen déroulant vigoureusement ses oreillesgagnent en puissance.', 55,66,44,85,56,44,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (42, 'Lockpin', 'À la fin de la saison chaude, il mue et se recouvred’une fourrure épaisse et légère pour se préparerà affronter le froid hivernal.', 65,76,84,105,96,54,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (43, 'Magirêve', 'Il susurre des malédictions pour tourmenterses ennemis et leur infliger de terriblesmigraines ou des hallucinations terrifiantes.', 60,60,60,105,105,105,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (44, 'Corboss', 'Ses sous-fifres font le sale boulot pour lui.Il n’intervient en combat que pour donnerle coup de grâce à son ennemi.', 100,125,52,71,52,105,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (45, 'Chaglam', 'Lorsqu’il est heureux, sa queue décrit des arabesquescomme un ruban de gymnastique rythmique.', 49,55,42,85,37,42,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (46, 'Chaffreux', 'Il ceinture sa taille de sa queue bifide pour paraîtreplus imposant.', 71,82,64,112,59,64,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (47, 'Korillon', 'Quand il sautille, l’orbe qu’il a dans la bouche s’agite et tintecomme une cloche.', 45,30,50,45,50,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (48, 'Moufouette', 'S’il lève la queue, c’est qu’il s’apprête à projeterun liquide tellement nauséabond qu’il peut faireperdre connaissance.', 63,63,47,74,41,41,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (49, 'Moufflair', 'Ce Pokémon, qui creuse son terrier dans le sol,peut projeter un liquide à l’odeur infecte avecle bout de sa queue.', 103,93,67,84,61,71,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (50, 'Archéomire', 'On raconte qu’il se met à briller et à refléterla vérité quand on le polit, mais il a horreurde cela.', 57,24,86,23,86,24,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (51, 'Archéodong', 'De nombreux savants pensent que ce Pokémonn’est pas originaire de Galar à cause des motifsqui ornent son corps.', 67,89,116,33,116,79,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (52, 'Manzaï', 'Il vit dans les zones rocheuses arides.Quand ses boules vertes sont sèches,elles se mettent à briller légèrement.', 50,80,95,10,45,10,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (53, 'Mime Jr.', 'Il considère les meilleurs danseurs parmiles M. Glaquette comme ses maîtreset s’évertue à mimer leurs mouvements.', 20,25,45,60,90,70,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (54, 'Ptiravi', 'Il prête volontiers son caillou rond à ceuxqu’il apprécie, mais éclate en sanglots etse met en colère si on ne le lui rend pas.', 100,5,5,30,65,15,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (55, 'Pijako', 'On peut lui enseigner quelques mots. S’il s’agit d’un groupe,ils retiendront les mêmes phrases.', 76,65,45,91,42,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (56, 'Spiritomb', 'Pour punir son comportement déplorable,un voyageur a utilisé un mystérieux sortilègepour l’enchaîner à une Clé de Voûte.', 50,92,108,35,108,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (57, 'Griknot', 'Il attaque et entraîne dans son antre tout ce quibouge, sans distinction. Sa gueule est immense,mais son estomac est relativement petit.', 58,70,45,42,45,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (58, 'Carmache', 'Il cache des pierres précieuses dans le solde sa grotte, et accueille à coups de griffeset de crocs quiconque ose y pénétrer.', 68,90,65,82,55,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (59, 'Carchacrok', 'Ce Pokémon très rapide même au sol captureses proies sur les monts enneigés et rentre vitedans son antre avant que son corps ne gèle.', 108,130,95,102,85,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (60, 'Goinfrex', 'Il conserve sa nourriture sous son épaisse fourrure.Il lui arrive d’en offrir un peu à ceux en quiil a confiance.', 135,85,40,5,85,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (61, 'Riolu', 'Il perçoit sous forme d’aura non seulementles sentiments des autres, mais aussi l’étatde la nature.', 40,70,40,60,40,35,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (62, 'Lucario', 'Étant capable de lire ce que pensent les humains,ce Pokémon ne s’attache qu’aux Dresseurs dotésd’un cœur pur.', 70,110,70,90,70,115,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (63, 'Hippopotas', 'Il est principalement actif pendant la journée.Il s’enfouit profondément dans le sable pourdormir, car les nuits désertiques sont très froides.', 68,72,78,32,42,38,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (64, 'Hippodocus', 'Il entre dans une violente colère quandon l’énerve : il expulse le sable qu’il a accumulépour provoquer de véritables tempêtes.', 108,112,118,47,72,68,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (65, 'Rapion', 'Il attaque avec les pinces empoisonnéesde sa queue. Le venin se répand alorsdans le corps de la victime et la paralyse.', 40,50,90,65,55,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (66, 'Drascore', 'Sa férocité lui a valu le surnom de « démondu désert », mais face à un Hippodocus, il estparfaitement docile et évite de le provoquer.', 70,90,110,95,75,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (67, 'Cradopaud', 'Dilué, son poison sert de remède. Cela lui a valude devenir la mascotte populaire d’une entreprisede produits pharmaceutiques.', 48,61,40,50,40,61,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (68, 'Coatox', 'Il lance un coassement viril de victoirequand il parvient à terrasser sa proie.Il s’apparente aux Crapustule.', 83,106,65,85,65,86,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (69, 'Vortente', 'Il s’accroche aux arbres des marais et attire ses proies avecsa salive à l’odeur enivrante.', 74,100,72,46,72,90,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (70, 'Écayon', 'Il attire ses proies avec ses nageoires caudaleslumineuses. Il passe la journée à la surfaceet se retire dans les abysses la nuit venue.', 49,49,56,66,61,49,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (71, 'Luminéon', 'Il se déplace en rampant dans les profondeursde l’océan. Ses nageoires brillent telles desétoiles dans le ciel nocturne.', 69,69,76,91,86,69,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (72, 'Babimanta', 'Il nage au sein des bancs de Rémoraid.Lorsqu’un prédateur les attaque, il se batà leurs côtés.', 45,20,50,50,120,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (73, 'Blizzi', 'Les Baies en forme de crèmes glacées quipoussent sur son ventre sont le mets favorides Darumarond vivant à Galar.', 60,62,50,40,60,62,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (74, 'Blizzaroi', 'Ce grand Pokémon déclenche des blizzards.Lorsqu’il agite son corps, tout ce qui l’entourese retrouve couvert de neige.', 90,92,75,60,85,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (75, 'Dimoret', 'Il communique avec ses semblables en gravantdes motifs avec ses griffes. Il existerait prèsde 500 signes différents.', 70,120,65,125,85,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (76, 'Magnézone', 'On pense qu’il a évolué après avoir été exposéà un champ magnétique particulier qui a modifiéla structure moléculaire de son corps.', 70,70,115,60,90,130,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (77, 'Coudlangue', 'Il est aussi habile avec sa langue que les êtreshumains le sont avec leurs mains. En revanche,il est maladroit quand il utilise ses doigts.', 110,85,95,50,95,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (78, 'Rhinastoc', 'Il repousse les attaques avec ses Protecteurs etprofite du moment où l’adversaire est déstabilisépour le transpercer avec sa vrille, dont il est fier.', 115,140,130,40,55,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (79, 'Bouldeneu', 'Ses lianes poussent plus rapidement s’il vitsous un climat chaud. Quand elles deviennenttrop longues, il se les coupe.', 100,100,125,50,50,110,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (80, 'Élekable', 'La quantité d’électricité qu’il génère est énorme,même pour un Pokémon Électrik. Il inflige defortes décharges du bout de ses queues.', 75,123,67,95,85,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (81, 'Maganon', 'Au fil du temps, son corps est devenu similaireà un volcan, et un de ses organes ressembledésormais à une chambre magmatique.', 75,95,67,83,95,125,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (82, 'Togekiss', 'On dit que ce Pokémon dispense ses bienfaits,ce qui explique pourquoi il est représentésur les porte-bonheurs depuis fort longtemps.', 85,50,95,80,115,120,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (83, 'Yanmega', 'Il peut voler avec aise tout en portant un adulte dans ses sixpattes. Les ailes de sa queue lui servent de balancier.', 86,76,86,95,56,116,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (84, 'Phyllali', 'Grâce à sa queue aussi effilée qu’une lame, il estcapable de fendre en deux des arbres immenses.', 65,110,130,95,65,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (85, 'Givrali', 'Ce Pokémon exhale un vent glacial qui créeune neige poudreuse. Sa présence est trèsappréciée dans les stations de ski.', 65,60,110,65,95,130,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (86, 'Scorvol', 'Il vole sans un bruit, et capture ses proies avec sa queuepour leur infliger une morsure critique.', 75,95,125,95,75,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (87, 'Mammochon', 'Comme son physique le laisse deviner, il est trèsfort. La taille de ses défenses de glace augmenteà mesure que la température baisse.', 110,130,80,80,60,70,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (88, 'Porygon-Z', 'On dit qu’un nouveau programme l’a fait évoluer,mais les avis des scientifiques divergent quant àsavoir s’il s’agit réellement d’une évolution.', 85,80,70,90,75,135,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (89, 'Gallame', 'Ses sens aiguisés lui indiquent lorsque quelqu’unest en danger. Il accourt alors à la rescousse.', 68,125,65,80,115,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (90, 'Tarinorme', 'Il utilise ses trois petites unités pour combattreses ennemis et capturer ses proies. Le reste deson corps se contente de donner des ordres.', 60,55,145,40,150,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (91, 'Noctunoir', 'Il dévore l’âme de ses ennemis en lesengloutissant dans sa bouche ventrale.Il recrache ensuite leur enveloppe corporelle.', 45,100,135,45,135,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (92, 'Momartik', 'Il gèle ses proies avec son souffle à - 50 °C,les amène dans son antre, puis les y aligneavec soin.', 70,80,70,110,70,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (93, 'Motisma', 'Son corps composé de plasma lui permetd’infiltrer diverses machines.Il adore surprendre les gens.', 50,50,77,91,77,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (94, 'Créhelf', 'On dit que sa venue a fourni aux humains le bon sensnécessaire pour améliorer leur existence.', 75,75,130,95,130,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (95, 'Créfollet', 'Il dort au fond d’un lac. On dit que son esprit abandonneson corps pour voler à sa surface.', 80,105,105,80,105,105,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (96, 'Créfadet', 'On raconte que Créhelf, Créfollet et Créfadet proviennentdu même Œuf.', 75,125,70,115,70,125,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (97, 'Dialga', 'Il peut contrôler le temps. Les mythes de Sinnoh en parlentcomme d’une divinité ancienne.', 100,120,120,90,100,150,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (98, 'Palkia', 'Il peut modeler l’espace. Les mythes de Sinnoh en parlentcomme d’une divinité ancienne.', 90,120,100,100,120,150,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (99, 'Heatran', 'Le sang de ce Pokémon des cratères bouillonne dansson corps comme du magma.', 91,90,106,77,106,130,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (100, 'Regigigas', 'On raconte qu’après avoir créé des Pokémonavec différentes matières, il aurait placéles continents à leur position actuelle.', 110,160,110,100,110,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (101, 'Giratina', 'Sa grande violence lui a valu d’être banni. Il observeles hommes en silence depuis le Monde Distorsion.', 150,100,120,90,120,100,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (102, 'Cresselia', 'Dormir avec une de ses plumes à la main permet de fairede beaux rêves. On le surnomme « avatar du croissantde lune ».', 120,70,120,85,130,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (103, 'Phione', 'Ce Pokémon des mers chaudes revient toujours à son lieude naissance, peu importe la distance.', 80,80,80,80,80,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (104, 'Manaphy', 'Il possède l’étrange pouvoir de s’entendre avec n’importequel Pokémon.', 100,100,100,100,100,100,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (105, 'Darkrai', 'Il a la capacité de bercer les gens pour les faire rêver.Il se montre durant les nuits de nouvelle lune.', 70,90,90,125,90,135,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (106, 'Shaymin', 'Il prend son essor pour répandre la gratitude lorsqueles Gracidées fleurissent.', 100,100,100,100,100,100,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (107, 'Arceus', 'Dans la mythologie, ce Pokémon existait déjà avantla formation de l’univers.', 120,120,120,120,120,120,0);
