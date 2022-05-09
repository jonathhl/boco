// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

Cypress.Commands.add("login", () => {
    cy.visit("/")

    cy.get("[data-cy=navbar-login-button]").click()
    cy.get("[data-cy=username]").type("john@doe.org")
    cy.get("[data-cy=password]").type("password")
    cy.intercept(
        {
            method: "POST",
            url: "http://localhost:8888/auth*",
        },
        (req) => {
            expect(req.body.username).to.include("john@doe.org")
            expect(req.body.password).to.include("password")

            req.reply({
                token: "testtest",
            })
        }
    ).as("getToken")
    cy.intercept(
        {
            method: "GET",
            url: "http://localhost:8888/user",
        },
        (req) => {
            expect(req.headers.authorization).to.equal("Bearer testtest")

            req.reply({
                //token: "testtest",
                email: "john@doe.org",
                firstname: "John",
                lastname: "Doe",
                phone: "+4700000000",
                role: "USER",
                zip: "0",
            })
        }
    ).as("getUserInfo")
    cy.get("[data-cy=submit]").click()
    cy.wait("@getToken")
    cy.wait("@getUserInfo")
})
