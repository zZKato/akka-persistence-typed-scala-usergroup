# akka-persistence-typed-scala-usergroup

This repo contains demo code for the talk about the akka-persistence-typed library

## What is contained

Basic akka-http web service with akka-http-typed event sourced persistence
This example is not runnable as there is a bunch of configuration missing and no persistence backend setup but it serves to show the basics of akka-persistence-typed

## Domain

As an example we take a simple bank account entity

BankAccount {
    accountNumber: UUID,
    balance: double,
    type: int
}

This will enable us to:
- create and delete an account
- Add and subtract cash