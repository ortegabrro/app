package model

// Reprensenta un Demandado en la blockchain, utilizada para la deserializacion y serializacion
// de un demandado en formato JSON
type Demandado struct {
	Persona
	ResEmbargo      string         `json:"resEmbargo,omitempty"`
	FechaResolucion utils.Fecha    `json:"fechaResolucion,omitempty"`
	MontoAEmbargar  float64        `json:"montoAEmbargar"`
	MontoEmbargado  float64        `json:"montoEmbargado"`
	Intentos        []Intento      `json:"intentos"`
}
