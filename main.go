// Copyright 2018 Michael Morello. All Rights Reserved.
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"syscall"
)

func main() {

	var m []byte

	ready := 0
	liveness := -1
	cliveness := 0

	if readyVar := os.Getenv("READINESS_DELAY"); readyVar != "" {
		var err error
		ready, err = strconv.Atoi(readyVar)
		if err != nil {
			panic(err)
		}
	}

	if livenessVar := os.Getenv("LIVENESS_DELAY"); livenessVar != "" {
		var err error
		liveness, err = strconv.Atoi(livenessVar)
		if err != nil {
			panic(err)
		}
	}

	log.Println("Starting leak application...")

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Leak memory application, use /leak?size={bytes}\n")
	})

	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		if liveness < 0 {
			log.Printf("/health called - ok\n")
			w.WriteHeader(200)
		} else if liveness > 0 && cliveness < liveness {
			cliveness = cliveness + 1
			log.Printf("/health called - %d remaining \n", liveness-cliveness)
			w.WriteHeader(200)
		} else {
			log.Printf("/health called - fail")
			w.WriteHeader(503)
		}
	})

	http.HandleFunc("/ready", func(w http.ResponseWriter, r *http.Request) {
		if ready > 0 {
			ready = ready - 1
			log.Printf("/ready called - %d remaining \n", ready)
			w.WriteHeader(503)
		} else {
			log.Printf("/ready called\n")
			w.WriteHeader(200)
		}
	})

	http.HandleFunc("/leak", func(w http.ResponseWriter, r *http.Request) {
		size, ok := r.URL.Query()["size"]
		if !ok || len(size) < 1 {
			fmt.Fprintf(w, "Leak memory application, use /leak?size={bytes}\n")
			return
		}
		sizeAsInt, err := strconv.Atoi(size[0])
		if err != nil {
			fmt.Fprintf(w, "Url Param 'size' is missing %+v", err)
			return
		}
		if m == nil {
			m = make([]byte, sizeAsInt, sizeAsInt)
		} else {
			n := make([]byte, sizeAsInt, sizeAsInt)
			m = append(m, n...)
		}
		log := fmt.Sprintf("Slice len=%d cap=%d\n", len(m), cap(m))
		fmt.Fprintf(w, log)
	})

	s := http.Server{Addr: ":8888"}
	go func() {
		log.Fatal(s.ListenAndServe())
	}()

	signalChan := make(chan os.Signal, 1)
	signal.Notify(signalChan, syscall.SIGINT, syscall.SIGTERM)
	<-signalChan

	log.Println("Shutdown signal received, bye !")

	s.Shutdown(context.Background())
}
