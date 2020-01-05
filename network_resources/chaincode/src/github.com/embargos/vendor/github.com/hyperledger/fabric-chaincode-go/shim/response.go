// Copyright the Hyperledger Fabric contributors. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package shim

import (
	pb "github.com/hyperledger/fabric-protos-go/peer"
)

const (
	// OK constant - status code less than 400, endorser will endorse it.
	// OK means init or invoke successfully.
	OK = 200

	// BADREQUEST constant - status code greater than or equal to 400 will be considered an error and rejected by endorser.
	ERRORTHRESHOLD = 400

	// ERROR constant - default internal error value
	ERROR = 500

	// CONFLICT constant- The request could not be completed due to a conflict with the current state of the resource
	CONFLICT = 409
)

// Success ...
func Success(payload []byte) pb.Response {
	return pb.Response{
		Status:  OK,
		Payload: payload,
	}
}

// Bad request ...
func Bad(msg string) pb.Response {
	return pb.Response{
		Status:  ERRORTHRESHOLD,
		Message: "Solicitud malformada: " + msg,
	}
}

// Error ...
func Error(msg string) pb.Response {
	return pb.Response{
		Status:  ERROR,
		Message: "Error interno del servidor: " + msg,
	}
}

// Bad request ...
func Conflict(msg string) pb.Response {
	return pb.Response{
		Status:  CONFLICT,
		Message: "Conflicto: " + msg,
	}
}
