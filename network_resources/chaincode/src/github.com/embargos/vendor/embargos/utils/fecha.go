package utils

// Representa una fecha para ser serializada o deserializada en formato JSON, tiene un formato en
// Ingles para permitir la interaccion con diferentes tipos de Date de la mayoria de los lenguajes de programacion
type Fecha struct {
	Year  int `json:"year"`
	Month int `json:"month"`
	Day   int `json:"day"`
}
