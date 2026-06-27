CREATE TABLE IF NOT EXISTS juegos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL UNIQUE,
    precio INT NOT NULL,
    CONSTRAINT chk_juegos_precio CHECK (precio >= 1000)
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS items_catalogo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    id_juego INT NOT NULL,
    CONSTRAINT chk_items_catalogo_nombre CHECK (CHAR_LENGTH(nombre) >= 2),
    CONSTRAINT fk_item_juego FOREIGN KEY (id_juego) REFERENCES juegos(id)
);

CREATE TABLE IF NOT EXISTS item_categoria (
    id_item_categoria INT AUTO_INCREMENT PRIMARY KEY,
    id_item INT NOT NULL,
    id_categoria INT NOT NULL,
    CONSTRAINT uq_item_categoria UNIQUE (id_item, id_categoria),
    CONSTRAINT fk_ic_item FOREIGN KEY (id_item) REFERENCES items_catalogo(id),
    CONSTRAINT fk_ic_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

INSERT INTO juegos (titulo, precio) VALUES
('Warframe', 1000),
('Dota 2', 1000),
('Counter-Strike 2', 1000);

INSERT INTO categoria (nombre, descripcion) VALUES
('Skin', 'Apariencias cosmeticas para personajes o armas'),
('Mod', 'Modificadores que alteran estadisticas de juego'),
('Moneda', 'Divisas o recursos canjeables dentro del juego');

INSERT INTO items_catalogo (nombre, id_juego) VALUES
('Skin Arcana Excalibur', 1),
('Mod Serration', 1),
('Arcana Dragonclaw Hook', 2);

INSERT INTO item_categoria (id_item, id_categoria) VALUES
(1, 1),
(2, 2),
(3, 1);
