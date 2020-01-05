package model

import (
	"embargos/utils"
)

// Reprensenta un Intento en la blockchain, utilizada para la deserializacion y serializacion
// de un Intento en formato JSON
type Intento struct {
	FechaEjecucion utils.Fecha `json:"fechaEjecucion"`
	Exito          bool        `json:"exito,omitempty"`
	Mensaje        string      `json:"mensaje"`
	Cuentas        []Cuenta    `json:"cuentas"`
}
