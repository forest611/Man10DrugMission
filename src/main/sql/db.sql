create table dunce_table
(
	id int auto_increment,
	player varchar(16) null,
	uuid varchar(36) null,
	disconnect_count int default 0 null,
	constraint dunce_table_pk
		primary key (id)
);

create index dunce_table_uuid_index
	on dunce_table (uuid);

