insert into venue (nombre, direccion, capacidad, ciudad) values
('Auditorio Central', 'Calle 123 #45-67', 300, 'Bogota'),
('Centro de Convenciones Norte', 'Avenida 19 #101-20', 1200, 'Bogota'),
('Teatro Metropolitano', 'Carrera 50 #40-10', 850, 'Medellin'),
('Plaza Mayor', 'Calle 41 #55-80', 2500, 'Medellin'),
('Arena Pacifico', 'Avenida 6N #20-55', 5000, 'Cali'),
('Casa Cultural Sur', 'Carrera 15 #12-30', 180, 'Cali'),
('Centro Historico', 'Calle 35 #8-15', 450, 'Cartagena'),
('Club Ejecutivo', 'Carrera 54 #72-80', 220, 'Barranquilla'),
('Parque Tecnologico', 'Anillo Vial Km 4', 1500, 'Bucaramanga'),
('Foro Universitario', 'Campus Central', 600, 'Manizales'),
('Estadio Municipal', 'Avenida Olimpica #1-01', 18000, 'Pereira'),
('Salon Empresarial', 'Calle 80 #12-10', 320, 'Bogota'),
('Museo de Arte Moderno', 'Calle 24 #6-00', 260, 'Medellin'),
('Jardin Botanico', 'Avenida Verde #10-40', 900, 'Cali'),
('Centro Deportivo Nacional', 'Carrera 30 #63-00', 4000, 'Bogota');

insert into categories (name, description) values
('Conciertos', 'Eventos musicales en vivo'),
('Talleres', 'Sesiones practicas y formativas'),
('Conferencias', 'Charlas y ponencias especializadas'),
('Deportes', 'Competiciones y actividades deportivas'),
('Gastronomia', 'Experiencias culinarias y degustaciones'),
('Festivales', 'Celebraciones y encuentros de gran formato'),
('Teatro', 'Obras escenicas y presentaciones teatrales');

insert into event (nombre, fecha, descripcion, venue_id, active)
select
    'Evento ' || lpad(x, 3, '0'),
    dateadd('DAY', mod(x, 180), date '2026-01-01'),
    case mod(x, 8)
        when 0 then 'Encuentro de tecnologia'
        when 1 then 'Experiencia musical'
        when 2 then 'Competencia deportiva'
        when 3 then 'Agenda cultural'
        when 4 then 'Foro de negocios'
        when 5 then 'Jornada educativa'
        when 6 then 'Muestra gastronomica'
        else 'Seminario de salud'
    end,
    mod(x - 1, 15) + 1,
    true
from system_range(1, 200);

insert into event_categories (event_id, category_id)
select x, mod(x - 1, 7) + 1
from system_range(1, 200);

insert into event_categories (event_id, category_id)
select x, mod(x + 2, 7) + 1
from system_range(1, 200)
where mod(x, 3) = 0;
