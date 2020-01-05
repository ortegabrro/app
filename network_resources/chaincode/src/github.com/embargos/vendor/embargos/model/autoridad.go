// Paquete model provee los modelos junto a las funciones que permiten interactuar
// con la blockchain
package model

import (
	"bytes"
	"embargos/utils"
	"encoding/json"
	"fmt"
	"strconv"
	"time"

	"github.com/hyperledger/fabric-chaincode-go/shim"
	pb "github.com/hyperledger/fabric-protos-go/peer"
)

// Reprensenta una autoridad en la blockchain, utilizada para la deserializacion y serializacion
// de un embargo en formato JSON
type Autoridad struct {
	IdAutoridad        string           `json:"idAutoridad"`
	TipoAutoridad      string           `json:"tipoAutoridad"`
	Nombre             string           `json:"nombre"`
	Direccion          string           `json:"direccion"`
	Ciudad             string           `json:"ciudad"`
	Departamento       string           `json:"departamento"`
	EmbargosRealizados []string         `json:"embargosRealizados"`
	Usuarios           []UsuarioSistema `json:"usuarios"`
	Habilitado         bool             `json:"habilitado"`
}

// RegistrarEmbargoEnAutoridad registra un embargo a una autoridad en la blockchain
// Recibe: Stub
// Recibe: autoridadJSONasBytes la autoridad representada en Json como bytes
// Recibe: idEmbargo el id del embargo a registrar
// Retorna: Error si no se aprobo la transaccion, Success si se registro el embargo en la autoridad
func (a *Autoridad) RegistrarEmbargoEnAutoridad(stub shim.ChaincodeStubInterface, autoridadJSONasBytes []byte, idEmbargo string) pb.Response {
	err := json.Unmarshal(autoridadJSONasBytes, &a)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso autoridad, intente de nuevo o comuniquese con soporte.")
	}
	a.EmbargosRealizados = append(a.EmbargosRealizados, idEmbargo)
	autoridadBytes, err := json.Marshal(a)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso autoridad, intente de nuevo o comuniquese con soporte.")
	}
	// === Guardar la autoridad ===
	err = stub.PutState(a.IdAutoridad, autoridadBytes)
	if err != nil {
		return shim.Error("Fallo al guardar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	return shim.Success(nil)
}

// CrearAutoridad crea una autoridad en la blockchain
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la autoridad a registrar en formato Json
// Retorna: Error si no se aprobo la transaccion, Success si se registro la autoridad
func (a *Autoridad) CrearAutoridad(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	// === Verificar  si existe la autoridad===
	if err := a.ExisteAutoridad(stub, a.IdAutoridad); err.Status != shim.OK {
		return err
	}

	// === Guardar la autoridad ===
	err := stub.PutState(a.IdAutoridad, []byte(args[0]))
	if err != nil {
		return shim.Error("Fallo al guardar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	fmt.Println("Autoridad creada")
	return shim.Success(nil)
}

// EditarAutoridad edita una autoridad en la blockchain
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la autoridad a registrar en formato Json
// Retorna: Error si no se aprobo la transaccion, Success si se registro la autoridad
func (a *Autoridad) EditarAutoridad(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	fmt.Println("Se recibio los siguientes parametros: " + args[0])
	// === Guardar la autoridad ===
	err := stub.PutState(a.IdAutoridad, []byte(args[0]))
	if err != nil {
		return shim.Error("Fallo al guardar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	fmt.Println("Autoridad editada")
	return shim.Success(nil)
}

// EliminarAutoridad elimina una autoridad del WORLD STATE
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo el id de la autoridad
// Retorna: El embargo consultado en bytes, Error si no se aprobo la transaccion
func (a *Autoridad) EliminarAutoridad(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	idAutoridad := args[0]

	// === Eliminar autoridad ===
	err := stub.DelState(idAutoridad)
	if err != nil {
		return shim.Error("Failed to delete state:" + err.Error())
	}
	return shim.Success(nil)
}

// ConsultarAutoridadPorCampo permite la consulta de un Autoridad por cualquier campo, consulta de tipo RichQuery
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la consulta a realizarse
// Retorna: El Autoridad consultado en bytes, Error si no se aprobo la transaccion
func (e *Autoridad) ConsultarAutoridadPorCampo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	queryString := args[0]

	queryResults, err := utils.ConsultaPorString(stub, queryString)
	if err != nil {
		return shim.Error("Fallo en realizar la consulta, intente de nuevo o comuniquese con soporte.")
	}
	return shim.Success(queryResults)
}

// ConsultarAutoridad permite la consulta de una autoridad por el campo "idAutoridad", consulta de tipo GetState
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la consulta a realizar en formato HyperledgerFabric
// Retorna: El embargo consultado en bytes, Error si no se aprobo la transaccion
func ConsultarAutoridad(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var idAutoridad string
	var err error

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	idAutoridad = args[0]
	valAsbytes, err := stub.GetState(idAutoridad)
	if err != nil {
		return shim.Error("Fallo al buscar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte.")
	} else if valAsbytes == nil {
		return shim.Success(nil)
	}
	return shim.Success(valAsbytes)
}

//Funcion que verifica si existe una autoridad
//Retorna ERROR si la autoridad NO existe
func (a *Autoridad) NoExisteAutoridad(stub shim.ChaincodeStubInterface, idAutoridad string) (pb.Response, []byte) {
	autoridadJSONasBytes, err := stub.GetState(idAutoridad)
	if err != nil {
		return shim.Error("Fallo al buscar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte."), nil
	} else if autoridadJSONasBytes == nil {
		fmt.Println("La autoridad no existe en la Blockchain")
		return shim.Conflict("Autoridad no existente en la blockchain, comuniquese con soporte para la creacion de nuevas autoridades."), nil
	}
	json.Unmarshal(autoridadJSONasBytes, &a)
	if !a.Habilitado {
		fmt.Println("La autoridad esta inhabilitada")
		return shim.Conflict("La autoridad esta inhabilitada en la blockchain, comuniquese con soporte para la habilitacion autoridades."), nil
	}
	return shim.Success(nil), autoridadJSONasBytes
}

//Funcion que verifica si existe una autoridad
//Retorna ERROR si la autoridad existe
func (a *Autoridad) ExisteAutoridad(stub shim.ChaincodeStubInterface, idAutoridad string) pb.Response {
	autoridadJSONasBytes, err := stub.GetState(idAutoridad)
	if err != nil {
		return shim.Error("Fallo al buscar la autoridad en la blockchain, intente de nuevo o comuniquese con soporte.")
	} else if autoridadJSONasBytes == nil {
		fmt.Println("La autoridad no existe")
		return shim.Success(nil)
	}
	return shim.Conflict("Autoridad ya existente en la blockchain, verificar informacion del embargo.")
}

func (e *Autoridad) HistorialAutoridad(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	idAutoridad := args[0]

	fmt.Printf("- start getHistory: %s\n", idAutoridad)

	resultsIterator, err := stub.GetHistoryForKey(idAutoridad)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing historic values for the marble
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		response, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"TxId\":")
		buffer.WriteString("\"")
		buffer.WriteString(response.TxId)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Value\":")
		// if it was a delete operation on given key, then we need to set the
		//corresponding value null. Else, we will write the response.Value
		//as-is (as the Value itself a JSON marble)
		if response.IsDelete {
			buffer.WriteString("null")
		} else {
			buffer.WriteString(string(response.Value))
		}

		buffer.WriteString(", \"Timestamp\":")
		buffer.WriteString("\"")
		buffer.WriteString(time.Unix(response.Timestamp.Seconds, int64(response.Timestamp.Nanos)).String())
		buffer.WriteString("\"")

		buffer.WriteString(", \"IsDelete\":")
		buffer.WriteString("\"")
		buffer.WriteString(strconv.FormatBool(response.IsDelete))
		buffer.WriteString("\"")

		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- getHistoryForMarble returning:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func ConsultarUsuarioEmbargosAutPag(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	autoridad := Autoridad{}

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 2); err.Status != shim.OK {
		return err
	}

	idAutoridad := args[0]
	paginacion, _ := strconv.Atoi(args[1])
	valAsbytes, _ := stub.GetState(idAutoridad)
	err := json.Unmarshal(valAsbytes, &autoridad)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso, intente de nuevo o comuniquese con soporte.")
	}
	var idembargos []string = autoridad.EmbargosRealizados[0:paginacion]
	var buffer bytes.Buffer
	for index := 0; index < paginacion; index++ {
		valAsbytes, err := stub.GetState(idembargos[index])
		if err != nil {
			return shim.Error("Fallo al buscar el embargo en la blockchain, intente de nuevo o comuniquese con soporte.")
		}
		buffer.WriteString(string(valAsbytes))
	}
	return shim.Success(buffer.Bytes())
}
