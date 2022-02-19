/*
 * Ski Data API for NEU Seattle distributed systems course
 *
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * API version: 1.16
 * Generated by: Swagger Codegen (https://github.com/swagger-api/swagger-codegen.git)
 */
package swagger

type ResortsListResorts struct {

	ResortName string `json:"resortName,omitempty"`

	ResortID int32 `json:"resortID,omitempty"`
}