package main

import (
	"fmt"
	"math/rand"
	"time"
)

type Votes struct{ a, b int }

func station(out chan<- Votes) {
	for i := 0; i < 10; i++ {
		time.Sleep(time.Duration(rand.Intn(2000)) * time.Millisecond)
		aVotes := rand.Intn(100)
		out <- Votes{aVotes, 100 - aVotes}
	}
	close(out)
}

func collector(in1, in2 <-chan Votes, out chan<- Votes) {

}

func main() {
	rand.Seed(time.Now().UnixNano())
	var votes = make(chan Votes)

	go station(votes)

	var tally Votes
	for {
		var v, ok = <-votes
		if !ok {
			break
		}
		tally.a += v.a
		tally.b += v.b
		fmt.Println("Current tally:", tally)
	}

	tot := tally.a + tally.b

	if tot != 1000 {
		fmt.Println("Tally issue:", tot)
	}

	var winner string
	switch {
	case tally.a > tally.b:
		winner = "A"
	case tally.a < tally.b:
		winner = "B"
	default:
		winner = "undetermined"
	}
	fmt.Printf("All votes counted. And the winner is: %s\n", winner)
	if winner == "B" {
		fmt.Println("A: This must be FRAUD!!!")
	}
}
