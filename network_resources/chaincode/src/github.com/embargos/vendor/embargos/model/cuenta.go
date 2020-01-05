package model

import (
	"embargos/utils"
)

// Reprensenta una cuanta de un banco en la blockchain, utilizada para la deserializacion y serializacion
// de una cuenta en formato JSON
type Cuenta struct {
	IdBanco          string      `json:"idBanco"`
	IdCuenta         string      `json:"idCuenta"`
	TipoCuenta       string      `json:"tipoCuenta"`
	SubTipoCuenta    string      `json:"subTipoCuenta"`
	FechaCreacion    utils.Fecha `json:"fechaCreacion"`
	MontoEmbargado   float64     `json:"montoEmbargado"`
	Reglas           []string    `json:"reglas"`
	SaldoCuentaFecha float64     `json:"saldoCuentaFecha"`
	Estado           string      `json:"estado"`
}
