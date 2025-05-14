drop database if exists linkfix;
create database linkfix;

use linkfix;


/*creacion tablas*/
/*departamento*/
create table Departamento
(
	id int auto_increment,
	nombre varchar(50) not null,
	primary key (id)
);
/*provincia*/
create table Provincia
(
	id int auto_increment,
	nombre varchar(50) not null,
	primary key (id)
);
/*distrito*/
create table Distrito
(
	id int auto_increment,
	nombre varchar(50) not null,
	primary key (id)
);
/*persona*/
create table Persona
(
	id bigint auto_increment,
	nombre varchar(50),
	apellidos varchar(50),
	dni char(8) unique not null,
	idDepartamento int,
	idProvincia int, 
	idDistrito int,
	telefono varchar(9) unique,
	direcicon varchar(75),
	primary key (id)
);
/*usuario*/
create table Usuario
(
	id bigint auto_increment,
	idPersona bigint unique,
	correo varchar(50) unique not null,
	contrasena varchar(30),
	calificacion float,
	estado bit,
	primary key (id)
);
/*Rol*/
create table Rol
(
	id int auto_increment,
	nombre varchar(100) unique not null,
	primary key (id)
);
/*usuario_rol*/
create table Usuario_rol
(
	id bigint auto_increment,
	idUsuario bigint not null,
	idRol int not null,
	primary key (id)
);
/*Dia*/
create table Dia
(
	id int auto_increment,
	nombre varchar(10) unique not null,
	primary key (id)
);
/*disponibilidad*/
create table Disponibilidad
(
	id bigint auto_increment,
	idTecnico bigint not null,
	idDia int not null,
	hora_inicio time DEFAULT '00:00:00',
	hora_fin time DEFAULT '23:59:59',
	primary key (id)
);
/*Electrodomestico*/
create table Electrodomestico
(
	id bigint auto_increment,
	nombre varchar(100) unique not null,
	primary key (id)
);
/*Especialidad*/
create table Especialidad
(
	id bigint auto_increment,
	idTecnico bigint not null,
	idElectrodomestico bigint not null,
	primary key (id)
);

/*Solicitud_registro*/
create table Solicitud_registro
(
	id bigint auto_increment,
	idTecnico bigint unique not null,
	fecha DATETIME default NOW(),
	idAprobadoPor bigint not null,	
	primary key (id)
);

/*Estado*/
create table Estado
(
	id int auto_increment,
	nombre varchar(50) unique not null,		
	primary key (id)
);

/*Servicio*/
create table Servicio
(
	id bigint auto_increment,
	idCliente bigint not null,
	idTecnico bigint not null,
	idElectrodomestico bigint not null,
	fecha_solicitud DATETIME default now(),
	fecha_visita DATETIME,
	fecha_finalizacion DATETIME,
	idEstado int,
	calificacion float,	
	primary key (id)
);

/*Incidencia*/
create table Incidencia
(
	id bigint auto_increment,
	idUsuario bigint not null,
	idServicio bigint not null,
	idAdministrador bigint not null,
	titulo varchar(50) not null,
	descripcion varchar(300) not null,
	fecha_registro DATETIME default NOW(),
	fecha_atencion DATETIME,
	idEstado int,
	primary key (id)
);





/*----------------------*/
/*constraints fk*/
-- Persona -> Departamento, Provincia, Distrito
alter table Persona add constraint fk_persona_departamento
foreign key (idDepartamento) references Departamento(id);

alter table Persona add constraint fk_persona_provincia
foreign key (idProvincia) references Provincia(id);

alter table Persona add constraint fk_persona_distrito
foreign key (idDistrito) references Distrito(id);

-- Usuario -> Persona
alter table Usuario add constraint fk_usuario_persona
foreign key (idPersona) references Persona(id);

-- Usuario_rol -> Usuario, Rol
alter table Usuario_rol add constraint fk_usuario_rol_usuario
foreign key (idUsuario) references Usuario(id);

alter table Usuario_rol add constraint fk_usuario_rol_rol
foreign key (idRol) references Rol(id);

-- Disponibilidad -> Usuario, Dia
alter table Disponibilidad add constraint fk_disponibilidad_usuario
foreign key (idTecnico) references Usuario(id);

alter table Disponibilidad add constraint fk_disponibilidad_dia
foreign key (idDia) references Dia(id);

-- Especialidad -> Usuario, Electrodomestico
alter table Especialidad add constraint fk_especialidad_usuario
foreign key (idTecnico) references Usuario(id);

alter table Especialidad add constraint fk_especialidad_electro
foreign key (idElectrodomestico) references Electrodomestico(id);

-- Solicitud_registro -> Usuario (idTecnico y AprobadoPor)
alter table Solicitud_registro add constraint fk_solicitud_tecnico
foreign key (idTecnico) references Usuario(id);

alter table Solicitud_registro add constraint fk_solicitud_admin
foreign key (idAprobadoPor) references Usuario(id);

-- Servicio -> Usuario (cliente y técnico), Electrodomestico, Estado
alter table Servicio add constraint fk_servicio_cliente
foreign key (idCliente) references Usuario(id);

alter table Servicio add constraint fk_servicio_tecnico
foreign key (idTecnico) references Usuario(id);

alter table Servicio add constraint fk_servicio_electro
foreign key (idElectrodomestico) references Electrodomestico(id);

alter table Servicio add constraint fk_servicio_estado
foreign key (idEstado) references Estado(id);

-- Incidencia -> Usuario (cliente o técnico), Servicio, Usuario (admin), Estado
alter table Incidencia add constraint fk_incidencia_usuario
foreign key (idUsuario) references Usuario(id);

alter table Incidencia add constraint fk_incidencia_servicio
foreign key (idServicio) references Servicio(id);

alter table Incidencia add constraint fk_incidencia_admin
foreign key (idAdministrador) references Usuario(id);

alter table Incidencia add constraint fk_incidencia_estado
foreign key (idEstado) references Estado(id);




