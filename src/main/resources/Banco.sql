USE db_app1

Create TABLE Tb_User (
	id_User BIGINT NOT NULL AUTO_INCREMENT,
	Nome_User VARCHAR (50) NOT NULL,
	Email_User VARCHAR (50) NOT NULL,
	Endereco_User VARCHAR (50) NOT NULL,
	Senha_hash VARCHAR(255) NOT NULL, 
	PRIMARY KEY (id_User)  
)