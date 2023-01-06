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

CREATE TABLE IF NOT EXISTS Item_store (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT
);

CREATE TABLE IF NOT EXISTS Egg_type (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    type TEXT CHECK( type IN ('common', 'rare', 'super rare', 'legendary') ) NOT NULL,
    id_item_store BIGINT NOT NULL,
    FOREIGN KEY (id_item_store) REFERENCES Item_store(id)
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
    speed TEXT CHECK( speed IN ('slow', 'medium', 'fast')) NOT NULL,
    id_item_store INTEGER NOT NULL,
    FOREIGN KEY (id_item_store) REFERENCES Item_store(id)
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
    id_item_1 INTEGER NOT NULL,
    id_item_2 INTEGER NOT NULL,
    id_item_3 INTEGER NOT NULL,
    price_1 INT NOT NULL,
    price_2 INT NOT NULL,
    price_3 INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES User(id),
    FOREIGN KEY (id_item_1) REFERENCES Item_store(id),
    FOREIGN KEY (id_item_2) REFERENCES Item_store(id),
    FOREIGN KEY (id_item_3) REFERENCES Item_store(id)
);

INSERT OR REPLACE INTO "Item_store" (id) VALUES (1);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (2);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (3);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (4);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (5);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (6);
INSERT OR REPLACE INTO "Item_store" (id) VALUES (7);


INSERT OR REPLACE INTO "Egg_type" (id, type, id_item_store) VALUES (1, 'common', 1);
INSERT OR REPLACE INTO "Egg_type" (id, type, id_item_store) VALUES (2, 'rare', 2);
INSERT OR REPLACE INTO "Egg_type" (id, type, id_item_store) VALUES (3, 'super rare', 3);
INSERT OR REPLACE INTO "Egg_type" (id, type, id_item_store) VALUES (4, 'legendary', 4);

INSERT OR REPLACE INTO "Incubator_type" (id, speed, id_item_store) VALUES (1, 'slow', 5);
INSERT OR REPLACE INTO "Incubator_type" (id, speed, id_item_store) VALUES (2, 'medium', 6);
INSERT OR REPLACE INTO "Incubator_type" (id, speed, id_item_store) VALUES (3, 'fast', 7);

INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (1, 1, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (2, 2, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (3, 3, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (4, 4, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (5, 5, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (6, 6, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (7, 7, 1);
INSERT OR REPLACE INTO "Tower_type" (id, difficulty, id_trainer) VALUES (8, 8, 1);

INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (0, 'Tortipouss', '', 55,68,64,31,55,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (1, 'Boskara', '', 75,89,85,36,65,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (2, 'Torterra', '', 95,109,105,56,85,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (3, 'Ouisticram', '', 44,58,44,61,44,58,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (4, 'Chimpenfeu', '', 64,78,52,81,52,78,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (5, 'Simiabraz', '', 76,104,71,108,71,104,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (6, 'Tiplouf', '', 53,51,53,40,56,61,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (7, 'Prinplouf', '', 64,66,68,50,76,81,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (8, 'Pingoléon', '', 84,86,88,60,101,111,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (9, 'Étourmi', '', 40,55,30,60,30,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (10, 'Étourvol', '', 55,75,50,80,40,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (11, 'Étouraptor', '', 85,120,70,100,60,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (12, 'Keunotor', '', 59,45,40,31,40,35,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (13, 'Castorno', '', 79,85,60,71,60,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (14, 'Crikzik', '', 37,25,41,25,41,25,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (15, 'Mélokrik', '', 77,85,51,65,51,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (16, 'Lixy', '', 45,65,34,45,34,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (17, 'Luxio', '', 60,85,49,60,49,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (18, 'Luxray', '', 80,120,79,70,79,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (19, 'Rozbouton', '', 40,30,35,55,70,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (20, 'Roserade', '', 60,70,65,90,105,125,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (21, 'Kranidos', '', 67,125,40,58,30,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (22, 'Charkos', '', 97,165,60,58,50,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (23, 'Dinoclier', '', 30,42,118,30,88,42,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (24, 'Bastiodon', '', 60,52,168,30,138,47,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (25, 'Cheniti', '', 40,29,45,36,45,29,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (26, 'Cheniselle', '', 60,59,85,36,105,79,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (27, 'Papilord', '', 70,94,50,66,50,94,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (28, 'Apitrini', '', 30,30,42,70,42,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (29, 'Apireine', '', 70,80,102,40,102,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (30, 'Pachirisu', '', 60,45,70,95,90,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (31, 'Mustébouée', '', 55,65,35,85,30,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (32, 'Mustéflott', '', 85,105,55,115,50,85,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (33, 'Ceribou', '', 45,35,45,35,53,62,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (34, 'Ceriflor', '', 70,60,70,85,78,87,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (35, 'Sancoki', '', 76,48,48,34,62,57,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (36, 'Tritosor', '', 111,83,68,39,82,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (37, 'Capidextre', '', 75,100,66,115,66,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (38, 'Baudrive', '', 90,50,34,70,44,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (39, 'Grodrive', '', 150,80,44,80,54,90,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (40, 'Laporeille', '', 55,66,44,85,56,44,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (41, 'Lockpin', '', 65,76,84,105,96,54,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (42, 'Magirêve', '', 60,60,60,105,105,105,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (43, 'Corboss', '', 100,125,52,71,52,105,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (44, 'Chaglam', '', 49,55,42,85,37,42,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (45, 'Chaffreux', '', 71,82,64,112,59,64,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (46, 'Korillon', '', 45,30,50,45,50,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (47, 'Moufouette', '', 63,63,47,74,41,41,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (48, 'Moufflair', '', 103,93,67,84,61,71,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (49, 'Archéomire', '', 57,24,86,23,86,24,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (50, 'Archéodong', '', 67,89,116,33,116,79,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (51, 'Manzaï', '', 50,80,95,10,45,10,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (52, 'Mime Jr.', '', 20,25,45,60,90,70,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (53, 'Ptiravi', '', 100,5,5,30,65,15,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (54, 'Pijako', '', 76,65,45,91,42,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (55, 'Spiritomb', '', 50,92,108,35,108,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (56, 'Griknot', '', 58,70,45,42,45,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (57, 'Carmache', '', 68,90,65,82,55,50,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (58, 'Carchacrok', '', 108,130,95,102,85,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (59, 'Goinfrex', '', 135,85,40,5,85,40,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (60, 'Riolu', '', 40,70,40,60,40,35,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (61, 'Lucario', '', 70,110,70,90,70,115,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (62, 'Hippopotas', '', 68,72,78,32,42,38,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (63, 'Hippodocus', '', 108,112,118,47,72,68,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (64, 'Rapion', '', 40,50,90,65,55,30,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (65, 'Drascore', '', 70,90,110,95,75,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (66, 'Cradopaud', '', 48,61,40,50,40,61,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (67, 'Coatox', '', 83,106,65,85,65,86,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (68, 'Vortente', '', 74,100,72,46,72,90,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (69, 'Écayon', '', 49,49,56,66,61,49,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (70, 'Luminéon', '', 69,69,76,91,86,69,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (71, 'Babimanta', '', 45,20,50,50,120,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (72, 'Blizzi', '', 60,62,50,40,60,62,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (73, 'Blizzaroi', '', 90,92,75,60,85,92,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (74, 'Dimoret', '', 70,120,65,125,85,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (75, 'Magnézone', '', 70,70,115,60,90,130,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (76, 'Coudlangue', '', 110,85,95,50,95,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (77, 'Rhinastoc', '', 115,140,130,40,55,55,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (78, 'Bouldeneu', '', 100,100,125,50,50,110,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (79, 'Élekable', '', 75,123,67,95,85,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (80, 'Maganon', '', 75,95,67,83,95,125,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (81, 'Togekiss', '', 85,50,95,80,115,120,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (82, 'Yanmega', '', 86,76,86,95,56,116,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (83, 'Phyllali', '', 65,110,130,95,65,60,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (84, 'Givrali', '', 65,60,110,65,95,130,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (85, 'Scorvol', '', 75,95,125,95,75,45,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (86, 'Mammochon', '', 110,130,80,80,60,70,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (87, 'Porygon-Z', '', 85,80,70,90,75,135,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (88, 'Gallame', '', 68,125,65,80,115,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (89, 'Tarinorme', '', 60,55,145,40,150,75,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (90, 'Noctunoir', '', 45,100,135,45,135,65,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (91, 'Momartik', '', 70,80,70,110,70,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (92, 'Motisma', '', 50,50,77,91,77,95,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (93, 'Créhelf', '', 75,75,130,95,130,75,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (94, 'Créfollet', '', 80,105,105,80,105,105,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (95, 'Créfadet', '', 75,125,70,115,70,125,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (96, 'Dialga', '', 100,120,120,90,100,150,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (97, 'Palkia', '', 90,120,100,100,120,150,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (98, 'Heatran', '', 91,90,106,77,106,130,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (99, 'Regigigas', '', 110,160,110,100,110,80,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (100, 'Giratina', '', 150,100,120,90,120,100,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (101, 'Cresselia', '', 120,70,120,85,130,75,1);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (102, 'Phione', '', 80,80,80,80,80,80,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (103, 'Manaphy', '', 100,100,100,100,100,100,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (104, 'Darkrai', '', 70,90,90,125,90,135,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (105, 'Shaymin', '', 100,100,100,100,100,100,0);
INSERT OR REPLACE INTO 'Pokemon_type' (id, name, description, hp, attack, defense, speed, defense_special, attack_special, is_legendary) VALUES (106, 'Arceus', '', 120,120,120,120,120,120,1);
