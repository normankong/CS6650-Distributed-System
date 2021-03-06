/*
 * Ski Data API for NEU Seattle distributed systems course
 *
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * API version: 1.16
 * Generated by: Swagger Codegen (https://github.com/swagger-api/swagger-codegen.git)
 */
package swagger

import (
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

func AddSeason(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json; charset=UTF-8")
	params := mux.Vars(r)
	_, err := strconv.Atoi(params["resortID"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("invalid ID"))
	}
	w.WriteHeader(http.StatusCreated)
}

func GetResortSeasons(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json; charset=UTF-8")
	params := mux.Vars(r)
	_, err := strconv.Atoi(params["resortID"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("invalid ID"))
	}
	w.WriteHeader(http.StatusOK)
}

func GetResortSkiersDay(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json; charset=UTF-8")
	params := mux.Vars(r)
	_, err := strconv.Atoi(params["resortID"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("invalid ID"))
	}
	_, err = strconv.Atoi(params["seasonID"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("invalid ID"))
	}

	_, err = strconv.Atoi(params["dayID"])
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("invalid ID"))
	}
	w.WriteHeader(http.StatusOK)
}

func GetResorts(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json; charset=UTF-8")
	w.WriteHeader(http.StatusOK)
}
