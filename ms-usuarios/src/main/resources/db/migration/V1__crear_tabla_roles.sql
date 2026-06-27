CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(60) NOT NULL UNIQUE,
    correo VARCHAR(120) NOT NULL UNIQUE,
    tipo_rol VARCHAR(30) NOT NULL,
    estado_conexion VARCHAR(20) NOT NULL DEFAULT 'DESCONECTADO'
);

INSERT INTO roles (nombre_usuario, correo, tipo_rol, estado_conexion) VALUES
('admin_arcade', 'admin@digitalarcade.cl', 'ADMIN', 'EN_LINEA'),
('jugador_uno', 'jugador1@digitalarcade.cl', 'JUGADOR', 'DESCONECTADO'),
('comerciante_pro', 'pro@digitalarcade.cl', 'JUGADOR', 'EN_EL_JUEGO');
