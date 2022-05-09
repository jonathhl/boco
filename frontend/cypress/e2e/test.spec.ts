import "@testing-library/cypress/add-commands"

it("adds todos", () => {
    cy.visit("https://todo.app.com")
    cy.get(".new-input").type("write code{enter}").type("write tests{enter}")
    // confirm the application is showing two items
    cy.get("li.todo").should("have.length", 2)
})
