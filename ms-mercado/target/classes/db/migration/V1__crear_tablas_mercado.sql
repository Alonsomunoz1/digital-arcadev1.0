CREATE TABLE IF NOT EXISTS metodos_pago (
    id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
    nombre_metodo_pago VARCHAR(60) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS ordenes_mercado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_juego INT NOT NULL,
    id_item INT NOT NULL,
    precio_venta INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    CONSTRAINT chk_precio_venta CHECK (precio_venta >= 1)
);

INSERT INTO metodos_pago (nombre_metodo_pago, descripcion, estado) VALUES
('Tarjeta de credito', 'Pago con tarjetas Visa/Mastercard', 'ACTIVO'),
('Webpay', 'Pago a traves de Transbank Webpay', 'ACTIVO'),
('Saldo comunidad', 'Saldo acumulado dentro de la tienda', 'ACTIVO');

INSERT INTO ordenes_mercado (id_usuario, id_juego, id_item, precio_venta, estado) VALUES
(1, 1, 1, 5000, 'DISPONIBLE'),
(2, 1, 2, 2500, 'DISPONIBLE'),
(1, 2, 3, 8000, 'VENDIDO');
