// Paquete utils provee utilidades para dar manejo a fechas y dar formato a las respuestas
// de consultas realizadas a la blockchain
package utils

import (
	"bytes"
	"fmt"

	"github.com/hyperledger/fabric-chaincode-go/shim"
	pb "github.com/hyperledger/fabric-protos-go/peer"
)

// ConsultaPorString permite una consulta a la blockchain en formato libre
// Recibe: Stub
// Recibe: QueryString la consulta a realizar
// Retorna: Arreglos de bytes con la respuesta de la consulta, Error si no se logra la consulta
func ConsultaPorString(stub shim.ChaincodeStubInterface, queryString string) ([]byte, error) {

	fmt.Printf("- getQueryResultForQueryString queryString:\n%s\n", queryString)

	resultsIterator, err := stub.GetQueryResult(queryString)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	buffer, err := construirRespuestaConsulta(resultsIterator)
	if err != nil {
		return nil, err
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return buffer.Bytes(), nil
}

// ConstruirRespuestaConsulta contruye un arreglo JSON que contiene los resultados de una consulta
// apartir de un ResultIterator
// Recibe: ResultIterator
// Retorna: Buffer de bytes, Error si el Iterator no esta bien formado
func construirRespuestaConsulta(resultsIterator shim.StateQueryIteratorInterface) (*bytes.Buffer, error) {
	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}

	return &buffer, nil
}

func InputSanation(args []string, numArgs int) pb.Response {
	if len(args) != numArgs {
		return shim.Bad("Información mal estructurada, verificar numero de argumentos.")
	}

	for i := 0; i < numArgs; i++ {
		if len(args[i]) <= 0 {
			return shim.Bad("Información mal estructurada, ningun campo de la solicitud puede ser vacia.")
		}
	}
	return shim.Success(nil)
}
