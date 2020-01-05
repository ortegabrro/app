package model

// Reprensenta una Persona en la blockchain, utilizada para la herencia de atributos
type Persona struct {
	Identificacion     string `json:"identificacion"`
	TipoIdentificacion string `json:"tipoIdentificacion"`
	Nombres            string `json:"nombres"`
	Apellidos          string `json:"apellidos"`
}
