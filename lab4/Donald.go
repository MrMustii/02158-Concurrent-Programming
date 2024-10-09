package main

import "fmt"
import "sync"

var wg sync.WaitGroup

func p(name string) {
    defer wg.Done()

    for i := 0; i < 10; i++ {
       fmt.Printf("I am %s\n", name)
    }
}

func main() {
    wg.Add(3);

    go p("Huey")
    go p("Dewey")
    go p("Louie")

    wg.Wait()
}