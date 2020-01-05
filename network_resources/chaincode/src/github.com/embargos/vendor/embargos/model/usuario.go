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

// Reprensenta un usuario en la blockchain, utilizada para la deserializacion y serializacion
// de un usuario en formato JSON
type Usuario struct {
	Persona
	EmbargosDemandante []string `json:"embargosDemandante"`
	EmbargosDemandado  []string `json:"embargosDemandado"`
}

// RegistrarUsuarioConEmbargo registra un usuario por primera vez junto a un embargo inicial
// Recibe: Stub
// Retorna: Error si no se aprobo la transaccion, Success si se registro el usuario
func (u *Usuario) registrarUsuarioConEmbargo(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("Registrando usuario")
	usuarioBytes, err := json.Marshal(u)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso usuario, intente de nuevo o comuniquese con soporte.")
	}
	key, err := stub.CreateCompositeKey("idPersona", []string{u.Identificacion, u.TipoIdentificacion})
	if err != nil {
		return shim.Error("Fallo en la creación de la llave compuesta de usuario, intente de nuevo o comuniquese con soporte.")
	}
	err = stub.PutState(key, usuarioBytes)
	if err != nil {
		return shim.Error("Fallo al guardar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	return shim.Success(nil)
}

// RegistrarUsuarioConEmbargo registra un usuario por primera vez junto a un embargo inicial
// Recibe: Stub
// Retorna: Error si no se aprobo la transaccion, Success si se registro el usuario
func (u *Usuario) CrearUsuario(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Registrando usuario")

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	// === Verificar  si existe el embargo===
	if err := ExisteUsuario(stub, u.Identificacion); err.Status == shim.OK {
		return err
	}

	key, err := stub.CreateCompositeKey("idPersona", []string{u.Identificacion, u.TipoIdentificacion})
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, []byte(args[0]))
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

// RegistrarUsuarioConEmbargo registra un usuario por primera vez junto a un embargo inicial
// Recibe: Stub
// Retorna: Error si no se aprobo la transaccion, Success si se registro el usuario
func (u *Usuario) EditarUsuario(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Editando usuario")

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	key, err := stub.CreateCompositeKey("idPersona", []string{u.Identificacion, u.TipoIdentificacion})
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, []byte(args[0]))
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

// EliminarEmbargo elimina un embargo del WORLD STATE
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo el id del embargo
// Retorna: El embargo consultado en bytes, Error si no se aprobo la transaccion
func (e *Usuario) EliminarUsuario(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 2); err.Status != shim.OK {
		return err
	}

	identificacion := args[0]
	tipoIdentificacion := args[1]

	key, err := stub.CreateCompositeKey("idPersona", []string{identificacion, tipoIdentificacion})
	if err != nil {
		return shim.Error(err.Error())
	}

	// === Eliminar Usuario ===
	err = stub.DelState(key)
	if err != nil {
		return shim.Error("Failed to delete state:" + err.Error())
	}
	return shim.Success(nil)
}

// ConsultarEmbargoPorCampo permite la consulta de un embargo por cualquier campo, consulta de tipo RichQuery
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la consulta a realizarse
// Retorna: El embargo consultado en bytes, Error si no se aprobo la transaccion
func (u *Usuario) ConsultarUsuarioPorCampo(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 1); err.Status != shim.OK {
		return err
	}

	queryString := args[0]

	queryResults, err := utils.ConsultaPorString(stub, queryString)
	if err != nil {
		return shim.Error("Fallo al buscar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	return shim.Success(queryResults)
}

func (u *Usuario) HistorialUsuario(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 2); err.Status != shim.OK {
		return err
	}

	identificacion := args[0]
	tipoIdentificacion := args[1]

	key, err := stub.CreateCompositeKey("idPersona", []string{identificacion, tipoIdentificacion})
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("- start getHistory: %s\n", key)

	resultsIterator, err := stub.GetHistoryForKey(key)
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

func ExisteUsuario(stub shim.ChaincodeStubInterface, idUsuario string) pb.Response {
	usuarioAsBytes, err := stub.GetState(idUsuario)
	if err != nil {
		return shim.Error("Failed to get marble: " + err.Error())
	} else if usuarioAsBytes == nil {
		fmt.Println("This user does not exists: " + idUsuario)
		return shim.Error("This user does not exists: " + idUsuario)
	}
	return shim.Success(nil)
}

// AgregarEmbargoEnUsuario agrega un embargo a un usuario existente
// Recibe: Stub
// Recibe: usuarioJSONBytes el usuario representado en Json como bytes
// Recibe: idEmbargo el id del embargo a registrar
// Retorna: Error si no se aprobo la transaccion, Success si se registro el embargo en el usuario
func (u *Usuario) agregarEmbargoEnUsuario(stub shim.ChaincodeStubInterface, usuarioJSONBytes []byte, idEmbargo string, demandante bool) pb.Response {
	err := json.Unmarshal(usuarioJSONBytes, &u)
	if err != nil {
		return shim.Error("Fallo en la codificación del usuario, intente de nuevo o comuniquese con soporte.")
	}
	if demandante {
		u.EmbargosDemandante = append(u.EmbargosDemandante, idEmbargo)
	} else {
		u.EmbargosDemandado = append(u.EmbargosDemandado, idEmbargo)
	}
	usuarioBytes, err := json.Marshal(u)
	if err != nil {
		return shim.Error("Fallo en la codificación del usuario, intente de nuevo o comuniquese con soporte.")
	}
	key, err := stub.CreateCompositeKey("idPersona", []string{u.Identificacion, u.TipoIdentificacion})
	if err != nil {
		return shim.Error("Fallo en la creación de la llave compuesta de usuario, intente de nuevo o comuniquese con soporte.")
	}
	err = stub.PutState(key, usuarioBytes)
	if err != nil {
		return shim.Error("Fallo al agregar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	return shim.Success(nil)
}

// ConsultarUsuario permite la consulta de un usuario por el campo "identificacion" y "tipoidentificacion", consulta de tipo GetState
// Recibe: Stub
// Recibe: Args especificando en la posicion 0 del arreglo la identificacion y en la posicion 1 el tipo de identificacion
// Retorna: La persona consultada en bytes, Error si no se aprobo la transaccion
func (u *Usuario) ConsultarUsuario(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 2); err.Status != shim.OK {
		return err
	}

	identificacion := args[0]
	tipoIdentificacion := args[1]
	key, err := stub.CreateCompositeKey("idPersona", []string{identificacion, tipoIdentificacion})
	if err != nil {
		return shim.Error("Fallo en la creación de la llave compuesta de usuario, intente de nuevo o comuniquese con soporte.")
	}
	valAsbytes, err := stub.GetState(key)
	if err != nil {
		return shim.Error("Fallo al buscar el usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	} else if valAsbytes == nil {
		return shim.Success(nil)
	}
	return shim.Success(valAsbytes)
}

func (u *Usuario) ConsultarUsuarioEmbargosDteTotal(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error
	usuario := Usuario{}

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 2); err.Status != shim.OK {
		return err
	}

	identificacion := args[0]
	tipoIdentificacion := args[1]
	key, err := stub.CreateCompositeKey("idPersona", []string{identificacion, tipoIdentificacion})
	if err != nil {
		return shim.Error("Fallo en la creación de la llave compuesta, intente de nuevo o comuniquese con soporte.")
	}
	valAsbytes, err := stub.GetState(key)
	if err != nil {
		return shim.Error("Fallo al buscar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	err = json.Unmarshal(valAsbytes, &usuario)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso, intente de nuevo o comuniquese con soporte.")
	}
	var total int = len(usuario.EmbargosDemandante)
	return shim.Success([]byte(strconv.Itoa(total)))
}

func (u *Usuario) ConsultarUsuarioEmbargosDtePag(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var err error
	usuario := Usuario{}

	fmt.Println("Se recibio los siguientes parametros: " + args[0])

	// === Verificar argumentos ===
	if err := utils.InputSanation(args, 3); err.Status != shim.OK {
		return err
	}

	identificacion := args[0]
	tipoIdentificacion := args[1]
	paginacion, _ := strconv.Atoi(args[2])
	key, err := stub.CreateCompositeKey("idPersona", []string{identificacion, tipoIdentificacion})
	if err != nil {
		return shim.Error("Fallo al buscar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	valAsbytes, err := stub.GetState(key)
	if err != nil {
		return shim.Error("Fallo al buscar usuario en la blockchain, intente de nuevo o comuniquese con soporte.")
	}
	err = json.Unmarshal(valAsbytes, &usuario)
	if err != nil {
		return shim.Error("Fallo en la codificación del recurso, intente de nuevo o comuniquese con soporte.")
	}
	if len(usuario.EmbargosDemandante) < paginacion {
		paginacion = len(usuario.EmbargosDemandante)
	}
	var idembargos []string = usuario.EmbargosDemandante[0:paginacion]
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
